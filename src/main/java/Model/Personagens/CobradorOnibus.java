package Model.Personagens;

import Model.Jogo;
import Service.JogoService;

public class CobradorOnibus extends Interagiveis {

    public CobradorOnibus(){
        super();
    }

    @Override //interação para terminar o dia de jogo
    public void interacao(Jogo jogo, JogoService jogoService){
        jogoService.encerrarDia(jogo);
    }
}
