package me.fairygel;

import me.fairygel.field.Coordinates;
import me.fairygel.field.Field;
import me.fairygel.field.StateOfAttack;
import me.fairygel.players.Bot;
import me.fairygel.players.Player;
import me.fairygel.players.Server;

public class WarshipGame {
    private Field playerField;
    private Field enemyField;
    private final Player player;
    private Player enemy;

    public WarshipGame() {
        player = new Server();
    }

    public void initialize() {
        System.out.println("Welcome to Warship Game!");
        System.out.print("To start, enter your name: ");
        player.setName(player.getString());
        System.out.println("Your name: " + player.getName());
        System.out.print("Do you like to play with Player or with Bot? (p/b): ");
        char choice = player.getString().charAt(0);
        while (choice != 'p' && choice != 'b') {
            System.out.println("If you want to play with Player, type p, if with bot - b. ");
            System.out.print("Do you like to play with Player or with Bot? (p/b): ");
            choice = player.getString().charAt(0);
        }
        if (choice == 'b') enemy = new Bot();
        else {
            //TODO
        }
        playerField = new Field(player);
        placeShips(playerField);
        enemyField = new Field(enemy);
        placeShips(enemyField);
    }

    public void start() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
        print("Battle begins!%n");
        // if someone doesn't have ships, so, why we should play? main cycle
        while (playerField.getShipsRemaining() > 0 || enemyField.getShipsRemaining() > 0) {
            String move = "%s move:%n";
            print(move, player.getName());
            startTurn(enemyField, playerField);
            if (enemyField.getShipsRemaining() < 1) break;
            print(move, enemy.getName());
            startTurn(playerField, enemyField);
        }
        print("Game Over!%n");
        displayResult();
    }

    private void displayResult() {
        if (enemyField.getShipsRemaining() < 1) {
            player.displayInfo("You win!%n");
            enemy.displayInfo("You lose!%n");
        } else {
            enemy.displayInfo("You win!%n");
            player.displayInfo("You lose!%n");
        }
    }

    private void startTurn(Field whoIsAttacked, Field attacking) {
        displayField(attacking);
        attack(whoIsAttacked, attacking);
    }

    private void displayField(Field attacking) {
        Player attackingPlayer = attacking.getPlayer();
        attackingPlayer.displayInfo("Your field:%n");
        attackingPlayer.displayInfo(attacking.getField());
    }

    private void attack(Field whoIsAttacked, Field attacking) {
        Player attackingPlayer = attacking.getPlayer();
        attackingPlayer.displayInfo("Enemy field:%n");
        attackingPlayer.displayInfo(whoIsAttacked.getFieldWithoutShips());
        // get coordinates. if error while attacking, you should enter coordinates again.
        attackingPlayer.displayInfo("Enter Coordinates of attack:%n");
        Coordinates coordinates = new Coordinates(attackingPlayer.getChar(), attackingPlayer.getInt());
        StateOfAttack state;
        while (true) {
            state = whoIsAttacked.beAttacked(coordinates.getX(), coordinates.getY());
            if (state.isHaveError()) {
                attackingPlayer.displayInfo("you typed not valid coordinates.%n");
                attackingPlayer.displayInfo("Enter Coordinates of attack:%n");
                coordinates = new Coordinates(attackingPlayer.getChar(), attackingPlayer.getInt());
            } else break;
        }
        // if was hitted, start turn again
        if (state.isHitted()) {
            attackingPlayer.displayInfo("you hitted on enemy ship!");
            attackingPlayer.displayInfo(whoIsAttacked.getFieldWithoutShips());
            startTurn(whoIsAttacked, attacking);
        } else {
            attackingPlayer.displayInfo("missed.");
        }
    }

    private void placeShips(Field field) {
        Player fieldPlayer = field.getPlayer();
        fieldPlayer.displayInfo("start placing your ships:%n");
        fieldPlayer.displayInfo(field.getField());
        field.placeShips();
        fieldPlayer.displayInfo("your final field:");
        fieldPlayer.displayInfo(field.getField());
    }

    private void print(String str, Object... args) {
        player.displayInfo(str, args);
        enemy.displayInfo(str, args);
    }
}
