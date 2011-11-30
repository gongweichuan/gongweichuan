/*
 * Created on 2006-8-30
 *
 */
package com.coolsql.exportdata.excel;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.coolsql.pub.component.WaitDialog;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.ConnectionUtil;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 处理Excel文件，生成Excel文件，将指定的数据写入文件
 */
public class ExcelUtil {
	
	public static short transColor(Color color,HSSFWorkbook workbook)
	{
		 HSSFPalette palette = workbook.getCustomPalette();
		  HSSFColor hssfColor = palette.findColor((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
		  if (hssfColor == null ){
		      palette.setColorAtIndex(HSSFColor.LAVENDER.index, (byte)color.getRed(), (byte)color.getGreen(),
		(byte)color.getBlue());
		      hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
		  }
		  return hssfColor.getIndex();
	}
	
    private static ExcelUtil instance = null;

    private int index = 0; //当前处理的数据索引

    private int total = 1; //总的数据条数

    private boolean isRun; //该变量用来控制数据导出的中断

    private boolean isRunning;//当前excel生成类是否正在执行导出动作

    private WaitDialog waiter = null;

    private ExcelUtil() {
        isRun = true;
        isRunning = false;
    }

    public static synchronized ExcelUtil getInstance() {
        if (instance == null)
            instance = new ExcelUtil();
        return instance;
    }

    public File createFile(String name) {
        File tmp = new File(name);
        //		if (tmp.exists()) {
        //			return null;
        //		}
        return tmp;
    }

    public void setWaitDialog(WaitDialog waiter) {
        this.waiter = waiter;
    }

    public WaitDialog getWaitDialog() {
        return waiter;
    }

    /**
     * 生成excel文件数据,以向量集合为数据基础
     * 
     */
    public void buildExcel(File file, ExcelComponentSet setting, Vector<Vector<Object>> data)
            throws ExcelProcessException, IOException, FileNotFoundException {
        if (isRunning) {
            throw new ExcelProcessException(PublicResource
                    .getSQLString("export.error.isrunning"));
        }
        setRun(true); //设置启动标志

        HSSFWorkbook wb = new HSSFWorkbook();
        checkExcelSet(setting);

        /**
         * 设置计算进度所需的相关信息
         */
        index = 0;
        total = data.size();

        waiter.setTaskLength(data.size());

        //开始数据导出
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(file.getAbsolutePath(),false, false);
            out = new FileOutputStream(file);

            isRunning = true;
            do {
                index = createSheet(wb, setting, data, "");
            } while (++index < data.size()&&isRun);
            wb.write(out);
            out.flush();
        } finally {
            isRunning = false;
            disposeWork(wb);
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 生成excel文件数据,以数据库结果集为数据基础
     * 
     * @param file
     * @param setting
     * @param set
     */
    public void buildExcel(File file, ExcelComponentSet setting, ResultSet set)
            throws SQLException, ExcelProcessException, IOException,
            FileNotFoundException {
        if (isRunning) {
            throw new ExcelProcessException(PublicResource
                    .getSQLString("export.error.isrunning"));
        }
        setRun(true); //设置启动标志

        HSSFWorkbook wb = null;
        checkExcelSet(setting);

        if (isRun) {
            //获取结果集的总记录数
            total = ResultUtil.countResultRow(set);

            if (total > 0) {
                waiter.setTaskLength(total);
            }
        }
        //已经导出的行数
        index = 0;

        int fileCount = 0;
        try {
            isRunning = true;
            do {
                if(!isRun)
                    return ;
                
                wb = new HSSFWorkbook();
                createSheet(wb, setting, set, getNextFile(file, fileCount,
                        "xls"), "");
                fileCount++;
            } while (ConnectionUtil.hasNext(set) && checkBound(index)&&isRun);
        } finally {
            set.close();
            set = null;
            isRunning = false;
        }
    }

    /**
     * 判断给定行数是否在数据源范围之内，如果无法获取总的行数，那么直接返回true
     * 
     * @param count
     * @return
     */
    private boolean checkBound(int count) {
        if (total != -1) {
            return count < total;
        } else
            return true;
    }

    /**
     * 销毁wb对象
     * 
     * @param wb
     */
    private void disposeWork(HSSFWorkbook wb) {
        if (wb == null)
            return;
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                sheet.removeRow(sheet.getRow(j));
            }
            wb.removeSheetAt(i);
        }
    }

    /**
     * 生成一个表单，该表单数据建立在结果集的基础上 同时对以参数传入的工作簿对象进行注销处理
     * 
     * @param wb
     * @param setting
     * @param set
     * @param sheetName
     * @throws SQLException
     * @return --返回当前表单的行数
     */
    public int createSheet(HSSFWorkbook wb, ExcelComponentSet setting,
            ResultSet set, String fileName, String sheetName)
            throws SQLException, IOException {
        if (sheetName == null || sheetName.trim().equals(""))
            sheetName = "sheet" + (wb.getNumberOfSheets() + 1);
        HSSFSheet sheet = wb.createSheet(sheetName);

        int count = 0;
        if (setting.isDisplayHead()) //是否在第一行添加各列的标题描述
        {
            createExcelHeader(wb, sheet, setting);
            count++;
        }
        //根据设置，获取当前表单的最大行数
        int maxRows = setting.isSheets() ? setting.getRowsOfSheet()
                : ExcelComponentSet.MAXROWS_ONESHEET;

        int maxWriteRows = setting.getWriteRows();

        CellDefined[] tmpDefined = setting.getHeadDefined();
        FileOutputStream out = new FileOutputStream(fileName);
        try {
            /**
             * 该处使用的方式是为了避免使用方法ResultSet.isLast()
             */
            while (isRun && count < maxRows && set.next()) {
                createRow(count, tmpDefined, set, sheet);
                count++;
                index++;
                updateProgress();
                for (int j = 0; isRun && count < maxRows && j < maxWriteRows
                        && set.next(); count++, j++) {
                    createRow(count, tmpDefined, set, sheet);
                    index++;
                    updateProgress();
                }
            }
            wb.write(out);
            out.flush();
        } finally {
            if (out != null)
                out.close();
            if (wb != null)
                disposeWork(wb);
            wb = null;
        }
        return count;
    }

    /**
     * 建立一行数据
     * 
     * @param currentCount
     *            当前行的行号
     * @param tmpDefined
     *            列定义
     * @param set
     *            --结果集数据
     * @param sheet
     *            －－工作簿对象
     * @throws SQLException
     */
    public void createRow(int currentCount, CellDefined[] tmpDefined,
            ResultSet set, HSSFSheet sheet) throws SQLException {
        HSSFRow row = sheet.createRow(currentCount);
        for (int i = 0; isRun && i < tmpDefined.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            int type = tmpDefined[i].getType();

            cell.setCellType(type); //设置表格类型
//            if (type == HSSFCell.CELL_TYPE_STRING) {
//                cell.setEncoding(HSSFCell.ENCODING_UTF_16); //设置编码格式
//            }
            if (tmpDefined[i].getCellStyle() != null) {
                cell.setCellStyle(tmpDefined[i].getCellStyle()); //设置表格元素的风格
            }
            if (tmpDefined[i].getEncoding() != null) //如果指定了编码格式，按照编码格式进行转换
            {
                byte[] b = set.getBytes(i);
                try {
                    cell.setCellValue(new HSSFRichTextString(new String(b, tmpDefined[i + 1]
                            .getEncoding())));
                } catch (UnsupportedEncodingException e) {
                    LogProxy.outputErrorLog(e);
                    cell.setCellValue(new HSSFRichTextString("no supported encoding!"));
                }
            } else
            {
                //无编码格式，按正常取法
            	String tmpStr=set.getString(i + 1);
                cell.setCellValue(new HSSFRichTextString(set.wasNull()?"<NULL>":tmpStr));
            }
        }
    }

    /**
     * 生成一个表单，同时返回数据集合的行索引
     * 
     * @return 表单数据最后一行对应数据集合的索引
     */
    protected int createSheet(HSSFWorkbook wb, ExcelComponentSet setting,
            Vector<Vector<Object>> data, String sheetName) {
        if (sheetName == null || sheetName.trim().equals(""))
            sheetName = "sheet" + (wb.getNumberOfSheets() + 1);
        HSSFSheet sheet = wb.createSheet(sheetName);

        CellDefined[] tmpDefined = setting.getHeadDefined();
        int count = 0;
        if (setting.isDisplayHead()) //是否在第一行添加各列的标题描述
        {
            createExcelHeader(wb, sheet, setting);
            count++;
        }
        //根据设置，获取当前表单的最大行数
        int maxRows = setting.isSheets() ? setting.getRowsOfSheet()
                : ExcelComponentSet.MAXROWS_ONESHEET;

        //开始处理数据
        for (; isRun && count < maxRows && index < data.size(); count++, index++) {
            HSSFRow row = sheet.createRow(count);
            Vector<Object> rowData = (Vector<Object>) data.get(index);
            for (int i = 0; isRun && i < rowData.size(); i++) {
                HSSFCell cell = row.createCell((short) i);
                int type = tmpDefined[i].getType();
                cell.setCellType(type);
//                if (type == HSSFCell.CELL_TYPE_STRING) {
//                    cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//                }
                if (tmpDefined[i].getCellStyle() != null) {
                    cell.setCellStyle(tmpDefined[i].getCellStyle());
                }
                Object tmpData=rowData.get(i);
                cell.setCellValue(new HSSFRichTextString(tmpData==null?"<NULL>":tmpData.toString()));
            }

            updateProgress();
        }
        return index;
    }

    /**
     * 创建Excel表头信息，设置字体颜色为红色，粗体
     */
    protected void createExcelHeader(HSSFWorkbook wb, HSSFSheet sheet,
            ExcelComponentSet setting) {
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(transColor(setting.getHeadColumnColor(), wb));
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFont(font);
        CellDefined tmpDefined[] = setting.getHeadDefined();
        for (int i = 0; i < tmpDefined.length; i++) {
            HSSFCell cell = row.createCell((short) i);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(tmpDefined[i].getHeaderName()));
        }
    }

