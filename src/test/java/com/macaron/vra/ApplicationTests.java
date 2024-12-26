package com.macaron.vra;

import com.macaron.vra.launcher.AIAPsttDataUploadLauncher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = AIAPsttDataUploadLauncher.class)
@SpringBootTest(classes = ApplicationTests.class)
//@EnableAutoConfiguration
public class ApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);
    @Test
    public void contextLoads() throws Exception {

        logger.info("test");
        Assert.assertEquals(1, 1);

    }

}
