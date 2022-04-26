package it.polimi.ingsw.model;

public abstract class Character{
    public int price;
    private Match match;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public abstract void effect();

    public Match getMatch() {
        return match;
    }

}
