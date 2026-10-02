package edu.fatec.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {

    @ParameterizedTest
    @CsvSource({
            "1",
            "33",
            "100"
    })
    void testCriacaoBanco(int codigo) {
        Banco banco = new Banco(codigo);
        assertEquals(codigo, banco.getCodigo());
        assertTrue(banco.getSaques().isEmpty());
        assertEquals(0, banco.getMaiorSaque());
        assertEquals(0, banco.getMenorSaque());
        assertEquals(0.0, banco.getMediaSaques());
        assertEquals(0, banco.getTotalSaques());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10, 20, 30",
            "2, 50, 100, 150",
            "3, 5, 10, 25"
    })
    void testEstatisticas(int codigoBanco, int menorSaque, int medioSaque, int maiorSaque) {

        Banco banco = new Banco(codigoBanco);

        banco.adicionarSaque(new Saque(menorSaque, codigoBanco, Collections.emptyMap()));
        banco.adicionarSaque(new Saque(medioSaque, codigoBanco, Collections.emptyMap()));
        banco.adicionarSaque(new Saque(maiorSaque, codigoBanco, Collections.emptyMap()));

        assertEquals(3, banco.getSaques().size());
        assertEquals(maiorSaque, banco.getMaiorSaque());
        assertEquals(menorSaque, banco.getMenorSaque());
        assertEquals((menorSaque + medioSaque + maiorSaque) / 3.0, banco.getMediaSaques());
        assertEquals(menorSaque + medioSaque + maiorSaque, banco.getTotalSaques());
    }

}
