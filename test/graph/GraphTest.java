package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mihash
 */
public class GraphTest {

    Graph<String, String> instance = new Graph<>(true);

    public GraphTest() {
    }

    /**
     * Test of numVertices method, of class Graph.
     */
    @Test
    public void testNumVertices() {
        System.out.println("Test numVertices");

        assertTrue((instance.numVertices() == 0));

        instance.insertVertex("A");
        assertTrue((instance.numVertices() == 1));

        instance.insertVertex("B");
        assertTrue((instance.numVertices() == 2));

        instance.removeVertex("A");
        assertTrue((instance.numVertices() == 1));

        instance.removeVertex("B");
        assertTrue((instance.numVertices() == 0));
    }

    /**
     * Test of vertices method, of class Graph.
     */
    @Test
    public void testVertices() {
        System.out.println("Test vertices");

        Iterator<String> itVerts = instance.vertices().iterator();

        assertTrue(itVerts.hasNext() == false);

        instance.insertVertex("A");
        instance.insertVertex("B");

        itVerts = instance.vertices().iterator();

        assertTrue((itVerts.next().compareTo("A") == 0));
        assertTrue((itVerts.next().compareTo("B") == 0));

        instance.removeVertex("A");

        itVerts = instance.vertices().iterator();
        assertTrue((itVerts.next().compareTo("B")) == 0);

        instance.removeVertex("B");

        itVerts = instance.vertices().iterator();
        assertTrue(itVerts.hasNext() == false);
    }

    /**
     * Test of numEdges method, of class Graph.
     */
    @Test
    public void testNumEdges() {
        System.out.println("Test numEdges");

        assertTrue((instance.numEdges() == 0));

        instance.insertEdge("A", "B", "Edge1", 6);
        assertTrue((instance.numEdges() == 1));

        instance.insertEdge("A", "C", "Edge2", 1);
        assertTrue((instance.numEdges() == 2));

        instance.removeEdge("A", "B");
        assertTrue((instance.numEdges() == 1));

        instance.removeEdge("A", "C");
        assertTrue((instance.numEdges() == 0));
    }

    /**
     * Test of edges method, of class Graph.
     */
    @Test
    public void testEdges() {
        System.out.println("Test Edges");

        Iterator<Edge<String, String>> itEdge = instance.edges().iterator();

        assertTrue((itEdge.hasNext() == false));

        instance.insertEdge("A", "B", "Edge1", 6);
        instance.insertEdge("A", "C", "Edge2", 1);
        instance.insertEdge("B", "D", "Edge3", 3);
        instance.insertEdge("C", "D", "Edge4", 4);
        instance.insertEdge("C", "E", "Edge5", 1);
        instance.insertEdge("D", "A", "Edge6", 2);
        instance.insertEdge("E", "D", "Edge7", 1);
        instance.insertEdge("E", "E", "Edge8", 1);

        itEdge = instance.edges().iterator();

        itEdge.next();
        itEdge.next();
        assertTrue(itEdge.next().getElement().equals("Edge3") == true);

        itEdge.next();
        itEdge.next();
        assertTrue(itEdge.next().getElement().equals("Edge6") == true);

        instance.removeEdge("A", "B");

        itEdge = instance.edges().iterator();
        assertTrue(itEdge.next().getElement().equals("Edge2") == true);

        instance.removeEdge("A", "C");
        instance.removeEdge("B", "D");
        instance.removeEdge("C", "D");
        instance.removeEdge("C", "E");
        instance.removeEdge("D", "A");
        instance.removeEdge("E", "D");
        instance.removeEdge("E", "E");
        itEdge = instance.edges().iterator();
        assertTrue((itEdge.hasNext() == false));
    }

