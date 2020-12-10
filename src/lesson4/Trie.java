package lesson4;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
    }

    private Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override

    /*
    * В среднем трудоемкость O(N), ресурсоемкость O(N), где N - количество букв в слове.
    * Это не критично, если речь идет, например, о словаре. Так как максимальная длина одного слова
    * не более нескольких десятков символов.
    * Моя реализация не оптимальна по памяти, так как все слова сохраняются в очередь в начале работы итератора.
    * Можно было хранить меньшее количество информации, если искать следующий элемент в ходе итерации.
    * Трудоемкость останется O(N), однако ресурсоемкость, преодполагаю, будет константной.
    * */

    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public class TrieIterator implements Iterator{
        private ArrayDeque<String> deque;
        private String currentElement;

        public TrieIterator() {
             deque = new ArrayDeque<>();
             currentElement = null;
             fillDeque(root, "");
        }

        //Трудоемкость O(N), где N - количество букв в слове
        //Ресурсоемкость O(N), создание map размера N + новые переменные создаются N раз
        private void fillDeque(Node parent, String word){
            Map<Character, Node> childrenMap = parent.children;
            if (!childrenMap.isEmpty()){
                for (Map.Entry<Character, Node> element: childrenMap.entrySet()) {
                    Character character = element.getKey();
                    Node node = element.getValue();
                    if (character != (char) 0) {
                        fillDeque(node,word + character);
                    } else {
                        deque.addFirst(word);
                    }
                }
            }
        }

        //Трудоемкость O(1)
        //Ресурсоемкость O(1)
        @Override
        public boolean hasNext() {
            return !deque.isEmpty();
        }

        //Трудоемкость O(1), т.к. удаление первого элемента из очереди производится за О(1)
        //Ресурсоемкость O(1)
        @Override
        public Object next() {
            if (!hasNext()) throw new IllegalStateException();
            String next = deque.poll();
            currentElement = next;
            return next;
        }

        //Трудоемкость O(N), где N - количество букв в слове
        //Ресурсоемкость O(1)
        @Override
        public void remove() {
            if (currentElement == null) throw new IllegalStateException();
            Trie.this.remove(currentElement);
            currentElement = null;
        }
    }
}