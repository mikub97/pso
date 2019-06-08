package src;


import java.lang.reflect.Array;

public class PSO {
    public static void main(String[] args) {
        Mainframe mainframe;
        String properties=null;
        String matrix=null;
        boolean parrarel=true;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("non"))
                parrarel=false;
            if (args[i].contains("properties"))
                properties = args[i];
            if (args[i].contains("matrix"))
                matrix=args[i];
        }
        if (properties==null) {
            System.err.println("Error: there is no properties file");
            return;
        }
        if (matrix==null)
            mainframe = new Mainframe(parrarel,properties,2);
        else
            mainframe = new Mainframe(parrarel,properties,2,matrix);

        mainframe.initializeAnts();
        long time0=System.currentTimeMillis();
        mainframe.startSolving();
        System.out.println("Calculated in " +(System.currentTimeMillis()-time0) + "ms");


    }
}
//    Creating random graph with 40 verticles
//        Reading graph from : matrix.in
//        with 40 verticles
//        Parameters:
//        alpha = 1.0
//        antCount = 1200
//        iterations= 1000
//        beta = 4.0
//        evaporation = 0.5
//        vertices = 40
//        threads_count = 2
//
//        Running parrarel PSO
//        Be patient, it can take few minutes
//        Best tour length: 1252.0
//        Best tour order: [14, 23, 38, 27, 28, 9, 3, 11, 36, 16, 33, 19, 21, 37, 20, 34, 7, 31, 4, 18, 10, 25, 5, 26, 39, 6, 22, 13, 15, 2, 17, 30, 8, 24, 1, 12, 32, 35, 29, 0]
//        Calculated in 101434ms
//                      102375ms