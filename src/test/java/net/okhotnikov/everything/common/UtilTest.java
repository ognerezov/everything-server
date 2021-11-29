package net.okhotnikov.everything.common;

import static junit.framework.Assert.*;
import net.okhotnikov.everything.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sergey Okhotnikov.
 */
@SpringBootTest
@ActiveProfiles("test")
public class UtilTest {

    @Test
    public void testNameGenerator(){
        Set<String> set = new HashSet<>();
        for(int i= 0; i<100; i++){
            String s = StringUtil.getName(5);
            System.out.println(s);
            assertFalse(set.contains(s));
            set.add(s);
        }
    }
}
