package Model.personagens;

import Model.Jogador;
import Model.personagens.Npc;

public class Maeli extends Npc {

    public Maeli (int id, String nome, int posx, int posy, int loc){
        super(id, nome, posx, posy, loc);
    }

    @Override
    public void interacao (Jogador jogador){
        //printar o tutorial//
    }

}