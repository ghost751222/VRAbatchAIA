package com.macaron.vra;

import com.macaron.vra.launcher.AIAPsttDataUploadLauncher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AIAPsttDataUploadLauncher.class)
@EnableAutoConfiguration
public class ApplicationTests {
    
    @Test
    public void contextLoads() throws Exception {

        Assert.assertEquals(1, 1);

    }

}
