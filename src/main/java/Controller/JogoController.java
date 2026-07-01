package Controller;

import Model.Jogo;
import Repository.JogoRepository;
import Service.JogoService;

import java.util.Collection;

public class JogoController {

    private final JogoRepository jogoRepository = new JogoRepository();
    private final JogoService jogoService = new JogoService();

    public Jogo novaPartida(String id) {
        Jogo jogo = jogoService.criarJogo(id);
        jogoRepository.salvar(jogo);
        return jogo;
    }

    public Jogo carregarPartida(String id) {
        jogoRepository.inicializar();
        Jogo jogo = jogoRepository.buscarPorId(id);
        if (jogo == null){
            System.out.println("criando outra partida");
            jogo = novaPartida(id);
        }
        return jogo;
    }

    public void salvarPartida(Jogo jogo) {
        jogoRepository.salvar(jogo);
    }

    public void deletarPartida(String id) {
        jogoRepository.deletar(id);
    }

    public Collection<Jogo> listarPartidas() {
        jogoRepository.inicializar();
        return jogoRepository.buscarTodos();
    }

    public void rodarJogo(Jogo jogo) throws InterruptedException {
        jogoService.rodarJogo(jogo);
    }
}