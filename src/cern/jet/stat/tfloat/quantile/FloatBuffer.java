/*
Copyright (C) 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
is hereby granted without fee, provided that the above copyright notice appear in all copies and 
that both that copyright notice and this permission notice appear in supporting documentation. 
CERN makes no representations about the suitability of this software for any purpose. 
It is provided "as is" without expressed or implied warranty.
 */
package cern.jet.stat.tfloat.quantile;

import cern.colt.list.tfloat.FloatArrayList;
import cern.jet.stat.Buffer;

/**
 * A buffer holding <tt>float</tt> elements; internally used for computing
 * approximate quantiles.
 */
class FloatBuffer extends Buffer {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected FloatArrayList values;

    protected boolean isSorted;

    /**
     * This method was created in VisualAge.
     * 
     * @param k
     *            int
     */
    public FloatBuffer(int k) {
        super(k);
        this.values = new FloatArrayList(0);
        this.isSorted = false;
    }

    /**
     * Adds a value to the receiver.
     */
    public void add(float value) {
        if (!isAllocated)
            allocate(); // lazy buffer allocation can safe memory.
        values.add(value);
        this.isSorted = false;
    }

    /**
     * Adds a value to the receiver.
     */
    public void addAllOfFromTo(FloatArrayList elements, int from, int to) {
        if (!isAllocated)
            allocate(); // lazy buffer allocation can safe memory.
        values.addAllOfFromTo(elements, from, to);
        this.isSorted = false;
    }

    /**
     * Allocates the receiver.
     */
    protected void allocate() {
        isAllocated = true;
        values.ensureCapacity(k);
    }

    /**
     * Clears the receiver.
     */
    @Override
    public void clear() {
        values.clear();
    }

    /**
     * Returns a deep copy of the receiver.
     * 
     * @return a deep copy of the receiver.
     */
    @Override
    public Object clone() {
        FloatBuffer copy = (FloatBuffer) super.clone();
        if (this.values != null)
            copy.values = copy.values.copy();
        return copy;
    }

    /**
     * Returns whether the specified element is contained in the receiver.
     */
    public boolean contains(float element) {
        this.sort();
        return values.contains(element);
    }

    /**
     * Returns whether the receiver is empty.
     */
    @Override
    public boolean isEmpty() {
        return values.size() == 0;
    }

    /**
     * Returns whether the receiver is empty.
     */
    @Override
    public boolean isFull() {
        return values.size() == k;
    }

    /**
     * Returns the number of elements currently needed to store all contained
     * elements. This number usually differs from the results of method
     * <tt>size()</tt>, according to the underlying algorithm.
     */
    public int memory() {
        return values.elements().length;
    }

    /**
     * Returns the rank of a given element within the sorted sequence of the
     * receiver. A rank is the number of elements <= element. Ranks are of the
     * form {1,2,...size()}. If no element is <= element, then the rank is zero.
     * If the element lies in between two contained elements, then uses linear
     * interpolation.
     * 
     * @return the rank of the element.
     * @param list
     *            cern.colt.list.FloatArrayList
     * @param element
     *            the element to search for
     */
    public float rank(float element) {
        this.sort();
        return cern.jet.stat.tfloat.FloatDescriptive.rankInterpolated(this.values, element);
    }

    /**
     * Returns the number of elements contained in the receiver.
     */
    @Override
    public int size() {
        return values.size();
    }

    /**
     * Sorts the receiver.
     */
    @Override
    public void sort() {
        if (!this.isSorted) {
            // IMPORTANT: TO DO : replace mergeSort with quickSort!
            // currently it is mergeSort only for debugging purposes (JDK 1.2
            // can't be imported into VisualAge).
            values.sort();
            // values.mergeSort();
            this.isSorted = true;
        }
    }

    /**
     * Returns a String representation of the receiver.
     */
    @Override
    public String toString() {
        return "k=" + this.k + ", w=" + Long.toString(weight()) + ", l=" + Integer.toString(level()) + ", size="
                + values.size();
        // ", v=" + values.toString();
    }
}