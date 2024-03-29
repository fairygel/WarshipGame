package me.fairygel.field;

import me.fairygel.players.Bot;
import me.fairygel.players.Player;

public class Field {
    // variables
    private final char[][] cells = new char[16][16]; // O - ship, X - eliminated, # - cant place/missed, . - empty
    private int shipsRemaining;
    private final Player player;

    public Field(Player player) {
        this.player = player;
        generateField();
    }

    public Player getPlayer() {
        return player;
    }
    public int getShipsRemaining() {
        return shipsRemaining;
    }

    public char getCharAt(int x, int y) {
        return cells[y][x];
    }
    // prints
    public String getField() {
        StringBuilder result = new StringBuilder();
        result.append("   ");
        for (int i = 0; i < 16; i++) {
            result.append((char) ('a' + i)).append(" ");
        }
        result.append("%n");
        for (int i = 0; i < 16; i++) {
            if (i < 9) result.append(" ");
            result.append((i + 1)).append(" ");
            for (int j = 0; j < 16; j++)
                result.append(cells[i][j]).append(" ");
            result.append("%n");
        }
        return result.toString();
    }

    public String getBattleField(Field enemyField) { //that displays your and enemy field
        StringBuilder result = new StringBuilder();
        // prints characters from a to p
        result.append("            Your Field:                              Enemy Field:%n");
        result.append("   ");
        for (int i = 0; i < 16; i++) {
            result.append((char) ('a' + i)).append(" ");
        }
        result.append("   |   ");
        result.append("   ");
        for (int i = 0; i < 16; i++) {
            result.append((char) ('a' + i)).append(" ");
        }
        result.append("%n");
        for (int i = 0; i < 16; i++) {
            //player field
            //prints numbers at left side
            if (i < 9) result.append(" ");
            result.append((i + 1)).append(" ");
            //prints field
            for (int j = 0; j < 16; j++) {
                result.append(cells[i][j]).append(" ");
            }
            result.append("   |   ");
            //enemy field
            if (i < 9) result.append(" ");
            result.append((i + 1)).append(" ");
            for (int j = 0; j < 16; j++) {
                if (enemyField.getCharAt(j, i) == 'O') {
                    result.append(". ");
                    continue;
                }
                result.append(enemyField.getCharAt(j, i)).append(" ");
            }
            result.append("%n");
        }
        return result.toString();
    }

    // stuff to place ships
    public void placeShips(boolean isAuto) {
        //cycle, i of which equals 6, 5 5, 4 4 4 ...(or size of our ship )
        for (int i = 6; i >= 1; i--) {
            for (int j = 6; j >= i; j--) {
                //coordinates are x, y, choice(1 or 0) 1 - vertical, 0 - horizontal
                player.displayInfo("you are placing ship with size: %s (%s remaining)%n", i, j - i + 1);
                Coordinates coordinates;
                if (isAuto || player instanceof Bot) {
                    Bot bot = new Bot();
                    coordinates = new Coordinates(bot.getChar(), bot.getInt(), bot.getDirection());
                } else if (i == 1)
                    coordinates = new Coordinates(player.getChar(), player.getInt(), 'v');
                else
                    coordinates = new Coordinates(player.getChar(), player.getInt(), player.getDirection());
                while (!canPlaceShipOn(coordinates, i)) {
                    clearScreen();
                    player.displayInfo("Error on placement.%n");
                    if (isAuto || player instanceof Bot) {
                        Bot bot = new Bot();
                        coordinates = new Coordinates(bot.getChar(), bot.getInt(), bot.getDirection());
                    } else if (i == 1)
                        coordinates = new Coordinates(player.getChar(), player.getInt(), 'v');
                    else
                        coordinates = new Coordinates(player.getChar(), player.getInt(), player.getDirection());
                }
                int x = coordinates.getX();
                int y = coordinates.getY();
                int choice = coordinates.getDirection();
                setAsUnavailableForPlacing(x, y, choice, i);
                placeShip(x, y, choice, i);
                //add ship
                shipsRemaining++;
                clearScreen();
                player.displayInfo(getField());
            }
        }
        removeUnusedChars();
    }

