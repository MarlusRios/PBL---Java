package Testes;

import org.junit.*;
import static org.junit.Assert.*;
import Model.Jogador;
import Model.Jogo;
import Model.maps.Map;
import Service.JogoService;
import Service.MapService;
import Service.InteragiveisService;

public class JogoServiceTest {

    private JogoService jogoService = new JogoService();

    private Jogo novoJogo() {
        return new Jogo("id1", new Map());
    }

    // verificarFimDoDia

    @Test
    public void testVerificarFimDoDiaFalseAntesDas19() {
        Jogo jogo = novoJogo();
        jogo.setTime(18.9);
        assertFalse(jogoService.verificarFimDoDia(jogo));
    }

    @Test
    public void testVerificarFimDoDiaTrueExatamente19() {
        Jogo jogo = novoJogo();
        jogo.setTime(19.0);
        assertTrue(jogoService.verificarFimDoDia(jogo));
    }

    @Test
    public void testVerificarFimDoDiaTrueApos19() {
        Jogo jogo = novoJogo();
        jogo.setTime(20.5);
        assertTrue(jogoService.verificarFimDoDia(jogo));
    }

    // verificarFormatura

    @Test
    public void testVerificarFormaturaFalseAndamento5() {
        Jogo jogo = novoJogo();
        jogo.getPlayer().setAndamento(5);
        assertFalse(jogoService.verificarFormatura(jogo)); // > 5, não >= 5
    }

    @Test
    public void testVerificarFormaturaFalseAndamentoBaixo() {
        Jogo jogo = novoJogo();
        jogo.getPlayer().setAndamento(4);
        assertFalse(jogoService.verificarFormatura(jogo));
    }

    @Test
    public void testVerificarFormaturaTrueAndamento6() {
        Jogo jogo = novoJogo();
        jogo.getPlayer().setAndamento(6);
        assertTrue(jogoService.verificarFormatura(jogo));
    }

    // fimDoDia

    @Test
    public void testFimDoDiaResetaEnergia() {
        Jogo jogo = novoJogo();
        jogo.getPlayer().setEnergia(30.0);

        jogoService.fimDoDia(jogo.getPlayer());

        assertEquals(100.0, jogo.getPlayer().getEnergia(), 0.01);
    }

    // avancarSemana

    @Test
    public void testAvancarSemanaIncrementaSemana() {
        Jogo jogo = novoJogo();
        jogo.setSemana(2);

        jogoService.avancarSemana(jogo);

        assertEquals(3, jogo.getSemana());
    }

    @Test
    public void testAvancarSemanaSemana4DefinExamTime() {
        Jogo jogo = novoJogo();
        jogo.setSemana(4);

        jogoService.avancarSemana(jogo);

        assertTrue(jogo.isExamTime());
        assertEquals(5, jogo.getSemana());
    }

    @Test
    public void testAvancarSemanaSemana8DefinExamTime() {
        Jogo jogo = novoJogo();
        jogo.setSemana(8);

        jogoService.avancarSemana(jogo);

        assertTrue(jogo.isExamTime());
    }

    @Test
    public void testAvancarSemanaSemana8AvancarSemestre() {
        Jogo jogo = novoJogo();
        jogo.setSemana(8);
        jogo.setSemestre(1);
        jogo.getPlayer().setDesempenho(8.0);

        jogoService.avancarSemana(jogo);

        assertEquals(1, jogo.getSemana());
        assertEquals(2, jogo.getSemestre());
    }

    @Test
    public void testAvancarSemanaForaDeSemanaExamNaoTemExamTime() {
        Jogo jogo = novoJogo();
        jogo.setSemana(3);
        jogo.setExamTime(true);

        jogoService.avancarSemana(jogo);

        assertFalse(jogo.isExamTime());
    }

    // avancarSemestre

    @Test
    public void testAvancarSemestreComDesempenhoSuficienteIncrementa() {
        Jogo jogo = novoJogo();
        jogo.setSemestre(1);
        jogo.setSemana(5);
        jogo.getPlayer().setDesempenho(7.0);
        jogo.getPlayer().setAndamento(1);

        jogoService.avancarSemestre(jogo);

        assertEquals(2, jogo.getSemestre());
        assertEquals(2, jogo.getPlayer().getAndamento());
        assertEquals(1, jogo.getSemana());
    }

    @Test
    public void testAvancarSemestreComDesempenhoInsuficienteNaoIncrementaAndamento() {
        Jogo jogo = novoJogo();
        jogo.setSemestre(1);
        jogo.getPlayer().setDesempenho(5.0);
        jogo.getPlayer().setAndamento(1);

        jogoService.avancarSemestre(jogo);

        assertEquals(2, jogo.getSemestre());
        assertEquals(1, jogo.getPlayer().getAndamento());
        assertEquals(1, jogo.getSemana());
    }

    @Test
    public void testAvancarSemestreResetaAtributos() {
        Jogo jogo = novoJogo();
        jogo.setSemestre(1);
        jogo.getPlayer().setEnergia(30.0);
        jogo.getPlayer().setMotivacao(20.0);
        jogo.getPlayer().setSaude(50);
        jogo.getPlayer().setDesempenho(8.0);

        jogoService.avancarSemestre(jogo);

        assertEquals(100.0, jogo.getPlayer().getEnergia(), 0.01);
        assertEquals(100.0, jogo.getPlayer().getMotivacao(), 0.01);
        assertEquals(100, jogo.getPlayer().getSaude());
    }

    @Test
    public void testAvancarSemestreEncerraJogoQuandoAndamento5() {
        Jogo jogo = novoJogo();
        jogo.setSemestre(5);
        jogo.getPlayer().setAndamento(5);

        jogoService.avancarSemestre(jogo);

        assertEquals(6, jogo.getSemestre());
    }

    @Test
    public void testAvancarSemestreAdicionaDinheiroAoResetar() {
        Jogo jogo = novoJogo();
        jogo.setSemestre(1);
        jogo.getPlayer().setDinheiro(100.0);
        jogo.getPlayer().setDesempenho(8.0);

        jogoService.avancarSemestre(jogo);

        assertEquals(400.0, jogo.getPlayer().getDinheiro(), 0.01); // 100 + 300
    }

    // encerrarDia

    @Test
    public void testEncerrarDiaResetaHorario() {
        Jogo jogo = novoJogo();
        jogo.setTime(22.0);

        jogoService.encerrarDia(jogo);

        assertEquals(7.0, jogo.getTime(), 0.01);
    }

    @Test
    public void testEncerrarDiaResetaEnergiaJogador() {
        Jogo jogo = novoJogo();
        jogo.getPlayer().setEnergia(10.0);

        jogoService.encerrarDia(jogo);

        assertEquals(100.0, jogo.getPlayer().getEnergia(), 0.01);
    }

    // criarJogo
    @Test
    public void testCriarJogoInicializaHorario() {
        JogoService svc = new JogoService();
        Map mapa = new Map();

        Jogo jogo = svc.criarJogo("xyz", mapa);

        assertEquals(7.0, jogo.getTime(), 0.01);
    }

    @Test
    public void testCriarJogoPreencheMapa() {
        JogoService svc = new JogoService();
        Map mapa = new Map();

        Jogo jogo = svc.criarJogo("g1", mapa);

        assertNotNull(jogo.getMapa());
        assertNotNull(jogo.getMapa().getList()[0]);
    }
}
