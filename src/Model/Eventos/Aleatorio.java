package Model.Eventos;

import Model.Jogador;

public abstract class Aleatorio {
    private double probabilidade;

    public Aleatorio (double probabilidade){
        this.probabilidade = probabilidade;
    }

    public double getProbabilidade(){ return probabilidade; }

    public abstract void aplicarEvento(Jogador jogador);

    public boolean acontece(){
        return Math.random()<probabilidade;
    }
}
