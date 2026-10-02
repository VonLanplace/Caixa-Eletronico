package edu.fatec.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CedulaTest {

    @ParameterizedTest
    @CsvSource({
            "200, 6, 1200, 'R$ 200 - 6 nota(s)'",
            "100, 5, 500, 'R$ 100 - 5 nota(s)'",
            "50, 4, 200, 'R$ 50 - 4 nota(s)'",
            "20, 3, 60, 'R$ 20 - 3 nota(s)'",
            "10, 2, 20, 'R$ 10 - 2 nota(s)'",
            "5, 1, 5, 'R$ 5 - 1 nota(s)'"
    })
    void testCriacaoETotal(int valor, int quantidade, int totalEsperado, String toStringEsperado) {
        Cedula cedula = new Cedula(valor, quantidade);
        assertEquals(valor, cedula.getValor());
        assertEquals(quantidade, cedula.getQuantidade());
        assertEquals(totalEsperado, cedula.getTotal());
        assertEquals(toStringEsperado, cedula.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "100, 2, 3, 5",
            "50, 1, 10, 11",
            "20, 5, 0, 5"
    })
    void testAdicionar(int valor, int inicial, int adicionar, int finalEsp) {
        Cedula cedula = new Cedula(valor, inicial);
        cedula.adicionar(adicionar);
        assertEquals(finalEsp, cedula.getQuantidade());
    }

    @ParameterizedTest
    @CsvSource({
            "100, 3, 2, 1",
            "50, 1, 1, 0",
            "20, 0, 1, 0"
    })
    void testRetirar(int valor, int inicial, int retirarVezes, int finalEsp) {
        Cedula cedula = new Cedula(valor, inicial);
        for (int i = 0; i < retirarVezes; i++) {
            cedula.retirar();
        }
        assertEquals(finalEsp, cedula.getQuantidade());
    }
}
