package Model.personagens;

import Model.Jogador;
import Model.personagens.Npc;

public class VendedorCantina extends Npc {

    public VendedorCantina (int id, String nome, int posx, int posy, int loc){
        super(id, nome, posx, posy, loc);
    }

    @Override
    public void interacao (Jogador jogador){

    }

}
