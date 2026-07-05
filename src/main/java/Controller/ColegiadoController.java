package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.Personagens.Maeli;
import Repository.JogoRepository;
import Service.JogadorService;
import Service.JogoService;

public class ColegiadoController {
    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    public void Help(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        jogadorService.interagir(jogador, new Maeli());
    }
}
