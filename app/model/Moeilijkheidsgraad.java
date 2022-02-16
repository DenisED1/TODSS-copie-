package model;

import java.util.ArrayList;
import java.util.List;

public enum Moeilijkheidsgraad {
    Makkelijk("Makkelijk"),
    Moeilijk("Moeilijk");

    private String text;

    Moeilijkheidsgraad(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static List<String> getMoeilijkheidsgraden(){
        List<String> moeilijkheidsgraden = new ArrayList<>();

        for(Moeilijkheidsgraad moeilijkheidsgraad : Moeilijkheidsgraad.values()){
            moeilijkheidsgraden.add(moeilijkheidsgraad.name());
        }

        return moeilijkheidsgraden;
    }

    public static Moeilijkheidsgraad fromString(String text) {
        for (Moeilijkheidsgraad moeilijkheidsgraad : Moeilijkheidsgraad.values()) {
            if (moeilijkheidsgraad.text.equalsIgnoreCase(text)) {
                return moeilijkheidsgraad;
            }
        }
        throw new IllegalArgumentException("Geen moeilijkheidsgraag gevonden met " + text);
    }
}
