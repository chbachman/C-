package com.chbachman.cminus.util;

/**
 * Created by Chandler on 4/23/17.
 */
public class Lazy<E> {

    E object;

    public boolean lazy() {
        return object != null;
    }

    public E get() {
        return object;
    }

    public E create(E obj) {
        this.object = obj;
        return object;
    }

}
