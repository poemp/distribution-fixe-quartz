package org.poem;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.poem.vo.QuartzServiceClass;

import java.util.List;

/**
 * @author poem
 */
@Data
public class QuartzInstanceInfo {

    /**
     * id
     */
    private String id;
    /**
     * hostname
     */
    private String hostName;

    /**
     * appname
     */
    private String appName;

    /**
     * 获取的数据
     */
    private List<QuartzServiceClass> quartzServiceClasses;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
