package datastructures.graphs;

import datastructures.Queue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Graph<T> {
    final Map<Vertex<T>, List<Vertex<T>>> adjacencyLists;
    private final Map<Vertex<T>, BFS> memoizedBFS;


    public Graph(Map<T, List<T>> adjacencyListMap) {
        adjacencyLists = new HashMap<>(adjacencyListMap.size());
        for (Entry<T, List<T>> entry : adjacencyListMap.entrySet()) {
            T key = entry.getKey();
            Collection<Vertex<T>> existingVertices = new HashSet<>(adjacencyListMap.size());
            Vertex<T> vertex = find(existingVertices, key);
            if (vertex == null) {
                vertex = new Vertex<>(key);
                existingVertices.add(vertex);
            }
            List<Vertex<T>> values = new ArrayList<>(entry.getValue().size());
            for (T t : entry.getValue()) {
                Vertex<T> vertex1 = find(existingVertices, t);
                if (vertex1 == null) {
                    vertex1 = new Vertex<>(t);
                    existingVertices.add(vertex1);
                }
                values.add(vertex1);
            }
            adjacencyLists.put(vertex, values);
        }
        memoizedBFS = new HashMap<Vertex<T>, BFS>(adjacencyLists.size());
        adjacencyLists.entrySet().stream()
                .forEach(entry -> entry.getValue().remove(entry.getKey()));
    }

    private Vertex<T> find(Iterable<Vertex<T>> existingVertices, T key) {
        for (Vertex<T> existingVertice : existingVertices) {
            if (existingVertice.data.equals(key)) {
                return existingVertice;
            }
        }
        return null;
    }

    public final String breadthFirstSearch(T source, T destination) {
        Vertex<T> sourceVertex = new Vertex<>(source);
        BFS existingBFS = memoizedBFS.get(sourceVertex);
        if (existingBFS == null) {
            existingBFS = new BFS(source);
            existingBFS.breadthFirstSearch();
            memoizedBFS.put(sourceVertex, existingBFS);
        }
        return existingBFS.print(destination);
    }


    /*
        BFS finds the shortest path from the source
        to all reachable vertices
    */
    private class BFS {
        private final T source;
        Map<BFSWrapper<T>, List<BFSWrapper<T>>> adjacencyListWrapper = new HashMap<>(adjacencyLists.size());

        BFS(T source) {
            this.source = source;
            List<Vertex<T>> noAdjacencyLists = adjacencyLists.values().stream()
                    .flatMap(Collection::stream)
                    .filter(vertex -> !adjacencyLists.keySet().contains(vertex))
                    .collect(Collectors.toList());
            Collection<BFSWrapper<T>> createdWrappers = new HashSet<BFSWrapper<T>>(adjacencyLists.size());
            for (Vertex<T> vertex : noAdjacencyLists) {
                BFSWrapper<T> bfsWrapper = find(createdWrappers, vertex);
                if (bfsWrapper == null) {
                    bfsWrapper = new BFSWrapper<>(vertex);
                    createdWrappers.add(bfsWrapper);
                }
                adjacencyLists.put(vertex, Collections.emptyList());
            }
            for (Entry<Vertex<T>, List<Vertex<T>>> entry : adjacencyLists.entrySet()) {
                BFSWrapper<T> bfsWrapper = find(createdWrappers, entry.getKey());
                if (bfsWrapper == null) {
                    bfsWrapper = new BFSWrapper<>(entry.getKey());
                    createdWrappers.add(bfsWrapper);
                }
                List<BFSWrapper<T>> bfsWrappers = new ArrayList<BFSWrapper<T>>(entry.getValue().size());
                for (Vertex<T> tVertex : entry.getValue()) {
                    BFSWrapper<T> tbfsWrapper = find(createdWrappers, tVertex);
                    if (tbfsWrapper == null) {
                        tbfsWrapper = new BFSWrapper<>(tVertex);
                        createdWrappers.add(tbfsWrapper);
                    }
                    bfsWrappers.add(tbfsWrapper);
                }
                adjacencyListWrapper.put(bfsWrapper, bfsWrappers);
            }
        }

        private BFSWrapper<T> find(Iterable<BFSWrapper<T>> createdWrappers, Vertex<T> key) {
            for (BFSWrapper<T> createdWrapper : createdWrappers) {
                if (createdWrapper.vertex.equals(key)) {
                    return createdWrapper;
                }
            }
            return null;
        }

        private BFSWrapper<T> findSource() {
            return adjacencyListWrapper.keySet().stream()
                    .filter(wrapper -> wrapper.vertex.data.equals(source))
                    .findAny()
                    .orElse(null);
        }

        private BFSWrapper<T> find(T data) {
            return adjacencyListWrapper.keySet().stream()
                    .filter(wrapper -> wrapper.vertex.data.equals(data))
                    .findAny()
                    .orElse(null);
        }

        final void breadthFirstSearch() {
            Queue<BFSWrapper<T>> queue = new Queue<>();
            queue.enqueue(findSource());
            while (!queue.isEmpty()) {
                BFSWrapper<T> u = queue.dequeue();
                List<BFSWrapper<T>> vertices = adjacencyListWrapper.get(u);
                vertices.stream()
                        .filter(wrapper -> wrapper.color == Color.WHITE)
                        .forEach(wrapper -> {
                            wrapper.color = Color.BLACK;
                            wrapper.distance = u.distance + 1;
                            wrapper.parent = u;
                            queue.enqueue(wrapper);
                        });
                u.color = Color.BLACK;
            }
        }

        final String print(T sink) {
            return print(findSource(), find(sink));
        }

        private String print(BFSWrapper<T> sourceWrapper, BFSWrapper<T> sink) {
            if (sink.vertex.data.equals(sourceWrapper.vertex.data)) {
                return source.toString();
            }
            if (sink.parent == null) {
                return "No path exists";
            }

            return sink.vertex.data + " <= " + print(sourceWrapper, sink.parent);
        }

        class BFSWrapper<E> {
            BFSWrapper<E> parent;
            Color color;
            int distance;
            final Vertex<E> vertex;

            BFSWrapper(Vertex<E> vertex) {
                this.vertex = vertex;
                color = Color.WHITE;
            }
        }
    }

    @Override
    public final String toString() {
        return "Graph{" + "adjacencyLists=" + adjacencyLists +
                '}';
    }

}

