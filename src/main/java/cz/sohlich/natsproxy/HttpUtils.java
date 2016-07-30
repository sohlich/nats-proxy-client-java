package cz.sohlich.natsproxy;

import cz.sohlich.natsproxy.common.impl.Request;
import cz.sohlich.natsproxy.exception.ClientException;
import cz.sohlich.natsproxy.proto.Protobuf;
import org.apache.http.entity.ContentType;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/29/16.
 */
public class HttpUtils {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String UTF_8 = "UTF-8";


    /**
     * Parse the request data to form values {Key,Value} according the rules
     * of golang parse form.
     * <p>
     * If body contains same key as the url query. The body values override
     * the query params.
     *
     * @param request  request with body to be parsed
     * @param mimeType mimeType from the header
     * @return key/value map
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> parseForm(Request request, String mimeType)
            throws MalformedURLException, UnsupportedEncodingException {
        if (request.getData() == null) {
            throw new ClientException("Request has no body");
        }

        String ct;

        if (mimeType == null || mimeType.isEmpty()) {
            ct = APPLICATION_X_WWW_FORM_URLENCODED;
        } else {
            ct = ContentType.parse(mimeType).getMimeType();
        }

        if (APPLICATION_X_WWW_FORM_URLENCODED.equals(ct)) {
            return HttpUtils.splitQuery(new String(request.getData()));
        }

        throw new ClientException("cannot parse other mime than application/x-www-form-urlencoded");
    }

    /**
     * Splits query into {Key,Value} pairs.
     *
     * @param data string containing data
     * @return Map with params
     */
    public static Map<String, String> splitQuery(String data) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = data.split("&");
        for (String pair : pairs) {
            String[] keyVal = pair.split("=");
            if (keyVal.length > 1) {
                try {
                    query_pairs.put(URLDecoder.decode(keyVal[0], UTF_8),
                            URLDecoder.decode(keyVal[1], UTF_8));
                } catch (UnsupportedEncodingException ex) {
                    throw new ClientException(ex);
                }
            }
        }
        return query_pairs;
    }


    /**
     * Parse the contnet type from request header. The mime type is
     * defined under "Content-Type" header or "Content-Disposition" header.
     *
     * @param header Map with header params. Header from proto
     * @return mime type string from header.
     */
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

}
