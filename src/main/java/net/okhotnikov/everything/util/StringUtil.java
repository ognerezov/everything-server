package net.okhotnikov.everything.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Sergey Okhotnikov.
 */
public class StringUtil {
    private static SecureRandom random = new SecureRandom();

    public static String getName() {
        return new BigInteger(130, random).toString(32);
    }

    public static String getName(Integer length) {
        return getName().substring(0,length);
    }
}
