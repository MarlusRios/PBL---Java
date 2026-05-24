package Testes;

import org.junit.*;
import static org.junit.Assert.*;
import Model.Jogador;
import Model.personagens.Cachorro;

public class CachorroTest {

    // Interação com energia suficiente

    @Test
    public void testInteracaoAumentaMotivacao() {
        Cachorro cachorro = new Cachorro(1, 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(50.0);
        jogador.setMotivacao(50.0);

        cachorro.interacao(jogador);

        assertTrue(jogador.getMotivacao() >= 40.0);
    }

    @Test
    public void testInteracaoReduzEnergia() {
        Cachorro cachorro = new Cachorro(1, 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(50.0);

        cachorro.interacao(jogador);

        assertTrue(jogador.getEnergia() <= 49.8);
    }

    @Test
    public void testInteracaoEnergiaExatamente02Permitida() {
        Cachorro cachorro = new Cachorro(1, 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(0.2);
        jogador.setMotivacao(50.0);

        cachorro.interacao(jogador);

        assertTrue(jogador.getMotivacao() >= 40.0);
    }

    // Interação sem energia suficiente

    @Test
    public void testInteracaoSemEnergiaNaoAlteraMotivacao() {
        Cachorro cachorro = new Cachorro(1, 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(0.0);
        double motivacaoAntes = jogador.getMotivacao();

        cachorro.interacao(jogador);

        assertEquals(motivacaoAntes, jogador.getMotivacao(), 0.01);
    }

    @Test
    public void testInteracaoSemEnergiaNaoAlteraEnergia() {
        Cachorro cachorro = new Cachorro(1, 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(0.1);

        cachorro.interacao(jogador);

        assertEquals(0.1, jogador.getEnergia(), 0.01);
    }

    // Mordida reduz saúde quando dispara
    // Testamos os limites do jogador (saude nunca negativa)

    @Test
    public void testSaudeNuncaNegativa() {
        Cachorro cachorro = new Cachorro(1, 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setSaude(0);
        jogador.setEnergia(50.0);

        cachorro.interacao(jogador);

        assertTrue(jogador.getSaude() >= 0);
    }
}
