package cz.sohlich.natsproxy.util;

import com.google.protobuf.ByteString;
import cz.sohlich.natsproxy.core.impl.Request;
import cz.sohlich.natsproxy.proto.Protobuf;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/19/16.
 */
public class ContextUtilsTest {


    @Test
    public void mimeTest() {
        String headerString = "application/x-form;title*0*=us-ascii'en'This%20is%20even%20more%20";
        Map<String, Protobuf.Values> testMap = new HashMap<String, Protobuf.Values>();
        testMap.put("Content-Type", Protobuf.Values.newBuilder().addArr(headerString).build());
        String mime = ContextUtils.parseMimeType(testMap);
        Assert.assertEquals("application/x-form", mime);
    }

    @Test
    public void formParseTest() {
        String data = "name=Radek&surname=Sohlich&page=";
        Protobuf.Request protoRequest = Protobuf.Request
                .newBuilder()
                .setBody(ByteString.copyFrom(data.getBytes()))
                .build();
        Request request = new Request(protoRequest);
        try {
            Map<String, String> result = cz.sohlich.natsproxy.util.ContextUtils.parseForm(request, "application/x-www-form-urlencoded");
            Assert.assertEquals(2, result.size());
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
            Assert.fail();
        }


    }
}
