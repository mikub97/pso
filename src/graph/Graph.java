/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.lang.reflect.Array;
import java.util.*;

/**
 *
 * @author gcabral
 * @param <V>
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class Graph<V, E> implements GraphInterface<V, E> {

    private int numVert;
    private int numEdge;
    private boolean isDirected;
    private Map<V, Vertex<V, E>> vertices;  //all Vertices of the graph 

    // Constructs an empty graph (either undirected or directed)
    public Graph(boolean directed) {
        numVert = 0;
        numEdge = 0;
        isDirected = directed;
        vertices = new LinkedHashMap<>();
    }

    @Override
    public int numVertices() {
        return numVert;
    }

    @Override
    public Iterable<V> vertices() {
        return vertices.keySet();
    }

    public boolean validVertex(V vert) {
        return vertices.get(vert) != null;
    }

    public int getKey(V vert) {
        return vertices.get(vert).getKey();
    }

    @Override
    public int numEdges() {
        return numEdge;
    }

    public Iterable<V> adjVertices(V vert) {

        if (!validVertex(vert)) {
            return null;
        }

        Vertex<V, E> vertex = vertices.get(vert);

        return vertex.getAllAdjVerts();
    }

    @Override
    public Iterable<Edge<V, E>> edges() {
        List<Edge<V, E>> edgeList = new ArrayList<>();
        Iterator<Vertex<V, E>> it1 = this.vertices.values().iterator();
        while (it1.hasNext()) {
            Vertex<V, E> v = it1.next();
            Iterator<V> it2 = v.getAllAdjVerts().iterator();
            while (it2.hasNext()) {
                edgeList.add(v.getEdge(it2.next()));
            }
        }
        return edgeList;
    }

    @Override
    public Edge<V, E> getEdge(V vOrig, V vDest) {

        if (!validVertex(vOrig) || !validVertex(vDest)) {
            return null;
        }

        Vertex<V, E> vorig = vertices.get(vOrig);

        return vorig.getEdge(vDest);
    }

    @Override
    public V[] endVertices(Edge<V, E> edge) {
        if (edge == null) {
            return (V[]) Array.newInstance(null, 0);
        }

        if (!validVertex(edge.getVOrig()) || !validVertex(edge.getVDest())) {
            return (V[]) Array.newInstance(null, 0);
        }

        Vertex<V, E> vorig = vertices.get(edge.getVOrig());

        if (!edge.equals(vorig.getEdge(edge.getVDest()))) {
            return (V[]) Array.newInstance(null, 0);
        }

        return edge.getEndpoints();
    }

    @Override
    public Iterable<Edge<V, E>> outgoingEdges(V vert) {

        if (!validVertex(vert)) {
            return null;
        }

        Vertex<V, E> vertex = vertices.get(vert);

        return vertex.getAllOutEdges();
    }

    @Override
    public Iterable<Edge<V, E>> incomingEdges(V vert) {
        if (!validVertex(vert)) {
            return null;
        }
        List<Edge<V, E>> incoming = new ArrayList<>();
        Vertex<V, E> vertex = vertices.get(vert);
        Iterator<Vertex<V, E>> it = vertices.values().iterator();
        while (it.hasNext()) {
            Vertex<V, E> v = it.next();
            Edge<V, E> e = v.getEdge(vert);
            if (e != null) {
                incoming.add(e);
            }
        }
        return incoming;
    }

    @Override
    public boolean insertVertex(V vert) {

        if (validVertex(vert)) {
            return false;
        }

        Vertex<V, E> vertex = new Vertex<>(numVert, vert);
        vertices.put(vert, vertex);
        numVert++;

        return true;
    }

    @Override
    public boolean insertEdge(V vOrig, V vDest, E eInf, double eWeight) {

        if (getEdge(vOrig, vDest) != null) {
            return false;
        }

        if (!validVertex(vOrig)) {
            insertVertex(vOrig);
        }

        if (!validVertex(vDest)) {
            insertVertex(vDest);
        }

        Vertex<V, E> vorig = vertices.get(vOrig);
        Vertex<V, E> vdest = vertices.get(vDest);

        Edge<V, E> newEdge = new Edge<>(eInf, eWeight, vorig, vdest);
        vorig.addAdjVert(vDest, newEdge);
        numEdge++;

        //if graph is not direct insert other edge in the opposite direction 
        if (!isDirected) // if vDest different vOrig
        {
            if (getEdge(vDest, vOrig) == null) {
                Edge<V, E> otherEdge = new Edge<>(eInf, eWeight, vdest, vorig);
                vdest.addAdjVert(vOrig, otherEdge);
                numEdge++;
            }
        }

        return true;
    }

    @Override
    public boolean removeVertex(V vert) {

        if (!validVertex(vert)) {
            return false;
        }

        //remove all edges that point to vert
        for (Edge<V, E> edge : incomingEdges(vert)) {
            V vadj = edge.getVOrig();
            removeEdge(vadj, vert);
        }

        Vertex<V, E> vertex = vertices.get(vert);

        //update the keys of subsequent vertices in the map
        vertices.values().forEach((v) -> {
            int keyVert = v.getKey();
            if (keyVert > vertex.getKey()) {
                keyVert = keyVert - 1;
                v.setKey(keyVert);
            }
        });
        //The edges that live from vert are removed with the vertex    
        vertices.remove(vert);

        numVert--;

        return true;
    }

    @Override
    public boolean removeEdge(V vOrig, V vDest) {

        if (!validVertex(vOrig) || !validVertex(vDest)) {
            return false;
        }

        Edge<V, E> edge = getEdge(vOrig, vDest);

        if (edge == null) {
            return false;
        }

        Vertex<V, E> vorig = vertices.get(vOrig);

        vorig.remAdjVert(vDest);
        numEdge--;

        //if graph is not direct 
        if (!isDirected) {
            edge = getEdge(vDest, vOrig);
            if (edge != null) {
                Vertex<V, E> vdest = vertices.get(vDest);
                vdest.remAdjVert(vOrig);
                numEdge--;
            }
        }
        return true;
    }

//
//    @Override
//    public int hashCode() {
//        return Objects.hash(numVert, numEdge, isDirected, vertices);
//    }
}
