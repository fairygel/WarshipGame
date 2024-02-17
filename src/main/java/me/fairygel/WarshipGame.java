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
        String playerName = player.getString();
        while (playerName.length() < 3 || playerName.length() > 20) {
            System.out.println("Name length must be between 3 and 20 characters.");
            System.out.print("Enter your name: ");
            playerName = player.getString();
        }
        player.setName(playerName);
        System.out.println("Your name: " + player.getName());
        System.out.print("Do you like to play with Player or with Bot? (p/b): ");
        String choice = player.getString();
        while (!choice.isEmpty() && choice.charAt(0) != 'p' && choice.charAt(0) != 'b') {
            System.out.println("If you want to play with Player, type p, if with bot - b. ");
            System.out.print("Do you like to play with Player or with Bot? (p/b): ");
            choice = player.getString();
        }
        if (choice.charAt(0) == 'b') enemy = new Bot();
        else {
            enemy = new Server();
            System.out.print("Enter your name, second player: ");
            String enemyName = player.getString();
            while (enemyName.length() < 3 || enemyName.length() > 20) {
                System.out.println("Name length must be between 3 and 20 characters.");
                System.out.print("Enter your name: ");
                enemyName = player.getString();
            }
            enemy.setName(enemyName);
            System.out.println("Your name: " + enemy.getName());
        }
        playerField = new Field(player);
        placeShips(playerField);
        enemyField = new Field(enemy);
        placeShips(enemyField);
    }

    public void start() {
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
        attack(whoIsAttacked, attacking);
    }

    private void attack(Field whoIsAttacked, Field attacking) {
        Player attackingPlayer = attacking.getPlayer();
        attackingPlayer.displayInfo(attacking.getBattleField(whoIsAttacked));
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
            attackingPlayer.displayInfo(attacking.getBattleField(whoIsAttacked));
            startTurn(whoIsAttacked, attacking);
        } else {
            if (enemy instanceof Server) {
                for (int i = 0; i < 20; i++) {
                    print("%n");
                }
                print("You missed. Show screen to another player. If you is another player, send any character to continue: ");
                attacking.getPlayer().getString();
            }
        }
    }

    private void placeShips(Field field) {
        Player fieldPlayer = field.getPlayer();
        fieldPlayer.displayInfo("start placing your ships, %s:%n", fieldPlayer.getName());
        fieldPlayer.displayInfo(field.getField());
        field.placeShips();
        fieldPlayer.displayInfo("your final field:%n");
        fieldPlayer.displayInfo(field.getField());
        fieldPlayer.displayInfo("Enter anything to continue: ");
        fieldPlayer.getString();
        for (int i = 0; i < 20; i++) {
            fieldPlayer.displayInfo("%n");
        }
    }

    private void print(String str, Object... args) {
        player.displayInfo(str, args);
        if (!(enemy instanceof Server)) enemy.displayInfo(str, args);
    }
}
