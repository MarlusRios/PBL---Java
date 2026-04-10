package Model.maps;

public class Map {
    private final String nome = "UEFS";
    private final MiniMapa[] list;

    public Map (){
        list = new MiniMapa[8];
        int pos = 0;
        for (int i = 0; i<8; i++){
            if (pos + 30 != 31 && pos + 30 != 33 && pos + 30 != 35){
                MiniMapa map = new MiniMapa(pos + 30, 30, 30);
                list[pos++] = map;
            }else{
                MiniMapa map = new MiniMapa(pos + 30, 30, 10);
                list[pos++] = map;
            }
        }
    }

    public MiniMapa[] getList() {
        return list;
    }

    public MiniMapa getMinimapa(int id){
        for (int i = 0; i<list.length; i++){
            if (list[i].getId() == id){
                return list[i];
            }
        }
         return null;
    }

}
