/*
 * Created on 2007-1-31
 */
package com.coolsql.modifydatabase.insert;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;

import com.coolsql.action.common.ActionCommand;
import com.coolsql.adapters.dialect.DialectFactory;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.exportdata.Actionable;
import com.coolsql.modifydatabase.EntityDisplayPanel;
import com.coolsql.modifydatabase.insert.action.BatchPasteCommand;
import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.DefinableToolBar;
import com.coolsql.pub.component.DisplayPanel;
import com.coolsql.pub.component.IconButton;
import com.coolsql.pub.component.SelectIconButton;
import com.coolsql.pub.component.WaitDialog;
import com.coolsql.pub.component.WaitDialogManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.StatusBar;
import com.coolsql.pub.display.TableScrollPane;
import com.coolsql.pub.display.exception.NotRegisterException;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.SqlUtil;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.Database;
import com.coolsql.sql.commonoperator.ColumnPropertyOperator;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.DataType;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.ColumnDisplayDefinition;

/**
 * @author liu_xlin 插入数据对话框，该窗口中，通过选择对应的实体对象，然后输入对应字段的数据值，可以生成相应的插入语句。
 *         对于可编辑的表格，其编辑方式只是根据字段的长度控制了表格元素所能输入的最大长度。因而可以方便的进行批量表格的粘贴。
 */
public class InsertDataDialog extends BaseDialog {
	private static final long serialVersionUID = 1L;

	private final static String selectRowsLabel = PublicResource
            .getSQLString("rowinsert.dialog.status.selectrows.label");

    private final static String selectColumnsLabel = PublicResource
            .getSQLString("rowinsert.dialog.status.selectcolumns.label");

    /**
     * 工具栏中定义的相关按钮
     */
    private IconButton addSingle = null; //添加单行

    private IconButton addMuti = null; //添加多行

    private IconButton deleteSelect = null; //删除选中

    private SelectIconButton preview = null; //预览

    private SelectIconButton exportSQL = null; //导出sql语句

//    private IconButton execute = null; //执行
    /**
     * 展示实体信息的面板
     */
    private EntityDisplayPanel entityInfoPane = null;

    /**
     * 可编辑表控件
     */
    private EditorTable editTable;

    private BaseMenuManage tablePopMenu = null;

    /**
     * 状态栏
     */
    private StatusBar statusBar1 = null; //显示选中的第一个表格位置

    private StatusBar statusBar2 = null; //显示选中的行与列数

    private StatusBar statusBar3 = null; //显示表控件的总行数，与总列数

    /**
     * 插入sql语句中命令字段部分的字符串值
     */
    private String constantOfEntity;

    /**
     * Indicate whether the mouse is pressed, A true value means mouse is pressed.
     */
    private boolean isMousePressed=false;
    public InsertDataDialog(JFrame frame) {
        this(frame, null);
    }

    public InsertDataDialog(JFrame frame, Entity entity) {
        super(frame);
        constantOfEntity = "";
        createGUI(entity);
    }

    public InsertDataDialog(JDialog frame) {
        this(frame, null);
    }

    public InsertDataDialog(JDialog frame, Entity entity) {
        super(frame);
        constantOfEntity = "";
        createGUI(entity);
    }

    public InsertDataDialog(JFrame frame, boolean isModel) {
        this(frame, isModel, null);
    }

    public InsertDataDialog(JFrame frame, boolean isModel, Entity entity) {
        super(frame, isModel);
        constantOfEntity = "";
        createGUI(entity);
    }

    public InsertDataDialog(JDialog frame, boolean isModel) {
        this(frame, isModel, null);
    }

    public InsertDataDialog(JDialog frame, boolean isModel, Entity entity) {
        super(frame, isModel);
        constantOfEntity = "";
        createGUI(entity);
    }

    /**
     * 初始化界面组件
     *  
     */
    @SuppressWarnings("unchecked")
    protected void createGUI(Entity entity) {
        setTitle(PublicResource.getSQLString("rowinsert.dialog.title"));
        JPanel main = (JPanel) getContentPane();

        //添加工具栏
        main.add(createToolBar(), BorderLayout.NORTH);

        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());

