/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sendmailapp;

import filelog.Filelog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import models.controllerEmailBeans;
import models.emailOrigemBeans;
import sqlServer.SqlServer;

/**
 *
 * @author RCotez
 */

/*Com IMAGEM*/
public class SendMail {

    public Connection connection;
    private String usr;
    private String pwd;

    public void SendMail( final String usr,final String pwd) {
        this.usr = usr;
        this.pwd = pwd;
    }
    
    
    
    public void status(String Name) {
    }

    public void Send() {
        boolean bd = false;
        
        final String cancelado = "Cancelado";
        final String aberto = "Aberto";
        final String finalizado = "Concluido";
        final String andamento = "em Andamento";
        final String novo = "Novo Ticket";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        File template = new File("template//templateEmail.html");
        SqlServer sql = new SqlServer();
        Date date = new Date();

        
        
        
        
        if (sql.connect() == true) {
             // TODO code application logic here
            Properties props = new Properties();
            /**
             * Parâmetros de conexão com servidor Outlook
             */
            List<emailOrigemBeans> auth = sql.getAuthEmailSend();
            for(emailOrigemBeans att : auth){       
            
           SendMail(att.getEmailOrigem(),att.getPassword());
                
                        }
            try{
            props.put("mail.smtp.host", "outlook.office365.com");
            props.put("mail.smtp.socketFactory.port", "587");
            props.put("mail.smtp.starttls.enable", "true");
            /*Deve ser adicionado essa autenticação*/
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(usr, pwd);
                            // return new PasswordAuthentication("rcortez@imnet.com", "Titanium_Zinho");
                            
                            
                        }
                    });
           
            /**
             * Ativa Debug para sessão
             */
            session.setDebug(false);
            try {

                // Create a default MimeMessage object.
                Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(usr));

                List<controllerEmailBeans> send = sql.getControllerMail();
                Log("Quantidade de Email para envio : " + send.size());
                
                for (controllerEmailBeans to : send) {
                    Log("Email à ser enviado : " + to.getTo());

                    File pt = new File(template.getParent());

                    if (!pt.exists()) {
                        pt.mkdir();
                    }
                    /*Check template existe*/

                    if (template.exists()) {

                        // Set To: header field of the header.
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(to.getTo()));//To.getto = email de destino.

                        // Set Subject: header field
                        message.setSubject("Prompt Manager - Tim");

                        // This mail has 2 part, the BODY and the embedded image
                        MimeMultipart multipart = new MimeMultipart("related");

                        // first part (the html)
                        BodyPart messageBodyPart = new MimeBodyPart();
                        String conteudo = "";
                        try {
                            FileReader arq = new FileReader(template);
                            BufferedReader html = new BufferedReader(arq);

                            String linha = html.readLine();
                            conteudo += linha;

                            while (linha != null) {
                                linha = linha.trim();

                                conteudo += linha;
                                linha = html.readLine();
                            }

                        } catch (IOException e) {
                            Log(e.getMessage());
                        }

                        if ("C".equals(to.getStatus())) {
                            conteudo = conteudo.replace("<java:1>", finalizado);
                            conteudo = conteudo.replace("<java:2>", to.getName());
                            conteudo = conteudo.replace("<java:3>", "");
                            conteudo = conteudo.replace("<java:3>", dateFormat.format(date));

                        }

                        if ("O".equals(to.getStatus())) {
                            conteudo = conteudo.replace("<java:1>", aberto);
                            conteudo = conteudo.replace("<java:2>", to.getName());
                            conteudo = conteudo.replace("<java:3>", "SLA: " + to.getIDSLA());
                            conteudo = conteudo.replace("<java:4>", dateFormat.format(date));
                        }

                        if ("X".equals(to.getStatus())) {
                            conteudo = conteudo.replace("<java:1>", cancelado);
                            conteudo = conteudo.replace("<java:2>", to.getName());
                            conteudo = conteudo.replace("<java:3>", "");
                            conteudo = conteudo.replace("<java:4>", dateFormat.format(date));
                        }
                        if ("P".equals(to.getStatus())) {
                            conteudo = conteudo.replace("<java:1>", andamento);
                            conteudo = conteudo.replace("<java:2>", to.getName());
                            conteudo = conteudo.replace("<java:3>", "SLA: " + to.getIDSLA());
                            conteudo = conteudo.replace("<java:4>", dateFormat.format(date));
                        }
                          if ("N".equals(to.getStatus())) {
                            conteudo = conteudo.replace("<java:1>", novo);
                            conteudo = conteudo.replace("<java:2>", to.getName());
                            conteudo = conteudo.replace("<java:3>", "SLA: " + to.getIDSLA());
                            conteudo = conteudo.replace("<java:4>", dateFormat.format(date));
                        }
                        

                        String htmlText = conteudo;
                        messageBodyPart.setContent(htmlText, "text/html");

                        // add it
                        multipart.addBodyPart(messageBodyPart);

                        // second part (the image)
                        messageBodyPart = new MimeBodyPart();
                        DataSource fds = new FileDataSource(
                                "template\\top.gif");
                        messageBodyPart.setDataHandler(new DataHandler(fds));
                        messageBodyPart.setHeader("Content-ID","<image>");

                        // add image to the multipart
                        multipart.addBodyPart(messageBodyPart);

                        // put everything together
                        message.setContent(multipart);
                        // Send message
                        Transport.send(message);
                        
                        Log("Sent message successfully....");
                        sql.updateSucesso(to.getIDTicket(), to.getTo());
                    } else {
                        Log("Template NÂO LOCALIZADO");
                        sql.updateFalha(to.getIDSLA(), to.getTo());
                    }
                }

            } catch (MessagingException e) {
                Log(e.getMessage());
                throw new RuntimeException(e);
            }
            sql.disconnect();

    }catch(RuntimeException e){
         Log(e.getMessage());
    }
        }
    }

    public static void Log(String log) {
        Filelog filelog = new Filelog();
        try {
            filelog.Filelog(log);
        } catch (IOException e) {
        }
    }
}
