import java.awt.*;

public class Minefield {

private Tile[][] field = new Tile[20][20];
private final int BOMBS = 50;
private  int amountofbombs = BOMBS;
private int notbombs = field.length*field.length-amountofbombs; //amount of not bomb tiles in the game
private boolean won = false;
private boolean lost = false;
private boolean gameover = false;
private int counter = 0;

    public Minefield (){
        //set field with tiles
        for (int y = 0; y <field.length ; y++) {
            for (int x = 0; x <field[y].length ; x++) {
                Tile tiles = new Tile(x,y);
                field[y][x] = tiles;
            }
        }
    }
    public void incrementcounter(){
         counter++;
    }

    /**
     * draws the field
     * @param g
     */
    public void draw(Graphics g){
        g.setFont(new Font("Arial", Font.PLAIN,19));
        if (counter < 1000)
            g.drawString("Time played: "+counter,250,50);


        for (int y = 0; y <field.length ; y++) {
            for (int x = 0; x <field[y].length ; x++) {
                field[y][x].draw(g,false);
            }
        }

        g.drawString("Minesweeper",150,25);
        g.drawString("Bombs left: "+amountofbombs,10,50);

        if(won) {
            gameover = true;
            System.out.println("won");
            g.drawString("You Won. Press R to restart",10,75);
            return;
        }
        if (lost) {
            gameover = true;
            g.drawString("You Lost. Press R to restart",10,75);
            drawAllBombs(g);
            System.out.println("lost");

        }

    }

    public boolean isGameover() {
        return gameover;
    }

    /**
     * resets the field
     */
    public void reset(){
        for (int y = 0; y <field.length ; y++) {
            for (int x = 0; x <field[y].length ; x++) {
                field[y][x].reset();
            }
        }
        amountofbombs = BOMBS;
        notbombs = field.length*field.length-amountofbombs;
        won = false;
        lost = false;
        gameover = false;
        counter = 0;
    }

    /**
     * draws all bombs in the game
     * @param g
     */
    public void drawAllBombs(Graphics g){
        for (int y = 0; y <field.length ; y++) {
            for (int x = 0; x <field[y].length ; x++) {
                if (field[y][x].isIsbomb() ||(field[y][x].isIsbomb() && field[y][x].isIsflagged()) ){  //if tile is a bomb regardless of flagged or unflagged
                    //redraw tile
                    g.setColor(Color.WHITE);
                    g.fillRect(x*Tile.getWidth(),100+y*Tile.getHeight(),Tile.getWidth(),Tile.getHeight());
                    g.setColor(Color.BLACK);
                    g.drawRect(x*Tile.getWidth(),100+y*Tile.getHeight(),Tile.getWidth(),Tile.getHeight());

                    field[y][x].reveil();
                    field[y][x].draw(g,true);
                }

            }
        }
    }

    /**
     * places Bombs into the field
     * @param sx x-ccordinate of the first legal click
     * @param sy y- coordinate of the first legal click
     */
    public void placeBombs(int sx, int sy){
        int startx = sx/Tile.getWidth();
        int starty = sy/Tile.getHeight();
        int counter = amountofbombs;
        while(counter > 0) {
            int rndx = (int) (Math.random()*20);
            int rndy = (int) (Math.random()*20);
            if (rndx == startx && rndy == starty) // so a bomb wont spawn on the tile your reveil on the start of the game
                continue;
            if (!field[rndy][rndx].isIsbomb()) {
                field[rndy][rndx].setIsbomb(true);
                counter--;
            }
        }

        setNeighbourBombnr();
    }

    /**
     * Set the nr of neighbourbombs for every not Bomb tile
     */
    public void setNeighbourBombnr(){
        for (int y = 0; y <field.length ; y++) {
            for (int x = 0; x <field[y].length ; x++) {
                int neighbourbombs = getNeighbourBombs(x,y);
                if (!field[y][x].isIsbomb())
                    field[y][x].setNeighbourbomb(neighbourbombs);
            }
        }
    }

