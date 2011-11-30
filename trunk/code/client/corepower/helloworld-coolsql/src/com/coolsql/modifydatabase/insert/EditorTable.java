/*
 * Created on 2007-1-26
 */
package com.coolsql.modifydatabase.insert;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.coolsql.pub.display.CommonDataTable;
import com.coolsql.pub.display.PopMenuMouseListener;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.model.Column;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.ColumnDisplayDefinition;

/**
 * @author liu_xlin 可编辑表控件，提供了表格粘贴
 */
public class EditorTable extends CommonDataTable {
	private static final long serialVersionUID = 1L;

	/**
	 * shortdrag' process type ,define two statement for type.
	 */
	final static int SHORTDRAG_TYPE_COPY = 0; // copy the value of first cell
	final static int SHORTDRAG_TYPE_CONTINUOUS = 1; // get continuous value of
													// first cell

	private final static EditeTableCellRender cellRender = new EditeTableCellRender();

	/**
	 * variables below are used to process shortdrag.
	 */
	private Point dragBegin;
	private Point dragEnd;
	private int beginRow;
	private int beginColumn;
	private int[] dragRows;
	private int[] dragColumns;
	private int shortDragType = Setting.getInstance().getIntProperty(
			PropertyConstant.PROPERTY_EDITORTABLE_SHORTDRAGTYPE,
			SHORTDRAG_TYPE_CONTINUOUS);
	/**
	 * 如果列定义不存在，将返回null。同时如果列定义中的列对象值为null，将导致错误
	 * 
	 * @param colDefs
	 *            --列定义
	 * @return --由列定义colDefs转换而来的显示数组
	 */
	private static Object[] getHeaderList(ColumnDisplayDefinition colDefs[]) {
		if (colDefs == null || colDefs.length < 1)
			return new Object[0];

		Object[] ob = new Object[colDefs.length];
		for (int i = 0; i < colDefs.length; i++) {
			ob[i] = colDefs[i].getLabel();
		}
		return ob;
	}
	/**
	 * 单元格校验
	 */
	private CellValidation cellValidate = null;

	private boolean isHeaderPopMenu;// 是否允许在表头弹出右键菜单

	private JPopupMenu headerPopMenu;// 表头右键弹出菜单
	public EditorTable() {
		super();
		cellValidate = new DefaultCellValidation();
	}
	@SuppressWarnings("unchecked")
	public EditorTable(List data, ColumnDisplayDefinition colDefs[]) {
		this(data, colDefs, null);
	}

	public EditorTable(Object[][] data, ColumnDisplayDefinition colDefs[]) {
		super(new EditorTableModel(data, getHeaderList(colDefs)), null);
		initGUI(colDefs);
		cellValidate = new DefaultCellValidation();
	}
	@SuppressWarnings("unchecked")
	public EditorTable(List data, ColumnDisplayDefinition colDefs[],
			int[] renderCols) {
		super(
				new EditorTableModel(data, Arrays
						.asList(getHeaderList(colDefs))), renderCols);
		initGUI(colDefs);
		cellValidate = new DefaultCellValidation();
	}

	public EditorTable(TableModel model) {
		super(model);
		initGUI(null);
		cellValidate = new DefaultCellValidation();
	}

