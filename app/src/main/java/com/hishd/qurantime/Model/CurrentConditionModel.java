package com.hishd.qurantime.Model;

public class CurrentConditionModel {
    private boolean headache;
    private boolean cough;
    private boolean shortnessOfBreath;
    private boolean soreTroat;
    private boolean fever;

    public boolean isHeadache() {
        return headache;
    }

    public void setHeadache(boolean headache) {
        this.headache = headache;
    }

    public boolean isCough() {
        return cough;
    }

    public void setCough(boolean cough) {
        this.cough = cough;
    }

    public boolean isShortnessOfBreath() {
        return shortnessOfBreath;
    }

    public void setShortnessOfBreath(boolean shortnessOfBreath) {
        this.shortnessOfBreath = shortnessOfBreath;
    }

    public boolean isSoreTroat() {
        return soreTroat;
    }

    public void setSoreTroat(boolean soreTroat) {
        this.soreTroat = soreTroat;
    }

    public boolean isFever() {
        return fever;
    }

    public void setFever(boolean fever) {
        this.fever = fever;
    }
}
