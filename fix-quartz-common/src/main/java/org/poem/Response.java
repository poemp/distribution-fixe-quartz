package org.poem;

/**
 * @author poem
 */
public class Response {

    private Integer status;

    private String message;

    private Response(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private static volatile ResponseBuilder rd = null;

    private static ResponseBuilder getInstance() {
        ResponseBuilder builder = rd;
        if (builder == null) {
            builder = new ResponseBuilder();
        }
        return builder;
    }

    public static Response.ResponseBuilder status(Integer status) {
        if (status == null) {
            throw new IllegalArgumentException();
        }
        Response.ResponseBuilder builder = getInstance();
        builder.status = status;
        return builder;
    }

    public static Response.ResponseBuilder message(String message) {

        Response.ResponseBuilder builder = getInstance();
        builder.message = message;
        return builder;
    }


    public static class ResponseBuilder {

        private Integer status;

        private String message;


        public Integer getStatus() {
            return status;
        }

        public ResponseBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public Response builder() {
            return new Response(this.status, this.message);
        }


    }
}
