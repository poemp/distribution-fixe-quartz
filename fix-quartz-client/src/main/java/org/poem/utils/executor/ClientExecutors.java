package org.poem.utils.executor;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.poem.SpringUtils;
import org.poem.instanceinfo.QuartzServiceMethodParms;
import org.poem.transfer.TransferRequest;
import org.poem.utils.exception.ClientException;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Administrator
 */
public class ClientExecutors {

    /**
     * 调用的参数
     */
    private List<QuartzServiceMethodParms> quartzServiceMethodParms;
    /**
     * 调用的类
     */
    private String className;
    /**
     * 调用方法
     */
    private String methodName;

    /**
     * 执行验证
     *
     * @throws ClientException
     */
    private void validate() throws ClientException {
        if (StringUtils.isEmpty( className )) {
            throw new ClientException( " exector class[" + this.className + "] can\'t be empty" );
        }
        if (StringUtils.isEmpty( methodName )) {
            throw new ClientException( " exector class[" + this.className + "]  method[ " + this.methodName + "] can\'t be empty" );
        }
    }

    /**
     * 得到参数
     *
     * @return
     */
    private List<Class<?>> getPart() {
        List<Class<?>> parameterTypes = Lists.newArrayList();
        return parameterTypes;
    }

    /**
     * 执行
     */
    public TransferRequest executor() throws ClientException {
        this.validate();
        List<Class<?>> parts = getPart();
        try {
            Class<?> clazz = Class.forName( this.className );
            Method method;
            Object ser = SpringUtils.getBean( className, clazz );
            if (CollectionUtils.isEmpty( parts )) {
                method = clazz.getMethod( methodName );
            } else {
                Class[] classes = parts.toArray( new Class[0] );
                method = clazz.getMethod( methodName, classes );
            }
            return TransferRequest.returnObject( method.invoke( ser ) ).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return TransferRequest.throwable( new ClientException( e.getMessage() ) ).build();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return TransferRequest.throwable( new ClientException( e.getMessage() ) ).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return TransferRequest.throwable( new ClientException( e.getMessage() ) ).build();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return TransferRequest.throwable( new ClientException( e.getMessage() ) ).build();
        }
    }

    public List<QuartzServiceMethodParms> getQuartzServiceMethodParms() {
        return quartzServiceMethodParms;
    }

    public void setQuartzServiceMethodParms(List<QuartzServiceMethodParms> quartzServiceMethodParms) {
        this.quartzServiceMethodParms = quartzServiceMethodParms;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
