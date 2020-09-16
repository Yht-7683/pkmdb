package com.pkm.pkmdb.common.excel;

import com.zongs.commons.exception.BusinessException;
import com.zongs.commons.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class ExcelUtils {
    /**
     * 2003- 版本的excel
     */
    private final static String excel2003L = ".xls";
    /**
     * 2007+ 版本的excel
     */
    private final static String excel2007U = ".xlsx";

    /**
     * 文件上传
     */
    public static List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
        List<List<Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<List<Object>>();
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                //遍历所有的列
                List<Object> li = new ArrayList<Object>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(getCellValue(cell));
                }
                list.add(li);
            }
        }
        return list;
    }

    /**
     * 将excel转换为对象
     * @param in 1
     * @param fileName 2
     * @param entityClass 3
     * @param fields 4
     * @return java.util.List<T>
     * @author meco.he
     * @date 2019/11/14 12:38
     */
    public static <T> List<T> execltoList(InputStream in, String fileName, Class<T> entityClass,
                                              Map<String, String> fields) throws Exception {
        List<T> resultList = new ArrayList<T>();

        Workbook workbook = getWorkbook(in, fileName);
        if (null == workbook) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        // excel中字段的中英文名字数组
        String[] egtitles = new String[fields.size()];
        String[] cntitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String cntitle = it.next();
            String egtitle = fields.get(cntitle);
            egtitles[count] = egtitle;
            cntitles[count] = cntitle;
            count++;
        }

        // 得到excel中sheet总数
        int sheetcount = workbook.getNumberOfSheets();

        if (sheetcount == 0) {
            in.close();
            throw new Exception("Excel文件中没有任何数据");
        }

        // 数据的导出
        for (int i = 0; i < sheetcount; i++) {
            sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 每页中的第一行为标题行，对标题行的特殊处理
            Row firstRow = sheet.getRow(0);
            if (firstRow == null) {
                continue;
            }
            int celllength = firstRow.getLastCellNum();

            String[] excelFieldNames = new String[celllength];
            LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();

            // 获取Excel中的列名
            for (int f = 0; f < celllength; f++) {
                cell = firstRow.getCell(f);
                excelFieldNames[f] = cell.getStringCellValue().trim();
                // 将列名和列号放入Map中,这样通过列名就可以拿到列号
                for (int g = 0; g < excelFieldNames.length; g++) {
                    colMap.put(excelFieldNames[g], g);
                }
            }
            // 由于数组是根据长度创建的，所以值是空值，这里对列名map做了去空键的处理
            colMap.remove(null);
            // 将sheet转换为list
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                // 根据泛型创建实体类
                T entity = entityClass.newInstance();
                // 给对象中的字段赋值
                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    // 获取中文字段名
                    String cnNormalName = entry.getKey();
                    // 获取英文字段名
                    String enNormalName = entry.getValue();
                    // 根据中文字段名获取列号
                    Integer col = colMap.get(cnNormalName);
                    if(ObjectUtils.isEmpty(col)){
                        continue;
                    }
                    // 获取当前单元格中的内容
                    Cell cellContent = row.getCell(col);
                    if(ObjectUtils.isEmpty(cellContent)){
                        continue;
                    }
                    String content = getValue(cellContent);
                    // 给对象赋值
                    setFieldValueByName(enNormalName, content, entity);
                }
                resultList.add(entity);
            }
        }
        in.close();
        return resultList;
    }

    /**
     * 根据名称设置对象值
     * @param fieldName 1
     * @param fieldValue 2
     * @param o 3
     * @return void
     * @author meco.he
     * @date 2019/11/14 12:39
     */
    private static void setFieldValueByName(String fieldName,
                                            Object fieldValue, Object o) throws Exception {
        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            if(ObjectUtils.isEmpty(fieldValue)){
                return;
            }
            // 获取字段类型
            Class<?> fieldType = field.getType();

            // 根据字段类型给字段赋值
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
            } else if ((Integer.TYPE == fieldType)
                    || (Integer.class == fieldType)) {
                field.set(o, Integer.parseInt(fieldValue.toString()));
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                field.set(o, Long.valueOf(fieldValue.toString()));
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                field.set(o, Float.valueOf(fieldValue.toString()));
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                field.set(o, Short.valueOf(fieldValue.toString()));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(o, Double.valueOf(fieldValue.toString()));
            } else if (Character.TYPE == fieldType) {
                if ((fieldValue != null)
                        && (fieldValue.toString().length() > 0)) {
                    field.set(o,
                            Character.valueOf(fieldValue.toString().charAt(0)));
                }
            } else if (BigDecimal.class == fieldType){
                field.set(o, new BigDecimal(fieldValue.toString()));
            } else if (Date.class == fieldType) {
                field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(fieldValue.toString()));
            } else {
                field.set(o, fieldValue);
            }
        } else {
            throw new Exception(o.getClass().getSimpleName() + "类不存在字段名 "
                    + fieldName);
        }
    }

    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        // 拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        // 如果本类和父类都没有，则返回空
        return null;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            //2003-
            wb = new HSSFWorkbook(inStr);
        } else if (excel2007U.equals(fileType)) {
            //2007+
            wb = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 根据单元格类型转换值
     * @param cell 1
     * @return java.lang.String
     * @author meco.he
     * @date 2019/11/14 12:40
     */
    public static String getValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                //short s = cell.getCellStyle().getDataFormat();
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    // 验证short值
                    if (cell.getCellStyle().getDataFormat() == 14) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                    } else if (cell.getCellStyle().getDataFormat() == 21) {
                        sdf = new SimpleDateFormat("HH:mm:ss");
                    } else if (cell.getCellStyle().getDataFormat() == 22) {
                        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    } else {
                        throw new RuntimeException("日期格式错误!!!");
                    }
                    Date date = cell.getDateCellValue();
                    cellValue = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = String.valueOf(cell.getRichStringCellValue().getString());
                }
                break;
            case Cell.CELL_TYPE_STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: // 空值
                cellValue = null;
                break;
            case Cell.CELL_TYPE_ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    /**
     * 描述：对表格中数值进行格式化
     */
    public static Object getCellValue(Cell cell) {
        Object value = null;
        //格式化number String字符
        DecimalFormat df = new DecimalFormat("0");
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        //格式化数字
        DecimalFormat df2 = new DecimalFormat("0.00");

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }




    /**
     * @param sheetName     工作簿名称
     * @param clazz         导出数据对象Class
     * @param objs          导出数据对象list
     * @param excelBeanList 表头及对应字段List<ExcelBean>
     * @method createExcelFile
     * @Description:
     * @Date: 16:56 2019/3/11
     * @return: org.apache.poi.hssf.usermodel.HSSFWorkbook
     * @respbody:
     */
    public static HSSFWorkbook createExcelFile(String sheetName,
                                               Class clazz,
                                               List<Object> objs,
                                               List<ExcelBean> excelBeanList)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException, IntrospectionException {
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //字体样式,可自己修改

        //创建标题（表头）
        createTableHeader(sheet, excelBeanList);
        //添加数据内容
        if (CollectionUtils.isNotEmpty(objs)) {
            //创建内容
            createTableRows(sheet, excelBeanList, objs, clazz);
        }
        return workbook;
    }

    /**
     * @method createTableHeader
     * @Description:
     * @Date: 16:36 2019/3/11
     * @return: void
     * @respbody:
     */
    public static final void createTableHeader(HSSFSheet sheet, List<ExcelBean> excelBeanList) {
        HSSFRow titleRow = sheet.createRow(0);
        int i = 0;
        for (ExcelBean excelBean : excelBeanList) {
            titleRow.createCell(i).setCellValue(excelBean.getHeadTextName());
            i++;
        }
    }

    /**
     * @method createTableRows
     * @Description:
     * @Date: 16:38 2019/3/11
     * @return: void
     * @respbody:
     */
    public static void createTableRows(HSSFSheet sheet,
                                       List<ExcelBean> excelBeanList,
                                       List<Object> objs,
                                       Class<?> clazz)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException {
        for (Object obj : objs) {
            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            int i = 0;
            for (ExcelBean excelBean : excelBeanList) {
                PropertyDescriptor pd = new PropertyDescriptor(excelBean.getPropertyName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object rtn = getMethod.invoke(obj);
                String value = "";
                // 如果是日期类型进行转换
                if (rtn != null) {
                    if (rtn instanceof Date) {
                        value = DateUtils.formatDate((Date) rtn, "yyyy-MM-dd");
                    } else if (rtn instanceof BigDecimal) {
                        NumberFormat nf = new DecimalFormat("#,##0.00");
                        value = nf.format(rtn);
                    } else if ((rtn instanceof Integer) && (Integer.valueOf(rtn.toString()) < 0)) {
                        value = "--";
                    } else {
                        value = rtn.toString();
                    }
                }
                dataRow.createCell(i).setCellValue(value);
                i++;
            }
        }
    }

    /**
     * 公共导出方法 单个
     */
    public <T> void commonExport(String sheetName, Class clazz, Map<String, String> beanMap, List<T> dataList, HttpServletResponse response) throws BusinessException {

        try {
            //创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            //创建工作表
            HSSFSheet sheet = createSheet(workbook,sheetName);
            //添加数据
            createExcelFilePlus(sheet, clazz, beanMap, dataList,0);
            //写入文件
            writeWorkBook(workbook, response);

        } catch (Exception e) {
            throw new BusinessException(e.getMessage(),e);
        }
    }

    /**
     * 公共导出方法 多个
     */
    public <T> void commonExportPlus(String sheetName, Class clazz, Map<String, String> beanMap, Object obj,
                                     Class clazzPlus, Map<String, String> beanMapPlus, List<T> dataListPlus, HttpServletResponse response) throws BusinessException {

        try {
            //创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();

            //创建工作表
            HSSFSheet sheet = createSheet(workbook,sheetName);
            List<Object> dataList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(obj)){
                dataList.add(obj);
            }
            //添加数据
            createExcelFilePlus(sheet, clazz, beanMap, dataList,0);
            createExcelFilePlus(sheet, clazzPlus, beanMapPlus, dataListPlus,sheet.getLastRowNum()+2);

            //写入文件
            writeWorkBook(workbook, response);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(),e);
        }
    }

    /**
     * 添加数据
     * @param clazz 导出数据对象Class
     * @param beanMap 表头及对应字段Map
     * @param dataList 导出数据对象list
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> void createExcelFilePlus(HSSFSheet sheet, Class clazz, Map<String, String> beanMap, List<T> dataList, int rownum)throws Exception {
        //创建标题（表头）
        createTableHeaderPlus(sheet, beanMap,rownum);
        //添加数据内容
        createTableRowsPlus(sheet, beanMap, dataList, clazz);
    }

    /**
     * 创建工作表
     * @param workbook
     * @param sheetName
     * @return
     */
    private HSSFSheet createSheet(HSSFWorkbook workbook,String sheetName) {
        //在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //字体样式,可自己修改
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(1, true);

        return sheet;
    }

    /**
     * 创建标题（表头）
     * @param sheet
     * @param map
     * @param rownum
     */
    public static final void createTableHeaderPlus(HSSFSheet sheet, Map<String, String> map, int rownum) {
        if(!map.isEmpty()){
            HSSFRow titleRow = sheet.createRow(rownum);
            int i = 0;
            for (Map.Entry entry : map.entrySet()) {
                String headTextName = entry.getKey().toString();
                titleRow.createCell(i).setCellValue(headTextName);
                i++;
            }
        }

    }

    /**
     * 添加数据内容
     * @method createTableRows
     * @Description:
     * @Date: 16:38 2019/3/11
     * @return: void
     * @respbody:
     */
    public <T> void createTableRowsPlus(HSSFSheet sheet, Map<String, String> beanMap, List<T> dataList, Class<?> clazz) throws Exception {
        if(CollectionUtils.isNotEmpty(dataList)) {
            for (T obj : dataList) {
                HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                int i = 0;
                if(!beanMap.isEmpty()) {
                    for (Map.Entry entry : beanMap.entrySet()) {
                        String propertyName = entry.getValue().toString();
                        PropertyDescriptor pd = new PropertyDescriptor(propertyName, clazz);
                        Method getMethod = pd.getReadMethod();
                        Object rtn = getMethod.invoke(obj);
                        String value = "";
                        // 如果是日期类型进行转换
                        if (rtn != null) {
                            if (rtn instanceof Date) {
                                value = DateUtil.format((Date) rtn,"yyyy-MM-dd");
                            } else if (rtn instanceof BigDecimal) {
                                NumberFormat nf = new DecimalFormat("#,##0.00");
                                value = nf.format(rtn);
                            } else if ((rtn instanceof Integer) && (Integer.valueOf(rtn.toString()) < 0)) {
                                value = "--";
                            } else {
                                value = rtn.toString();
                            }
                        }
                        dataRow.createCell(i).setCellValue(value);
                        i++;
                    }
                }
            }
        }
    }

    /**
     * 写入文件
     * @param workbook
     * @param response
     * @throws Exception
     */
    private void writeWorkBook(HSSFWorkbook workbook, HttpServletResponse response) throws Exception {

        ServletOutputStream outputStream = response.getOutputStream();
        try {
            if (null != workbook) {
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                String fileName = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS")+ ".xls";
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            outputStream.flush();
            outputStream.close();
        }
    }
}