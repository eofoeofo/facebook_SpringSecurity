package com.koreait.facebook;

import com.koreait.facebook.common.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacebookApplicationTests {

    @Autowired
    private EmailService email;

    @Test
    void sendEmail() {
        String to = "pirbak@daum.net";
        String subject = "성헌이는 청소를 합니다.";
        String txt = "관광을 가서도 청소를 합니다. <a href=\"http://localhost:8090/user/login\">로그인으로 이동</a>";
        email.sendMimeMessage(to, subject, txt);
    }

}
