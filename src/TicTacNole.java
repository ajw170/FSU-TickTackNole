/* Andrew J Wood
   COP3252 - Java Programming
   Tic Tac Nole!

   This program implements a GUI using the java.swing GUI components to
   represent a tic tac toe board.  The user clicks on the board to mark the
   'X' or 'O'.  Each turn alternates between X and O.

   Future plan is to add computer player functionality.

   When a winner is detected or a draw occurs, a message displays asking if the user
   would like to play another game.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TicTacNole extends JFrame
{
    private final JLabel titleBar;
    private final JPanel titleArea;
    private final JPanel buttonArea;
    private final JPanel statusArea;
    private final JButton[] buttons;
    private final JLabel statusBar;
    private final JLabel infoBar;
    private final JButton reset;
    private final static String[] buttonLabels = {" "," "," "," "," "," "," "," "," "};
    private boolean xGoes = true; //starts out as X turn always

    //TicTacNole Constructor
    public TicTacNole()
    {
        //specifies the title of the window
        super("FSU - Tic Tac Nole");

        //Menu Bar
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');

        //Exit Item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('x');
        fileMenu.add(exitItem);
        exitItem.addActionListener(
                //anonymous inner class
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent event)
                    {
                        System.exit(0);
                    }
                }

        );

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        //Panel for Title Area
        titleArea = new JPanel();
        titleArea.setLayout(new FlowLayout());
        titleBar = new JLabel("FSU - Tic Tac Nole");
        titleArea.add(titleBar); //add title bar to the title area Panel

        //Panel for Button Area
        buttonArea = new JPanel();
        buttonArea.setLayout(new GridLayout(3,3)); //3 rows by 3 columns
        buttons = new JButton[buttonLabels.length];
        Font defaultFont = new Font(Font.SANS_SERIF,Font.BOLD,26);

        //Create handler object
        TicTacNoleHandler handler = new TicTacNoleHandler();

        for (int count = 0; count < buttonLabels.length; ++count)
        {
            buttons[count] = new JButton(buttonLabels[count]);
            buttons[count].addActionListener(handler);
            buttons[count].setFont(defaultFont);
            buttonArea.add(buttons[count]);
        }

        //Panel for Status Area - This will display current turn information as well as a reset button
        statusArea = new JPanel();
        statusArea.setLayout(new GridLayout(3,1)); //3 rows by 1 column
        statusBar = new JLabel("X's Turn"); //default starting is X's turn
        statusBar.setHorizontalAlignment(JLabel.CENTER);
        infoBar = new JLabel("Created by Andrew Wood");
        infoBar.setHorizontalAlignment(JLabel.CENTER);

        reset = new JButton("Reset Game");
        reset.addActionListener(handler);
        statusArea.add(statusBar);
        statusArea.add(reset);
        statusArea.add(infoBar);

        //Add all panels to the Frame
        add(titleArea,BorderLayout.NORTH);
        add(buttonArea,BorderLayout.CENTER);
        add(statusArea,BorderLayout.SOUTH);
    }

    private class TicTacNoleHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            //Determine which button was pressed
            Object sourceButton = event.getSource();

            //if the reset button was pressed
            if (sourceButton == reset)
            {
                TicTacNole.this.resetBoard(); //reset the board
            }
            else //a board button was clicked
            {
                //loop through the buttons until source of click is found
                for (JButton button : buttons) {
                    if (button == sourceButton) {
                        if (button.getText().equals(" ")) //if the space is empty
                        {
                            if (xGoes) {
                                button.setText("X");
                                statusBar.setText("O's Turn");
                            } else {
                                button.setText("O");
                                statusBar.setText("X's Turn");
                            }
                            xGoes = !xGoes;
                        }
                    }
                }
                //now check to see if the game is in an end state
                GameState state = checkEnd();
                int playAgain;
                switch(state)
                {
                    case CONTINUE:
                        break; //do nothing
                    case DRAW:
                        playAgain = JOptionPane.showConfirmDialog(TicTacNole.this,"It's a draw! " +
                                "Would you like to play again?","Draw",JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if (playAgain == JOptionPane.YES_OPTION)
                        {
                            resetBoard();
                            xGoes = true;
                        }
                        else
                        {
                            System.exit(0);
                        }
                        break;
                    case X_WIN:
                        playAgain = JOptionPane.showConfirmDialog(TicTacNole.this,"X Wins! " +
                                        "Would you like to play again?","X Wins",JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        if (playAgain == JOptionPane.YES_OPTION)
                        {
                            resetBoard();
                            statusBar.setText("X's Turn");
                            xGoes = true;
                        }
                        else
                        {
                            System.exit(0);
                        }
                        break;
                    case O_WIN:
                        playAgain = JOptionPane.showConfirmDialog(TicTacNole.this,"O Wins! " +
                                    "Would you like to play again?","O Wins",JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                        if (playAgain == JOptionPane.YES_OPTION)
                        {
                            resetBoard();
                            xGoes = true;
                        }
                        else
                        {
                            System.exit(0);
                        }
                        break;
                }

            }
        }
    }

    //reset the board and start game over
    private void resetBoard()
    {
        for (JButton button : buttons)
        {
            button.setText(" ");
        }
        xGoes = true;
    }

    private enum GameState {X_WIN,O_WIN,DRAW,CONTINUE}

    //check to see if there is a winner of if the board is a draw
    private GameState checkEnd()
    {
        final int [][] winningCombos = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

        //loop through arrays to see if end game is found
        for (int [] winMatrix : winningCombos)
        {
                if (buttons[winMatrix[0]].getText().equals(buttons[winMatrix[1]].getText()) &&
                        buttons[winMatrix[1]].getText().equals(buttons[winMatrix[2]].getText()) &&
                        !buttons[winMatrix[0]].getText().equals(" "))
                {
                    //a win was found, process appropriately
                    char winningLetter = buttons[winMatrix[0]].getText().charAt(0);
                    switch (winningLetter) {
                        case 'X':
                            statusBar.setText("X Wins!");
                            return GameState.X_WIN;
                        case 'O':
                            statusBar.setText("O Wins!");
                            return GameState.O_WIN;
                        default: //should never reach here
                            System.err.println("Fatal error encountered.");
                            System.exit(1);
                    }
                }
        }
        //No win was found, check to see if the board is full
        boolean isFull = true;
        for (JButton button : buttons)
        {
            if (button.getText().equals(" ")) {
                isFull = false;
                break;
            }
        }
        if (isFull)
            return GameState.DRAW;
        else
            return GameState.CONTINUE;
    }

    //Tic Tac Nole's main class
    public static void main(String[] args)
    {
        TicTacNole game = new TicTacNole();
        game.setVisible(true);
        game.setSize(360,360);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}