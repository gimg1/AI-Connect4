package com.college;

import java.io.*;

public class HumanPlayer extends Player {
    private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public HumanPlayer(char marker) {
        super(marker);
    }

    public String generateMove(Board b) {
        System.out.print("Player (" + getMarker() + ") enter a move: " );
        String move = null;
        try {
            move = br.readLine();
        } catch(IOException e) {
            System.out.println("There was an error trying to read your move.");
        }

        return move;
    }
}
