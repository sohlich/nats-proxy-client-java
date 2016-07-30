package cz.sohlich.natsproxy.common.impl;

import com.google.protobuf.ByteString;
import cz.sohlich.natsproxy.proto.Protobuf;

/**
 * Class to encapsulate HTTP response.
 * Created by Radomir Sohlich on 3/16/16.
 */
public class Response {

    Protobuf.Response.Builder responseBuilder;

    public Response() {
        this.responseBuilder = Protobuf.Response.newBuilder();
    }

    public void setStatusCode(int status) {
        responseBuilder.setStatusCode(status);
    }

    public void setBody(byte[] data) {
        responseBuilder.setBody(ByteString.copyFrom(data));
    }


    public byte[] marshall() {
        return responseBuilder.build().toByteArray();
    }
}
