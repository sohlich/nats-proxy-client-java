package cz.sohlich.natsproxy;

import com.google.protobuf.ByteString;
import cz.sohlich.natsproxy.common.impl.Request;
import cz.sohlich.natsproxy.proto.Protobuf;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/30/16.
 */
public class HttpUtilsTest {

    public static final String MIMETYPE_TESTHEADER = "application/x-form;title*0*=us-ascii'en'This%20is%20even%20more%20";
    public static final String TEST_MIME = "application/x-form";

    @Test
    public void testParseForm() throws Exception {
        String data = "name=Radek&surname=Sohlich&page=";
        Protobuf.Request protoRequest = Protobuf.Request
                .newBuilder()
                .setBody(ByteString.copyFrom(data.getBytes()))
                .build();
        Request request = new Request(protoRequest);
        try {
            Map<String, String> result = HttpUtils.parseForm(request,
                    HttpUtils.APPLICATION_X_WWW_FORM_URLENCODED);
            Assert.assertEquals(2, result.size());
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testSplitQuery() throws Exception {
        String data = "name=Radek&surname=Sohlich&page=";
        Map<String, String> params = HttpUtils.splitQuery(data);
        Assert.assertEquals(2, params.size());
        Assert.assertEquals("Radek", params.get("name"));

    }

    @Test
    public void testParseMimeType() throws Exception {
        String headerString = MIMETYPE_TESTHEADER;
        Map<String, Protobuf.Values> testMap = new HashMap<String, Protobuf.Values>();
        testMap.put(HttpUtils.CONTENT_TYPE, Protobuf.Values.newBuilder().addArr
                (headerString)
                .build());
        String mime = HttpUtils.parseMimeType(testMap);
        Assert.assertEquals(TEST_MIME, mime);
    }
}
