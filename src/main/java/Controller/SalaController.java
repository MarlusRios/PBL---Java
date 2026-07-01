package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.personagens.Professor;
import Service.JogadorService;
import Service.JogoService;

public class SalaController {

    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    public void Conversar(Jogo jogo){
        Jogador jogador = jogo.getPlayer();
        jogadorService.interagir(jogador, new Professor());
    }
}
