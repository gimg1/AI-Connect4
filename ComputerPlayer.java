package com.college;

import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    public ComputerPlayer(char marker) {
        super(marker);
    }

    public String generateMove(Board b) {
        System.out.println("Computer (" + getMarker() + ") is thinking...");

        try {
            Thread.sleep(3000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Set<String> validMoves = b.getValidMoves(canVeto(b));
        int item = new Random().nextInt(validMoves.size());

        return validMoves.toArray(new String[validMoves.size()])[item];
    }
}
