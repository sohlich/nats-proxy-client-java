package cz.sohlich.natsproxy.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Radomir Sohlich on 7/17/16.
 */
public class UrlUtilsTest {

    String testPath = "/home/:event/:session/:token";


    @Test
    public void testSubscribeURLToNats() throws Exception {
        String result = UrlUtils.subscribeURLToNats("GET", testPath);
        Assert.assertEquals("GET:.home.*.*.*", result);

    }
}
