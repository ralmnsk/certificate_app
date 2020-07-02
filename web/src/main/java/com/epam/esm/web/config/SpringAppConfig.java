package com.epam.esm.web.config;

import com.epam.esm.service.exception.NoHandlerException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SpringAppConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) {

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        container.addListener(new ContextLoaderListener(rootContext));

        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

        ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext) {
            @Override
            protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
                throw new NoHandlerException("No handler found");
            }
        });
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
