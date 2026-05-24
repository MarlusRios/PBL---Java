package Model.Eventos;

import Model.Jogador;
import Model.Jogo;
import Service.JogoService;

public abstract class Aleatorio {
    private double probabilidade;

    public Aleatorio (double probabilidade){
        this.probabilidade = probabilidade;
    }

    //getter de probabilidade
    public double getProbabilidade(){ return probabilidade; }

    //metodos a serem subscritos por herança/polimorfismo
    public void aplicarEvento(Jogador jogador){}

    public void aplicarEvento(JogoService jogoService,Jogo jogo){}

    public void aplicarEvento(Jogador jogador, Jogo jogo){}

    //metodo de verificação se o evento vai acontecer
    public boolean acontece(){
        return Math.random()<probabilidade;
    }
}
