import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

class DraggableImage {
    ImageIcon horizImageIcon;
    ImageIcon vertImageIcon;
    Point imageUpperLeft, prevPoint;
    Boolean horizontal;
    int length;


    DraggableImage(String imagePath, String imagePathH, Point initialLocation, int length) {
        try {
            File file = new File(imagePath);
            BufferedImage bufferedImage = ImageIO.read(file);
            vertImageIcon = new ImageIcon(bufferedImage);
            imageUpperLeft = initialLocation;
            prevPoint = imageUpperLeft;
            File file2 = new File(imagePathH);
            BufferedImage bufferedImage2 = ImageIO.read(file2);
            horizImageIcon = new ImageIcon(bufferedImage2);
            horizontal = false;
            this.length = length;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Rectangle getBounds() {
        return new Rectangle((int) imageUpperLeft.getX(), (int) imageUpperLeft.getY(),
                getImageIcon().getIconWidth(), getImageIcon().getIconHeight());
    }
    int getlen(){
        return length;
    }
    void rotate(){
        horizontal = !horizontal;
    }

    ImageIcon getImageIcon(){
        if(horizontal){
            return horizImageIcon;
        }
        return vertImageIcon;
    }

}


class MyPanel extends JPanel{
    Vector<DraggableImage> imageList;
    DraggableImage selectedImage;


    MyPanel(){
            imageList = new Vector<>();
            imageList.add(new DraggableImage("./ship1.png","./ship1h.png", new Point(100, 100),5)); // Add first image
            imageList.add(new DraggableImage("./ship2.png","./ship2h.png", new Point(150, 100),4)); // Add first image
            imageList.add(new DraggableImage("./ship3.png","./ship3h.png", new Point(200, 100),3)); // Add first image
            imageList.add(new DraggableImage("./ship4.png","./ship4h.png", new Point(250, 100),3)); // Add first image
            imageList.add(new DraggableImage("./ship5.png","./ship5h.png", new Point(300, 100),2)); // Add second image
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
            image.getImageIcon().paintIcon(this, g, (int) image.imageUpperLeft.getX(), (int) image.imageUpperLeft.getY());
        }
    }

    public void rotateBattleship(){
        imageList.get(1).rotate();
        this.paintComponent(getGraphics());
    }

    public void rotateCarrier(){
        imageList.get(0).rotate();
        this.paintComponent(getGraphics());
    }

    public void rotateSubmarine(){
        imageList.get(2).rotate();
        this.paintComponent(getGraphics());
    }

    public void rotateDestroyer(){
        imageList.get(3).rotate();
        this.paintComponent(getGraphics());
    }

    public void rotatePatrol(){
        imageList.get(4).rotate();
        this.paintComponent(getGraphics());
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

    public List<Integer> getShipCoordinates() {
        List<Integer> coords = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            DraggableImage image = imageList.get(i);
            Point location = image.imageUpperLeft;
            coords.add((int) location.getY());
            coords.add((int) location.getX());
            //coords.add(image.getImageIcon().getIconWidth());
            //coords.add(image.getImageIcon().getIconHeight());
        }
        return coords;
    }

    public boolean getHorizontal(int index) {
        return imageList.get(index).horizontal;
    }
}