    private boolean canPlaceShipOn(Coordinates coordinates, int size) {
        int x = coordinates.getX();
        int y = coordinates.getY();
        int choice = coordinates.getDirection(); //1 - vertical, 0 - horizontal
        // if horizontal, y side is ignored. if vertical, left ignored
        if ((y + size) * choice > 16 || (x + size) * (choice - 1) < -16) return false;
        for (int i = 0; i < size; i++) {
            // i var doesn't have sense at y, if horizontal, so we will have y+0, same with x
            if (cells[y + (i * choice)][x - (i * (choice - 1))] != '.') return false;
        }
        return true;
    }

    private void setAsUnavailableForPlacing(int x, int y, int choice, int size) {
        int actualSize = size - 1;
        // creates # around future placement of ship
        for (int i = Math.max(0, y - 1); i <= Math.min(15, y + 1 + (actualSize * choice)); i++) {
            for (int j = Math.max(0, x - 1); j <= Math.min(15, x + 1 - (actualSize * (choice - 1))); j++) {
                cells[i][j] = '#';
            }
        }
    }

    private void placeShip(int x, int y, int choice, int shipSize) {
        if (choice == 1) {
            for (int k = 0; k < shipSize; k++) {
                cells[y + k][x] = 'O';
            }
        } else {
            for (int k = 0; k < shipSize; k++) {
                cells[y][x + k] = 'O';
            }
        }
    }

    private void removeUnusedChars() {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
                if (cells[i][j] == '#') cells[i][j] = '.';
    }

    private void generateField() {
        //filling field with dots
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                cells[i][j] = '.';
            }
        }
    }

    public StateOfAttack beAttacked(int x, int y) {
        StateOfAttack state = new StateOfAttack(false, true);
        if (x < 0 || x > 15) return state;
        if (y < 0 || y > 15) return state;

        if (cells[y][x] == '.') {
            cells[y][x] = '#';
            state.setHaveError(false);
        } else if (cells[y][x] == 'O') {
            cells[y][x] = 'X';
            state.setHaveError(false);
            state.setHitted(true);
            killShipIfCan(x, y);
        }
        return state;
    }

    private void killShipIfCan(int x, int y) {//god save me
        int startX = x;
        int startY = y;
        char[] neighbors = getNeighbors(startX, startY);
        while (neighbors[0] != '.' && neighbors[0] != '#') {
            startY--;
            neighbors = getNeighbors(startX, startY);
        }
        while (neighbors[1] != '.' && neighbors[1] != '#') {
            startX--;
            neighbors = getNeighbors(startX, startY);
        }
        int lenOfShip = 1;
        int direction;
        int endX = startX;
        int endY = startY;
        if (neighbors[2] == '.' || neighbors[2] == '#') {
            direction = 0; //horizontal
        } else {
            direction = 1; //vertical
        }
        if (direction == 1) {
            while (neighbors[2] != '.' && neighbors[2] != '#') {
                if (cells[endY][endX] == 'O') return;
                lenOfShip++;
                endY++;
                neighbors = getNeighbors(endX, endY);
            }
        } else {
            while (neighbors[3] != '.' && neighbors[3] != '#') {
                if (cells[endY][endX] == 'O') return;
                lenOfShip++;
                endX++;
                neighbors = getNeighbors(endX, endY);
            }
        }
        setAsUnavailableForPlacing(startX, startY, direction, lenOfShip);
        shipsRemaining--;
        for (int i = startY; i <= endY; i++) {
            for (int j = startX; j <= endX; j++) {
                cells[i][j] = 'X';
            }
        }
    }

    private char[] getNeighbors(int x, int y) {
        char[] neighbors = new char[4];
        int index = 0;

        if (y - 1 >= 0) {
            neighbors[index++] = cells[y - 1][x];
        } else neighbors[index++] = '.';
        if (x - 1 >= 0) {
            neighbors[index++] = cells[y][x - 1];
        } else neighbors[index++] = '.';
        if (y + 1 < 16) {
            neighbors[index++] = cells[y + 1][x];
        } else neighbors[index++] = '.';
        if (x + 1 < 16) {
            neighbors[index] = cells[y][x + 1];
        } else neighbors[index] = '.';
        return neighbors;
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
