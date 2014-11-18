package com.college;

public class InvalidMoveException extends Exception {
    private final String move;

    public InvalidMoveException(String move) {
        this(move, "The move " + move + " is invalid.");
    }

    public InvalidMoveException(String move, String message) {
        super(message);
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}
