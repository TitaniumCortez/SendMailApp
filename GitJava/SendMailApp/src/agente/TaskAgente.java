/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agente;


import java.util.Timer;
import java.util.TimerTask;
import sendmailapp.SendMail;
import sendmailapp.SendMailApp;


/**
 *
 * @author RCotez
 */
public class TaskAgente {


    // simulate a time consuming task

    
   

    public static void main(String args[]) {
  
        
        Timer timer = new Timer();   
	final long segundos = (1000*60);
        SendMail send = new SendMail();
        SendMailApp sendNotf = new SendMailApp();
	TimerTask timerTask = new TimerTask(){

	@Override
	public void run(){
            sendNotf.Send();
            send.Send();
            //System.out.println("Sed");
        }
	};
	
	timer.scheduleAtFixedRate(timerTask, 0, segundos);

}

}