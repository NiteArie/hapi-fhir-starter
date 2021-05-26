package com.healthcare.lab.hapistarter.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationConfig {

    @Value("${openemr.token_url}")
    String tokenUrl;
    @Value("${openemr.api_url}")
    String apiUrl;
    @Value("${openemr.client_id}")
    String clientID;
    @Value("${openemr.grant_type}")
    String grantType;
    @Value("${openemr.user_role}")
    String userRole;
    @Value("${openemr.scope}")
    String scope;
    @Value("${openemr.username}")
    String username;
    @Value("${openemr.password}")
    String password;

    String accessToken;

}