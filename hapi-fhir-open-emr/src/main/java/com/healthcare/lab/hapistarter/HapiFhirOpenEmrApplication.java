package com.healthcare.lab.hapistarter;

import com.healthcare.lab.hapistarter.configurations.ApplicationConfig;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ServletComponentScan
public class HapiFhirOpenEmrApplication {

    public static void main(String[] args) {
        SpringApplication.run(HapiFhirOpenEmrApplication.class, args);
    }


    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    CommandLineRunner initAccessToken() {
        return args -> {
            try {
                ApplicationConfig config = applicationContext.getBean(ApplicationConfig.class);

                CloseableHttpClient client = HttpClients.createDefault();

                HttpPost httpPost = new HttpPost( config.getTokenUrl());

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("client_id", config.getClientID()));
                params.add(new BasicNameValuePair("grant_type", config.getGrantType()));
                params.add(new BasicNameValuePair("user_role", config.getUserRole()));
                params.add(new BasicNameValuePair("scope", config.getScope()));
                params.add(new BasicNameValuePair("username", config.getUsername()));
                params.add(new BasicNameValuePair("password", config.getPassword()));

                httpPost.setEntity(new UrlEncodedFormEntity(params));

                CloseableHttpResponse response = client.execute(httpPost);

                System.out.println(response.getStatusLine().getStatusCode());

                HttpEntity responseEntity = response.getEntity();

                if (responseEntity != null) {
                    String bodyContent = EntityUtils.toString(responseEntity);
                    System.out.println(bodyContent);

                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(bodyContent);

                    config.setAccessToken(json.get("access_token").toString());
                }

                client.close();

            } catch (IOException error) {
                error.printStackTrace();
            } catch (ParseException error) {
                error.printStackTrace();
            }
        };
    }
}
