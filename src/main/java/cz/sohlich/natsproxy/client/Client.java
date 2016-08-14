package cz.sohlich.natsproxy.client;

import io.nats.client.Connection;

/**
 * Created by Radomir Sohlich on 3/16/16.
 */
public interface Client {


    /**
     * Sibscribes to GET method for given url
     *
     * @param natsUrl
     * @param handler
     */
    void get(String natsUrl, NatsHandler handler);

    /**
     * Sibscribes to POST method for given url
     *
     * @param natsUrl
     * @param handler
     */
    void post(String natsUrl, NatsHandler handler);


    /**
     * Sibscribes to PUT method for given url
     *
     * @param natsUrl
     * @param handler
     */
    void put(String natsUrl, NatsHandler handler);

    /**
     * Sibscribes to DELETE method for given url
     *
     * @param natsUrl
     * @param handler
     */
    void delete(String natsUrl, NatsHandler handler);

    /**
     * Sibscribes to given method for given url
     *
     * @param natsUrl
     * @param handler
     */
    void subscribe(String method, String natsUrl, NatsHandler handler);


    /**
     * Obtains underlying nats connection.
     *
     * @return
     */
    Connection getConnection();

}
