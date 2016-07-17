package cz.sohlich.natsproxy.core.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import cz.sohlich.natsproxy.proto.Protobuf;

/**
 * Class to encapsulate request.
 * Created by Radomir Sohlich on 3/16/16.
 */
public class Request {

    private Protobuf.Request request;

    public Request() {
    }

    public void unmarshallFrom(byte[] data) throws InvalidProtocolBufferException {
        request = Protobuf.Request.parseFrom(data);
    }

    public byte[] getData() {
        ByteString bodyData = request.getBody();
        return bodyData != null ? bodyData.toByteArray() : null;
    }


}
