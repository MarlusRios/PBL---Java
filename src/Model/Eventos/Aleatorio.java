package Model.Eventos;

import Model.Jogador;
import Model.Jogo;

public abstract class Aleatorio {
    private double probabilidade;

    public Aleatorio (double probabilidade){
        this.probabilidade = probabilidade;
    }

    public double getProbabilidade(){ return probabilidade; }

    public void aplicarEvento(Jogador jogador){}

    public void aplicarEvento(Jogador jogador, Jogo jogo){}

    public boolean acontece(){
        return Math.random()<probabilidade;
    }
}
