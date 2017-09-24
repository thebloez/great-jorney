package com.springMVC.shop.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringWebAppInitializer implements WebApplicationInitializer{

	public void onStartup(ServletContext servletContext) throws ServletException {

		AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
		appContext.register(ApplicationContextConfig.class);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher",
				new DispatcherServlet((WebApplicationContext) appContext));
		dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        ContextLoaderListener contextLoaderListener =
                new ContextLoaderListener((WebApplicationContext) appContext);
        FilterRegistration.Dynamic fr = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);

        fr.setInitParameter("encoding", "UTF-8");
        fr.setInitParameter("forceEncoding", "true");
        fr.addMappingForUrlPatterns(null, true, "/");
	}

}
