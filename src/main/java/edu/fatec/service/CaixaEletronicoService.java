package edu.fatec.service;

import edu.fatec.model.Banco;
import edu.fatec.model.Cedula;
import edu.fatec.model.Saque;

import java.util.*;

/**
 * Camada de serviço: concentra toda a regra de negócio do caixa eletrônico.
 * Não conhece Scanner, JOptionPane ou qualquer forma de entrada/saída —
 * apenas recebe dados já validados/parseados e devolve resultados ou
 * lança exceções quando uma regra é violada. Isso permite trocar a
 * camada de interface (console, Swing, testes) sem tocar nesta classe.
 */
public class CaixaEletronicoService {

    private static final int LIMITE_SAQUES = 100;

    private final List<Cedula> cedulas;
    private final Map<Integer, Banco> bancos;

    private int quantidadeSaques;

    public CaixaEletronicoService() {
        cedulas = new ArrayList<>();
        bancos = new HashMap<>();
        quantidadeSaques = 0;

        inicializarCedulas();
    }

    private void inicializarCedulas() {
        // Cada cédula começa com 6 ocorrências.
        cedulas.add(new Cedula(200, 6));
        cedulas.add(new Cedula(100, 6));
        cedulas.add(new Cedula(50, 6));
        cedulas.add(new Cedula(20, 6));
        cedulas.add(new Cedula(10, 6));
    }

    public List<Cedula> getCedulas() {
        return Collections.unmodifiableList(cedulas);
    }

    /**
     * Adiciona notas ao caixa a partir de um mapa valor -> quantidade a somar.
     * Valores não presentes no mapa, ou com quantidade nula/zero, são ignorados.
     */
    public void carregarNotas(Map<Integer, Integer> quantidadesPorValor) {
        for (Cedula cedula : cedulas) {
            Integer quantidade = quantidadesPorValor.get(cedula.getValor());
            if (quantidade != null && quantidade > 0) {
                cedula.adicionar(quantidade);
            }
        }
    }

    public boolean limiteSaquesAtingido() {
        return quantidadeSaques >= LIMITE_SAQUES;
    }

    /**
     * Executa um saque, validando as regras de negócio.
     *
     * @throws IllegalStateException  quando o caixa não pode atender ao saque
     *                                 por regra de estado (limite atingido, sem notas,
     *                                 impossibilidade de compor o valor).
     * @throws IllegalArgumentException quando o valor informado é inválido.
     */
    public Saque realizarSaque(int valor, int codigoBanco) {

        if (limiteSaquesAtingido()) {
            throw new IllegalStateException("LIMITE DE 100 RETIRADAS ATINGIDO.");
        }

        if (getSaldoCaixa() == 0) {
            throw new IllegalStateException("NÃO HÁ MAIS NOTAS NO CAIXA.");
        }

        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do saque deve ser maior que zero.");
        }

        if (valor > getSaldoCaixa()) {
            throw new IllegalArgumentException("EXCEDEU O LIMITE DO CAIXA");
        }

        Map<Integer, Integer> notasSaque = calcularNotas(valor);

        if (notasSaque.isEmpty()) {
            throw new IllegalStateException(
                    "NÃO É POSSÍVEL REALIZAR O SAQUE COM AS NOTAS DISPONÍVEIS."
            );
        }

        int valorCalculado = notasSaque.entrySet()
                .stream()
                .mapToInt(e -> e.getKey() * e.getValue())
                .sum();

        if (valorCalculado != valor) {
            throw new IllegalStateException(
                    "NÃO É POSSÍVEL DISPENSAR ESSE VALOR COM AS NOTAS DISPONÍVEIS."
            );
        }

        retirarNotas(notasSaque);

        Saque saque = new Saque(valor, codigoBanco, notasSaque);

        bancos.computeIfAbsent(codigoBanco, Banco::new)
                .adicionarSaque(saque);

        quantidadeSaques++;

        return saque;
    }

    private Map<Integer, Integer> calcularNotas(int valor) {

        Map<Integer, Integer> resultado = new LinkedHashMap<>();

        int restante = valor;

        // Maior para menor.
        for (Cedula cedula : cedulas) {

            int quantidadeNecessaria = restante / cedula.getValor();
            int quantidadeDisponivel = cedula.getQuantidade();
            int quantidadeUsada = Math.min(quantidadeNecessaria, quantidadeDisponivel);

            if (quantidadeUsada > 0) {
                resultado.put(cedula.getValor(), quantidadeUsada);
                restante -= quantidadeUsada * cedula.getValor();
            }

            if (restante == 0) {
                break;
            }
        }

        // Se não conseguiu montar o valor exato, o saque não será realizado.
        if (restante != 0) {
            return Collections.emptyMap();
        }

        return resultado;
    }

    private void retirarNotas(Map<Integer, Integer> notas) {

        for (Map.Entry<Integer, Integer> entry : notas.entrySet()) {

            int valor = entry.getKey();
            int quantidade = entry.getValue();

            for (Cedula cedula : cedulas) {

                if (cedula.getValor() == valor) {

                    for (int i = 0; i < quantidade; i++) {
                        cedula.retirar();
                    }

                    break;
                }
            }
        }
    }

    public List<Banco> getBancosOrdenados() {
        List<Banco> lista = new ArrayList<>(bancos.values());
        lista.sort(Comparator.comparingInt(Banco::getCodigo));
        return lista;
    }

    public int getSaldoCaixa() {
        return cedulas.stream()
                .mapToInt(Cedula::getTotal)
                .sum();
    }

    public int getQuantidadeSaques() {
        return quantidadeSaques;
    }
}
