package Model.personagens;

import Model.Jogador;
import Model.Jogo;
import Service.JogoService;

public abstract class Interagiveis {
    //metodos que serão sobrescritos
    public void interacao(Jogo jogo, JogoService jogoService){}

    public  void interacao (Jogador jogador){}

    public void interacao (Jogador jogador, Jogo jogo){}

}
