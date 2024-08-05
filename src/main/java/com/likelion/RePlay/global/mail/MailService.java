package com.likelion.RePlay.global.mail;

import com.likelion.RePlay.domain.info.web.dto.InfoSubmitResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
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

    public void sendMail(InfoSubmitResponseDto infoSubmitResponseDto, List<MultipartFile> attachments)
            throws MessagingException, IOException {
        // MimeMessage의 경우 멀티파트 데이터를 처리 할 수 있고
        // SimpleMailMessage는 단순한 텍스트 데이터만 전송이 가능
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subject = "[리플레이] "+infoSubmitResponseDto.getWriter()+"님의 투고 신청 : "+infoSubmitResponseDto.getTitle();
        String content = "작성자: " + infoSubmitResponseDto.getWriter() + "\n\n" + infoSubmitResponseDto.getContent();
        helper.setSubject(subject);
        helper.setText(content);
        helper.setTo("lucy011121@naver.com");

        // 첨부파일 추가
        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if (!file.isEmpty()) {
                    InputStreamSource source = new ByteArrayResource(file.getBytes());
                    helper.addAttachment(file.getOriginalFilename(), source);
                }
            }
        }


        javaMailSender.send(message);
    }
}
