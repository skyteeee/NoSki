package com.skyteeee.noski.logic;

public class Cell {
    public boolean isEnabled;
    public String value;

    public int x;
    public int y;


    public Cell(int x, int y,boolean enable) {
        isEnabled = enable;
        this.x = x;
        this.y = y;
        //pickLetter();
    }

    public Cell(int x, int y) {
        isEnabled = true;
        this.x = x;
        this.y = y;
        //pickLetter();
    }

    private void pickLetter() {
        String lets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int i = (int) (Math.random()*lets.length());
        value = lets.substring(i, i+1);
    }

}
