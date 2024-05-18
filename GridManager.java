package spider;

import java.awt.*;

public class GridManager {
    public int WIDTH, HEIGHT;

    public GridManager(int _w, int _h) {
        WIDTH = _w;
        HEIGHT = _h;
    }

    public int relativeSizeX(float scale) {
        return (int) (WIDTH * scale);
    }

    public int relativeSizeY(float scale) {
        return (int) (HEIGHT * scale);
    }

    public Rectangle relative(float _x, float _y, float _w, float _h) {
        return new Rectangle(
                relativeSizeX(_x),
                relativeSizeY(_y),
                relativeSizeX(_w),
                relativeSizeY(_h)
        );
    }
}
