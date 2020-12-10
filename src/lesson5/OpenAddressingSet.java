package lesson5;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private Object removedObj = new Object();

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != removedObj) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */

    /*
    * Трудоемкось операции удаления зависит от заполненности таблицы.
    * Если в ней всего несколько элементов, то трудоемкость близка к O(1).
    * Когда таблица переполняется, трудоемкость превращается в линейную.
    * */

    // Трудоемкость O(N) - максимальная трудоемкость, где N - размер
    // Ресурсоемкость О(1)
    @Override
    public boolean remove(Object o) {
        if (!contains(o)) return false;
        int startInd = startingIndex(o);
        int index = startInd;
        while (!storage[index].equals(o) && storage[index] != null){
            index = (index + 1) % capacity;
            if (index == startInd) return false;
        }
        storage[index] = removedObj;
        size--;
        return true;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new OpenAddressingSetIterator();
    }

    public class OpenAddressingSetIterator implements Iterator<T>{

        int count = 0;
        int index = 0;
        Object current;

        // Трудоемкость O(1)
        // Ресурсоемкость О(1)
        @Override
        public boolean hasNext() {
            return count < size;
        }

        // Трудоемкость O(N) - максимальная, если, например, таблица близка к переполнению,
        // а удаление еще не производилось. N - размер.
        // Ресурсоемкость О(1)
        @Override
        public T next() {
            if (!hasNext()) throw new IllegalStateException();
            while (storage[index] == null || storage[index] == removedObj){
                index++;
            }
            current = storage[index];
            index++;
            count++;
            return (T) current;
        }

        //Трудоемкость - О(1)
        // Ресуерсоемкость - О(1)
        @Override
        public void remove() {
            if (current == null || current == removedObj) throw new IllegalStateException();
            storage[index - 1] = removedObj;
            size--;
            count--;
        }
    }
}
