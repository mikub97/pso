package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mihash
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GraphAlgorithmsTest {

    Graph<String, String> completeMap = new Graph<>(false);
    Graph<String, String> incompleteMap = new Graph<>(false);

    public GraphAlgorithmsTest() {
    }

    @BeforeAll
    public void setUp() throws Exception {

        completeMap.insertVertex("Porto");
        completeMap.insertVertex("Braga");
        completeMap.insertVertex("Vila Real");
        completeMap.insertVertex("Aveiro");
        completeMap.insertVertex("Coimbra");
        completeMap.insertVertex("Leiria");

        completeMap.insertVertex("Viseu");
        completeMap.insertVertex("Guarda");
        completeMap.insertVertex("Castelo Branco");
        completeMap.insertVertex("Lisboa");
        completeMap.insertVertex("Faro");

        completeMap.insertEdge("Porto", "Aveiro", "A1", 75);
        completeMap.insertEdge("Porto", "Braga", "A3", 60);
        completeMap.insertEdge("Porto", "Vila Real", "A4", 100);
        completeMap.insertEdge("Viseu", "Guarda", "A25", 75);
        completeMap.insertEdge("Guarda", "Castelo Branco", "A23", 100);
        completeMap.insertEdge("Aveiro", "Coimbra", "A1", 60);
        completeMap.insertEdge("Coimbra", "Lisboa", "A1", 200);
        completeMap.insertEdge("Coimbra", "Leiria", "A34", 80);
        completeMap.insertEdge("Aveiro", "Leiria", "A17", 120);
        completeMap.insertEdge("Leiria", "Lisboa", "A8", 150);

        completeMap.insertEdge("Aveiro", "Viseu", "A25", 85);
        completeMap.insertEdge("Leiria", "Castelo Branco", "A23", 170);
        completeMap.insertEdge("Lisboa", "Faro", "A2", 280);

        incompleteMap.insertVertex("Porto");
        incompleteMap.insertVertex("Braga");
        incompleteMap.insertVertex("Vila Real");
        incompleteMap.insertVertex("Aveiro");
        incompleteMap.insertVertex("Coimbra");
        incompleteMap.insertVertex("Leiria");

        incompleteMap.insertVertex("Viseu");
        incompleteMap.insertVertex("Guarda");
        incompleteMap.insertVertex("Castelo Branco");
        incompleteMap.insertVertex("Lisboa");
        incompleteMap.insertVertex("Faro");

        incompleteMap.insertEdge("Porto", "Aveiro", "A1", 75);
        incompleteMap.insertEdge("Porto", "Braga", "A3", 60);
        incompleteMap.insertEdge("Porto", "Vila Real", "A4", 100);
        incompleteMap.insertEdge("Viseu", "Guarda", "A25", 75);
        incompleteMap.insertEdge("Guarda", "Castelo Branco", "A23", 100);
        incompleteMap.insertEdge("Aveiro", "Coimbra", "A1", 60);
        incompleteMap.insertEdge("Coimbra", "Lisboa", "A1", 200);
        incompleteMap.insertEdge("Coimbra", "Leiria", "A34", 80);
        incompleteMap.insertEdge("Aveiro", "Leiria", "A17", 120);
        incompleteMap.insertEdge("Leiria", "Lisboa", "A8", 150);

        incompleteMap.insertEdge("Aveiro", "Viseu", "A25", 85);
        incompleteMap.insertEdge("Leiria", "Castelo Branco", "A23", 170);
        incompleteMap.insertEdge("Lisboa", "Faro", "A2", 280);

        incompleteMap.removeEdge("Aveiro", "Viseu");
        incompleteMap.removeEdge("Leiria", "Castelo Branco");
        incompleteMap.removeEdge("Lisboa", "Faro");
    }

    @Test
    public void testShortestPath() {
        System.out.println("Test of shortest path");
        LinkedList<String> shortPath = new LinkedList<>();
        double lenpath = 0;
        lenpath = GraphAlgorithms.shortestPath(completeMap, "Porto", "LX", shortPath);
        assertTrue(lenpath == 0);

        lenpath = GraphAlgorithms.shortestPath(incompleteMap, "Porto", "Faro", shortPath);
        assertTrue(lenpath == 0);
        lenpath = GraphAlgorithms.shortestPath(completeMap, "Porto", "Porto", shortPath);
        assertTrue(shortPath.size() == 1);

        lenpath = GraphAlgorithms.shortestPath(incompleteMap, "Porto", "Lisboa", shortPath);
        assertTrue(lenpath == 335);

        Iterator<String> it = shortPath.iterator();

        assertTrue(it.next().compareTo("Porto") == 0);
        assertTrue(it.next().compareTo("Aveiro") == 0);
        assertTrue(it.next().compareTo("Coimbra") == 0);
        assertTrue(it.next().compareTo("Lisboa") == 0);

        lenpath = GraphAlgorithms.shortestPath(incompleteMap, "Braga", "Leiria", shortPath);
        assertTrue(lenpath == 255);

        it = shortPath.iterator();

        assertTrue(it.next().compareTo("Braga") == 0);
        assertTrue(it.next().compareTo("Porto") == 0);
        assertTrue(it.next().compareTo("Aveiro") == 0);
        assertTrue(it.next().compareTo("Leiria") == 0);

        shortPath.clear();
        lenpath = GraphAlgorithms.shortestPath(completeMap, "Porto", "Castelo Branco", shortPath);
        assertTrue(lenpath == 335);
        assertTrue(shortPath.size() == 5);

        it = shortPath.iterator();

        assertTrue(it.next().compareTo("Porto") == 0);
        assertTrue(it.next().compareTo("Aveiro") == 0);
        assertTrue(it.next().compareTo("Viseu") == 0);
        assertTrue(it.next().compareTo("Guarda") == 0);
        assertTrue(it.next().compareTo("Castelo Branco") == 0);

        //Changing Edge: Aveiro-Viseu with Edge: Leiria-C.Branco
        //should change shortest path between Porto and Castelo Branco
        completeMap.removeEdge("Aveiro", "Viseu");
        completeMap.insertEdge("Leiria", "Castelo Branco", "A23", 170);
        shortPath.clear();
        lenpath = GraphAlgorithms.shortestPath(completeMap, "Porto", "Castelo Branco", shortPath);
        assertTrue(lenpath == 365);
        assertTrue(shortPath.size() == 4);

        it = shortPath.iterator();

        assertTrue(it.next().compareTo("Porto") == 0);
        assertTrue(it.next().compareTo("Aveiro") == 0);
        assertTrue(it.next().compareTo("Leiria") == 0);
        assertTrue(it.next().compareTo("Castelo Branco") == 0);

    }

    /**
     * Test of allPaths method, of class GraphAlgorithms.
     */
    @Test
    public void testAllPaths() {
        System.out.println("Test of all paths");

        List<LinkedList<String>> paths = new ArrayList<LinkedList<String>>();

        paths = GraphAlgorithms.allPaths(completeMap, "Porto", "LX");
        assertTrue(paths.size() == 0);

        paths = GraphAlgorithms.allPaths(incompleteMap, "Porto", "Lisboa");
        assertTrue(paths.size() == 4);

        paths = GraphAlgorithms.allPaths(incompleteMap, "Porto", "Faro");
        assertTrue(paths.size() == 0);
    }

}
