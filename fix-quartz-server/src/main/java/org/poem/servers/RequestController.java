package org.poem.servers;

import org.poem.QuartzInstanceInfo;
import org.poem.nt.ResultVO;
import org.poem.repository.InstanceInfoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/v1/request")
public class RequestController {


    @RequestMapping("/getAll")
    public ResultVO<List<QuartzInstanceInfo>> getAll() {
        return new ResultVO<>( 0, InstanceInfoRepository.getAllInstanceInfo() );
    }
}
