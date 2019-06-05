package src;

import java.io.*;
import java.util.*;

public class Mainframe {
    private int[] bestTourOrder;
    private double bestTourLength;
    private double[] probabilities;
    private int currentIndex;
    private double alpha;
    private double beta;
    private double evaporation;
    private ArrayList<Ant> ants;
    private double[][] graph;
    private int vertices;
    private int antCount;
    private int iterations;
    public Random random;

    public Mainframe() throws IOException {
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
            ex.printStackTrace();
        }
        ants = new ArrayList<Ant>();
        probabilities = new double[vertices];
        graph = new double[vertices][vertices];
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
        /*for (int i = 0; i < vertices; i++) {
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
        resetAnts();
        for (int i = 0; i < iterations; i++) {
            k=true;
            System.out.println("Iteration: " + (i+1)+".");

            moveAnts();
            updateWays();
            updateBest();
            resetAnts();
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
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i < j)
                    graph[j][i] *= evaporation;
                else
                    graph[i][j] *= evaporation;
            }
        }

        for (Ant a : ants) {
            //printMatrix();
            System.out.println(Arrays.toString(a.way));

            double contribution = a.wayLength(graph);
            for (int i = 0; i < vertices - 1; i++) {

                if (a.way[i] > a.way[i + 1]) {
                    graph[a.way[i]][a.way[i + 1]] += contribution;
                } else {
                    graph[a.way[i + 1]][a.way[i]] += contribution;
                }
            }
            if (a.way[0] > a.way[vertices - 1])
                graph[a.way[0]][a.way[vertices - 1]] += contribution;
            else
                graph[a.way[vertices - 1]][a.way[0]] += contribution;

            System.out.println("contriburion" + contribution);
        }
    }
    boolean k = true;
    private void moveAnts() {
        for (int i = currentIndex; i < vertices - 1; i++) {
            for (int j = 0; j < ants.size(); j++) {
                ants.get(j).move(pickNextVertex(ants.get(j)), currentIndex);
                if(k) {
                    System.out.println(Arrays.toString(probabilities));
                    k=false;
                }

            }
            currentIndex++;
        }


    }

    private int pickNextVertex(Ant ant) {
        random = new Random();
        calculateProbabilities(ant);
        //System.out.println(Arrays.toString(probabilities));
        //System.out.println(Arrays.toString(ant.way));

        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < vertices; i++) {
            total += probabilities[i];

            if (total >= r) {
                return i;
            }
        }

        throw new RuntimeException("There are no other cities");
    }

    private void calculateProbabilities(Ant ant) {
        int i = ant.way[currentIndex];
        double pheromone = 0.0;
        int l, j;
        for (l = 0; l < i; l++) {
            if (!ant.checkIfVisited(l))
                pheromone += Math.pow(graph[i][l], alpha) * Math.pow(1 / graph[l][i], beta);
        }
        for (l = i + 1; l < vertices; l++) {
            if (!ant.checkIfVisited(l))
                pheromone += Math.pow(graph[l][i], alpha) * Math.pow(1 / graph[i][l], beta);

        }
        for (j = 0; j < i; j++) {
            if (ant.checkIfVisited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(graph[i][j], alpha) * Math.pow(1.0 / graph[j][i], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
        probabilities[j] = 0.0;
        for (j = i + 1; j < vertices; j++) {
            if (ant.checkIfVisited(j)) {
                probabilities[j] = 0.0;
            } else {
                double numerator = Math.pow(graph[j][i], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
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
