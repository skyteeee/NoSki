package com.skyteeee.noski.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class GameLogic {

    static final boolean WORD_DEBUG = false;
    private Cell[][] field;
    public int width;
    public int height;
    Random rnd = new Random(System.currentTimeMillis());
    private Map<Integer, List<String>> nouns = new HashMap<>();

    public int score = 0;
    public int level = 0;
    public int wordsFound = 0;

    public List<String> wordBank;

    public GameLogic(int fieldWidth, int fieldHeight) {
        field = new Cell[fieldWidth][fieldHeight];
        width = fieldWidth;
        height = fieldHeight;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                field[x][y] = new Cell(x,y);
            }
        }

        loadNouns();

    }

    public void clearField() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                field[x][y].clear();
            }
        }
    }

    public boolean allFound() {
        return wordsFound == wordBank.size();
    }

    public void newLevel() {
        int lvlCycle = 3;

        int minA = 3;
        int minB = 8;

        int maxA = 6;
        int maxB = 12;

        int minLetters = minA + (level % lvlCycle) * (minB - minA) / (lvlCycle - 1);
        int maxLetters = maxA + (level % lvlCycle) * (maxB - maxA) / (lvlCycle - 1);;
        int maxWordAmount = Math.min(12, 2 + level/lvlCycle);

        level++;
        wordsFound = 0;

        wordBank = generateField(minLetters, maxLetters, 0, maxWordAmount, false);
        Collections.sort(wordBank);
        System.out.println("Level Word Bank:");
        System.out.println(wordBank);


    }

    public int checkSelection(List<Cell> selection) {
        StringBuilder wordBuilder = new StringBuilder();
        for (Cell c : selection) {
            wordBuilder.append(c.value.charAt(0));
        }
        String word = wordBuilder.toString().toLowerCase();

        return wordBank.indexOf(word);
    }

    public void onMatch(int wordIdx) {
        wordsFound++;
        score += wordBank.get(wordIdx).length() * 99 + wordIdx;
    }

    private void loadNouns() {
        FileHandle file = Gdx.files.internal("nouns.txt");
        File actualFile = file.file();
        try {
            Scanner scanner = new Scanner(file.read());
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                word = word.trim();
                int length = word.length();
                if (length == 0) continue;
                if (!nouns.containsKey(length)) {
                    nouns.put(length, new ArrayList<>());
                }
                nouns.get(length).add(word);
            }
            scanner.close();

        } catch (Exception ignored) {
            System.out.println("ERROR! ");
            ignored.printStackTrace();
        }
    }

    private List<String> generateField(int minLetters, int maxLetters, int maxBends, int maxWordAmount, boolean allowBackwards) {
        List<String> wordBank = new ArrayList<>();

        List<Cell> empties = new ArrayList<>(width * height);
        for (Cell[] col : field) {
            for (Cell cell : col) {
                if (cell.value == null) {
                    empties.add(cell);
                }
            }
        }

        for (int i = 0; i < maxWordAmount; i++) {
            int letters = rnd.nextInt(minLetters, maxLetters);
            List<String> words = nouns.get(letters);
            String word = words.get(rnd.nextInt(words.size()));

            List<Cell> path = null;
            Set<Cell> cellsUsed = new HashSet<>();
            List<Cell> tempEmpties = new ArrayList<>(empties);
            do {
                Cell start = tempEmpties.remove(rnd.nextInt(tempEmpties.size()));
                if (cellsUsed.contains(start)) continue;
                path = tryPlace(word, start, maxBends, allowBackwards, cellsUsed);

            } while (path == null && !tempEmpties.isEmpty());
            if (path != null) {
                empties.removeAll(path);
                wordBank.add(word);
                for (int j = 0; j < path.size(); j++) {
                    Cell cell = path.get(j);
                    cell.parentWord = word;
                    cell.value = word.substring(j, j+1).toUpperCase() + (WORD_DEBUG ? i : "");
                    System.out.println("Path #" + i + ": (" + cell.x + ", " + cell.y + ") = " + cell.value);
                }
            } else {
                System.out.println("Could not fit word #" + i + " : " + word);
            }
        }
        for (Cell c : empties) {
            c.pickLetter();
        }
        return wordBank;
    }

    private List<Cell> tryPlace(String word, Cell start, int maxBends, boolean allowBackwards, Set<Cell> cellsUsed) {
        List<Cell> path = new ArrayList<>();
        int[][] dirs = {
                {1,0},
                {0,1},
                {-1,0},
                {0,-1}
        };
        Cell current = start;
        Cell next;
        path.add(current);
        int maxDirs = allowBackwards ? 4 : 2;

        while(path.size() < word.length()) {
            cellsUsed.add(current);
            int dirsUsed = 0;
            int dirIdx = rnd.nextInt(maxDirs);
            int[] delta = dirs[dirIdx];
            do {
                next = getNearCell(current, delta);
                dirsUsed++;
                dirIdx = (dirIdx + 1) % maxDirs;
                delta = dirs[dirIdx];
            } while ((next == null || next.value != null || cellsUsed.contains(next))
                    && dirsUsed < maxDirs);
            if (next == null || next.value != null  || cellsUsed.contains(next)) {
                path.remove(path.size()-1);
                if (path.isEmpty()) return null;
                current = path.get(path.size()-1);
            } else {
                path.add(next);
                current = next;
                maxDirs = 4;
                cellsUsed.add(current);
            }
        }

        return path;
    }

    private Cell getNearCell(Cell current, int[] delta) {
        int newX = current.x + delta[0];
        int newY = current.y + delta[1];
        if (newX < 0 || newX >= width || newY < 0 || newY >= height) return null;
        return field[newX][newY];
    }




    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    public String getWord(int idx) {
        return wordBank.get(idx);
    }

}
