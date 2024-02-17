package me.fairygel.players;

import java.util.Random;

public class Bot implements Player {
    private String name = "bot";
    private final Random random = new Random();

    @Override
    public void displayInfo(String info, Object... args) {
        //no sense to show some info for bot, so it is empty
    }

    @Override
    public int getInt() {
        return random.nextInt(16) + 1;
    }

    @Override
    public String getString() {
        return "bot";
    }

    @Override
    public char getChar() {
        return (char) (random.nextInt(16) + 'a');
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public char getDirection() {
        return random.nextInt(2) == 1 ? 'v' : 'h';
    }
}
