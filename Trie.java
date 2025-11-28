import java.util.ArrayList;
import java.util.List;

public class Trie {
    private static class TrieNode {
        private static final int ALPHABET_SIZE = 26;
        private TrieNode[] children;
        private boolean isEndOfWord;
        private int prefixCount; // сколько слов проходит через этот узел

        public TrieNode() {
            children = new TrieNode[ALPHABET_SIZE];
            isEndOfWord = false;
            prefixCount = 0;
        }

        public int getCharIndex(char c) {
            return c - 'a';
        }

        public boolean containsChar(char c) {
            return children[getCharIndex(c)] != null;
        }

        public void setChild(char c, TrieNode node) {
            children[getCharIndex(c)] = node;
        }

        public TrieNode getChild(char c) {
            return children[getCharIndex(c)];
        }
    }

    private TrieNode root;
    private int wordCount; // общее число слов в дереве

    public Trie() {
        root = new TrieNode();
        wordCount = 0;
    }

    // Вставка слова
    public void insert(String word) {
        if (word == null) return;
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            if (!current.containsChar(c)) {
                current.setChild(c, new TrieNode());
            }
            current = current.getChild(c);
            current.prefixCount++; // увеличиваем счётчик префиксов
        }
        if (!current.isEndOfWord) {
            current.isEndOfWord = true;
            wordCount++;
        }
    }

    // Удаление слова (с корректным освобождением узлов)
    public void delete(String word) {
        if (word == null || !contains(word)) return;
        deleteHelper(root, word, 0);
        wordCount--;
    }

    private boolean deleteHelper(TrieNode node, String word, int index) {
        if (index == word.length()) {
            if (!node.isEndOfWord) return false; // такого слова нет
            node.isEndOfWord = false;
            return node.prefixCount == 0; // можно ли удалить?
        }

        char c = word.charAt(index);
        int idx = node.getCharIndex(c);
        TrieNode child = node.children[idx];
        if (child == null) return false;

        boolean shouldDeleteChild = deleteHelper(child, word, index + 1);

        if (shouldDeleteChild) {
            node.children[idx] = null;
            child.prefixCount--; // на самом деле уже не нужно, но для логики
        }

        // узел можно удалить, если он не является концом другого слова
        // и у него нет других детей
        return !node.isEndOfWord && hasNoChildren(node);
    }

    private boolean hasNoChildren(TrieNode node) {
        for (TrieNode child : node.children) {
            if (child != null) return false;
        }
        return true;
    }

    // Проверка наличия слова
    public boolean contains(String word) {
        TrieNode current = searchNode(word);
        return current != null && current.isEndOfWord;
    }

    // Проверка существования слов с данным префиксом
    public boolean startsWith(String prefix) {
        return searchNode(prefix) != null;
    }

    // Подсчёт слов с данным префиксом
    public int countWordsWithPrefix(String prefix) {
        TrieNode node = searchNode(prefix);
        return node == null ? 0 : node.prefixCount;
    }

    // Получение всех слов с префиксом
    public List<String> getByPrefix(String prefix) {
        List<String> result = new ArrayList<>();
        TrieNode node = searchNode(prefix);
        if (node == null) return result;
        collectWords(node, new StringBuilder(prefix), result);
        return result;
    }

    // Получение всех слов в дереве
    public List<String> getAllWords() {
        return getByPrefix("");
    }

    // Общее число слов
    public int size() {
        return wordCount;
    }

    // Пусто ли дерево?
    public boolean isEmpty() {
        return wordCount == 0;
    }

    // Вспомогательные методы

    private TrieNode searchNode(String str) {
        TrieNode current = root;
        for (char c : str.toCharArray()) {
            if (!current.containsChar(c)) {
                return null;
            }
            current = current.getChild(c);
        }
        return current;
    }

    private void collectWords(TrieNode node, StringBuilder current, List<String> result) {
        if (node.isEndOfWord) {
            result.add(current.toString());
        }
        for (int i = 0; i < TrieNode.ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                char c = (char) ('a' + i);
                current.append(c);
                collectWords(node.children[i], current, result);
                current.deleteCharAt(current.length() - 1);
            }
        }
    }
}
