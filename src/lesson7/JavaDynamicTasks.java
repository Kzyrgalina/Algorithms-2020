package lesson7;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя 6
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    //Трудоемкость O(N * M), Ресурсоемкость O(N * M), где N - first.length, M - second.length
    public static String longestCommonSubSequence(String first, String second) {
        int fl = first.length();
        int sl = second.length();
        int[][] matrix = new int[fl + 1][sl + 1];

        for (int i = 0; i < fl + 1; i++) {
            for (int j = 0; j < sl + 1; j++) {
                if (i == 0 || j == 0) matrix[i][j] = 0;
                else if (first.charAt(i - 1) == second.charAt(j - 1)) matrix[i][j] = matrix[i - 1][j - 1] + 1;
                     else matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (fl > 0 && sl > 0){
            if (matrix[fl][sl] == matrix[fl - 1][sl]) sl++;
            else if (matrix[fl][sl - 1] == matrix[fl][sl]) fl++;
                 else sb.append(first.charAt(fl - 1));
                 fl--;
                 sl--;
        }

        return sb.reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная 7
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    //Трудоемкость O(n^2) Ресурсоемкость O(n)
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        int size = list.size();
        AbstractList<Integer> answer = new ArrayList<>();
        int[] index = new int[size];
        int[] recAnswer = new int[size];
        int lengthMax = 0;
        int indexMax = 0;

        if (list.size() < 2) return list;

        for (int i = 0; i < size; i++) {
            recAnswer[i] = -1;
            index[i]  = 1;
            for (int j = 0; j < i; j++) {
                if (list.get(j) < list.get(i) && index[j] + 1 > index[i]){
                    index[i] = index[j] + 1;
                    recAnswer[i] = j;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            if (index[i] > lengthMax){
                lengthMax = index[i];
                indexMax = i;
            }
        }
        while (indexMax > -1){
            answer.add(list.get(indexMax));
            indexMax = recAnswer[indexMax];
        }

        Collections.reverse(answer);
        return answer;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя 5
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
