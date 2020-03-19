package com.excelutils.readwrite;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 导出工具类
 */
@Slf4j
public class ExcelUtils {

    /**
     * 根据数据集合导出数据(单个sheet页)保存在固定路径
     *
     * @param data       数据集合
     * @param clazz      对应的模板model对象
     * @param exportPath 导出的路径
     * @param fileName   导出生成excel的文件名
     * @param sheetName  导出生成的sheet名称
     * @param <T>
     * @throws Exception
     */
    public static <T> void exportExcel(List<T> data, Class<T> clazz, String exportPath, String fileName, String sheetName) throws Exception {
        String pathName = exportPath + File.separator + fileName + ExcelTypeEnum.XLSX.getValue();
        EasyExcel.write(pathName, clazz).sheet(sheetName).doWrite(data);
    }

    /**
     * 根据数据集合导出数据(单个sheet页)保存在固定路径
     *
     * @param data       数据集合
     * @param clazz      对应的模板model对象
     * @param exportPath 导出的路径
     * @param fileName   导出生成excel的文件名
     * @param sheetName  导出生成的sheet名称
     * @param typeEnum   导出生成那种excel .xls 或者.xlsx 默认为 .xlsx
     * @param <T>
     * @throws Exception
     */
    public static <T> void exportExcel(List<T> data, Class<T> clazz, String exportPath, String fileName, String sheetName, ExcelTypeEnum typeEnum) throws Exception {
        if (typeEnum == null) {
            typeEnum = ExcelTypeEnum.XLSX;
        }
        String pathName = exportPath + File.separator + fileName + typeEnum.getValue();
        EasyExcel.write(pathName, clazz).sheet(sheetName).doWrite(data);
    }

    /**
     * 根据数据集合导出数据(支持多个sheet页)保存在固定路径
     *
     * @param data       数据集合
     * @param clazz      对应的模板model对象
     * @param exportPath 导出的路径
     * @param fileName   导出生成excel的文件名
     * @param sheetName  导出生成的sheet名称
     * @param typeEnum   导出生成那种excel .xls 或者.xlsx 默认为 .xlsx
     * @param pageSize   每一个sheet页生成多少 大于0
     * @param <T>
     * @throws Exception
     */
    public static <T> void exportExcel(List<T> data, Class<T> clazz, String exportPath, String fileName, String sheetName, ExcelTypeEnum typeEnum, Integer pageSize) throws Exception {
        if (typeEnum == null) {
            typeEnum = ExcelTypeEnum.XLSX;
        }
        String pathName = exportPath + File.separator + fileName + typeEnum.getValue();
        if (pageSize == null || (!CollectionUtils.isEmpty(data) && data.size() < pageSize)) {
            EasyExcel.write(pathName, clazz).sheet(sheetName).doWrite(data);
        } else {//按设置的值分页
            ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(pathName).head(clazz);
            ExcelWriter writer = excelWriterBuilder.build();
            writeSheets(data, sheetName, pageSize, excelWriterBuilder, writer);
            writer.finish();
        }
    }

    /**
     * 写多个sheet页
     *
     * @param <T>
     * @param data
     * @param sheetName
     * @param pageSize
     * @param excelWriterBuilder
     * @param writer
     */
    private static <T> void writeSheets(List<T> data, String sheetName, Integer pageSize, ExcelWriterBuilder excelWriterBuilder, ExcelWriter writer) {
        int fromIndex = 0, toIndex = 0;
        int size = data.size();
        int sheets = (size - 1) / pageSize + 1;
        for (int i = 0; i < sheets; i++) {
            String suffix = i == 0 ? "" : i + "";
            fromIndex = i * pageSize;
            toIndex = pageSize * (i + 1) > size ? size : pageSize * (i + 1);
            WriteSheet writeSheet = excelWriterBuilder.sheet(i, sheetName + suffix).build();
            writer.write(data.subList(fromIndex, toIndex), writeSheet);
        }
    }

    /**
     * 导出excel单个sheet页返回到前台下载
     * 不支持自定义模板
     *
     * @param response 响应体
     * @param clazz    excel的model对象
     * @param data     数据集合
     * @param <T>
     * @throws IOException
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName, String sheetName) throws Exception {
        setDownloadElement(response, fileName);
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(data);
    }

    /**
     * 导出excel支持多个sheet页返回到前台下载
     * 不支持自定义模板
     *
     * @param response 响应体
     * @param clazz    excel的model对象
     * @param data     数据集合
     * @param <T>
     * @throws IOException
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName, String sheetName, Integer pageSize) throws Exception {
        if (CollectionUtils.isEmpty(data)){
            data = new ArrayList<>();
        }
        setDownloadElement(response, fileName);
        if (pageSize == null || (!CollectionUtils.isEmpty(data) && data.size() < pageSize)) {
            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(data);
        } else {
            ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream(), clazz);
            ExcelWriter writer = excelWriterBuilder.build();
            writeSheets(data, sheetName, pageSize, excelWriterBuilder, writer);
            writer.finish();
        }
    }


    /**
     * key list型数据的导出生成到配置文件位置
     *
     * @param data
     * @param exportPath
     * @param fileName
     * @param
     * @throws Exception
     */
    public static void exportExcel(List<CalendarExportDTO> data, String exportPath, String fileName) throws Exception {
        String pathName = exportPath + File.separator + fileName + ExcelTypeEnum.XLSX.getValue();
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(pathName);
        ExcelWriter writer = excelWriterBuilder.build();
        creatExcelForMapData(data, excelWriterBuilder, writer);
    }

