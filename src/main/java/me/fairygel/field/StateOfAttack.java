package me.fairygel.field;

public class StateOfAttack {
    private boolean isHitted;
    private boolean isHaveError;

    public StateOfAttack(boolean isHitted, boolean isHaveError) {
        this.isHitted = isHitted;
        this.isHaveError = isHaveError;
    }

    public boolean isHitted() {
        return isHitted;
    }

    public void setHitted(boolean isHitted) {
        this.isHitted = isHitted;
    }

    public boolean isHaveError() {
        return isHaveError;
    }

    public void setHaveError(boolean isHaveError) {
        this.isHaveError = isHaveError;
    }
}
