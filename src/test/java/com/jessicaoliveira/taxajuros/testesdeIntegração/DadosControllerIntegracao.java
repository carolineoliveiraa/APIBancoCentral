package com.jessicaoliveira.taxajuros.testesdeIntegração;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jessicaoliveira.taxajuros.DTO.DadosObrigatoriosDTO;
import com.jessicaoliveira.taxajuros.controller.DadosController;
import com.jessicaoliveira.taxajuros.model.DadosModel;
import com.jessicaoliveira.taxajuros.repository.DadosRepository;
import com.jessicaoliveira.taxajuros.response.ErrorResponse;
import com.jessicaoliveira.taxajuros.service.DadosService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DadosControllerIntegracao {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DadosService dadosService;

    @Autowired
    private DadosRepository dadosRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testBuscarPorId() throws Exception {
        // Cria um objeto DadosModel para ser buscado
        DadosModel dados = new DadosModel();
        dados.setInstituicaoFinanceira("BCO DO BRASIL SA");
        dados.setTaxaJurosAoMes(0.00);
        dadosRepository.save(dados);

        // Faz a requisição GET para buscar o objeto criado acima
        mockMvc.perform(get("/api/buscar/1" + dados.getId()));
    }

    @Test
    public void testBuscarPorIdNotFound() throws Exception {
        mockMvc.perform(get("/api/buscar/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testListarTodos() throws Exception {
        // Cria dois objetos DadosModel para serem listados
        DadosModel dados1 = new DadosModel();
        dados1.setInstituicaoFinanceira("BCO DO BRASIL SA");
        dados1.setTaxaJurosAoMes(0.00);
        dadosRepository.save(dados1);

        DadosModel dados2 = new DadosModel();
        dados2.setInstituicaoFinanceira("BCO RODOBENS SA");
        dados2.setTaxaJurosAoMes(0.00);
        dadosRepository.save(dados2);

        // Faz a requisição GET para listar os objetos criados acima
        mockMvc.perform(get("/api/consultarTodos"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andReturn();
    }

    @Test
    public void testListarTodosNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/listar")).andReturn();
        int status = result.getResponse().getStatus();
    }

    @Test
    public void testCriar() throws Exception {
        // Cria um objeto DadosObrigatoriosDTO para ser criado
        DadosObrigatoriosDTO dadosDTO = new DadosObrigatoriosDTO();
        dadosDTO.setInstituicaoFinanceira("BCO DO BRASIL SA");
        dadosDTO.setTaxaJurosAoMes(0.00);

        // Faz a requisição POST para criar o objeto acima
        mockMvc.perform(post("/api/salvar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.InstituicaoFinanceira").value("BCO DO BRASIL SA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.TaxaJurosAoMes").value(0.00));
    }


    @Test
    public void testAtualizarDados() {
        // Cria um novo objeto DadosModel para atualizar
        DadosModel dados = new DadosModel();
        dados.setAnoMes("2023-01");

        // Insere o objeto na base de dados para poder atualizá-lo
        dados = dadosRepository.save(dados);

        // Cria uma URI absoluta para o endpoint de atualização
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/api/atualizar/{id}")
                .buildAndExpand(dados.getId())
                .toUri();

        // Faz uma requisição PUT para atualizar o objeto com o novo nome
        ResponseEntity<DadosModel> response = restTemplate.exchange(
                uri,
                HttpMethod.PUT,
                new HttpEntity<>(dados),
                DadosModel.class
        );

        // Verifica se a resposta é 200 (OK) e se o objeto foi atualizado corretamente
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("2023-01", response.getBody().getAnoMes());
    }

    @Test
    public void testDeletarDados() {
        // Cria um novo objeto DadosModel para deletar
        DadosModel dados = new DadosModel();

        // Insere o objeto na base de dados para poder deletá-lo
        dados = dadosRepository.save(dados);

        // Define a URL base
        String baseUrl = "http://localhost:8080" ;

        // Define o caminho do endpoint para deletar um objeto pelo ID
        String deleteUrl = baseUrl + "/api/apague/{id}";

        // Cria uma variável com os parâmetros para a requisição
        Map<String, Long> params = new HashMap<>();
        params.put("id", dados.getId());

        // Faz uma requisição DELETE para deletar o objeto
        ResponseEntity<Void> response = restTemplate.exchange(
                deleteUrl,
                HttpMethod.DELETE,
                null,
                Void.class,
                params
        );

        // Verifica se a resposta é 204 (NO CONTENT) e se o objeto foi deletado corretamente
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(dadosRepository.findById(dados.getId()).isPresent());
    }

    @Test
    public void testListarComPaginacao() {
        // Cria um novo objeto DadosModel para listar
        DadosModel dados = new DadosModel();

        // Insere o objeto na base de dados para poder lista-lo
        dados = dadosRepository.save(dados);

        // Define a URL base
        String baseUrl = "http://localhost:8080" ;

        // Define o caminho do endpoint para listar por paginaçao
        String paginadoUrl = baseUrl + "/api/paginado";


        // Faz uma requisição GET para listar o objeto
        ResponseEntity<Void> response = restTemplate.exchange(
                paginadoUrl,
                HttpMethod.GET,
                null,
                Void.class
        );

    }

    @Test
    public void testListarPorAnoMes() {
        String anoMes = "2022-11";
        String baseUrl = "http://localhost:8080";
        String endpointUrl = "/api/ano-mes/" + anoMes;

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + endpointUrl, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testListarPorInstituicaoComResultados() {
        String InstituicaoFinanceira = "CAIXA ECONOMICA FEDERAL";
        DadosModel dado1 = new DadosModel(1L, "CAIXA ECONOMICA FEDERAL", InstituicaoFinanceira, 0.00);
        DadosModel dado2 = new DadosModel(2L, "CAIXA ECONOMICA FEDERAL", InstituicaoFinanceira, 0.00);
        List<DadosModel> dados = Arrays.asList(dado1, dado2);

        // Cria o mock da classe DadosService
        DadosService dadosServiceMock = mock(DadosService.class);

        // Configura o mock para retornar os dados esperados
        when(dadosServiceMock.listarPorInstituicaoFinanceira(InstituicaoFinanceira)).thenReturn(dados);

        // Realiza a chamada ao endpoint com o RestTemplate
        ResponseEntity<List> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/instituicao?InstituicaoFinanceira=" + InstituicaoFinanceira, List.class);

        // Verifica o status da resposta e o tamanho da lista retornada
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(16, responseEntity.getBody().size());
    }


    @Test
    void testListarPorInstituicaoSemResultados() {
        String InstituicaoFinanceira = "CAIXA ECONOMICA FEDERAL";
        DadosService dadosService = Mockito.mock(DadosService.class);
        when(dadosService.listarPorInstituicaoFinanceira(InstituicaoFinanceira)).thenReturn(new ArrayList<>());

        // injeta o dadosService no controller
        DadosController dadosController = new DadosController(dadosService);

        // cria o restTemplate com o restTemplateBuilder
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8080")
                .build();

        ResponseEntity<?> responseEntity = restTemplate.getForEntity("/api/instituicao?InstituicaoFinanceira=" + InstituicaoFinanceira, Object.class);

        if (responseEntity.getBody() instanceof List) {
            List<?> responseList = (List<?>) responseEntity.getBody();
            assertEquals(16, responseList.size());
        } else {
            ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
            assertEquals("Nenhuma instituição financeira encontrada com o nome informado.", errorResponse.getMessage());
        }
    }
}

