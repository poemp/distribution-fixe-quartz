package org.poem.nt.excel;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.poem.nt.excel.vo.ExcelVO;
import org.poem.nt.field.NtField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author poem
 */
public class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 生成随机数
     */
    private static Random random = new Random();

    /**
     * 身份证
     */
    public static final String IDCARD = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" + "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" + "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";

    /**
     * 正整数正则表达式 >=0 ^[1-9]\d*|0$
     */
    public static final String INTEGER_NEGATIVE = "^[1-9]\\d*|0$";

    /**
     * 日期
     */
    public static final String DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}";

    /**
     * 获取文件列
     *
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> LinkedHashMap<String, String> getCloumnField(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (Field field : fields) {
            NtField columnField = field.getAnnotation(NtField.class);
            if (columnField != null) {
                map.put(field.getName(), columnField.name());
            }
        }
        return map;
    }

    /**
     * 获取文件列
     *
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> LinkedHashMap<String, String> getFieldCloumn(Class<T> tClass) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        LinkedHashMap<String, String> linkedHashMap = getCloumnField(tClass);
        linkedHashMap.forEach((key, value) -> {
            map.put(value, key);
        });
        return map;
    }

    /**
     * 创建一个模板
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> void createTemplate(OutputStream outputStream, Class<T> tClass) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        LinkedHashMap<String, String> map = getCloumnField(tClass);
        List<String> head = Lists.newArrayList();
        head.addAll(map.values());
        createExcel(outputStream, head, Lists.newArrayList(), tClass);
    }

    /**
     * 创建一个模板
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> void exportExcel(OutputStream outputStream, Class<T> tClass, List<T> datas) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        LinkedHashMap<String, String> map = getCloumnField(tClass);
        List<String> head = Lists.newArrayList();
        head.addAll(map.values());
        createExcel(outputStream, head, datas, tClass);
    }

    /**
     * @param filed
     * @return
     */
    private static String getMethod(String filed) {
        StringBuilder stringBuffer = new StringBuilder();
        char[] bytes = filed.toCharArray();
        for (int i = 0; i < bytes.length; i++) {
            if (i == 0) {
                stringBuffer.append(String.valueOf(bytes[i]).toUpperCase());
            } else {
                stringBuffer.append(String.valueOf(bytes[i]));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 获取数据列表
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> List<List<String>> getData(List<T> data, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<List<String>> datas = new ArrayList<>(data.size());
        LinkedHashMap<String, String> field = getCloumnField(clazz);
        for (T datum : data) {
            List<String> strings = new ArrayList<>(field.keySet().size());
            for (String k : field.keySet()) {
                Method method = clazz.getMethod("get" + getMethod(k));
                Object o = method.invoke(datum, new Object[0]);
                if (o == null) {
                    strings.add("");
                } else {
                    strings.add(String.valueOf(o));
                }
            }
            datas.add(strings);
        }
        return datas;
    }

    /**
     * 写入数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> void createExcel(OutputStream outputStream, List<String> head, List<T> data, Class<T> clazz) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //写入文件头
        CellStyle font = workbook.createCellStyle();
        // 居中
        font.setAlignment(HorizontalAlignment.CENTER);
        Row row = sheet.createRow(0);
        for (int i = 0; i < head.size(); i++) {
            row.setRowStyle(font);
            row.createCell(i).setCellValue(head.get(i));
        }
        List<String> roleList;
        if (CollectionUtils.isNotEmpty(data)) {
            List<List<String>> datas = getData(data, clazz);
            for (int i = 0; i < datas.size(); i++) {
                row = sheet.createRow(i + 1);
                roleList = datas.get(i);
                for (int i1 = 0; i1 < roleList.size(); i1++) {
                    row.setRowStyle(font);
                    row.createCell(i1).setCellValue(roleList.get(i1));
                }
            }
        }
        workbook.write(outputStream);
    }


    /**
     * 读取excel数据， 舍弃表头
     *
     * @param inputStream
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static <T> ExcelVO<T> readExcel(InputStream inputStream, Class<T> clazz) throws IOException, InvalidFormatException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        OPCPackage pkg = OPCPackage.open(inputStream);
        XSSFWorkbook workbook = new XSSFWorkbook(pkg);
        Sheet sheet = workbook.getSheetAt(0);
        List<T> tList = Lists.newArrayList();
        ExcelVO<T> excelVO = new ExcelVO<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        List<String> head = Lists.newArrayList();
        LinkedHashMap<String, String> linkedHashMap = getFieldCloumn(clazz);
        List<String> message = Lists.newArrayList();
        int roleIndex = 0;
        Field[] fields = clazz.getDeclaredFields();
        while (rowIterator.hasNext()) {
            T t = clazz.newInstance();
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int cellIndex = 0;
            boolean save = false;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if (roleIndex == 0) {
                    head.add(cell.getStringCellValue());
                } else {
                    if (cellIndex < fields.length) {
                        Field field = fields[cellIndex];
                        NtField columnField = field.getAnnotation(NtField.class);
                        if (columnField != null && null != linkedHashMap.get(head.get(cellIndex))) {
                            save = true;
                            cell.setCellType(CellType.STRING);
                            String methodName = "set" + getMethod(linkedHashMap.get(head.get(cellIndex)));
                            Method method = clazz.getMethod(methodName, String.class);
                            List<String> errors = validateCell(roleIndex, cellIndex, cell.getStringCellValue(), columnField);
                            if (CollectionUtils.isNotEmpty(errors)) {
                                message.addAll(errors);
                            }
                            method.invoke(t, cell.getStringCellValue());
                        }

                    }
                }
                cellIndex++;
            }
            if (roleIndex == 0) {
                excelVO.setHead(head);
                List<String> not = head.stream().filter(s -> !linkedHashMap.containsKey(s)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(not)) {
                    message.add("表头不对:" + String.join(",", not));
                }
                logger.info("head:" + JSONObject.toJSONString(head));
            }
            roleIndex++;
            if (save) {
                tList.add(t);
            }

        }
        excelVO.setData(tList);
        excelVO.setMessages(message);
        excelVO.setErr(CollectionUtils.isNotEmpty(message));
        excelVO.setRedisKey("redis@key&" + System.currentTimeMillis() + "@" + getRandomChar());
        return excelVO;
    }

    /**
     * 验证
     *
     * @param stringCellValue
     * @param columnField
     * @return
     */
    private static List<String> validateCell(int r, int c, String stringCellValue, NtField columnField) {
        List<String> errorMes = Lists.newArrayList();
        if (columnField.isDate()) {
            Pattern pattern = Pattern.compile(DATE);
            if (!pattern.matcher(stringCellValue).matches()) {
                errorMes.add("第" + r + "行，" + c + "列 " + stringCellValue + " 格式不对 (yyyy-mm-dd)");
            }
        }

        if (columnField.isIdNum()) {
            Pattern pattern = Pattern.compile(IDCARD);
            if (!pattern.matcher(stringCellValue).matches()) {
                errorMes.add("第" + r + "行，" + c + "列 " + stringCellValue + " 身份证格式不对");
            }
        }

        if (columnField.isIdNum()) {
            Pattern pattern = Pattern.compile(INTEGER_NEGATIVE);
            if (!pattern.matcher(stringCellValue).matches()) {
                errorMes.add("第" + r + "行，" + c + "列 " + stringCellValue + " 只能是数字");
            }
        }
        return errorMes;
    }

    /**redis key
     *
     * 获取随机数
     *
     * @return
     */
    private static String getRandomChar() {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            stringBuffer.append(getRandom());
        }
        return stringBuffer.toString();
    }

    private static int getRandom() {
        return random.nextInt(9);
    }
}
