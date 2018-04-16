/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author RCotez
 */
public class controllerEmailBeans {
    

    private int Sent;
    private String To;
    private String Name;
    private String Status;
    private String IDSLA;
    private String IDTicket;

    public String getIDTicket() {
        return IDTicket;
    }

    public void setIDTicket(String IDTicket) {
        this.IDTicket = IDTicket;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getIDSLA() {
        return IDSLA;
    }

    public void setIDSLA(String IDSLA) {
        this.IDSLA = IDSLA;
    }
    

 

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

  
    public int getSent() {
        return Sent;
    }

    public void setSent(int Sent) {
        this.Sent = Sent;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }
    
    
    
    
}
