/*
 * 创建日期 2006-8-30
 *
 */
package com.coolsql.exportdata.excel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import com.coolsql.sql.util.TypesHelper;

/**
 * @author liu_xlin
 *定义了表单头列的显示风格
 */
public class CellDefined {
	/**
	 * 元素风格
	 */
	private HSSFCellStyle cellStyle=null;
	/**
	 * 元素类型
	 */
	private int type=-1;
	/**
	 * 列名
	 */
	private String headerName=null;
	/**
	 * 默认为数字类型
	 *
	 */
	/**
	 * 对于结果集的Excel导出，可能需要编码格式的转换,默认无编码格式
	 */
	private String encoding=null;
	public CellDefined()
	{
		this(0,"");
	}
	public CellDefined(int type ,String name)
	{
		this(type,name,null);
	}
	public CellDefined(int type,String name,HSSFCellStyle cellStyle)
	{
	  this.cellStyle=cellStyle;	
	  if(type<0||type>5)
	  {
	  	throw new IllegalArgumentException("参数错误，表格元素类型不正确：type="+type);
	  }	  
	  this.type=type;
	  headerName=name;
	}	
	public HSSFCellStyle getCellStyle() {
		return cellStyle;
	}
	public void setCellStyle(HSSFCellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	/**
	 *根据结果集，获取结果集所对应字段的列定义
	 * @param set
	 * @return
	 * @throws ExcelProcessException
	 */
	public static CellDefined[] createInstanceOfResult(ResultSet set) throws ExcelProcessException
	{
		if(set==null)
			throw new ExcelProcessException("无结果集！");
		try {
			ResultSetMetaData metaData=set.getMetaData();
			int colCount=metaData.getColumnCount();
			CellDefined[] defined=new CellDefined[colCount];
			for(int i=0;i<colCount;i++)
			{
				int type=metaData.getColumnType(i+1);
				if(TypesHelper.isText(type))   //如果字段为文本类型
				{
					type=HSSFCell.CELL_TYPE_STRING;
				}else if(TypesHelper.isNumberic(type))  //如果字段为数据型
				{
					type=HSSFCell.CELL_TYPE_NUMERIC;
				}else                             //其他情况暂定为文本型
					type=HSSFCell.CELL_TYPE_STRING;
				String des=metaData.getColumnLabel(i+1);
				CellDefined tmp=new CellDefined(type,des);
			    defined[i]=tmp;
			}
			return defined;
		} catch (SQLException e) {
			throw new ExcelProcessException(e);
		}
		
	}
	/**
	 * @return 返回 encoding。
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * @param encoding 要设置的 encoding。
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
