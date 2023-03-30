package com.jessicaoliveira.taxajuros.testesUnitarios;

import com.jessicaoliveira.taxajuros.model.DadosModel;
import com.jessicaoliveira.taxajuros.model.ResponseDadosModel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ResponseDadosModelTest {

    @Test
    void testGetDados() {
        DadosModel dados1 = new DadosModel();
        DadosModel dados2 = new DadosModel();
        List<DadosModel> listaDados = Arrays.asList(dados1, dados2);
        ResponseDadosModel response = new ResponseDadosModel(listaDados);
        assertEquals(listaDados, response.getDados());
    }

    @Test
    void testSetDados() {
        DadosModel dados1 = new DadosModel();
        DadosModel dados2 = new DadosModel();
        List<DadosModel> listaDados1 = Arrays.asList(dados1, dados2);
        ResponseDadosModel response = new ResponseDadosModel(listaDados1);
        DadosModel dados3 = new DadosModel();
        DadosModel dados4 = new DadosModel();
        List<DadosModel> listaDados2 = Arrays.asList(dados3, dados4);
        response.setDados(listaDados2);
        assertEquals(listaDados2, response.getDados());
    }

}
