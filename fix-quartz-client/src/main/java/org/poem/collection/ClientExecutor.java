package org.poem.collection;

import org.poem.ssl.header.QuartzAccept;
import org.poem.ssl.url.OhttpUrl;
import org.poem.transfer.TransferInfo;
import org.poem.transfer.TransferRequest;
import org.poem.utils.exception.ClientException;
import org.poem.utils.executor.ClientExecutors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("")
public class ClientExecutor {


    /**
     * 获取参数
     *
     * @param info 提交的参数
     * @return
     */
    @PostMapping(value = OhttpUrl.EXECUTE_SERVER_PATH, headers = {QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE})
    @Consumes({"application/json; charset=utf-8"})
    public TransferRequest metadata(@RequestBody TransferInfo info, HttpServletRequest request) {
        ClientExecutors executors = new ClientExecutors();
        try {
            executors.setClassName( info.getClassName() );
            executors.setMethodName( info.getMethodName() );
            executors.setQuartzServiceMethodParms( info.getQuartzServiceMethodParms() );
            return executors.executor();
        } catch (ClientException e) {
            e.printStackTrace();
            return TransferRequest.throwable( new ClientException( e.getMessage() ) ).build();
        }
    }
}
