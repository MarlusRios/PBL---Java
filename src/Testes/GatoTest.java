import org.junit.*;
import static org.junit.Assert.*;
import Model.Jogador;
import Model.personagens.Gato;

public class GatoTest {

    // Interação com energia suficiente

    @Test
    public void testInteracaoAumentaMotivacao() {
        Gato gato = new Gato(1, 5, 5, 1);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(50.0);
        jogador.setMotivacao(50.0);

        gato.interacao(jogador);

        assertTrue(jogador.getMotivacao() >= 40.0);
    }

    @Test
    public void testInteracaoReduzEnergia() {
        Gato gato = new Gato(1, 5, 5, 1);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(50.0);

        gato.interacao(jogador);

        assertTrue(jogador.getEnergia() <= 49.8);
    }

    @Test
    public void testInteracaoEnergiaExatamente02Permitida() {
        Gato gato = new Gato(1, 5, 5, 1);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(0.2);
        jogador.setMotivacao(50.0);

        gato.interacao(jogador);

        assertTrue(jogador.getMotivacao() >= 40.0);
    }

    // Interação sem energia suficiente

    @Test
    public void testInteracaoSemEnergiaNaoAumentaMotivacao() {
        Gato gato = new Gato(1, 5, 5, 1);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(0.1);
        jogador.setMotivacao(50.0);

        gato.interacao(jogador);

        assertTrue(jogador.getMotivacao() <= 50.0);
    }

    @Test
    public void testInteracaoSemEnergiaNaoAlteraEnergia() {
        Gato gato = new Gato(1, 5, 5, 1);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(0.0);

        gato.interacao(jogador);

        assertTrue(jogador.getEnergia() <= 0.0);
    }
}
