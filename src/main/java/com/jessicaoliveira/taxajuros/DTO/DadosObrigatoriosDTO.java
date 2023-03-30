package com.jessicaoliveira.taxajuros.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadosObrigatoriosDTO {

    @NotNull
    @JsonProperty("Mes")
    private String mes;

    @JsonProperty("Modalidade")
    private String modalidade;

    @JsonProperty("Posicao")
    public Integer posicao;

    @JsonProperty("InstituicaoFinanceira")
    private String instituicaoFinanceira;

    @JsonProperty("TaxaJurosAoMes")
    private Double taxaJurosAoMes;

    @JsonProperty("TaxaJurosAoAno")
    private Double taxaJurosAoAno;

    @JsonProperty("cnpj8")
    private Integer cnpj8;

    @JsonProperty("anoMes")
    private String anoMes;

}
