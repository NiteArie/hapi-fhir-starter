package com.healthcare.toolsgenpostgres;

import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class ApplicationHelper {

    private Properties properties;

    public void loadProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        properties = new Properties();
        try {
            InputStream inputStream = loader.getResourceAsStream("application.properties");
            properties.load(inputStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void createResources(String jsonRecord) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(properties.get("postgres.fhir_url").toString());
        httpPost.setEntity(new StringEntity(jsonRecord));
        httpPost.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
    }
}
