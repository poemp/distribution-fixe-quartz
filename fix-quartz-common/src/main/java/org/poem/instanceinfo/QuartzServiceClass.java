package org.poem.instanceinfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行的server
 *
 * @author poem
 */
@Data
@NoArgsConstructor
public class QuartzServiceClass {

    /**
     * 服务的名字
     */
    private String serverName;

    /**
     * 服务的全名
     */
    private String className;

    /**
     * 服务的class 类型
     */
    private Class aClass;

    /**
     * 方法
     */
    private List<QuartzServiceMethod> quartzServiceMethodList;


}
