package com.jessicaoliveira.taxajuros.testesUnitarios;

import com.jessicaoliveira.taxajuros.response.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorResponseTest {

    @Test
    public void testConstrutorComStatusEMessage() {
        ErrorResponse error = new ErrorResponse(404, "Página não encontrada");
        assertEquals(404, error.getStatus());
        assertEquals("Página não encontrada", error.getMessage());
    }

    // Teste para o construtor com mensagem apenas
    @Test
    public void testConstructorComMessageApenas() {
        ErrorResponse error = new ErrorResponse("Erro interno");
        assertEquals("Erro interno", error.getMessage());
        assertEquals(0, error.getStatus());
    }

    // Teste para o método setStatus
    @Test
    public void testSetStatus() {
        ErrorResponse error = new ErrorResponse("Erro interno");
        error.setStatus(500);
        assertEquals(500, error.getStatus());
    }

    // Teste para o método setMessage
    @Test
    public void testSetMessage() {
        ErrorResponse error = new ErrorResponse(404, "Página não encontrada");
        error.setMessage("Erro interno");
        assertEquals("Erro interno", error.getMessage());
    }

}


