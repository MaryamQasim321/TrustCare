package com.example.trustcare;
import com.example.trustcare.HelloResource;
import com.example.trustcare.controller.CaregiverResource;
import com.example.trustcare.controller.UserResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
@ApplicationPath("/api")
public class TrustCareApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(HelloResource.class);
        classes.add(CaregiverResource.class);
        classes.add(UserResource.class);
        classes.add(org.glassfish.jersey.media.multipart.MultiPartFeature.class); // add this


        return classes;
    }
}