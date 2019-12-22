package model;

public class Dice {
    private boolean canUse;
    private boolean isUsed;
    private int value;

    public Dice() {
        this.value = (int) (Math.random() * 6) + 1;
    }

    public boolean canUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
