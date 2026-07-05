package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.Personagens.Aluno;
import Repository.JogoRepository;
import Service.JogadorService;
import Service.JogoService;

public class LabController {
    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    public void baterPapo(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        jogadorService.interagirInt(jogador, new Aluno());
    }
}
