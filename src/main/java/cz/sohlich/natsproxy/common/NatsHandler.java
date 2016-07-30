package cz.sohlich.natsproxy.common;

/**
 * Handler interface.
 *
 * Created by Radomir Sohlich on 3/16/16.
 */
public interface NatsHandler {

    /**
     * Handles the incoming request and writes and response
     *
     * @param c
     */
    void Handle(Context c);

}
