import org.junit.*;
import static org.junit.Assert.*;
import Model.Jogador;
import Model.Jogo;
import Model.maps.Map;
import Model.maps.MiniMapa;
import Model.personagens.Aluno;
import Model.personagens.Interagiveis;
import Service.JogadorService;

public class JogadorServiceTest {

    private JogadorService service = new JogadorService();

    //cria MiniMapa 10x10 limpo
    private MiniMapa novoMapa() {
        return new MiniMapa(1, 10, 10);
    }

    // naSala

    @Test
    public void testNaSalaRetornaTrueQuandoLoc4() {
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setLoc(4);
        assertTrue(service.naSala(jogador));
    }

    @Test
    public void testNaSalaRetornaFalseQuandoLocDiferente() {
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setLoc(3);
        assertFalse(service.naSala(jogador));
    }

    @Test
    public void testNaSalaRetornaFalseLocNegativo() {
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setLoc(-1);
        assertFalse(service.naSala(jogador));
    }

    // executarInteracao

    @Test
    public void testExecutarInteracaoComInteragiveisNaPosicao() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setEnergia(50.0);
        Interagiveis aluno = new Aluno(3, "Nando", 2, 2, 1);
        mapa.adicionarInter(aluno);
        double conhecimentoAntes = jogador.getConhecimento();

        service.executarInteracao(jogador, mapa, 2, 2);

        assertTrue(jogador.getConhecimento() >= conhecimentoAntes + 2.0);
    }

    @Test
    public void testExecutarInteracaoComCelulaVaziaNaoAltera() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setEnergia(50.0);
        double conhecimentoAntes = jogador.getConhecimento();

        service.executarInteracao(jogador, mapa, 2, 2);

        assertEquals(conhecimentoAntes, jogador.getConhecimento(), 0.01);
    }

    @Test
    public void testExecutarInteracaoPosicaoInvalidaNaoAltera() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setEnergia(50.0);
        double conhecimentoAntes = jogador.getConhecimento();

        service.executarInteracao(jogador, mapa, -1, -1);

        assertEquals(conhecimentoAntes, jogador.getConhecimento(), 0.01);
    }

    // interagir (usa seta do jogador)

    @Test
    public void testInteragirSetaCimaChamaInteracaoAcima() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setPosx(5);
        jogador.setPosy(5);
        jogador.setSeta("cima");
        jogador.setEnergia(50.0);
        Interagiveis aluno = new Aluno(3, "Nando", 5, 4, 1);
        mapa.adicionarInter(aluno);
        double conhecimentoAntes = jogador.getConhecimento();

        service.interagir(jogador, mapa);

        assertTrue(jogador.getConhecimento() >= conhecimentoAntes + 2.0);
    }

    @Test
    public void testInteragirSetaBaixoChamaInteracaoAbaixo() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setPosx(5);
        jogador.setPosy(5);
        jogador.setSeta("baixo");
        jogador.setEnergia(50.0);
        Interagiveis aluno = new Aluno(3, "Nando", 5, 6, 1); // y+1
        mapa.adicionarInter(aluno);
        double conhecimentoAntes = jogador.getConhecimento();

        service.interagir(jogador, mapa);

        assertTrue(jogador.getConhecimento() >= conhecimentoAntes + 2.0);
    }

    // Prova

    @Test
    public void testProvaSemana4CalculaDesempenho() {
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setConhecimento(80.0);
        Jogo jogo = new Jogo("id1", "P", 0, 0, new Map());
        jogo.setSemana(4);
        jogo.setPlayer(jogador);

        service.Prova(jogo);
        assertTrue(jogo.getPlayer().getDesempenho() >= 8.0);
    }

    @Test
    public void testProvaSemana8CalculaDesempenhoMedio() {
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setConhecimento(60.0);
        jogador.setDesempenho(8.0);
        Jogo jogo = new Jogo("id1", "P", 0, 0, new Map());
        jogo.setSemana(8);
        jogo.setPlayer(jogador);

        service.Prova(jogo);
        assertTrue(jogo.getPlayer().getDesempenho() >= 7.0);
    }

    @Test
    public void testProvaSemanaOutraNaoFazNada() {
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setConhecimento(50.0);
        jogador.setDesempenho(0.0);
        Jogo jogo = new Jogo("id1", "P", 0, 0, new Map());
        jogo.setSemana(3);
        jogo.setPlayer(jogador);

        service.Prova(jogo);

        assertEquals(0.0, jogo.getPlayer().getDesempenho(), 0.01);
    }

    // mover

    @Test
    public void testMoverWMovePosxMenos1() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setPosx(5);
        jogador.setPosy(5);

        service.mover(jogador, 'w', mapa);

        assertEquals(4, jogador.getPosx());
    }

    @Test
    public void testMoverSMovePosxMais1() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setPosx(5);
        jogador.setPosy(5);

        service.mover(jogador, 's', mapa);

        assertEquals(6, jogador.getPosx());
    }

    @Test
    public void testMoverAMovePosy() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setPosx(5);
        jogador.setPosy(5);

        service.mover(jogador, 'a', mapa);

        assertEquals(4, jogador.getPosy());
    }

    @Test
    public void testMoverDMovePosy() {
        MiniMapa mapa = novoMapa();
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setPosx(5);
        jogador.setPosy(5);

        service.mover(jogador, 'd', mapa);

        assertEquals(6, jogador.getPosy());
    }

    @Test
    public void testMoverNaoMoveSeCelulaOcupada() {
        MiniMapa mapa = novoMapa();
        mapa.setCelula(4, 5, 99);
        Jogador jogador = new Jogador("P", 0, 0);
        jogador.setPosx(5);
        jogador.setPosy(5);

        service.mover(jogador, 'w', mapa);

        assertEquals(5, jogador.getPosx());
    }
}
