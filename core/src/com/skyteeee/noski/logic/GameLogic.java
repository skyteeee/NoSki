package com.skyteeee.noski.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class GameLogic {
    private Cell[][] field;
    public int width;
    public int height;
    Random rnd = new Random(System.currentTimeMillis());
    private Map<Integer, List<String>> nouns = new HashMap<>();

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

    private void generateField(int minLetters, int maxLetters, int maxBends, int maxWordAmount, boolean allowBackwards) {
        List<String> wordBank = new ArrayList<>();

        List<Cell> empties = new ArrayList<>(width * height);
        for (Cell[] col : field) {
            empties.addAll(Arrays.asList(col));
        }

        for (int i = 0; i < maxWordAmount; i++) {
            int letters = rnd.nextInt(minLetters, maxLetters);
            List<String> words = nouns.get(letters);
            String word = words.get(rnd.nextInt(words.size()));

            Cell start = empties.get(rnd.nextInt(empties.size()));
            tryPlace(word, start, maxBends, allowBackwards);
        }
    }

    private List<Cell> tryPlace(String word, Cell start, int maxBends, boolean allowBackwards) {
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
            Set<Integer> dirsUsed = new HashSet<>();
            int dirIdx = rnd.nextInt(maxDirs);
            int[] delta = dirs[dirIdx];
            do {
                next = getNearCell(current, delta);
                dirsUsed.add(dirIdx);
                dirIdx = (dirIdx+1) % maxDirs;
                delta = dirs[dirIdx];
            } while ((next == null || next.value != null) && dirsUsed.size() < maxDirs);
            if (next == null || next.value != null) return null;
            path.add(next);
            current = next;
            maxDirs = 4;
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

}
