package org.poem.transfer.impl;

import com.alibaba.fastjson.JSONObject;
import org.poem.QuartzInstanceInfo;
import org.poem.ssl.header.QuartzAccept;
import org.poem.ssl.url.OhttpUrl;
import org.poem.transfer.TransferConsumers;
import org.poem.transfer.TransferInfo;
import org.poem.transfer.TransferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Administrator
 */
@Service
public class AbstractTransferConsumers implements TransferConsumers {

    /**
     *
     */
    private static final Logger logger = LoggerFactory.getLogger( AbstractTransferConsumers.class );

    /**
     *
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 日志
     *
     * @param uri
     */
    private void log(String uri) {
        logger.info( " Future Uri : " + uri );
        logger.info( " Headers: " + QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE );
        logger.info( " Content-Type : application/json; charset=utf-8" );
    }
    /**
     * 执行
     * @param transferInfo
     * @return
     */
    @Override
    public TransferRequest execute(TransferInfo transferInfo) {
        String uri = "http://" + transferInfo.getAppName() + ":" + transferInfo.getPort() + OhttpUrl.EXECUTE_SERVER_PATH;
        HttpHeaders headers = new HttpHeaders();
        headers.add( QuartzAccept.QUARTZ_ACCEPT_HEADER_KEY, QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE );
        headers.add( "Content-Type", "application/json; charset=utf-8" );
        HttpEntity<String> entity = new HttpEntity<>( JSONObject.toJSONString(transferInfo ), headers );

        log( uri );

        TransferRequest request = restTemplate.
                postForEntity( uri, entity, TransferRequest.class, new Object() ).getBody();
        logger.info( " Registered instance : " + request );
        return request;
    }
}
