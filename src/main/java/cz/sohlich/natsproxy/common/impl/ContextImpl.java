package cz.sohlich.natsproxy.common.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.sohlich.natsproxy.HttpUtils;
import cz.sohlich.natsproxy.common.HttpStatus;
import cz.sohlich.natsproxy.exception.ClientException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Class wrapping request and response to be
 * transferred through NATS channel.
 * <p>
 * Created by Radomir Sohlich on 3/16/16.
 */
public class ContextImpl implements cz.sohlich.natsproxy.common.Context {

    private Request request;
    private Response response;
    private int index = -1;
    private int abortIndex = 0;
    private Map<String, Integer> params;
    private Map<String, String> form;

    public ContextImpl(Map<String, Integer> params, Response response, Request request) {
        this.params = params;
        this.response = response;
        this.request = request;
    }

    @Override
    public Request getRequest() {
        return request;
    }


    @Override
    public Response getResponse() {
        return response;
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
    public Object bindJSON(Class clazz) {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(request.getData(), clazz);
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }


    @Override
    public String formVariable(String name) {
        if (form != null) {
            return form.get(name);
        }
        return null;
    }

    @Override
    public boolean isAborted() {
        return abortIndex <= index;
    }

    @Override
    public void JSON(int statusCode, Object object) throws JsonProcessingException {
        response.setStatusCode(statusCode);
        response.setBody(objectToBody(object));
    }

    @Override
    public String pathVariable(String name) {
        String requestURL = request.getURL();
        if (requestURL == null || requestURL.isEmpty()) {
            return "";
        }

        String URL = requestURL.replaceAll("[?]{1}.*", "");
        URL = URL.replaceAll("^[a-z]{4}:/{2}[a-z]{0,3}\\.?[a-z,0-9,\\.]{1,}\\.?[a-z]{0,}:?[0-9]{0,4}", "");
        String[] urlParts = URL.split("/");
        Integer index = params.get(name);
        if (index != null && urlParts.length > index) {
            return urlParts[index];
        }
        return "";
    }

    @Override
    public void parseForm() {
        String header = HttpUtils.parseMimeType(request.getHeader());
        try {
            String method = request.getMethod();
            String query = new URL(request.getURL()).getQuery();
            Map<String, String> queryValues = HttpUtils.splitQuery(query);

            Map<String, String> formValues;
            if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
                formValues = HttpUtils.parseForm(request, header);
                // If form contains same param as query,
                // the form value will be used.
                queryValues.putAll(formValues);
            }
            form = queryValues;
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            throw new ClientException(ex);
        }
    }


    private byte[] objectToBody(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(o);
    }


}
