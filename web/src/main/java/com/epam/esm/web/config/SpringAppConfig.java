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


/**
 * The type Spring app config.
 * The implementation of {@link WebApplicationInitializer} interface.
 */
public class SpringAppConfig implements WebApplicationInitializer {
    /*
     * onStartup method configures {@link javax.servlet.ServletContext},
     * creates web application context, adds dispatcher and set
     * dispatcher configuration.
     *
     * */
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
