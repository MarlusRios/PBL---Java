package Service;

import Model.maps.MiniMapa;
import Model.personagens.Interagiveis;

public class NpcService {

    public void moverNpc(Interagiveis interagiveis, MiniMapa map, int newx, int newy){
        if(map.posValid(newx, newy)){
            if(map.getCelula(newx, newy) == 0){
                map.setCelula(interagiveis.getPosx(), interagiveis.getPosy(), 0);
                map.setCelula(newx, newy, 3);
                interagiveis.setPosx(newx);
                interagiveis.setPosy(newy);
            }
        }
    }

    public void adicionarNpc(MiniMapa map, Interagiveis interagiveis){
        map.adicionarNpc(interagiveis);
    }

    public void removerNpc(MiniMapa map, Interagiveis interagiveis){
        map.removerNpc(interagiveis.getPosx(), interagiveis.getPosy());
    }
}
