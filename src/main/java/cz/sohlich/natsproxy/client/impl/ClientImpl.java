package cz.sohlich.natsproxy.client.impl;

import cz.sohlich.natsproxy.NatsUtils;
import cz.sohlich.natsproxy.client.Client;
import cz.sohlich.natsproxy.common.NatsHandler;
import io.nats.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Radomir Sohlich on 3/16/16.
 */
public class ClientImpl implements Client {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";


    private final List<NatsHandler> filters;
    private final Connection connection;

    public ClientImpl(Connection connection)
            throws IOException, TimeoutException {
        this.connection = connection;
        filters = new ArrayList<>();
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
        String subUrl = NatsUtils.subscribeURLToNats(method, natsUrl);
        connection.subscribe(subUrl, new NatsProxyMessageHandler(this, filters,
                handler, natsUrl));
    }

    public Connection getConnection() {
        return connection;
    }
}
