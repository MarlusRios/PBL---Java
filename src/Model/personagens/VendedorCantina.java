package Model.personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.FilaGrande;
import Model.Jogador;
import Model.Jogo;

public class VendedorCantina extends Interagiveis {

    Aleatorio filaGrande = new FilaGrande();
    public VendedorCantina (int id, int posx, int posy){
        super(id, "vendedor", posx, posy, 3);
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
