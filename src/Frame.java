import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class Frame extends JFrame implements MouseListener, KeyListener,ActionListener {
        private Screen screen = new Screen();
        private Minefield minefield = new Minefield();
        private int width = 400;
        private int height = 500;
        private boolean start = true;


    public Frame(){
        super("Minesweeper");
        setResizable(false);
        addMouseListener(this);
        addKeyListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(screen);
        pack();
        setSize(width + getInsets().left + getInsets().right, height + getInsets().bottom + getInsets().top);
        setVisible(true);
        setLocationRelativeTo(null);

        Timer timer = new Timer(1000,this);
        timer.setInitialDelay(1);
        timer.start();


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
    /**
     * function which evaluates the mouseclick
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 3 && start) // first click should not be a right click
            return;
        if (start) {  //so first leftklick never on bomb
            //initialises the bombs on the minefield
            minefield.placeBombs(e.getX() - getInsets().left, e.getY() - getInsets().top-100);//-100 abhängigi von height
            start = false;

        }
        // out of bounds
        if (e.getY() < getInsets().top+100)
            return;
        if (e.getY() > getInsets().top+height)
            return;
        if (e.getX() < getInsets().left)
            return;
        if(e.getX() >getInsets().left+width)
            return;

        // on leftclick reveal the tile you clicked (either bomb or safe tile where you get the nr of adjacent bombs)
        if (e.getButton() == 1)
            minefield.leftclicked(e.getX() - getInsets().left, e.getY() - getInsets().top-100); //-100 depends on height
        // on rightclick flags the tile and not reveal the tile
        if (e.getButton() == 3)
            minefield.rightclicked(e.getX() - getInsets().left, e.getY() - getInsets().top-100);//-100 depends on height

        // do not repaint if game is over
        if (!minefield.isGameover())
            screen.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }
    /**
     * reset Game
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R){
            start = true;
            minefield.reset();
            screen.repaint();
        }
    }
    /**
     * increment timer while game is running
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
            if (!minefield.isGameover()){
                minefield.incrementcounter();
                screen.repaint();
            }
    }

    class Screen extends JPanel{

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            minefield.draw(g);
        }

    }


}


