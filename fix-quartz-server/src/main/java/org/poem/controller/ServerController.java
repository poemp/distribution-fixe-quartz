package org.poem.controller;

import org.apache.commons.lang.StringUtils;
import org.poem.QuartzInstanceInfo;
import org.poem.Response;
import org.poem.ssl.header.QuartzAccept;
import org.poem.ssl.url.OhttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;

/**
 * @author poem
 */
@RestController
@RequestMapping("")
public class ServerController {


    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);

    /**
     * 获取参数
     *
     * @param info
     * @return
     */
    @PostMapping(value = OhttpUrl.serverPath, headers = {QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE})
    @Consumes({"application/json"})
    public Response metadata(@RequestBody QuartzInstanceInfo info) {
        // validate that the instanceinfo contains all the necessary required fields
        // 验证Instance实例的所有必填字段
        if (StringUtils.isEmpty(info.getId())) {
            return Response.status(400).message("Missing instanceId").builder();
        } else if (StringUtils.isEmpty(info.getHostName())) {
            return Response.status(400).message("Missing hostname").builder();
        } else if (StringUtils.isEmpty(info.getAppName())) {
            return Response.status(400).message("Missing appName").builder();
        } else if (info.getQuartzServiceClasses() == null) {
            return Response.status(400).message("Missing dataCenterInfo").builder();
        }
        logger.info("%s", info);
        return Response.status(200).message("request is ok ").builder();
    }
}
