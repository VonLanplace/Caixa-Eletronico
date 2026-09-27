package edu.fatec;

import edu.fatec.service.CaixaEletronico;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        CaixaEletronico caixa = new CaixaEletronico();

        int opcao;

        do {

            exibirMenu();

            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.print(
                        "Digite uma opção válida: "
                );
                scanner.next();
            }

            opcao = scanner.nextInt();

            switch (opcao) {

                case 1:
                    caixa.carregarNotas();
                    break;

                case 2:
                    caixa.realizarSaque();
                    break;

                case 3:
                    caixa.estatistica();
                    break;

                case 9:
                    System.out.println(
                            "\nSistema encerrado."
                    );
                    break;

                default:
                    System.out.println(
                            "\nOpção inválida!"
                    );
            }

        } while (opcao != 9);

        scanner.close();
    }

    private static void exibirMenu() {

        System.out.println("\n");
        System.out.println("==============================");
        System.out.println("       CAIXA ELETRÔNICO");
        System.out.println("==============================");
        System.out.println("1 - Carregar Notas");
        System.out.println("2 - Retirar Notas");
        System.out.println("3 - Estatística");
        System.out.println("9 - Fim");
        System.out.println("==============================");

    }
}
