package com.jessicaoliveira.taxajuros.service;

import com.jessicaoliveira.taxajuros.model.DadosModel;
import com.jessicaoliveira.taxajuros.repository.DadosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DadosService {
    private final DadosRepository dadosRepository;


    private static final String API_URL = "https://olinda.bcb.gov.br/olinda/servico/taxaJuros/versao/v2/odata/TaxasJurosMensalPorMes?$top=100&$format=json";


    public static List<DadosModel> listarDadosModel() {
        RestTemplate restTemplate = new RestTemplate();
        DadosModelResponse response = restTemplate.getForObject(API_URL, DadosModelResponse.class);
        return response.getValue();
    }
    private static class DadosModelResponse{
        private List<DadosModel> value;

        public List<DadosModel> getValue(){
            return value;
        }

        public void setValue(List<DadosModel> value){
            this.value = value;
        }
    }

    //OK NOS TESTES
    public List<DadosModel> listarTodos() {
        List<DadosModel> dados = dadosRepository.findAll();
        if (dados.isEmpty()) {
            dados = listarDadosModel();
            dadosRepository.saveAll(dados);
        }
        return dados;
    }

    //OK NOS TESTES
    public DadosModel criar(DadosModel dados) {
        return dadosRepository.save(dados);
    }

    //OK NOS TESTES
    public DadosModel atualizar(Long id, DadosModel dados) {
        Optional<DadosModel> dadosOptional = dadosRepository.findById(id);
        if (dadosOptional.isPresent()) {
            DadosModel dadosAtualizados = dadosOptional.get();
            dadosAtualizados.setAnoMes(dados.getAnoMes());
            dadosAtualizados.setModalidade(dados.getModalidade());
            dadosAtualizados.setPosicao(dados.getPosicao());
            dadosAtualizados.setInstituicaoFinanceira(dados.getInstituicaoFinanceira());
            dadosAtualizados.setTaxaJurosAoMes(dados.getTaxaJurosAoMes());
            dadosAtualizados.setTaxaJurosAoAno(dados.getTaxaJurosAoAno());
            dadosAtualizados.setCnpj8(dados.getCnpj8());
            return dadosRepository.save(dadosAtualizados);
        }
        return null;
    }

    //OK NOS TESTES
    public void deletar(Long id) {
        dadosRepository.deleteById(id);
    }

    //OK NOS TESTES
    public Page<DadosModel> listarComPaginacao(Integer pagina, Integer tamanho) {
        Pageable paginacao = PageRequest.of(pagina, tamanho);
        return dadosRepository.findAll(paginacao);
    }

    public List<DadosModel> listarPorAnoMes(String anoMes) {
        return dadosRepository.findByAnoMes(anoMes);
    }

    //OK NOS TESTES
    public Optional<DadosModel> buscarPorId(Long id) {
        return dadosRepository.findById(id);
    }

    public List<DadosModel> listarPorInstituicaoFinanceira(String InstituicaoFinanceira) {
        List<DadosModel> dados = dadosRepository.findAll();
        return dados.stream()
                .filter(dado -> InstituicaoFinanceira.equals(dado.getInstituicaoFinanceira()))
                .collect(Collectors.toList());
    }

    public DadosModel salvarDadosModel(DadosModel dados) {
        return dadosRepository.save(dados);
    }

    public void deletarDadosModel(Long id) {
        dadosRepository.deleteById(id);
    }

    public DadosModel atualizarDadosModel(Long id, DadosModel dados) {
        Optional<DadosModel> dadosOptional = dadosRepository.findById(id);
        if (dadosOptional.isPresent()) {
            DadosModel dadosSalvos = dadosOptional.get();
            dadosSalvos.setMes(dados.getMes());
            dadosSalvos.setModalidade(dados.getModalidade());
            dadosSalvos.setPosicao(dados.getPosicao());
            dadosSalvos.setInstituicaoFinanceira(dados.getInstituicaoFinanceira());
            dadosSalvos.setTaxaJurosAoMes(dados.getTaxaJurosAoMes());
            dadosSalvos.setTaxaJurosAoAno(dados.getTaxaJurosAoAno());
            dadosSalvos.setCnpj8(dados.getCnpj8());
            dadosSalvos.setAnoMes(dados.getAnoMes());
            return dadosRepository.save(dadosSalvos);
        } else {
            return null;
        }
    }

    public List<DadosModel> buscarPorAnoMes(String anoMes) {
        return dadosRepository.findByAnoMes(anoMes);

    }
}



