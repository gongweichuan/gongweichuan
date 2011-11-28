/*
 * 创建日期 2006-9-14
 */
package com.coolsql.pub.display;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.coolsql.pub.component.BaseMenuManage;

/**
 * @author liu_xlin 显示基本信息时所用的表控件
 */
public class CommonDataTable extends BaseTable {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否进行信息提示，默认为提示
	 */
	private boolean isToolTip = true;

	/**
	 * 是否有右键菜单，默认为弹出
	 */
	private boolean isPopMenu = true;

	/**
	 * 表控件右键菜单
	 */
	private BaseMenuManage menu = null;
	public CommonDataTable() {
		super();
	}
	public CommonDataTable(TableModel model) {
		this(model, null);
	}
	public CommonDataTable(TableModel model, int[] renderCols) {
		this(model, renderCols, null);
		menu = new TableMenu(this);
	}
	public CommonDataTable(Vector<?> data, Vector<?> header) {
		this(data, header, null);
	}

	public CommonDataTable(Object[][] data, Object[] header) {
		this(data, header, null);
	}
	/**
	 * 初始化表控件，添加数据，同时进行表格元素渲染
	 * 
	 * @param data
	 *            数据
	 * @param header
	 *            表头定义
	 * @param renderCols
	 *            定义被渲染的列
	 */
	public CommonDataTable(Vector<?> data, Vector<?> header, int[] renderCols) {
		this(data, header, renderCols, null);
		menu = new TableMenu(this);
	}

	public CommonDataTable(Vector<?> data, Vector<?> header, int[] renderCols,
			TableMenu menu) {
		this(new CommonTableModel(data, header), renderCols, menu);

	}
	public CommonDataTable(TableModel model, int[] renderCols, TableMenu menu) {
		super(model);
		// getTableHeader().setUI(new IBasicTableHeaderUI());
		getTableHeader().setReorderingAllowed(false);
		setCellSelectionEnabled(true);
		this.menu = menu;
		setEnableToolTip(true);
		TableMouse tm = new TableMouse();
		addMouseListener(tm);
		addMouseMotionListener(tm);

		if (renderCols != null && renderCols.length > 0) {
			IconTableCellRender render = new IconTableCellRender();
			for (int i = 0; i < renderCols.length; i++) {
				getColumnModel().getColumn(renderCols[i]).setCellRenderer(
						render);
			}
		}
	}
	/**
	 * 初始化表控件，添加数据，同时进行表格元素渲染
	 * 
	 * @param data
	 *            数据
	 * @param header
	 *            表头定义
	 * @param renderCols
	 *            定义被渲染的列
	 */
	public CommonDataTable(Object[][] data, Object[] header, int[] renderCols) {
		this(data, header, renderCols, null);
		menu = new TableMenu(this);
	}
	public CommonDataTable(Object[][] data, Object[] header, int[] renderCols,
			TableMenu menu) {
		this(new CommonTableModel(data, header), renderCols, menu);
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	/**
	 * 设置表控件信息提示的可用性
	 * 
	 * @param isTip
	 */
	public void setEnableToolTip(boolean isTip) {
		this.isToolTip = isTip;
		if (!isTip) {
			this.setToolTipText(null);
		}
	}
	/**
	 * 返回当前table控件是否进行表格信息的提示
	 * 
	 * @return true if prompt,false if not
	 */
	public boolean isToolTip() {
		return this.isToolTip;
	}
	/**
	 * 设置右键菜单是否弹出
	 * 
	 * @param isDisplay
	 */
	public void setPopMenuDisplay(boolean isDisplay) {
		isPopMenu = isDisplay;
	}
	/**
	 * 该方法用于返回当前table控件的右键菜单是否显示“信息提示”的菜单项
	 * 
	 * @return 默认情况下显示，可重写该方法
	 */
	public boolean isDisplayToolTipSelectMenu() {
		return true;
	}
	/**
	 * 
	 * @author liu_xlin 表的鼠标监听器，提示信息以及右键菜单的处理都封装于此
	 */
	protected class TableMouse extends MouseAdapter
			implements
				MouseMotionListener {
		boolean isPopupTriggerWhenPress;
		/**
		 * 重写父类方法。根据鼠标悬停行，进行信息提示
		 * 
		 * @param e
		 */
		public void mouseMoved(MouseEvent e) {
			if (!isToolTip) // 不使用信息提示
				return;
			if (e.getSource() instanceof CommonDataTable) {
				CommonDataTable source = (CommonDataTable) e.getSource();
				int row = source.rowAtPoint(e.getPoint()); // 鼠标所在行
				int cols = source.getColumnCount();

				String displayText = "<html><table border=0><tr>";
				for (int i = 0; i < cols; i++) {
					displayText += "<th align=left color=blue>"
							+ source.getColumnName(i) + "</th>";
				}
				displayText += "</tr><tr>";
				for (int i = 0; i < cols; i++) {
					Object tmpOb = source.getValueAt(row, i);
					displayText += "<td align=left>"
							+ (tmpOb != null ? tmpOb.toString() : "") + "</th>";
				}
				displayText += "</tr></table></html>";

				setToolTipText(displayText); // 设置信息提示内容
			}
		}
		/**
		 * 处理右键菜单的弹出
		 */
		public void mouseReleased(MouseEvent e) {
			if (isPopupTriggerWhenPress||e.isPopupTrigger()) {
				if (isPopMenu && menu != null) // 如果允许弹出右键菜单
					menu.getPopMenu().show((JTable) e.getSource(), e.getX(),
							e.getY());
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			isPopupTriggerWhenPress=e.isPopupTrigger();
		}
		/*
		 * （非 Javadoc）
		 * 
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
		 */
		public void mouseDragged(MouseEvent e) {

		}
	}
	public void setMenuManage(BaseMenuManage bmm) {
		menu = bmm;
	}
	/**
	 * @return 返回 menu。
	 */
	public BaseMenuManage getPopMenuManage() {
		return menu;
	}
}
