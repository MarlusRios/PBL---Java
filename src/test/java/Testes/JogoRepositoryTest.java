package Testes;

import Model.Jogo;
import Model.maps.Map;
import Repository.JogoRepository;
import org.junit.Test;
import static org.junit.Assert.*;

public class JogoRepositoryTest {

    @Test
    public void testSalvarECarregar() {
        JogoRepository repository = new JogoRepository();
        Jogo jogo = new Jogo("1", new Map());

        repository.salvar(jogo);
        Jogo carregado = repository.buscarPorId("1");

        assertNotNull(carregado);
        assertEquals("1", carregado.getId());
        assertEquals(1, carregado.getSemana());
        assertEquals(1, carregado.getSemestre());
    }

    @Test
    public void testBuscarIdInexistente() {
        JogoRepository repository = new JogoRepository();

        Jogo jogo = repository.buscarPorId("999");

        assertNull(jogo);
    }

    @Test
    public void testDeletar() {
        JogoRepository repository = new JogoRepository();
        Jogo jogo = new Jogo("1", new Map());

        repository.salvar(jogo);
        repository.deletar("1");

        assertFalse(repository.existe("1"));
    }

    @Test
    public void testProximoId() {
        JogoRepository repository = new JogoRepository();
        repository.salvar(new Jogo("1", new Map()));
        repository.salvar(new Jogo("2", new Map()));

        assertEquals("3", repository.proximoId());
    }
}