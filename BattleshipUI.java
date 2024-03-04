import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BattleshipUI extends JFrame {
    private static Game game = new Game();
    private JPanel playerPanel, opponentPanel;
    private JLabel [][] playerGridCells, opponentGridCells;
    private int gridSize;

    public BattleshipUI (int size) {
        gridSize = size;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        playerPanel = createBoardPanel(size, "Player Board");
        opponentPanel = createBoardPanel(size, "Opponent Board");

        c.gridx = 0; // col 0
        c.gridy = 0; // row 0
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        this.add(opponentPanel, c);
        
        c.gridx = 0; // col 0
        c.gridy = 1; // row 1, under opponent's board
        this.add(playerPanel, c);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private JPanel createBoardPanel(int size, String title) {
        JPanel boardPanel = new JPanel(new GridLayout(size, size));
        boardPanel.setBorder(BorderFactory.createTitledBorder(title));
        JLabel[][] gridCells = new JLabel[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                gridCells[row][col] = new JLabel();
                gridCells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(gridCells[row][col]);
            }
        }

        // set the title of the board created
        if (title.equals("Player Board")) {
            playerGridCells = gridCells;
        } else {
            opponentGridCells = gridCells;
        }

        return boardPanel;

    }

    public static void main(String[] args) {
        // Assuming the gridSize is passed as a command-line argument
        // Check if an argument is provided
        if (args.length > 0) {
            try {
                int gridSize = Integer.parseInt(args[0]); // Convert the first argument to integer
                new BattleshipUI(gridSize); // Create a new instance of BattleshipUI with the provided grid size
            } catch (NumberFormatException e) {
                System.err.println("Argument" + args[0] + " must be an integer.");
                System.exit(1);
            }
        } else {
            System.err.println("You must provide a grid size as a command-line argument.");
            System.exit(1);
        }
    }

}