    /**
     * Test of getEdge method, of class Graph.
     */
    @Test
    public void testGetEdge() {
        System.out.println("Test getEdge");

        instance.insertEdge("A", "B", "Edge1", 6);
        instance.insertEdge("A", "C", "Edge2", 1);
        instance.insertEdge("B", "D", "Edge3", 3);
        instance.insertEdge("C", "D", "Edge4", 4);
        instance.insertEdge("C", "E", "Edge5", 1);
        instance.insertEdge("D", "A", "Edge6", 2);
        instance.insertEdge("E", "D", "Edge7", 1);
        instance.insertEdge("E", "E", "Edge8", 1);

        assertTrue(instance.getEdge("A", "E") == null);

        assertTrue(instance.getEdge("B", "D").getElement().equals("Edge3") == true);
        assertTrue(instance.getEdge("D", "B") == null);

        instance.removeEdge("D", "A");
        assertTrue(instance.getEdge("D", "A") == null);
        assertTrue(instance.getEdge("E", "E").getElement().equals("Edge8") == true);
    }

    /**
     * Test of endVertices method, of class Graph.
     */
    @Test
    public void testEndVertices() {
        System.out.println("Test endVertices");

        instance.insertEdge("A", "B", "Edge1", 6);
        instance.insertEdge("A", "C", "Edge2", 1);
        instance.insertEdge("B", "D", "Edge3", 3);
        instance.insertEdge("C", "D", "Edge4", 4);
        instance.insertEdge("C", "E", "Edge5", 1);
        instance.insertEdge("D", "A", "Edge6", 2);
        instance.insertEdge("E", "D", "Edge7", 1);
        instance.insertEdge("E", "E", "Edge8", 1);

        Edge<String, String> edge0 = new Edge<>();

        String[] vertices = new String[2];

        //assertTrue("endVertices should be null", instance.endVertices(edge0)==null);
        Edge<String, String> edge1 = instance.getEdge("A", "B");
        //vertices = instance.endVertices(edge1);
        assertTrue(instance.endVertices(edge1)[0].equals("A"));
        assertTrue(instance.endVertices(edge1)[1].equals("B"));
    }

    /**
     * Test of opposite method, of class Graph.
     */
    /**
     * Test of outgoingEdges method, of class Graph.
     */
    @Test
    public void testOutgoingEdges() {
        System.out.println(" Test outgoingEdges");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.insertEdge("A", "B", "Edge1", 6);
        instance.insertEdge("A", "C", "Edge2", 1);
        instance.insertEdge("B", "D", "Edge3", 3);
        instance.insertEdge("C", "D", "Edge4", 4);
        instance.insertEdge("C", "E", "Edge5", 1);
        instance.insertEdge("D", "A", "Edge6", 2);
        instance.insertEdge("E", "D", "Edge7", 1);
        instance.insertEdge("E", "E", "Edge8", 1);

        Iterator<Edge<String, String>> itEdge = instance.outgoingEdges("C").iterator();
        Edge<String, String> first = itEdge.next();
        Edge<String, String> second = itEdge.next();
        assertTrue(((first.getElement().equals("Edge4") == true && second.getElement().equals("Edge5") == true)
                || (first.getElement().equals("Edge5") == true && second.getElement().equals("Edge4") == true)));

        instance.removeEdge("E", "E");

        itEdge = instance.outgoingEdges("E").iterator();
        assertTrue((itEdge.next().getElement().equals("Edge7") == true));

        instance.removeEdge("E", "D");

        itEdge = instance.outgoingEdges("E").iterator();
        assertTrue((itEdge.hasNext() == false));
    }

