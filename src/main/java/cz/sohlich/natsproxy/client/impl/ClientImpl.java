package cz.sohlich.natsproxy.client.impl;

import cz.sohlich.natsproxy.client.Client;
import cz.sohlich.natsproxy.core.NatsHandler;
import cz.sohlich.natsproxy.util.UrlUtils;
import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Radomir Sohlich on 3/16/16.
 */
public class ClientImpl implements Client {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";


    private final Connection connection;

    public ClientImpl(ConnectionFactory factory) throws IOException, TimeoutException {
        connection = factory.createConnection();
    }


    public void get(String natsUrl, NatsHandler handler) {
        subscribe(METHOD_GET, natsUrl, handler);
    }

    public void post(String natsUrl, NatsHandler handler) {
        subscribe(METHOD_POST, natsUrl, handler);
    }

    public void put(String natsUrl, NatsHandler handler) {
        subscribe(METHOD_PUT, natsUrl, handler);
    }

    public void delete(String natsUrl, NatsHandler handler) {
        subscribe(METHOD_DELETE, natsUrl, handler);
    }

    public void subscribe(String method, String natsUrl, NatsHandler handler) {
        String subUrl = UrlUtils.subscribeURLToNats(method, natsUrl);
        connection.subscribe(subUrl, new NatsProxyMessageHandler(this, handler, natsUrl));
    }

    public Connection getConnection() {
        return connection;
    }
}
