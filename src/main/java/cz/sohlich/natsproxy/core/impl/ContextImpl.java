package cz.sohlich.natsproxy.core.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.sohlich.natsproxy.client.exception.ClientException;
import cz.sohlich.natsproxy.core.HttpStatus;
import cz.sohlich.natsproxy.proto.Protobuf;
import cz.sohlich.natsproxy.util.ContextUtils;
import org.omg.CORBA.Object;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * Class wrapping request and response to be
 * transferred through NATS channel.
 * <p>
 * Created by Radomir Sohlich on 3/16/16.
 */
public class ContextImpl implements cz.sohlich.natsproxy.core.Context {

    private final String url;
    private Request request;
    private Response response;
    private int index;
    private int abortIndex;
    private Map<String, Integer> params;
    private Map<String, String> form;

    public ContextImpl(String url, Response response, Request request) {
        this.url = url;
        this.response = response;
        this.request = request;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getAbortIndex() {
        return abortIndex;
    }

    @Override
    public void setAbortIndex(int abortIndex) {
        this.abortIndex = abortIndex;
    }

    @Override
    public Map<String, Integer> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<String, Integer> params) {
        this.params = params;
    }


    @Override
    public void abort() {
        this.abortIndex = index;
    }

    @Override
    public void abortWithJson(Object object) throws JsonProcessingException {
        abort();
        JSON(HttpStatus.SERVER_ERROR, object);
    }

    @Override
    public void bindJSON(Object object) {

    }


    @Override
    public String formVariable(String name) {
        return null;
    }

    @Override
    public boolean isAborted() {
        return abortIndex <= index;
    }

    @Override
    public void JSON(int statusCode, Object object) throws JsonProcessingException {
        response.setStatusCode(HttpStatus.SERVER_ERROR);
        response.setBody(objectToBody(object));
    }

    @Override
    public String pathVariable(String name) {
        return null;
    }

    @Override
    public void parseForm() {
        Protobuf.Values vals = request.getHeader().get("Content-Type");
        String header = null;
        if (vals != null) {
            if (vals.getArrList().size() > 0) {
                header = vals.getArrList().get(0);
            }
        }
        try {
            form = ContextUtils.parseForm(request, header);
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            throw new ClientException(ex);
        }
    }


    private byte[] objectToBody(Object o) throws JsonProcessingException {
        byte[] body = new ObjectMapper().writeValueAsBytes(o);
        return body;
    }


}
