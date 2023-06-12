package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

public class HttpUtils {
    private HttpUtils() {
        throw new IllegalStateException("HttpUtils class");
    }

    private static final String CHARACTER_ENCODING = "UTF-8";

    private static Gson gson = new GsonBuilder().create();

    public static <T> T httpGetWithJwt(String url, List<NameValuePair> headersExtra, String jwt, Class<T> r) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet request = new HttpGet(url);
            if(headersExtra!=null && !headersExtra.isEmpty()) {
                for(NameValuePair head : headersExtra) request.setHeader(head.getName(), head.getValue());
            }
            if(jwt!=null) request.setHeader("Authorization", "Bearer "+jwt);

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseString = EntityUtils.toString(response.getEntity(), CHARACTER_ENCODING);
                return responseString.isEmpty() ? null : gson.fromJson(responseString, r);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T httpPostWithJwt(String url, List<NameValuePair> headersExtra, String jwt, Object t, Class<T> r) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        StringEntity params = new StringEntity(gson.toJson(t), CHARACTER_ENCODING);

        request.addHeader("content-type", "application/json");
        if(headersExtra!=null && !headersExtra.isEmpty()) {
            for(NameValuePair head : headersExtra) request.setHeader(head.getName(), head.getValue());
        }
        if(jwt!=null) request.setHeader("Authorization", "Bearer "+jwt);

        request.setEntity(params);
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            HttpEntity result = response.getEntity();
            String responseString = EntityUtils.toString(result, CHARACTER_ENCODING);
            if (response.getStatusLine().getStatusCode() == 200) {
                return responseString.isEmpty() ? null : gson.fromJson(responseString, r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T> T getWithHeadersResponseJsonIgnoreSSL(String url, List<NameValuePair> headers, Class<T> r) throws KeyStoreException, NoSuchAlgorithmException, IOException, HttpException, KeyManagementException {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        HostnameVerifier hnv = new NoopHostnameVerifier();
        SSLConnectionSocketFactory sslcf = new SSLConnectionSocketFactory(sslContext, hnv);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslcf)
                                                    .setDefaultRequestConfig(RequestConfig.custom()
                                                    .setCookieSpec(CookieSpecs.STANDARD).build())
                                                    .build();

        // building http client
//        HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpGet request = new HttpGet(url);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if(headers!=null && !headers.isEmpty()) {
            for(NameValuePair head : headers) request.setHeader(head.getName(), head.getValue());
        }

        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() != 200) throw new HttpException("status code error");

        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "utf-8");
        return gson.fromJson(responseString, r);
    }
}
