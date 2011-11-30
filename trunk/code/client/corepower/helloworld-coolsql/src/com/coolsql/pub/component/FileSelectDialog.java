/*
 * 创建日期 2006-6-3
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.coolsql.pub.component;

import javax.swing.JFileChooser;

/**
 * @author liu_xlin
 *文件选择对话框，增加了一个保存先前文件路径的变量
 */
public class FileSelectDialog extends JFileChooser {
    public static String oldSelectPath="";
	/**
	 * @param oldSelectPath 要设置的 oldSelectPath。
	 */
	public static void setOldSelectPath(String oldSelectPath) {
		FileSelectDialog.oldSelectPath = oldSelectPath;
	}
}
