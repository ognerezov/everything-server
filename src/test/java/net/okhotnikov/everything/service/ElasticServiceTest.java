package net.okhotnikov.everything.service;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by Sergey Okhotnikov.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticServiceTest {

    @Autowired
    private ElasticService elasticService;

    @Test
    public void testNotFoundKey() throws IOException {
        assertNull(elasticService.get("-1",ElasticService.BOOK));
    }
}
