package com.jessicaoliveira.taxajuros.testesUnitarios;

import com.jessicaoliveira.taxajuros.controller.DadosController;
import com.jessicaoliveira.taxajuros.model.DadosModel;
import com.jessicaoliveira.taxajuros.model.ResponseDadosModel;
import com.jessicaoliveira.taxajuros.response.ErrorResponse;
import com.jessicaoliveira.taxajuros.service.DadosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class DadosControllerTest {
    @Mock
    DadosService dadosService;

    @InjectMocks
    DadosController dadosController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorIdExistente() {
        Long id = 1L;
        DadosModel dadosModel = new DadosModel(id, "Jan-2023", "FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PÓS-FIXADO REFERENCIADO EM IPCA", 1, "BCO DO BRASIL SA", 0.00, 0.00, 00000000, "2023-01");
        when(dadosService.buscarPorId(id)).thenReturn(Optional.of(dadosModel));
        ResponseEntity<?> responseEntity = dadosController.buscarPorId(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testBuscarPorIdNaoExistente() {
        Long id = 1L;
        when(dadosService.buscarPorId(id)).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = dadosController.buscarPorId(id);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testListarTodos() {
        List<DadosModel> dadosList = new ArrayList<>();
        dadosList.add(new DadosModel(4L, "Jan-2023", "FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PÓS-FIXADO REFERENCIADO EM IPCA", 1, "APE POUPEX", 0.38,  4.69, 655522, "2023-01"));
        dadosList.add(new DadosModel(5L, "Jan-2023", "FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PRÉ-FIXADO", 3, "BCO SANTANDER (BRASIL) S.A.", 0.93, 11.81, 90400888, "2023-01"));
        when(dadosService.listarTodos()).thenReturn(dadosList);
        ResponseEntity<?> responseEntity = dadosController.listarTodos();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testListarTodosSemDados() {
        when(dadosService.listarTodos()).thenReturn(new ArrayList<>());
        ResponseEntity<ResponseDadosModel> responseEntity = dadosController.listarTodos();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().getDados().size());
    }


    @Test
    public void testAtualizarDadosExistente() {
        // DadosModel e ResponseEntity
        DadosModel dados = new DadosModel();
        ResponseEntity<DadosModel> responseEntity = ResponseEntity.ok(dados);

        // Configuração do mock do DadosService
        Long id = 1L;
        DadosModel dadosAtualizados = new DadosModel();
        when(dadosService.buscarPorId(id)).thenReturn(Optional.of(dados));
        when(dadosService.atualizar(id, dados)).thenReturn(dadosAtualizados);

        // Executa o método da classe a ser testada
        ResponseEntity<?> resultado = dadosController.atualizar(id, dados);

        // Verifica se o resultado foi o esperado
        assertEquals(responseEntity.getStatusCode(), resultado.getStatusCode());
    }

    @Test
    public void testAtualizarDadosInexistente() {
        // ErrorResponse e ResponseEntity
        ErrorResponse error = new ErrorResponse("Dados não encontrados para o ID informado.");
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        // Configuração do mock do DadosService
        Long id = 1L;
        when(dadosService.buscarPorId(id)).thenReturn(Optional.empty());

        // Executa o método da classe a ser testada
        ResponseEntity<?> resultado = dadosController.atualizar(id, new DadosModel());

        // Verifica se o resultado foi o esperado
        assertEquals(responseEntity.getStatusCode(), resultado.getStatusCode());
    }

    @Test
    public void testDeletarDadosExistente() {
        // ResponseEntity
        ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();

        // Configuração do mock do DadosService
        Long id = 1L;
        when(dadosService.buscarPorId(id)).thenReturn(Optional.of(new DadosModel()));

        // Executa o método da classe a ser testada
        ResponseEntity<?> resultado = dadosController.deletar(id);

        // Verifica se o resultado foi o esperado
        assertEquals(responseEntity.getStatusCode(), resultado.getStatusCode());
        assertNull(resultado.getBody());
    }

    @Test
    public void testDeletarDadosInexistente() {
        // ErrorResponse e ResponseEntity
        ErrorResponse error = new ErrorResponse("Dados não encontrados para o id: 1");
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

        // Configuração do mock do DadosService
        Long id = 1L;
        when(dadosService.buscarPorId(id)).thenReturn(Optional.empty());

        // Executa o método da classe a ser testada
        ResponseEntity<?> resultado = dadosController.deletar(id);

        // Verifica se o resultado foi o esperado
        assertEquals(responseEntity.getStatusCode(), resultado.getStatusCode());
    }


    @Test
    public void testBuscarTaxaPorIdExistente() {
        Long id = 1L;
        Double taxaJuros = 1.5;
        DadosModel dados = new DadosModel();
        dados.setId(id);
        dados.setTaxaJurosAoMes(taxaJuros);
        Optional<DadosModel> optionalDados = Optional.of(dados);
        when(dadosService.buscarPorId(id)).thenReturn(optionalDados);

        ResponseEntity<?> response = dadosController.buscarTaxaPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxaJuros, response.getBody());
    }



    @Test
    public void testBuscarTaxaPorIdInexistente() {
        Long id = 1L;
        Optional<DadosModel> optionalDados = Optional.empty();
        when(dadosService.buscarPorId(id)).thenReturn(optionalDados);

        ResponseEntity<?> response = dadosController.buscarTaxaPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("Dados não encontrados para o id: " + id, errorResponse.getMessage());
    }



    @Test
    public void testListarComPaginacao() {
        Integer pagina = 0;
        Integer tamanho = 10;

        // criação do objeto simulado (mock) do serviço dadosService
        DadosService dadosService = Mockito.mock(DadosService.class);

        Page<DadosModel> page = new PageImpl<>(new ArrayList<>());
        when(dadosService.listarComPaginacao(pagina, tamanho)).thenReturn(page);

        // criação do objeto controlador a ser testado, injetando o mock criado anteriormente
        DadosController dadosController = new DadosController(dadosService);

        ResponseEntity<Page<DadosModel>> responseEntity = dadosController.listarComPaginacao(pagina, tamanho);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(page, responseEntity.getBody());

        // verificação se o método listarComPaginacao foi chamado uma vez com os argumentos corretos
        verify(dadosService, times(1)).listarComPaginacao(pagina, tamanho);
    }

    @Test
    public void testListarComPaginacaoBadRequest() {
        Integer pagina = 1;
        Integer tamanho = 10;
        when(dadosService.listarComPaginacao(pagina, tamanho)).thenThrow(new IllegalArgumentException());
        ResponseEntity<Page<DadosModel>> responseEntity = dadosController.listarComPaginacao(pagina, tamanho);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testListarComPaginacaoInternalServerError() {
        // Configuração do mock para lançar uma exceção
        Integer pagina = 0;
        Integer tamanho = 10;
        when(dadosService.listarComPaginacao(pagina, tamanho)).thenThrow(new RuntimeException());

        // Chamada do método do controller e captura da exceção lançada
        ResponseEntity<Page<DadosModel>> responseEntity = null;
        try {
            responseEntity = dadosController.listarComPaginacao(pagina, tamanho);
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
            // Verificação se a exceção capturada é do tipo esperado
            assertEquals(RuntimeException.class, e.getClass());
            // Verificação do status code da resposta HTTP
        }
    }

    @Test
    void testListarPorInstituicao() {
        // Criação de um objeto de DadosModel para testar
        DadosModel dados = new DadosModel();
        dados.setInstituicaoFinanceira("CAIXA ECONOMICA FEDERAL");
        dados.setTaxaJurosAoMes(0.00);

        // Criação de uma lista com o objeto criado acima
        List<DadosModel> dadosList = new ArrayList<>();
        dadosList.add(dados);

        // Mock do serviço de dados para retornar a lista criada acima
        when(dadosService.listarPorInstituicaoFinanceira("CAIXA ECONOMICA FEDERAL")).thenReturn(dadosList);

        // Execução do endpoint com o parâmetro "Banco A"
        ResponseEntity responseEntity = dadosController.listarPorInstituicao("CAIXA ECONOMICA FEDERAL");

        // Verifica se o status code da resposta é OK
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verifica se o corpo da resposta contém a lista criada acima
        List<DadosModel> responseBody = (List<DadosModel>) responseEntity.getBody();
        assertEquals(dadosList, responseBody);

        // Execução do endpoint com um parâmetro que não corresponde a nenhum dado
        when(dadosService.listarPorInstituicaoFinanceira("Banco B")).thenReturn(new ArrayList<>());

        // Verifica se o status code da resposta é NOT_FOUND
        responseEntity = dadosController.listarPorInstituicao("Banco B");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Verifica se o corpo da resposta contém uma mensagem de erro
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("Nenhuma instituição financeira encontrada com o nome informado.", errorResponse.getMessage());
    }

    @Test
    public void testListarPorAnoMes() throws Exception {
        // criação do objeto de teste
        String anoMes = "2023-01";

        // criação do mock do ResponseEntity
        ResponseEntity<?> responseEntity = mock(ResponseEntity.class);

        // mock do método "listarPorAnoMes"
        List<DadosModel> dados = new ArrayList<>();
        when(dadosService.listarPorAnoMes(anoMes)).thenReturn(dados);

        // chama o método de teste
        ResponseEntity<?> result = dadosController.listarPorAnoMes(anoMes);

        // verificações
        verify(dadosService).listarPorAnoMes(anoMes);
        if (anoMes.matches("^\\d{4}-\\d{2}$")) {
            verify(responseEntity).ok(dados);
        } else {
            verify(responseEntity).badRequest()
                    .body("Formato de ano-mês inválido. O formato correto é: YYYY-MM, tente como o exemplo:2023-01.");
        }
    }

}

