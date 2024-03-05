import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class MyPanel extends JPanel{
    ImageIcon image;
    Point imageUpperLeft, prevPoint;

    MyPanel(){
        //make an image for each ship and attatch click and drag listenet to reach on
        try {   
            File file = new File("./ship2.png");
            BufferedImage bufferedImage = ImageIO.read(file);
            ImageIcon imageIcon = new ImageIcon(bufferedImage);
            image = imageIcon;
            imageUpperLeft = new Point(400,400);
            prevPoint = imageUpperLeft;
            ClickListener clickListener = new ClickListener();
            this.addMouseListener(clickListener);
            DragListener dragListener = new DragListener();
            this.addMouseMotionListener(dragListener);
        } catch (Exception e) {
            // TODO: handle exception
        }
    
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        image.paintIcon(this, g, (int) imageUpperLeft.getX(), (int)
        imageUpperLeft.getY());
    }

    private class ClickListener extends MouseAdapter{
        public void mousePressed(MouseEvent event) {
            prevPoint = event.getPoint();
        }
    }

    private class DragListener extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent event) {
            Point currPoint = event.getPoint();
            int dx = (int) (currPoint.getX() - prevPoint.getX());
            int dy = (int) (currPoint.getY() - prevPoint.getY());
            imageUpperLeft.translate(dx, dy);
            prevPoint = currPoint;
            repaint();
        }
    }
}
