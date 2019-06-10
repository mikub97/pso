package pso;

import java.io.*;
import java.util.*;

public class Mainframe {
    private int[] bestTourOrder;
    private double bestTourLength;
    GraphDrawer graphDrawer;
    private double[] probabilities;
    private List<Calc> calcs;
    private int currentIndex;
    private int noThreads;
    private double alpha;
    private double beta;
    private double evaporation;
    private int vertices;  //wierzchołki
    private int antCount;
    private int iterations;
    private ArrayList<Ant> ants;

    //macierz zawierająca odległości ORAZ feromony 118320 117810
    //na górze od diagonali odległości
    //na dole feromony
    //na diagonali zera
    private double[][] graph;

    boolean parrarel;
    public Random random;

    /**
     * @param printMatrix : 0 nic nie wypisuje ; 1 pisze matrix na stdout ; 2 do pliku matrix.out
     *
     *
     * */

    public Mainframe(boolean parrarel, String propFile,int printMatrix,String matrixInFile) {

        this.parrarel = parrarel;
        random = new Random();

        try (InputStream input = new FileInputStream(propFile)) {
            Properties prop = new Properties();
            prop.load(input);
            alpha = Double.parseDouble(prop.getProperty("alpha"));
            antCount = Integer.parseInt(prop.getProperty("ants"));
            iterations = Integer.parseInt(prop.getProperty("it"));
            beta = Double.parseDouble(prop.getProperty("beta"));
            evaporation = Double.parseDouble(prop.getProperty("evaporation"));
            vertices = Integer.parseInt(prop.getProperty("vertices"));
            noThreads = Integer.parseInt(prop.getProperty("threads_count"));
        } catch (IOException ex) {
            System.out.println("Something wrong with reading the file");
            ex.printStackTrace();
        }
        ants = new ArrayList<>();
        probabilities = new double[vertices];
        calcs = new ArrayList<Calc>();
        for(int i=0;i<noThreads;i++)
            calcs.add(new Calc(this));

        readMatrix(matrixInFile);

        if (printMatrix==1)
            printMatrix();
        else if (printMatrix == 2)
            printMatrixInFile();
    }
    public Mainframe(boolean parrarel, Map prop,int printMatrix,String matrixInFile) {

        this.parrarel = parrarel;
        random = new Random();
        antCount = (int) prop.get("ants");
        alpha = (double )prop.get("alpha");
        iterations = (int)prop.get("it");
        beta = (double )prop.get("beta");
        evaporation =(double)prop.get("evaporation");
        vertices =(int)prop.get("vertices");
        noThreads = (int) prop.get("threads_count");
        ants = new ArrayList<>();
        probabilities = new double[vertices];
        calcs = new ArrayList<Calc>();
        for(int i=0;i<noThreads;i++)
            calcs.add(new Calc(this));

        readMatrix(matrixInFile);

        if (printMatrix==1)
            printMatrix();
        else if (printMatrix == 2)
            printMatrixInFile();
    }
    public Mainframe(boolean parrarel,String properFile,int printMatrix) {
        this.parrarel = parrarel;
        this.random = new Random();
        try (InputStream input = new FileInputStream(properFile)) {
            Properties prop = new Properties();
            prop.load(input);
            alpha = Double.parseDouble(prop.getProperty("alpha"));
            antCount = Integer.parseInt(prop.getProperty("ants"));
            iterations = Integer.parseInt(prop.getProperty("it"));
            beta = Double.parseDouble(prop.getProperty("beta"));
            evaporation = Double.parseDouble(prop.getProperty("evaporation"));
            vertices = Integer.parseInt(prop.getProperty("vertices"));
            noThreads = Integer.parseInt(prop.getProperty("threads_count"));
        } catch (IOException ex) {
            System.out.println("Something wrong with reading the file");
            ex.printStackTrace();
        }
        ants = new ArrayList<>();
        probabilities = new double[vertices];
        graphDrawer = new GraphDrawer();
        graphDrawer.generateCoords(vertices);
        calcs = new ArrayList<Calc>();
        for(int i=0;i<noThreads;i++)
            calcs.add(new Calc(this));

        randMatrix(vertices);

        if (printMatrix==1)
            printMatrix();
        else if (printMatrix == 2)
            printMatrixInFile();
    }

