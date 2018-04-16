/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlServer;

import filelog.Filelog;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.controllerEmailBeans;
import models.emailOrigemBeans;

/**
 *
 * @author RCotez
 */
public class SqlServer {

    //private final String host = "192.168.1.119";
    //private final String host = "192.168.1.119";
    private final String host = "ipHost";
    //private final String port = "1433";
    private final String port = "1414";
    private final String user = "user";
    private final String pass = "senhaUser";
    private final String database = "IMPromptManager";
    final String DataMails = "select * from smtp_mail";
    final String Notificacao = "select * from smtp_NotifEmail";
    final String aut = "Select * from auth_smtp"; 
    final String EmailOrigem = "selec *from EmailOrigem";

    public Connection c;

    /*
      Construtor da classe
      
     host Host em que se deseja conectar
     database Nome do database em que se deseja conectar
     user Nome do usuário
     pass Senha do usuário
     */
    public SqlServer() {
    }

    /**
     * Método que estabelece a conexão com o banco de dados
     *
     * @return True se conseguir conectar, falso em caso contrário.
     */
    public boolean connect() {
        boolean isConnected = false;

        String url;
       /* String portNumber = this.port;
        String userName = this.user;
        String passName = this.pass;*/
        url = "jdbc:sqlserver://"+host+":"+port+";databaseName="+database;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            this.c = DriverManager.getConnection(url, user, pass);
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Log(e.getMessage());
            isConnected = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Log(e.getMessage());
            isConnected = false;
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Log(e.getMessage());
            isConnected = false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Log(e.getMessage());
            isConnected = false;
        }

        Log(url +"  Conected :" + isConnected);
        return isConnected;
    }

    /**
     * Método que estabelece a desconexão com o banco de dados
     *
     * @return True se conseguir desconectar, falso em caso contrário.
     */
    public boolean disconnect() {
        boolean isConnected = false;

        String url;

         url = "jdbc:sqlserver://"+host+":"+port+";databaseName="+database;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            this.c = DriverManager.getConnection(url, user, pass);
            this.c.close();
            isConnected = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            isConnected = false;
        }

        return isConnected;
    }

    /**
     * Esse método executa a query dada, e retorna um ResultSet Talvez fosse
     * melhor idéia fazer esse método lançar uma exception a faze-lo retornar
     * null como eu fiz, porém isso é apenas um exemplo para demonstrar a
     * funcionalidade do comando execute
     *
     * @param query String contendo a query que se deseja executar
     * @return ResultSet em caso de estar tudo Ok, null em caso de erro.
     */
    public ResultSet executar(String query) {
        Statement st;
        ResultSet rs;

        try {
            st = this.c.createStatement();
            rs = st.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            Log(e.getMessage());
        }

        return null;
    }

    /**
     * Executa uma query como update, delete ou insert. Retorna o número de
     * registros afetados quando falamos de um update ou delete ou retorna 1
     * quando o insert é bem sucedido. Em outros casos retorna -1
     *
     * @param query A query que se deseja executar
     * @return 0 para um insert bem sucedido. -1 para erro
     */
    public int inserir(String query) {
        Statement st;
        int result = -1;

        try {
            st = this.c.createStatement();
            result = st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            Log(e.getMessage());
        }

        return result;
    }

    public int updateSucesso(String idticket , String to ) {
        String sql = "update PM_Email set Sent=1 where IDTicket= ? and [To]= ? ";
       // Statement st;
        PreparedStatement stmt;
        int result = -1;
        try {
            stmt = this.c.prepareStatement(sql);
            stmt.setString(1, idticket);
            stmt.setString(2, to);
            //st = this.c.createStatement();
                      
            result =  stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Log(e.getMessage());

        }

        return result;
    }
    
    public int updateNewEmailSucesso(String idticket) {
        String sql = "update PM_Ticket set EmailSend=1 where IDTicket =?  ";
       // Statement st;
        PreparedStatement stmt;
        int result = -1;
        try {
            stmt = this.c.prepareStatement(sql);
            stmt.setString(1, idticket);           
            //st = this.c.createStatement();
                      
            result =  stmt.executeUpdate();
        } catch (SQLException e) {
            Log(e.getMessage());
            e.printStackTrace();

        }

        return result;
    }
      public int updateFalha(String idticket , String to ) {
        String sql = "update PM_Email set Error=1 where IDTicket= ? and [To]= ? ";
       // Statement st;
        PreparedStatement stmt;
        int result = -1;
        try {
            stmt = this.c.prepareStatement(sql);
            stmt.setString(1, idticket);
            stmt.setString(2, to);
            //st = this.c.createStatement();
                      
            result =  stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Log(e.getMessage());

        }

        return result;
    }

    /*Obtem a tabela de para controle de send email */
    public java.util.List<controllerEmailBeans> getControllerMail() {

        java.util.List<controllerEmailBeans> tb = new ArrayList<controllerEmailBeans>();

        try {
            PreparedStatement stm = this.c.prepareStatement(DataMails);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                controllerEmailBeans data = new controllerEmailBeans();
               
                data.setIDTicket(rs.getString("IDTicket"));
                data.setName(rs.getString("Name"));
                data.setStatus(rs.getString("Status"));
                data.setIDSLA(rs.getString("IDSLA"));           
                data.setSent(rs.getInt("Sent"));
                data.setTo(rs.getString("To"));
                tb.add(data);
            }
            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Log(e.getMessage());
        }

        return tb;

    }
    
        public java.util.List<controllerEmailBeans> getNewMail() {

        java.util.List<controllerEmailBeans> tb = new ArrayList<controllerEmailBeans>();

        try {
            PreparedStatement stm = this.c.prepareStatement(Notificacao);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                controllerEmailBeans data = new controllerEmailBeans();
                
                data.setIDTicket(rs.getString("IDTicket"));  
                data.setName(rs.getString("Name"));
                data.setStatus(rs.getString("Status"));             
                data.setTo(rs.getString("NotificationEmail"));
                tb.add(data);
            }
            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Log(e.getMessage());
        }

        return tb;

    }

    /*Obtem a tabela de para controle de send email */
    public java.util.List<emailOrigemBeans> getAuthEmailSend() {

        java.util.List<emailOrigemBeans> auth = new ArrayList<emailOrigemBeans>();

        try {
            PreparedStatement stm = this.c.prepareStatement(aut);
            ResultSet rs = stm.executeQuery();
            int i=0;
while (rs.next() && i<=1) {
            emailOrigemBeans credenciais = new emailOrigemBeans();
            credenciais.setEmailOrigem(rs.getString("EmailSupport"));
            credenciais.setPassword(rs.getString("Password"));

            auth.add(credenciais);
            i++;
}
            rs.close();
            stm.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Log(e.getMessage());
        }

        return auth;

    }

 
    public static void Log(String log) {
        Filelog filelog = new Filelog();
        try {
            filelog.Filelog(log);
        } catch (IOException e) {
        }
    }

}
