package cz.sohlich.natsproxy.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.sohlich.natsproxy.core.impl.Request;
import cz.sohlich.natsproxy.core.impl.Response;
import org.omg.CORBA.Object;

import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/17/16.
 */
public interface Context {
    Request getRequest();

    void setRequest(Request request);

    Response getResponse();

    void setResponse(Response response);

    int getIndex();

    void setIndex(int index);

    int getAbortIndex();

    void setAbortIndex(int abortIndex);

    Map<String, Integer> getParams();

    void setParams(Map<String, Integer> params);

    void abort();

    void abortWithJson(Object object) throws JsonProcessingException;

    void bindJSON(Object object);

    String formVariable(String name);

    boolean isAborted();

    void JSON(int statusCode, Object object) throws JsonProcessingException;

    String pathVariable(String name);

    void parseForm();
}
