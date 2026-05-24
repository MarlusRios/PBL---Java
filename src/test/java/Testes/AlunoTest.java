package Testes;

import org.junit.*;
import static org.junit.Assert.*;
import Model.Jogador;
import Model.personagens.Aluno;

public class AlunoTest {

    // Interação com energia suficiente

    @Test
    public void testInteracaoAumentaConhecimento() {
        Aluno aluno = new Aluno(1, "Nando", 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(50.0);
        double conhecimentoAntes = jogador.getConhecimento();

        aluno.interacao(jogador);

        assertTrue(jogador.getConhecimento() >= conhecimentoAntes + 2.0);
    }

    @Test
    public void testInteracaoReduzEnergia() {
        Aluno aluno = new Aluno(1, "Nando", 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(50.0);

        aluno.interacao(jogador);

        assertTrue(jogador.getEnergia() <= 48.0);
    }

    @Test
    public void testInteracaoAumentaMotivacao() {
        Aluno aluno = new Aluno(1, "Nando", 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(50.0);
        jogador.setMotivacao(30.0);

        aluno.interacao(jogador);

        assertTrue(jogador.getMotivacao() >= 40.0);
    }

    @Test
    public void testInteracaoEnergiaExatamente2Permitida() {
        Aluno aluno = new Aluno(1, "Nando", 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(2.0);
        double conhecimentoAntes = jogador.getConhecimento();

        aluno.interacao(jogador);

        assertTrue("Energia == 2 deve permitir interação",
                jogador.getConhecimento() >= conhecimentoAntes + 2.0);
    }

    // Interação sem energia suficiente

    @Test
    public void testInteracaoSemEnergiaNaoAlteraConhecimento() {
        Aluno aluno = new Aluno(1, "Nando", 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(1.0);
        double conhecimentoAntes = jogador.getConhecimento();

        aluno.interacao(jogador);

        assertEquals(conhecimentoAntes, jogador.getConhecimento(), 0.01);
    }

    @Test
    public void testInteracaoSemEnergiaNaoAlteraEnergia() {
        Aluno aluno = new Aluno(1, "Nando", 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(1.5);

        aluno.interacao(jogador);

        assertEquals(1.5, jogador.getEnergia(), 0.01);
    }

    @Test
    public void testInteracaoSemEnergiaNaoAlteraMotivacao() {
        Aluno aluno = new Aluno(1, "Nando", 5, 5, 1);
        Jogador jogador = new Jogador();
        jogador.setEnergia(0.0);
        double motivacaoAntes = jogador.getMotivacao();

        aluno.interacao(jogador);

        assertEquals(motivacaoAntes, jogador.getMotivacao(), 0.01);
    }
}
