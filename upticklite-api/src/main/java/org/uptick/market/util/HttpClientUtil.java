// +---------------------------------------------------------------------------- 
// | UptickLite is the community version of Uptick NFT marketpalce, including    
// | three major components - service, api and web, powered by Uptick Network    
// | and Loopring. It is available to the Uptick and Loopring dev community      
// | through the open source license.                                            
// | Uptick Network is a busienss grade NFT infrastructure for NFT applications  
// | with multichian and interchain support. Loopring is a zkRollup layer2 on top
// | of Ethereum.                                                                
// +---------------------------------------------------------------------------- 
// | Copyright (c) 2022~2099 Uptick Network (https://uptick.network/)            
// | All rights reserved.                                                        
// +---------------------------------------------------------------------------- 
// | Licensed ( https://www.gnu.org/licenses/gpl-3.0.en.html )                   
// +---------------------------------------------------------------------------- 
// | Author: Uptick Network development team tech@uptickproject.com              
// +---------------------------------------------------------------------------- 
package org.uptick.market.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HttpClientUtil {

    static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static class HttpClientHolder {
        private static final int TIME_OUT = 60;
        private static final HttpClientUtil INSTANCE = new HttpClientUtil();
        private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    public HttpClientUtil() {
    }

    public static HttpClientUtil getInstance() {
        return HttpClientHolder.INSTANCE;
    }


    public String get(String url, Map<String, String> headerMap) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try {
            ResponseBody responseBody = HttpClientHolder.CLIENT.newCall(request).execute().body();
            if (responseBody != null) {
                return responseBody.string();
            }
        } catch (IOException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }


    public String get(String url) {
        return get(url, null);
    }

    public static String post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {

            ResponseBody responseBody = HttpClientHolder.CLIENT.newCall(request).execute().body();
            if (responseBody != null) {
                String bodystr = responseBody.string();

                return bodystr;
            }
        } catch (IOException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }


    public String postForm(String url, Map<String, String> params) {

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            ResponseBody responseBody = HttpClientHolder.CLIENT.newCall(request).execute().body();
            if (responseBody != null) {
                return responseBody.string();
            }
        } catch (IOException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }

}
