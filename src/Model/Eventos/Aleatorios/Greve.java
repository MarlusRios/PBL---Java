package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogo;
import Service.JogoService;

public class Greve extends Aleatorio {

    public Greve(){
        super (0.01);
    }

    @Override
    public void aplicarEvento(JogoService jogoService, Jogo jogo){
        jogoService.avancarSemestre(jogo);
    }
}
