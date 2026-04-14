package Model.personagens;

import Model.Jogo;
import Service.JogoService;

public class CobradorOnibus extends Interagiveis {

    public CobradorOnibus(int id, int posx, int posy){
        super(id, "Cobrador", posx, posy, 1);
    }

    @Override
    public void interacao(Jogo jogo, JogoService jogoService){
        jogoService.encerrarDia(jogo);
    }
}
