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

    //metodo para verificar o estado da conversa com o professor e se está no dia de fazer prova
    public int conversar(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        if (jogoService.fazerProva(jogo)){//verifica se é dia de prova
            return -1;
        }
        return jogadorService.interagirInt(jogador, new Professor()); // retorna o estado da conversa em inteiro
    }

    // metodo para verificar se luiza passou na prova
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
