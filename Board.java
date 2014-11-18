package com.college;

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
                validMoves.add("d" + i);
                if(canVeto) {
                    validMoves.add("v" + i);
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
            }

            moveHistory.push(move);

            if(numDrops == ROWS * COLUMNS) {
                isOver = true;
            } else if(action == Player.DROP && maxNumConnected(columnIndex) == 4) {
                isOver = true;
                winner = p;
            }
        } else {
            throw new IllegalMoveException(move);
        }
    }

    private int maxNumConnected(int col) {
        int column = col - 1;
        int row = 0;
        while(board[column][row] == EMPTY) row++;

        char piece = board[column][row];
        int pieceCounter = 0;
        int maxNumConnected = 0;

        int verticalRow;

        for(verticalRow = 0; verticalRow < ROWS; verticalRow++) {
            if(board[column][verticalRow] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxNumConnected) maxNumConnected = pieceCounter;
        }

        pieceCounter = 0;
        int horizontalCol;

        for(horizontalCol = 0; horizontalCol < COLUMNS; horizontalCol++) {
            if(board[horizontalCol][row] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxNumConnected) maxNumConnected = pieceCounter;
        }

        pieceCounter = 0;
        int topDownCol = column - row;
        int topDownRow = 0;
        if(topDownCol >= COLUMNS) topDownCol = COLUMNS - 1;
        else if (topDownCol < 0) topDownCol = 0;

        while(topDownCol < COLUMNS && topDownRow < ROWS) {
            if(board[topDownCol][topDownRow] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxNumConnected) maxNumConnected = pieceCounter;
            topDownCol++;
            topDownRow++;
        }

        pieceCounter = 0;
        int bottomUpCol = column - ((ROWS - 1) - row);
        int bottomUpRow = ROWS - 1;
        if(bottomUpCol >= COLUMNS) bottomUpCol = COLUMNS - 1;
        else if (bottomUpCol < 0) bottomUpCol = 0;

        while(bottomUpCol < COLUMNS && bottomUpRow >= 0) {
            if(board[bottomUpCol][bottomUpRow] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxNumConnected) maxNumConnected = pieceCounter;
            bottomUpCol++;
            bottomUpRow--;
        }

        return maxNumConnected;
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
}