	/**
	 * 初始化界面
	 * 
	 */
	protected void initGUI(ColumnDisplayDefinition colDefs[]) {
		setAutoCreateColumnsFromModel(false);
		setTableHeader(new EditorTableHeader());
		getTableHeader().addMouseListener(new TableHeaderMouse());
		getTableHeader().setReorderingAllowed(true);

		EditorTableMouseInputHandler mouseHandler = new EditorTableMouseInputHandler();
		this.addMouseMotionListener(mouseHandler);
		this.addMouseListener(mouseHandler);

		TableColumnModel columnModel = createColumnModel(colDefs);
		if (columnModel != null)
			setColumnModel(columnModel);
		setSelection();
		ToolTipManager.sharedInstance().registerComponent(this); // 向信息提示管理器注册此表控件
	}
	protected void setSelection() {
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(true);
		setCellSelectionEnabled(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	/**
	 * 使用新的数据结构替换旧数据
	 * 
	 * @param list
	 *            --新数据集合
	 * @param colDefs
	 *            --列定义
	 */
	@SuppressWarnings("unchecked")
	public void setDataList(List list, ColumnDisplayDefinition colDefs[]) {
		EditorTableModel model = (EditorTableModel) getModel();
		model.setDataList(list, Arrays.asList(getHeaderList(colDefs)));
		TableColumnModel columnModel = createColumnModel(colDefs);
		if (columnModel != null) {
			setColumnModel(columnModel);
			setSelection();
		}
	}
	/**
	 * 根据给定的列定义，创建表控件的列模型对象 根据列定义的宽度，以及表头的字体来设置表头的最佳宽度
	 * 
	 * @param colDefs
	 *            --列定义数组
	 * @return --表控件的列模型对象
	 */
	private TableColumnModel createColumnModel(
			ColumnDisplayDefinition colDefs[]) {
		if (colDefs == null || colDefs.length < 1)
			return null;;
		TableColumnModel cm = new DefaultTableColumnModel();

//		FontMetrics fm = getTableHeader().getFontMetrics(
//				getTableHeader().getFont());

		float factor = 1f;
		int addtional = (int) (factor * 4);

		EditeTableEditor tableEditor = new EditeTableEditor();
		for (int i = 0; i < colDefs.length; i++) {
			ColumnDisplayDefinition colDef = colDefs[i];

			int colWidth = (int) (factor * colDef.getDisplayWidth())
					+ addtional;
			TableColumn col = new TableColumn(i, colWidth, cellRender,
					tableEditor);
			col.setHeaderValue(new EditeTableHeaderCell(true, (Column) colDef
					.getHeaderValue()));
			cm.addColumn(col);
		}
		return cm;
	}

	/**
	 * 覆写父类获取对应类型的缺省表格元素渲染类
	 */
	@SuppressWarnings("unchecked")
	public TableCellRenderer getDefaultRenderer(Class columnClass) {
		return cellRender;
	}

	/**
	 * 默认所有表格都可以编辑
	 */
	public boolean isCellEditable(int row, int column) {
		return true;
	}
	/**
	 * 在修改单元格值时，是否进行新值的校验
	 * 
	 * @return true:校验 false:不校验
	 */
	public boolean isCheckValue() {
		TableModel model = getModel();
		if (model instanceof EditorTableModel) {
			return ((EditorTableModel) model).isCheckValue();
		} else
			return false;
	}
	public void setCheckValue(boolean isValueCheck) {
		TableModel model = getModel();
		if (model instanceof EditorTableModel) {
			((EditorTableModel) model).setCheckValue(isValueCheck);
		}
	}
	/**
	 * @return Returns the cellValidate.
	 */
	public CellValidation getCellValidate() {
		return cellValidate;
	}
	/**
	 * @param cellValidate
	 *            The cellValidate to set.
	 */
	public void setCellValidate(CellValidation cellValidate) {
		this.cellValidate = cellValidate;
	}
	public String getToolTipText(MouseEvent e) {
		if (isCheckValue()) // 如果校验单元格数据，才进行相关的错误信息提示
		{
			int row = rowAtPoint(e.getPoint());
			int column = columnAtPoint(e.getPoint());
			Object value = getValueAt(row, column);
			if (value == null)
				return null;

			if (value instanceof EditeTableCell) {
				EditeTableCell tc = (EditeTableCell) value;
				if (tc.isNull())
					return PublicResource
							.getUtilString("editortable.cellcheck.null");
			}
			EditeTableHeaderCell cell = (EditeTableHeaderCell) getColumnModel()
					.getColumn(column).getHeaderValue();
			Column col = (Column) cell.getHeaderValue();
			if (col.isNumeric()) {
				return StringUtil.isNumberString(value.toString(), (int) col
						.getSize(), col.getNumberOfFractionalDigits());
			} else {
				if (StringUtil.checkLength(value.toString(), (int) col
						.getSize(), true))
					return null;
				else
					return PublicResource
							.getUtilString("editortable.cellcheck.str.overlength")
							+ col.getSize();

			}
		} else
			return null;
	}
	/**
	 * 
	 * @author liu_xlin 缺省的单元格校验类。 分别对文本型和数值型数据的校验
	 */
	protected class DefaultCellValidation implements CellValidation {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.coolsql.modifydatabase.insert.CellValidation#checkValidation(java.lang.Object,
		 *      int, int)
		 */
		public boolean checkValidation(Object value, int row, int column) {
			EditeTableHeaderCell cell = (EditeTableHeaderCell) getColumnModel()
					.getColumn(column).getHeaderValue();
			Column col = (Column) cell.getHeaderValue();
			if (col.isNumeric()) {
				return StringUtil.isNumberStr(value.toString(), (int) col
						.getSize(), col.getNumberOfFractionalDigits());
			} else {
				return StringUtil.checkLength(value.toString(), (int) col
						.getSize(), true);
			}
		}

	}
	/**
	 * @return Returns the headerPopMenu.
	 */
	public JPopupMenu getHeaderPopMenu() {
		return headerPopMenu;
	}
	/**
	 * @param headerPopMenu
	 *            The headerPopMenu to set.
	 */
	public void setHeaderPopMenu(JPopupMenu headerPopMenu) {
		this.headerPopMenu = headerPopMenu;
	}
	/**
	 * @return Returns the isHeaderPopMenu.
	 */
	public boolean isHeaderPopMenu() {
		return isHeaderPopMenu;
	}
	/**
	 * @param isHeaderPopMenu
	 *            The isHeaderPopMenu to set.
	 */
	public void setHeaderPopMenu(boolean isHeaderPopMenu) {
		this.isHeaderPopMenu = isHeaderPopMenu;
	}
	/**
	 * 
	 * @author liu_xlin 表头右键弹出菜单的弹出处理
	 */
	protected class TableHeaderMouse extends PopMenuMouseListener {
		public void mouseReleased(MouseEvent e) {
			if (isPopupTrigger(e)&& isHeaderPopMenu()
					&& headerPopMenu != null) {
				headerPopMenu.show((JComponent) e.getSource(), e.getX(), e
						.getY());
			}
		}
		
	}

	private static final Cursor crossCursor = Cursor
			.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	/**
	 * mouse input handler for continuous cell dragging.
	 * 
	 * @author 刘孝林(kenny liu)
	 * 
	 * 2008-1-28 create
	 */
	private class EditorTableMouseInputHandler extends MouseInputAdapter {
		private Cursor otherCursor = crossCursor;
		/**
		 * a rectangle area where user can process shortdrag ,and two varialbe
		 * below is the width and height of rectangle.
		 */
		private int shortDragWidth;
		private int shortDragHeight;

		public EditorTableMouseInputHandler() {
			this(4, 4);
		}
		public EditorTableMouseInputHandler(int shortDragWidth,
				int shortDragHeight) {
			this.shortDragWidth = shortDragWidth;
			this.shortDragHeight = shortDragHeight;
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			if (canShortDrag(e.getPoint())) {
				if (getCursor() != crossCursor)
					swapCursor();
			} else {
				if (getCursor() == crossCursor)
					swapCursor();
			}
		}
		private boolean canShortDrag(Point p) {
			int row = rowAtPoint(p);
			int column = columnAtPoint(p);
			if (!isCellSelected(row, column)) // return false directly if cell
												// which cursor is on is not
												// selected
				return false;
			Rectangle r = getCellRect(row, column, false);
			r.setBounds(r.x + r.width - shortDragWidth, r.y + r.height
					- shortDragHeight, shortDragWidth, shortDragHeight);
			if (r.contains(p)) {
				return true;
			} else
				return false;
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (!isShortDrag())
				return;
			beginRow = rowAtPoint(e.getPoint());
			beginColumn = columnAtPoint(e.getPoint());

			Rectangle r = getCellRect(beginRow, beginColumn, false);
			dragBegin = r.getLocation();
			dragEnd = new Point((int) (r.getX() + r.getWidth()), (int) (r
					.getY() + r.getHeight()));

//			dragRows = new int[0];
//			dragColumns = new int[0];
			repaint();
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!isShortDrag())
				return;
			processShortDragPaint(e);
			repaint();
		}
		private void processShortDragPaint(MouseEvent e) {
			int currentRow = rowAtPoint(e.getPoint());
			int currentColumn = columnAtPoint(e.getPoint());

			if (currentRow == beginRow || currentColumn == beginColumn) {
				sameRowOrColumn();
				return;
			}

			int[] rows = getSelectedRows();
			int[] columns = getSelectedColumns();

			if (orient(currentRow, currentColumn, e.getPoint()))// row
			{
				Rectangle r1 = getCellRect(rows[0], beginColumn, false);
				dragBegin = r1.getLocation();
				r1 = getCellRect(rows[rows.length - 1], beginColumn, false);
				dragEnd = new Point(r1.x + r1.width, r1.y + r1.height);

				dragRows = rows;
				dragColumns = new int[]{beginColumn};
			} else // column
			{
				Rectangle r1 = getCellRect(beginRow, columns[0], false);
				dragBegin = r1.getLocation();
				r1 = getCellRect(beginRow, columns[columns.length - 1], false);
				dragEnd = new Point(r1.x + r1.width, r1.y + r1.height);

				dragRows = new int[]{beginRow};
				dragColumns = columns;
			}
		}
		private void sameRowOrColumn() {
			int[] rows = getSelectedRows();
			int[] columns = getSelectedColumns();
			Rectangle r = getCellRect(rows[0], columns[0], false);
			dragBegin = r.getLocation();

			r = getCellRect(rows[rows.length - 1], columns[columns.length - 1],
					false);
			dragEnd = new Point((int) (r.getX() + r.getWidth()), (int) (r
					.getY() + r.getHeight()));

			dragRows = rows;
			dragColumns = columns;
		}
		/**
		 * return true if paint oriented-row,return false if paint
		 * oriented-column
		 * 
		 * @return
		 */
		private boolean orient(int currentRow, int currentColumn,
				Point currentPoint) {
			if (dragRows.length == 1 && dragColumns.length == 1)
				return true;

			int width = 0;
			int validateWidth = 0;
			Rectangle r = getCellRect(beginRow, beginColumn, false);
			if (dragRows.length == 1)// column
			{
				for (int i = 0; i < dragColumns.length; i++) {
					if (dragColumns[i] == beginColumn)
						continue;
					width += getColumnModel().getColumn(dragColumns[i])
							.getWidth();
				}
				if (currentRow > beginRow) {
					if (currentColumn > beginColumn) // top-left
					{
						validateWidth = currentPoint.y - r.y - r.height;

					} else // top-right
					{

						validateWidth = currentPoint.y - r.y - r.height;

					}
				} else {
					if (currentColumn > beginColumn) // bottom-left
					{
						validateWidth = r.y - currentPoint.y;

					} else // bottom-right
					{

						validateWidth = r.y - currentPoint.y;

					}
				}
				if (validateWidth < 0)
					LogProxy
							.errorLog("caculate error:validateWidth is negative(column)");
				if (width >= validateWidth)
					return false;
				else
					return true;
			} else // row
			{
				width = getRowHeight() * (dragRows.length - 1);
				if (currentRow > beginRow) {
					if (currentColumn > beginColumn) // top-left
					{

						validateWidth = currentPoint.x - r.x - r.width;

					} else // top-right
					{
						validateWidth = r.x - currentPoint.x;

					}
				} else {
					if (currentColumn > beginColumn) // bottom-left
					{
						validateWidth = currentPoint.x - r.x - r.width;
					} else // bottom-right
					{

						validateWidth = r.x - currentPoint.x;
					}
				}
				if (validateWidth < 0)
					LogProxy
							.errorLog("caculate error:validateWidth is negative(row)");
				if (width >= validateWidth)
					return true;
				else
					return false;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (!isShortDrag())
				return;

			swapCursor();
			processShortDragValue();
			dragBegin = null;
			dragEnd = null;
			beginRow = -1;
			beginColumn = -1;
			dragRows = null;
			dragColumns = null;
			repaint();

		}
		private void swapCursor() {
			Cursor tmp = getCursor();
			setCursor(otherCursor);
			otherCursor = tmp;
		}
		private boolean isShortDrag() {
			return getCursor() == crossCursor;
		}
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (null != dragBegin && null != dragEnd
				&& false == dragBegin.equals(dragEnd)) {

			int x = Math.min(dragBegin.x, dragEnd.x);
			int y = Math.min(dragBegin.y, dragEnd.y);
			int width = Math.abs(dragBegin.x - dragEnd.x);
			int heigh = Math.abs(dragBegin.y - dragEnd.y);

			Color colBuf = g.getColor();
			g.setColor(Color.blue);
			g.drawRect(x, y, width, heigh);
			g.drawRect(x - 1, y - 1, width, heigh);
			g.setColor(colBuf);
		}
	}
	/**
	 * After shortdrag,process relative cells'value.
	 * 
	 */

	protected void processShortDragValue() {
		if (dragBegin != null && null != dragEnd && dragRows != null
				&& dragColumns != null
				&& (dragRows.length > 1 || dragColumns.length > 1)) {
			Object ob = getValueAt(beginRow, beginColumn);
			if (ob == null || StringUtil.isEmpty(ob.toString()))
				return;
			String value = ob.toString();
			EditorTableModel model = (EditorTableModel) getModel();
			if (dragRows.length == 1)// column
			{
				for (int i = 0; i < dragColumns.length; i++) {
					String newValue = getRecursiveValue(value, dragColumns[i]
							- beginColumn);
					model.setValueNoNotify(newValue, beginRow, dragColumns[i]);
				}
			} else {
				for (int i = 0; i < dragRows.length; i++) {
					int increment = dragRows[i] - beginRow;
					String newValue = getRecursiveValue(value, increment);
					model.setValueNoNotify(newValue, dragRows[i], beginColumn);
				}
			}
			model.fireTableStructureChanged();
		}
	}
	/**
	 * get the type of shortdrag.
	 * 
	 * @see #SHORTDRAG_TYPE_CONTINUOUS
	 * @see #SHORTDRAG_TYPE_COPY
	 * @return
	 */
	public int getShortDragType() {
		return shortDragType;
	}
	public void setShortDragType(int type) throws IllegalArgumentException {
		if (type == SHORTDRAG_TYPE_CONTINUOUS || type == SHORTDRAG_TYPE_COPY)
			shortDragType = type;
		else
			throw new IllegalArgumentException("you must specify a valid type:"
					+ SHORTDRAG_TYPE_CONTINUOUS + " or " + SHORTDRAG_TYPE_COPY);
	}
	/**
	 * get the recursive value of param :value. if value is number value,it'll
	 * return a number value that param:value plus increment;
	 * 
	 * @param value
	 *            --the number char'value will be inreased;
	 * @param increment
	 * @return
	 */
	private String getRecursiveValue(String value, int increment) {
		if (getShortDragType() == SHORTDRAG_TYPE_COPY)
			return value;
		if (StringUtil.isEmpty(value))
			return value;
		if (increment == 0)
			return value;

		/**
		 * validate whethe or not value variable is a number value.
		 */
		try {
			long longValue = Long.parseLong(value);
			String stringValue= String.valueOf(longValue + increment);// return directly
															// if value variable
															// is a number
															// value.
			return StringUtil.padStringLeft(stringValue, value.length(), '0');
		} catch (Exception e) {
		}

		StringBuilder newValue = new StringBuilder("");
		boolean isNumberHappen = false;
		for (int i = value.length() - 1; i > -1; i--) {
			char c = value.charAt(i);
			if (Character.isDigit(c) && !isNumberHappen) {
				StringBuilder numberStr = new StringBuilder("" + c);
				while (i > 0) {
					c = value.charAt(i - 1);
					if (Character.isDigit(c)) {
						i--;
						numberStr.append(c);
					} else
						break;
				}
				isNumberHappen = true;
				numberStr = numberStr.reverse();
				int originalLength=numberStr.length();
				Long number = Long.parseLong(numberStr.toString());
				numberStr.delete(0, numberStr.length());
				String longStr=String.valueOf(Math.abs(number + increment));
				numberStr.append(StringUtil.padStringLeft(longStr,originalLength,'0'));
				newValue.append(numberStr.reverse());
			} else
				newValue.append(c);
		}
		return newValue.reverse().toString();
	}
}
