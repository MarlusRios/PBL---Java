package Model.personagens;

import Model.Jogador;
public class Gato extends Npc {

    public Gato (int id, String nome, int posx, int posy, int loc){
        super (id, nome, posx, posy, loc);
    }

    @Override
    public void interacao(Jogador jogador) {
        if (jogador.getEnergia() >= 0.2) {
            jogador.setMotivacao(jogador.getMotivacao() + 0.5);
            jogador.setEnergia(jogador.getEnergia() - 0.2);
        }
    }

    public void arranhao (Jogador jogador){
        if (jogador.getEnergia() >= 0.2) {
            jogador.setMotivacao(jogador.getMotivacao() - 10);
            jogador.setSaude(jogador.getSaude() - 5);
            jogador.setEnergia(jogador.getEnergia()- 0.2);
        }
    }
}
