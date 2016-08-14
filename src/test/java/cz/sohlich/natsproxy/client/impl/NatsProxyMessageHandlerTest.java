package cz.sohlich.natsproxy.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.sohlich.natsproxy.client.Client;
import cz.sohlich.natsproxy.client.NatsHandler;
import cz.sohlich.natsproxy.common.Context;
import cz.sohlich.natsproxy.exception.ClientException;
import io.nats.client.Connection;
import io.nats.client.Message;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Radomir Sohlich on 8/14/16.
 */
public class NatsProxyMessageHandlerTest {

    @Test(expected = ClientException.class)
    public void testOnMessage() throws Exception {
        NatsProxyMessageHandler handler = new NatsProxyMessageHandler(Mockito
                .mock(Client.class), new LinkedList<>(), Mockito.mock
                (NatsHandler.class), "");
        Message m = Mockito.mock(Message.class);
        Mockito.when(m.getData()).thenReturn("bad".getBytes());
        handler.onMessage(m);
    }


    @Test
    public void testHandleFilterAndAbortedContext() {
        NatsHandler mainHandler = Mockito.mock(NatsHandler.class);
        List<NatsHandler> filters = new ArrayList<>();
        filters.add(new NatsHandler() {
            @Override
            public void Handle(Context c) {
                try {
                    c.abortWithJson("OK");
                } catch (JsonProcessingException e) {
                    throw new ClientException("cannot abort");
                }
            }
        });
        Client client = Mockito.mock(Client.class);
        Mockito.when(client.getConnection()).thenReturn(Mockito.mock
                (Connection.class));
        NatsProxyMessageHandler handler = new NatsProxyMessageHandler(client,
                filters, mainHandler, "");
        Message m = Mockito.mock(Message.class);
        Mockito.when(m.getData()).thenReturn("".getBytes());
        handler.onMessage(m);
        Mockito.verifyZeroInteractions(mainHandler);
    }


}
