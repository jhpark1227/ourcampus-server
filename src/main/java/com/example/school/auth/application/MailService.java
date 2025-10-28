package com.example.school.auth.application;

import com.example.school.auth.config.util.RedisUtils;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtils redisUtils;

    @Autowired
    public MailService(JavaMailSender javaMailSender, RedisUtils redisUtils) {
        this.javaMailSender = javaMailSender;
        this.redisUtils = redisUtils;
    }

    private MimeMessage createMessage(String code, String email) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        System.out.println("Email address: " + email);
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("지금 우리 학교는 인증 번호입니다.");
        message.setText("이메일 인증코드: " + code);

        message.setFrom("5959kop@naver.com"); // 보내는 사람.

        return message;
    }

    public void sendMail(String code, String email) throws Exception {

        try {
            MimeMessage mimeMessage = createMessage(code, email);
            javaMailSender.send(mimeMessage);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    public String sendCertificationMail(String email) throws GeneralException {

        try {
            String code = UUID.randomUUID().toString().substring(0, 6); //랜덤 인증번호 uuid를 이용!
            sendMail(code, email);

            redisUtils.setDataExpire(email, code, 60 * 3L); // {key,value} 3분동안 저장.

            // 추가된 로깅
            System.out.println("Data stored in Redis - Email: " + email + ", Code: " + code);

            return code;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

//    public void verifyCertificationCode(String email, String authCode) throws GeneralException {
//        try {
//            System.out.println("Before fetching code - Email: " + email);
//            System.out.println(authCode);
//            String storedCode = redisUtils.getData(email);
//            System.out.println("Fetched code - Email: " + email + ", Code: " + storedCode);
//
//            if (storedCode == null || !storedCode.equals(authCode)) {
//                throw new RuntimeException("인증번호가 일치하지 않습니다.");
//            }
//
//            // 인증 성공 시 해당 이메일의 Redis 데이터 삭제
//            redisUtils.deleteData(email);
//
//            // 추가된 로깅
//            System.out.println("Verification successful - Email: " + email);
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            throw new GeneralException(ErrorStatus.REDIS_ERROR);
//        }
//    }

    public Boolean verifyCertificationCode(String email, String authCode) {
        try {
            System.out.println("Before fetching code - Email: " + email);
            System.out.println(authCode);
            String storedCode = redisUtils.getData(email);
            System.out.println("Fetched code - Email: " + email + ", Code: " + storedCode);

            if (storedCode == null || !storedCode.equals(authCode)) {
                // If authentication fails, return false
                return false;
            }

            // If authentication is successful, delete Redis data for the email
            redisUtils.deleteData(email);

            // added logging
            System.out.println("Verification successful - Email: " + email);

            // Return true for successful verification
            return true;

        } catch (Exception exception) {
            exception.printStackTrace();
            // In case of any exception, return false
            return false;
        }
    }
}