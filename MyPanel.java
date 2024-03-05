import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

class DraggableImage {
    ImageIcon imageIcon;
    Point imageUpperLeft, prevPoint;

    DraggableImage(String imagePath, Point initialLocation) {
        try {
            File file = new File(imagePath);
            BufferedImage bufferedImage = ImageIO.read(file);
            imageIcon = new ImageIcon(bufferedImage);
            imageUpperLeft = initialLocation;
            prevPoint = imageUpperLeft;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Rectangle getBounds() {
        return new Rectangle((int) imageUpperLeft.getX(), (int) imageUpperLeft.getY(),
                imageIcon.getIconWidth(), imageIcon.getIconHeight());
    }
}


class MyPanel extends JPanel{
    Vector<DraggableImage> imageList;
    DraggableImage selectedImage;


    MyPanel(){
            imageList = new Vector<>();
            imageList.add(new DraggableImage("./ship1.png", new Point(100, 100))); // Add first image
            imageList.add(new DraggableImage("./ship2.png", new Point(150, 100))); // Add first image
            imageList.add(new DraggableImage("./ship3.png", new Point(200, 100))); // Add first image
            imageList.add(new DraggableImage("./ship4.png", new Point(250, 100))); // Add first image
            imageList.add(new DraggableImage("./ship5.png", new Point(300, 100))); // Add second image
            ClickListener clickListener = new ClickListener();
            this.addMouseListener(clickListener);
            DragListener dragListener = new DragListener();
            this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g) {
         super.paintComponent(g);
        ImageIcon backgroundIcon = new ImageIcon("bg.png"); // Replace "bg.png" with your background image file path
        Image bgImage = backgroundIcon.getImage();
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);

        for (DraggableImage image : imageList) {
            image.imageIcon.paintIcon(this, g, (int) image.imageUpperLeft.getX(), (int) image.imageUpperLeft.getY());
        }
    }

    private class ClickListener extends MouseAdapter{
        public void mousePressed(MouseEvent event) {
            for (DraggableImage image : imageList) {
                if (image.getBounds().contains(event.getPoint())) {
                    selectedImage = image;
                    selectedImage.prevPoint = event.getPoint();
                    return;
                }
            }
        }
    }

    private class DragListener extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent event) {
                if (selectedImage!=null){
                    Point currPoint = event.getPoint();
                    int dx = (int) (currPoint.getX() - selectedImage.prevPoint.getX());
                    int dy = (int) (currPoint.getY() - selectedImage.prevPoint.getY());
                    selectedImage.imageUpperLeft.translate(dx, dy);
                    selectedImage.prevPoint = currPoint;
                    repaint();
                }
            }
        }
    }

