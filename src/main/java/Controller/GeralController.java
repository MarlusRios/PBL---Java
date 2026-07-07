package Controller;

import Model.Jogo;
import Repository.JogoRepository;
import Service.JogoService;

//classe geral para verificação e atualização dos estados do jogo
public class GeralController {
    private final JogoService jogoService = new JogoService();
    private final JogoController jogoController = new JogoController();

    //metodo que atualiza os atributos do jogador e verifica fim do dia por tempo, saude ou motivação
    public boolean Atualizador(){
        Jogo jogo = JogoRepository.getJogoAtual();
        boolean choose = jogoService.atualizarCicloJogo(jogo);
        if (choose){
            jogoController.salvarPartida(jogo);
            return true;
        }
        return false;
    }

    //metodo para encerrar o dia
    public void EncerrarDia(){
        Jogo jogo = JogoRepository.getJogoAtual();
        jogoService.encerrarDia(jogo);
        jogoController.salvarPartida(jogo);
    }

    public void MudarTempo(){
        Relogio.incrementarTempo();
    }

    //metodo que verifica a situação do estudante, como disceente, formado, ou virou cachorro
    public int Formatura(){
        Jogo jogo = JogoRepository.getJogoAtual();
        return jogoService.verificarFormatura(jogo);
    }

    //metodo para encerrar a partida e deletar o save
    public void EncerrarJogo(){
        Jogo jogo = JogoRepository.getJogoAtual();
        jogoService.encerrarJogo(jogo);
        jogoController.deletarPartida(jogo.getId());
    }

}
