import com.jmatio.io.MatFileReader;
import com.jmatio.types.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Mira
 * Date: 10.2.14
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class MatlabImport {
    private Object[] pole = new Object[8];
    private double timestepfinal;
    private String datefinal;
    private int numLames;
    private double gantry_idfinal;
    private double[][] lanesfinal;
    private double[][] losfinal;
    private double[][] countcars;
    private double[][] speed;
    private double[][] lanesfinal1;

    public MatlabImport(){
    }

    public MatlabImport(String Gate,String day, String month, String year){
     try {

                MatFileReader matfilereader = new MatFileReader(Gate+"/sokp-"+Gate+"-"+year+month+day+".mat");
             //   MLArray aaa = matfilereader.getMLArray("data");
                Map<String, MLArray> data = matfilereader.getContent();
                Set<String> vars = data.keySet();
                for (Iterator<String> var = vars.iterator(); var.hasNext();) {
                    String varName = var.next();

                    MLArray varData = data.get(varName);
                    MLStructure Matlabdata = (MLStructure)varData;
                    Collection<MLArray> arraydata = Matlabdata.getAllFields();
                    Iterator arr = arraydata.iterator();

                    int i=0;
                    int j=0;
                    int k=0;
                    while (arr.hasNext()){
                        pole[i] = arr.next();
                        i++;
                    }
                    MLChar date;
                    MLDouble timestep;
                    MLDouble gantry_id;
                    MLArray lanes;
                    MLArray los;
                    date = (MLChar)pole[0];
                    timestep = (MLDouble)pole[1];
                    gantry_id = (MLDouble)pole[2];
                    lanes = (MLArray)pole[3];
                    los = (MLArray)pole[4];
                    MLCell countcell = (MLCell)pole[5];
                    MLCell countcell2 = (MLCell)pole[7];
                    Collection<MLArray> cells = countcell.cells();
                    Iterator celliterator = cells.iterator();
                    Collection<MLArray> cells2 = countcell2.cells();
                    Iterator celliterator2 = cells2.iterator();

                    timestepfinal = timestep.get(0);
                    datefinal = date.getString(0);
                    gantry_idfinal = gantry_id.get(0);
                    lanesfinal = ((MLDouble)lanes).getArray();
                    losfinal = ((MLDouble)los).getArray();
                    lanesfinal1 = lanesfinal;
                    numLames = Array.getLength(lanesfinal[0]);
             //       System.out.println("Datum: "+datefinal);
             //       System.out.println("Timestep: "+timestepfinal);
             //       System.out.println("Staniceni "+gantry_idfinal);
             //       System.out.println("Lanes: "+ Arrays.deepToString(lanesfinal));
             //       System.out.println("LOS: "+Arrays.deepToString(losfinal));


                    Object[] countcarso = new Object[9];
                    while (celliterator.hasNext()){
                        countcarso[j] = celliterator.next();
                        j++;
                }
                countcars = ((MLDouble)countcarso[2]).getArray();

                Object[] speedit = new Object[9];

               while (celliterator2.hasNext()){
                    speedit[k] = celliterator2.next();
                    k++;
                }
                 speed = ((MLDouble)speedit[2]).getArray();

      //          System.out.println("pocet aut"+Arrays.deepToString(countcars));
      //          System.out.println("prumerna rychlost"+Arrays.deepToString(speed));
              }
        }
        catch (Exception ex) {
                System.out.println(ex);
        }
     }
 public int getNumLanes(){
     return numLames;
 }

 public double getSpeed(int time, int lane){

     return speed[time][lane];
 }

 public int getNumCars(int time, int lane){
  return (int)countcars[time][lane];
 }

 public int getAllCarsInDay(){
  int allcars=0;
    for (int i=0;i<2;i++){
        for (int j=0;j<1440;j++) {
           allcars+= (int) countcars[j][i];

        }

    }
  return allcars;
 }

 public String getDate(){
  return datefinal;
 }

 public double getGate(){
  return gantry_idfinal;
 }



}