        entityInfoPane = new EntityDisplayPanel(entity);
        entityInfoPane.addEntityChangeListener(new Actionable() //如果实体发生变化
                {

                    public void action() {
                        try {
                            loadNewEntity(entityInfoPane.getEntity());
                        } catch (UnifyException e) {
                            LogProxy.errorReport(InsertDataDialog.this, e);
                        } catch (SQLException e) {
                            LogProxy.SQLErrorReport(InsertDataDialog.this, e);
                        }

                    }

                });
        pane.add(entityInfoPane, BorderLayout.NORTH);
        try {
            editTable = new EditorTable((List) null, parseEntity(entity))
            {
				private static final long serialVersionUID = 1L;

				@Override
				public void changeSelection(int rowIndex, int columnIndex,
						boolean toggle, boolean extend) {
					super.changeSelection(rowIndex, columnIndex, toggle,extend);
					if(!isMousePressed)
						updateStatus();
				}
			}
            ;
        } catch (SQLException e) {
            LogProxy.SQLErrorReport(e);
            editTable = new EditorTable(new EditorTableModel());
        } catch (Exception e) { //如果抛出异常，将表控件初始化为一个空表
            LogProxy.errorReport(e);
            editTable = new EditorTable(new EditorTableModel());
        }
        editTable.setHeaderPopMenu(true);
        editTable.setHeaderPopMenu(new TableHeaderMenu());
        createShortcut();
        //        editeTable.setEnableToolTip(false);
        pane.add(new TableScrollPane(editTable), BorderLayout.CENTER);

        tablePopMenu = new EditorTableMenuManage(editTable);
        editTable.setMenuManage(tablePopMenu); //向表控件添加右键菜单
        
        main.add(pane, BorderLayout.CENTER);

        /**
         * 状态栏的初始化
         */
        JPanel statusPane = new JPanel();
        statusPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.gridx = 0;

        statusBar3 = new StatusBar(12);
        statusBar3.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.status.tablescale"));
        statusPane.add(statusBar3, gbc);

        gbc.gridx++;
        statusBar1 = new StatusBar(12);
        statusBar1.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.status.selectpoint"));
        statusPane.add(statusBar1, gbc);

        gbc.weightx = 1;
        gbc.gridx++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        statusBar2 = new StatusBar();
        statusBar2.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.status.selectcells"));
        statusPane.add(statusBar2, gbc);
        main.add(statusPane, BorderLayout.SOUTH);

