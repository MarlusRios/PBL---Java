package Model.personagens;

import Model.Jogador;

public class Professor extends Interagiveis {

    public Professor (int id, String nome, int posx, int posy, int loc){
        super(id, nome, posx, posy, loc);
    }

    @Override
    public void interacao (Jogador jogador){
        if (jogador.getEnergia() >= 5) {
            jogador.setConhecimento(5.0);
            jogador.setEnergia(jogador.getEnergia() - 5.0);
            jogador.setMotivacao(jogador.getMotivacao() + 5.0);
        }
    }

}
