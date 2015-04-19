package Model;

import java.util.Random;
import java.util.function.BiConsumer;

// Based on http://algs4.cs.princeton.edu/34hash/LinearProbingHashST.java
public class LongHashMap<T> {
    int N;              // number of key-value pairs in the symbol table
    int M;              // size of linear probing table, always a power of 2
    int MMASK;          // == M-1, for quick modulo M operation
    public T[] vals;    // the values
    public long[] keys; // the values

    // Must be a power of two:
    private static final int INIT_CAPACITY = 1 << 4;
    final static int BMASK = (1 << 8) - 1;

    // randomly initialized with positive integers. Used for twisted tabulation hashing in hash(long):
    int[][] tab; {
        tab = new int[8][1<<8];
        Random rnd = new Random();
        for (int[] row : tab)
            for (int i = 0 ; i < 1 << 8 ; i++)
                row[i] = rnd.nextInt() & Integer.MAX_VALUE;
    }

    // create an empty hash table - use 16 as default size
    public LongHashMap() {
        this(INIT_CAPACITY);
    }

    // create an empty hash table with the given capacity. Must be a power of 2
    @SuppressWarnings("unchecked")
    public LongHashMap(int capacity) {
        assert (capacity & (capacity - 1)) == 0; // capacity must be a power of 2
        M = capacity;
        MMASK = M - 1;
        vals = (T[]) new Object[M];
        keys = new long[M];
    }

    // return the number of key-value pairs in the symbol table
    public int size() {
        return N;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // does a key-value pair with the given key exist in the symbol table?
    public boolean contains(long key) {
        return vals[idx(key)] != null;
    }

    // Twisted tabulation hashing:
    // http://dl.acm.org/citation.cfm?id=2627833
    int hash(long key) {
        int h = 0;
        for (int i = 0 ; i < 7 ; i++, key >>= 8) h ^= tab[i][(int)key &BMASK];
        return (h ^ tab[7][(int)(h^key) &BMASK]);
    }

    // Computed the index of a given key, or where it should go, if it is not currently in the map
    int idx(long key) {
        int i = hash(key) & MMASK;
        while (vals[i] != null && keys[i] != key) i = (i + 1) & MMASK;
        return i;
    }

    // resize the hash table to the given capacity by re-hashing all of the keys
    void resize(int capacity) {
        assert (capacity & (capacity - 1)) == 0; // capacity must be a power of 2
        @SuppressWarnings("unchecked")
        T[] nvals = (T[]) new Object[capacity];
        long[] nkeys = new long[capacity];
        int NMASK = capacity - 1;
        for (int i = M - 1 ; i >= 0 ; i--)
            if (vals[i] != null) {
                int j = hash(keys[i]) & NMASK;
                while (nvals[j] != null) j = (j + 1) & NMASK;
                nvals[j] = vals[i];
                nkeys[j] = keys[i];
            }
        vals = nvals;
        keys = nkeys;
        M    = capacity;
        MMASK = NMASK;
    }

    // return the value associated with the given key, null if no such value
    public T get(long key) {
        return vals[idx(key)];
    }

    // associate the given value with the given key.
    // return the value previously associated with the given key, null if no such value
    public T put(long key, T val) {
        if (val == null) return remove(key);
        if (N<<1 >= M) resize(2*M);
        int i = idx(key);
        T old = vals[i];
        keys[i] = key;
        vals[i] = val;
        N++;
        return old;
    }

    // remove the association (if any) with the given key.
    // return the value previously associated with the given key, null if no such value
    public T remove(long key) {
        T old = null;
        int i = idx(key);
        if (vals[i] == null) return null;
        old = vals[i];
        vals[i] = null;
        for (i = (i + 1) & MMASK ; vals[i] != null ; i = (i + 1) & MMASK) {
            long keyToRehash = keys[i];
            T    valToRehash = vals[i];
            vals[i] = null;
            N--;
            put(keyToRehash, valToRehash);
        }
        N--;
        if (N > 0 && N <= M/8) resize(M/2);
        return old;
    }

    public void forEach(BiConsumer<Long,T> f) {
        for (int i = 0 ; i < M ; i++) {
            if (vals[i] != null) f.accept(keys[i], vals[i]);
        }
    }

    public void clear() {
        vals = null;
        keys = null;
    }
}

