/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filelog;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 * @author RCotez
 */
public class Filelog {

    final String patch = "Log//Log.txt";
    FileWriter newfile;
    File log = new File(patch);
    String parent;
    public Filelog(){}
    public void Filelog(String logWR) throws IOException {
     
        /*Check arquivo Diretorio*/        
       File pt = new File(log.getParent());
       
       if(!pt.exists()){ 
                 pt.mkdir();
        }
          /*Check arquivo existe*/
        if (!log.exists()) {                                   
            try {
                newfile = new FileWriter(patch);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*Escrever*/
        FileWriter fw = new FileWriter(patch, true);
        PrintWriter gravarArq = new PrintWriter(fw);
        gravarArq.println(new Date() + ": " + logWR);       
        gravarArq.close();
        
    }
/*
    public Filelog(String log) throws IOException {
        FileWriter fw = new FileWriter(patch, true);
        PrintWriter gravarArq = new PrintWriter(fw);
        gravarArq.println(new Date() + ": " + log);       
        gravarArq.close();

    }*/

 }
