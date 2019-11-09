package org.poem.nt.txt;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.poem.nt.NtDataElementVO;
import org.poem.nt.field.NtField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author poem
 */
public class TxtParseUtils {

    private static final Logger logger = LoggerFactory.getLogger(TxtParseUtils.class);

    /**
     * @param outputStream
     * @param tClass
     * @param <T>
     */
    public static <T> void createTemplate(OutputStream outputStream, Class<T> tClass) {
        List<String> fields = getFiled();
        StringBuilder builder = new StringBuilder();
        for (String field : fields) {
            builder.append(field).append(": xxxx ");
        }
        try {
            outputStream.write(builder.toString().getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 获取列的注释
     *
     * @return
     */
    private static List<String> getFiled() {
        Class clazz = NtDataElementVO.class;
        List<String> stringList = Lists.newArrayList();
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            NtField ntField = field.getAnnotation(NtField.class);
            if (ntField != null) {
                stringList.add(ntField.name());
            }
        }
        return stringList;
    }

    /**
     * @param fieldName
     * @return
     */
    public static String toUpperCaseFirst(String fieldName) {
        StringBuilder stringBuffer = new StringBuilder();
        char[] chars = fieldName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0) {
                stringBuffer.append(String.valueOf(chars[i]).toUpperCase());
            } else {
                stringBuffer.append(String.valueOf(chars[i]));
            }
        }
        return stringBuffer.toString();
    }


    /**
     * @param line
     * @return
     */
    private static String subString(String line) {
        String str;
        int findexA = line.indexOf(":");
        int findex = line.indexOf("：");
        if (findexA > -1) {
            str = line.substring(findexA + 1);
        } else if (findex > -1) {
            str = line.substring(findex + 1);
        } else {
            str = line;
        }
        return str;
    }

    /**
     * 读取文字
     *
     * @param name
     * @return
     */
    public static List<NtDataElementVO> read(String name) {
        // 使用ArrayList来存储每行读取到的字符串
        List<NtDataElementVO> ntDataSources = Lists.newArrayList();
        InputStreamReader input = null;
        List<String> fileds = getFiled();
        String firstFiled = fileds.get(0);
        try {
            File file = new File(name);
            input = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(input);
            String str, line;
            NtDataElementVO ntDataSource = null;
            int lastIndex = -1;
            while ((line = bf.readLine()) != null) {
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                line = line.trim();
                if (line.startsWith(firstFiled)) {
                    if (ntDataSource != null) {
                        ntDataSources.add(ntDataSource);
                    }
                    lastIndex = -1;
                    ntDataSource = new NtDataElementVO();
                }

                //赋值
                int index = -1;
                for (int i = 0; i < fileds.size(); i++) {
                    if (line.startsWith(fileds.get(i))) {
                        index = i;
                        break;
                    }
                }
                str = subString(line);
                if (index == -1) {
                    if (lastIndex == -1) {
                        logger.warn(str);
                    } else {
                        Class clazz = ntDataSource.getClass();
                        Field f = clazz.getDeclaredFields()[lastIndex];
                        String fieldName = f.getName();
                        Method getMethod = clazz.getMethod("get" + toUpperCaseFirst(fieldName));
                        Object value = getMethod.invoke(ntDataSource);

                        Method setMethod = clazz.getMethod("set" + toUpperCaseFirst(fieldName), String.class);
                        if (value != null) {
                            str = str + String.valueOf(value);
                        }
                        setMethod.invoke(ntDataSource, str);
                    }
                } else if (ntDataSource != null) {
                    lastIndex = index;
                    Class clazz = ntDataSource.getClass();
                    Field f = clazz.getDeclaredFields()[index];
                    String fieldName = f.getName();
                    Method method = clazz.getMethod("set" + toUpperCaseFirst(fieldName), String.class);
                    method.invoke(ntDataSource, str);
                } else {
                    logger.warn("the entity is not init. ");
                }
            }
            bf.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(input);
        }
        return ntDataSources;
    }
}
