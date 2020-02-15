import java.awt.*;

/**
 * A tile for the board.
 * @author Kian
 */
public class Tile {
    private int x,y;  //position of Tile in Array
    private static int width = 20;
    private static int height = 20;
    private boolean isbomb = false;
    private boolean isreveiled = false;
    private int neighbourbomb = 0;
    private boolean isflagged = false;

    private Font font = new Font("Arial", 0, 19);
    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *  Draws a the right version of a tile (Bomb,flag,reveiled,not reveiled)
     * @param g
     * @param gameover boolean to check if the game is already over
     */
    void draw(Graphics g,boolean gameover){
        g.setFont(font);
        if (isflagged && isbomb && gameover){ //so u mark the bombs which are correctly flagged at the end of the game
            g.setColor(Color.RED);
            g.drawString("X",x*width+4,100+y*height+18);//+100 depends on height
            return;
        }
        if (isflagged){  //tile is flagged
            g.setColor(Color.RED);
            g.fillRect(x*width+5,100+y*height+5,width-10,height-10); //maybe import flag //+100 depends on height
            g.setColor(Color.BLACK);
            g.drawRect(x*width,100+y*height,width,height);//+100 depends on height
            return;
        }
        //empty tile
        g.setColor(Color.WHITE);
        g.fillRect(x*width,100+y*height,width,height);  //+100 depends on height
        g.setColor(Color.BLACK);
        g.drawRect(x*width,100+y*height,width,height);//+100 depends on height


        if (isbomb && isreveiled) { //&& isreveiled um bomben zu verstecken
            g.drawString("X",x*width+4,100+y*height+18);//+100 depends on height
            return;
        }

        if (isreveiled) //reveil tile
            g.drawString(""+neighbourbomb,x*width+4,100+y*height+18);//+100 depends on height



    }

    public void reset(){
        isbomb = false;
        isreveiled = false;
        neighbourbomb = 0;
        isflagged = false;
    }
    public void reveil(){
        this.isreveiled = true;
    }
    public void flag(){
        this.isflagged = true;
    }
    public void unflag(){
        this.isflagged = false;
    }

    public boolean isIsflagged() {
        return isflagged;
    }

    public boolean isIsreveiled() {
        return isreveiled;
    }

    public void setIsbomb(boolean isbomb) {
        this.isbomb = isbomb;
    }

    public boolean isIsbomb() {
        return isbomb;
    }

    public void setNeighbourbomb(int neighbourbomb) {
        this.neighbourbomb = neighbourbomb;
    }

    public int getNeighbourbomb() {
        return neighbourbomb;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

}
