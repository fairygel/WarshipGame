package me.fairygel;

import me.fairygel.field.Coordinates;
import me.fairygel.field.Field;
import me.fairygel.field.StateOfAttack;

import java.util.Scanner;

public class WarshipGame {
    private static final Scanner scanner = new Scanner(System.in);
    private Field playerField;
    private Field enemyField;

    public void initialize() {
        playerField = createField();
        placeShips(playerField);

        enemyField = createField();
        placeShips(enemyField);
    }

    private Field createField() {
        System.out.print("Enter your name: ");
        String playerName = scanner.next();
        System.out.printf("You are %s. Place your ships: %n", playerName);
        Field field = new Field();
        field.setPlayerName(playerName);
        return field;
    }

    public void start() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
        System.out.println("Battle begins!");
        // if someone doesn't have ships, so, why we should play? main cycle
        while (playerField.getShipsRemaining() > 0 || enemyField.getShipsRemaining() > 0) {
            System.out.printf("%s move:", playerField.getPlayerName());
            startTurn(enemyField, playerField);
            if (enemyField.getShipsRemaining() < 1) break;
            System.out.printf("%s move:", enemyField.getPlayerName());
            startTurn(playerField, enemyField);
        }
        System.out.println("Game Over!");
        displayResult();
    }

    private void displayResult() {
        if (enemyField.getShipsRemaining() < 1) System.out.printf("%s wins!%n", playerField.getPlayerName());
        else System.out.printf("%s wins!%n", enemyField.getPlayerName());
    }

    private void startTurn(Field whoIsAttacked, Field attacking) {
        if (getChoice() == 'v') {
            displayField(whoIsAttacked, attacking);
        } else {
            attack(whoIsAttacked, attacking);
        }
    }

    private void displayField(Field whoIsAttacked, Field attacking) {
        System.out.println("your field");
        attacking.printField();
        startTurn(whoIsAttacked, attacking);
    }

    private void attack(Field whoIsAttacked, Field attacking) {
        System.out.println("enemy field:");
        whoIsAttacked.printFieldWithoutShips();
        // get coordinates. if error while attacking, you should enter coordinates again.
        Coordinates coordinates = Coordinates.getCoordinates(false);
        StateOfAttack state;
        while (true) {
            state = whoIsAttacked.beAttacked(coordinates.getX(), coordinates.getY());
            if (state.isHaveError()) {
                System.out.println("you typed not valid coordinates.");
                coordinates = Coordinates.getCoordinates(false);
            } else break;
        }
        // if was hitted, start turn again
        if (state.isHitted()) {
            System.out.println("you hitted!");
            whoIsAttacked.printFieldWithoutShips();
            startTurn(whoIsAttacked, attacking);
        } else {
            System.out.println("missed.");
        }
    }

    private char getChoice() {
        System.out.println("Attack or view your field?(a/v)");
        char choice = scanner.next().charAt(0);
        while (choice != 'a' && choice != 'v') {
            System.out.println("Use v, if you want to see your field, a - to attack enemy.");
            choice = scanner.next().charAt(0);
        }
        return choice;
    }

    private void placeShips(Field field) {
        System.out.println("start placing your ships: ");
        field.printField();
        field.placeShips();
        System.out.println("your final field:");
        field.printField();
        System.out.print("press any character to continue: ");
        scanner.next();
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }
}
