package util;

import java.util.LinkedList;
import java.util.ListIterator;

public class Selector {
    private ListIterator<String> item;

    public Selector(LinkedList<String> items) {
        item = items.listIterator();
    }

    public String current() {
        String res = item.next();
        item.previous();
        return res;
    }

    public void next() {
        item.next();
        if (!item.hasNext())
            /* We're on the last element, so we go back from one */
            item.previous();
    }

    public void previous() {
        if (item.hasPrevious())
            item.previous();
    }

    public void rewind() {
        while (item.hasPrevious())
            item.previous();
    }

    public void select(String value) {
        rewind();

        while (item.hasNext() && !current().equals(value))
            next();

        /* Item not found, rewind */
        if (!current().equals(value))
            rewind();
    }
}