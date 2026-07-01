package Service;

import Model.Eventos.Obrigatorios.FazerProva1;
import Model.Eventos.Obrigatorios.FazerProva2;
import Model.Jogo;
import Model.Eventos.Obrigatorio;
import Model.Jogador;
import Model.personagens.Interagiveis;

import java.security.PublicKey;

public class JogadorService {

    //metodo para interagir com os Interagiveis e aplicar suas consequencias
    public void interagir(Jogador player,Interagiveis interagiveis) {
        interagiveis.interacao(player);
    }

    //metodo para fazer a prova
    public void Prova(Jogo jogo){
        if(jogo.getSemana() == 4)   {
            Obrigatorio prova = new FazerProva1();
            prova.aplicarEvento(jogo.getPlayer());
        }else if(jogo.getSemana() == 8){
            Obrigatorio prova = new FazerProva2();
            prova.aplicarEvento(jogo.getPlayer());
        }
    }
}
