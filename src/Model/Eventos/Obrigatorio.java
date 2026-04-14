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

    public Obrigatorio(int semanaDeOcorrencia){
        this.semanaDeOcorrencia = semanaDeOcorrencia;
    }

    public Obrigatorio() {}

    public boolean deveOcorrer(Jogo jogo) {
        return jogo.getSemana() == semanaDeOcorrencia && jogo.getSemestre() == semestreDeOcorrencia;
    }

    public void aplicarEvento(Jogador jogador, Jogo jogo){}

    public void aplicarEvento(Jogador jogador){}

    public int getSemanaDeOcorrencia() { return semanaDeOcorrencia; }
    public int getSemestreDeOcorrencia() { return semestreDeOcorrencia; }
}
