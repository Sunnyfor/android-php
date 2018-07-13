package com.cocosh.shmstore.widget.wheel;

import java.util.ArrayList;

/**
 * Desc:
 * Author: chencha
 * Date: 16/11/4
 */

public class ArrayWheelAdapter2<T> implements WheelAdapter2 {

    /** The default items length */
    public static final int DEFAULT_LENGTH = 4;

    // items
    private ArrayList<T> items;
    // length
    private int length;

    /**
     * Constructor
     * @param items the items
     * @param length the max items length
     */
    public ArrayWheelAdapter2(ArrayList<T> items, int length) {
        this.items = items;
        this.length = length;
    }

    /**
     * Contructor
     * @param items the items
     */
    public ArrayWheelAdapter2(ArrayList<T> items) {
        this(items, DEFAULT_LENGTH);
    }

    @Override
    public Object getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return "";
    }

    @Override
    public int getItemsCount() {
        return items.size();
    }

    @Override
    public int indexOf(Object o){
        return items.indexOf(o);
    }

}