    /**
     * 特定型数据的导出
     *
     * @param data
     * @param fileName
     * @throws Exception
     */
    public static void exportExcel(HttpServletResponse response, List<CalendarExportDTO> data, String fileName) throws Exception {
        setDownloadElement(response, fileName);
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream());
        ExcelWriter writer = excelWriterBuilder.build();
        creatExcelForMapData(data, excelWriterBuilder, writer);
    }

    /**
     * Map<Class, Map<String, List<T>>> 特定数据类型生成excle
     *
     * @param data
     * @param excelWriterBuilder
     * @param writer
     */
    private static void creatExcelForMapData(List<CalendarExportDTO> data, ExcelWriterBuilder excelWriterBuilder, ExcelWriter writer) {
        int index = 0;
        for (CalendarExportDTO entry : data) {
            String sheetName = StringUtils.isBlank(entry.getSheetName()) ? "":entry.getSheetName();
            List<? extends Object> sheetData = entry.getDatas() ;
            if ( sheetData == null){
               sheetData = new ArrayList<>();
            }
            Class clazz = entry.getClazz();
            WriteSheet writeSheet = excelWriterBuilder.sheet(index, sheetName).head(clazz).build();
            writer.write(sheetData, writeSheet);
            index++;
        }
        writer.finish();
    }

    /**
     * 设置下载必要元素
     *
     * @param response
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    private static void setDownloadElement(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        //response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        fileName = fileName + ExcelTypeEnum.XLSX.getValue();
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
    }

    /**
     * 生成文件名称
     */
    public static String getFileName(String prefix,String suffix){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String midString = dateFormat.format(new Date());
        if (StringUtils.isBlank(prefix)){
            prefix ="";
        }
        if (StringUtils.isBlank(suffix)){
            suffix = "";
        }
        return prefix+midString+suffix;
    }


    /**
     * 读取文件
     * @param clazz 需要封装成的class 只能读取第一个sheet
     * @param listener 由excel转换成实体的监听器处理类 监听器不能交由
     *                 spring管理,每次使用都要手动创建
     * @param clazz 将要解析成的实体类型
     */
    public static void read(String readPath,String fileName,Class<? extends Object> clazz, AnalysisEventListener<? extends Object> listener) {
        String readFileName = readPath + File.separator + fileName;
        EasyExcel.read(readFileName, clazz, listener).sheet().doRead();
    }

    /**
     * 读取文件
     * @param clazz 需要封装成的class 只能读取第一个sheet
     * @param listener 由excel转换成实体的监听器处理类 监听器不能交由
     *                 spring管理,每次使用都要手动创建
     * @param clazz 将要解析成的实体类型
     */
    public static void readFile(String readPath,String fileName,Class<? extends Object> clazz, AnalysisEventListener<? extends Object> listener) {
        String readFileName = readPath + File.separator + fileName;
        ExcelReader excelReader = EasyExcel.read(readFileName,clazz,listener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        excelReader.finish();
    }


    /**
     * 读取文件
     * @param clazz 需要封装成的class 读取多个sheet
     * @param listener 由excel转换成实体的监听器处理类 监听器不能交由
     *                 spring管理,每次使用都要手动创建
     * @param clazz 将要解析成的实体类型
     */
    public static void readFileWithSheets(String readPath,String fileName,Class<? extends Object> clazz, AnalysisEventListener<? extends Object> listener) {
        String readFileName = readPath + File.separator + fileName;
        ExcelReader excelReader = EasyExcel.read(readFileName,clazz,listener).build();
        readWithSheets(excelReader);
    }

    /**
     * 读取文件,使用map接收
     * @param readPath
     * @param fileName
     * @param listener 通过封装之后生成Map<Integer,Object>,前者为字段在excel表格中的索引位置,后者为该索引位置的值
     */
    public static void readFileWithMap(String readPath,String fileName, AnalysisEventListener<? extends Object> listener) {
        String readFileName = readPath + File.separator + fileName;
        EasyExcel.read(readFileName, listener).sheet().doRead();
    }

    /**
     * 读取上传的文件 只能读取第一个sheet页
     * @param file 文件流
     * @param clazz 上传的数据类型
     * @param listener 监听器
     * @throws IOException
     */
    public static void readUploadFileFirstSheet(MultipartFile file,Class<? extends Object> clazz,AnalysisEventListener<? extends Object> listener) throws IOException {
        EasyExcel.read(file.getInputStream(), clazz,listener).sheet().doRead();
    }


    /**
     * 读取上传的文件 可以是多个sheet也可以是单个sheet
     * @param file 文件流
     * @param clazz 上传的数据类型
     * @param listener 监听器
     * @throws IOException
     */
    public static void readUploadFile(MultipartFile file,Class<? extends Object> clazz,AnalysisEventListener<? extends Object> listener) throws IOException {
        ExcelReader excelReader = EasyExcel.read(file.getInputStream(),clazz,listener).build();
        readWithSheets(excelReader);
    }

    /**
     * 读取多个sheet页
     * @param excelReader
     */
    private static void readWithSheets(ExcelReader excelReader) {
        List<ReadSheet> readSheets = excelReader.excelExecutor().sheetList();
        if (CollectionUtils.isEmpty(readSheets)) {
            //log.info("当前导入的数据集合为空", new Date());
            return;
        }
        for (int i = 0, j = readSheets.size(); i < j; i++) {
            excelReader.read(readSheets.get(i));
        }
        excelReader.finish();
    }
}
