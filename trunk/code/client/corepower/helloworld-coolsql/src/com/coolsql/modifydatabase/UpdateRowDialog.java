/*
 * Created on 2007-1-17
 */
package com.coolsql.modifydatabase;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.coolsql.exportdata.Actionable;
import com.coolsql.modifydatabase.model.BaseTableCell;
import com.coolsql.modifydatabase.model.CheckBean;
import com.coolsql.modifydatabase.model.ColumnNameTableCell;
import com.coolsql.modifydatabase.model.NewValueTableCell;
import com.coolsql.modifydatabase.model.OldValueTableCell;
import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.display.EditorSQLArea;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.SqlUtil;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.DataType;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.util.TypesHelper;
import com.coolsql.view.log.LogProxy;

/**
 * 更新实体中的记录时，选择并修改相应的字段
 */
public class UpdateRowDialog extends BaseDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final static String totalColumn=PublicResource.getSQLString("rowupdate.status.totalcolumn");
    private final static String updateColumn=PublicResource.getSQLString("rowupdate.status.updatecolumn");
    private final static String qualifyColumn=PublicResource.getSQLString("rowupdate.status.qualifycolumn");
    /**
     * 展示实体信息的面板
     */
    private EntityDisplayPanel entityInfoPane = null;

    /**
     * 更新行记录的表控件
     */
    private UpdateRowTable table = null;

    //sql显示区域
    private EditorSQLArea sqlArea = null;

    //实体列所对应的值，保存在该映射对象中。key:列名 value：该列的值
    private Map<String,Object> dataMap = null;

    /**
     * 更新结果集中的字段时,如果结果集列字段不是全,那么需要判断是否将未显示的字段加入到更新的行中来. true:全部显示
     * false:显示有效的一些字段
     */
    private boolean isDisplayAll;

    /**
     * 右键菜单管理器
     */
    private BaseMenuManage popMenuManage = null;

    /**
     * 组成更新sql的两部分
     */
    private String needModifyPartStr = null; //赋值部分

    private String qualifyPartStr = null; //条件部分

    public UpdateRowDialog() {
        this(null);
    }

    public UpdateRowDialog(Entity entity) {
        this(entity, (Map<String,Object>) null, false);
    }

    public UpdateRowDialog(Entity entity, Map<String,Object> dataMap, boolean isDisplayAll) {
        super(GUIUtil.getMainFrame());
        this.dataMap = dataMap;
        this.isDisplayAll = isDisplayAll;
        initDialog(entity);
    }

    public UpdateRowDialog(Dialog owner, Entity entity) {
        this(owner, entity, null, false);
    }

    public UpdateRowDialog(Frame owner, Entity entity) {
        this(owner, entity, null, false);
    }

    public UpdateRowDialog(Dialog owner, Entity entity, Map<String,Object> dataMap,
            boolean isDisplayAll) {
        super(owner);
        this.dataMap = dataMap;
        this.isDisplayAll = isDisplayAll;
        initDialog(entity);
    }

    public UpdateRowDialog(Frame owner, Entity entity, Map<String,Object> dataMap,
            boolean isDisplayAll) {
        super(owner);
        this.dataMap = dataMap;
        this.isDisplayAll = isDisplayAll;
        initDialog(entity);
    }

    /**
     * 初始化界面
     *  
     */
    private void initDialog(Entity entity) {
        setTitle(PublicResource.getSQLString("rowupdate.dialog.title"));

        JPanel pane = (JPanel) getContentPane();
        pane.setLayout(new BorderLayout());

        entityInfoPane = new EntityDisplayPanel(entity) {
			private static final long serialVersionUID = 1L;

			/**
             * 不显示“更改实体”按钮
             */
            public boolean isDisplayChangeBtn() {
                return false;
            }
        };
        pane.add(entityInfoPane, BorderLayout.NORTH);

        table = new UpdateRowTable();
        ToolTipManager.sharedInstance().registerComponent(table); //注册信息提示

        sqlArea = new EditorSQLArea(entity.getBookmark());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(table), sqlArea);
        splitPane.setDividerSize(4);
        splitPane.setDividerLocation(350);
        pane.add(splitPane, BorderLayout.CENTER);

        //菜单管理器的初始化
        popMenuManage = new UpdateRowTableMenuManage(table);
        table.addMouseListener(new MouseAdapter() //为表控件添加右键菜单
                {
        			boolean isPopupTriggerWhenPress;
        			@Override
        			public void mousePressed(MouseEvent e)
        			{
        				isPopupTriggerWhenPress=e.isPopupTrigger();
        				int row=table.rowAtPoint(e.getPoint());
        				table.getSelectionModel().setSelectionInterval(row, row);
        			}
        			@Override
                    public void mouseReleased(MouseEvent e) {
                        if (isPopupTriggerWhenPress||e.isPopupTrigger())
                            popMenuManage.getPopMenu().show(table, e.getX(),
                                    e.getY());
                    }
                });

        entityInfoPane.addEntityChangeListener(new EntityChangeAction());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
        setSize(560, 530);
        toCenter();

        try {
            loadEntity(entity, dataMap);
        } catch (UnifyException e) {
            LogProxy.errorReport(this, e);
        } catch (SQLException e) {
            LogProxy.SQLErrorReport(this, e);
        }

        table.getModel().addTableModelListener(new CellValueChangeListener()); //添加表模型监听
    }

    /**
     * 获取当前正在处理的实体对象
     * 
     * @return --实体对象
     */
    public Entity getEntityObject() {
        return entityInfoPane.getEntity();
    }

    /**
     * 关闭对话框
     *  
     */
    public void closeDialog() {
        sqlArea.dispose(); //关闭高亮线程
        if (dataMap != null) {
            dataMap.clear();
            dataMap = null;
        }
        dispose();
        removeAll();
    }

    /**
     * 装载实体数据，将表控件的界面进行更新.获取实体的列信息后，创建表控件的数据
     * warning:如果参数map为null,那么本次表控件界面的布局被认为是更换实体;如果map非null，那么会将不map键（key）集合中的字段设置为不可勾选
     * 
     * @param entity
     *            --新实体对象
     * @param map
     *            --实体列与列值得映射对象
     * @throws SQLException
     * @throws UnifyException
     */
    private void loadEntity(Entity entity, Map<String,Object> map) throws UnifyException,
            SQLException {

        Column[] columns = entity.getColumns();
        if (columns == null) {
            throw new UnifyException(PublicResource
                    .getSQLString("createsql.noentityinfo"));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>(); //表的新数据
        for (int i = 0; i < columns.length; i++) {
            Vector<Object> rowData = new Vector<Object>();

            if (map == null)
                rowData.add(new CheckBean(new Boolean(false), true));//第一列：勾选列
            else {
                if (map.containsKey(columns[i].getName().toUpperCase())) //如果包含了列，那么可勾选
                    rowData.add(new CheckBean(new Boolean(false), true));
                else {
                    if (isDisplayAll) //如果将不可改的字段也显示
                        rowData.add(new CheckBean(new Boolean(false), false)); //不可勾选
                    else
                        //如果不显示不可改的字段，直接进入下一次处理
                        continue;
                }
            }

            rowData.add(new ColumnNameTableCell(table, columns[i])); //第二列：字段名

            //第三列：原值
            if (map == null) //此种情况下，将会使原值列可编辑
            {
                rowData.add(new OldValueTableCell(table, null, true));
            } else //如果不为空，获取列所对应的对象值
            {
                Object ob = map.get(columns[i].getName().toUpperCase());
                String value = ob == null ? "" : ob.toString();
                rowData.add(new OldValueTableCell(table, value));
            }

            //第四列：新值列
            rowData.add(new NewValueTableCell(table, "", EditorFactory
                    .getEditorTypeBySQLType(columns[i].getType())));

            data.add(rowData);
        }
        table.replaceData(data);
        
        qualifyPartStr=getQualifyPart();  //装载实体后，同时将条件字段更新
    }

    /**
     * @return Returns the needModifyPartStr.
     */
    public String getNeedModifyPartStr() {
        return needModifyPartStr;
    }

    /**
     * @return Returns the qualifyPartStr.
     */
    public String getQualifyPartStr() {
        return qualifyPartStr;
    }

    /**
     * 返回更新sql中，字段赋值的部分
     * 
     * @return --(String) 返回更新sql中，字段赋值的部分
     * @throws UnifyException
     */
    public String getEvaluatePart() throws UnifyException {
        int count = 0; //计算需要修改的字段数量
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < table.getRowCount(); i++) {
            CheckBean bean = (CheckBean) table.getValueAt(i, 0); //获取选中状态的表格元素值
            if (!bean.getSelectValue().booleanValue()) //如果没有选中,直接进入下一轮处理
                continue;

            TableCell columnCell = (TableCell) table.getValueAt(i, 1); //获取字段列的表格元素值
            TableCell newValueCell = (TableCell) table.getValueAt(i, 3); //获取新值列的表格元素值

            Column col = (Column) columnCell.getValue();
            String newValue = (String) newValueCell.getValue();

            //获取对应的数据类型
            DataType dataType = col.getParentEntity().getBookmark()
                    .getDbInfoProvider().getDataType(col.getTypeName());
                
            if (TypesHelper.isLob(dataType.getJavaType())) {
                buffer.append(col.getName() + "=?,");
            } else if(newValueCell.isNullValue())
            {
                buffer.append(col.getName() + "=null,");
            } else {
            	if (dataType == null)
                {
                	buffer.append(col.getName()).append("=").append(newValue==null?"null":SqlUtil.qualifyColumnValue(newValue, col.getType())).append(",");
                }else
                {
	                String currentStr =(dataType.getLiteralPrefix()==null?"": dataType.getLiteralPrefix())
	                        + newValue
	                        + (dataType.getLiteralSuffix()==null?"":dataType.getLiteralSuffix());
	                buffer.append(col.getName()).append("=").append(currentStr + ",");
                }
            }
            count++;
        }
        if (count > 0)
            buffer.deleteCharAt(buffer.length() - 1); //除去最后一个字符‘,’
        return buffer.toString();
    }

    /**
     * 获取更新sql中的条件部分
     * 
     * @return --更新sql中的条件部分
     * @throws UnifyException
     */
    public String getQualifyPart() throws UnifyException {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < table.getRowCount(); i++) {
            TableCell columnCell = (TableCell) table.getValueAt(i, 1); //获取字段列的表格元素值
            if (columnCell.isAsTerm()) {
				Column col = (Column) columnCell.getValue();
				// 获取对应的数据类型
				DataType dataType = col.getParentEntity().getBookmark()
						.getDbInfoProvider().getDataType(col.getTypeName());
				if (TypesHelper.isLob(dataType.getJavaType())) // 类型为二进制对象，不做处理
					continue;

				TableCell oldValueCell = (TableCell) table.getValueAt(i, 2); // 获取原值列的表格元素值
				String value = (String) oldValueCell.getValue();

				if (value != null) // 如果原值表格对象值不为null时
					if (dataType == null) {
						buffer.append(" ").append(col.getName()).append(" = ")
								.append(SqlUtil.qualifyColumnValue(value.toString(), col.getType()));
					} else {
						buffer.append(" ")
								.append(col.getName())
								.append("=")
								.append(
										(dataType.getLiteralPrefix() == null
												? ""
												: dataType.getLiteralPrefix())
												+ value.trim()
												+ (dataType.getLiteralSuffix() == null
														? ""
														: dataType
																.getLiteralSuffix()))
								.append(" and");
					}
				else
					// 为null时
					buffer.append(" ").append(col.getName()).append(
							" is null and");
			}
        }
        if (buffer.length() > 0)
            buffer.delete(buffer.length() - 3, buffer.length()); // 删除多余的串"and"
        return buffer.toString();
    }

    /**
     * 返回状态信息，该信息记录了当前表的选种修改状态
     * @return
     */
    public String getStatueInfo()
    {
        int updateCols=0;
        int qualifyCols=0;
        for(int i=0;i<table.getRowCount();i++)
        {
            CheckBean bean = (CheckBean) table.getValueAt(i, 0); //获取选中状态的表格元素值
            if(bean.getSelectValue().booleanValue())
                updateCols++;
            
            TableCell columnCell = (TableCell) table.getValueAt(i, 1); //获取字段列的表格元素值
            if(columnCell.isAsTerm())
                qualifyCols++;
        }
        return totalColumn+table.getRowCount()+"\t"+updateColumn+updateCols+"\t\t"+qualifyColumn+qualifyCols;
    }
    /**
     * 更新sql显示区域的sql
     *
     */
    public void updateSQLInArea()
    {
        
        StringBuffer buffer=new StringBuffer();
        String tmp=StringUtil.trim(getEntityObject().getSchema());
        if(!tmp.equals(""))
        	tmp=tmp+".";
        buffer.append("update "+tmp+getEntityObject().getQualifiedName()+" set ");
        buffer.append(getNeedModifyPartStr()==null?"":getNeedModifyPartStr());
        buffer.append(StringUtil.trim(getQualifyPartStr()).equals("")?"":(" where "+getQualifyPartStr()));
        
        sqlArea.setText(buffer.toString());
    }
    private class EntityChangeAction implements Actionable {

        /*
         * (non-Javadoc)
         * 
         * @see com.coolsql.exportdata.Actionable#action()
         */
        public void action() {
            sqlArea.setText("");
            try {
                loadEntity(entityInfoPane.getEntity(), dataMap);

                if (dataMap != null) {
                    //更换实体后,将数据据映射对象删除
                    dataMap.clear();
                    dataMap = null;
                }
            } catch (UnifyException e) {
                LogProxy.errorReport(UpdateRowDialog.this, e);
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(UpdateRowDialog.this, e);
            }
        }

    }

    /**
     * 
     * @author liu_xlin 表格元素值被更新后，进行相应处理。 1、第一列（选择状态）如果发生值变更，将该行表格数据对象的底色进行更新
     *         2、同时将新值列的是否可编辑属性进行调整
     */
    private class CellValueChangeListener implements TableModelListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
         */
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) //如果更新元素值，才作处理
            {
                if (e.getColumn() == 0) {

                    int row = e.getFirstRow();
                    CheckBean bean = (CheckBean) table.getValueAt(row, 0);
                    if (bean.getSelectValue().booleanValue()) //如果该行被选中，将颜色置为指定色
                    {
                        table.setRowBackgroundColor(row,
                                UpdateRowTable.cellBackground);
                        BaseTableCell cell = (BaseTableCell) table.getValueAt(
                                row, 3);
                        cell.setEditable(true);

                    } else //如果该行未被选中
                    {
                        //                        setRowBackgroundColor(row,getSelectionBackground());
                        table.setRowBackgroundColor(row, null);
                        BaseTableCell cell = (BaseTableCell) table.getValueAt(
                                row, 3);
                        cell.setEditable(false);
                    }
                    
                    repaint();
                    
                    try {
                        needModifyPartStr = getEvaluatePart();  //更新赋值部分
                    } catch (UnifyException e1) {
                       LogProxy.errorReport(UpdateRowDialog.this,e1);
                       return;
                    }
                    
                    updateSQLInArea();
                    
                    sqlArea.setStatueInfo(getStatueInfo());
                } else if (e.getColumn() == 1) //如果第一列发生变化，一般发生该列元素的是否作为更新条件的值发生了变化
                {
                    try {
                        qualifyPartStr = getQualifyPart();  //更新条件部分
                    } catch (UnifyException e1) {
                       LogProxy.errorReport(UpdateRowDialog.this,e1);
                       return;
                    }
                    
                    updateSQLInArea();
                    sqlArea.setStatueInfo(getStatueInfo());
                } else if (e.getColumn() == 3) {
                    try {
                        needModifyPartStr = getEvaluatePart();  //更新赋值部分
                    } catch (UnifyException e1) {
                        LogProxy.errorReport(UpdateRowDialog.this,e1);
                        return;
                    }
                    
                    updateSQLInArea();
                    sqlArea.setStatueInfo(getStatueInfo());
                }
            }
        }

    }
}
