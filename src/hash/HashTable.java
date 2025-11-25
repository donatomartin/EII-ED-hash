package hash;

/**
 * Interface for a hash table data structure that stores elements using hashing.
 * <p>
 * This interface does not specify whether the implementation uses open or
 * closed hashing, nor does it mandate any particular collision resolution
 * strategy.
 * </p>
 *
 * @param <T> the type of elements stored in the hash table
 */
public interface HashTable<T> {

    /**
     * Adds an element to the hash table. If the element already exists in the
     * table, no action is taken.
     *
     * @param element the element to add
     * @return true if the element was successfully added, false if the element
     *         already exists
     * @throws NullPointerException  if the element is {@code null}
     * @throws IllegalStateException if adding would exceed the table's capacity
     */
    boolean add(T element);

    /**
     * Searches for an element in the hash table.
     *
     * @param element the element to search for
     * @return true if the element is found, false otherwise
     * @throws NullPointerException if the element is nill
     */
    boolean search(T element);

    /**
     * Removes an element from the hash table.
     *
     * @param element the element to remove
     * @return true if the element was successfully removed, false if the element
     *         does not exist
     * @throws NullPointerException  if the element is null
     * @throws IllegalStateException if the table is empty
     */
    boolean remove(T element);

    /**
     * Returns the number of elements currently stored in the hash table.
     *
     * @return the number of elements in the table
     */
    int getElementNumber();

    /**
     * Returns the total capacity of the hash table.
     *
     * @return the maximum number of elements the table can hold
     */
    int getCapacityB();

    /**
     * Calculates and returns the current load factor of the hash table.
     *
     * The load factor is defined as the ratio of the number of elements to the
     * table's capacity: elementNumber / capacity.
     *
     * @return the load factor as a value between 0.0 and 1.0
     */
    double getLoadFactor();

}
