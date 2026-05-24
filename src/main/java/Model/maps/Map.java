package Model.maps;

public class Map {
    private MiniMapa[] list;

    public Map (){
        list = new MiniMapa[8];
    }

    //getters e setters
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

    // metodos para adicionar um mapa na posição
    public void add(MiniMapa mapa, int pos){
        list[pos] = mapa;
    }

}
