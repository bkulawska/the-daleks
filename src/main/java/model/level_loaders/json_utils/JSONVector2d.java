package model.level_loaders.json_utils;

import utils.Vector2d;

public class JSONVector2d {
    public int x, y;

    public Vector2d toVector2d() {
        return new Vector2d(x, y);
    }
}
