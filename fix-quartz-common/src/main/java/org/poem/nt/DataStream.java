package org.poem.nt;

import org.poem.nt.field.NtField;
import org.poem.nt.txt.TxtParseUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author poem
 */
public class DataStream {

    /**
     * @return
     */
    public static List<NtDataElementVO> read(String path) throws Exception {
        List<NtDataElementVO> ntDataSources = TxtParseUtils.read(path);
        for (NtDataElementVO ntDataSource : ntDataSources) {
            Class clazz = ntDataSource.getClass();
            Field[] fs = clazz.getDeclaredFields();
            for (Field f : fs) {
                String fieldName = f.getName();
                NtField ntField = f.getAnnotation(NtField.class);
                Method getMethod = clazz.getMethod("get" + TxtParseUtils.toUpperCaseFirst(fieldName));
                Object value = getMethod.invoke(ntDataSource);
                if (value == null) {
                    System.err.println(ntField.name() + ":" + String.valueOf(""));
                } else {
                    System.err.println(ntField.name() + ":" + String.valueOf(value));
                }
            }
            System.err.println("\n");
        }
        return ntDataSources;
    }

    /**
     * get main function
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            read("/Users/poem/Documents/02-studyCode/55-distribution-fixe-quartz/data.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

