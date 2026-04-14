package Model.personagens;

import Model.Jogador;

public class Maeli extends Interagiveis {

    public Maeli (int id, int posx, int posy){
        super(id, "Maeli", posx, posy, 8);
    }

    @Override
    public void interacao (Jogador jogador){
        //printar o tutorial
    }

}