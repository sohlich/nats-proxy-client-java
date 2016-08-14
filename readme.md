[![Build Status](https://travis-ci.org/sohlich/nats-proxy-client-java.svg?branch=master)](https://travis-ci.org/sohlich/nats-proxy-client-java) [![Coverage Status](https://coveralls.io/repos/github/sohlich/nats-proxy-client-java/badge.svg?branch=master)](https://coveralls.io/github/sohlich/nats-proxy-client-java?branch=master)

# Java Client for [REST to NATS proxy](https://github.com/sohlich/nats-proxy) 
framework.


In manner of "Everything integrates with java." the Java client for the REST 
to NATS framework is implemented.

It provides the functionality of connecting the java service (microservice) 
to "REST over NATS" distributed system.



## Basic usage:

```java 
private final ConnectionFactory cf = new ConnectionFactory();
Connection conn = cf.createConnection();
ClientImpl client = new ClientImpl(conn);

// Subscribe the handler for specific
// URL.
client.get("/test",c -> {
    // Process the data
    byte[] data = c.getRequest().getData();
    c.JSON(HttpStatus.OK,"OK");
});
```




