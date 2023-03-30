package com.jessicaoliveira.taxajuros.testesUnitarios;

import com.jessicaoliveira.taxajuros.model.DadosModel;
import com.jessicaoliveira.taxajuros.repository.DadosRepository;
import com.jessicaoliveira.taxajuros.service.DadosService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest
public class DadosServiceTest {
    @Mock
    private DadosRepository dadosRepository;

    private DadosService dadosService;


    private static final String API_URL = "https://olinda.bcb.gov.br/olinda/servico/taxaJuros/versao/v2/odata/TaxasJurosMensalPorMes?$top=100&$format=json";

    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        restTemplate = mock(RestTemplate.class);
    }

    @Test
    public void testListarDadosModel() {
        RestTemplate restTemplate = new RestTemplate();
        DadosModelResponse response = restTemplate.getForObject(API_URL, DadosModelResponse.class);
        List<DadosModel> dadosModelList = response.getValue();

        Assertions.assertNotNull(dadosModelList, "A lista de DadosModel não deve ser nula");
        Assertions.assertFalse(dadosModelList.isEmpty(), "A lista de DadosModel não deve estar vazia");
        Assertions.assertEquals(100, dadosModelList.size(), "A lista de DadosModel deve ter 100 elementos");
    }

    private static class DadosModelResponse {
        private List<DadosModel> value;

        public List<DadosModel> getValue() {
            return value;
        }

        public void setValue(List<DadosModel> value) {
            this.value = value;
        }
    }

    @Test
    void listarTodos() {
        // Arrange
        List<DadosModel> dados = Arrays.asList(new DadosModel(), new DadosModel());
        when(dadosRepository.findAll()).thenReturn(dados);

        dadosService = new DadosService(dadosRepository);

        // Act
        List<DadosModel> resultado = dadosService.listarTodos();

        // Assert
        assertThat(resultado).isEqualTo(dados);
    }

    @Test
    void listarComPaginacao() {
        // Arrange
        int pagina = 0;
        int tamanho = 20;
        List<DadosModel> dados = Arrays.asList(new DadosModel(), new DadosModel());
        when(dadosRepository.findAll(PageRequest.of(pagina, tamanho))).thenReturn(new PageImpl<>(dados));

        dadosService = new DadosService(dadosRepository);

        // Act
        List<DadosModel> resultado = dadosService.listarComPaginacao(pagina, tamanho).getContent();

        // Assert
        assertThat(resultado).isEqualTo(dados);
    }

    @Test
    void buscarPorId() {
        // Arrange
        Long id = 1L;
        DadosModel dados = new DadosModel();
        dados.setId(id);
        when(dadosRepository.findById(id)).thenReturn(Optional.of(dados));

        dadosService = new DadosService(dadosRepository);

        // Act
        Optional<DadosModel> resultado = dadosService.buscarPorId(id);

        // Assert
        assertThat(resultado.isPresent()).isTrue();
        assertThat(resultado.get()).isEqualTo(dados);
    }

    @Test
    void criar() {
        // Arrange
        DadosModel dados = new DadosModel();
        when(dadosRepository.save(dados)).thenReturn(dados);

        dadosService = new DadosService(dadosRepository);

        // Act
        DadosModel resultado = dadosService.criar(dados);

        // Assert
        assertThat(resultado).isEqualTo(dados);
    }

    @Test
    void atualizar() {
        // Arrange
        Long id = 1L;
        DadosModel dados = new DadosModel();
        dados.setId(id);
        dados.setAnoMes("202102");
        when(dadosRepository.findById(id)).thenReturn(Optional.of(dados));
        when(dadosRepository.save(dados)).thenReturn(dados);

        dadosService = new DadosService(dadosRepository);

        // Act
        DadosModel resultado = dadosService.atualizar(id, dados);

        // Assert
        assertThat(resultado).isEqualTo(dados);
    }

    @Test
    void deletar() {
        // Arrange
        Long id = 1L;

        dadosService = new DadosService(dadosRepository);

        // Act
        dadosService.deletar(id);

        // Assert
        // Verifica que o método foi chamado pelo menos uma vez
        verify(dadosRepository, atLeastOnce()).deleteById(id);
    }


}
