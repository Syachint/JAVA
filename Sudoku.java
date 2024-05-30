import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sudoku extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];
    private int[][] initialBoard = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    public Sudoku() {
        setTitle("Sudoku");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(9, 9));
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.PLAIN, 20));
                ((AbstractDocument) cells[row][col].getDocument()).setDocumentFilter(new DigitFilter());

                int value = initialBoard[row][col];
                if (value != 0) {
                    cells[row][col].setText(String.valueOf(value));
                    cells[row][col].setEditable(false);
                    cells[row][col].setBackground(Color.LIGHT_GRAY);
                }

                panel.add(cells[row][col]);
            }
        }

        JButton checkButton = new JButton("Check");
        checkButton.addActionListener(new CheckActionListener());

        JPanel controlPanel = new JPanel();
        controlPanel.add(checkButton);

        add(panel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private class CheckActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isValidSolution()) {
                JOptionPane.showMessageDialog(Sudoku.this, "Sudoku Solved Correctly!");
            } else {
                JOptionPane.showMessageDialog(Sudoku.this, "There are mistakes in your solution.");
            }
        }

        private boolean isValidSolution() {
            int[][] board = new int[9][9];
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = cells[row][col].getText();
                    if (text.isEmpty() || !text.matches("[1-9]")) {
                        return false;
                    }
                    board[row][col] = Integer.parseInt(text);
                }
            }
            return isValidSudoku(board);
        }

        private boolean isValidSudoku(int[][] board) {
            for (int i = 0; i < 9; i++) {
                boolean[] rowCheck = new boolean[9];
                boolean[] colCheck = new boolean[9];
                boolean[] boxCheck = new boolean[9];
                for (int j = 0; j < 9; j++) {
                    // Check rows
                    if (board[i][j] != 0) {
                        if (rowCheck[board[i][j] - 1]) return false;
                        rowCheck[board[i][j] - 1] = true;
                    }
                    // Check columns
                    if (board[j][i] != 0) {
                        if (colCheck[board[j][i] - 1]) return false;
                        colCheck[board[j][i] - 1] = true;
                    }
                    // Check 3x3 subgrids
                    int rowIndex = 3 * (i / 3) + j / 3;
                    int colIndex = 3 * (i % 3) + j % 3;
                    if (board[rowIndex][colIndex] != 0) {
                        if (boxCheck[board[rowIndex][colIndex] - 1]) return false;
                        boxCheck[board[rowIndex][colIndex] - 1] = true;
                    }
                }
            }
            return true;
        }
    }

    private class DigitFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[1-9]")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[1-9]")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Sudoku frame = new Sudoku();
            frame.setVisible(true);
        });
    }
}
