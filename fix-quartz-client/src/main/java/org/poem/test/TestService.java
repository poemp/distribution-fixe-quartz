package org.poem.test;

import org.poem.annotation.QuartzMethod;
import org.poem.annotation.QuartzService;
import org.springframework.stereotype.Service;

/**
 * @author poem
 */
@Service
@QuartzService(name = "01-测试服务")
public class TestService {

    /**
     * @param args
     */
    @QuartzMethod(name = "测试方法")
    public void testMethod(String args) {
        System.out.println( "distribution hello word ." );
    }


    /**
     *
     */
    @QuartzMethod(name = "主方法")
    public void main() {
        System.out.println( "主方法 ." );
    }
}