    /**
     * Check setting of excel exporter.
     * 
     * @param setting
     * @throws ExcelProcessException
     */
    private void checkExcelSet(ExcelComponentSet setting)
            throws ExcelProcessException {
        if (setting == null)
            throw new ExcelProcessException("Can't get Excel setting information,setting=null");
        CellDefined[] tmpDefined = setting.getHeadDefined();
        if (tmpDefined == null || tmpDefined.length == 0) {
            throw new ExcelProcessException("No define for columns！");
        }
    }

    /**
     * 更新进度信息
     *  
     */
    private void updateProgress() {
        waiter.setProgressValue(index);
    }

    /**
     * 进度信息
     * 
     * @return
     */
    public int getProcessInfo() {
        if (total == -1)
            return -1;
        return (index * 100 / total);
    }

    /**
     * @param isRun
     *            要设置的 isRun。
     */
    public synchronized void setRun(boolean isRun) {
        this.isRun = isRun;
    }

    /**
     * 返回该excel生成类是否正在生成excel文件
     * 
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 获取下一个后续的文件名
     * 
     * @param file
     * @param seqNo
     * @param fileType
     * @return
     */
    public static String getNextFile(File file, int seqNo, String fileType) {
        if (seqNo < 0)
            throw new IllegalArgumentException("seqNo must be positive");
        if (seqNo == 0)
            return file.getAbsolutePath();
        String type = StringUtil.getFileType(file.getName());
        if (type == null)
            return file.getAbsolutePath() + seqNo;
        else if (type.equals(fileType))
            return StringUtil.getNoTypeFileStr(file.getAbsolutePath()) + seqNo
                    + "." + type;
        else
            return file.getAbsolutePath() + seqNo + "." + fileType;
    }
}
