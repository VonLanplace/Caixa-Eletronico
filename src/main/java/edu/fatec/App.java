package edu.fatec;

import edu.fatec.controller.CaixaEletronicoController;

import javax.swing.*;

public class App {

    public static void main(String[] args) {

        CaixaEletronicoController controller = new CaixaEletronicoController();

        int opcao;

        do {
            opcao = exibirMenu();

            switch (opcao) {

                case 1:
                    controller.carregarNotas();
                    break;

                case 2:
                    controller.realizarSaque();
                    break;

                case 3:
                    controller.estatistica();
                    break;

                case 4:
                    controller.mostrarNotas();
                    break;

                case 9:
                    JOptionPane.showMessageDialog(null, "Sistema encerrado.");
                    break;

                case -1:
                    // Usuário fechou/cancelou o diálogo do menu: encerra sem mensagem extra.
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
            }

        } while (opcao != 9 && opcao != -1);

        System.exit(0);
    }

    private static int exibirMenu() {

        String menu =
                "==============================\n" +
                "       CAIXA ELETRÔNICO\n" +
                "==============================\n" +
                "1 - Carregar Notas\n" +
                "2 - Retirar Notas\n" +
                "3 - Estatística\n" +
                "4 - Mostrar Notas\n" +
                "9 - Fim\n" +
                "==============================";

        String entrada = JOptionPane.showInputDialog(menu, "Escolha uma opção:");

        if (entrada == null) {
            return -1; // usuário fechou o diálogo -> encerra o sistema
        }

        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            return -2; // cai no "default" do switch como opção inválida
        }
    }
}
