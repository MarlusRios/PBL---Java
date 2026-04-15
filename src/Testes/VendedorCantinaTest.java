import org.junit.*;
import static org.junit.Assert.*;
import Model.Jogador;
import Model.Jogo;
import Model.maps.Map;
import Model.personagens.VendedorCantina;

public class VendedorCantinaTest {

    private Jogador novoJogador() {
        return new Jogador("Player", 0, 0);
    }

    private Jogo novoJogo() {
        return new Jogo("id1", "Player", 0, 0, new Map());
    }

    //Efeitos base garantidos

    @Test
    public void testInteracaoReduzDinheiro() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        Jogador jogador = novoJogador();
        Jogo jogo = novoJogo();

        vendedor.interacao(jogador, jogo);

        assertEquals(280.0, jogador.getDinheiro(), 0.01);
    }

    @Test
    public void testInteracaoAumentaEnergia() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        Jogador jogador = novoJogador();
        Jogo jogo = novoJogo();

        vendedor.interacao(jogador, jogo);

        assertEquals(110.0, jogador.getEnergia(), 0.01);
    }

    @Test
    public void testInteracaoSaudeCapadaEm100() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        Jogador jogador = novoJogador();
        Jogo jogo = novoJogo();

        vendedor.interacao(jogador, jogo);

        assertEquals(100, jogador.getSaude());
    }

    @Test
    public void testInteracaoSaudeAbaixoDoMaxAumenta() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        Jogador jogador = novoJogador();
        jogador.setSaude(80);
        Jogo jogo = novoJogo();

        vendedor.interacao(jogador, jogo);

        assertEquals(90, jogador.getSaude());
    }

    @Test
    public void testInteracaoMotivacaoCapadaEm100() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        Jogador jogador = novoJogador();
        Jogo jogo = novoJogo();

        vendedor.interacao(jogador, jogo);

        assertEquals(100.0, jogador.getMotivacao(), 0.01);
    }

    @Test
    public void testInteracaoMotivacaoAumentaQuandoBaixa() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        Jogador jogador = novoJogador();
        jogador.setMotivacao(40.0);
        Jogo jogo = novoJogo();

        vendedor.interacao(jogador, jogo);

        assertTrue(jogador.getMotivacao() >= 40.0);
    }

    //Construtor

    @Test
    public void testGetNome() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        assertEquals("vendedor", vendedor.getNome());
    }

    @Test
    public void testGetLoc() {
        VendedorCantina vendedor = new VendedorCantina(1, 5, 5);
        assertEquals(3, vendedor.getLoc());
    }
}
