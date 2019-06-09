package pso;

import com.opencsv.CSVWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MainframeTest {

    Mainframe mainframe;

    @Test
    public void testParameters() {
        CSVWriter writer =null;
        File file = new File("out.csv");
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"verticles", "alpha","beta","antCount","iterations","evaporation","threads","parrarel","best","time","tour"};
            writer.writeNext(header);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //MEEEEGAAA DŁUGO BĘDZIE TO ROBIĆ
        double[] alphas={0.4,0.5,0.7,0.9,1.2,1.5,2};
        double[] betas={0.4,0.5,0.6,1,1.2,1.4,1.6,2};
        double[] evaporations={0.4,0.5,0.6};
        int [] ants={10};
        int [] its={50};
        int [] threads_counts={2};


        for (double alpha : alphas) {
            for (double beta : betas) {
                for (double evaporation : evaporations) {
                    for (int ant : ants) {
                        for (int it : its) {
                            for (int threads_count : threads_counts) {
                                Map<String,Object> properties;
                                properties = new HashMap<>();
                                properties.put("vertices",40);
                                properties.put("alpha",alpha);
                                properties.put("beta",beta);
                                properties.put("ants",ant);
                                properties.put("it",it);
                                properties.put("evaporation",evaporation);
                                properties.put("threads_count",threads_count);
                                iteration(properties,writer,true);
                                iteration(properties,writer,false);
                            }
                        }
                    }
                }
            }
        }


        try {
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

//        mainframe.setAlpha(alpha);
//        mainframe.setBeta(beta);
//        mainframe.setAntCount(ants_count);
//        mainframe.setEvaporation(evaporation);
//        mainframe.setIterations(iteration);
//        mainframe.initializeAnts();
    }
    void iteration(Map prop,CSVWriter writer,boolean parrarel) {

        mainframe = new Mainframe(parrarel,prop,0,"matrix.in");
        mainframe.initializeAnts();
        long time0=System.currentTimeMillis();
        mainframe.startSolving();
        String tour="";
        for (int i = 0; i < mainframe.getBestTourOrder().length; i++) {
            tour+=mainframe.getBestTourOrder()[i];
            tour+=" ";
        }
        try {
            // add data to csv
            String[] data1 = {String.valueOf(mainframe.getVertices()), String.valueOf(mainframe.getAlpha()), String.valueOf(mainframe.getBeta()),
                    String.valueOf(mainframe.getAntCount()), String.valueOf(mainframe.getIterations()), String.valueOf(mainframe.getEvaporation()),
                    String.valueOf(mainframe.getNoThreads()), String.valueOf(mainframe.isParrarel()), String.valueOf(mainframe.getBestTourLength()),
                    String.valueOf((System.currentTimeMillis() - time0)), tour};
            writer.writeNext(data1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}