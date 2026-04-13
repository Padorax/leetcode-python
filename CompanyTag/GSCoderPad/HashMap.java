package CompanyTag.GSCoderPad;

//List<List<Integer>>
// or Node[], get a sentinel node, iterate with prev, cur (prev.next)

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class HashMap {
    private static class MyHashMap<K, V> {
        private List<List<Entry<K, V>>> buckets; //!!!K, V

        MyHashMap() { //!! constructor does not need K, V
            this.buckets = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                //buckets.add( new LinkedList<>());
                buckets.set(i, new LinkedList<>());
            }
        }

        void put(K key, V value) {
            if (key == null || value == null) return;
            int idx = getIndex(key);
            List<Entry<K, V>> bucket = buckets.get(idx); //Entry needs K, V
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    entry.val = value;
                    return;
                }
            }
            bucket.add(new Entry<>(key, value)); //Entry<> not Entry
        }

        V get(K key) {
            if (key == null) return null;
            List<Entry<K, V>> bucket = buckets.get(getIndex(key));
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.val;
                }
            }
            return null;
        }

        private int getIndex(K key) {
            //System.out.println(Entry.name);
            return key.hashCode() % buckets.size(); // !!key not K
        }
    
        private static class Entry<K, V> { // !!! not inner class can access any 
            // variable on outer class, including private ones (DIRECTLY)
            // and outer class can access inner class private variable (thru an instance)!
            //static String name = "a";
            private final K key; // don't forget private!!
            private V val;

            Entry(K key, V val) {
                this.key = key;
                this.val = val;    
            }
        }
    }
}
