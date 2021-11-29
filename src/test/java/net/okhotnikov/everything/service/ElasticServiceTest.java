package net.okhotnikov.everything.service;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by Sergey Okhotnikov.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ElasticServiceTest {

    @Autowired
    private ElasticService elasticService;

    @Test
    public void testNotFoundKey() throws IOException {
        assertNull(elasticService.get("-1",ElasticService.BOOK));
    }

    @Test
    public void testRead() throws IOException {
        System.out.println(elasticService.multiGet(new HashSet<>(Collections.singleton("1"))));
    }
}
