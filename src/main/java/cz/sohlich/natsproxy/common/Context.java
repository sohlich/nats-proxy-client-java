package cz.sohlich.natsproxy.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.sohlich.natsproxy.common.impl.Request;
import cz.sohlich.natsproxy.common.impl.Response;

/**
 * Created by Radomir Sohlich on 7/17/16.
 */
public interface Context {
    Request getRequest();

    Response getResponse();

    void abort();

    void abortWithJson(Object object) throws JsonProcessingException;

    Object bindJSON(Class clazz);

    String formVariable(String name);

    boolean isAborted();

    void JSON(int statusCode, Object object) throws JsonProcessingException;

    String pathVariable(String name);

    void parseForm();
}
