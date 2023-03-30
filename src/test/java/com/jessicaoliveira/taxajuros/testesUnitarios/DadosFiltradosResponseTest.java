package com.jessicaoliveira.taxajuros.testesUnitarios;

import com.jessicaoliveira.taxajuros.response.DadosFiltradosResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DadosFiltradosResponseTest {
    @Test
    public void testSetMensagem() {
        DadosFiltradosResponse response = new DadosFiltradosResponse();
        response.setMensagem("Teste de mensagem");
        assertEquals("Teste de mensagem", response.getMensagem());
    }

}
