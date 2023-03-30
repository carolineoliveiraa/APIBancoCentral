package com.jessicaoliveira.taxajuros.response;

import com.jessicaoliveira.taxajuros.model.DadosModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DadosFiltradosResponse {

    private String mensagem;
    private List<DadosModel> dadosFiltrados;

    public DadosFiltradosResponse() {
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setDadosFiltrados(List<DadosModel> dadosFiltrados) {
        this.dadosFiltrados = dadosFiltrados;
    }
}
