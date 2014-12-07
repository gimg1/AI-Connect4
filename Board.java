package com.college;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Class to represent the playing board for Connect4
 */
public class Board {
    public final static int ROWS = 5;
    public final static int COLUMNS = 7;
    public final static char EMPTY = '_';
    public final static String ALL_MARKER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static char[] MARKERS = ALL_MARKER.toCharArray();
    public final static char VETOED = 'X';
    public final static String validMovePattern = "^[dv][1-7]$";
    private static String ROW_SEPARATOR = "";
    private final static String COLUMN_SEPARATOR = "|";
    private final static String SPACE = " ";
    private static String COLUMN_LABELS = "";

    private char[][] board;
    private Stack<String> moveHistory;
    private int numDrops;
    private int numVetos;
    private boolean isOver;
    private Player winner;
    private int vetoedColumn = -1;
    private int movesSinceVeto = 0;

    public Board() {
        numDrops = 0;
        numVetos = 0;
        moveHistory = new Stack<String>();
        board = new char[COLUMNS][ROWS];

        for(char[] column : board) {
            Arrays.fill(column, EMPTY);
        }

        for(int c = 0; c < COLUMNS; c++) {
            ROW_SEPARATOR += "+---";
            COLUMN_LABELS += SPACE + SPACE + (c + 1) + SPACE;
        }
        ROW_SEPARATOR += "+\n";
    }

    /**
     * Gets the list of legal moves that a player can play
     * @param boolean canVeto whether the player can currently veto
     * @return The list of legal moves a player can make
     */
    public Set<String> getValidMoves(boolean canVeto) {
        Set<String> validMoves = new HashSet<String>();
        for(int i = 0; i < board.length; i++) {
            if(board[i][0] == EMPTY) {
                validMoves.add("d" + (i + 1));
                if(canVeto) {
                    validMoves.add("v" + (i + 1));
                }
            }
        }

        return validMoves;
    }

    /**
     * Checks if the board currently contains a veto move
     * @return boolean indicating if the board contains a veto move
     */
    public boolean containsVeto() {
        for(int i = 0; i < board.length; i++) {
            if(board[i][0] == VETOED) {
                return true;
            }
        }

        return false;
    }

    /**
     * Commits a move by a player
     * @param move The move to be committed
     * @param p The player who is committing the move
     * @throws InvalidMoveException If the move is invalid
     * @throws  IllegalMoveException If the move is illegal
     */
    public void commitMove(String move, Player p) throws InvalidMoveException, IllegalMoveException {
        if(getValidMoves(p.canVeto(this)).contains(move)) {
            char[] moveSplit = parseMove(move);
            char action = moveSplit[0];
            int columnIndex = Character.getNumericValue(moveSplit[1]);

            char[] column = board[columnIndex - 1];
            int index = 0;

            if(action == Player.DROP) {
                while(index < ROWS && column[index] == EMPTY) index++;

                column[index - 1] = p.getMarker();
                numDrops++;
            } else if(action == Player.VETO) {
                while(index < ROWS && column[index] == EMPTY) {
                    column[index] = VETOED;
                    index++;
                }
                numVetos++;
                vetoedColumn = columnIndex;
            }

            moveHistory.push(move);

            if(numDrops == ROWS * COLUMNS) {
                isOver = true;
            } else if(action == Player.DROP && maxNumConnected(columnIndex) == 4) {
                isOver = true;
                winner = p;
            }

            if(vetoedColumn != -1) {
                if(movesSinceVeto >= 1) {
                    clearVetos();
                    movesSinceVeto = 0;
                    vetoedColumn = -1;
                } else {
                    movesSinceVeto++;
                }
            }
        } else {
            throw new IllegalMoveException(move);
        }
    }

    private void clearVetos() {
        int r = 0;
        for(int c = 0; c < board.length; c++) {
            while(r < ROWS && board[c][r] == VETOED) board[c][r++] = EMPTY;
            r = 0;
        }
    }

    /**
     * Looks at a column and the last move made in it and counts how many in a row it has.
     * @param col The column to investigate for a connection
     * @return The maximum number of 'in a row' it has.
     */
    private int maxNumConnected(int col) {
        int row = 0;
        do {
            row++;
        } while(board[col - 1][row - 1] == EMPTY);

        Position p = new Position(col, row, this);

        System.err.println(p);

        return p.getMaxScore();
    }

    /**
     * Parses the move into its action & columns parts
     * @param move
     * @return A char array containing the action and column
     * @throws InvalidMoveException
     */
    public char[] parseMove(String move) throws InvalidMoveException {
        if(move.matches(validMovePattern)) {
             return move.toCharArray();
        } else {
            throw new InvalidMoveException(move);
        }
    }

    public boolean isOver() {
        return isOver;
    }

    public Player getWinner() {
        return winner;
    }

    @Override
    public String toString() {
        String boardString = "";

        for(int r = 0; r < ROWS; r++) {
            boardString += ROW_SEPARATOR;

            for(int c = 0; c < COLUMNS; c++) {
                boardString += COLUMN_SEPARATOR + SPACE + board[c][r] + SPACE;
            }

            boardString += COLUMN_SEPARATOR + "\n";
        }

        boardString += ROW_SEPARATOR;
        boardString += COLUMN_LABELS;

        return boardString;
    }

    public int numAvailableColumns() {
        int availableColumns = 0;
        for(int i = 0; i < board.length; i++) {
            if(board[i][0] == EMPTY || board[i][0] == VETOED) {
                availableColumns++;
            }
        }

        return availableColumns;
    }

    public char[][] asCharArray() {
        return this.board;
    }
}
