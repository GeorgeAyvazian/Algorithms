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
        Character a = 'a';
        Character b = 'b';
        Character c = 'c';
        Character d = 'd';
        List<Character> adjacencyListForA = Arrays.asList(b, c);
        List<Character> adjacencyListForB = Arrays.asList(c, d);
        List<Character> adjacencyListForD = Collections.singletonList(c);
        Map<Character, List<Character>> adjacencyLists = new HashMap<>();
        adjacencyLists.put(a, adjacencyListForA);
        adjacencyLists.put(b, adjacencyListForB);
        adjacencyLists.put(d, adjacencyListForD);
        Graph<Character> graph = new Graph<>(adjacencyLists);
        System.out.println(graph.findShortestPath(c, b));
        System.out.println(graph.findShortestPath(b, c));
        System.out.println(graph.findShortestPath(a, c));
        System.out.println(graph.findAllPaths(a, c));
        System.out.println(graph.findShortestPath(a, b));
        System.out.println(graph.findShortestPath(a, d));
    }
}
