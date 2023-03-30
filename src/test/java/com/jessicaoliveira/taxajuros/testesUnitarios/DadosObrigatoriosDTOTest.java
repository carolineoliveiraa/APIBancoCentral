package com.jessicaoliveira.taxajuros.testesUnitarios;

import com.jessicaoliveira.taxajuros.DTO.DadosObrigatoriosDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class DadosObrigatoriosDTOTest {

    @Test
    public void testGettersAndSetters() {
        // given
        String mes = "Jan-2023";
        String modalidade = "FINANCIAMENTO IMOBILIARIO COM TAXAS REGULADAS - PRÉ-FIXADO";
        Integer posicao = 1;
        String instituicaoFinanceira = "BCO DO BRASIL SA";
        Double taxaJurosAoMes = 0.00;
        Double taxaJurosAoAno = 0.00;
        Integer cnpj8 = 00000000;
        String anoMes = "2023-01";

        // when
        DadosObrigatoriosDTO dto = new DadosObrigatoriosDTO();
        dto.setMes(mes);
        dto.setModalidade(modalidade);
        dto.setPosicao(posicao);
        dto.setInstituicaoFinanceira(instituicaoFinanceira);
        dto.setTaxaJurosAoMes(taxaJurosAoMes);
        dto.setTaxaJurosAoAno(taxaJurosAoAno);
        dto.setCnpj8(cnpj8);
        dto.setAnoMes(anoMes);

        // then
        Assertions.assertEquals(mes, dto.getMes());
        Assertions.assertEquals(modalidade, dto.getModalidade());
        Assertions.assertEquals(posicao, dto.getPosicao());
        Assertions.assertEquals(instituicaoFinanceira, dto.getInstituicaoFinanceira());
        Assertions.assertEquals(taxaJurosAoMes, dto.getTaxaJurosAoMes());
        Assertions.assertEquals(taxaJurosAoAno, dto.getTaxaJurosAoAno());
        Assertions.assertEquals(cnpj8, dto.getCnpj8());
        Assertions.assertEquals(anoMes, dto.getAnoMes());
    }

}
