package cz.sohlich.natsproxy.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.ByteString;
import cz.sohlich.natsproxy.proto.Protobuf;
import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Created by Radomir Sohlich on 7/17/16.
 */
public class ClientImplTest {


    private final Logger log = LoggerFactory.getLogger(ClientImplTest.class);
    private final String NATS_URL = "nats://localhost:4222";


    @org.junit.Test
    public void testGet() throws Exception {

        ConnectionFactory cf = new ConnectionFactory();
        Connection conn = cf.createConnection();
        ClientImpl client = new ClientImpl(conn);


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

        testConnection.close();
        conn.close();
    }


    @Test
    public void integrationTest() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory cf = new ConnectionFactory();
        Connection connection = cf.createConnection();
        ClientImpl client = new ClientImpl(connection);


        client.get("/test/:id", c -> {
            log.info("Getting {}", c.pathVariable("id"));
            try {
                c.JSON(200, "Ok");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });


        Thread.sleep(600000);

        connection.close();
    }









}
