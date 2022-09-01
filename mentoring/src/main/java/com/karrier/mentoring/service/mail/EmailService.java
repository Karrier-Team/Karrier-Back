package com.karrier.mentoring.service.mail;

import java.util.Random;

// 추후 이메일 서비스의 확정성을 고려
public interface EmailService {
    // 메세지 전송
    public void sendSimpleMessage(String to);

    // 인증번호 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 6; i++) {
            int index = rnd.nextInt(3);
            switch (index) {
                case 0:
                    key.append(((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append(((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }
}
