package Service;

import Model.maps.MiniMapa;
import Model.personagens.Npc;

public class NpcService {

    public void moverNpc(Npc npc, MiniMapa map, int newx, int newy){
        if(map.posValid(newx, newy)){
            if(map.getCelula(newx, newy) == 0){
                map.setCelula(npc.getPosx(), npc.getPosy(), 0);
                map.setCelula(newx, newy, 3);
                npc.setPosx(newx);
                npc.setPosy(newy);
            }
        }
    }

    public void adicionarNpc(MiniMapa map, Npc npc){
        map.adicionarNpc(npc);
    }

    public void removerNpc(MiniMapa map, Npc npc){
        map.removerNpc(npc.getPosx(), npc.getPosy());
    }
}
