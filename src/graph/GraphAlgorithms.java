/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gcabral
 */
@SuppressWarnings("unchecked")
public class GraphAlgorithms {

    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vDest Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path stack with vertices of the current path (the path is in
     * reverse order)
     * @param paths List with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited, LinkedList<V> path, List<LinkedList<V>> paths) {

        visited[g.getKey(vOrig)] = true;
        path.add(vOrig);
        for (V adj : g.adjVertices(vOrig)) {
            int adjIndex = g.getKey(adj);
            if (adj == vDest) {
                path.add(adj);
                paths.add(revPath(path));
                path.removeLast();
            } else if (visited[adjIndex] == false) {
                allPaths(g, adj, vDest, visited, path, paths);
            }
        }
        visited[g.getKey(path.removeLast())] = false;
    }

    /**
     * @param g Graph instancege
     * @return paths List with all paths from voInf to vdInf
     */
    public static <V, E> List<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {

        List<LinkedList<V>> paths = new ArrayList<>();
        if (g.validVertex(vOrig) && g.validVertex(vDest)) {
            boolean[] visited = new boolean[g.numVertices()];
            LinkedList<V> aux = new LinkedList<>();
            allPaths(g, vOrig, vDest, visited, aux, paths);
        }
        return paths;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with nonnegative edge weights This implementation
     * uses Dijkstra's algorithm
     *
     * @param <V>
     * @param <E>
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vertices
     * @param visited set of discovered vertices
     * @param pathKeys
     * @param dist minimum distances
     */
    protected static <V, E> void shortestPathLength(Graph<V, E> g, V vOrig, V[] vertices, boolean[] visited, int[] pathKeys, double[] dist) {
        int index = g.getKey(vOrig);
        dist[index] = 0;

        while (index != -1) {
            visited[g.getKey(vOrig)] = true;
            for (Edge<V, E> ed : g.outgoingEdges(vOrig)) {
                V vert = ed.getVDest() != vOrig ? ed.getVDest() : ed.getVOrig();
                if (!visited[g.getKey(vert)] && dist[g.getKey(vert)] > dist[g.getKey(vOrig)] + ed.getWeight()) {
                    dist[g.getKey(vert)] = dist[g.getKey(vOrig)] + ed.getWeight();
                    pathKeys[g.getKey(vert)] = g.getKey(vOrig);
                }
            }
            Double min = Double.MAX_VALUE;
            index = -1;
            for (int i = 0; i < g.numVertices(); i++) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    index = i;
                }
            }
            for (V v : g.vertices()) {
                if (g.getKey(v) == index) {
                    vOrig = v;
                    break;
                }
            }
        }

    }

    /**
     * Extracts from pathKeys the minimum path between vOrig and vDest The path
     * is constructed from the end to the beginning
     *
     * @param <V>
     * @param <E>
     * @param g Graph instance
     * @param vOrig
     * @param vDest
     * @param verts
     * @param pathKeys
     * @param path stack with the minimum path (correct order)
     */
    protected static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest, V[] verts, int[] pathKeys, LinkedList<V> path) {

        path.addFirst(vDest);
        if (vOrig != vDest) {
            vDest = verts[pathKeys[g.getKey(vDest)]];
            getPath(g, vOrig, vDest, verts, pathKeys, path);
        }

    }

    //shortest-path between vOrig and vDest
    public static <V, E> double shortestPath(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return 0;
        }

        int nVerts = g.numVertices();
        V[] vertices = (V[]) new Object[nVerts];
        boolean[] visited = new boolean[nVerts];
        int[] pathKeys = new int[nVerts];
        double[] dist = new double[nVerts];

        for (int i = 0; i < nVerts; i++) {
            visited[i] = false;
            vertices[i] = null;
            dist[i] = Double.MAX_VALUE;
        }

        for (V v : g.vertices()) {
            vertices[g.getKey(v)] = v;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);
        shortPath.clear();

        int destID = g.getKey(vDest);
        if (!visited[destID]) {
            return 0;
        }

        getPath(g, vOrig, vDest, vertices, pathKeys, shortPath);
        revPath(shortPath);
        return dist[destID];
    }

    /**
     * Reverses the path
     *
     * @param path stack with path
     */
    public static <V, E> LinkedList<V> revPath(LinkedList<V> path) {

        LinkedList<V> pathcopy = new LinkedList<>(path);
        LinkedList<V> pathrev = new LinkedList<>();

        while (!pathcopy.isEmpty()) {
            pathrev.push(pathcopy.pop());
        }

        return pathrev;
    }

}
