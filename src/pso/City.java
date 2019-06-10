package pso;

import java.util.Random;

public class City {
    int x;
    int y;
    int cityNr;
    public City(int nr) {
        Random r = new Random();
        this.x = r.nextInt(1000);
        this.y = r.nextInt(1000);
        this.cityNr = nr;
    }
    public double getDistance(City c){
        return Math.sqrt((c.y-y)*(c.y-y)+(c.x-x)*(c.x-x));
    }
}
