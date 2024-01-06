package com.rm.connecteducacionalpro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public String enviarEmailTexto(String destinatario, String titulo,String mensagem){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(remetente);
            System.out.println("-------->"+ remetente);
            simpleMailMessage.setTo(destinatario);
            System.out.println("-------->"+ destinatario);
            simpleMailMessage.setSubject(titulo);
            System.out.println("-------->"+ titulo);
            simpleMailMessage.setText(mensagem);
            System.out.println("-------->"+ mensagem);
            javaMailSender.send(simpleMailMessage);
            System.out.println("-------->"+ simpleMailMessage);
            return "Email enviado";
        }catch (Exception e){
            return  "Erro ao enviar o email";
        }
    }


}
