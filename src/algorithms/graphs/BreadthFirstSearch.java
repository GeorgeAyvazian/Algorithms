package algorithms.graphs;

import datastructures.graphs.Graph;

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
        List<Character> adjacencyListForA = Arrays.asList(sink, sink2);
        List<Character> adjacencyListForB = Collections.singletonList(sink2);
        Map<Character, List<Character>> adjacencyLists = new HashMap<>();
        adjacencyLists.put(source, adjacencyListForA);
        adjacencyLists.put(sink, adjacencyListForB);
        Graph<Character> graph = new Graph<>(adjacencyLists);
        System.out.println(graph.breadthFirstSearch(sink2, sink));
        System.out.println(graph.breadthFirstSearch(sink, sink2));
        System.out.println(graph.breadthFirstSearch(source, sink2));
        System.out.println(graph.breadthFirstSearch(source, sink));
    }
}
