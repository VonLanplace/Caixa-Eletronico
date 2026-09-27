package edu.fatec.model;

import java.util.ArrayList;
import java.util.List;

public class Banco {

    private final int codigo;
    private final List<Saque> saques = new ArrayList<>();

    public Banco(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void adicionarSaque(Saque saque) {
        saques.add(saque);
    }

    public List<Saque> getSaques() {
        return saques;
    }

    public int getMaiorSaque() {
        return saques.stream()
                .mapToInt(Saque::getValor)
                .max()
                .orElse(0);
    }

    public int getMenorSaque() {
        return saques.stream()
                .mapToInt(Saque::getValor)
                .min()
                .orElse(0);
    }

    public double getMediaSaques() {
        if (saques.isEmpty()) {
            return 0;
        }

        return saques.stream()
                .mapToInt(Saque::getValor)
                .average()
                .orElse(0);
    }

    public int getTotalSaques() {
        return saques.stream()
                .mapToInt(Saque::getValor)
                .sum();
    }
}
