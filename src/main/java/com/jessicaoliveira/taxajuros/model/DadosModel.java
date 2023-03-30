package com.jessicaoliveira.taxajuros.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DadosModel {
    //Declarações feitas, e o JsonProperty mapeia uma propriedade JSON
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonProperty("Mes")
    private String Mes;
    @JsonProperty("Modalidade")
    private String Modalidade;
    @JsonProperty("Posicao")
    private Integer Posicao;
    @JsonProperty("InstituicaoFinanceira")
    private String InstituicaoFinanceira;
    @JsonProperty("TaxaJurosAoMes")
    private Double TaxaJurosAoMes;
    @JsonProperty("TaxaJurosAoAno")
    private Double TaxaJurosAoAno;
    @JsonProperty("cnpj8")
    private Integer cnpj8;
    @JsonProperty("anoMes")
    private String anoMes;


    //Construtores para o teste de integraçao
    public DadosModel(String s, String teste, String teste1, int s1,
                      String s2, BigDecimal teste2, BigDecimal v, String v1) {
    }

    public DadosModel(String Mes, String Modalidade, String Posicao,
                      String InstituicaoFinanceira, String cnpj8,
                      String anoMes, double TaxaJurosAoMes, double TaxaJurosAoAno) {
    }

    public DadosModel(long l, String dados, String instituicaoFinanceira, double v) {
    }

}
