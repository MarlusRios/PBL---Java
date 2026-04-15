import org.junit.*;
import static org.junit.Assert.*;
import Model.Jogador;
import Model.personagens.Professor;

public class ProfessorTest {

    //Interação na sala → Aula obrigatória

    @Test
    public void testInteracaoNaSalaAumentaConhecimento() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 5);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(100.0);
        jogador.setConhecimento(0.0);

        professor.interacao(jogador);

        assertEquals(25.0, jogador.getConhecimento(), 0.01);
    }

    @Test
    public void testInteracaoNaSalaReduzEnergia() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 5);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(100.0);

        professor.interacao(jogador);

        assertEquals(70.0, jogador.getEnergia(), 0.01);
    }

    @Test
    public void testInteracaoNaSalaComEnergiaInsuficienteAindaAplicaAula() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 5);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(10.0);
        jogador.setConhecimento(0.0);

        professor.interacao(jogador);

        assertEquals(25.0, jogador.getConhecimento(), 0.01);
        assertEquals(-20.0, jogador.getEnergia(), 0.01);
    }

    //Interação fora da sala com energia suficiente

    @Test
    public void testInteracaoForaDaSalaDefineConhecimento() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 3);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(50.0);

        professor.interacao(jogador);

        assertEquals(5.0, jogador.getConhecimento(), 0.01);
    }

    @Test
    public void testInteracaoForaDaSalaReduzEnergia() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 3);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(50.0);

        professor.interacao(jogador);

        assertTrue(jogador.getEnergia() <= 45.0);
    }

    @Test
    public void testInteracaoForaDaSalaAumentaMotivacao() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 3);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(50.0);
        jogador.setMotivacao(40.0);

        professor.interacao(jogador);

        assertTrue(jogador.getMotivacao() >= 25.0);
    }

    //Interação fora da sala sem energia suficiente

    @Test
    public void testInteracaoForaDaSalaSemEnergiaNaoAltera() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 3);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(4.0);
        double conhecimentoAntes = jogador.getConhecimento();
        double motivacaoAntes = jogador.getMotivacao();

        professor.interacao(jogador);

        assertEquals(conhecimentoAntes, jogador.getConhecimento(), 0.01);
        assertEquals(4.0, jogador.getEnergia(), 0.01);
        assertEquals(motivacaoAntes, jogador.getMotivacao(), 0.01);
    }

    @Test
    public void testInteracaoForaDaSalaEnergiaExatamente5Permitida() {
        Professor professor = new Professor(1, "Felipe", 2, 15, 3);
        Jogador jogador = new Jogador("Player", 0, 0);
        jogador.setEnergia(5.0);

        professor.interacao(jogador);

        assertEquals(5.0, jogador.getConhecimento(), 0.01);
        assertTrue(jogador.getEnergia() <= 0.0);
    }
}