    /**
     * Test of incomingEdges method, of class Graph.
     */
    @Test
    public void testIncomingEdges() {

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.insertEdge("A", "B", "Edge1", 6);
        instance.insertEdge("A", "C", "Edge2", 1);
        instance.insertEdge("B", "D", "Edge3", 3);
        instance.insertEdge("C", "D", "Edge4", 4);
        instance.insertEdge("C", "E", "Edge5", 1);
        instance.insertEdge("D", "A", "Edge6", 2);
        instance.insertEdge("E", "D", "Edge7", 1);
        instance.insertEdge("E", "E", "Edge8", 1);

        Iterator<Edge<String, String>> itEdge = instance.incomingEdges("D").iterator();

        assertTrue((itEdge.next().getElement().equals("Edge3") == true));
        assertTrue((itEdge.next().getElement().equals("Edge4") == true));
        assertTrue((itEdge.next().getElement().equals("Edge7") == true));

        itEdge = instance.incomingEdges("E").iterator();

        assertTrue((itEdge.next().getElement().equals("Edge5") == true));
        assertTrue((itEdge.next().getElement().equals("Edge8") == true));

        instance.removeEdge("E", "E");

        itEdge = instance.incomingEdges("E").iterator();

        assertTrue((itEdge.next().getElement().equals("Edge5") == true));

        instance.removeEdge("C", "E");

        itEdge = instance.incomingEdges("E").iterator();
        assertTrue((itEdge.hasNext() == false));
    }

    /**
     * Test of insertVertex method, of class Graph.
     */
    @Test
    public void testInsertVertex() {
        System.out.println("Test insertVertex");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        Iterator<String> itVert = instance.vertices().iterator();

        assertTrue((itVert.next().equals("A") == true));
        assertTrue((itVert.next().equals("B") == true));
        assertTrue((itVert.next().equals("C") == true));
        assertTrue((itVert.next().equals("D") == true));
        assertTrue((itVert.next().equals("E") == true));
    }

    /**
     * Test of insertEdge method, of class Graph.
     */
    @Test
    public void testInsertEdge() {
        System.out.println("Test insertEdge");

        assertTrue((instance.numEdges() == 0));

        instance.insertEdge("A", "B", "Edge1", 6);
        assertTrue((instance.numEdges() == 1));

        instance.insertEdge("A", "C", "Edge2", 1);
        assertTrue((instance.numEdges() == 2));

        instance.insertEdge("B", "D", "Edge3", 3);
        assertTrue(instance.numEdges() == 3);

        instance.insertEdge("C", "D", "Edge4", 4);
        assertTrue((instance.numEdges() == 4));

        instance.insertEdge("C", "E", "Edge5", 1);
        assertTrue((instance.numEdges() == 5));

        instance.insertEdge("D", "A", "Edge6", 2);
        assertTrue((instance.numEdges() == 6));

        instance.insertEdge("E", "D", "Edge7", 1);
        assertTrue((instance.numEdges() == 7));

        instance.insertEdge("E", "E", "Edge8", 1);
        assertTrue((instance.numEdges() == 8));

        Iterator<Edge<String, String>> itEd = instance.edges().iterator();
        itEd.next();
        itEd.next();
        assertTrue((itEd.next().getElement().equals("Edge3")));
        itEd.next();
        itEd.next();
        assertTrue((itEd.next().getElement().equals("Edge6")));
    }

    /**
     * Test of removeVertex method, of class Graph.
     */
    @Test
    public void testRemoveVertex() {
        System.out.println("Test removeVertex");

        instance.insertVertex("A");
        instance.insertVertex("B");
        instance.insertVertex("C");
        instance.insertVertex("D");
        instance.insertVertex("E");

        instance.removeVertex("C");
        assertTrue((instance.numVertices() == 4));

        Iterator<String> itVert = instance.vertices().iterator();
        assertTrue((itVert.next().equals("A") == true));
        assertTrue((itVert.next().equals("B") == true));
        assertTrue((itVert.next().equals("D") == true));
        assertTrue((itVert.next().equals("E") == true));

        instance.removeVertex("A");
        assertTrue((instance.numVertices() == 3));

        itVert = instance.vertices().iterator();
        assertTrue((itVert.next().equals("B") == true));
        assertTrue((itVert.next().equals("D") == true));
        assertTrue((itVert.next().equals("E") == true));

        instance.removeVertex("E");
        assertTrue((instance.numVertices() == 2));

        itVert = instance.vertices().iterator();

        assertTrue(itVert.next().equals("B") == true);
        assertTrue(itVert.next().equals("D") == true);

        instance.removeVertex("B");
        instance.removeVertex("D");
        assertTrue((instance.numVertices() == 0));
    }

