package net.okhotnikov.everything.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Sergey Okhotnikov.
 */
public class StringUtil {
    private static SecureRandom random = new SecureRandom();

    public static String getName() {
        return new BigInteger(130, random).toString(32);
    }

    public static String getName(int length) {
        return getName().substring(0,length);
    }

    public static String getNumberCode(int length){
        return getName().replaceAll("\\D","").substring(0,length);
    }
}
