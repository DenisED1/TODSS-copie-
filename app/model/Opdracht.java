package model;

import java.util.ArrayList;
import java.util.List;

public class Opdracht {
    private Woord currentWoord;

    private List<Woord> woorden;

    private int aantalGoed = 0;

    private int aantalFout = 0;

    public Opdracht() {
        this(new ArrayList<>());
    }

    public Opdracht(List<Woord> woorden) {
        this.woorden = woorden;
    }

    public void setWoorden(List<Woord> woorden) {
        this.woorden = woorden;
    }

    public boolean hasNextWoord(){
        return woorden.size() > 0;
    }

    public Woord getNextWoord(){
        currentWoord = woorden.get(0);
        woorden.remove(0);
        return currentWoord;
    }

    public List<Woord> getWoorden() {
        return woorden;
    }

    public Woord getCurrentWoord() {
        return currentWoord;
    }

    public int getAantalWoordenOver(){
        return woorden.size();
    }

    public boolean geefAntwoord(String antwoord){
        if(currentWoord.getWoord().equals(antwoord)){
            aantalGoed += 1;
            return true;
        }

        aantalFout += 1;
        return false;
    }
    public boolean geefAntwoord(String antwoord, String correctAntwoord){
        if(correctAntwoord.equals(antwoord)){
            aantalGoed += 1;
            return true;
        }

        aantalFout += 1;
        return false;
    }

    public int getAantalGoed(){
        return aantalGoed;
    }

    public int getAantalFout(){

        return aantalFout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Opdracht opdracht = (Opdracht) o;

        if (woorden != null ? !woorden.equals(opdracht.woorden) : opdracht.woorden != null) return false;
        return currentWoord != null ? currentWoord.equals(opdracht.currentWoord) : opdracht.currentWoord == null;
    }
    @Override
    public int hashCode() {
        int result = woorden != null ? woorden.hashCode() : 0;
        result = 31 * result + (currentWoord != null ? currentWoord.hashCode() : 0);
        return result;
    }
}