    /**
     * gets the neighbout bombs of a tile with the coordinates x and y
     * @param x x-coordinate of tile
     * @param y y-coordinate of tile
     * @return nr of neighbour bombs of a tile
     */
    public int getNeighbourBombs(int x, int y){
        int neighbourbombs = 0;
        int bpx = x+1;
        int bmx = x-1;
        int bpy = y+1;
        int bmy = y-1;
        //out of bound checks
        if (bpx < field[y].length && bpy < field.length){
            if (field[bpy][bpx].isIsbomb())
                neighbourbombs++;
        }
        if (bpx < field[y].length && bmy > -1){
            if (field[bmy][bpx].isIsbomb())
                neighbourbombs++;
        }
        if (bmx > -1 && bpy < field.length){
            if (field[bpy][bmx].isIsbomb())
                neighbourbombs++;
        }
        if (bmx >-1 && bmy > -1){
            if (field[bmy][bmx].isIsbomb())
                neighbourbombs++;
        }


        if (bpx < field[y].length){
            if (field[y][bpx].isIsbomb())
                neighbourbombs++;
        }
        if (bmx > -1){
            if (field[y][bmx].isIsbomb())
                neighbourbombs++;
        }
        if (bpy < field.length){
            if (field[bpy][x].isIsbomb())
                neighbourbombs++;
        }
        if (bmy >-1){
            if (field[bmy][x].isIsbomb())
                neighbourbombs++;
        }

        return neighbourbombs;
    }

    /**
     * flags or unflags a tile
     * @param x x-coordinate of a tile
     * @param y y-ccordinate of a tile
     */
    public void klickedright(int x, int y){
        int tilex = x / Tile.getWidth();
        int tiley = y / Tile.getHeight();
        if (field[tiley][tilex].isIsreveiled())
            return;
        if (!field[tiley][tilex].isIsflagged()) {
            field[tiley][tilex].flag();
            amountofbombs--;
        }
        else {
            field[tiley][tilex].unflag();
            amountofbombs++;
        }
    }

    /**
     *  changes proberties of a tile (bomb or shows number) and their neighbour tile if tile not bomb and nr is 0
     * @param x x-coordinate of a tile
     * @param y y-coordinate of a tile
     * @param fromMouseevent true when function was called because of a mouseevent
     */
    public void klickedleft(int x, int y, boolean fromMouseevent){
        int tilex;
        int tiley;

        if (fromMouseevent) {
             tilex = x / Tile.getWidth();
             tiley = y / Tile.getHeight();
        }
        else{
            tilex = x;
            tiley = y;
        }

        if (field[tiley][tilex].isIsreveiled())
            return;
        if (field[tiley][tilex].isIsflagged())
            return;

        field[tiley][tilex].reveil();

        if (field[tiley][tilex].isIsbomb() && fromMouseevent) {
            lost = true;
            return;
        }
        if (field[tiley][tilex].isIsbomb()) {
            return;
        }

        notbombs--;

        if (notbombs == 0) {
            won = true;
            return;
        }

        if (field[tiley][tilex].getNeighbourbomb() == 0){
            int bpx = tilex+1;
            int bmx = tilex-1;
            int bpy = tiley+1;
            int bmy = tiley-1;
            //out of bound check
            if (bpx < field.length && bpy < field.length){
                if (!field[bpy][bpx].isIsbomb())
                    klickedleft(bpx,bpy,false);
            }
            if (bpx < field.length && bmy > -1){
                if (!field[bmy][bpx].isIsbomb())
                    klickedleft(bpx,bmy,false);
            }
            if (bmx > -1 && bpy < field.length){
                if (!field[bpy][bmx].isIsbomb())
                    klickedleft(bmx,bpy,false);
            }
            if (bmx >-1 && bmy > -1){
                if (!field[bmy][bmx].isIsbomb())
                    klickedleft(bmx,bmy,false);
            }


            if (bpx < field.length){
                if (!field[tiley][bpx].isIsbomb())
                    klickedleft(bpx,tiley,false);
            }
            if (bmx > -1){
                if (!field[tiley][bmx].isIsbomb())
                    klickedleft(bmx,tiley,false);
            }
            if (bpy < field.length){
                if (!field[bpy][tilex].isIsbomb())
                    klickedleft(tilex,bpy,false);
            }
            if (bmy >-1){
                if (!field[bmy][tilex].isIsbomb())
                    klickedleft(tilex,bmy,false);
            }
        }

    }






}
