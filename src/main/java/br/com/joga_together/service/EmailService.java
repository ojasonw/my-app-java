package br.com.joga_together.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendConfirmCodeRegistrer(String to, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            //TODO AJUSTAR ISSO PARA VAR DE AMBIENTE
            message.setFrom("jogatogether@gmail.com");
            message.setTo(to);
            message.setSubject("Confirmação de Registro - Joga Together");
            message.setText("Olá, \n\n" +
                    "Obrigado por se registrar no Joga Together! Para confirmar seu registro, utilize o código de confirmação abaixo:\n\n" +
                    "Código de Confirmação: " + code + "\n\n" +
                    "lembrando que o código expira em 15 minutos.");
            emailSender.send(message);
        } catch (Exception e) {
            log.error("ERROR TO SEND EMAIL to {}: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendEmailRegisterConfirmed(String to, String nameUser) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("jogatogether@gmail.com");
            message.setTo(to);
            message.setSubject("Registro Confirmado - Joga Together");
            message.setText("Cadastro Confirmado na Joga Together, Bem-vindo " + nameUser);
            emailSender.send(message);
        } catch (Exception e) {
            log.error("ERROR TO SEND CONFIRMATION EMAIL to {}: {}", to, e.getMessage());
        }
    }

}
