package org.poem.nt;

import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.poem.nt.excel.ExcelUtils;
import org.poem.nt.excel.vo.ExcelVO;
import org.poem.nt.txt.TxtParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class Controller {


    private static final Logger logger = LoggerFactory.getLogger(Controller.class);


    /**
     * 导出 txt 模板
     *
     * @param response
     */
    @ApiOperation(value = "导出模板", httpMethod = "GET")
    @GetMapping("/getExportTemplateTxt")
    public void getExportTemplateTxt(HttpServletResponse response) {
        try {
            String fileName = "数据元-模板.txt";
            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                    new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1") + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            TxtParseUtils.createTemplate(response.getOutputStream(), NtDataSourceVO.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
        }
    }
    /**
     * 导出 excel 模板
     *
     * @param response
     */
    @ApiOperation(value = "导出模板", httpMethod = "GET")
    @GetMapping("/getExportTemplateXls")
    public void getExportTemplateXls(HttpServletResponse response) {
        try {
            String fileName = "数据元-模板.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                    new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1") + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            ExcelUtils.createTemplate(response.getOutputStream(), NtDataSourceVO.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            logger.debug(e.getMessage(), e);
        }
    }

    /**
     * 第一步，验证导入文件正确性
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "第一步，验证导入文件正确性", httpMethod = "POST")
    @PostMapping("/readExcel")
    public ResultVO<ExcelVO<NtDataSourceVO>> readExcel(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (!multipartResolver.isMultipart(request)) {
            return new ResultVO<ExcelVO<NtDataSourceVO>>(0, null, "导入数据");
        }
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Collection<MultipartFile> collection = multiRequest.getFileMap().values();
        if (collection.size() == 0) {
            return new ResultVO<ExcelVO<NtDataSourceVO>>(1, null, "没有文件");
        }
        MultipartFile multipartFile = collection.toArray(new MultipartFile[0])[0];
        try {
            ExcelVO<NtDataSourceVO> excelVO = ExcelUtils.readExcel(multipartFile.getInputStream(), NtDataSourceVO.class);
            if (!excelVO.getErr()) {
//                redisUtil.set(excelVO.getRedisKey(), excelVO);
            }
            return new ResultVO<ExcelVO<NtDataSourceVO>>(0, excelVO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return new ResultVO<ExcelVO<NtDataSourceVO>>(-1, null, e.getMessage());
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return new ResultVO<ExcelVO<NtDataSourceVO>>(-1, null, e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return new ResultVO<ExcelVO<NtDataSourceVO>>(-1, null, e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return new ResultVO<ExcelVO<NtDataSourceVO>>(-1, null, e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return new ResultVO<ExcelVO<NtDataSourceVO>>(-1, null, e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return new ResultVO<ExcelVO<NtDataSourceVO>>(-1, null, e.getMessage());
        }
    }

    /**
     * 倒入
     *
     * @param redisKey
     * @param request
     * @return
     */
    @ApiOperation(value = "第二步，验证导入数据正确性和导入数据", httpMethod = "POST")
    @PostMapping("/importData")
    public ResultVO<List<String>> importData(String redisKey, HttpServletRequest request) {
        ExcelVO<NtDataSourceVO> excelVO = new ExcelVO<NtDataSourceVO>();
        return new ResultVO<List<String>>(0, null, "完成");
    }
}
