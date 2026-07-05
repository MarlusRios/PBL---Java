package Model.Personagens;

import Model.Jogador;
import Model.Jogo;
import Service.JogoService;

public abstract class Interagiveis {
    //metodos que serão sobrescritos
    public void interacao(Jogo jogo, JogoService jogoService){}

    public  void interacao (Jogador jogador){}

    public int interacaoInt(Jogador jogador, Jogo jogo){return 0;}

    public  int interacaoInt(Jogador jogador){return 0;}



}
