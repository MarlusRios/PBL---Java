package Service;

import Model.maps.MiniMapa;
public class MinimapService {

    public boolean posValid(int x, int y) {
        if (y < 0) {
            return false;
        }
        if (y >= matriz.length) {
            return false;
        }
        if (x < 0) {
            return false;
        }
        if (x >= matriz[0].length) {
            return false;
        }
        return true;
    }
}
}
