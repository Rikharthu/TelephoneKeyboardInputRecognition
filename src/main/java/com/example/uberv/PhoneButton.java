package com.example.uberv;

import java.util.Arrays;
import java.util.List;

public class PhoneButton {
    private final int number;
    private final List<String> characters;

    public PhoneButton(int number, String... characters) {
        this.number = number;
        this.characters = Arrays.asList(characters);
    }

    public int getNumber() {
        return number;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public boolean hasCharacter(String character) {
        return characters.contains(character);
    }
}
