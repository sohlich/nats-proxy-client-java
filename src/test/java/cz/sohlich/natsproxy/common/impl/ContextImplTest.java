package cz.sohlich.natsproxy.common.impl;

import cz.sohlich.natsproxy.NatsUtils;
import cz.sohlich.natsproxy.common.Context;
import cz.sohlich.natsproxy.proto.Protobuf;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/22/16.
 */
public class ContextImplTest {


    @Test
    public void completeTest() throws IOException {

//        Original request go code snippet
//        reader = strings.NewReader("post=postval")
//        req, _ = http.NewRequest("POST", "http://127.0.0.1:3000/test/12324/123?name=testname", reader)
//        req.Header.Set("Content-Type", "application/x-www-form-urlencoded; param=value")


        byte[] requestData = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("test_request.dat"));
        Protobuf.Request r = Protobuf.Request.parseFrom(requestData);
        Request request = new Request(r);

        Map<String, Integer> paramMap = NatsUtils.buildParamsMap("/test/:id1/:id2");

        Response resp = new Response();

        Context context = new ContextImpl(paramMap, resp, request);
        context.parseForm();

        String formVariable = context.formVariable("post");
        Assert.assertEquals("postval", formVariable);

        String pathVariable = context.pathVariable("id1");
        Assert.assertEquals("12324", pathVariable);


        //Test get/set
        Assert.assertEquals(resp, context.getResponse());
        Assert.assertEquals(request, context.getRequest());
    }


}
