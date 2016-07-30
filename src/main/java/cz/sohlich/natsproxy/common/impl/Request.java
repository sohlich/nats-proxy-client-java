package cz.sohlich.natsproxy.common.impl;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import cz.sohlich.natsproxy.proto.Protobuf;

import java.util.Map;

/**
 * Class to encapsulate request.
 * Created by Radomir Sohlich on 3/16/16.
 */
public class Request {

    private Protobuf.Request request;

    public Request() {
    }

    public Request(Protobuf.Request request) {
        this.request = request;
    }

    public String getMethod() {
        return request.getMethod();
    }

    public void unmarshallFrom(byte[] data) throws InvalidProtocolBufferException {
        request = Protobuf.Request.parseFrom(data);
    }

    public byte[] getData() {
        ByteString bodyData = request.getBody();
        return bodyData != null ? bodyData.toByteArray() : null;
    }

    public Map<String, Protobuf.Values> getHeader() {
        return request.getHeader();
    }

    public String getURL() {
        return request.getURL();
    }

}
