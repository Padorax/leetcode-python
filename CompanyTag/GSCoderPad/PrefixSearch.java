package CompanyTag.GSCoderPad;

import java.util.Map; 
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

public class PrefixSearch {
    static class MyPrefixSearch {
        private final TrieNode index = new TrieNode();
        MyPrefixSearch(String document) {
            buildIndex(document);
        }

        private void buildIndex(String document) {
            int location = 0;
            String[] words = document.split(" ");
            for (String word : words) {
                if (word.length() > 0) {
                    String clean = word.toLowerCase().replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]","");

                    index.add(clean, location);
                }
                location += word.length() + 1;
            }
        }

        public List<Integer> findAll(String prefix) {
            return index.get(prefix);
        }

        static class TrieNode {
            private final Map<Character, TrieNode> nodes = new HashMap<>();
            private final List<Integer> locations = new LinkedList<>();

            public void add(String chars, int location) {
                TrieNode cur = this;
                for (char c : chars.toCharArray()) {
                    cur.nodes.putIfAbsent(c, new TrieNode());
                    cur = cur.nodes.get(c);
                    cur.locations.add(location);
                }
            }

            public List<Integer> get(String prefix) {
                TrieNode cur = this;
                for (char c : prefix.toCharArray()) {
                    if (!cur.nodes.containsKey(c)) {
                        return new LinkedList<>();
                    }
                    cur = cur.nodes.get(c);
                }
                return cur.locations;
            }
        }
    }
}
