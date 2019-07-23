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
     * ip
     */
    private String ip;
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

    private   QuartzInstanceInfo(){

    }

    private QuartzInstanceInfo(String ip, String hostName, String appName, List<QuartzServiceClass> quartzServiceClasses) {
        this.ip = ip;
        this.hostName = hostName;
        this.appName = appName;
        this.quartzServiceClasses = quartzServiceClasses;
    }

    private static Builder INFO;


    private static Builder instance(){
        if (INFO ==null){
            INFO = new Builder();
        }
        return INFO;
    }


    public static class Builder{
        /**
         * ip
         */
        private String ip;
        /**
         * hostname
         */
        private String hostName;

        /**
         * appname
         */
        private String appName;

        private List<QuartzServiceClass> quartzServiceClasses;

        public static QuartzInstanceInfo.Builder ip(String ip){
            QuartzInstanceInfo.Builder builder = instance();
            builder.ip = ip;
            return builder;
        }

        public  QuartzInstanceInfo.Builder hostName(String hostName){
            QuartzInstanceInfo.Builder builder = instance();
            builder.hostName = hostName;
            return builder;
        }
        public  QuartzInstanceInfo.Builder appName(String appName){
            QuartzInstanceInfo.Builder builder = instance();
            builder.appName = appName;
            return builder;
        }

        public  QuartzInstanceInfo.Builder quartzServiceClasses(List<QuartzServiceClass> quartzServiceClasses){
            QuartzInstanceInfo.Builder builder = instance();
            builder.quartzServiceClasses = quartzServiceClasses;
            return builder;
        }

        public  QuartzInstanceInfo build(){
            return new QuartzInstanceInfo(INFO.ip, INFO.hostName, INFO.appName, INFO.quartzServiceClasses);
        }
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
