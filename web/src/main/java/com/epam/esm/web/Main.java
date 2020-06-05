package com.epam.esm.web;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    private static final String PORT = "8082";

    public static void main(String[] args) throws Exception {

        //String webappDirLocation = "src/main/webapp/";
        String webappDirLocation = "./";
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = PORT;
        }

        tomcat.setPort(Integer.parseInt(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        // If you want to declare an alternative location for your "WEB-INF/classes" dir
        // (Servlet 3.0 annotation will also work)
        /*
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);
        */

        tomcat.start();
        tomcat.getServer().await();

    }
}
