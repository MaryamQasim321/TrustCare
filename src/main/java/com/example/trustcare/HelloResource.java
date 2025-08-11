package com.example.trustcare;
import com.example.trustcare.Logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
@Path("/hello")
public class HelloResource {
    private static final Logger logger = LoggerFactory.getLogger(TrustCareApplication.class);
    static {
    logger.info(LogUtils.info("application loaded"));
    System.out.println("HelloResource constructor called");
}
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        return "Hello from TrustCare API!";
    }
}