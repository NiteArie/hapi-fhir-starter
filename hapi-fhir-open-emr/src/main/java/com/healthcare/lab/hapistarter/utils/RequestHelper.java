package com.healthcare.lab.hapistarter.utils;

import com.healthcare.lab.hapistarter.configurations.ApplicationConfig;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RequestHelper {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Value("${openemr.api_url}")
    private String baseURL;

    public JSONObject getAll(String resourceType) {
        try {

            CloseableHttpClient client = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(baseURL + resourceType);

            httpGet.setHeader("Authorization", applicationConfig.getAccessToken());

            CloseableHttpResponse response = client.execute(httpGet);

            HttpEntity responseEntity = response.getEntity();

            String bodyContent = EntityUtils.toString(responseEntity);

            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(bodyContent);

            client.close();

            return json;

        } catch (IOException error) {
            error.printStackTrace();
        } catch (ParseException error) {
            error.printStackTrace();
        }

        return new JSONObject();
    }

    public JSONObject getResource(String resourceType, String id) {
        try {

            CloseableHttpClient client = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(baseURL + resourceType + "/" + id);

            httpGet.addHeader("Authorization", "Bearer " + applicationConfig.getAccessToken());

            CloseableHttpResponse response = client.execute(httpGet);

            HttpEntity responseEntity = response.getEntity();

            String bodyContent = EntityUtils.toString(responseEntity);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(bodyContent);

            client.close();

            return json;

        } catch (IOException error) {
            error.printStackTrace();
        } catch (ParseException error) {
            error.printStackTrace();
        }

        return new JSONObject();
    }

    public JSONObject createResource(String resourceType, List<NameValuePair> params) {
        try {

            CloseableHttpClient client = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(baseURL + resourceType);

            httpPost.addHeader("Authorization", "Bearer " + applicationConfig.getAccessToken());
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            CloseableHttpResponse response = client.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();

            String bodyContent = EntityUtils.toString(responseEntity);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(bodyContent);

            client.close();

            return json;


        } catch (IOException error) {
            error.printStackTrace();
        } catch (ParseException error) {
            error.printStackTrace();
        }
        return new JSONObject();
    }

    public JSONObject createResourceOnAResourceWithJSON(
            String firstResourceType,
            String firstResourceID,
            String secondResourceType,
            JSONObject bodyJSON
    ) {
        try {

            CloseableHttpClient client = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(
                    baseURL + firstResourceType + "/" + firstResourceID + "/" + secondResourceType
            );

            httpPost.addHeader("Authorization", applicationConfig.getAccessToken());

            httpPost.setEntity(new StringEntity(bodyJSON.toString()));

            CloseableHttpResponse response = client.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();

            String bodyContent = EntityUtils.toString(responseEntity);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(bodyContent);

            client.close();

            return json;

        } catch (IOException error) {
            error.printStackTrace();
        } catch (ParseException error) {
            error.printStackTrace();
        }

        return new JSONObject();
    }

    public JSONObject createResourceWithJSON(String resourceType, JSONObject bodyJSON) {
        try {

            CloseableHttpClient client = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost(baseURL + resourceType);

            httpPost.addHeader("Authorization", "Bearer " + applicationConfig.getAccessToken());

            httpPost.setEntity(new StringEntity(bodyJSON.toString()));

            CloseableHttpResponse response = client.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();

            String bodyContent = EntityUtils.toString(responseEntity);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(bodyContent);

            client.close();

            return json;


        } catch (IOException error) {
            error.printStackTrace();
        } catch (ParseException error) {
            error.printStackTrace();
        }
        return new JSONObject();
    }

    public JSONObject updateResourceWithJSON(String resourceType, String id, JSONObject bodyJSON) {
        try {

            CloseableHttpClient client = HttpClients.createDefault();

            HttpPut httpPut = new HttpPut(baseURL + resourceType + "/" + id);

            httpPut.addHeader("Authorization", "Bearer " + applicationConfig.getAccessToken());

            httpPut.setEntity(new StringEntity(bodyJSON.toString()));

            CloseableHttpResponse response = client.execute(httpPut);

            HttpEntity responseEntity = response.getEntity();

            String bodyContent = EntityUtils.toString(responseEntity);

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(bodyContent);

            client.close();

            return json;


        } catch (IOException error) {
            error.printStackTrace();
        } catch (ParseException error) {
            error.printStackTrace();
        }
        return new JSONObject();
    }

}
