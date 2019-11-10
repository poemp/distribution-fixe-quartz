package org.poem.transfer.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
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

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class AbstractTransferConsumers implements TransferConsumers {

    @NacosInjected
    private NamingService namingService;

    /**
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractTransferConsumers.class);

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
        logger.info(" Future Uri : " + uri);
        logger.info(" Headers: " + QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE);
        logger.info(" Content-Type : application/json; charset=utf-8");
    }

    /**
     * @param ip
     * @param port
     */
    private TransferRequest executeHandler(String ip, Integer port, TransferInfo transferInfo) {
        String uri = "http://" + ip + ":" + port + OhttpUrl.EXECUTE_SERVER_PATH;
        HttpHeaders headers = new HttpHeaders();
        headers.add(QuartzAccept.QUARTZ_ACCEPT_HEADER_KEY, QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE);
        headers.add("Content-Type", "application/json; charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(transferInfo), headers);

        log(uri);

        TransferRequest request = restTemplate.
                postForEntity(uri, entity, TransferRequest.class, new Object()).getBody();
        logger.info(" Execute Instance : " + request);
        return request;
    }

    /**
     * 执行
     *
     * @param transferInfo
     * @return
     */
    @Override
    public List<TransferRequest> execute(TransferInfo transferInfo) {
        List<TransferRequest> transferRequests = Lists.newArrayList();
        try {
            List<Instance> instances = namingService.getAllInstances(transferInfo.getAppName());
            for (Instance instance : instances) {
                String ip = instance.getIp();
                Integer port = instance.getPort();
                transferRequests.add(executeHandler(ip, port, transferInfo));
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return transferRequests;
    }
}
