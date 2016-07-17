package cz.sohlich.natsproxy.util;

/**
 * Created by Radomir Sohlich on 7/16/16.
 */
public class UrlUtils {


    private final static String PATH_REGEX = ":[A-z,0-9,$,-,_,.,+,!,*,',(,),\\,]{1,}";


    private UrlUtils() {
    }


    public static String subscribeURLToNats(String method, String urlPath) {
        String subURL = urlPath.replaceAll(PATH_REGEX, "*");
        subURL = subURL.replaceAll("/", ".");
        subURL = String.format("%s:%s", method, subURL);
        return subURL;
    }


}
