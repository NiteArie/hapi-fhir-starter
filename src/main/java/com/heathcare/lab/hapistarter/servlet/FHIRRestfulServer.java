package com.heathcare.lab.hapistarter.servlet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.heathcare.lab.hapistarter.interceptor.OpenApiInterceptor;
import com.heathcare.lab.hapistarter.providers.BundleResourceProvider;
import com.heathcare.lab.hapistarter.providers.PatientResourceProvider;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = { "/postgres-r4/*" }, displayName = "FHIR Server")
public class FHIRRestfulServer extends RestfulServer {

    private ApplicationContext applicationContext;

    public FHIRRestfulServer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void initialize() throws ServletException {
        super.initialize();
        setFhirContext(FhirContext.forR4());
        OpenApiInterceptor openApiInterceptor = new OpenApiInterceptor();
        registerInterceptor(openApiInterceptor);
        registerProvider(applicationContext.getBean(BundleResourceProvider.class));
        registerProviders(applicationContext.getBean(PatientResourceProvider.class));
    }
}