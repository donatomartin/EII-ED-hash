package hash.map;

public class MapEntry<K, V> {
    K key;
    V value;
    public MapEntry() {
        this.key = null;
        this.value = null;
    }

    public MapEntry(K key) {
        this.key = key;
        this.value = null;
    }

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public int hashCode() {
        return key.hashCode();
    }
}
