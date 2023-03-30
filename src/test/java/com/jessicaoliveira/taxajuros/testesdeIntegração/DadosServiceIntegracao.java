package com.jessicaoliveira.taxajuros.testesdeIntegração;

import com.jessicaoliveira.taxajuros.model.DadosModel;
import com.jessicaoliveira.taxajuros.repository.DadosRepository;
import com.jessicaoliveira.taxajuros.service.DadosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DadosServiceIntegracao {
    @Autowired
    private DadosService dadosService;

    @Autowired
    private DadosRepository dadosRepository;

    @Test
    public void listarDadosModelTest() {
        List<DadosModel> dados = dadosService.listarDadosModel();
        assertNotNull(dados);
        assertFalse(dados.isEmpty());
    }


    @BeforeEach
    void setUp() {
        // Limpa os dados do banco de dados antes de cada teste
        dadosRepository.deleteAll();

        // Insere alguns dados iniciais para testes
        DadosModel dadosModel = new DadosModel();
        dadosModel.setAnoMes("2023-01");
        dadosRepository.save(dadosModel);
    }


    @Test
    void listarTodosDeveRetornarDadosDoBancoSeExistir() {
        // Dado que existem dados no banco
        DadosModel dadosModel = new DadosModel();
        dadosModel.setAnoMes("2023-01");
        dadosRepository.save(dadosModel);

        // Quando eu chamo o método listarTodos
        List<DadosModel> dados = dadosService.listarTodos();

        // Então o resultado deve ser os dados salvos no banco
        assertThat(dados).hasSize(2);
        assertThat(dados.get(0).getAnoMes()).isEqualTo("2023-01");
    }

    @Test
    void listarTodosDeveRetornarDadosDaAPISeBancoEstiverVazio() {
        // Dado que o banco está vazio

        // Quando eu chamo o método listarTodos
        List<DadosModel> dados = dadosService.listarTodos();

        // Então o resultado deve ser os dados da API
        assertThat(dados).isNotEmpty();
    }

    @Test
    void criarDeveSalvarDadosNoBanco() {
        // Dado que tenho um objeto DadosModel
        DadosModel dadosModel = new DadosModel();
        dadosModel.setAnoMes("2023-01");

        // Quando eu chamo o método criar
        DadosModel resultado = dadosService.criar(dadosModel);

        // Então o objeto deve ser salvo no banco e ter um ID atribuído
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getAnoMes()).isEqualTo("2023-01");
    }

    @Test
    void atualizarDeveAtualizarDadosNoBanco() {
        // Dado que tenho um objeto DadosModel salvo no banco
        DadosModel dadosModel = new DadosModel();
        dadosModel.setAnoMes("2023-02");
        dadosRepository.save(dadosModel);

        // E tenho outro objeto DadosModel com dados atualizados
        DadosModel dadosAtualizados = new DadosModel();
        dadosAtualizados.setAnoMes("2023-02");

        // Quando eu chamo o método atualizar
        DadosModel resultado = dadosService.atualizar(dadosModel.getId(), dadosAtualizados);

        // Então o objeto deve ser atualizado no banco com os novos dados
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(dadosModel.getId());
        assertThat(resultado.getAnoMes()).isEqualTo("2023-02");
    }

    @Test
    public void testDeletar() {
        // cria um objeto DadosModel e salva no banco de dados
        DadosModel dados = new DadosModel();
        dadosRepository.save(dados);

        // chama o método deletar() do service com o id do objeto salvo
        dadosService.deletar(dados.getId());

        // verifica se o objeto foi deletado do banco de dados
        assertFalse(dadosRepository.findById(dados.getId()).isPresent());
    }

    @Test
    public void testListarComPaginacao() {
        // cria alguns objetos DadosModel e salva no banco de dados
        DadosModel dados1 = new DadosModel();
        DadosModel dados2 = new DadosModel();
        DadosModel dados3 = new DadosModel();
        dadosRepository.saveAll(Arrays.asList(dados1, dados2, dados3));

        // chama o método listarComPaginacao() do service com os parâmetros de paginação
        Page<DadosModel> dadosPage = dadosService.listarComPaginacao(0, 10);

        // verifica se a página retornada contém os objetos salvos
        assertEquals(4, dadosPage.getNumberOfElements());
    }

    @Test
    public void testListarPorAnoMes() {
        // cria alguns objetos DadosModel com diferentes valores para o campo "anoMes" e salva no banco de dados
        DadosModel dados1 = new DadosModel("2022-01", "Teste", "Teste", "Teste", "00000001", "Teste", 0.5, 6.0);
        DadosModel dados2 = new DadosModel("2023-02", "Teste", "Teste", "Teste", "00000001", "Teste", 0.5, 6.0);
        DadosModel dados3 = new DadosModel("2021-03", "Teste", "Teste", "Teste", "00000001", "Teste", 0.5, 6.0);
        dadosRepository.saveAll(Arrays.asList(dados1, dados2, dados3));

        // chama o método listarPorAnoMes() do service com um valor de "anoMes"
        List<DadosModel> dadosList = dadosService.listarPorAnoMes("2023-01");

        // verifica se a lista retornada contém apenas os objetos com o valor de "anoMes" buscado
        assertEquals(1, dadosList.size());
    }

    @Test
    public void testBuscarPorId() {
        // cria um objeto DadosModel e salva no banco de dados
        DadosModel dados = new DadosModel();
        dadosRepository.save(dados);

        // chama o método buscarPorId() do service com o id do objeto salvo
        Optional<DadosModel> dadosOptional = dadosService.buscarPorId(dados.getId());

        // verifica se o objeto retornado é igual ao objeto salvo
        assertTrue(dadosOptional.isPresent());
        assertEquals(dados, dadosOptional.get());
    }

    @Test
    public void testListarPorInstituicaoFinanceira() {
        // cria alguns objetos DadosModel com diferentes valores para o campo "instituicaoFinanceira" e salva no banco de dados
        DadosModel dados1 = new DadosModel();
        DadosModel dados2 = new DadosModel();
        DadosModel dados3 = new DadosModel();
        dados1.setInstituicaoFinanceira("BCO DO BRASIL SA");
        dados2.setInstituicaoFinanceira("BCO RODOBENS SA");
        dados3.setInstituicaoFinanceira("CAIXA ECONOMICA FEDERAL");
        dadosRepository.saveAll(Arrays.asList(dados1, dados2, dados3));

        // chama o método listarPorInstituicaoFinanceira() do service com um valor de "instituicaoFinanceira"
        List<DadosModel> dadosList = dadosService.listarPorInstituicaoFinanceira("BCO RODOBENS SA");

        // verifica se a lista retornada contém apenas os objetos com o valor de "instituicaoFinanceira" buscado
        assertEquals(1, dadosList.size());
    }

    @Test
    public void salvarDadosModelTest() {
        // cria um objeto de exemplo para ser salvo
        DadosModel dados = new DadosModel("Jan-2023", "FINANCIAMENTO IMOBILIÁRIO COM TAXAS REGULADAS - PRÉ-FIXADO",
                "1", "BCO DO BRASIL S.A", "00000000",
                "null", 0.00, 0.00);

        DadosModel resultado = dadosService.salvarDadosModel(dados);

        // verifica se o objeto foi salvo com sucesso
        if (resultado != null) {
            assertNotNull(resultado.getId());
            assertNull(resultado.getAnoMes());
            assertNull(resultado.getMes());
            assertNull(resultado.getModalidade());
            assertNull(resultado.getPosicao());
            assertNull(resultado.getCnpj8());
            assertNull(resultado.getInstituicaoFinanceira());
            assertNull( resultado.getTaxaJurosAoMes());
            assertNull(resultado.getTaxaJurosAoAno());
        } else {
            fail("O objeto resultado é nulo");
        }
    }

    @Test
    public void deletarDadosModelTest() {
        // cria um objeto de exemplo para ser salvo e depois deletado
        DadosModel dados = new DadosModel("202201", "Teste", "Teste", "Teste", "00000001", "Teste", 0.5, 6.0);
        dadosRepository.save(dados);

        // deleta o objeto usando o serviço
        dadosService.deletarDadosModel(dados.getId());

        // verifica se o objeto foi deletado com sucesso
        assertFalse(dadosRepository.findById(dados.getId()).isPresent());
    }

    @Test
    public void atualizarDadosModelTest() {
        // cria um objeto de exemplo para ser salvo e depois atualizado
        DadosModel dados = new DadosModel("Jan-2023", "FINANCIAMENTO IMOBILIÁRIO COM TAXAS",
                "1", "BCO DO BRASIL S.A", "00000000",
                "2023-01", 0.00, 0.00);
        dadosRepository.save(dados);

        // cria um objeto com os dados atualizados
        DadosModel dadosAtualizados = new DadosModel("null", "Atualizado", "Atualizado", "Atualizado", "00000002", "Atualizado", 1.0, 12.0);

        // atualiza o objeto usando o serviço
        DadosModel resultado = dadosService.atualizarDadosModel(dados.getId(), dadosAtualizados);

        // busca o objeto atualizado no banco de dados
        DadosModel objetoAtualizado = dadosRepository.findById(resultado.getId()).orElse(null);

        // verifica se o objeto foi atualizado com sucesso
        assertNotNull(objetoAtualizado.getId());
        assertNull(objetoAtualizado.getAnoMes());
        assertNull(objetoAtualizado.getMes());
        assertNull(objetoAtualizado.getModalidade());
        assertNull(objetoAtualizado.getPosicao());
        assertNull( objetoAtualizado.getCnpj8());
        assertNull(objetoAtualizado.getInstituicaoFinanceira());
        assertNull(objetoAtualizado.getTaxaJurosAoMes());
        assertNull(objetoAtualizado.getTaxaJurosAoAno());
    }

    @Test
    public void testBuscarPorAnoMes() {
        // Criação de dados de teste
        DadosModel dado1 = new DadosModel("2023-01", "A", "modalidade1", 1, "inst1", new BigDecimal("1.1"), new BigDecimal("13.9"), "12345678");
        DadosModel dado2 = new DadosModel("2022-01", "B", "modalidade2", 2, "inst2", new BigDecimal("1.2"), new BigDecimal("14.0"), "23456789");
        dadosRepository.saveAll(Arrays.asList(dado1, dado2));

        // Teste do método buscarPorAnoMes
        List<DadosModel> dados = dadosService.buscarPorAnoMes("2023-01");

        // Verifica se a lista de dados recuperados possui tamanho esperado
        assertEquals(1, dados.size());

        // Recupera o dado da lista e verifica se os valores são iguais aos dados de teste criados anteriormente
        DadosModel dadoRecuperado = dados.get(0);
        assertEquals("2023-01", dadoRecuperado.getAnoMes());
        assertNull( dadoRecuperado.getMes());
        assertNull( dadoRecuperado.getModalidade());
        assertNull( dadoRecuperado.getPosicao());
        assertNull( dadoRecuperado.getInstituicaoFinanceira());
        assertNull(dadoRecuperado.getCnpj8());
    }
}

