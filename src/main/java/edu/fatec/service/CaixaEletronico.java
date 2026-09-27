package edu.fatec.service;


import edu.fatec.model.Banco;
import edu.fatec.model.Cedula;
import edu.fatec.model.Saque;

import java.util.*;

public class CaixaEletronico {

    private final List<Cedula> cedulas;
    private final Map<Integer, Banco> bancos;

    private int quantidadeSaques;

    public CaixaEletronico() {
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

    public void carregarNotas() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== CARREGAR NOTAS =====");

        for (Cedula cedula : cedulas) {

            System.out.print(
                    "Quantidade de notas de R$ "
                            + cedula.getValor()
                            + " para adicionar: "
            );

            int quantidade = lerInteiroNaoNegativo(scanner);

            cedula.adicionar(quantidade);
        }

        System.out.println("\nNotas carregadas com sucesso!");
    }

    public void realizarSaque() {

        if (quantidadeSaques >= 100) {
            System.out.println(
                    "\nLIMITE DE 100 RETIRADAS ATINGIDO."
            );
            return;
        }

        if (getSaldoCaixa() == 0) {
            System.out.println(
                    "\nNÃO HÁ MAIS NOTAS NO CAIXA."
            );
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n===== RETIRADA DE NOTAS =====");

        System.out.print("Digite o valor do saque: R$ ");
        int valor = lerInteiroPositivo(scanner);

        if (valor > getSaldoCaixa()) {
            System.out.println(
                    "EXCEDEU O LIMITE DO CAIXA"
            );
            return;
        }

        System.out.print("Digite o código do banco: ");
        int codigoBanco = lerInteiroPositivo(scanner);

        Map<Integer, Integer> notasSaque = calcularNotas(valor);

        if (notasSaque.isEmpty()) {
            System.out.println(
                    "NÃO É POSSÍVEL REALIZAR O SAQUE COM AS NOTAS DISPONÍVEIS."
            );
            return;
        }

        int valorCalculado = notasSaque.entrySet()
                .stream()
                .mapToInt(e -> e.getKey() * e.getValue())
                .sum();

        if (valorCalculado != valor) {
            System.out.println(
                    "NÃO É POSSÍVEL DISPENSAR ESSE VALOR COM AS NOTAS DISPONÍVEIS."
            );
            return;
        }

        retirarNotas(notasSaque);

        Saque saque = new Saque(
                valor,
                codigoBanco,
                notasSaque
        );

        bancos
                .computeIfAbsent(codigoBanco, Banco::new)
                .adicionarSaque(saque);

        quantidadeSaques++;

        System.out.println("\nSAQUE REALIZADO COM SUCESSO!");

        System.out.println("Notas entregues:");

        notasSaque.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByKey().reversed())
                .forEach(entry ->
                        System.out.println(
                                entry.getValue()
                                        + " x R$ "
                                        + entry.getKey()
                        )
                );

        System.out.println(
                "Total: R$ " + valor
        );
    }

    private Map<Integer, Integer> calcularNotas(int valor) {

        Map<Integer, Integer> resultado = new LinkedHashMap<>();

        int restante = valor;

        // Maior para menor.
        for (Cedula cedula : cedulas) {

            int quantidadeNecessaria =
                    restante / cedula.getValor();

            int quantidadeDisponivel =
                    cedula.getQuantidade();

            int quantidadeUsada =
                    Math.min(
                            quantidadeNecessaria,
                            quantidadeDisponivel
                    );

            if (quantidadeUsada > 0) {

                resultado.put(
                        cedula.getValor(),
                        quantidadeUsada
                );

                restante -=
                        quantidadeUsada * cedula.getValor();
            }

            if (restante == 0) {
                break;
            }
        }

        // Se não conseguiu montar o valor exato,
        // o saque não será realizado.
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

    public void estatistica() {

        System.out.println("\n================================");
        System.out.println("          ESTATÍSTICA");
        System.out.println("================================");

        if (bancos.isEmpty()) {
            System.out.println("Nenhum saque realizado.");
        } else {

            List<Integer> codigos =
                    new ArrayList<>(bancos.keySet());

            Collections.sort(codigos);

            for (Integer codigo : codigos) {

                Banco banco = bancos.get(codigo);

                System.out.println(
                        "\nBanco: " + banco.getCodigo()
                );

                System.out.println(
                        "Maior saque: R$ "
                                + banco.getMaiorSaque()
                );

                System.out.println(
                        "Menor saque: R$ "
                                + banco.getMenorSaque()
                );

                System.out.printf(
                        Locale.US,
                        "Média dos saques: R$ %.2f%n",
                        banco.getMediaSaques()
                );

                System.out.println(
                        "Valor total dos saques: R$ "
                                + banco.getTotalSaques()
                );
            }
        }

        System.out.println(
                "\nValor das sobras do caixa: R$ "
                        + getSaldoCaixa()
        );

        System.out.println(
                "Quantidade de saques realizados: "
                        + quantidadeSaques
        );

        System.out.println("================================");
    }

    public int getSaldoCaixa() {

        return cedulas.stream()
                .mapToInt(Cedula::getTotal)
                .sum();
    }

    public void mostrarNotas() {

        System.out.println("\n===== NOTAS DISPONÍVEIS =====");

        for (Cedula cedula : cedulas) {
            System.out.println(
                    "R$ "
                            + cedula.getValor()
                            + " -> "
                            + cedula.getQuantidade()
                            + " nota(s)"
            );
        }

        System.out.println(
                "Saldo total: R$ "
                        + getSaldoCaixa()
        );
    }

    private int lerInteiroPositivo(Scanner scanner) {

        while (true) {

            try {

                int valor = scanner.nextInt();

                if (valor > 0) {
                    return valor;
                }

                System.out.print(
                        "Digite um valor maior que zero: "
                );

            } catch (InputMismatchException e) {

                System.out.print(
                        "Digite apenas números: "
                );

                scanner.next();
            }
        }
    }

    private int lerInteiroNaoNegativo(Scanner scanner) {

        while (true) {

            try {

                int valor = scanner.nextInt();

                if (valor >= 0) {
                    return valor;
                }

                System.out.print(
                        "Digite zero ou um valor positivo: "
                );

            } catch (InputMismatchException e) {

                System.out.print(
                        "Digite apenas números: "
                );

                scanner.next();
            }
        }
    }
}
