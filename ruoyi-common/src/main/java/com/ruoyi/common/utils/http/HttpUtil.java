package com.ruoyi.common.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

public class HttpUtil {


    private static PoolingHttpClientConnectionManager connectionPool;
    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static final String UTF_8 = "UTF-8";
    public static final String TRANSTYPE_JSON = "application/json";

    private static void init() {
        if (connectionPool == null) {
            connectionPool = new PoolingHttpClientConnectionManager();
            // MaxTotal 值不应该太大
            connectionPool.setMaxTotal(500);
            // DefaultMaxPerRoute 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
            // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
            connectionPool.setDefaultMaxPerRoute(connectionPool.getMaxTotal());// （目前只有一个路由，因此让他等于最大值）
        }
    }

    private static CloseableHttpClient httpClient;

    private static CloseableHttpClient getHttpClient() {
        if (Objects.isNull(httpClient)) {
            init();
            httpClient = HttpClients.custom()
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                    .setConnectionManager(connectionPool).build();
        }

        return httpClient;
    }

    public static RespEntity request(String requestType, String url, Map<String, Object> headers, Map<String, Object> params, String jsonData) {
        RespEntity result = null;
        switch (requestType) {
            case "GET":
                result = get(url, headers, params);
                break;
            case "POST":
                result = post(url, headers, params, jsonData);
                break;
            case "PUT":
                result = put(url, headers, params, jsonData);
                break;
            case "PATCH":
                result = patch(url, headers, jsonData);
                break;
            case "DELETE":
                result = delete(url, headers, jsonData);
                break;
            default:
                log.error("无效的请求方式===={}!", requestType);
                break;
        }
        return result;
    }

    private static RespEntity get(String url, Map<String, Object> headers, Map<String, Object> params) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(url);
        if (null != params) {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            uriBuilder.setParameters(pairs);
        }
        HttpGet httpGet = null;
        try {
            String urlString = uriBuilder.build().toString();
            urlString = urlString.startsWith("/") ? urlString.substring(1) : urlString;
            httpGet = new HttpGet(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return RespEntity.failure(e.getMessage());
        }
        if (null != headers) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }
        return getResult(httpGet);
    }

    private static RespEntity post(String url, Map<String, Object> headers, Map<String, Object> params, String data) {
        HttpPost httpPost = new HttpPost(url);
        if (null != headers) {
            for (Map.Entry<String, Object> header : headers.entrySet()) {
                httpPost.addHeader(header.getKey(), String.valueOf(header.getValue()));
            }
        }
        if (null != params) {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return RespEntity.failure(e.getMessage());
            }
        }
        if (null != data) {
            StringEntity entity = new StringEntity(data, "UTF-8");
            entity.setContentType("application/json");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, TRANSTYPE_JSON));
            httpPost.setEntity(entity);
        }
        return getResult(httpPost);
    }

    private static RespEntity put(String url, Map<String, Object> headers, Map<String, Object> params, String data) {
        HttpPut httpPost = new HttpPut(url);
        if (null != headers) {
            for (Map.Entry<String, Object> header : headers.entrySet()) {
                httpPost.addHeader(header.getKey(), String.valueOf(header.getValue()));
            }
        }
        if (null != params) {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return RespEntity.failure(e.getMessage());
            }
        }
        if (null != data) {
            StringEntity entity = new StringEntity(data, "UTF-8");
            entity.setContentType("application/json");
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, TRANSTYPE_JSON));
            httpPost.setEntity(entity);
        }
        return getResult(httpPost);
    }

    // private static RespEntity put(String url, Map<String, Object> headers, String data) {
    // HttpPut httpPost = new HttpPut(url);
    // for (Map.Entry<String, Object> param : headers.entrySet()) {
    // httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
    // }
    // StringEntity entity = new StringEntity(data, "UTF-8");
    // entity.setContentType("application/json");
    // entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, TRANSTYPE_JSON));
    // httpPost.setEntity(entity);
    // return getResult(httpPost);
    // }

    private static RespEntity patch(String url, Map<String, Object> headers, String data) {
        HttpPatch httpPost = new HttpPatch(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        StringEntity entity = new StringEntity(data, "UTF-8");
        entity.setContentType("application/json");
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, TRANSTYPE_JSON));
        httpPost.setEntity(entity);
        return getResult(httpPost);
    }

    private static RespEntity delete(String url, Map<String, Object> headers, String data) {
        HttpDelete httpPost = new HttpDelete(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    private static RespEntity getResult(HttpRequestBase request) {
        request.setHeader("Connection", "close");
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).setConnectionRequestTimeout(6000).build();
        request.setConfig(requestConfig);
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            log.debug("STATUSLINE====={}", response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                response.close();
                log.debug("RESULT====={}", result);
                return RespEntity.success(result);
            } else {
                response.close();
                return RespEntity.failure(response.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return RespEntity.failure(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return RespEntity.failure(e.getMessage());
        }
    }
}