    public void readMatrix(String filename ){
        System.out.println("Reading graph from : " +filename);
        try {
            BufferedReader inputStream =new BufferedReader(new FileReader(new File(filename)));
            this.vertices =Integer.parseInt(inputStream.readLine());
            System.out.println("with " + vertices + " verticles");
            graph = new double[this.vertices][this.vertices];
            for (int i = 0; i < vertices; i++) {
                String line = inputStream.readLine();
                String[] val = line.split("  ");
                for (int j = 0; j < vertices; j++) {
                    graph[i][j] = Double.parseDouble(val[j]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void randMatrix(int vertices) {
        System.out.println("Creating random graph with " + vertices + " vertices");
        graph = new double[vertices][vertices];

        //wypełniania odległościami (losowa liczba) i wartościami początkowymi feromonów - 1
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i > j)
                    graph[i][j] = 1;
                else if (i == j)
                    graph[i][j] = 0;
                else
                    graph[i][j] = graphDrawer.getDistance(i,j);
            }
        }
        printMatrix();
    }
    public void printParameters() {
        System.out.println("Parameters:");
        System.out.println("alpha = "+alpha);
        System.out.println("antCount = "+antCount);

        System.out.println("iterations= "+ iterations );

        System.out.println("beta = "+ beta);

        System.out.println("evaporation = " +evaporation);

        System.out.println("vertices = " +vertices);

        System.out.println("threads_count = " + noThreads);
    }

    /*********START*************/
    public void startSolving() throws IOException {

        printParameters();

        if (parrarel){
            System.out.println("\nRunning parrarel pso.PSO");
        }
        else
            System.out.println("\nRunning nonparrarel pso.PSO");

        System.out.println("Be patient, it can take few minutes");

        //reset listy odwiedzonych miast
        resetAnts();
        //główna pętla
        for (int i = 0; i < iterations; i++) {
//            System.out.println("Iteration: " + (i+1)+".");
            //tu mrówki sb chodzą dopóki nie obejdą wszystkich miast
            if (parrarel) {
                for (Calc cal:calcs) {
                    cal.run();
                }
                for (Calc cal:calcs) {
                    try {
                        cal.join();
                    } catch (InterruptedException e) {
                    e.printStackTrace();
                    }
                }
            }else {
                moveAnts();
            }
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
        graphDrawer.drawResult(bestTourOrder);
    return;

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
        synchronized (graph) {
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

            }
        }
    }
    private void moveAnts() {
        //        //ruszamy wszystkie mrówki przez wszystkie krawędzie
        for (int i = currentIndex; i < vertices - 1; i++) {
            for (int j = 0; j < ants.size(); j++) {
                //zmiana wierzchołka
                ants.get(j).move(pickNextVertex(ants.get(j)), currentIndex);

            }
            //zwiększamy index bo kolejne miasto
            currentIndex++;
        }
    }

    private int pickNextVertex(Ant ant) {
        //wybieramy kolejny wierzchołek mróweczce
        random = new Random();
        calculateProbabilities(ant);
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

    public double[][] getGraph() {
        return graph;
    }

    public void initializeAnts() {
        if (parrarel) {
            int calcIterator = 0;
            Calc calc = calcs.get(calcIterator);
            for (int i = 0; i < antCount; i++) {
                Ant a = new Ant(vertices);
                ants.add(a);
                calc.addAnt(a);
                calcIterator++;
                if (calcIterator >= calcs.size())
                    calcIterator = 0;
                calc = calcs.get(calcIterator);
            }

        }else
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

    public void printMatrixInFile() {
        try {
            FileWriter fileWriter = new FileWriter("matrix.out");
            fileWriter.write(vertices+"\n");
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    fileWriter.write((graph[i][j] + "  "));
                }
                fileWriter.write("\n");
            }
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public int getVertices() {
        return vertices;

    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setEvaporation(double evaporation) {
        this.evaporation = evaporation;
    }

    public void setAntCount(int antCount) {
        this.antCount = antCount;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void setParrarel(boolean parrarel) {
        this.parrarel = parrarel;
    }

    public int[] getBestTourOrder() {
        return bestTourOrder;
    }

    public double getBestTourLength() {
        return bestTourLength;
    }

    public int getNoThreads() {
        return noThreads;
    }

    public double getEvaporation() {
        return evaporation;
    }

    public int getAntCount() {
        return antCount;
    }

    public int getIterations() {
        return iterations;
    }

    public boolean isParrarel() {
        return parrarel;
    }
}
