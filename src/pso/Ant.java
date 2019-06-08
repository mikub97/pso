package pso;

import java.util.List;

public class Ant {
    //lista wierzcholkow ktore odwiedzila mrowka
    public int[] way;
    //dł. trasy
    public int wayLength;
    //czy odwiedziła czy nie
    public boolean[] isVisited;

    public Ant(int vertices) {
        this.way = new int[vertices];
        this.wayLength = vertices;
        this.isVisited = new boolean[wayLength];
    }

    public void move(int vertex, int currentindex) {
        //tu sie dzieje chodzenie mrówki
        //System.out.println(vertex + "< vertex, currentindex >" + currentindex);
        this.isVisited[vertex] = true;
        this.way[currentindex] = vertex;
    }

    public boolean checkIfVisited(int vertex) {
        return isVisited[vertex];
    }

    public void clear() {
        for (int i = 0; i < wayLength; i++)
            isVisited[i] = false;
            this.way = new int[wayLength];
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
