package me.fairygel.players;

public interface Player {
    void displayInfo(String info, Object... args);

    int getInt();

    String getString();

    char getDirection();

    char getChar();

    void setName(String name);

    String getName();
}
