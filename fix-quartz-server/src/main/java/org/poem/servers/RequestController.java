package org.poem.servers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.poem.QuartzInstanceInfo;
import org.poem.nt.ResultVO;
import org.poem.repository.InstanceInfoRepository;
import org.poem.transfer.TransferConsumers;
import org.poem.transfer.TransferInfo;
import org.poem.transfer.TransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Administrator
 */
@Api(tags = {"服务管理"})
@RestController
@RequestMapping("/v1/server")
public class RequestController {


    @Autowired
    private TransferConsumers transferConsumers;

    /**
     * @return
     */

    @ApiOperation(value = "获取所有的链接请求", notes = "获取所有的链接请求")
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没有填好"),
            @ApiResponse(code = 404, message = "请求路径没有找到"),
            @ApiResponse(code = 500, message = "数据库发生错误")
    })
    @GetMapping(value = "/getAll")
    public ResultVO<List<QuartzInstanceInfo>> getAll() {
        return new ResultVO<>(0, InstanceInfoRepository.getAllInstanceInfo());
    }

    /**
     * @param transferInfo
     * @return
     */
    @ApiOperation(value = "执行引擎", notes = "执行引擎")
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没有填好"),
            @ApiResponse(code = 404, message = "请求路径没有找到"),
            @ApiResponse(code = 500, message = "数据库发生错误")
    })
    @PostMapping(value = "/executor")
    public ResultVO<TransferRequest> executor(@RequestBody TransferInfo transferInfo) {
        return new ResultVO<>(0, this.transferConsumers.execute(transferInfo));
    }
}
