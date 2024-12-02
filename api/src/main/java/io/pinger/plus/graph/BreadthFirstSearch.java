package io.pinger.plus.graph;

import java.util.*;

public class BreadthFirstSearch<E extends Comparable<E>> implements Iterator<E> {
    private final Graph<E> graph;
    private final Queue<E> queue = new LinkedList<>();
    private final Set<E> visited = new HashSet<>();

    public BreadthFirstSearch(Graph<E> graph, E startNode) {
        this.graph = graph;
        this.queue.add(startNode);
        this.visited.add(startNode);
    }

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public E next() {
        final E current = this.queue.remove();
        for (final E children : this.graph.getChildren(current)) {
            if (this.visited.add(children)) {
                this.queue.add(children);
            }
        }
        return current;
    }


}
