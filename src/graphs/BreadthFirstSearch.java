package graphs;

import datastructures.graphs.Graph;
import datastructures.graphs.Vertex;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BreadthFirstSearch {
           ;
    public static void main(String[] args) {
        Character source = 'a';
        Character sink = 'b';
        Character sink2 = 'c';
        Vertex<Character> a = new Vertex<>(source);
        Vertex<Character> b = new Vertex<>(sink);
        Vertex<Character> c = new Vertex<>(sink2);
        List<Vertex<Character>> adjacencyListForA = Arrays.asList(b, c);
        List<Vertex<Character>> adjacencyListForB = Collections.singletonList(c);
        Map<Vertex<Character>, List<Vertex<Character>>> adjacencyLists = new HashMap<>();
        adjacencyLists.put(a, adjacencyListForA);
        adjacencyLists.put(b, adjacencyListForB);
        Graph<Character> graph = new Graph<>(adjacencyLists);
        System.out.println(graph.breadthFirstSearch(sink2, sink));
        System.out.println(graph.breadthFirstSearch(sink, sink2));
        System.out.println(graph.breadthFirstSearch(source, sink2));
        System.out.println(graph.breadthFirstSearch(source, sink));
    }
}
