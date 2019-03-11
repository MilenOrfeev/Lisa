package com.example.milen.hope;

// TODO: Exceptions, check ranks.
public class RankedItem {
    private short rank;
    private final String description;

    RankedItem(String description, short rank) {
        this.description = description;
        this.rank = rank;
    }


    public String getDescription() {
        return description;
    }
    public short getRank() {
        return rank;
    }

    public void setRank(short rank) {

        this.rank = rank;
    }

    @Override public String toString() {
        return description + " " + rank;
    }
}
