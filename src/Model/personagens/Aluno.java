package Model.personagens;

import Model.Jogador;
public class Aluno extends Npc {

    public Aluno (int id,String nome, int posx, int posy, int loc){
        super (id, nome, posx, posy, loc);
    }

    @Override
    public void interacao(Jogador jogador){
        if (jogador.getEnergia() >= 2) {
            jogador.setConhecimento(jogador.getConhecimento() + 2.0);
            jogador.setEnergia(jogador.getEnergia() - 2.0);
            jogador.setMotivacao(jogador.getMotivacao() + 10.0);
        }
    }

    public void ensinar(Jogador jogador){
        if (jogador.getEnergia() >= 2) {
            jogador.setConhecimento(jogador.getConhecimento() + 10.0);
            jogador.setMotivacao(jogador.getMotivacao() + 20.0);
            jogador.setEnergia(jogador.getEnergia() - 4.0);
        }
    }
}
