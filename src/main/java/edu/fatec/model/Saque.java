package edu.fatec.model;

import java.util.Map;

public class Saque {

    private final int valor;
    private final int codigoBanco;
    private final Map<Integer, Integer> cedulas;

    public Saque(int valor, int codigoBanco, Map<Integer, Integer> cedulas) {
        this.valor = valor;
        this.codigoBanco = codigoBanco;
        this.cedulas = cedulas;
    }

    public int getValor() {
        return valor;
    }

    public int getCodigoBanco() {
        return codigoBanco;
    }

    public Map<Integer, Integer> getCedulas() {
        return cedulas;
    }
}
