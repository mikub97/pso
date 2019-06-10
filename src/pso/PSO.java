package pso;

import java.io.IOException;

public class PSO {
    public static void main(String[] args) throws IOException {
        Mainframe mainframe;
        String properties = null;
        String matrix = null;
        boolean parrarel = true;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("non"))
                parrarel = false;
            if (args[i].contains("properties"))
                properties = args[i];
            if (args[i].equals("matrix.in"))
                matrix = args[i];
        }
        if (properties == null) {
            System.err.println("Error: there is no properties file");
            return;
        }
        if (matrix == null)
            mainframe = new Mainframe(parrarel, properties, 2);
        else
            mainframe = new Mainframe(parrarel, properties, 2, matrix);

        mainframe.initializeAnts();
        long time0 = System.currentTimeMillis();
        mainframe.startSolving();
        System.out.println("Calculated in " + (System.currentTimeMillis() - time0) + "ms");


    }
}
