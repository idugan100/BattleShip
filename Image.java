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

    MyPanel(ImageIcon imageIcon){
        image = imageIcon;
        imageUpperLeft = new Point(100,100);
        prevPoint = imageUpperLeft;
        ClickListener clickListener = new ClickListener();
        this.addMouseListener(clickListener);
        DragListener dragListener = new DragListener();
        this.addMouseMotionListener(dragListener);
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
   class MyFrame extends JFrame {
        MyFrame(ImageIcon imageIcon){
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(800,800);
            this.setLocationRelativeTo(null);
            MyPanel myPanel = new MyPanel(imageIcon);
            myPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            myPanel.setSize(imageIcon.getIconHeight(), imageIcon.getIconWidth());
            this.add(myPanel);
            this.setSize(imageIcon.getIconHeight()*2, imageIcon.getIconWidth()*2);
            this.setBackground(Color.CYAN);
            this.setVisible(true);
        }
    }

public class Image {
    public static void main(String[] args) throws IOException {
    File file = new File("./");
    BufferedImage bufferedImage = ImageIO.read(file);
    ImageIcon imageIcon = new ImageIcon(bufferedImage);
    JFrame jFrame = new MyFrame(imageIcon);
    }
}
