package com.likelion.RePlay.global.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String subject, String content, List<MultipartFile> attachments)
            throws MessagingException, IOException {
        // MimeMessage의 경우 멀티파트 데이터를 처리 할 수 있고
        // SimpleMailMessage는 단순한 텍스트 데이터만 전송이 가능
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(subject);
        helper.setText(content);
        helper.setTo("lucy01112@naver.com");

        // 첨부파일 추가
        for (MultipartFile file : attachments) {
            helper.addAttachment(file.getOriginalFilename(), file);
        }

        javaMailSender.send(message);
    }
}
