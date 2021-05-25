package com.heathcare.lab.hapistarter.servlet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import com.heathcare.lab.hapistarter.providers.BundleResourceProvider;
import com.heathcare.lab.hapistarter.providers.PatientResourceProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;

@WebServlet(urlPatterns = { "/postgres-r4/*" }, displayName = "FHIR Server")
public class FHIRRestfulServer extends RestfulServer {

    private ApplicationContext applicationContext;

    public FHIRRestfulServer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void initialize() throws ServletException {
        setFhirContext(FhirContext.forR4());

        // General Configuration
        setCopyright("KMS Technology - Heathcare DC");
        setServerVersion("1.0");
        setDefaultPrettyPrint(true);
        setDefaultPreferReturn(PreferReturnEnum.MINIMAL);
        setDefaultResponseEncoding(EncodingEnum.JSON);

        // HAPI Server Configuration
        // Now register the logging interceptor
        var loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setLoggerName("test.accesslog");
        loggingInterceptor.setMessageFormat(
                "Source[${remoteAddr}] Operation[${operationType} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}]"
        );
        registerInterceptor(loggingInterceptor);

        // Now register the interceptor
        var interceptor = new ResponseHighlighterInterceptor();
        registerInterceptor(interceptor);

        // Now register the interceptor
        var exceptionHandlingInterceptor = new ExceptionHandlingInterceptor();
        exceptionHandlingInterceptor.setReturnStackTracesForExceptionTypes(
                InternalErrorException.class,
                NullPointerException.class
        );
        registerInterceptor(exceptionHandlingInterceptor);

        // Now register the interceptor
        var openApiInterceptor = new OpenApiInterceptor();
        openApiInterceptor.setBannerImage(
                "https://cdn.kms-technology.com/wp-content/uploads/2018/10/logo.png"
        );
        registerInterceptor(openApiInterceptor);

        registerProvider(applicationContext.getBean(PatientResourceProvider.class));
        registerProvider(applicationContext.getBean(BundleResourceProvider.class));

        // Define your CORS configuration. This is an example
        // showing a typical setup. You should customize this
        // to your specific needs
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("x-fhir-starter");
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Content-Type");
        config.addAllowedOrigin("*");
        config.addExposedHeader("Location");
        config.addExposedHeader("Content-Location");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        CorsInterceptor corsInterceptor = new CorsInterceptor(config);
        registerInterceptor(corsInterceptor);
    }
}