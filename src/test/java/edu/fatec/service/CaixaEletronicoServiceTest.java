package edu.fatec.service;

import edu.fatec.model.Banco;
import edu.fatec.model.Saque;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CaixaEletronicoServiceTest {

    private CaixaEletronicoService service;

    @BeforeEach
    void setUp() {
        service = new CaixaEletronicoService();
    }

    /**
     * <p>Os valores utilizados neste teste são fixos propositalmente, pois
     * representam o estado inicial definido pelo contrato da configuração padrão
     * do caixa: 5 tipos de cédulas, saldo inicial de 2280, nenhum saque realizado
     * e limite de saques não atingido. Caso esses valores sejam alterados na
     * implementação padrão, este teste deve falhar para indicar uma mudança
     * nesse comportamento esperado.</p>
     */
    @Test
    void testInicializacaoPadrao() {
        assertNotNull(service.getCedulas());
        assertEquals(5, service.getCedulas().size());
        assertEquals(2280, service.getSaldoCaixa());
        assertEquals(0, service.getQuantidadeSaques());
        assertFalse(service.limiteSaquesAtingido());
    }

    @ParameterizedTest
    @CsvSource({
            "200, 33, 200",
            "300, 10, 300",
            "370, 20, 370",
            "10, 15, 10"
    })
    void testSaqueValido(int valor, int codigoBanco, int valorEsperado) {
        Saque saque = service.realizarSaque(valor, codigoBanco);
        assertNotNull(saque);
        assertEquals(valorEsperado, saque.getValor());
        assertEquals(codigoBanco, saque.getCodigoBanco());
        assertFalse(saque.getCedulas().isEmpty());
        assertEquals(1, service.getQuantidadeSaques());

        List<Banco> bancos = service.getBancosOrdenados();
        assertEquals(1, bancos.size());
        assertEquals(codigoBanco, bancos.get(0).getCodigo());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50, -999})
    void testSaqueValorInvalido(int valorInvalido) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.realizarSaque(valorInvalido, 1)
        );
        assertEquals("O valor do saque deve ser maior que zero.", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {5000, 10000, 50000})
    void testSaqueExcedeSaldo(int valorExcessivo) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.realizarSaque(valorExcessivo, 1)
        );
        assertEquals("EXCEDEU O LIMITE DO CAIXA", ex.getMessage());
    }

    /**
     * <p>Os valores utilizados são fixos propositalmente, pois o teste parte
     * da configuração inicial padrão do caixa, cujo saldo total é 2280.
     */
    @Test
    void testCaixaSemNotas() {
        service.realizarSaque(2280, 1);
        assertEquals(0, service.getSaldoCaixa());

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.realizarSaque(10, 1)
        );
        assertEquals("NÃO HÁ MAIS NOTAS NO CAIXA.", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {15, 25, 35, 45, 55})
    void testValorImpossivelCompor(int valorSaque) {
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.realizarSaque(valorSaque, 1)
        );

        assertTrue(
                ex.getMessage().contains("NÃO É POSSÍVEL")
                        || ex.getMessage().contains("DISPENSAR"));
    }

}
