package src;

import java.io.*;
import java.util.*;

public class Mainframe {
    boolean k = true;

    private int[] bestTourOrder;
    private double bestTourLength;

    private double[] probabilities;

    private int currentIndex;
    private double alpha;
    private double beta;
    private double evaporation;
    private int vertices;  //wierzchołki
    private int antCount;
    private int iterations;

    private ArrayList<Ant> ants;
    //macierz zawierająca odległości ORAZ feromony
    //na górze od diagonali odległości
    //na dole feromony
    //na diagonali zera
    private double[][] graph;

    public Random random;

    public Mainframe() {
        Random random = new Random();
        try (InputStream input = new FileInputStream("properties.txt")) {
            Properties prop = new Properties();
            prop.load(input);
            alpha = Double.parseDouble(prop.getProperty("alpha"));
            antCount = Integer.parseInt(prop.getProperty("ants"));
            iterations = Integer.parseInt(prop.getProperty("it"));
            beta = Double.parseDouble(prop.getProperty("beta"));
            evaporation = Double.parseDouble(prop.getProperty("evaporation"));
            vertices = Integer.parseInt(prop.getProperty("vertices"));
        } catch (IOException ex) {
            System.out.println("Something wrong with reading the file");
            ex.printStackTrace();
        }
        ants = new ArrayList<>();
        probabilities = new double[vertices];

        graph = new double[vertices][vertices];
        //wypełniania odległościami (losowa liczba) i wartościami początkowymi feromonów - 1
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i > j)
                    graph[i][j] = 1;
                else if (i == j)
                    graph[i][j] = 0;
                else
                    graph[i][j] = Math.abs(random.nextInt(100) + 1);
            }
        }
         /*//graf do testowania - min. odległość to 150, zmien w properties ilość wierzcholkow na 4!
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i > j)
                    graph[i][j] = 1;
                else if (i == j)
                    graph[i][j] = 0;

                graph[0][1] = 20;
                graph[0][2] = 10;
                graph[0][3] = 50;
                graph[1][2] = 30;
                graph[1][3] = 60;
                graph[2][3] = 80;
            }
        }*/
        printMatrix();
        initializeAnts();
        startSolving();
    }

    private void startSolving() {
        //reset listy odwiedzonych miast
        resetAnts();
        //główna pętla
        for (int i = 0; i < iterations; i++) {
            k=true;
            System.out.println("Iteration: " + (i+1)+".");
            //tu mrówki sb chodzą dopóki nie obejdą wszystkich miast
            moveAnts();
            //po przejściu obliczamy feromony między miastami
            updateWays();
            //znajdujemy najlepszą trasę w iteracji
            updateBest();
            //reset listy odwiedzonych miast
            resetAnts();
            //ten indeks pomocniczy zwieksza się wraz z każdym odwiedzonym miastem
            currentIndex = 0;
        }
        System.out.println("Best tour length: " + (bestTourLength));
        System.out.println("Best tour order: " + Arrays.toString(bestTourOrder));


    }

    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants.get(0).way;
            bestTourLength = ants.get(0)
                    .wayLength(graph);
        }
        for (Ant a : ants) {
            if (a.wayLength(graph) < bestTourLength) {
                bestTourLength = a.wayLength(graph);
                bestTourOrder = a.way.clone();
            }
        }
    }

    private void updateWays() {
        //odparowanie dla wszystkich krawędzi
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i < j)
                    graph[j][i] *= evaporation;
                else
                    graph[i][j] *= evaporation;
            }
        }
        //dla każdej mrówki patrzymy na krawędzie po ktorych sb łaziła
        // i liczymy feromony na tych krawędziach
        for (Ant a : ants) {
            //printMatrix();
            System.out.println(Arrays.toString(a.way));
            //droga od pierwszego do ostatniego
            double contribution = a.wayLength(graph);
            for (int i = 0; i < vertices - 1; i++) {

                if (a.way[i] > a.way[i + 1]) {
                    graph[a.way[i]][a.way[i + 1]] += contribution;
                } else {
                    graph[a.way[i + 1]][a.way[i]] += contribution;
                }
            }
            //droga z ostatniego do pierwszego danej mrowki
            if (a.way[0] > a.way[vertices - 1])
                graph[a.way[0]][a.way[vertices - 1]] += contribution;
            else
                graph[a.way[vertices - 1]][a.way[0]] += contribution;

            System.out.println("contriburion" + contribution);
        }
    }
    private void moveAnts() {
        //ruszamy wszystkie mrówki przez wszystkie krawędzie
        for (int i = currentIndex; i < vertices - 1; i++) {
            for (int j = 0; j < ants.size(); j++) {
                //zmiana wierzchołka
                ants.get(j).move(pickNextVertex(ants.get(j)), currentIndex);
                if(k) {
                    System.out.println(Arrays.toString(probabilities));
                    k=false;
                }

            }
            //zwiększamy index bo kolejne miasto
            currentIndex++;
        }


    }

    private int pickNextVertex(Ant ant) {
        //wybieramy kolejny wierzchołek mróweczce
        random = new Random();

        calculateProbabilities(ant);
        //System.out.println(Arrays.toString(probabilities));
        //System.out.println(Arrays.toString(ant.way));
        //losujemy wierzchołek:
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < vertices; i++) {
            total += probabilities[i];

            if (total >= r) {
                return i;
            }
        }
        //jak to wyświetli to coś się zrąbało
        throw new RuntimeException("There are no other cities");
    }

    private void calculateProbabilities(Ant ant) {
        //i to nr wierzchołka na ktorym jest mruwa
        int i = ant.way[currentIndex];
        double pheromone = 0.0;
        int l, j;
        //chodzenie indeksami po tej macierzy - to była katorga zakodzić to
        //obliczanie mianownika tego równania
        for (l = 0; l < vertices; l++) {
            if (!ant.checkIfVisited(l) && l!=i){
                if(i>l)
                    pheromone += Math.pow(graph[i][l], alpha) * Math.pow(1 / graph[l][i], beta);
                else
                    pheromone += Math.pow(graph[l][i], alpha) * Math.pow(1 / graph[i][l], beta);
            }

        }

        probabilities[i] = 0.0;
        //chodzenie indeksami po tej macierzy - to była katorga zakodzić to
        //prawdopodobieństwo dla każdego wierzchołka dla mróweczki w danej chwili
        for (j = 0; j < vertices; j++) {
            if (ant.checkIfVisited(j) ) {
                probabilities[j] = 0.0;
            } else if(i!=j){
                if(i>j) {
                    double numerator = Math.pow(graph[i][j], alpha) * Math.pow(1.0 / graph[j][i], beta);
                    probabilities[j] = numerator / pheromone;
                }else{
                    double numerator = Math.pow(graph[j][i], alpha) * Math.pow(1.0 / graph[i][j], beta);
                    probabilities[j] = numerator / pheromone;
                }
            }
        }


    }

    private void initializeAnts() {
        for (int i = 0; i < antCount; i++) {
            ants.add(new Ant(vertices));
        }

    }

    private void resetAnts() {
        random = new Random();

        for (int i = 0; i < antCount; i++) {
            ants.get(i).clear();
        }

        currentIndex = 0;
    }

    public void printMatrix() {
        System.out.println("Matrix:");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                System.out.printf(graph[i][j] + "  ");
            }
            System.out.println();

        }
        System.out.println("###################");
    }
}
