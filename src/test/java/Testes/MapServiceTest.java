package Testes;

import org.junit.*;
import static org.junit.Assert.*;
import Model.maps.Map;
import Model.maps.MiniMapa;
import Service.InteragiveisService;
import Service.MapService;

public class MapServiceTest {

    private MapService mapService = new MapService();
    private InteragiveisService interagiveisService = new InteragiveisService();

    // preencherMinimapas

    @Test
    public void testPreencherMinimapasRetornaNaoNulo() {
        Map mapa = new Map();
        Map resultado = mapService.preencherMinimapas(mapa);
        assertNotNull(resultado);
    }

    @Test
    public void testPreencherMinimapasPreencheTodosOsSlots() {
        Map mapa = new Map();
        mapService.preencherMinimapas(mapa);
        MiniMapa[] list = mapa.getList();
        for (int i = 0; i < list.length; i++) {
            assertNotNull("Slot " + i + " não deve ser nulo", list[i]);
        }
    }

    // criarPonto

    @Test
    public void testCriarPontoTemIdCorreto() {
        MiniMapa ponto = mapService.criarPonto();
        assertEquals(30, ponto.getId());
    }

    @Test
    public void testCriarPontoPossuiCobradores() {
        MiniMapa ponto = mapService.criarPonto();
        for (int y = 3; y <= 4; y++) {
            for (int x = 21; x <= 24; x++) {
                assertEquals("Célula (" + x + "," + y + ") deve ter id 3",
                        3, ponto.getCelula(x, y));
            }
        }
    }

    @Test
    public void testCriarPontoPossuiPortasNaBase() {
        MiniMapa ponto = mapService.criarPonto();
        assertEquals(31, ponto.getCelula(15, 29));
        assertEquals(31, ponto.getCelula(14, 29));
    }

    // criarCantina
    @Test
    public void testCriarCantinaTemIdCorreto() {
        MiniMapa cantina = mapService.criarCantina();
        assertEquals(32, cantina.getId());
    }

    @Test
    public void testCriarCantinaPossuiVendedoresNasBordas() {
        MiniMapa cantina = mapService.criarCantina();
        for (int x = 24; x <= 29; x++) {
            assertEquals(3, cantina.getCelula(x, 9));
        }
    }

    @Test
    public void testCriarCantinaPossuiPortasNaSuperior() {
        MiniMapa cantina = mapService.criarCantina();
        assertEquals(31, cantina.getCelula(15, 0));
        assertEquals(31, cantina.getCelula(14, 0));
    }

    @Test
    public void testCriarCantinaPossuiPortasNaInferior() {
        MiniMapa cantina = mapService.criarCantina();
        assertEquals(31, cantina.getCelula(15, 29));
        assertEquals(31, cantina.getCelula(14, 29));
    }

    // criarColegiado

    @Test
    public void testCriarColegiadoTemIdCorreto() {
        MiniMapa colegiado = mapService.criarColegiado();
        assertEquals(38, colegiado.getId());
    }

    @Test
    public void testCriarColegiadoPossuiMaeliNaBordaSuperior() {
        MiniMapa colegiado = mapService.criarColegiado();
        for (int x = 9; x <= 19; x++) {
            assertEquals("Maeli faltando em x=" + x, 3, colegiado.getCelula(x, 24));
        }
    }

    // criarCorredor1

    @Test
    public void testCriarCorredor1TemIdCorreto() {
        MiniMapa corredor = mapService.criarCorredor1();
        assertEquals(31, corredor.getId());
    }

    @Test
    public void testCriarCorredor1PossuiCachorro() {
        MiniMapa corredor = mapService.criarCorredor1();
        assertNotNull(corredor.getInterNaPosicao(3, 16));
        assertEquals("cachorro", corredor.getInterNaPosicao(3, 16).getNome());
    }

    @Test
    public void testCriarCorredor1PortasCorretas() {
        MiniMapa corredor = mapService.criarCorredor1();
        assertEquals(30, corredor.getCelula(5, 0));
        assertEquals(32, corredor.getCelula(5, 29));
    }

    // criarCorredor2

    @Test
    public void testCriarCorredor2TemIdCorreto() {
        MiniMapa corredor = mapService.criarCorredor2();
        assertEquals(33, corredor.getId());
    }

    @Test
    public void testCriarCorredor2PossuiGato() {
        MiniMapa corredor = mapService.criarCorredor2();
        assertNotNull(corredor.getInterNaPosicao(3, 16));
        assertEquals("gato", corredor.getInterNaPosicao(3, 16).getNome());
    }

    // criarSala

    @Test
    public void testCriarSalaTemIdCorreto() {
        MiniMapa sala = mapService.criarSala();
        assertEquals(34, sala.getId());
    }

    @Test
    public void testCriarSalaPossuiProfessor() {
        MiniMapa sala = mapService.criarSala();
        assertNotNull(sala.getInterNaPosicao(2, 15));
        assertEquals("Felipe", sala.getInterNaPosicao(2, 15).getNome());
    }

    // criarLaboratorio

    @Test
    public void testCriarLaboratorioTemIdCorreto() {
        MiniMapa lab = mapService.criarLaboratorio();
        assertEquals(36, lab.getId());
    }

    @Test
    public void testCriarLaboratorioPossuiProfessor() {
        MiniMapa lab = mapService.criarLaboratorio();
        assertNotNull(lab.getInterNaPosicao(24, 2));
        assertEquals("Gustavo", lab.getInterNaPosicao(24, 2).getNome());
    }

    @Test
    public void testCriarLaboratorioPossuiAluno() {
        MiniMapa lab = mapService.criarLaboratorio();
        assertNotNull(lab.getInterNaPosicao(22, 20));
        assertEquals("Nando", lab.getInterNaPosicao(22, 20).getNome());
    }

    // criarCorredor3

    @Test
    public void testCriarCorredor3TemIdCorreto() {
        MiniMapa corredor = mapService.criarCorredor3();
        assertEquals(35, corredor.getId());
    }

    @Test
    public void testCriarCorredor3PossuiCachorro() {
        MiniMapa corredor = mapService.criarCorredor3();
        assertNotNull(corredor.getInterNaPosicao(3, 16));
        assertEquals("cachorro", corredor.getInterNaPosicao(3, 16).getNome());
    }
}
