package pso;

import java.util.List;
import java.util.Random;

public class Ant {
    boolean first;
    //lista wierzcholkow ktore odwiedzila mrowka
    public int[] way;
    //dł. trasy
    public int wayLength;
    //czy odwiedziła czy nie
    public boolean[] isVisited;

    public Ant(int vertices) {
        Random r = new Random();
        this.first = true;
        this.way = new int[vertices];
        this.wayLength = vertices;
        this.isVisited = new boolean[wayLength];
        way[0] = r.nextInt(wayLength);
        isVisited[way[0]]=true;
    }

    public void move(int vertex, int currentindex) {
        //tu sie dzieje chodzenie mrówki
        //System.out.println(vertex + "< vertex, currentindex >" + currentindex);
        this.isVisited[vertex] = true;
        this.way[currentindex+1] = vertex;
    }

    public boolean checkIfVisited(int vertex) {
        return isVisited[vertex];
    }

    public void clear() {
        Random r = new Random();

        first = true;
        for (int i = 0; i < wayLength; i++)
            isVisited[i] = false;
            this.way = new int[wayLength];
        way[0] = r.nextInt(wayLength);
        isVisited[way[0]]=true;
    }

    public double wayLength(double[][] graph) {
        //liczenie długości trasy którą pokanała sobie mrówka
        double length;
        if (way[wayLength - 1] > way[0]) {
            length = graph[way[0]][way[wayLength - 1]];
        } else {
            length = graph[way[wayLength - 1]][way[0]];
        }
        for (int i = 0; i < wayLength - 1; i++) {
            if (way[i] > way[i + 1]) {
                length += graph[way[i + 1]][way[i]];

            } else {
                length += graph[way[i]][way[i + 1]];

            }
        }
        return length;
    }
}
