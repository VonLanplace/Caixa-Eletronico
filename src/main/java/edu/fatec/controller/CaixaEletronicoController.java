package edu.fatec.controller;

import edu.fatec.model.Banco;
import edu.fatec.model.Cedula;
import edu.fatec.model.Saque;
import edu.fatec.service.CaixaEletronicoService;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Camada de controller: única responsável por conversar com o usuário
 * (JOptionPane), validar entradas e traduzir o resultado do service em
 * mensagens na tela. Não contém regra de negócio — apenas orquestra.
 */
public class CaixaEletronicoController {

    private final CaixaEletronicoService service;

    public CaixaEletronicoController() {
        this.service = new CaixaEletronicoService();
    }

    public void carregarNotas() {

        Map<Integer, Integer> quantidades = new LinkedHashMap<>();

        for (Cedula cedula : service.getCedulas()) {

            int quantidade = lerInteiroNaoNegativo(
                    "Quantidade de notas de R$ " + cedula.getValor() + " para adicionar:"
            );

            quantidades.put(cedula.getValor(), quantidade);
        }

        service.carregarNotas(quantidades);

        JOptionPane.showMessageDialog(null, "Notas carregadas com sucesso!");
    }

    public void realizarSaque() {

        if (service.limiteSaquesAtingido()) {
            JOptionPane.showMessageDialog(null, "LIMITE DE 100 RETIRADAS ATINGIDO.");
            return;
        }

        if (service.getSaldoCaixa() == 0) {
            JOptionPane.showMessageDialog(null, "NÃO HÁ MAIS NOTAS NO CAIXA.");
            return;
        }

        Integer valor = lerInteiroPositivoOuNulo("Digite o valor do saque: R$");
        if (valor == null) {
            return; // usuário cancelou
        }

        Integer codigoBanco = lerInteiroPositivoOuNulo("Digite o código do banco:");
        if (codigoBanco == null) {
            return; // usuário cancelou
        }

        try {

            Saque saque = service.realizarSaque(valor, codigoBanco);

            StringBuilder sb = new StringBuilder();
            sb.append("SAQUE REALIZADO COM SUCESSO!\n\nNotas entregues:\n");

            saque.getCedulas().entrySet()
                    .stream()
                    .sorted(Map.Entry.<Integer, Integer>comparingByKey().reversed())
                    .forEach(entry ->
                            sb.append(entry.getValue())
                                    .append(" x R$ ")
                                    .append(entry.getKey())
                                    .append("\n")
                    );

            sb.append("\nTotal: R$ ").append(saque.getValor());

            JOptionPane.showMessageDialog(null, sb.toString());

        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(
                    null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void estatistica() {

        StringBuilder sb = new StringBuilder();
        sb.append("================================\n");
        sb.append("          ESTATÍSTICA\n");
        sb.append("================================\n");

        if (service.getBancosOrdenados().isEmpty()) {
            sb.append("Nenhum saque realizado.\n");
        } else {

            for (Banco banco : service.getBancosOrdenados()) {

                sb.append("\nBanco: ").append(banco.getCodigo()).append("\n");
                sb.append("Maior saque: R$ ").append(banco.getMaiorSaque()).append("\n");
                sb.append("Menor saque: R$ ").append(banco.getMenorSaque()).append("\n");
                sb.append(String.format(
                        Locale.US, "Média dos saques: R$ %.2f%n", banco.getMediaSaques()
                ));
                sb.append("Valor total dos saques: R$ ").append(banco.getTotalSaques()).append("\n");
            }
        }

        sb.append("\nValor das sobras do caixa: R$ ").append(service.getSaldoCaixa()).append("\n");
        sb.append("Quantidade de saques realizados: ").append(service.getQuantidadeSaques()).append("\n");
        sb.append("================================");

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void mostrarNotas() {

        StringBuilder sb = new StringBuilder();
        sb.append("===== NOTAS DISPONÍVEIS =====\n");

        for (Cedula cedula : service.getCedulas()) {
            sb.append("R$ ").append(cedula.getValor())
                    .append(" -> ").append(cedula.getQuantidade())
                    .append(" nota(s)\n");
        }

        sb.append("Saldo total: R$ ").append(service.getSaldoCaixa());

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private int lerInteiroNaoNegativo(String mensagem) {

        while (true) {

            String entrada = JOptionPane.showInputDialog(mensagem);

            if (entrada == null) {
                return 0; // cancelado -> assume zero para essa cédula
            }

            try {
                int valor = Integer.parseInt(entrada.trim());

                if (valor >= 0) {
                    return valor;
                }

                JOptionPane.showMessageDialog(null, "Digite zero ou um valor positivo.");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite apenas números.");
            }
        }
    }

    private Integer lerInteiroPositivoOuNulo(String mensagem) {

        while (true) {

            String entrada = JOptionPane.showInputDialog(mensagem);

            if (entrada == null) {
                return null; // usuário cancelou a operação
            }

            try {
                int valor = Integer.parseInt(entrada.trim());

                if (valor > 0) {
                    return valor;
                }

                JOptionPane.showMessageDialog(null, "Digite um valor maior que zero.");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Digite apenas números.");
            }
        }
    }
}
