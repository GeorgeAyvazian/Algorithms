package datastructures;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    private final Vertex source;
    Set<Edge> edges;
    Set<Vertex> vertices;                                                            
    Vertex[][] adjacencyLists;

    public Graph(Vertex source, Edge... edges) {
        this.source = source;
        this.edges = new HashSet<>(edges.length);
        this.edges.addAll(Arrays.asList(edges));
        vertices = new HashSet<>(edges.length << 1);

        for (Edge edge : this.edges) {
            vertices.add(edge.source);
            vertices.add(edge.sink);
        }
    }
}

class Vertex {

    Object data;
    int distance;
    Color color;

    public Vertex(Object data) {
        this.data = data;
        color = Color.WHITE;
    }
}

class Edge {
    Vertex source, sink;

    public Edge(Vertex source, Vertex sink) {
        this.source = source;
        this.sink = sink;
    }
}
