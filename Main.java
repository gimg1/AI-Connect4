package com.college;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("What type of game would you like?");
        System.out.println("1: Human vs. Computer");
        System.out.println("2: Human vs. Human");
        System.out.println("3: Computer vs. Computer");
        System.out.println("Please enter the game type (1/2/3): ");

        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int gameType = 0;
        try {
            gameType = Integer.parseInt(br.readLine());
        } catch(IOException e) {
            System.out.println("There was an error trying to read the game type.");
        }

        Board game = new Board();
        Player[] players = new Player[2];

        switch(gameType) {
            case 1:
                players[0] = new HumanPlayer(Board.MARKERS[0]);
                players[1] = new ComputerPlayer(Board.MARKERS[1]);
                break;
            case 2:
                players[0] = new HumanPlayer(Board.MARKERS[0]);
                players[1] = new HumanPlayer(Board.MARKERS[1]);
                break;
            case 3:
                players[0] = new ComputerPlayer(Board.MARKERS[0]);
                players[1] = new ComputerPlayer(Board.MARKERS[1]);
                break;
            default:
                break;
        }

        String move;

        while(!game.isOver()) {
            for(Player p : players) {
                System.out.println(game);
                do {
                    move = p.generateMove(game);
                } while (!game.isOver() && !p.makeMove(move, game));
            }
        }

        Player winner = game.getWinner();

        if(winner == null) {
            System.out.println("The game was a draw.");
        } else {
            System.out.println("The winner is " + winner + "!");
        }

        System.out.println(game);
    }
}
