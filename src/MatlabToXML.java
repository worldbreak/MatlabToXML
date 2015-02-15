/**
 * Created by MIREK on 24.1.2015.
 */

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MatlabToXML {
    String km = "0201";
    String day = "01";
    String month ="10";
    String year ="2012";
    int period = 60;
    MatlabImport matlabImport = new MatlabImport(km,day,month,year);

    public static int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }


    public Document createLoopDocument(int lane) {
        Document document = DocumentHelper.createDocument();
        Element detector = document.addElement("detector");
        for (int i=0;i<1440;i++){
            double time = period*i;
            Element interval = detector.addElement( "interval" );
            interval.addAttribute("begin",Double.toString(time));
            interval.addAttribute("end",Double.toString(time+period));
            interval.addAttribute("nVehContrib",Integer.toString(matlabImport.getNumCars(i,lane)));
            interval.addAttribute("speed",Double.toString(matlabImport.getSpeed(i,lane)/3.6));

        }
        return document;
    }






   public MatlabToXML(){

        try {
            for (int lane=0;lane<matlabImport.getNumLanes();lane++) {
                FileWriter outLoop = new FileWriter("sokp-" + km + "-" + year + month + day + "_" + lane + ".xml");
                Document loopDocument = createLoopDocument(lane);
                OutputFormat format = OutputFormat.createPrettyPrint();
                XMLWriter writerLoop = new XMLWriter(outLoop, format);
                writerLoop.write(loopDocument);
                writerLoop.close();
            }

        }
        catch (Exception e){
            System.out.println(e);
        }
    System.out.println(matlabImport.getAllCarsInDay());
   }


   public static void main(String[] args) {
    new MatlabToXML();
   }

}
