package com.skyteeee.noski.logic;

public class Cell {
    public boolean isEnabled;
    public String value;

    public Cell(boolean enable) {
        isEnabled = enable;
        pickLetter();
    }

    public Cell() {
        isEnabled = true;
        pickLetter();
    }

    private void pickLetter() {
        String lets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int i = (int) (Math.random()*lets.length());
        value = lets.substring(i, i+1);
    }

}
