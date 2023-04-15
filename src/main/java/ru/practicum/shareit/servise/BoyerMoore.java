package ru.practicum.shareit.servise;

import java.util.Arrays;

public class BoyerMoore {

    public int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        // Построение таблицы смещений
        int[] shiftTable = new int[1280];
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
