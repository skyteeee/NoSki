package com.skyteeee.noski.logic;

public class Cell {
    public boolean isEnabled;
    public String value;

    public int x;
    public int y;
    public float opacity = 1f;
    public float screenX;
    public float screenY;
    public float ogScreenX;
    public float ogScreenY;

    public CellStatus status = CellStatus.NORMAL;

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

    public void pickLetter() {
        String lets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int i = (int) (Math.random()*lets.length());
        value = lets.substring(i, i+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return cell.x == x && cell.y == y;
    }

    public Cell copy() {
        Cell cell = new Cell(x, y, isEnabled);
        cell.screenX = screenX;
        cell.screenY = screenY;
        cell.status = status;
        cell.value = value;
        cell.opacity = opacity;
        return cell;
    }

    @Override
    public int hashCode() {
        return 1000*x + y;
    }

    @Override
    public String toString() {
        return value;
    }

    public int distance(Cell otherCell) {
        return Math.abs(otherCell.x - x) + Math.abs(otherCell.y - y);
    }


    public enum CellStatus {
        NORMAL,
        SELECTED,
        DEAD
    }

}
