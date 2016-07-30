package cz.sohlich.natsproxy.client.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import cz.sohlich.natsproxy.NatsUtils;
import cz.sohlich.natsproxy.client.Client;
import cz.sohlich.natsproxy.common.Context;
import cz.sohlich.natsproxy.common.NatsHandler;
import cz.sohlich.natsproxy.common.impl.ContextImpl;
import cz.sohlich.natsproxy.common.impl.Request;
import cz.sohlich.natsproxy.common.impl.Response;
import cz.sohlich.natsproxy.exception.ClientException;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/16/16.
 */
public class NatsProxyMessageHandler implements MessageHandler {

    private final NatsHandler handler;
    private final String url;
    private final Client client;
    private final Map<String, Integer> pathParamMap;
    private final List<NatsHandler> filters;

    public NatsProxyMessageHandler(Client client, List<NatsHandler> filters,
                                   NatsHandler handler, String url) {
        this.handler = handler;
        this.url = url;
        this.client = client;
        this.filters = filters;
        this.pathParamMap = NatsUtils.buildParamsMap(url);
    }

    public void onMessage(Message message) {
        Request request = new Request();
        try {
            request.unmarshallFrom(message.getData());
        } catch (InvalidProtocolBufferException ex) {
            throw new ClientException("Cannot parse data", ex);
        }

        Response response
                = new Response();

        Context context
                = new ContextImpl(pathParamMap, response, request);


        //Apply all filters
        for (NatsHandler filter : filters) {
            filter.Handle(context);
        }

        if (!context.isAborted()) {
            handler.Handle(context);
        }

        try {
            client.getConnection().publish(message.getReplyTo(),
                    response.marshall());
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }
}
