/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author gcabral
 * @param <V>
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class Vertex<V, E> {

    private int key;                     //Vertex key number
    private V element;                 //Vertex information
    private Map<V, Edge<V, E>> outVerts; //adjacent vertices

    public Vertex() {
        key = -1;
        element = null;
        outVerts = new LinkedHashMap<>();
    }

    public Vertex(int k, V vInf) {
        key = k;
        element = vInf;
        outVerts = new LinkedHashMap<>();
    }

    public int getKey() {
        return key;
    }

    public void setKey(int k) {
        key = k;
    }

    public V getElement() {
        return element;
    }

    public void setElement(V vInf) {
        element = vInf;
    }

    public void addAdjVert(V vAdj, Edge<V, E> edge) {
        outVerts.put(vAdj, edge);
    }

    public V getAdjVert(Edge<V, E> edge) {

        for (V vert : outVerts.keySet()) {
            if (edge.equals(outVerts.get(vert))) {
                return vert;
            }
        }

        return null;
    }

    public void remAdjVert(V vAdj) {
        outVerts.remove(vAdj);
    }

    public Edge<V, E> getEdge(V vAdj) {
        return outVerts.get(vAdj);
    }

    public int numAdjVerts() {
        return outVerts.size();
    }

    public Iterable<V> getAllAdjVerts() {
        return outVerts.keySet();
    }

    public Iterable<Edge<V, E>> getAllOutEdges() {
        return outVerts.values();
    }
//
//    @Override
//    public boolean equals(Object otherObj) {
//
//        if (this == otherObj) {
//            return true;
//        }
//
//        if (otherObj == null || this.getClass() != otherObj.getClass()) {
//            return false;
//        }
//
//        Vertex<V, E> otherVertex = (Vertex<V, E>) otherObj;
//
//        if (this.key != otherVertex.key) {
//            return false;
//        }
//
//        if (this.element != null && otherVertex.element != null
//                && !this.element.equals(otherVertex.element)) {
//            return false;
//        }
//
//        //adjacency vertices should be equal
//        if (this.numAdjVerts() != otherVertex.numAdjVerts()) {
//            return false;
//        }
//
//        //and edges also
//        Iterator<Edge<V, E>> it1 = this.getAllOutEdges().iterator();
//        while (it1.hasNext()) {
//            Iterator<Edge<V, E>> it2 = otherVertex.getAllOutEdges().iterator();
//            boolean exists = false;
//            while (it2.hasNext()) {
//                if (it1.next().equals(it2.next())) {
//                    exists = true;
//                }
//            }
//            if (!exists) {
//                return false;
//            }
//        }
//        return true;
//    }
//@Override
//public int hashCode() {
//    return Objects.hash(key, element, outVerts);
//}

}
