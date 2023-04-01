package fastcampus.workschedulemanagementbackend.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

//랜덤 값 만들기
@Component
public class StringUtil {

    public static String randomStr(int length) {
        Random random = new Random();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(3);
            switch(choice) {
                case 0:
                    str.append((char)(random.nextInt(25)+97));
                    break;
                case 1:
                    str.append((char)(random.nextInt(25) +65));
                    break;
                case 2:
                    str.append((char)(random.nextInt(10) +48));
                    break;
                default:
                    break;
            }
        }
        return str.toString();
    }
}
