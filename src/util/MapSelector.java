package util;

import java.util.LinkedList;
import java.util.ListIterator;

public class MapSelector {
    private LinkedList<String> maps;
    private ListIterator<String> item;

    public MapSelector(LinkedList<String> maps) {
        item = maps.listIterator();
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
}