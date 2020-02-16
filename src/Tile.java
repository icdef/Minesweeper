import java.awt.*;

/**
 * A tile for the board.
 * @author Kian
 */
public class Tile {
    private int x,y;  //position of Tile in Array
    private static int width = 20;
    private static int height = 20;
    private boolean bomb = false;
    private boolean revealed = false;
    private int neighbourbomb = 0;
    private boolean isflagged = false;

    private Font font = new Font("Arial", 0, 19);
    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *  Flaggs the clicked tile or shows you if the tile was a bomb or safe
     * @param g
     * @param gameover boolean to check if the game is already over
     */
    void draw(Graphics g,boolean gameover){
        g.setFont(font);
        //the bomb tiles which are correctly tagged get a red X at the end of the game
        if (isflagged && bomb && gameover){
            g.setColor(Color.RED);
            g.drawString("X",x*width+4,100+y*height+18);//+100 depends on height
            return;
        }
        if (isflagged){  //tile is flagged
            //draws a smaller red rectangle into the tile. does not reveal the tile
            g.setColor(Color.RED);
            g.fillRect(x*width+5,100+y*height+5,width-10,height-10); //maybe import flag //+100 depends on height
            g.setColor(Color.BLACK);
            g.drawRect(x*width,100+y*height,width,height);//+100 depends on height
            return;
        }

        //default (empty) tile
        g.setColor(Color.WHITE);
        g.fillRect(x*width,100+y*height,width,height);  //+100 depends on height
        g.setColor(Color.BLACK);
        g.drawRect(x*width,100+y*height,width,height);//+100 depends on height

        // if you clicked on a bomb the till will turn into a X
        if (bomb && revealed) {
            g.drawString("X",x*width+4,100+y*height+18);//+100 depends on height
            return;
        }
        //when clicked on a safe tile you get the nr of neighbour bombs written into the tile
        if (revealed) //shows the nr of neighbourbombs on the tile
            g.drawString(""+neighbourbomb,x*width+4,100+y*height+18);//+100 depends on height



    }

    public void reset(){
        bomb = false;
        revealed = false;
        neighbourbomb = 0;
        isflagged = false;
    }

    public void reveil(){
        this.revealed = true;
    }
    public void flag(){
        this.isflagged = true;
    }
    public void unflag(){
        this.isflagged = false;
    }

    public boolean isFlagged() {
        return isflagged;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public boolean isBomb() {
        return bomb;
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