    /**
     * Test of removeEdge method, of class Graph.
     */
    @Test
    public void testRemoveEdge() {
        System.out.println("Test removeEdge");

        assertTrue((instance.numEdges() == 0));

        instance.insertEdge("A", "B", "Edge1", 6);
        instance.insertEdge("A", "C", "Edge2", 1);
        instance.insertEdge("B", "D", "Edge3", 3);
        instance.insertEdge("C", "D", "Edge4", 4);
        instance.insertEdge("C", "E", "Edge5", 1);
        instance.insertEdge("D", "A", "Edge6", 2);
        instance.insertEdge("E", "D", "Edge7", 1);
        instance.insertEdge("E", "E", "Edge8", 1);

        assertTrue((instance.numEdges() == 8));

        instance.removeEdge("E", "E");
        assertTrue((instance.numEdges() == 7));

        Iterator<Edge<String, String>> itEd = instance.edges().iterator();

        itEd.next();
        itEd.next();
        assertTrue((itEd.next().getElement().equals("Edge3") == true));
        itEd.next();
        itEd.next();
        assertTrue((itEd.next().getElement().equals("Edge6") == true));

        instance.removeEdge("C", "D");
        assertTrue((instance.numEdges() == 6));

        itEd = instance.edges().iterator();
        itEd.next();
        itEd.next();
        assertTrue((itEd.next().getElement().equals("Edge3") == true));
        assertTrue((itEd.next().getElement().equals("Edge5") == true));
        assertTrue((itEd.next().getElement().equals("Edge6") == true));
        assertTrue((itEd.next().getElement().equals("Edge7") == true));
    }

//    @Test
//    public void TestHashCode() {
//        Graph<String,String> graph = new Graph<>(true);
//        assertEquals(Objects.hash(0,0,true,new LinkedHashMap<>()),graph.hashCode());
//
//        graph = new Graph<>(false);
//        assertEquals(Objects.hash(0,0,false,new LinkedHashMap<>()),graph.hashCode());
//    }
    @Test
    public void validVertex() {
        Graph<String, String> graph = new Graph<>(false);
        graph.insertVertex("Elo");
        assertTrue(graph.validVertex("Elo"));
    }

    @Test
    public void notValidVertex() {
        Graph<String, String> graph = new Graph<>(false);
        graph.insertVertex("Elo");
        assertFalse(graph.validVertex("Yo"));
    }

    @Test
    public void outgoingEdges() {

        Graph<String, String> graph = new Graph<>(false);
        graph.insertVertex("Elo");
        graph.insertVertex("Yo");
        graph.insertEdge("Elo", "Yo", "LALA", 3);
        assertEquals("Yo", graph.outgoingEdges("Elo").iterator().next().getVDest());

    }

    @Test
    public void incomingEdges() {

        Graph<String, String> graph = new Graph<>(false);
        graph.insertVertex("Elo");
        graph.insertVertex("Yo");
        graph.insertEdge("Elo", "Yo", "LALA", 3);
        assertEquals("Elo", graph.outgoingEdges("Yo").iterator().next().getVDest());

    }

    @Test
    public void testAdjVerticesNotNull() {
        System.out.println("adjVertices");
        Graph<String, String> g = new Graph<>(true);
        g.insertVertex("Initial");
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertEdge("Initial", "A", "x", 1);
        g.insertEdge("Initial", "B", "x", 1);
        Iterator<String> x = g.adjVertices("Initial").iterator();
        assertEquals("A", x.next());
        assertEquals("B", x.next());
    }

    @Test
    public void testAdjVerticesNull() {
        System.out.println("adjVertices");
        Graph<String, String> g = new Graph<>(true);
        g.insertVertex("Initial");
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertEdge("Initial", "A", "x", 1);
        g.insertEdge("Initial", "B", "x", 1);
        assertNull(g.adjVertices("X"));
    }

}
