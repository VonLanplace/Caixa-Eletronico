package edu.fatec.model;

public class Cedula {

    private final int valor;
    private int quantidade;

    public Cedula(int valor, int quantidade) {
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public int getValor() {
        return valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void adicionar(int quantidade) {
        this.quantidade += quantidade;
    }

    public void retirar() {
        if (quantidade > 0) {
            quantidade--;
        }
    }

    public int getTotal() {
        return valor * quantidade;
    }

    @Override
    public String toString() {
        return "R$ " + valor + " - " + quantidade + " nota(s)";
    }
}
