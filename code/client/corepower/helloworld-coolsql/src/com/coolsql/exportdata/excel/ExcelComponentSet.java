/*
 * 创建日期 2006-8-30
 *
 */
package com.coolsql.exportdata.excel;

import java.awt.Color;

/**
 * @author liu_xlin
 *         定义excel导出时的基本数据信息，包含了如下信息：每个文档导出的最大行数、每个工作簿的最大行数，是否允许多个工作簿，如果允许，则限制最大的数量
 */
public class ExcelComponentSet {
    public static final int MAXROWS_ONESHEET = 60000; //文件只有一个表单时，限制的最大行数

    public static final int maxSheet = 6; //最大表单数

    public static final int MAXROWS_SHEETS = 20000; //文件允许多个表单时，每个表单的最大行数

    public static final int MAXROWS_WRITE = 60000; //缓冲区中累计60000条时向文件写一次

    public static final int DEFAULT_WRITE=6000;
    /**
     * 是否划分为多表单
     */
    private boolean isSheets = false;

    /**
     * 划分的表单数 ***暂不使用***
     */
    private int sheets = 1;

    /**
     * 每个表单的行数设置
     */
    private int rowsOfSheet = MAXROWS_SHEETS;

    /**
     * 内存累计数据写入文件的条数
     */
    private int writeRows = DEFAULT_WRITE;

    /**
     * 数据列定义
     */
    private CellDefined[] headDefined = null;

    /**
     * 第一行是否显示列名描述
     */
    private boolean isDisplayHead = false;

    private Color headColumnColor;
    public ExcelComponentSet() {
        this(null, false, true, MAXROWS_ONESHEET, DEFAULT_WRITE,Color.RED);
    }

    public ExcelComponentSet(CellDefined[] headDefined, boolean isSheets,
            boolean isDisplayHead, int rowsOfSheet, int writeRows,Color headColumnColor) {
        this.headDefined = headDefined;
        this.isSheets = isSheets;
        this.isDisplayHead = isDisplayHead;

        if (rowsOfSheet < 1)
            this.rowsOfSheet = MAXROWS_SHEETS;
        else if (rowsOfSheet > MAXROWS_SHEETS)
            this.rowsOfSheet = MAXROWS_SHEETS;
        else
            this.rowsOfSheet = rowsOfSheet;

        if (writeRows < 1 || writeRows > MAXROWS_WRITE) {
            this.writeRows = MAXROWS_WRITE;
        } else
            this.writeRows = writeRows;
        
        if(headColumnColor==null)
        {
        	this.headColumnColor=Color.RED;
        }else
        {
        	this.headColumnColor=headColumnColor;
        }
    }

    public CellDefined[] getHeadDefined() {
        return headDefined;
    }

    public void setHeadDefined(CellDefined[] headDefined) {
        this.headDefined = headDefined;
    }

    public boolean isDisplayHead() {
        return isDisplayHead;
    }

    public void setDisplayHead(boolean isDisplayHead) {
        this.isDisplayHead = isDisplayHead;
    }

    public boolean isSheets() {
        return isSheets;
    }

    public void setSheets(boolean isSheets) {
        this.isSheets = isSheets;
    }

    public int getSheets() {
        return sheets;
    }

    public void setSheets(int sheets) {
        this.sheets = sheets;
    }

    /**
     * @return 返回 rowsOfSheet。
     */
    public int getRowsOfSheet() {
        return rowsOfSheet;
    }

    /**
     * @param rowsOfSheet
     *            要设置的 rowsOfSheet。
     */
    public void setRowsOfSheet(int rowsOfSheet) {
        this.rowsOfSheet = rowsOfSheet;
    }

    /**
     * @return 返回 writeRows。
     */
    public int getWriteRows() {
        return writeRows;
    }

    /**
     * @param writeRows
     *            要设置的 writeRows。
     */
    public void setWriteRows(int writeRows) {
        this.writeRows = writeRows;
    }

	/**
	 * @return the headColumnColor
	 */
	public Color getHeadColumnColor() {
		return this.headColumnColor;
	}
	/**
	 * @param headColumnColor the headColumnColor to set
	 */
	public void setHeadColumnColor(Color headColumnColor) {
		this.headColumnColor = headColumnColor;
	}
}
