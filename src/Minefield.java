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
                if (field[y][x].isBomb() ||(field[y][x].isBomb() && field[y][x].isFlagged()) ){  //if tile is a bomb regardless of flagged or unflagged
                    //redraw  a tile to the default tile

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
            if (rndx == startx && rndy == starty) // so a bomb wont spawn on the tile your reveal on the start of the game
                continue;
            if (!field[rndy][rndx].isBomb()) {
                //sets the bombs into the tile
                field[rndy][rndx].setBomb(true);
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
                if (!field[y][x].isBomb())
                    field[y][x].setNeighbourbomb(neighbourbombs);
            }
        }
    }

    /**
     * gets the neighbour bombs of a tile with the coordinates x and y
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
        //out of bound checks.
        //If neighbour tile of the tile with the x and y coordinate is not out of bounds increment neighbourbombs number of the tile
        // with the coordinates x and y
        if (bpx < field[y].length && bpy < field.length){
            if (field[bpy][bpx].isBomb())
                neighbourbombs++;
        }
        if (bpx < field[y].length && bmy > -1){
            if (field[bmy][bpx].isBomb())
                neighbourbombs++;
        }
        if (bmx > -1 && bpy < field.length){
            if (field[bpy][bmx].isBomb())
                neighbourbombs++;
        }
        if (bmx >-1 && bmy > -1){
            if (field[bmy][bmx].isBomb())
                neighbourbombs++;
        }


        if (bpx < field[y].length){
            if (field[y][bpx].isBomb())
                neighbourbombs++;
        }
        if (bmx > -1){
            if (field[y][bmx].isBomb())
                neighbourbombs++;
        }
        if (bpy < field.length){
            if (field[bpy][x].isBomb())
                neighbourbombs++;
        }
        if (bmy >-1){
            if (field[bmy][x].isBomb())
                neighbourbombs++;
        }

        return neighbourbombs;
    }

    /**
     * flags or unflags a tile
     * @param x x-coordinate of a tile
     * @param y y-ccordinate of a tile
     */
    public void rightclicked(int x, int y){
        int tilex = x / Tile.getWidth();
        int tiley = y / Tile.getHeight();
        //cannot flag revealed tiles
        if (field[tiley][tilex].isRevealed())
            return;

        if (!field[tiley][tilex].isFlagged()) {
            field[tiley][tilex].flag();
            amountofbombs--;
        }
        else {
            field[tiley][tilex].unflag();
            amountofbombs++;
        }
    }

    /**
     *  changes properties of a tile (bomb or shows number)
     *  Onto next call of the draw function it will be shown on the field.
     * @param x x-coordinate of a tile
     * @param y y-coordinate of a tile
     */
    public void leftclicked(int x, int y){
        int tilex = x / Tile.getWidth();
        int tiley = y / Tile.getHeight();

        //tile was once already clicked on
        if (field[tiley][tilex].isRevealed())
            return;
        //tile is flagged
        if (field[tiley][tilex].isFlagged())
            return;
        //tile gets revealed and his information will get drawn
        field[tiley][tilex].reveil();
        // player klicked on a bomb. Game is over.
        if (field[tiley][tilex].isBomb()) {
            lost = true;
            return;
        }


        notbombs--;
        //found all safa tiles. Game is over. you won
        if (notbombs == 0) {
            won = true;
            return;
        }

        // clicked tile has no bombs next to it so we can reveal all their neighbours
        if (field[tiley][tilex].getNeighbourbomb() == 0){
            int bpx = tilex+1;
            int bmx = tilex-1;
            int bpy = tiley+1;
            int bmy = tiley-1;

            //out of bound checks If neighbour tile is not out of bounds
            // reveal their neighbour tiles as long as they are not flagged or bombs

            if (bpx < field.length && bpy < field.length){
                if (!field[bpy][bpx].isBomb())
                    revealNeighbourTiles(bpx,bpy);
            }
            if (bpx < field.length && bmy > -1){
                if (!field[bmy][bpx].isBomb())
                    revealNeighbourTiles(bpx,bmy);
            }
            if (bmx > -1 && bpy < field.length){
                if (!field[bpy][bmx].isBomb())
                    revealNeighbourTiles(bmx,bpy);
            }
            if (bmx >-1 && bmy > -1){
                if (!field[bmy][bmx].isBomb())
                    revealNeighbourTiles(bmx,bmy);
            }


            if (bpx < field.length){
                if (!field[tiley][bpx].isBomb())
                    revealNeighbourTiles(bpx,tiley);
            }
            if (bmx > -1){
                if (!field[tiley][bmx].isBomb())
                    revealNeighbourTiles(bmx,tiley);
            }
            if (bpy < field.length){
                if (!field[bpy][tilex].isBomb())
                    revealNeighbourTiles(tilex,bpy);
            }
            if (bmy >-1){
                if (!field[bmy][tilex].isBomb())
                    revealNeighbourTiles(tilex,bmy);
            }
        }


    }

    /**
     * Recursively reveils the neighbour tiles of the xy tile as long as the neighbour is not
     * a bomb or flagged by the player
     * @param x x-coordinate of tile
     * @param y y-coordinate of tile
     */
    private void revealNeighbourTiles(int x, int y){
        int tilex = x;
        int tiley = y;

        //tile was already clicked on
        if (field[tiley][tilex].isRevealed())
            return;
        //tile is flagged which not get revealed
        if (field[tiley][tilex].isFlagged())
            return;
        //tile gets revealed and his information will get drawn
        field[tiley][tilex].reveil();
        notbombs--;
        //found all safa tiles. Game is over. you won
        if (notbombs == 0) {
            won = true;
            return;
        }
       //tile is a bomb which should not get revealed
        if (field[tiley][tilex].isBomb()) {
            return;
        }
        //is tile who has adjacent bombs. Only goes into recursion
        //when tiles has no bombs next to it
        if (field[tiley][tilex].getNeighbourbomb() != 0)
            return;

        int bpx = tilex+1;
        int bmx = tilex-1;
        int bpy = tiley+1;
        int bmy = tiley-1;

        //out of bound checks If neighbour tile is not out of bounds
        // call function with the coordinates of the neighbour tile.
        // boolean is false cause the call of the function was not from a mouse event.
        if (bpx < field.length && bpy < field.length){
            if (!field[bpy][bpx].isBomb())
                revealNeighbourTiles(bpx,bpy);
        }
        if (bpx < field.length && bmy > -1){
            if (!field[bmy][bpx].isBomb())
                revealNeighbourTiles(bpx,bmy);
        }
        if (bmx > -1 && bpy < field.length){
            if (!field[bpy][bmx].isBomb())
                revealNeighbourTiles(bmx,bpy);
        }
        if (bmx >-1 && bmy > -1){
            if (!field[bmy][bmx].isBomb())
                revealNeighbourTiles(bmx,bmy);
        }


        if (bpx < field.length){
            if (!field[tiley][bpx].isBomb())
                revealNeighbourTiles(bpx,tiley);
        }
        if (bmx > -1){
            if (!field[tiley][bmx].isBomb())
                revealNeighbourTiles(bmx,tiley);
        }
        if (bpy < field.length){
            if (!field[bpy][tilex].isBomb())
                revealNeighbourTiles(tilex,bpy);
        }
        if (bmy >-1){
            if (!field[bmy][tilex].isBomb())
                revealNeighbourTiles(tilex,bmy);
        }
    }







}
