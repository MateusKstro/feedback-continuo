//package com.feedbackcontinuos.service;
//
//import com.feedbackcontinuos.dto.UserFullDTO;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class EmailService {
//
//    private final freemarker.template.Configuration fmConfiguration;
//    @Value("${spring.mail.username}")
//    private String from;
//    private final JavaMailSender emailSender;
//
//    public void sendEmail(UserFullDTO userFullDTO) {
//        MimeMessage mimeMessage = emailSender.createMimeMessage();
//        try {
//
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//
//            mimeMessageHelper.setFrom(from);
//            mimeMessageHelper.setTo(userFullDTO.getEmail());
//            mimeMessageHelper.setSubject("Recuperar senha de acesso");
//            mimeMessageHelper.setText(getContentFromTemplate(userFullDTO), true);
//
//            emailSender.send(mimeMessageHelper.getMimeMessage());
//        } catch (MessagingException | IOException | TemplateException e) {
//            e.printStackTrace();
//        }
//
//    }
//    public String getContentFromTemplate(UserFullDTO userFullDTO) throws IOException, TemplateException {
//        Map<String, Object> dados = new HashMap<>();
//        dados.put("nome", userFullDTO.getName());
//        dados.put("email", from);
//
//        Template template = fmConfiguration.getTemplate("email-template.delete.ftl");
//        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
//        return html;
//    }
//}
//
