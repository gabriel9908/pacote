
package com.library;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(5000);
        tomcat.setHostname("0.0.0.0");
        
        String webappDirLocation = "src/main/webapp/";
        Context ctx = tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        
        tomcat.start();
        System.out.println("Library Management System started at http://0.0.0.0:5000");
        tomcat.getServer().await();
    }
}
