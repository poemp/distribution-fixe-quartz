package org.poem.collection;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.poem.annotation.QuartzMethod;
import org.poem.annotation.QuartzService;
import org.poem.utils.SpringContextHolder;
import org.poem.utils.exception.ParameterException;
import org.poem.vo.QuartzServiceClass;
import org.poem.vo.QuartzServiceMethod;
import org.poem.vo.QuartzServiceMethodParms;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeArray.join;

/**
 * 执行期
 * @author poem
 */

@Data
public class QuartzCollections {

    /**
     *
     */
    private static List<QuartzServiceClass> quartzServiceClasses;


    /**
     *
     */
    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * init
     * get all this class and  the method
     */

    public static void init() {
        String[] beans = SpringContextHolder.getApplicationContext().getBeanDefinitionNames();
        List<QuartzServiceClass> quartzServiceClasses = Lists.newArrayList();
        QuartzServiceClass quartzServiceClass;
        for (String beanName : beans) {
            Class<?> beanType = SpringContextHolder.getApplicationContext().getType(beanName);
            QuartzService quartzService = beanType.getAnnotation(QuartzService.class);
            if (quartzService == null) {
                continue;
            }
            quartzServiceClass = new QuartzServiceClass();
            String name = quartzService.name();
            if (StringUtils.isEmpty(name)) {
                quartzServiceClass.setClassName(name);
            }
            quartzServiceClass.setAClass(beanType);
            quartzServiceClass.setClassName(beanType.getName());
            quartzServiceClass.setQuartzServiceMethodList(getMethod(beanType));
            quartzServiceClasses.add(quartzServiceClass);
        }
        QuartzCollections.quartzServiceClasses = quartzServiceClasses;
    }


    /**
     * get the task
     *
     * @param beanType
     * @return
     */
    private static List<QuartzServiceMethod> getMethod(Class<?> beanType) {
        Method[] methods = beanType.getMethods();
        List<QuartzServiceMethod> quartzServiceMethods = Lists.newArrayList();
        for (Method m : methods) {
            if (null != m.getAnnotation(QuartzMethod.class)) {
                QuartzServiceMethod method = new QuartzServiceMethod();
                method.setMethod(method.getMethodName());
                method.setMethodName(m.getAnnotation(QuartzMethod.class).name());
                method.setQuartzServiceMethodParmsList((getMethodParms(m)));
                quartzServiceMethods.add(method);
            }
        }
        return quartzServiceMethods;
    }


    /**
     * 获取参数
     *
     * @param method 参数信息
     * @return
     */
    private static List<QuartzServiceMethodParms> getMethodParms(Method method) {
        List<QuartzServiceMethodParms> parms = Lists.newArrayList();
        String[] param = discoverer.getParameterNames(method);
        Annotation[][] annotateds = method.getParameterAnnotations();
        Annotation[] annotations;
        Class[] paramClazzs = method.getParameterTypes();
        if (param.length != paramClazzs.length && annotateds.length != paramClazzs.length) {
            try {
                throw new ParameterException(String.format("Illegal registration for paramter '%s': All Paramter Must Has Option:@ShellOptions", method.getName()));
            } catch (ParameterException e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < param.length; i++) {
                annotations = annotateds[i];
                parms.add(new QuartzServiceMethodParms(param[i], paramClazzs[i], join("", annotations)));
            }
        }
        return parms;
    }
}
