package cz.sohlich.natsproxy.core.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.sohlich.natsproxy.core.HttpStatus;
import org.omg.CORBA.Object;

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
    public void Abort() {
        this.abortIndex = index;
    }

    @Override
    public void AbortWithJson(Object object) throws JsonProcessingException {
        Abort();
        JSON(HttpStatus.SERVER_ERROR, object);
    }

    @Override
    public void BindJSON(Object object) {

    }

    @Override
    public String FormVariable(String name) {
        return null;
    }

    @Override
    public boolean IsAborted() {
        return abortIndex <= index;
    }

    @Override
    public void JSON(int statusCode, Object object) throws JsonProcessingException {
        response.setStatusCode(HttpStatus.SERVER_ERROR);
        response.setBody(objectToBody(object));
    }

    @Override
    public String PathVariable(String name) {
        return null;
    }


    private byte[] objectToBody(Object o) throws JsonProcessingException {
        byte[] body = new ObjectMapper().writeValueAsBytes(o);
        return body;
    }
}
