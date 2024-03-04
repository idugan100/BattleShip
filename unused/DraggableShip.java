import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Image;

public class DraggableShip extends JLabel {
    private Ship ship;
    private Point mousePressLocation;
    private final int gridSize;
    private Point originalLocation;
    private Game game;

    public DraggableShip(Ship ship, int gridSize, Game game) {
        this.ship = ship;
        this.gridSize = gridSize;
        this.originalLocation = getLocation();
        this.game = game;

        // set shiop objects image
        this.setIcon(ship.getImageIcon());
        this.setSize(ship.getImageIcon().getIconWidth(), ship.getImageIcon().getIconHeight());

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // When mouse is pressed set anchor point
                mousePressLocation = e.getPoint();
                originalLocation = getLocation();
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                    // calculate distance the mouse moved
                    int xChange = e.getX() - mousePressLocation.x;
                    int yChange = e.getY() - mousePressLocation.y;
                    setLocation(getX() + xChange, getY() + yChange);
                }
            
            
            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
    
                // snap ship to nearest grid location
                snapToGrid();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        
    }

    private void snapToGrid() {
        // Get current location of the ship
        Point location = getLocation();

        // convert the location to grid coords
        int gridX = Math.round(location.x / (float)gridSize) * gridSize;
        int gridY = Math.round(location.y / (float)gridSize) * gridSize;

        // Check if new location is valid before snapping
        if (checkShipPlacementOnBoard(gridX, gridY)) {
            setLocation(gridX * gridSize, gridY * gridSize);
        } else {
            setLocation(originalLocation);
        }
    }

    private boolean checkShipPlacementOnBoard(int gridX, int gridY) {
        // ASSUMES SHIPS ONLY PLACED VERTICALLY CURRENTLY
        Coordinate[] coordinates = new Coordinate[ship.getSize()];
        for (int i = 0; i < ship.getSize(); i++) {
            coordinates[i] = new Coordinate(gridY + i, gridX);
        }

        // check if all corrds are valid and don't collide with other ships
        Ship newShip = new Ship(ship.getSize(), ship.getName(), ship.getMarker(), ship.getImageIcon());
        boolean valid = game.player.board.addShip(newShip);

        if (valid) {
            newShip.placeShip(coordinates);
            game.player.board.removeShip(ship);
        }

        return valid;
    }
}


