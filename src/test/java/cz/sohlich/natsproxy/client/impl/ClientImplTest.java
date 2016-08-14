package cz.sohlich.natsproxy.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.ByteString;
import cz.sohlich.natsproxy.client.Client;
import cz.sohlich.natsproxy.common.HttpStatus;
import cz.sohlich.natsproxy.proto.Protobuf;
import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
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
    private final ConnectionFactory cf = new ConnectionFactory();

    @org.junit.Test
    public void testGet() throws Exception {
        TestObject to = new TestObject() {
            @Override
            String getMethod() {
                return "GET";
            }

            @Override
            void createSubscribtion(Client client) {
                client.get("/test", c -> {
                    byte[] data = c.getRequest().getData();
                    Assert.assertTrue(data != null);
                    Assert.assertTrue(data.length > 0);
                    System.out.println("Message: " + new String(data,
                            StandardCharsets.UTF_8) + " received.");
                    try {
                        c.JSON(HttpStatus.OK, "OK");
                    } catch (JsonProcessingException e) {
                        Assert.fail();
                    }
                });
            }
        };
        to.doTest();
    }


    @org.junit.Test
    public void testDelete() throws Exception {
        TestObject to = new TestObject() {
            @Override
            String getMethod() {
                return "DELETE";
            }

            @Override
            protected Protobuf.Request createRequest() {
                try {
                    byte[] requestData = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("test_request.dat"));
                    Protobuf.Request r = Protobuf.Request.parseFrom(requestData);
                    return Protobuf.Request.newBuilder(r).setURL
                            ("http://test?id=10")
                            .build();

                } catch (Exception e) {
                    Assert.fail();
                }
                return null;
            }

            @Override
            void createSubscribtion(Client client) {
                client.delete("/test", c -> {
                    byte[] data = c.getRequest().getData();
                    c.parseForm();
                    Assert.assertEquals("10", c.formVariable("id"));
                    try {
                        c.JSON(HttpStatus.OK, "OK");
                    } catch (JsonProcessingException e) {
                        Assert.fail();
                    }
                });
            }
        };
        to.doTest();
    }


    @org.junit.Test
    public void testPost() throws Exception {
        TestObject to = new TestObject() {
            @Override
            String getMethod() {
                return "POST";
            }

            @Override
            protected Protobuf.Request createRequest() {
                try {
                    byte[] requestData = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("test_request.dat"));
                    Protobuf.Request r = Protobuf.Request.parseFrom(requestData);
                    return Protobuf.Request.newBuilder(r).setURL("http://test")
                            .build();

                } catch (Exception e) {
                    Assert.fail();
                }
                return null;
            }

            @Override
            void createSubscribtion(Client client) {
                client.post("/test", c -> {
                    byte[] data = c.getRequest().getData();
                    Assert.assertTrue(data != null);
                    Assert.assertTrue(data.length > 0);
                    c.parseForm();
                    Assert.assertEquals("postval", c.formVariable("post"));
                    System.out.println("Form value: " + c.formVariable
                            ("post") + " received.");
                    try {
                        c.JSON(HttpStatus.OK, "OK");
                    } catch (JsonProcessingException e) {
                        Assert.fail();
                    }
                });
            }
        };
        to.doTest();
    }

    @org.junit.Test
    public void testPut() throws Exception {
        TestObject to = new TestObject() {
            @Override
            String getMethod() {
                return "PUT";
            }

            @Override
            protected Protobuf.Request createRequest() {
                try {
                    byte[] requestData = IOUtils.toByteArray(ClassLoader.getSystemResourceAsStream("test_request.dat"));
                    Protobuf.Request r = Protobuf.Request.parseFrom(requestData);
                    return Protobuf.Request.newBuilder(r).setURL("http://test")
                            .build();

                } catch (Exception e) {
                    Assert.fail();
                }
                return null;
            }

            @Override
            void createSubscribtion(Client client) {
                client.put("/test", c -> {
                    byte[] data = c.getRequest().getData();
                    Assert.assertTrue(data != null);
                    Assert.assertTrue(data.length > 0);
                    c.parseForm();
                    Assert.assertEquals("postval", c.formVariable("post"));
                    System.out.println("Form value: " + c.formVariable
                            ("post") + " received.");
                    try {
                        c.JSON(HttpStatus.OK, "OK");
                    } catch (JsonProcessingException e) {
                        Assert.fail();
                    }
                });
            }
        };
        to.doTest();
    }


    abstract class TestObject {

        abstract void createSubscribtion(Client client);

        abstract String getMethod();

        protected Protobuf.Request createRequest() {
            return Protobuf
                    .Request
                    .newBuilder()
                    .setBody(ByteString
                            .copyFrom("hello".getBytes()))
                    .setMethod(getMethod())
                    .setURL("/test")
                    .build();
        }

        public void doTest() throws InterruptedException, IOException, TimeoutException {
            Connection conn = cf.createConnection();
            ClientImpl client = new ClientImpl(conn);
            createSubscribtion(client);
            Connection testConnection = cf.createConnection();
            Protobuf.Request testRequest = createRequest();
            Message m = testConnection.request(getMethod() + ":.test",
                    testRequest
                            .toByteArray());
            Thread.sleep(1000);
            Protobuf.Response resp = Protobuf.Response.parseFrom(m.getData());
            Assert.assertEquals("\"OK\"", resp.getBody().toStringUtf8());
            testConnection.close();
            conn.close();
        }


    }


}
