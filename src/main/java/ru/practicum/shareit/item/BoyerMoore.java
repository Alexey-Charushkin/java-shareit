package ru.practicum.shareit.item;

import java.util.Arrays;

public class BoyerMoore {
    private int[] shiftTable;

    public int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        // Построение таблицы смещений
        shiftTable = new int[256];
        Arrays.fill(shiftTable, m);
        for (int i = 0; i < m - 1; i++) {
            char c = pattern.charAt(i);
            shiftTable[c] = m - 1 - i;
        }

        // Поиск подстроки
        int i = m - 1;
        while (i < n) {
            int j = m - 1;
            while (j >= 0 && text.charAt(i) == pattern.charAt(j)) {
                i--;
                j--;
            }
            if (j < 0) {
                return i + 1; // Подстрока найдена
            }
            i += Math.max(shiftTable[text.charAt(i)], m - 1 - j);
        }
        return -1; // Подстрока не найдена
    }
}
