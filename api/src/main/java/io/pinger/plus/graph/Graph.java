package io.pinger.plus.graph;

import java.util.*;

public class Graph<E extends Comparable<E>> {
    private final Map<E, List<E>> graph = new TreeMap<>(E::compareTo);

    public Iterable<E> traverse(E startNode) {
        return () -> new BreadthFirstSearch<>(this, startNode);
    }

    public void addNode(E node) {
        this.graph.put(node, new ArrayList<>());
    }

    public void addEdge(E parent, E child) {
        this.graph.compute(parent, ($, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(child);
            return list;
        });
    }

    public boolean existsNode(E node) {
        return this.graph.containsKey(node);
    }

    public List<E> getChildren(E parent) {
        return this.graph.computeIfAbsent(parent, k -> new ArrayList<>());
    }

    public Map<E, List<E>> getGraph() {
        return this.graph;
    }

    public Set<E> getVertices() {
        return this.graph.keySet();
    }
}
