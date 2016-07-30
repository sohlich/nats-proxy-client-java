package cz.sohlich.natsproxy;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/30/16.
 */
public class NatsUtilsTest {

    String testPath = "/home/:event/:session/:token";


    @Test
    public void testSubscribeURLToNats() throws Exception {
        String result = NatsUtils.subscribeURLToNats("GET", testPath);
        Assert.assertEquals("GET:.home.*.*.*", result);

    }

    @Test
    public void testBuildParams() {
        String url = "/bla/bla/:param1/:param2";
        Map<String, Integer> resultMap = NatsUtils.buildParamsMap(url);
        Assert.assertEquals(new Integer(3), resultMap.get("param1"));
    }
}
