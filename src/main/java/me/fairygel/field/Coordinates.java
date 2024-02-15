package me.fairygel.field;

import java.util.Scanner;

public class Coordinates {
    private int x;
    private int y;
    private int direction;
    private static final Scanner scanner = new Scanner(System.in);

    public Coordinates() {
    }

    public static Coordinates getCoordinates(boolean isDirectionNeeded) {
        Coordinates coordinates = new Coordinates();
        //beautiful input and checking for errors
        System.out.print("Enter x(a-p): ");
        int x = scanner.next().toLowerCase().charAt(0) - 'a';
        while (x < 0 || x > 15) {
            System.out.println("x must be between A and P");
            x = scanner.next().toLowerCase().charAt(0) - 'a';
        }
        System.out.print("Enter y(1-16): ");
        int y;
        while (true) {
            if (scanner.hasNextInt()) {
                y = scanner.nextInt() - 1;
                if (y >= 0 && y <= 15) {
                    break; // Exit the loop if y is a valid integer
                } else {
                    System.out.println("y must be between 1 and 16");
                }
            } else {
                System.out.println("invalid input for y. please enter an integer.");
                scanner.next(); // Consume the invalid input to avoid an infinite loop
            }
        }
        coordinates.setX(x);
        coordinates.setY(y);
        if (!isDirectionNeeded) return coordinates;

        System.out.print("Enter direction(h/v): ");
        char choice = scanner.next().charAt(0);
        //error if chosen not v or h
        while (choice != 'v' && choice != 'h') {
            System.out.print("choose between h(horizontal) and v(vertical) placement: ");
            choice = scanner.next().charAt(0);
        }
        coordinates.setDirection(choice == 'v' ? 1 : 0);
        return coordinates;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
