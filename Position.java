package com.college;

public class Position {
    private int column;
    private int row;
    private Board board;

    public Position(int column, int row, Board board) {
        this.column = column - 1;
        this.row = row - 1;
        this.board = board;
    }

    public int getVerticalScore() {
        char[][] charBoard = board.asCharArray();
        char piece = charBoard[column][row];
        int pieceCounter = 0, verticalRow, maxConnected = 0;

        for(verticalRow = 0; verticalRow < Board.ROWS; verticalRow++) {
            if(charBoard[column][verticalRow] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxConnected) maxConnected = pieceCounter;
        }

        return maxConnected;
    }

    public int getHorizontalScore() {
        char[][] charBoard = board.asCharArray();
        char piece = charBoard[column][row];
        int pieceCounter = 0, horizontalCol, maxConnected = 0;

        for(horizontalCol = 0; horizontalCol < Board.COLUMNS; horizontalCol++) {
            if(charBoard[horizontalCol][row] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxConnected) maxConnected = pieceCounter;
        }

        return maxConnected;
    }

    public int getTopLeftDownScore() {
        char[][] charBoard = board.asCharArray();
        char piece = charBoard[column][row];
        int pieceCounter = 0;
        int maxConnected = 0;
        int topDownCol = column - row;
        int topDownRow = row - column;

        if(topDownCol >= Board.COLUMNS) topDownCol = Board.COLUMNS - 1;
        else if (topDownCol < 0) topDownCol = 0;

        if(topDownRow >= Board.ROWS) topDownRow = Board.ROWS - 1;
        else if (topDownRow < 0) topDownRow = 0;

        while(topDownCol < Board.COLUMNS && topDownRow < Board.ROWS) {
            if(charBoard[topDownCol][topDownRow] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxConnected) maxConnected = pieceCounter;
            topDownCol++;
            topDownRow++;
        }

        return maxConnected;
    }

    public int getBottomLeftUpScore() {
        char[][] charBoard = board.asCharArray();
        char piece = charBoard[column][row];
        int pieceCounter = 0;
        int maxConnected = 0;
        int bottomUpCol = column - ((Board.ROWS - 1) - row);
        int bottomUpRow = Board.ROWS - 1;

        if(bottomUpCol >= Board.COLUMNS) bottomUpCol = Board.COLUMNS - 1;
        else if (bottomUpCol < 0) bottomUpCol = 0;

        while(bottomUpCol < Board.COLUMNS && bottomUpRow >= 0) {
            if(charBoard[bottomUpCol][bottomUpRow] == piece) {
                pieceCounter++;
            } else {
                pieceCounter = 0;
            }

            if(pieceCounter > maxConnected) maxConnected = pieceCounter;
            bottomUpCol++;
            bottomUpRow--;
        }

        return maxConnected;
    }

    @Override
    public String toString() {
        return "Position(" + (column + 1) + ", " + (row + 1) +") has scores: " + "\n" +
                "Vertical: " + getVerticalScore() + "\n" +
                "Horizontal: " + getHorizontalScore() + "\n" +
                "Top Left to Bottom Right: " + getTopLeftDownScore() + "\n" +
                "Bottom Left to Top Right: " + getBottomLeftUpScore() + "\n";
    }

    public int getMaxScore() {
        return Math.max(
            Math.max(this.getVerticalScore(), this.getHorizontalScore()),
            Math.max(this.getTopLeftDownScore(), this.getBottomLeftUpScore()));
    }
}
