package Controller;

import Model.Jogo;
import Repository.JogoRepository;
import Service.JogoService;

import java.util.Collection;

public class JogoController {

    private final JogoRepository jogoRepository = new JogoRepository();
    private final JogoService jogoService = new JogoService();

    //metodo que adiciona uma nova partida nos saves
    public Jogo novaPartida(String id) {
        jogoRepository.inicializar();

        Jogo jogo = jogoService.criarJogo(id);
        jogoRepository.salvar(jogo);
        JogoRepository.setJogoAtual(jogo);
        return jogo;
    }

    //metodo que vasculha os saves e define por id
    public Jogo carregarPartida(String id) {
        jogoRepository.inicializar();
        Jogo jogo = jogoRepository.buscarPorId(id);
        if (jogo == null){
            System.out.println("criando outra partida");
            jogo = novaPartida(id);
        }else{
            JogoRepository.setJogoAtual(jogo);
        }
        return jogo;
    }

    //metodo que salva a partida
    public void salvarPartida(Jogo jogo) {
        jogoRepository.inicializar();
        jogoRepository.salvar(jogo);
    }

    //metodo que deleta a partida
    public void deletarPartida(String id) {
        jogoRepository.inicializar();
        jogoRepository.deletar(id);
    }

    //listar todos os saves
    public Collection<Jogo> listarPartidas() {
        jogoRepository.inicializar();
        return jogoRepository.buscarTodos();
    }
}