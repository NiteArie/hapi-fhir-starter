package com.healthcare.lab.hapistarter;

import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Data
public class ApplicationHelper {
    private String accessToken;
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

    public void createResources(JSONObject jsonRecord) {
        if (jsonRecord.containsKey("entry")) {

            JSONArray entries = (JSONArray) jsonRecord.get("entry");

            for (int i = 0; i < entries.size(); i++) {
                postResource((JSONObject) entries.get(i));
            }
        }
    }

    public void postResource(JSONObject entry) {
        try {

            if (entry.containsKey("resource") && entry.containsKey("request")) {

                JSONObject requestObject = (JSONObject) entry.get("request");
                String resourceType = requestObject.get("url").toString();

                if (!resourceType.equals("Patient")) {
                    return;
                }

                JSONObject resource = (JSONObject) entry.get("resource");

                CloseableHttpClient client = HttpClients.createDefault();

                HttpPost httpPost = new HttpPost(properties.get("openemr.fhir_url").toString() + resourceType);

                httpPost.setHeader("Authorization", "Bearer " + accessToken);
                httpPost.setEntity(new StringEntity(resource.toString()));

                CloseableHttpResponse response = client.execute(httpPost);

            }

        } catch (IOException error) {
            error.printStackTrace();
        }


    }

    public JSONObject toJSON(String entry) {
        try {

            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(entry);

        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        return new JSONObject();
    }

    public void requestAccessToken() {
        try {
            CloseableHttpClient client = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(properties.get("openemr.token_url").toString());

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair("client_id", properties.get("openemr.client_id").toString()));
            params.add(new BasicNameValuePair("grant_type", properties.get("openemr.grant_type").toString()));
            params.add(new BasicNameValuePair("user_role", properties.get("openemr.user_role").toString()));
            params.add(new BasicNameValuePair("scope", properties.get("openemr.scope").toString()));
            params.add(new BasicNameValuePair("username", properties.get("openemr.username").toString()));
            params.add(new BasicNameValuePair("password", properties.get("openemr.password").toString()));

            httpPost.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse response = client.execute(httpPost);

            System.out.println(response.getStatusLine().getStatusCode());

            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                String bodyContent = EntityUtils.toString(responseEntity);
                System.out.println(bodyContent);

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(bodyContent);

                this.accessToken = json.get("access_token").toString();
            }

            client.close();

        } catch (IOException error) {
            error.printStackTrace();
        } catch (ParseException error) {
            error.printStackTrace();
        }
    }
}
