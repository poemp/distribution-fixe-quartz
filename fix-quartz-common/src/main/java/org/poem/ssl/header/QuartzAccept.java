package org.poem.ssl.header;


import org.apache.http.message.BasicHeader;

/**
 * @author poem
 */
public class QuartzAccept {

    /**
     * header key
     */
    public static final String QUARTZ_ACCEPT_HEADER_KEY = "quartz_accept_header";


    /**
     * header value
     */
    public static final String QUARTZ_ACCEPT_HEADER_VALUE = "QUARTZ_ACCEPT_HEADER";
    /**
     * 支持的文件头
     */
    public static final BasicHeader QUARTZ_ACCEPT_HEADER = new BasicHeader( QUARTZ_ACCEPT_HEADER_KEY, QUARTZ_ACCEPT_HEADER_VALUE );
}
