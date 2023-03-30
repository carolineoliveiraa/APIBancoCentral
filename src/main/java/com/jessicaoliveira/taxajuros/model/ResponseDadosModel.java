package com.jessicaoliveira.taxajuros.model;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ResponseDadosModel {
    private List<DadosModel> dados;

    public ResponseDadosModel(List<DadosModel> dados) {
        this.dados = dados;
    }

    public List<DadosModel> getDados() {
        return dados;
    }

    public void setDados(List<DadosModel> dados) {
        this.dados = dados;
    }

}
