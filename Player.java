package com.college;

import java.util.Stack;

/**
 * A class describing the player that will play Connect4
 */
public abstract class Player {
    public final static char DROP = 'd';
    public final static char VETO = 'v';
    public final static char[] MOVES = {DROP, VETO};

    private char Marker;
    private Stack<String> moveHistory;

    public Player(char Marker) {
        this.Marker = Marker;
        this.moveHistory = new Stack<String>();
    }

    /**
     *
     * @param b The board to check against.
     * @return boolean indicating whether the player can veto or not.
     */
    public boolean canVeto(Board b) {
        // A player can only veto if the following 3 conditions are met:
        //      1: The other player has not just played a veto
        //      2: The players last move was not a veto
        //      3: The veto would completely prevent the other player from making a move
        return !b.containsVeto() &&
                (moveHistory.empty() || !moveHistory.peek().startsWith(String.valueOf(VETO))) &&
                b.numAvailableColumns() > 1;
    }

    /**
     * Make the move m on the board b
     * @param m A string containing the move to be made
     * @param b The board to make the move on
     * @return A boolean indicating if the move was successful
     */
    public boolean makeMove(String m, Board b){
        boolean moveSuccessful = true;

        try {
            b.commitMove(m, this);
            moveHistory.push(m);
        } catch (InvalidMoveException e) {
            System.err.println(e.getMessage());
            moveSuccessful = false;
        } catch (IllegalMoveException e2) {
            System.err.println(e2.getMessage());
            moveSuccessful = false;
        }

        return moveSuccessful;
    }

    /**
     * Generates a move for the player
     * @return the generated move
     */
    public abstract String generateMove(Board b);

    /**
     * Returns the marker identifying the player
     * @return char Marker
     */
    public char getMarker() {
        return Marker;
    }

    @Override
    public String toString() {
        return "Player (" + Marker + ")";
    }
}
