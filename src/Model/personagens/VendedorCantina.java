package Model.personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.FilaGrande;
import Model.Jogador;
import Model.Jogo;
import Model.personagens.Npc;

public class VendedorCantina extends Npc {

    Aleatorio filaGrande = new FilaGrande();
    public VendedorCantina (int id, String nome, int posx, int posy, int loc){
        super(id, nome, posx, posy, loc);
    }

    @Override
    public void interacao (Jogador jogador, Jogo jogo){
        jogador.setSaude(jogador.getSaude() + 10);
        jogador.setEnergia(jogador.getEnergia() + 10);
        jogador.setDinheiro(jogador.getDinheiro() - 20);
        if(filaGrande.acontece()){
            filaGrande.aplicarEvento(jogador, jogo);
        }
    }

}
