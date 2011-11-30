/*
 * 创建日期 2006-6-26
 *
 */
package com.coolsql.view.resultset;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.display.BaseTable;
import com.coolsql.pub.display.BaseTableCellRender;
import com.coolsql.pub.display.DataTran;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.view.resultset.action.DataSetTableCopyAction;


/**
 * @author liu_xlin 数据库结果集展示表控件
 */
public class DataSetTable extends BaseTable implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	/**
	 * The icon should be display with table row number if the row should be deleted.
	 */
	private Icon deletedRowIcon;
	private static Color modifiedCellHighlight;  //The foreground of cell value of which has been modifyed
	static 
	{
		modifiedCellHighlight=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT,
        		Color.blue);
		Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
		{

			public void propertyChange(PropertyChangeEvent evt) {
				modifiedCellHighlight=Setting.getInstance().getColorProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT,
		        		Color.blue);
			}
			
		},
				PropertyConstant.PROPERTY_VIEW_RESULTSET_MODIFIEDCELL_HIGHLIGHT);
	}
	/**
     * 用于保存修改前的值
     */
    private Object oldOb = null;
    //修改前表格的最新值
    private Object sourceOb=null;

    private boolean isSaved = true;

    private boolean isEditable=true; //Indicate whether the table can be edited, this variant controls all cells.
    public DataSetTable() {
        this((Vector<Object>)null, null);
    }

    public DataSetTable(ColumnDisplayDefinition[] cols) {
        super();
        pubInit(null, cols);
    }

    public DataSetTable(Vector<Object> data, ColumnDisplayDefinition[] cols) {
        super();
        pubInit(data, cols);
    }
    public DataSetTable(Object[][] data, ColumnDisplayDefinition[] cols) {
        super();
        pubInit(DataTran.convertToVector(data), cols);
    }

    private void pubInit(Vector<Object> data, ColumnDisplayDefinition[] cols) {
        SortableTableModel stm = new SortableTableModel(new DefaultTableModel());
        
//        getTableHeader().setResizingAllowed(true);
//        getTableHeader().setReorderingAllowed(true);
        setAutoCreateColumnsFromModel(false);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setModel(stm);
        
        createTableHeader();
        
        this.addPropertyChangeListener(this);
        TableColumnModel tcm = createColumnModel(cols);
        setColumnModel(tcm);
        setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(true);
        setCellSelectionEnabled(true);
        
        //Cover the keystroke
        KeyStroke ks=KeyStroke.getKeyStroke("ctrl C");
        registerKeyboardAction(new DataSetTableCopyAction(), ks, JComponent.WHEN_FOCUSED);
        
        this.addMouseListener(new MouseClick());
        stm.addTableModelListener(new TableUpdateListener());
        stm.addSortModelListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(SortableTableModel.PROPERTYNAME_ORDER)) {
					firePropertyChange(PROPERTY_DELETEDROW, -1, getRowCount());
				}
			}
        	
        });
        stm.addTableModelModifyListener(new TableModelModifyListener() {

			public void dataChanged(TableModelModifyEvent e) {
				if (e.getActionType() == TableModelModifyEvent.ACTION_TYPE_DELETE || 
						e.getActionType() == TableModelModifyEvent.ACTION_TYPE_CANCEL_DELETE) {
					firePropertyChange(PROPERTY_DELETEDROW, -1, getRowCount());
				}
				
			}
        	
        });
        
        if (data != null)
            stm.insertRows(data);
        this.adjustPerfectWidth();
        
        putClientProperty("JTable.autoStartsEdit", false);
        
        deletedRowIcon = PublicResource.getIcon("resultView.datasettable.deletedrow.icon");
    }
    @Override
    public boolean isCellEditable(int row,int column)
    {
    	return isEditable&&super.isCellEditable(row, column);
    }
    public void setEditable(boolean isEditable)
    {
    	this.isEditable=isEditable;
    }
    public boolean isEditable()
    {
    	return this.isEditable;
    }
    /**
     * 创建表头
     *
     */
    protected JTableHeader createTableHeader()
    {
        ButtonTableHeader header = new ButtonTableHeader(this);
        setTableHeader(header);

        header.setSortable(Setting.getInstance().
				getBoolProperty(PropertyConstant.PROPERTY_VIEW_RESULTSET_DATASETTABLE_ISSORTABLE, true));
        
        return header;
    }
    /**
     * 设置表头信息
     * 
     * @param colDefs
     */
    public void setColumnDisplayDefinition(ColumnDisplayDefinition colDefs[]) {
        this.setColumnModel(createColumnModel(colDefs));

    }
    /**
     * 获取界面上的展示数据,该控件，保存了修改后的表格元素值，同时也保存了修改前的值，该方法有助于数据的复制或者其他相关处理
     * @param row -- row index in the view
     * @param column --column index in the view
     */
    public Object getDisplayData(int row,int column)
    {
        SortableTableModel stm = (SortableTableModel) getModel();
        
        return stm.getDisplayValue(row, convertColumnIndexToModel(column));
    }
    /**
     * 设置表控件是否允许排序
     */
    public void setSortable(boolean isSortable)
    {
        ButtonTableHeader header=(ButtonTableHeader)this.getTableHeader();
        header.setSortable(isSortable);
    }
    /**
     * 返回是否允许排序
     */
    public boolean isSortable()
    {
        return ((ButtonTableHeader)this.getTableHeader()).isSortable();
    }
    @Override
    public Icon getRowIcon(int row) {
    	if (isShouldDelete(row)) {
    		return deletedRowIcon;
    	} else {
    		return null;
    	}
    }
    /**
     * 创建表头
     */
    private TableColumnModel createColumnModel(
            ColumnDisplayDefinition colDefs[]) {
        if (colDefs == null)
            colDefs = new ColumnDisplayDefinition[0];
        TableColumnModel cm = new DefaultTableColumnModel();
        MyCellEditor editor = new MyCellEditor(new TextEditor());
        
        CellRenderer cellRender=new CellRenderer();
        
        for (int i = 0; i < colDefs.length; i++) {
            ColumnDisplayDefinition colDef = colDefs[i];
            
            TableColumn col = new TableColumn(i, 0,cellRender,
                    editor);
            col.setHeaderValue(colDef.getHeaderValue());
            cm.addColumn(col);
        }
        return cm;
    }
    /**
     * 重置渲染状态
     *
     */
    public void clearRenderState()
    {
        SortableTableModel model = (SortableTableModel) this.getModel();
        model.clearRenderState();  //清空模型的状态
        
        ButtonTableHeader header=(ButtonTableHeader)getTableHeader();
        header.clearState();  //清空表头的状态
    }
    /**
     * 更新所有数据
     */
    public void updateAllData(Vector<Object> data)
    {
        clearRenderState();
        SortableTableModel model = (SortableTableModel) this.getModel();
        model.setDataVector(data,getColumns());
        model.rowInfoReset(data.size());
    }
    /**
     * Get all column  name.
     */
    public Vector<String> getColumns()
    {
        SortableTableModel model = (SortableTableModel) this.getModel();
        return model.getColumns();
    }
    /**
     * 
     * @return cells which have been modified . The element of list is int array size of which is 2.
     * first element is row in the table model, second element is column in the table model.
     */
    public List<int[]> getModifiedCells()
    {
    	List<int[]> list=new ArrayList<int[]>();
    	SortableTableModel model = (SortableTableModel) this.getModel();
    	Iterator<String> it = model.modifyData.keySet().iterator();
    	while(it.hasNext())
    	{
    		String key=(String)it.next();
    		int[] result=StringUtil.resolveString(key);
    		if(result==null)
    			continue;
    		
    		list.add(result);
    	}
    	
    	return list;
    }
    /**
     * Return model index corresponding to view index : row.
     * @param row the index in view.
     */
    public int getRealRow(int row) {
        SortableTableModel model = (SortableTableModel) this.getModel();
        return model._indexes[row].intValue();
    }
    /**
     * To mark the row as deleted.
     * @param row the row should be deleted.
     */
    public void markDeletedRow(int row) {
    	SortableTableModel model = (SortableTableModel) this.getModel();
    	model.markDeletedRow(model._indexes[row]);
    	
//    	firePropertyChange(PROPERTY_DELETEDROW, -1, row);
    }
    public void markDeletedRow(int[] rows) {
    	SortableTableModel model = (SortableTableModel) this.getModel();
    	for (int row : rows) {
    		model.markDeletedRow(model._indexes[row]);
    	}
    	
//    	firePropertyChange(PROPERTY_DELETEDROW, -1, 0);
    }
    /**
     * Cancel deleting the row has been marked as deleted to normal status.
     */
    public void cancelDeletedRow(int row) {
    	SortableTableModel model = (SortableTableModel) this.getModel();
    	model.cancelDeletedRow(model._indexes[row]);
//    	firePropertyChange(PROPERTY_DELETEDROW, -1, row);
    }
    public void cancelDeletedRow(int[] rows) {
    	SortableTableModel model = (SortableTableModel) this.getModel();
    	for (int row : rows) {
    		model.cancelDeletedRow(model._indexes[row]);
    	}
    	
//    	firePropertyChange(PROPERTY_DELETEDROW, -1, 0);
    }
    /**
     * Return true if the specified row should be deleted.
     */
    public boolean isShouldDelete(int row) {
    	if (row < 0) {
    		return false;
    	}
    	SortableTableModel model = (SortableTableModel) this.getModel();
    	return model.isShouldDelete(model._indexes[row]);
    }
    /*
     * （非 Javadoc）
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("columnModel")) {
            SortableTableModel model = (SortableTableModel) this.getModel();
            DefaultTableColumnModel columnModel = (DefaultTableColumnModel) this
                    .getColumnModel();
            int count = columnModel.getColumnCount();
            Object[] ob = new Object[count];
            for (int i = 0; i < count; i++)
                ob[i] = (columnModel.getColumn(i).getHeaderValue());
            model.setColumnIdentifiers(ob);
        }
    }
    public void restoreAll()
    {
    	 SortableTableModel stm = (SortableTableModel) getModel();
         Map<String, Object> tmp = stm.modifyData;
         tmp.clear();
         repaint();
    }
    /**
     * Return true if some table cells has been modified.
     * @return true if modified
     */
    public boolean hasModified()
    {
    	 SortableTableModel stm = (SortableTableModel) getModel();
         Map<String, Object> tmp = stm.modifyData;
         return tmp.size()>0 || stm.deletedRows.size() > 0;
    }
    /**
     * Return true if the specified cell has been modified .
     * @param row --table row index in the table view.
     * @param column  --table column index in the table view.
     * @return  --the value of removed cell.
     */
    public boolean isModified(int row,int column)
    {
    	SortableTableModel stm = (SortableTableModel) getModel();
        Map<String, Object> tmp = stm.modifyData;
        column=convertColumnIndexToModel(column);
        row=stm.getRealRow(row);
        
        return tmp.containsKey(StringUtil.compose(row, column));
    }
    /**
     * To restore specified cell .
     * @param row --table row index.
     * @param column  --table column index in the table view.
     * @return  --the value of removed cell.
     */
    public Object restoreCell(int row,int column)
    {
    	SortableTableModel stm = (SortableTableModel) getModel();
        Map<String, Object> tmp = stm.modifyData;
    	column=convertColumnIndexToModel(column);
    	row=stm.getRealRow(row);
    	Object value=tmp.remove(StringUtil.compose(row, column));
    	repaint();
    	return value;
    }
    /**
     * @author liu_xlin 对表格元素进行渲染
     */
    private final class CellRenderer extends BaseTableCellRender {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);     
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            }

            SortableTableModel stm = (SortableTableModel) getModel();
            Map<String, Object> tmp = stm.modifyData;
            /**
             * 检验是该行和列对应的表格元素是否被修改过，修改过用亮色渲染
             */
            int realRow = stm._indexes[row].intValue();
            int realColumn=table.convertColumnIndexToModel(column);
            
            String key=StringUtil.compose(realRow, realColumn);
            
            if (tmp.containsKey(key)) {
            	Object text = (String) tmp.get(key);
            	if(text==null)
            		label.setText("<NULL>");
            	else
            		label.setText( text.toString());
                
                label.setForeground(modifiedCellHighlight);
            } else
                label.setForeground(table.getForeground());
            return label;
        }

        CellRenderer() {
        }
    }

    /**
     * 
     * @author liu_xlin 保存要修改的表格数据，以便于修改完毕后，对表模型的数据修改
     */
    protected final class MouseClick extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
        	if(!isEditable())
        		return;
            //			if (e.getClickCount() == 2) {
            int c = DataSetTable.this.columnAtPoint(e.getPoint());
            int r = DataSetTable.this.rowAtPoint(e.getPoint());
            oldOb = DataSetTable.this.getValueAt(r, c);
            
            SortableTableModel stm = (SortableTableModel) DataSetTable.this
            .getModel();
            int realRow=stm.getRealRow(r);
            sourceOb=stm.modifyData.get(StringUtil.compose(realRow,convertColumnIndexToModel(c)));
            isSaved = false;
        }
    }
    /**
     * This mothod pay attention to the modifydata map, doesn't make a change to the real model.
     * @param value --the new value will be set into modifyData map.
     * @param row --table row in the table view.
     * @param column --table column in the table view.
     */
    public void setValue(Object value,int row,int column)
    {
    	 SortableTableModel stm = (SortableTableModel) DataSetTable.this
         .getModel();
    	Object currentValue=getValueAt(row, column);
    	if(currentValue==null)
    	{
    		if(value==null)
    		{
    			confirmRestoreCell(row,column,stm,"<NULL>");
    		}else
    			stm.modifyData.put(StringUtil.compose(row, convertColumnIndexToModel(column)), value);
    	}else
    	{
    		if(currentValue.equals(value))
    		{
    			confirmRestoreCell(row,column,stm,currentValue.toString());
    		}else
    			stm.modifyData.put(StringUtil.compose(row, convertColumnIndexToModel(column)), value);
    	}
    }
    private boolean confirmRestoreCell(int row,int column,SortableTableModel stm,String oldValue)
    {
    	//Do nothing if current cell specified by row and column has not been modified.
    	row=stm.getRealRow(row);
    	column= convertColumnIndexToModel(column);
    	if(stm.modifyData.containsKey(StringUtil.compose(row, column)))
    		return false;
    	
    	int result = JOptionPane.showConfirmDialog(CommonFrame
                .getParentFrame(DataSetTable.this), PublicResource
                .getString("resultView.editorcell.equal",oldValue),"confirm",JOptionPane.YES_NO_OPTION);
        if(result==JOptionPane.YES_OPTION)
        {
            stm.modifyData.remove(StringUtil.compose(row, column));
        }
        return result==JOptionPane.YES_OPTION;
    }
    /**
     * 
     * @author liu_xlin
     * 
     * 修改表格元素后，更新模型对象,该监听器是针对表格模型（SortableTableModel）的真正数据模型属性(_actualModel)
     */
    private final class TableUpdateListener implements TableModelListener {

        /**
         * 恢复对实际模型的数据修改，将修改后的值放入hashmap缓冲中
         * 
         * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
         */
        public void tableChanged(TableModelEvent e) {
            if (!isSaved && oldOb != null
                    && e.getType() == TableModelEvent.UPDATE) {
                isSaved = true;
                SortableTableModel stm = (SortableTableModel) DataSetTable.this
                        .getModel();
                int row = e.getFirstRow();  //获取的是真正数据模型的行数
                int column = e.getColumn();
                Object newOb = stm.getValueInRealModel(row, column);
                
                boolean isRestore=false;//是否进行了原值恢复
                if (sourceOb!=null&&newOb.toString().equals(oldOb.toString())) {
                    int result = JOptionPane.showConfirmDialog(CommonFrame
                            .getParentFrame(DataSetTable.this), PublicResource
                            .getString("resultView.editorcell.equal",oldOb.toString()),"confirm",JOptionPane.YES_NO_OPTION);
                    if(result==JOptionPane.YES_OPTION)
                    {
                        stm.modifyData.remove(StringUtil.compose(row, column));
                        isRestore=true;
                    }else
                    {
                       stm.setValueInRealModel(oldOb, row, column);   //防止真正的模型数据被修改
                       return;
                    }
                }else 
                {
                    if(sourceOb!=null&&newOb.toString().equals(sourceOb.toString()))  //没有做任何修改
                    {
                        stm.setValueInRealModel(oldOb, row, column);   //防止真正的模型数据被修改
                        return;
                    }else if(sourceOb==null)
                    {
                        if(newOb.toString().equals(oldOb.toString()))
                        {
                            return;
                        }
                    }
                }
                stm.setValueInRealModel(oldOb, row, column);   //防止真正的模型数据被修改

                /**
                 * 将更新的数据保存在内存中
                 */
                if(!isRestore)
                    stm.modifyData.put(StringUtil.compose(row, column), newOb);
                oldOb = null;
                sourceOb=null;
            }
        }

    }

    @SuppressWarnings("serial")
	private final class MyCellEditor extends DefaultCellEditor {
        public MyCellEditor(final JTextField textField) {
            super(textField);
        }

        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            SortableTableModel stm = (SortableTableModel) getModel();
            Map<String, Object> tmp = stm.modifyData;
            int realRow = stm.getRealRow(row);
            Object text = (String) tmp.get(StringUtil.compose(realRow, table.convertColumnIndexToModel(column)));
            if (text != null)
                delegate.setValue(text);
            else
                delegate.setValue(value);
            return editorComponent;
        }
    }
}
