package cz.sohlich.natsproxy.client.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import cz.sohlich.natsproxy.client.Client;
import cz.sohlich.natsproxy.client.exception.ClientException;
import cz.sohlich.natsproxy.core.Context;
import cz.sohlich.natsproxy.core.NatsHandler;
import cz.sohlich.natsproxy.core.impl.ContextImpl;
import cz.sohlich.natsproxy.core.impl.Request;
import cz.sohlich.natsproxy.core.impl.Response;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

import java.io.IOException;

/**
 * Created by Radomir Sohlich on 7/16/16.
 */
public class NatsProxyMessageHandler implements MessageHandler {

    private final NatsHandler handler;
    private final String url;
    private final Client client;

    public NatsProxyMessageHandler(Client client, NatsHandler handler, String url) {
        this.handler = handler;
        this.url = url;
        this.client = client;
    }

    public void onMessage(Message message) {
        Request request = new Request();
        try {
            request.unmarshallFrom(message.getData());
        } catch (InvalidProtocolBufferException ex) {
            throw new ClientException("Cannot parse data", ex);
        }

        Response response = new Response();

        Context context = new ContextImpl(url, response, request);

        handler.Handle(context);

        try {
            client.getConnection().publish(message.getReplyTo(), response.marshall());
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }
}
