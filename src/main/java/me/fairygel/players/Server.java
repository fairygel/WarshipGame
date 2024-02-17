package me.fairygel.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server implements Player {

    private String name;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void displayInfo(String info, Object... args) {
        System.out.printf(info, args);
    }

    @Override
    public int getInt() {
        int userInput;
        while (true) {
            try {
                System.out.print("Enter Y(1-16): ");
                String input = reader.readLine();
                userInput = Integer.parseInt(input);
                while (userInput < 1 || userInput > 16) {
                    System.out.println("Y must be between 1 and 16.");
                    System.out.print("Enter Y(1-16): ");
                    input = reader.readLine();
                    userInput = Integer.parseInt(input);
                }
                return userInput - 1;
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    @Override
    public String getString() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public char getDirection() {
        try {
            System.out.print("Vertical or Horizontal(v/h)? ");
            char direction = reader.readLine().charAt(0);
            while (direction != 'v' && direction != 'h') {
                System.out.println("To place ship Vertically, type v or h for Horizontal.");
                System.out.print("Vertical or Horizontal(v/h)? ");
                direction = reader.readLine().charAt(0);
            }
            return direction;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public char getChar() {
        try {
            System.out.print("Enter X(a-p): ");
            char character = reader.readLine().toLowerCase().charAt(0);
            while (character < 'a' || character > 'p') {
                System.out.println("Y must be between a and p.");
                System.out.print("Enter X(a-p): ");
                character = reader.readLine().toLowerCase().charAt(0);
            }
            return character;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
