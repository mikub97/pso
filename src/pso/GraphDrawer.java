package pso;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphDrawer {
    List<City> cities;

    public GraphDrawer() {

    }

    public void generateCoords(int vertices){
        cities = new ArrayList<>();
        for(int i = 0 ; i < vertices; i++){
            cities.add(new City(i));
        }
    }
    public double getDistance(int i, int j){
        return cities.get(j).getDistance(cities.get(i));
    }

    public void drawResult(int[] bestTourOrder) throws IOException {
        final BufferedImage image = new BufferedImage ( 1000, 1000, BufferedImage.TYPE_INT_ARGB );
        final Graphics2D graphics = image.createGraphics ();
        graphics.setPaint ( Color.WHITE );
        graphics.fillRect ( 0,0,1000,1000 );
        graphics.setPaint(Color.RED);

        int i;
        for( i = 0 ; i< cities.size();i++){
            graphics.fillOval(cities.get(i).x - 7,cities.get(i).y - 7,15,15);
        }
        graphics.setPaint(Color.BLACK);
        for(i = 0; i < bestTourOrder.length-1; i++){
            graphics.drawLine(cities.get(bestTourOrder[i]).x ,cities.get(bestTourOrder[i]).y,
                    cities.get(bestTourOrder[i+1]).x,cities.get(bestTourOrder[i+1]).y);

        }
        graphics.drawLine(cities.get(bestTourOrder[0]).x,cities.get(bestTourOrder[0]).y,
                cities.get(bestTourOrder[bestTourOrder.length-1]).x,cities.get(bestTourOrder[bestTourOrder.length-1]).y);

        ImageIO.write ( image, "png", new File ( "image.png" ) );
    }
}
