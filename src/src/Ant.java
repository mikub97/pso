package src;

import java.util.List;

public class Ant {
    public int[] way;
    public int wayLength;
    public boolean[] isVisited;

    public Ant(int vertices) {
        this.way = new int[vertices];
        this.wayLength = vertices;
        this.isVisited = new boolean[wayLength];
    }

    public void move(int vertex, int currentindex) {
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
