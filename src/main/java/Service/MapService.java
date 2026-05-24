package Service;

import Model.maps.*;
import Model.personagens.*;

public class MapService {

    private final InteragiveisService interagiveisService = new InteragiveisService();

    public Map preencherMinimapas(Map mapa){
        //algoritmo para preencher os mapas, ex: cantina e colegiado//
        MiniMapa cantina = criarCantina();
        mapa.add(cantina, 2);
        MiniMapa colegiado = criarColegiado();
        mapa.add(colegiado, 7);
        MiniMapa corredor1 = criarCorredor1();
        mapa.add(corredor1, 1);
        MiniMapa corredor2 = criarCorredor2();
        mapa.add(corredor2, 3);
        MiniMapa corredor3 = criarCorredor3();
        mapa.add(corredor3, 5);
        MiniMapa sala = criarSala();
        mapa.add(sala, 4);
        MiniMapa lab = criarLaboratorio();
        mapa.add(lab, 6);
        MiniMapa ponto = criarPonto();
        mapa.add(ponto, 0);
        return mapa;
    }

    //metodo para criar o ponto de onibus
    public MiniMapa criarPonto(){
        MiniMapa ponto = new MiniMapa(30, 30, 30);
        for(int y = 3; y<=4; y++){
            for (int x = 21; x<= 24; x++){
                Interagiveis cobrador = new CobradorOnibus(3, x, y);
                interagiveisService.adicionarInter(ponto, cobrador);
            }
        }
        ponto.setCelula(15,29,31);
        ponto.setCelula(14,29,31);
        return ponto;
    }

    //metodo para criar a cantina
    public MiniMapa criarCantina(){
        MiniMapa cantina = new MiniMapa(32, 30, 30);
        for (int y = 9; y <= 19; y++) {
            for (int x = 24; x <= 29; x++) {
                if (y == 9 || y == 19 || x == 24 || x == 29) {
                    Interagiveis vendedor = new VendedorCantina(3,x,y);
                    interagiveisService.adicionarInter(cantina,vendedor);
                }
            }
        }
        cantina.setCelula(15,0,31);
        cantina.setCelula(14,0,31);
        cantina.setCelula(15,29,31);
        cantina.setCelula(14,29,31);
        return cantina;
    }

    //metodo para criar o colegiado
    public MiniMapa criarColegiado(){
        MiniMapa colegiado = new MiniMapa(38, 30, 30);
        for (int y = 24; y <=29; y++) {
            for (int x = 9; x <= 19; x++) {
                if (x == 9 || x == 19 || y == 24 || y == 29) {
                    Interagiveis maeli = new Maeli(3,x,y);
                    interagiveisService.adicionarInter(colegiado, maeli);
                }
            }
        }
        colegiado.setCelula(15, 0 , 37);
        colegiado.setCelula(14, 0, 37);
        return colegiado;
    }

    //metodo para criar o corredor
    public MiniMapa criarCorredor1(){
        MiniMapa corredor = new MiniMapa(31, 30, 30);
        corredor.setCelula(5, 0, 30);
        corredor.setCelula(4,0,30);
        corredor.setCelula(5, 29, 32);
        corredor.setCelula(4, 29, 32);
        Interagiveis dog = new Cachorro(3, 3, 16, 2);
        interagiveisService.adicionarInter(corredor,dog);
        return corredor;
    }

    //metodo para criar o corredor
    public MiniMapa criarCorredor2( ){
        MiniMapa corredor = new MiniMapa(33, 30, 30);
        corredor.setCelula(5, 0, 32);
        corredor.setCelula(4,0,32);
        corredor.setCelula(5, 29, 34);
        corredor.setCelula(4, 29, 34);
        Interagiveis cat = new Gato(3, 3, 16, 4);
        interagiveisService.adicionarInter(corredor, cat);
        return corredor;
    }

    //metodo para criar o corredor
    public MiniMapa criarCorredor3(){
        MiniMapa corredor = new MiniMapa(35, 30, 30);
        corredor.setCelula(5, 0, 34);
        corredor.setCelula(4,0,34);
        corredor.setCelula(5, 29, 36);
        corredor.setCelula(4, 29, 36);
        Interagiveis dog = new Cachorro(3, 3, 16, 6);
        interagiveisService.adicionarInter(corredor,dog);
        return corredor;
    }

    //metodo para criar a sala
    public MiniMapa criarSala( ){
        MiniMapa sala = new MiniMapa(34, 30, 30);
        sala.setCelula(15, 0, 33);
        sala.setCelula(14,0,33);
        sala.setCelula(15, 29, 35);
        sala.setCelula(14, 29, 35);
        Interagiveis professor = new Professor(3, "Felipe", 2, 15, 5);
        interagiveisService.adicionarInter(sala, professor);
        return sala;
    }

    //metodo para criar o labooratorio
    public MiniMapa criarLaboratorio(){
        MiniMapa lab = new MiniMapa(36, 30, 30);
        lab.setCelula(15, 0, 35);
        lab.setCelula(14, 0, 35);
        lab.setCelula(15, 29, 37);
        lab.setCelula(14, 29, 37);
        Interagiveis professor = new Professor(3, "Gustavo", 24, 2, 7);
        Interagiveis aluno = new Aluno(3, "Nando", 22, 20, 7);
        interagiveisService.adicionarInter(lab,aluno);
        interagiveisService.adicionarInter(lab,professor);
        return lab;
    }
}
