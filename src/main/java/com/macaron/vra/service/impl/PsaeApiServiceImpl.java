package com.macaron.vra.service.impl;

import com.macaron.vra.util.HttpClientUtils;
import com.macaron.vra.util.JacksonUtils;
import com.macaron.vra.vo.PsaeVo;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class PsaeApiServiceImpl {


    private static final Logger logger = LoggerFactory.getLogger(PsaeApiServiceImpl.class);

    PsaeVo psaeVo;

    private String psaeBaseRestUrl;
    private String userToken;

    private String getUserToken() {
        return userToken;
    }

    public PsaeApiServiceImpl(PsaeVo vo) throws Exception {
        this.psaeVo = vo;
        this.psaeBaseRestUrl = String.format("%s://%s:%d/PSAE/rest/v1", psaeVo.getProtocol(), psaeVo.getIp(), psaeVo.getPort());
        logger.info(" psaeBaseRestUrl={}", psaeBaseRestUrl);
        accessToken();
    }


    private Map<String, Object> accessToken() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Map<String, Object> map = null;
        InputStream is = null;
        try {
            HttpClient client = HttpClientUtils.getHttpClient();

            String url = new StringBuilder()
                    .append(this.psaeBaseRestUrl)
                    .append(new MessageFormat("/user/{0}/login").format(new Object[]{psaeVo.getUser()}))
                    .toString();

            HttpPost post = HttpClientUtils.getHttpPostMethod(url);
            HttpClientUtils.addHeaderContentJson(post);
            Map<String, String> params = new HashMap<String, String>();
            params.put("passWord", psaeVo.getPassword());
            StringEntity entity = new StringEntity(JacksonUtils.objectToJsonStr(params));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            is = response.getEntity().getContent();
            String content = IOUtils.toString(is, StandardCharsets.UTF_8);

            map = JacksonUtils.jsonStrToMap(content);
            this.userToken = map.get("userToken").toString();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return map;

    }

    private StringEntity getTokenEntity() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        String json = "{\"userToken\":\"" + userToken + "\"}";
        StringEntity params = new StringEntity(json);
        return params;
    }

    public Map<String, Object> getDimission() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Map<String, Object> map = null;
        InputStream is = null;
        try {

            HttpClient client = HttpClientUtils.getHttpClient();

            String url = new StringBuilder()
                    .append(this.psaeBaseRestUrl)
                    .append("/task/dimensions")
                    .toString();

            HttpClientUtils.HttpGet get = HttpClientUtils.getHttpGetMethod(url);
            get.setEntity(this.getTokenEntity());
            HttpResponse response = client.execute(get);

            is = response.getEntity().getContent();
            String content = IOUtils.toString(is, StandardCharsets.UTF_8);

            map = JacksonUtils.jsonStrToMap(content);

        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return map;
    }

    public Map<String, Object> getModels() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Map<String, Object> map = null;
        InputStream is = null;
        try {

            HttpClient client = HttpClientUtils.getHttpClient();

            String url = new StringBuilder()
                    .append(this.psaeBaseRestUrl)
                    .append("/models")
                    .toString();

            HttpClientUtils.HttpGet get = HttpClientUtils.getHttpGetMethod(url);
            get.setEntity(this.getTokenEntity());
            HttpResponse response = client.execute(get);

            is = response.getEntity().getContent();
            String content = IOUtils.toString(is, StandardCharsets.UTF_8);

            map = JacksonUtils.jsonStrToMap(content);

        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return map;
    }

    public Map<String, Object> getAnalysisResultByTaskId(String taskId) throws Exception {
        return this.getAnalysisResultByTaskId(taskId, "modelAnalyzeResults");
    }


    public Map<String, Object> getAnalysisResultByTaskId(String taskId, String attachTaskFields) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        InputStream is = null;
        try {
            HttpClient client = HttpClientUtils.getHttpClient();
            String url = new StringBuilder()
                    .append(this.psaeBaseRestUrl)
                    .append("/analyzeResults/task/" + taskId)
                    .toString();
            HttpClientUtils.HttpGet get = HttpClientUtils.getHttpGetMethod(url);
            get.setURI(URI.create(url));
            get.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));
            String json = "{\"userToken\":\"" + userToken + "\",\"accessKey\":\"" + getAccessKey() + "\",\"attachTaskFields\":\"" + attachTaskFields + "\",\"attachSpeechFields\":\"SpeechPath\",\"containKeyword\":true}";
            StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
            get.setEntity(params);
            HttpResponse response = client.execute(get);

            is = response.getEntity().getContent();
            String content = IOUtils.toString(is, StandardCharsets.UTF_8);
            map = JacksonUtils.jsonStrToMap(content);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return map;
    }

    public Map<String, Object> getKeywordContextByTaskId(String taskId, String keyWord, int begin, int end) throws Exception {
        Map<String, Object> map;
        InputStream is = null;
        try {

            HttpClient client = HttpClientUtils.getHttpClient();
            String url = new StringBuilder()
                    .append(this.psaeBaseRestUrl)
                    .append("/task/" + taskId + "/keywordContext")
                    .toString();
            HttpClientUtils.HttpGet get = HttpClientUtils.getHttpGetMethod(url);
            get.setURI(URI.create(url));
            get.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));

            String json = "{\"userToken\":\"" + userToken + "\",\"accessKey\":\"" + getAccessKey() + "\",\"keyword\":\"" + keyWord + "\",\"begin\":" + begin + ",\"end\":" + end + "}";
            StringEntity params = new StringEntity(json, ContentType.APPLICATION_JSON);
            get.setEntity(params);
            HttpResponse response = client.execute(get);

            is = response.getEntity().getContent();
            String content = IOUtils.toString(is, StandardCharsets.UTF_8);
            // logger.info("KeywordContextByTaskId taskId ={},result={}",taskId,content);
            map = JacksonUtils.jsonStrToMap(content);
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return map;
    }


    private String getAccessKey() throws Exception {
        Map<String, Object> map = null;
        InputStream is = null;
        String accessKey = "";
        try {
            HttpClient client = HttpClientUtils.getHttpClient();

            String url = new StringBuilder()
                    .append(this.psaeBaseRestUrl)
                    .append("/accessKey")
                    .toString();

            HttpPost post = HttpClientUtils.getHttpPostMethod(url);
            HttpClientUtils.addHeaderContentJson(post);
            Map<String, String> params = new HashMap<String, String>();
            params.put("userToken", this.userToken);
            StringEntity entity = new StringEntity(JacksonUtils.objectToJsonStr(params));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            is = response.getEntity().getContent();
            String content = IOUtils.toString(is, StandardCharsets.UTF_8);

            map = JacksonUtils.jsonStrToMap(content);
            if (map.get("functionResult").toString().equalsIgnoreCase("SUCCESS")) {
                Map<String, Object> _accessKey = (Map<String, Object>) map.get("accessKey");

                accessKey = _accessKey.get("accessKey").toString();
            } else {
                throw new Exception("獲取 aceessKey 失敗");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }
        return accessKey;
    }
}