        setSize(850, 550);
        toCenter();
        loadStatusListener();
    }

    /**
     * 
     * 为editetable创建快捷键 crtl+v :批量粘贴 DELETE ：删除选择表格的值 ctrl+A 将所有表格选中
     */
    protected void createShortcut() {
        /**
         * 批量粘贴表格数据
         */
        Action pastAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                ActionCommand command = new BatchPasteCommand(editTable);
                try {
					command.exectue();
				} catch (Exception e1) {
					LogProxy.errorReport(e1);
				}
            }

        };
        GUIUtil.bindShortKey(editTable, "control V", pastAction, false);

        /**
         * 删除单元格的数据
         */
        Action delAction = new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                int[] selectrow = editTable.getSelectedRows();
                int[] selectcolumn = editTable.getSelectedColumns();
                if (selectrow == null || selectrow.length < 1
                        || selectcolumn == null || selectcolumn.length < 1)
                    return;

                for (int i = 0; i < selectrow.length; i++) {
                    for (int j = 0; j < selectcolumn.length; j++) {
                        int row = selectrow[i];
                        int column = selectcolumn[j];
                        EditeTableCell cell = (EditeTableCell) editTable
                                .getValueAt(row, column);
                        cell.setEmpty();
                        editTable.setValueAt(cell, row, column);
                    }
                }
            }

        };
        GUIUtil.bindShortKey(editTable, "DELETE", delAction, false);

        /**
         * 全选处理
         */
        Action selectAllAction = new SelectAllAction();
        GUIUtil.bindShortKey(editTable, "ctrl A", selectAllAction, false);
    }

    /**
     * 加载新的实体信息
     * 
     * @param entity
     *            --新实体对象
     * @throws UnifyException
     * @throws SQLException
     */
    public void loadNewEntity(Entity entity) throws UnifyException,
            SQLException {
        ColumnDisplayDefinition[] def = parseEntity(entity);
        editTable.setDataList(null, def);

        addOneRowToTable();//添加一行,准备添加信息数据
        editTable.adjustPerfectWidth();
//        updateConstantOfSQL(entity);
    }

    /**
     * 更新对象属性constantOfEntity的值
     *  
     */
    private void updateConstantOfSQL(Entity entity, boolean isFullQualifiedName) {
        if (entity == null)
            return;
        StringBuilder sb=new StringBuilder("INSERT INTO " + (isFullQualifiedName ? entity.getQualifiedName() : entity.getName()) + " (");

        TableColumnModel columnModel = editTable.getColumnModel();
        int i = 0;
        for (; i < columnModel.getColumnCount(); i++) {
            TableHeaderCell cell = (TableHeaderCell) columnModel.getColumn(i)
                    .getHeaderValue();
            if (!cell.getState()) //没有选中该列
                continue;

            Column col = (Column) cell.getHeaderValue();
            sb.append(col.getName() + ",");
        }
        sb.deleteCharAt(sb.length()-1).append(") VALUES ");
        constantOfEntity=sb.toString();

    }

    /**
     * //在表控件中添加一行
     *  
     */
    private void addOneRowToTable() {
        if (!(editTable.getModel() instanceof EditorTableModel)
                || editTable.getColumnCount() < 1) //如果表控件的模型对象不是EditorTableModel类型，不添加行数据
            return;

        //在表控件中添加一行
        EditorTableModel model = (EditorTableModel) editTable.getModel();
        model.addRow(EditorTableModel.getEmptyCell(editTable
                .getColumnCount()));
    }

    /**
     * 加载状态信息的监听 初始化状态信息
     */
    private void loadStatusListener() {
        editTable.getModel().addTableModelListener(new TableStructListener());
        StatusChangeListener mouseListener = new StatusChangeListener();
        editTable.addMouseListener(mouseListener);
        editTable.addMouseMotionListener(mouseListener);
        addOneRowToTable();
        editTable.adjustPerfectWidth();
    }

    /**
     * 根据实体对象解析为编辑表控件（EditorTable）的列定义数组对象
     * 
     * @param entity
     *            --实体对象
     * @return --表控件的列定义数组对象
     * @throws UnifyException
     *             --获取实体的列出错时，抛出此异常
     * @throws SQLException
     *             --获取实体的列出错时，抛出此异常
     */
    public ColumnDisplayDefinition[] parseEntity(Entity entity)
            throws UnifyException, SQLException {
        if (entity == null)
            return null;

        Column[] columns = entity.getColumns();
        if (columns == null || columns.length == 0)
            throw new UnifyException(PublicResource
                    .getSQLString("rowinsert.unknownentity"));

        ColumnDisplayDefinition[] def = new ColumnDisplayDefinition[columns.length];

        for (int i = 0; i < columns.length; i++) {
            def[columns[i].getPosition()-1] = new ColumnDisplayDefinition(
                    (int) columns[i].getSize() + 10, columns[i].getName(),
                    columns[i]);
        }
        return def;
    }

    /**
     * 校验非null字段是否被选中,如果非null的字段没有被选中,提示用户是否继续后面的操作
     * 
     * @return --true:继续正常的执行, false：取消之后的执行
     */
    protected boolean checkColumnValidate() {
        TableColumnModel columnModel = editTable.getColumnModel();
        String message = PublicResource
                .getSQLString("rowinsert.table.columnid")
                + "\"";
        boolean isWarning = false;
        for (int i = 0; i < editTable.getColumnCount(); i++) {
            TableColumn tableColumn = columnModel.getColumn(i);
            TableHeaderCell headerCell = (TableHeaderCell) tableColumn
                    .getHeaderValue();
            Column col = (Column) headerCell.getHeaderValue();
            if (!col.isNullable() && !headerCell.getState()) {
                message += col.getName() + " ";
                isWarning = true;
            }
        }

        if (isWarning) {
            int result = JOptionPane.showConfirmDialog(this, message
                    + "\""
                    + PublicResource
                            .getSQLString("rowinsert.table.columnidnotnull"),
                    "confirm information", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) //继续
                return true;
            else
                //撤销
                return false;
        } else
            //没有非null字段没选中
            return true;
    }

    /**
     * 创建工具栏 1、添加单行（表的尾部） 2、添加多行 （表的尾部） 3、删除选中 4、预览（sql）
     * 
     * @return
     */
    private JToolBar createToolBar() {
        DefinableToolBar toolBar = new DefinableToolBar("edite table", JToolBar.HORIZONTAL);

        /**
         * 添加单行图标按钮
         */
        ActionListener addSingleAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                EditorTableModel model = (EditorTableModel) editTable
                        .getModel();
                model.addRow(EditorTableModel.getEmptyCell(editTable
                        .getColumnCount()));

            }
        };
        addSingle = new IconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.addsingle.icon"));
        addSingle.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.addsingle.tip"));
        addSingle.addActionListener(addSingleAction);
        toolBar.add(addSingle);
        /**
         * 添加多行图标按钮
         */
        addMuti = new IconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.addmuti.icon"));
        addMuti.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.addmuti.tip"));
        addMuti.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int rows = 0;
                String promptTxt = PublicResource
                        .getSQLString("rowinsert.table.popmenu.insertmutirows");
                String value = "";
                while (true) {
                    value = JOptionPane.showInputDialog(InsertDataDialog.this,
                            promptTxt);
                    if (value == null) //撤销
                        return;
                    try {
                        rows = Integer.parseInt(value);
                        break;
                    } catch (NumberFormatException e1) {
                        promptTxt = PublicResource
                                .getSQLString("rowinsert.table.popmenu.insert.invalidatevalue");
                    }
                }
                EditorTableModel model = (EditorTableModel) editTable
                        .getModel();
                model.addRows(EditorTableModel.getEmptyCell(rows,
                        editTable.getColumnCount()));
            }

        });
        toolBar.add(addMuti);
        /**
         * 删除选中图标按钮
         */
        deleteSelect = new IconButton(PublicResource
                .getSQLIcon("rowinsert.table.popmenu.deleteselected.icon"));
        deleteSelect.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.deleteselect.tip"));
        deleteSelect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int[] selectRows = editTable.getSelectedRows();
                if (selectRows == null || selectRows.length < 1)
                    return;
                EditorTableModel model = (EditorTableModel) editTable
                        .getModel();

                for (int i = selectRows.length - 1; i > -1; i--) {
                    model.removeRow(selectRows[i]);
                }

            }

        });
        toolBar.add(deleteSelect);

        /**
         * 预览sql图标按钮
         */
        preview = new SelectIconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.preview.icon"));
        //预览全部数据
        preview.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.preview.all"), null,
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (!checkComponentValidate())
                            return;

                        int[] previewIndex = new int[editTable.getRowCount()];
                        for (int i = 0; i < previewIndex.length; i++) {
                            previewIndex[i] = i;
                        }
                        previewSQL(previewIndex);
                    }

                });
        //预览选中行
        preview.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.preview.select"), null,
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (!checkComponentValidate())
                            return;

                        if (editTable.getSelectedRowCount() < 1) {
                            LogProxy
                                    .message(
                                            InsertDataDialog.this,
                                            PublicResource
                                                    .getSQLString("rowinsert.dialog.table.selectnorow"),
                                            JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        int[] previewIndex = editTable.getSelectedRows();
                        previewSQL(previewIndex);
                    }

                });
        preview.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.preview.tip"));
        toolBar.add(preview);

        /**
         * 导出sql语句
         */
        //导出全部
        Action exportAllSQLAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int[] previewIndex = new int[editTable.getRowCount()];
                for (int i = 0; i < previewIndex.length; i++) {
                    previewIndex[i] = i;
                }
                exportSQL(previewIndex);
            }

        };
        Action exportSelectedSQLAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                if (editTable.getSelectedRowCount() < 1) {
                    LogProxy
                            .message(
                                    InsertDataDialog.this,
                                    PublicResource
                                            .getSQLString("rowinsert.dialog.table.selectnorow"),
                                    JOptionPane.WARNING_MESSAGE);
                    return;
                }
                exportSQL(editTable.getSelectedRows());
            }

        };
        exportSQL = new SelectIconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.export.icon"));
        exportSQL.setToolTipText(PublicResource
                .getSQLString("rowinsert.dialog.export.tip"));
        exportSQL.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.export.all"), null,
                exportAllSQLAction);
        exportSQL.addSelectItem(PublicResource
                .getSQLString("rowinsert.dialog.export.select"), null,
                exportSelectedSQLAction);
        toolBar.add(exportSQL);
        /**
         * 执行sql图标按钮
         */
        Action executeAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                if (!checkComponentValidate())
                    return;

                int result = JOptionPane
                        .showConfirmDialog(
                                InsertDataDialog.this,
                                PublicResource
                                        .getSQLString("rowinsert.dialog.confirmstartexecute"),
                                "confirm execute", JOptionPane.YES_NO_OPTION);
                
                Bookmark bookmark = entityInfoPane.getEntity().getBookmark();
                if(!bookmark.isAutoCommit()&&result == JOptionPane.YES_OPTION)
                {
                	boolean r=GUIUtil.getYesNo(PublicResource.getSQLString("rowinsert.dialog.beforeadd.enableautocommit"));
                	if(!r)
                	{
                		return;
                	}
                }
                
                if (result == JOptionPane.YES_OPTION) //选择添加数据
                {
                    if (!checkColumnValidate()) //取消继续执行
                        return;

                    int[] previewIndex = new int[editTable.getRowCount()];
                    for (int i = 0; i < previewIndex.length; i++) {
                        previewIndex[i] = i;
                    }
                    final ExecuteAction exe = new ExecuteAction(previewIndex);
                    final ProcessThread pt = new ProcessThread(exe);
                    final WaitDialog waitDialog = WaitDialogManage
                            .getInstance().register(pt, InsertDataDialog.this);
                    waitDialog.setTitle("add data to database");
                    waitDialog.setPrompt(PublicResource
                            .getSQLString("rowinsert.waitdialog.prompt"));
                    waitDialog.setTaskLength(previewIndex.length);
                    waitDialog.addQuitAction(new Actionable() {

                        public void action() {

                            WaitDialogManage.getInstance().disposeRegister(pt);
                            waitDialog.dispose();
                            exe.stopExecute();
                        }

                    });
                    pt.start(); //启动线程
                    waitDialog.setVisible(true); //显示等待对话框
                }
            }

        };
        toolBar.addIconButton(PublicResource
                .getSQLIcon("rowinsert.dialog.execute.icon"), executeAction,
                PublicResource.getSQLString("rowinsert.dialog.execute.tip"));

        toolBar.setBackground(DisplayPanel.getThemeColor());
        return toolBar;
    }

    /**
     * 导出指定的sql
     * 
     * @param rows
     *            --指定需要导出的行
     */
    private void exportSQL(int rows[]) {
        if (rows == null)
            return;
        /**
         * 获取文件，并且打开本地文件输出流
         */
        File selectFile = GUIUtil.selectFileNoFilter(InsertDataDialog.this);
        if (selectFile == null)
            return;
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(selectFile.getAbsolutePath(), false, true);
            out = new FileOutputStream(selectFile);

            for (int i = 0; i < rows.length; i++) {
                out.write((StringUtil.trim(getSQLToRow(rows[i]))+";\r\n").getBytes());
            }
        } catch (FileNotFoundException e1) {
            LogProxy.errorReport(InsertDataDialog.this, e1);
        } catch (IOException e1) {
            LogProxy.errorReport(InsertDataDialog.this, e1);
        } catch (UnifyException e1) {
            LogProxy.errorReport(InsertDataDialog.this, e1);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 校验组件有效性
     * 
     * @return --true:有效 false:无效
     */
    private boolean checkComponentValidate() {
        if (editTable.getColumnCount() < 1) {
            LogProxy.errorMessage(this, PublicResource
                    .getSQLString("rowinsert.dialog.novalidateentity"));
            return false;
        } else
            return true;
    }

    /**
     * 更新状态信息（首选表格位置，选择表格数量）
     *  
     */
    protected void updateStatus() {
        int[] rows = editTable.getSelectedRows();
        int[] columns = editTable.getSelectedColumns();
        if (rows == null || rows.length < 1 || columns == null
                || columns.length < 1) {
            statusBar1.setText("");
            statusBar2.setText("");
        } else {
            statusBar1.setText((rows[0] + 1) + "," + (columns[0] + 1));
            statusBar2.setText(selectRowsLabel + rows.length + ","
                    + selectColumnsLabel + columns.length);
        }
    }

    /**
     * 更新表结构表格数量信息
     *  
     */
    protected void updateTableScaleInfo() {
        updateStatus();
        if (editTable != null) {
            int rows = editTable.getRowCount();
            int columns = editTable.getColumnCount();
            statusBar3.setText(rows + ":" + columns);
        }
    }

    /**
     * 预览指定行所生成的sql
     * 
     * @param rowIndex
     *            --指定的行索引数组
     */
    protected void previewSQL(int[] rowIndex) {
        if (!checkColumnValidate()) //如果取消操作
            return;

        //updateConstantOfSQL(entityInfoPane.getEntity());//先更新插入sql的命令部分
        PreviewSQLDialog previewDialog = new PreviewSQLDialog(null, this, true);
        ProcessThread pt = new ProcessThread(new PreviewAction(previewDialog,
                rowIndex));

        /**
         * 先启动线程,然后再显示预览对话框
         */
        pt.start();
        previewDialog.setVisible(true);
    }

    /**
     * 获取指定行数据所生成的插入sql语句
     * 
     * @param rowIndex
     *            --指定的行索引
     * @return --sql语句
     * @throws UnifyException
     */
    protected String getSQLToRow(int rowIndex) throws UnifyException {
        StringBuilder buffer = new StringBuilder(constantOfEntity + "(");

        Database db = entityInfoPane.getEntity().getBookmark().getDbInfoProvider();
        TableColumnModel columnModel = editTable.getColumnModel();
        TableModel model = editTable.getModel();
        int i = 0;
        boolean isPostgreSQL=DialectFactory.isPostgreSQL(db.getDatabaseMetaData());
        for (; i < editTable.getColumnCount(); i++) {
            TableColumn tableColumn = columnModel.getColumn(i);
            TableHeaderCell cell = (TableHeaderCell) tableColumn
                    .getHeaderValue();
            if (!cell.getState()) //如果该列没有选中,直接进入下一轮循环
                continue;

            Column col = (Column) cell.getHeaderValue();

//            int realColumnIndex = editeTable.convertColumnIndexToModel(i);
            EditeTableCell value = (EditeTableCell) model.getValueAt(rowIndex,
            		editTable.convertColumnIndexToModel(i)); //获取视图对应的真正表格对象值
            String tmpValue=value.getDisplayLabel();
            
            if(isPostgreSQL)
            {
            	buffer.append(value.isNull()?"null":SqlUtil.qualifyColumnValue(tmpValue, col.getType())).append(",");
            	continue;
            }
            
            DataType dataType = db.getDataType(col.getTypeName()); //获取字段的类型对象
            if(dataType==null)
            {
            	buffer.append(value.isNull()?"null":SqlUtil.qualifyColumnValue(tmpValue, col.getType())).append(",");
            	continue;
            }
            if(SqlUtil.isNumberType(col.getType())&&tmpValue.equals(""))
            {
                tmpValue="0";
            }
            
            String realValue = (value.isNull() ? "null,"
                    : (StringUtil.trim(dataType.getLiteralPrefix())
                            + tmpValue
                            + StringUtil.trim(dataType.getLiteralSuffix()) + ","));
            buffer.append(realValue);
        }
        buffer.deleteCharAt(buffer.length() - 1); //删除最后的逗号"'"
        buffer.append(")");

        return buffer.toString();
    }

    /**
     * 
     * @author liu_xlin 预览sql处理逻辑定义类
     */
    private class PreviewAction implements Actionable {
        private PreviewSQLDialog previewDialog; //预览显示sql的对话框

        private int[] rowIndex; //需要预览的行

        public PreviewAction(PreviewSQLDialog previewDialog, int[] rowIndex) {
            this.previewDialog = previewDialog;
            this.rowIndex = rowIndex;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com..exportdata.Actionable#action()
         */
        public void action() {
            if (previewDialog == null)
                return;

            previewDialog.setStatueInfo("sql count:" + rowIndex.length);
            updateConstantOfSQL(entityInfoPane.getEntity(), false);
            for (int i = 0; i < rowIndex.length; i++) {
                try {
                    previewDialog.append(getSQLToRow(rowIndex[i]) + ";");
                } catch (BadLocationException e) {
                    LogProxy.errorReport(InsertDataDialog.this, e); //如果发生异常,中断执行
                    return;
                } catch (UnifyException e) {
                    LogProxy.errorReport(InsertDataDialog.this, e);//如果发生异常,中断执行
                    return;
                }
            }
        }

    }

    /**
     * 
     * @author liu_xlin 执行表格数据所生成的sql
     */
    private class ExecuteAction implements Actionable {
        private int[] rowIndex; //需要执行的列

        private boolean isRun; //控制执行处理的终止

        public ExecuteAction(int[] rowIndex) {
            isRun = true;
            this.rowIndex = rowIndex;
        }

        private void stopExecute() {
            isRun = false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com..exportdata.Actionable#action()
         */
        public void action() {
            Bookmark bookmark = entityInfoPane.getEntity().getBookmark();
            boolean oldIsAutoCommit = bookmark.isAutoCommit();
            
            WaitDialog dialog;
            try {
                dialog = WaitDialogManage.getInstance().getDialogOfCurrent();
                dialog.setTaskLength(rowIndex.length);
            } catch (NotRegisterException e2) {
                LogProxy.errorReport(e2);
                return;
            }
            try {
            	if(!oldIsAutoCommit)
            	{
            		bookmark.getConnection().commit();
            	}
            	bookmark.setAutoCommit(false);
            } catch (SQLException e3) {
            	if (bookmark.isAutoCommit() != oldIsAutoCommit) {
            		try {
						bookmark.setAutoCommit(oldIsAutoCommit);
					} catch (Exception e) {
						e.printStackTrace();
					} 
            	}
                LogProxy.SQLErrorReport(InsertDataDialog.this, e3);
                dialog.dispose();
                return;
            } catch (UnifyException e3) {
            	if (bookmark.isAutoCommit() != oldIsAutoCommit) {
            		try {
						bookmark.setAutoCommit(oldIsAutoCommit);
					} catch (Exception e) {
						e.printStackTrace();
					} 
            	}
                LogProxy.errorReport(InsertDataDialog.this, e3);
                dialog.dispose();
                return;
            }
            Connection con = null;
            Statement sm = null;
            try {
                con = bookmark.getConnection();
                sm = con.createStatement();
            } catch (UnifyException e) {
                LogProxy.errorReport(InsertDataDialog.this, e);
                return;
            } catch (SQLException e) {
                LogProxy.SQLErrorReport(InsertDataDialog.this, e);
                return;
            }
            try {
            	updateConstantOfSQL(entityInfoPane.getEntity(), true);
            	int successCount = 0;//The row count of row data has been saved into database successfully.
                for (int i = 0; i < rowIndex.length; i++) {
                    if (!isRun) { //如果终止运行
                        int result = JOptionPane
                                .showConfirmDialog(
                                        InsertDataDialog.this,
                                        PublicResource
                                                .getSQLString("rowinsert.dialog.confirmstop"),
                                        "confirm stop",
                                        JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) //如果选择是
                        {
                            if (successCount == 0) {//如果没有插入过数据,直接跳出循环
                                break;
                            }

                            result = JOptionPane
                                    .showConfirmDialog(
                                            InsertDataDialog.this,
                                            PublicResource
                                                    .getSQLString("rowinsert.dialog.confirmrollback")
                                                    + successCount, "confirm rollback",
                                            JOptionPane.YES_NO_CANCEL_OPTION);

                            if (result == JOptionPane.NO_OPTION) {
                                break;
                            } else if (result == JOptionPane.YES_OPTION) {
                                try {
                                    con.rollback();
                                } catch (SQLException e1) {
                                    LogProxy.SQLErrorReport(
                                            InsertDataDialog.this, e1);
                                }
                                break;
                            } else {
                                isRun = true; //如果继续执行
                            }

                        } else {
                            isRun = true; //如果继续执行
                        }
                    } else //正常的sql执行
                    {
                        try {
                            processRowInsert(rowIndex[i], sm, bookmark);
                            successCount ++;
                        } catch (UnifyException e1) {
                            LogProxy.errorReport(InsertDataDialog.this,"current row number:"+rowIndex[i], e1);
                            isRun = false; //发生异常后,让用户确认是否退出执行
                        } catch (SQLException e1) {
                            LogProxy.SQLErrorReport(InsertDataDialog.this, e1);
                            isRun = false;//发生异常后,让用户确认是否退出执行
                        } finally {
                        	dialog.setProgressValue(i+1); //设置进度
                        }
                    }
                }
            } finally {
                /**
                 * 提交事务，同时恢复数据库连接的提交属性，关闭Statement对象
                 */
                try {
                    con.commit();
                    sm.close();
                    bookmark.setAutoCommit(oldIsAutoCommit);
                } catch (SQLException e1) {
                    LogProxy.SQLErrorReport(InsertDataDialog.this, e1);
                } catch (UnifyException e1) {
                    LogProxy.errorReport(InsertDataDialog.this, e1);
                }
                

                /**
                 * 关闭等待对话框
                 */
                WaitDialogManage.getInstance().disposeRegister(
                        Thread.currentThread());
                if (dialog != null)
                {
                	int value=dialog.getProgressValue();
                    dialog.dispose();
                    if(value>0)
                    {
                    	int taskLen=dialog.getTaskLength();
                    	if(taskLen==value)//Have finished the task.
                    	{
                    		JOptionPane.showMessageDialog(InsertDataDialog.this,
                    				"Executing has been finished successfully","finished!",JOptionPane.INFORMATION_MESSAGE);
                    	}
                    }
                    	
                }
            }
        }

        /**
         * 执行插入的sql语句
         * 
         * @param index
         *            --指定行索引
         * @param sm
         *            --Statement对象
         * @throws UnifyException
         * @throws SQLException
         */
        private void processRowInsert(int index, Statement sm, Bookmark bookmark)
                throws UnifyException, SQLException {
            String sql = getSQLToRow(index);
            LogProxy.getProxy().debug(
                    "The INSERT SQL (" + bookmark.getAliasName() + "): " + sql);
            sm.execute(sql);
        }
    }

    /**
     * 
     * @author liu_xlin 状态更新的监听类，更新首选表格位置，选择表格数量两种信息
     */
    protected class StatusChangeListener extends MouseAdapter implements
            MouseMotionListener {
        public void mousePressed(MouseEvent e) {
        	isMousePressed=true;
//            updateStatus();

        }

        public void mouseReleased(MouseEvent e) {
        	isMousePressed=false;
//            updateStatus();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
         */
        public void mouseDragged(MouseEvent e) {
            updateStatus();

        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent e) {
        }
    }

    /**
     * 
     * @author liu_xlin 覆盖表控件的缺省快捷键：ctrl A的全选处理
     */
    private class SelectAllAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		/*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            editTable.setRowSelectionInterval(0, editTable.getRowCount() - 1);
            editTable.setColumnSelectionInterval(0, editTable
                    .getColumnCount() - 1);
//            updateStatus();
        }

    }
    /**
     * 
     * @author liu_xlin 为表结构模型添加监听处理类
     */
    private class TableStructListener implements TableModelListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
         */
        public void tableChanged(TableModelEvent e) {
            if (e.getType() == TableModelEvent.UPDATE) {
                if (e.getFirstRow() > -1 || e.getColumn() > -1) //如果是更新表格数据，直接返回
                    return;

                updateTableScaleInfo();
            } else {
                clearInvalidateRow(e);
                updateTableScaleInfo();
            }
        }

        private void clearInvalidateRow(TableModelEvent e) {
            editTable.getSelectionModel().clearSelection();
        }
    }

    /**
     * 
     * @author liu_xlin
     *表头右键菜单
     */
    private class TableHeaderMenu extends JPopupMenu {
		private static final long serialVersionUID = 1L;
		JMenuItem queryColumn = null;
        private int x1=0,y1=0;
        public TableHeaderMenu() {
            super();
            /**
             * 查看列属性菜单项
             */
            queryColumn = new JMenuItem(
                    PublicResource
                            .getSQLString("rowinsert.dialog.table.header.popmenu.querycolumn"));
            Action queryAction = new AbstractAction() //查询列属性信息处理
            {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e1) {
                    /**
                     * 获取对应的列对象信息
                     */
                    int columnIndex = editTable.getTableHeader()
                            .columnAtPoint(
                                    new Point(x1,y1));
                    Object ob = editTable.getColumnModel().getColumn(
                            columnIndex).getHeaderValue();
                    
                    Column col = (Column)((EditeTableHeaderCell)ob).getHeaderValue();

                    /**
                     * 弹出列属性对话框
                     */
                    Operatable operator;
                    try {
                        operator = OperatorFactory
                                .getOperator(ColumnPropertyOperator.class);
                    } catch (ClassNotFoundException e) {
                        LogProxy.errorReport(e);
                        return;
                    } catch (InstantiationException e) {
                        LogProxy.internalError(e);
                        return;
                    } catch (IllegalAccessException e) {
                        LogProxy.internalError(e);
                        return;
                    }
                    try {
                        operator.operate(InsertDataDialog.this, col);
                    } catch (UnifyException e2) {
                        LogProxy.errorReport(InsertDataDialog.this, e2);
                    } catch (SQLException e2) {
                        LogProxy.SQLErrorReport(InsertDataDialog.this, e2);
                    }
                }

            };
            queryColumn.addActionListener(queryAction);

            add(queryColumn);
        }
        public void show(Component com,int x,int y)
        {
            this.x1=x;
            this.y1=y;
            super.show(com,x,y);
        }
    }
}
