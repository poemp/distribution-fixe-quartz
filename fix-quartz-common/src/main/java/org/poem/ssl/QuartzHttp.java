package org.poem.ssl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.poem.QuartzInstanceInfo;
import org.poem.ssl.header.QuartzAccept;
import org.poem.ssl.url.OhttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Administrator
 */
@Data
public class QuartzHttp {

    private static final Logger logger = LoggerFactory.getLogger(QuartzHttp.class);

    /**
     * host
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 参数
     */
    private QuartzInstanceInfo params;


    /**
     * post
     *
     * @return
     */
    public String post() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httpPost
        String url = this.host + ":" + this.port + OhttpUrl.SERVER_PATH;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader(QuartzAccept.QUARTZ_ACCEPT_HEADER);
        StringEntity entity = new StringEntity(JSONObject.toJSONString(params), Consts.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {

            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            } else {
                logger.error("请求返回:" + state + "(" + url + ")");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
