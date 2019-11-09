package org.poem.utils.executor;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.poem.SpringUtils;
import org.poem.transfer.TransferParametersInfo;
import org.poem.transfer.TransferRequest;
import org.poem.utils.exception.ClientException;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class ClientExecutors {

    /**
     * 调用的参数
     */
    private List<TransferParametersInfo> transferParametersInfos;
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
        if (StringUtils.isEmpty(className)) {
            throw new ClientException(" exector class[" + this.className + "] can\'t be empty");
        }
        if (StringUtils.isEmpty(methodName)) {
            throw new ClientException(" exector class[" + this.className + "]  method[ " + this.methodName + "] can\'t be empty");
        }
    }

    /**
     * 得到参数
     *
     * @return
     */
    private List<Class<?>> getMethodParsClass() throws ClassNotFoundException {
        List<Class<?>> parameterTypes = Lists.newArrayList();
        if (CollectionUtils.isEmpty(this.getTransferParametersInfos())) {
            return parameterTypes;
        }
        for (TransferParametersInfo transferParametersInfo : this.getTransferParametersInfos()) {
            Class<?> clazz = Class.forName(transferParametersInfo.getClassName());
            parameterTypes.add(clazz);
        }
        return parameterTypes;
    }

    /**
     * 获取参数的值
     *
     * @return
     */
    private List<Object> getMethodParasValue() {
        List<Object> values = Lists.newArrayList();
        for (TransferParametersInfo transferParametersInfo : this.getTransferParametersInfos()) {
            values.add(transferParametersInfo.getValue());
        }
        return values;
    }

    /**
     * 执行方法
     *
     * @return
     * @throws ClientException
     */
    public TransferRequest executor() throws ClientException {
        this.validate();
        try {
            List<Class<?>> parts = getMethodParsClass();
            Class<?> clazz = Class.forName(this.className);
            Method method;
            Object ser = SpringUtils.getBean(clazz);
            if (CollectionUtils.isEmpty(parts)) {
                method = clazz.getMethod(methodName);
                return TransferRequest.returnObject(method.invoke(ser)).build();
            } else {
                Class[] classes = parts.toArray(new Class[0]);
                method = clazz.getMethod(methodName, classes);
                List<Object> values = getMethodParasValue();
                return TransferRequest.returnObject(method.invoke(ser, values.toArray(new Object[0]))).build();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return TransferRequest.throwable(new ClientException(e.getMessage())).build();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return TransferRequest.throwable(new ClientException(e.getMessage())).build();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return TransferRequest.throwable(new ClientException(e.getMessage())).build();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return TransferRequest.throwable(new ClientException(e.getMessage())).build();
        }
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
