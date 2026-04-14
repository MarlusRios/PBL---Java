package Service;

import Model.maps.*;
import Model.personagens.*;

public class MapService {

    public void preencherMinimapas(Map mapa){
        //algoritmo para preencher os mapas, ex: cantina e colegiado//
        MiniMapa cantina = criarCantina();
        MiniMapa colegiado = criarColegiado();
        MiniMapa corredor1 = criarCorredor1();
        MiniMapa corredor2 = criarCorredor2();
        MiniMapa corredor3 = criarCorredor3();
        MiniMapa sala = criarSala();
    }

    public MiniMapa criarCantina(){
        MiniMapa cantina = new MiniMapa(32, 30, 30);
        for (int y = 9; y <= 19; y++) {
            for (int x = 24; x <= 29; x++) {
                if (y == 9 || y == 19 || x == 24 || x == 29) {
                    Interagiveis vendedor = new VendedorCantina(3,x,y);
                    cantina.adicionarInter(vendedor);
                }
            }
        }
        cantina.setCelula(15,0,31);
        cantina.setCelula(14,0,31);
        cantina.setCelula(15,29,31);
        cantina.setCelula(14,29,31);
        return cantina;
    }

    public MiniMapa criarColegiado(){
        MiniMapa colegiado = new MiniMapa(38, 30, 30);
        for (int y = 24; y <=29; y++) {
            for (int x = 9; x <= 19; x++) {
                if (x == 9 || x == 19 || y == 24 || y == 29) {
                    Interagiveis maeli = new Maeli(3,x,y);
                    colegiado.adicionarInter(maeli);
                }
            }
        }
        colegiado.setCelula(15, 0 , 37);
        colegiado.setCelula(14, 0, 37);
        return colegiado;
    }

    public MiniMapa criarCorredor1(){
        MiniMapa corredor = new MiniMapa(31, 10, 30);
        corredor.setCelula(5, 0, 30);
        corredor.setCelula(4,0,30);
        corredor.setCelula(5, 29, 32);
        corredor.setCelula(4, 29, 32);
        Interagiveis dog = new Cachorro(3, 3, 16, 1);
        return corredor;
    }

    public MiniMapa criarCorredor2(){
        MiniMapa corredor = new MiniMapa(33, 10, 30);
        corredor.setCelula(5, 0, 32);
        corredor.setCelula(4,0,32);
        corredor.setCelula(5, 29, 34);
        corredor.setCelula(4, 29, 34);
        Interagiveis cat = new Gato(3, 3, 16, 3);
        return corredor;
    }

    public MiniMapa criarCorredor3(){
        MiniMapa corredor = new MiniMapa(35, 10, 30);
        corredor.setCelula(5, 0, 34);
        corredor.setCelula(4,0,34);
        corredor.setCelula(5, 29, 36);
        corredor.setCelula(4, 29, 36);
        Interagiveis dog = new Cachorro(3, 3, 16, 1);
        return corredor;
    }

    public MiniMapa criarSala(){
        MiniMapa sala = new MiniMapa(34, 30, 30);
        sala.setCelula(15, 0, 33);
        sala.setCelula(14,0,33);
        sala.setCelula(15, 29, 35);
        sala.setCelula(14, 29, 35);
        return sala;
    }
}
