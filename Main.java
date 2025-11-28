public class Main {
    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("apply");
        trie.insert("banana");

        System.out.println(trie.contains("app")); 
        System.out.println(trie.startsWith("ap"));
        System.out.println(trie.countWordsWithPrefix("app")); 
        System.out.println(trie.getByPrefix("app")); 
        System.out.println(trie.size()); 

        trie.delete("app");
        System.out.println(trie.contains("app")); 
        System.out.println(trie.countWordsWithPrefix("app"));
        System.out.println(trie.size()); 
    }
}
