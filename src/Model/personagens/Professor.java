package Model.personagens;

import Model.Jogador;

public class Professor extends Npc{
    private String materia;

    public Professor (int id, String nome, int posx, int posy, int loc, String materia){
        super(id, nome, posx, posy, loc);
        this.materia = materia;
    }

    @Override
    public void interacao (Jogador jogador){
        if (jogador.getEnergia() >= 5) {
            jogador.setConhecimento(jogador.getConhecimento() + 5.0);
            jogador.setEnergia(jogador.getEnergia() - 5.0);
            jogador.setMotivacao(jogador.getMotivacao() + 5.0);
        }
    }

}
