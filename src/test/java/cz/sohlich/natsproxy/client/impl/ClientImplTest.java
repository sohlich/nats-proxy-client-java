package cz.sohlich.natsproxy.client.impl;

import com.google.protobuf.ByteString;
import cz.sohlich.natsproxy.proto.Protobuf;
import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Created by Radomir Sohlich on 7/17/16.
 */
public class ClientImplTest {


    private final Logger log = LoggerFactory.getLogger(ClientImplTest.class);
    private final String NATS_URL = "nats://localhost:4222";


    @org.junit.Test
    public void testGet() throws Exception {

        ConnectionFactory cf = new ConnectionFactory();
        ClientImpl client = new ClientImpl(cf);


        client.get("/test", c -> {
            byte[] data = c.getRequest().getData();
            Assert.assertTrue(data != null);
            Assert.assertTrue(data.length > 0);
            if (data != null) {
                System.out.println("Got:" + new String(data, StandardCharsets.UTF_8));
            }
        });


        Connection testConnection = cf.createConnection();

        Protobuf.Request testRequest = Protobuf
                .Request
                .newBuilder()
                .setBody(ByteString
                        .copyFrom("hello".getBytes()))
                .setMethod("GET")
                .setURL("/test")
                .build();


        testConnection.publish("GET:.test", testRequest.toByteArray());
        Thread.sleep(1000);
    }
}
