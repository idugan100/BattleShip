import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// BatteShipView inherits JFrame
public class BattleShipView extends JFrame {
    private Game game;
    private static JButton [][] coordinateGrid; // 2D array of buttons for boards
    private JPanel mainMenuPanel;
    private JFrame frame;

    public BattleShipView(Game game) {
        this.game = game;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("BattleShip");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = initializeBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel initializeBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(10, 10));
        boardPanel.setBorder(BorderFactory.createLineBorder(color.BLACK, 2));
        boardPanel.setPreferredSize(new Dimension(720, 1080));

        for (int row = 0; row < game.player.board.HEIGHT; row++) {
            for (int col = 0; col < game.player.board.WIDTH; col++) {
                JButton cellButton = new JButton();
                cellButton.setBackground(Color.BLUE);
                cellBUtton.setOpaque(true);
                cellButton.setPreferredSize(new Dimension(40, 40));
                cellButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                boardPanel.add(cellButton);
            }
        }
        return boardPanel;
    }

    public static void main(String[] args) {
        Game game = new Game();
        new BattleShipView(game);
    }
}
