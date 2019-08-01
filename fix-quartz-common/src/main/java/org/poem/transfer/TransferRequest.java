package org.poem.transfer;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回日志
 *
 * @author Administrator
 */
@Data
public class TransferRequest implements Serializable {

    private static volatile TransferRequestBuilder builder;
    /**
     * 是否执行成功
     */
    private boolean error;

    /**
     * 返回的信息
     */
    private String message;

    /**
     * 异常信息
     */
    private Throwable throwable;

    /**
     * 返回的值
     */
    private Object returnObject;


    private TransferRequest() {
    }


    private TransferRequest(boolean error, String message, Throwable throwable, Object returnObject) {
        this.error = error;
        this.message = message;
        this.throwable = throwable;
        if (this.throwable != null){
            this.error = false;
        }
        this.returnObject = returnObject;
    }

    /**
     * @return
     */
    private static TransferRequestBuilder instance() {
        TransferRequestBuilder b = builder;
        if (b == null) {
            b = new TransferRequestBuilder();
        }
        return b;
    }

    public static TransferRequest.TransferRequestBuilder error(boolean error) {
        TransferRequest.TransferRequestBuilder builder = instance();
        builder.error = error;
        return builder;
    }

    public static TransferRequest.TransferRequestBuilder message(String message) {
        TransferRequest.TransferRequestBuilder builder = instance();
        builder.message = message;
        return builder;
    }

    public static TransferRequest.TransferRequestBuilder returnObject(Object returnObject) {
        TransferRequest.TransferRequestBuilder builder = instance();
        builder.returnObject = returnObject;
        builder.error = false;
        return builder;
    }

    public static TransferRequest.TransferRequestBuilder throwable(Throwable throwable) {
        TransferRequest.TransferRequestBuilder builder = instance();
        builder.throwable = throwable;
        builder.error = true;
        return builder;
    }


    @Data
    public static class TransferRequestBuilder {
        private boolean error;
        private String message;
        private Throwable throwable;
        private Object returnObject;

        public TransferRequest build() {
            return new TransferRequest( this.error, this.message, this.throwable, this.returnObject );
        }

        private TransferRequestBuilder() {
        }

        public TransferRequestBuilder error(boolean error) {
            this.error = error;
            return this;
        }

        public TransferRequestBuilder message(String message) {
            this.message = message;
            return this;
        }

        public TransferRequestBuilder returnObject(Object returnObject) {
            this.returnObject = returnObject;
            return this;
        }

        public TransferRequestBuilder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }


    }
}
