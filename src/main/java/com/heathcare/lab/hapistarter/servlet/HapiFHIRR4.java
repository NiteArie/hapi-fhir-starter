package com.heathcare.lab.hapistarter.servlet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.*;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import com.heathcare.lab.hapistarter.providers.PatientResourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.cors.CorsConfiguration;

import java.io.Serial;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = { "/r4/*" }, displayName = "Hapi FHIR Server - Version R4")
public class HapiFHIRR4 extends RestfulServer {

  @Autowired
  AutowireCapableBeanFactory beanFactory;

  @Serial
  private static final long serialVersionUID = 1L;

  @Override
  protected void initialize() throws ServletException {
    setFhirContext(FhirContext.forR4());

    // General Configuration
    setCopyright("KMS Technology - Heathcare DC");
    setServerVersion("1.0");
    setDefaultPrettyPrint(true);
    setDefaultPreferReturn(PreferReturnEnum.MINIMAL);
    setDefaultResponseEncoding(EncodingEnum.JSON);

    // Resource Configuration
    var patientResource = new PatientResourceProvider();
    beanFactory.autowireBean(patientResource);

    setResourceProviders(patientResource);

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
