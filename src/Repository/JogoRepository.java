package Repository;

import Model.Jogo;
import java.util.HashMap;
import java.util.Collection;

public class JogoRepository {

    private HashMap<String, Jogo> jogos = new HashMap<>();

    // salva ou atualiza um jogo
    public void salvar(Jogo jogo) {
        jogos.put(jogo.getId(), jogo);
    }

    // busca um jogo pelo id
    public Jogo buscarPorId(String id) {
        return jogos.get(id);
    }

    // retorna todos os jogos
    public Collection<Jogo> buscarTodos() {
        return jogos.values();
    }

    // deleta um jogo pelo id
    public void deletar(String id) {
        jogos.remove(id);
    }

    // verifica se um jogo existe
    public boolean existe(String id) {
        return jogos.containsKey(id);
    }
}