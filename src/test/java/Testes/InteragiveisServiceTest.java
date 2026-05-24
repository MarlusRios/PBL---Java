package Testes;

import org.junit.*;
import static org.junit.Assert.*;
import Model.maps.MiniMapa;
import Model.personagens.Aluno;
import Model.personagens.Cachorro;
import Model.personagens.Interagiveis;
import Service.InteragiveisService;

public class InteragiveisServiceTest {

    private InteragiveisService service = new InteragiveisService();

    // adicionarInter

    @Test
    public void testAdicionarInterRegistraNaMapa() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis aluno = new Aluno(3, "Nando", 2, 4, 1);

        service.adicionarInter(mapa, aluno);
        assertEquals(3, mapa.getCelula(2, 4));
    }

    @Test
    public void testAdicionarInterPermiteRecuperarPorPosicao() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis aluno = new Aluno(3, "Nando", 2, 4, 1);

        service.adicionarInter(mapa, aluno);

        assertNotNull(mapa.getInterNaPosicao(2, 4));
        assertEquals("Nando", mapa.getInterNaPosicao(2, 4).getNome());
    }

    @Test
    public void testAdicionarMultiplosInters() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis aluno   = new Aluno(3, "Nando", 2, 4, 1);
        Interagiveis cachorro = new Cachorro(3, 5, 7, 1);

        service.adicionarInter(mapa, aluno);
        service.adicionarInter(mapa, cachorro);

        assertNotNull(mapa.getInterNaPosicao(2, 4));
        assertNotNull(mapa.getInterNaPosicao(5, 7));
    }

    // removerInter

    @Test
    public void testRemoverInterLimpaMatriz() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis aluno = new Aluno(3, "Nando", 2, 4, 1);
        service.adicionarInter(mapa, aluno);

        service.removerInter(mapa, aluno);

        assertEquals(0, mapa.getCelula(2, 4));
    }

    @Test
    public void testRemoverInterLimpaHashMap() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis aluno = new Aluno(3, "Nando", 2, 4, 1);
        service.adicionarInter(mapa, aluno);

        service.removerInter(mapa, aluno);

        assertNull(mapa.getInterNaPosicao(2, 4));
    }

    // moverInter

    @Test
    public void testMoverInterParaCelulaVazia() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis cachorro = new Cachorro(3, 2, 2, 1);
        service.adicionarInter(mapa, cachorro);

        service.moverInter(cachorro, mapa, 3, 2);

        assertEquals(3, cachorro.getPosx());
        assertEquals(2, cachorro.getPosy());
        assertEquals(3, mapa.getCelula(3, 2));
        assertEquals(0, mapa.getCelula(2, 2));
    }

    @Test
    public void testMoverInterNaoMoveParaPosicaoInvalida() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis cachorro = new Cachorro(3, 1, 1, 1);
        service.adicionarInter(mapa, cachorro);

        service.moverInter(cachorro, mapa, -1, 1);

        assertEquals(1, cachorro.getPosx());
        assertEquals(1, cachorro.getPosy());
    }

    @Test
    public void testMoverInterNaoMoveParaCelulaOcupada() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis c1 = new Cachorro(3, 1, 1, 1);
        Interagiveis c2 = new Cachorro(3, 2, 1, 1);
        service.adicionarInter(mapa, c1);
        service.adicionarInter(mapa, c2);

        service.moverInter(c1, mapa, 2, 1);

        assertEquals(1, c1.getPosx());
    }

    @Test
    public void testMoverInterAtualizaPosicaoDoObjeto() {
        MiniMapa mapa = new MiniMapa(1, 10, 10);
        Interagiveis aluno = new Aluno(3, "X", 0, 0, 1);
        service.adicionarInter(mapa, aluno);

        service.moverInter(aluno, mapa, 1, 0);

        assertEquals(1, aluno.getPosx());
        assertEquals(0, aluno.getPosy());
    }
}
