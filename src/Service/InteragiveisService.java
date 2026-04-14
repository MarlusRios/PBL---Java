package Service;

import Model.maps.MiniMapa;
import Model.personagens.Interagiveis;

public class InteragiveisService {

    public void moverInter(Interagiveis interagiveis, MiniMapa map, int newx, int newy){
        if(map.posValid(newx, newy)){
            if(map.getCelula(newx, newy) == 0){
                map.setCelula(interagiveis.getPosx(), interagiveis.getPosy(), 0);
                map.setCelula(newx, newy, 3);
                interagiveis.setPosx(newx);
                interagiveis.setPosy(newy);
            }
        }
    }

    public void adicionarInter(MiniMapa map, Interagiveis interagiveis){
        map.adicionarInter(interagiveis);
    }


    public void removerInter(MiniMapa map, Interagiveis interagiveis){
        map.removerInter(interagiveis.getPosx(), interagiveis.getPosy());
    }
}
