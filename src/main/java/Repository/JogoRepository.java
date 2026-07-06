package Repository;

import Model.Jogo;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

public class JogoRepository {

    //private static final String CAMINHO = System.getProperty("user.dir")  + "/Bixo_Quest/Saves/saves.ser";
    private static final String CAMINHO = System.getProperty("user.dir") + "/Saves/saves.ser";
    private HashMap<String, Jogo> jogos = new HashMap<>();
    private static Jogo jogoAtual;

    // carrega os saves do arquivo ao iniciar
    public void inicializar() {
        try (ObjectInputStream inputData = new ObjectInputStream(new FileInputStream(CAMINHO))) {
            jogos = (HashMap<String, Jogo>) inputData.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("arquivo não encontrado");
        } catch (IOException | ClassNotFoundException except) {
            except.printStackTrace();
        }
    }

    // salva ou atualiza um jogo e persiste no arquivo
    public void salvar(Jogo jogo) {
        jogos.put(jogo.getId(), jogo);
        persistir();
    }

    // busca um jogo pelo id
    public Jogo buscarPorId(String id) {
        return jogos.get(id);
    }

    // retorna todos os jogos
    public Collection<Jogo> buscarTodos() {
        return jogos.values();
    }

    // deleta um jogo pelo id e persiste no arquivo
    public void deletar(String id) {
        jogos.remove(id);
        persistir();
    }

    // verifica se um jogo existe
    public boolean existe(String id) {
        return jogos.containsKey(id);
    }

    // grava o HashMap no arquivo
    private void persistir() {
        try (ObjectOutputStream outputData = new ObjectOutputStream(new FileOutputStream(CAMINHO))) {
            outputData.writeObject(jogos);
        } catch (IOException except) {
            except.printStackTrace(); // joga no console o caminho dos erros
        }
    }

    public static void setJogoAtual(Jogo jogoAtual) {
        JogoRepository.jogoAtual = jogoAtual;
    }

    public static Jogo getJogoAtual() {
        return jogoAtual;
    }

    public String proximoId() {
        inicializar();
        return String.valueOf(jogos.size() + 1);
    }
}