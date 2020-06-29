package auth.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PasswordUtil {

    private PasswordUtil() {
    }

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";

    private static final Random random = new Random();

    public static String generatePassword() {
        List<Character> passwordList = new LinkedList<>();
        passwordList.addAll(getRandomFromDic(ALPHA_CAPS));
        passwordList.addAll(getRandomFromDic(ALPHA));
        passwordList.addAll(getRandomFromDic(NUMERIC));
        Collections.shuffle(passwordList);
        StringBuilder password = new StringBuilder();
        for(Character c : passwordList) {
            password.append(c);
        }
        return password.toString();
    }

    private static List<Character> getRandomFromDic(String dic) {
        List<Character> password = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(dic.length());
            password.add(dic.charAt(index));
        }
        return password;
    }
}
