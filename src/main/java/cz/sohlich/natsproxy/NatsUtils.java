package cz.sohlich.natsproxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Radomir Sohlich on 7/16/16.
 */
public class NatsUtils {

    private final static String PATH_REGEX = ":[A-z,0-9,$,-,_,.,+,!,*,',(,),\\,]{1,}";


    private NatsUtils() {
    }

    public static String subscribeURLToNats(String method, String urlPath) {
        String subURL = urlPath.replaceAll(PATH_REGEX, "*");
        subURL = subURL.replaceAll("/", ".");
        subURL = String.format("%s:%s", method, subURL);
        return subURL;
    }

    public static Map<String, Integer> buildParamsMap(String URL) {
        Map<String, Integer> paramMap = new HashMap<>();
        String[] params = URL.split("/");
        int index = 0;
        for (String param : params) {
            if (param.length() > 1 && param.charAt(0) == ':') {
                paramMap.put(param.substring(1), index);
            }
            index++;
        }
        return paramMap;
    }


}
