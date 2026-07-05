package Model.Eventos;

import Model.Jogador;
import Model.Jogo;

public abstract class Obrigatorio {
    private int semanaDeOcorrencia;
    private int semestreDeOcorrencia;

    public Obrigatorio(int semanaDeOcorrencia, int semestreDeOcorrencia) {
        this.semanaDeOcorrencia = semanaDeOcorrencia;
        this.semestreDeOcorrencia = semestreDeOcorrencia;
    }

    //metodo de verificação se o tempo do evento vai acontecer
    public boolean deveOcorrer(Jogo jogo) {
        return jogo.getSemana() == semanaDeOcorrencia && jogo.getSemestre() == semestreDeOcorrencia;
    }

    // metodos a serem subscritos por herança/polimorfismo
    public void aplicarEvento(Jogador jogador, Jogo jogo){}

    public boolean aplicarEvento(Jogador jogador){
        return false;
    }

    public Obrigatorio(int semanaDeOcorrencia){
        this.semanaDeOcorrencia = semanaDeOcorrencia;
    }

    public Obrigatorio() {}

    //getters e setters
    public int getSemanaDeOcorrencia() { return semanaDeOcorrencia; }
    public int getSemestreDeOcorrencia() { return semestreDeOcorrencia; }
}
