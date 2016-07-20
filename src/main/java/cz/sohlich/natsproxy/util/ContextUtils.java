package cz.sohlich.natsproxy.util;

import cz.sohlich.natsproxy.client.exception.ClientException;
import cz.sohlich.natsproxy.core.impl.Request;
import cz.sohlich.natsproxy.proto.Protobuf;
import org.apache.http.entity.ContentType;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/20/16.
 */
public class ContextUtils {


    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    private ContextUtils() {
    }

    public static String parseMimeType(Map<String, Protobuf.Values> header) {
        Protobuf.Values contentType = header.get(CONTENT_TYPE);
        Protobuf.Values vals = header.get(CONTENT_TYPE);
        if (vals == null) {
            vals = header.get(CONTENT_DISPOSITION);
        }
        String value = null;
        if (vals != null && vals.getArrList().size() > 0) {
            value = vals.getArrList().get(0);
            value = value.split(";")[0];
        }
        return value;
    }

    public static Map<String, String> parseForm(Request request, String header) throws MalformedURLException, UnsupportedEncodingException {
        if (request.getData() == null) {
            throw new ClientException("Request has no body");
        }

        String ct;
        if (header == null || header.isEmpty()) {
            ct = "application/x-www-form-urlencoded";
        }

        ct = ContentType.parse(header).getMimeType();


        if ("application/x-www-form-urlencoded".equals(ct)) {
            return splitQuery(new String(request.getData()));
        }

        throw new ClientException("cannot parse other mime than application/x-www-form-urlencoded");
    }


    public static Map<String, String> splitQuery(String data) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = data;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyVal = pair.split("=");
            if (keyVal.length > 1) {
                query_pairs.put(URLDecoder.decode(keyVal[0], "UTF-8"), URLDecoder.decode(keyVal[1], "UTF-8"));
            }
        }
        return query_pairs;
    }
}
