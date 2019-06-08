package pso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Calc extends Thread {
    List<Ant> ants;
    Mainframe mainframe;
    int currentIndex;
    double[] probabilities;


    public Calc(Mainframe mainframe) {
        this.ants = new ArrayList<Ant>();
        this.mainframe = mainframe;
        currentIndex=0;
        probabilities = new double[mainframe.getVertices()];


    }

    public void moveAnts() {
        //ruszamy wszystkie mrówki przez wszystkie krawędzie
        for (int i = currentIndex; i < mainframe.getVertices() - 1; i++) {
            for (int j = 0; j < ants.size(); j++) {
                //zmiana wierzchołka
                ants.get(j).move(pickNextVertex(ants.get(j)), currentIndex);

            }
            //zwiększamy index bo kolejne miasto
            currentIndex++;
        }
    }

    public void addAnt(Ant ant) {
        this.ants.add(ant);
    }
    public void updatePheromone(){
        for (Ant a : ants) {
            System.out.println(Arrays.toString(a.way));
            //droga od pierwszego do ostatniego
            double contribution = a.wayLength(mainframe.getGraph());
            for (int i = 0; i < mainframe.getVertices()- 1; i++) {

                if (a.way[i] > a.way[i + 1]) {
                    mainframe.getGraph()[a.way[i]][a.way[i + 1]] += contribution;
                } else {
                    mainframe.getGraph()[a.way[i + 1]][a.way[i]] += contribution;
                }
            }
            //droga z ostatniego do pierwszego danej mrowki
            if (a.way[0] > a.way[mainframe.getVertices() - 1])
                mainframe.getGraph()[a.way[0]][a.way[mainframe.getVertices() - 1]] += contribution;
            else
                mainframe.getGraph()[a.way[mainframe.getVertices() - 1]][a.way[0]] += contribution;

            System.out.println("contriburion" + contribution);
        }
    }
    private int pickNextVertex(Ant ant) {
        //wybieramy kolejny wierzchołek mróweczce
        Random random = new Random();

        calculateProbabilities(ant);
        //System.out.println(Arrays.toString(probabilities));
        //System.out.println(Arrays.toString(ant.way));
        //losujemy wierzchołek:
        double r = random.nextDouble();
        double total = 0;
        for (int i = 0; i < mainframe.getVertices(); i++) {
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
        for (l = 0; l < mainframe.getVertices(); l++) {
            if (!ant.checkIfVisited(l) && l!=i){
                if(i>l)
                    pheromone += Math.pow(mainframe.getGraph()[i][l], mainframe.getAlpha()) * Math.pow(1 / mainframe.getGraph()[l][i], mainframe.getBeta());
                else
                    pheromone += Math.pow(mainframe.getGraph()[l][i], mainframe.getAlpha()) * Math.pow(1 / mainframe.getGraph()[i][l], mainframe.getBeta());
            }

        }

        probabilities[i] = 0.0;
        //chodzenie indeksami po tej macierzy - to była katorga zakodzić to
        //prawdopodobieństwo dla każdego wierzchołka dla mróweczki w danej chwili
        for (j = 0; j < mainframe.getVertices(); j++) {
            if (ant.checkIfVisited(j) ) {
                probabilities[j] = 0.0;
            } else if(i!=j){
                if(i>j) {
                    double numerator = Math.pow(mainframe.getGraph()[i][j], mainframe.getAlpha()) * Math.pow(1.0 / mainframe.getGraph()[j][i], mainframe.getBeta());
                    probabilities[j] = numerator / pheromone;
                }else{
                    double numerator = Math.pow(mainframe.getGraph()[j][i], mainframe.getAlpha()) * Math.pow(1.0 / mainframe.getGraph()[i][j], mainframe.getBeta());
                    probabilities[j] = numerator / pheromone;
                }
            }
        }


    }
    @Override
    public void run() {
        moveAnts();
        currentIndex=0;

    }
}
