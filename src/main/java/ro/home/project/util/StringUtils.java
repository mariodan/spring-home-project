package ro.home.project.util;

import java.util.Random;

public class StringUtils {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

    private static final Random random = new Random();

    public static String getRandomString(String inputString) {
        int max = 6;
        StringBuilder sb = new StringBuilder(max);
        for (int i = 0; i < max; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return inputString.concat("_").concat(sb.toString());
    }
}
