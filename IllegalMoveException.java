package com.college;

public class IllegalMoveException extends Exception {
    private final String move;

    public IllegalMoveException(String move) {
        this(move, "The move " + move + " is illegal.");
    }

    public IllegalMoveException(String move, String message) {
        super(message);
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}
