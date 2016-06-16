package datastructures.graphs;

import datastructures.Queue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph<T> {
    private final Map<Vertex<T>, List<Vertex<T>>> adjacencyLists;
    private final Map<Vertex<T>, BFS> memoizedBFS;
    private final Map<Vertex<T>, BFS> memoizedBFSAllPaths;


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
        memoizedBFSAllPaths = new HashMap<Vertex<T>, BFS>(adjacencyLists.size());
        adjacencyLists.entrySet().stream()
                .forEach(entry -> entry.getValue().remove(entry.getKey()));
    }

    private @Nullable Vertex<T> find(Iterable<Vertex<T>> existingVertices, T key) {
        for (Vertex<T> existingVertice : existingVertices) {
            if (existingVertice.data.equals(key)) {
                return existingVertice;
            }
        }
        return null;
    }

    public final String findAllPaths(T source, T destination) {
        Vertex<T> sourceVertex = new Vertex<>(source);
        BFS existingBFS = memoizedBFSAllPaths.get(sourceVertex);
        if (existingBFS == null) {
            existingBFS = new BFS(source);
            existingBFS.allPathsSearch();
            memoizedBFSAllPaths.put(sourceVertex, existingBFS);
        }
        return existingBFS.print(destination, false);
    }

    public final String findShortestPath(T source, T destination) {
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

        private @Nullable BFSWrapper<T> find(Iterable<BFSWrapper<T>> createdWrappers, Vertex<T> key) {
            for (BFSWrapper<T> createdWrapper : createdWrappers) {
                if (createdWrapper.vertex.equals(key)) {
                    return createdWrapper;
                }
            }
            return null;
        }

        private BFSWrapper<T> find(T data) {
            return adjacencyListWrapper.keySet().stream()
                    .filter(wrapper -> wrapper.vertex.data.equals(data))
                    .findAny()
                    .orElse(null);
        }

        final void breadthFirstSearch() {
            Queue<BFSWrapper<T>> queue = new Queue<>();
            queue.enqueue(find(source));
            while (!queue.isEmpty()) {
                BFSWrapper<T> u = queue.dequeue();
                assert u != null;
                List<BFSWrapper<T>> vertices = adjacencyListWrapper.get(u);
                vertices.stream()
                        .filter(wrapper -> wrapper.color == Color.WHITE)
                        .forEach(wrapper -> {
                            wrapper.color = Color.BLACK;
                            wrapper.distance = u.distance + 1;
                            if(!wrapper.parents.contains(u))
                                wrapper.parents.add(u);
                            queue.enqueue(wrapper);
                        });
                u.color = Color.BLACK;
            }
        }

        final void allPathsSearch() {
            Queue<BFSWrapper<T>> queue = new Queue<>(adjacencyListWrapper.keySet());
            for (int i = 0; i < adjacencyListWrapper.keySet().size(); i++) {
                BFSWrapper<T> u = queue.dequeue();
                assert u != null;
                List<BFSWrapper<T>> vertices = adjacencyListWrapper.get(u);
                vertices.stream()
                        .forEach(wrapper -> {
                            wrapper.color = Color.BLACK;
                            if (!wrapper.parents.contains(u)) {
                                wrapper.parents.add(u);
                            }
                        });
                u.color = Color.BLACK;
            }
        }

        final String print(T sink) {
            return print(find(source), find(sink));
        }

        final String print(T sink, boolean dummy) {
            HashSet<BFSWrapper<T>> objects = new HashSet<>();
            BFSWrapper<T> sink1 = find(sink);
            return print(find(source), sink1, objects);
        }

        private String print(BFSWrapper<T> sourceWrapper, BFSWrapper<T> sink, Set<BFSWrapper<T>> seen) {
            if (seen.contains(sink)) {
                return "|";
            }
            seen.add(sink);
            if (sink.vertex.data.equals(sourceWrapper.vertex.data)) {
                return source.toString() + ';';
            }
            if (sink.parents.isEmpty()) {
                return "No path exists;";
            }
            String s = "";
            for (BFSWrapper<T> parent : sink.parents) {
                s += sink.vertex.data + " <= " + print(sourceWrapper, parent, seen);
            }
            return s;
        }

        private String print(BFSWrapper<T> sourceWrapper, BFSWrapper<T> sink) {
            if (sink.vertex.data.equals(sourceWrapper.vertex.data)) {
                return source.toString() + ';';
            }
            if (sink.parents.isEmpty()) {
                return "No path exists;";
            }
            String s = "";
            for (BFSWrapper<T> parent : sink.parents) {
                s += sink.vertex.data + " <= " + print(sourceWrapper, parent);
            }
            return s;
        }

        class BFSWrapper<E> {
            List<BFSWrapper<E>> parents = new ArrayList<>();
            Color color;
            int distance;
            final Vertex<E> vertex;

            BFSWrapper(Vertex<E> vertex) {
                this.vertex = vertex;
                color = Color.WHITE;
            }
        }
    }
}

