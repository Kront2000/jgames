package games.breakout;

import javax.swing.*;

public class Brick extends Sprite {

    private boolean destroyed;

    public Brick(int x, int y) {
        initBrick(x, y);
    }
    
    private void initBrick(int x, int y) {
        
        this.x = x;
        this.y = y;
        
        destroyed = false;

        loadImage();
        getImageDimensions();
    }
    
    private void loadImage() {
        var ii = new ImageIcon(getClass().getResource("/breakout/brick.jpg"));
        image = ii.getImage();        
    }

    boolean isDestroyed() {
        return destroyed;
    }

    void setDestroyed() {
        destroyed = true;
    }
}
