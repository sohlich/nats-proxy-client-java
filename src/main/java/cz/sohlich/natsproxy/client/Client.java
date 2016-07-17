package cz.sohlich.natsproxy.client;

import cz.sohlich.natsproxy.core.NatsHandler;
import io.nats.client.Connection;

/**
 * Created by Radomir Sohlich on 3/16/16.
 */
public interface Client {


    public void get(String natsUrl, NatsHandler handler);

    public void post(String natsUrl, NatsHandler handler);

    public void put(String natsUrl, NatsHandler handler);

    public void delete(String natsUrl, NatsHandler handler);

    public void subscribe(String method, String natsUrl, NatsHandler handler);


    public Connection getConnection();

}
