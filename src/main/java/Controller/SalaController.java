package Controller;

import Model.Jogador;
import Model.Jogo;
import Repository.JogoRepository;
import Model.Personagens.Professor;
import Service.JogadorService;
import Service.JogoService;

public class SalaController {

    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    public int conversar(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        if (jogoService.fazerProva(jogo)){
            return -1;
        }
        return jogadorService.interagirInt(jogador, new Professor());
    }

    public boolean passou(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        if(jogador.getDesempenho()>= 7){
            return true;
        }else{
            return false;
        }
    }
}
