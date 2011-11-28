/*
 * 创建日期 2006-6-8
 */
package com.coolsql.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.coolsql.action.common.PublicAction;
import com.coolsql.action.framework.CheckCsAction;
import com.coolsql.action.framework.CsAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.component.IconButton;
import com.coolsql.pub.component.MyTabbedPane;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.PopMenuMouseListener;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.system.Setting;
import com.coolsql.view.resultset.DataSetPanel;
import com.coolsql.view.resultset.DataSetTable;
import com.coolsql.view.resultset.HeaderMouseListener;
import com.coolsql.view.resultset.ResultDisplayPopMenuManage;
import com.coolsql.view.resultset.TableHeaderMenuManage;
import com.coolsql.view.resultset.action.AddNewDataAction;
import com.coolsql.view.resultset.action.CancelDeleteRowsAction;
import com.coolsql.view.resultset.action.ChangeStatusOfDataSetPanelAction;
import com.coolsql.view.resultset.action.CopyAsSqlInsertAction;
import com.coolsql.view.resultset.action.DeleteRowsAction;
import com.coolsql.view.resultset.action.EditInDialogAction;
import com.coolsql.view.resultset.action.NextPageProcessAction;
import com.coolsql.view.resultset.action.PrePageProcessAction;
import com.coolsql.view.resultset.action.QueryAllRowsAction;
import com.coolsql.view.resultset.action.RefreshQueryAction;
import com.coolsql.view.resultset.action.RestoreCellAction;
import com.coolsql.view.resultset.action.SaveChangeToDBAction;
import com.coolsql.view.resultset.action.ShowColumnSelectDialogAction;
import com.coolsql.view.resultset.action.UpdateRowActioin;

/**
 * The view that display the result of executing including querying and updating operation.
 * Meanwhile this view supports modifying the result data and saving into database.
 * @author liu_xlin
 */
public class ResultSetView extends TabView {

	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(ResultSetView.class);
	
	public static final String DataTable = "dataTable";

    /**
     * tab组件，用于展示查询结果数据
     */
    private MyTabbedPane tab = null;

    private ResultDisplayPopMenuManage menuManage = null;

    private TableHeaderMenuManage headerMenu = null;

    /**
     * 菜单监听
     */
    private DataTableMouseListener popMenuListener = null;

    //表头组件的右键监听,便于菜单的弹出
    private HeaderMouseListener headerPopMenuListener = null;

    /**
     * 图标按钮定义
     */
    private CsAction prePageAction;

    private CsAction nextPageAction;

    private CsAction queryAllAction;
    
    private CsAction changeStatusAction;
    
    private CsAction restoreCellAction;
    
    private CsAction deleteRowAction;
    
    private CsAction cancelDeleteRowAction;
    
    private CsAction showColumnSelectDialogAction;
    
    private CsAction saveChangeToDBAction;


    private IconButton removeSelectedTab = null; //删除所选tab页

    private IconButton removeAllTab = null; //删除所有的tab页


    private CsAction copyAsInsertSQLAction;
    private CsAction updateRowAction;
    private CsAction refreshQueryAction;
    
    private CsAction editInDialogAction;
    
    private ResultSetOfDataPaneListener dataPaneListener = null; //数据面板监听

    /**
     * 用来记录tab控件的改变事件是否是删除事件或者添加tab事件
     */
    private boolean isRemoveOrAdd = false;

    private DataSetTableSelectionListener dataSetTableSelectionListener;
    public ResultSetView() {
        super();
        this.setTopTextIcon(PublicResource.getIcon("resultsetView.icon"));
        
        createActions();
        tab = new MyTabbedPane(JTabbedPane.TOP) {
			private static final long serialVersionUID = 1L;

			/**
             * 重写removeTabAt方法，便于清除作废的结果集数据的监听事件
             */
        	@Override
            public void removeTabAt(int index) {
                /**
                 * 删除结果集监听事件
                 */
                Component com = getSelectedComponent();

                /**
                 * 清空数据面板的监听处理
                 */
                if (com instanceof DataSetPanel) {
                    DataSetPanel dataPane = (DataSetPanel) com;

                    dataPane.removePanelPropertyChangeListener(dataPaneListener); //该监听是对工具按钮可用性的处理,该组件被移出后,必须清除该监听
                    if (dataPane.isRemoving()) { //如果彻底删除
//                        clearMenuProperty(dataPane);
                        dataPane.removeComponent();
                    }
                }

                setRemoveOrAddFlag(true);

                super.removeTabAt(index);

                checkButtonAvailable();
            }
        };
        menuManage = new ResultDisplayPopMenuManage(tab); //结果集视图的右键弹出菜单
        headerMenu = new TableHeaderMenuManage(null); //结果展示表控件表头的右键菜单管理类

        popMenuListener = new DataTableMouseListener(); //对于表控件的鼠标监控
        headerPopMenuListener = new HeaderMouseListener(); //表头右键弹出菜单监听
        dataPaneListener = new ResultSetOfDataPaneListener();

        this.setContent(tab); //将自定义tab控件作为结果集视图的主要容器
        tab.setHeadPopMenu(createTabHeadMenu());

        createIconButton(); //添加图标按钮

        tab.addChangeListener(new TabChangeListener()); //添加tab变化事件

        dataSetTableSelectionListener=new DataSetTableSelectionListener();
        checkButtonAvailable();//初始化后，做一次按钮可用性校验
    }

    protected void createActions() {
		prePageAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(PrePageProcessAction.class);
		nextPageAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(NextPageProcessAction.class);
		queryAllAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(QueryAllRowsAction.class);

		copyAsInsertSQLAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(CopyAsSqlInsertAction.class);
		updateRowAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(UpdateRowActioin.class);
		refreshQueryAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(RefreshQueryAction.class);
		changeStatusAction = Setting.getInstance().getShortcutManager()
				.getActionByClass(ChangeStatusOfDataSetPanelAction.class);
		
		deleteRowAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(DeleteRowsAction.class);
		
		cancelDeleteRowAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(CancelDeleteRowsAction.class);
		
		restoreCellAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(RestoreCellAction.class);
		
		showColumnSelectDialogAction = Setting.getInstance().getShortcutManager()
		.getActionByClass(ShowColumnSelectDialogAction.class);
		changeStatusAction.addPropertyChangeListener(new PropertyChangeListener()
		{

			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(CheckCsAction.SELECT_STATUS)||evt.getPropertyName().equals("enabled"))
				{
					if(changeStatusAction.isEnabled()) {
						showColumnSelectDialogAction.setEnabled(((CheckCsAction)changeStatusAction).isSelected());
					} else {
						showColumnSelectDialogAction.setEnabled(false);
					}
					
					if (changeStatusAction.isEnabled() && ((CheckCsAction)changeStatusAction).isSelected()) {
						Component component = getDisplayComponent();
				    	if(component instanceof DataSetPanel) {
				    		DataSetPanel dsPanel=(DataSetPanel)component;
				    		validateDeleteButton(dsPanel);
				    	} else {
				    		disableDeleteAction();
				    	}
					} else {
						disableDeleteAction();
					}
				}
			}
			
		}
		);
		
		editInDialogAction= Setting.getInstance().getShortcutManager()
		.getActionByClass(EditInDialogAction.class);
		
		saveChangeToDBAction= Setting.getInstance().getShortcutManager()
		.getActionByClass(SaveChangeToDBAction.class);
	}
    /**
     * 创建结果集展示tab控件的页头右键菜单
     * 
     * @return
     */
    private JPopupMenu createTabHeadMenu() {
        JPopupMenu menu = new BasePopupMenu();
        JMenuItem shut = new JMenuItem(PublicResource
                .getString("resultView.tab.headpopmenu.delete"));
        shut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeCurrentTab();
            }

        });
        menu.add(shut);
        return menu;
    }
    public void installDataSetTableSelectionListener(DataSetTable table)
    {
    	if(table==null)
    		return;
    	
    	table.getSelectionModel().addListSelectionListener(dataSetTableSelectionListener);
    	table.getColumnModel().getSelectionModel().addListSelectionListener(dataSetTableSelectionListener);
    }
    /**
     * 创建图标按钮
     *  
     */
    @SuppressWarnings("serial")
	private void createIconButton() {
    	
        addIconButton(prePageAction.getToolbarButton());
        addIconButton(nextPageAction.getToolbarButton());
        addIconButton(queryAllAction.getToolbarButton());

        addIconButton(refreshQueryAction.getToolbarButton());

        addIconButton(deleteRowAction.getToolbarButton());
        
        addIconButton(restoreCellAction.getToolbarButton());
        
        addIconButton(editInDialogAction.getToolbarButton());
        
        addIconButton(showColumnSelectDialogAction.getToolbarButton());
        
        addIconButton(saveChangeToDBAction.getToolbarButton());
        
        this.addIconButton(Setting.getInstance().getShortcutManager()
			.getActionByClass(AddNewDataAction.class).getToolbarButton());
        
        /**
         * 定义删除选中页的事件处理
         */
        Action removeTabListener = new PublicAction() {

            /**
             * 删除选中的tab页
             */
            public void actionPerformed(ActionEvent e) {

                removeCurrentTab();
            }

        };
        removeSelectedTab = this
                .addIconButton(
                        PublicResource
                                .getIcon("resultView.iconbutton.removeselected.icon"),
                        removeTabListener,
                        PublicResource
                                .getString("resultView.iconbutton.removeselected.tooltip")); //删除选中页按钮

        KeyStroke stroke = KeyStroke.getKeyStroke("DELETE");
        tab.getInputMap().put(stroke, "DELETE");
        tab.getActionMap().put("DELETE", removeTabListener);

        /**
         * 定义删除选中页的事件处理
         */
        Action removeAllListener = new PublicAction() {

            public void actionPerformed(ActionEvent e) {

                removeAllTab();
            }

        };
        removeAllTab = this.addIconButton(PublicResource
                .getIcon("resultView.iconbutton.removeall.icon"),
                removeAllListener, PublicResource
                        .getString("resultView.iconbutton.removeall.tooltip")); //删除所有页按钮
    }

    /**
     * 删除当前选中的tab页
     *  
     */
    public void removeCurrentTab() {
        int index = tab.getSelectedIndex();
        Component com = tab.getSelectedComponent();
        if (com instanceof DataSetPanel) {
            ((DataSetPanel) com).setRemoving(true);
        }
        tab.remove(index);
    }

    /**
     * 删除所有tab页
     *  
     */
    public void removeAllTab() {
        for (int i = tab.getTabCount() - 1; i > -1; i--) {
            Component com = tab.getComponentAt(i);
            if (com instanceof DataSetPanel) {
                ((DataSetPanel) com).setRemoving(true);
            }
            tab.remove(i);
        }
//        tab.removeAll();//删除所有tab页
    }

    /**
     * 因为结果集数据显示列表的右键菜单中可能保存了数据面板对象的引用。 该方法在彻底删除数据面板时调用
     * 
     * @param dataTable -- Table component in the DataSetPane.
     */
    public void clearMenuProperty(JTable dataTable) {

        //在右键菜单管理对象中可能保存了该组件的引用，如果引用了将引用删除
        Component content = dataTable;
        if (content instanceof JScrollPane)
            content = ((JScrollPane) content).getViewport().getView();

        if (content == menuManage.getNoRenderMenu().getClientProperty(
        		DataTable)) {
            menuManage.getNoRenderMenu().putClientProperty(DataTable, null); //删除属性数据的引用
        }
        if (content == menuManage.getComponent()) {
            menuManage.setComponent(null);
        }

    }

    /**
     * 向指定的表控件添加鼠标右键监听处理事件
     * 
     * @param table
     */
    public void addListenerToTable(JTable table) {
        table.addMouseListener(popMenuListener);
    }

    /**
     * 删除指定的表控件的鼠标右键监听处理事件
     * 
     * @param table
     */
    public void removeListenerFromTable(JTable table) {
        table.removeMouseListener(popMenuListener);
    }

    /**
     * 添加可关闭tab页，主要针对sql执行时信息的展示，对于添加的页，在之后会有界面显示的调整
     * 
     * @param BookMark
     * @return
     */
    public DataSetPanel addTab(Bookmark bookmark) {
        DataSetPanel pane = new DataSetPanel(bookmark);
        this.setRemoveOrAddFlag(true);
        getResultTab().addCloseTab(bookmark.getAliasName(), pane);
        pane.addPanelPropertyChangeListener(dataPaneListener);
        return pane;
    }

    /**
     * 添加可关闭组件,如果被添加的组件为DataSetPanel类型，那么将忽略参数:title,tip
     * 
     * @param com
     *            --被添加的组件
     * @param title
     *            --页标题
     * @param tip
     *            --提示信息
     */
    public void addTab(JComponent com, String title, String tip) {
        if (com == null)
            return;
        if (com instanceof DataSetPanel) {
            DataSetPanel pane = (DataSetPanel) com;

            getResultTab().addCloseTab(pane.getBookmark().getAliasName(), pane,
                    pane.getSql());
        } else {
            getResultTab().addCloseTab(title, com, tip);
        }
    }

    /**
     * 从tab组件中删除给定的组件
     * 
     * @param com
     *            --被删除的组件
     */
    public synchronized void removeTab(JComponent com) {
        if (com == null)
            return;
        getResultTab().remove(com);
    }

    /**
     * 获取数据面板结果集的监听事件
     * 
     * @return
     */
    public ResultSetOfDataPaneListener getResultSetListener() {
        return dataPaneListener;
    }

    /**
     * 获取组件在tab控件中的索引位置
     * 
     * @param com
     * @return
     */
    public int getPositionByComponent(JComponent com) {
        return tab.indexOfComponent(com);
    }

    /**
     * 设置tab控件指定索引页的标题
     * 
     * @param index
     * @param title
     */
    public void setTabTitle(int index, String title) {
        tab.setTitleAt(index, title);
    }

    /**
     * 选中指定的组件
     * 
     * @param com
     */
    public void setSelectedTab(JComponent com) {
        tab.setSelectedComponent(com);
    }
    /**
     * 获取tab组件中当前显示的组件
     * @return 
     */
    public JComponent getDisplayComponent()
    {
    	return (JComponent)tab.getSelectedComponent();
    }
    /*
     * （非 Javadoc）
     * 
     * @see src.view.View#getName()
     */
    public String getName() {

        return stringMgr.getString("view.resultset.title");
    }

    /*
     * （非 Javadoc）
     * 
     * @see src.view.Display#dispayInfo()
     */
    public void dispayInfo() {

    }

    /*
     * 
     * @see src.view.Display#popupMenu()
     */
    public void popupMenu(int x, int y) {
    }

    public MyTabbedPane getResultTab() {
        return tab;
    }

    /**
     * 校验按钮的可用性
     *  
     */
    public void checkButtonAvailable() {
    	
    	GUIUtil.processOnSwingEventThread(new Runnable() {
			public void run() {
				updateStatus();
				/**
				 * 检验tab控件是否存在组件
				 */
				if (tab.getTabCount() < 1) // 如果没有tab页,将相应的按钮置为不可用
				{
					prePageAction.setEnabled(false);
					nextPageAction.setEnabled(false);
					GUIUtil.setComponentEnabled(false, removeSelectedTab);
					GUIUtil.setComponentEnabled(false, removeAllTab);
					queryAllAction.setEnabled(false);
					copyAsInsertSQLAction.setEnabled(false);
					restoreCellAction.setEnabled(false);
					editInDialogAction.setEnabled(false);
					updateRowAction.setEnabled(false);
					refreshQueryAction.setEnabled(false);
				} else {

					JComponent com = (JComponent) tab.getSelectedComponent();
					if (com instanceof DataSetPanel) { // 该判断条件只是针对结果集数据编辑按钮
						DataSetPanel dataPane = (DataSetPanel) com;
						validateDeleteButton(dataPane);
						
						if (!dataPane.isReady()) { // 如果数据面板未准备好,将向前、后,以及查询所有、刷新按钮置为不可用
							prePageAction.setEnabled(false);
							nextPageAction.setEnabled(false);
							queryAllAction.setEnabled(false);
							// GUIUtil.setComponentEnabled(false, refresh);
							refreshQueryAction.setEnabled(false);
							copyAsInsertSQLAction.setEnabled(false);
							restoreCellAction.setEnabled(false);
							editInDialogAction.setEnabled(false);
							updateRowAction.setEnabled(false);
						} else {
							if (dataPane.getSqlResult().isResultSet()) { // 如果为查询类型的结果集
								prePageAction
										.setEnabled(dataPane.hasPrevious());

								nextPageAction.setEnabled(dataPane.hasNext());

								refreshQueryAction.setEnabled(true);
								queryAllAction.setEnabled(true);

								int selectedCount = getQueryResultAndSelected(dataPane);
								copyAsInsertSQLAction
										.setEnabled(selectedCount > 0);
								restoreCellAction
										.setEnabled((selectedCount > 0)
												&& isSelectedCellsModified());
								editInDialogAction
										.setEnabled(isQueryResultAndSelectedOneCell(dataPane));
								updateRowAction.setEnabled(selectedCount == 1);

							} else // 如果为更新或者删除类型结果集,将向前,向后,查询所有数据,刷新等按钮置为不可用
							{
								prePageAction.setEnabled(false);
								nextPageAction.setEnabled(false);
								queryAllAction.setEnabled(false);
								refreshQueryAction.setEnabled(false);
								copyAsInsertSQLAction.setEnabled(false);
								restoreCellAction.setEnabled(false);
								editInDialogAction.setEnabled(false);
								updateRowAction.setEnabled(false);
							}
						}
					} else {
						prePageAction.setEnabled(false);
						nextPageAction.setEnabled(false);
						copyAsInsertSQLAction.setEnabled(false);
						restoreCellAction.setEnabled(false);
						editInDialogAction.setEnabled(false);
						updateRowAction.setEnabled(false);
					}
					GUIUtil.setComponentEnabled(true, removeSelectedTab);
					GUIUtil.setComponentEnabled(true, removeAllTab);
				}
			}
		});
    }
    private int getQueryResultAndSelected(DataSetPanel dataPane)
    {
    	JComponent content=dataPane.getContent();
    	if(!(content instanceof JTable))
    	{
    		return -1;
    	}
    	return ((JTable)content).getSelectedRowCount();
    }
    private boolean isQueryResultAndSelectedOneCell(DataSetPanel dataPane)
    {
    	JComponent content=dataPane.getContent();
    	if(!(content instanceof JTable))
    	{
    		return false;
    	}
    	return (((JTable)content).getSelectedRowCount())==1&&(((JTable)content).getSelectedColumnCount())==1;
    }
    /**
	 * 设置tab控件的删除标志
	 */
    private void setRemoveOrAddFlag(boolean isRemoveOrAdd) {
        this.isRemoveOrAdd = isRemoveOrAdd;
    }
    /**
     * This method is used to update the status of button associated with modifying result data.
     */
    protected void updateStatus()
    {
    	Component component=getDisplayComponent();
    	if(component instanceof DataSetPanel)
    	{
    		DataSetPanel dsPanel=(DataSetPanel)component;
    		if(dsPanel.isEditable())
    		{
    			changeStatusAction.setEnabled(true);
    			((ChangeStatusOfDataSetPanelAction)changeStatusAction).setSelected(dsPanel.isAllowEdit());
    		}else
    		{
    			changeStatusAction.setEnabled(false);
    		}
    		
    		saveChangeToDBAction.setEnabled(dsPanel.isAllowEdit()&&hasModified(dsPanel));
    	}else
    	{
    		changeStatusAction.setEnabled(false);
    		saveChangeToDBAction.setEnabled(false);
    	}
    	
    }
    /**
     * Validate whether deleteRowAction and cancelDeleteRowAction are enabled.
     */
    private void validateDeleteButton(DataSetPanel dataPane) {
//    	if (!dataPane.isAllowEdit()) {
//    		deleteRowAction.setEnabled(false);
//    		cancelDeleteRowAction.setEnabled(false);
//    		return;
//    	}
    	
    	JComponent content = dataPane.getContent();
    	if(!(content instanceof DataSetTable))
    	{
    		disableDeleteAction();
    	} else {
    		DataSetTable t = (DataSetTable) content;
    		int[] selectedRows = t.getSelectedRows();
    		if (selectedRows == null || selectedRows.length == 0) {
    			deleteRowAction.setEnabled(false);
        		cancelDeleteRowAction.setEnabled(false);
    		} else {
    			boolean checkDeleteRow = false;
    			boolean checkCancelDeleteRow = false;
	    		for (int row : selectedRows) {
	    			if (!t.isShouldDelete(row)) {
	    				checkDeleteRow = true;
	    			} else {
	    				checkCancelDeleteRow = true;
	    			}
	    			if (checkDeleteRow && checkCancelDeleteRow) {
	    				break;
	    			}
	    		}
	    		deleteRowAction.setEnabled(checkDeleteRow);
	    		cancelDeleteRowAction.setEnabled(checkCancelDeleteRow);
    		}
    	}
    }
    private void disableDeleteAction() {
    	deleteRowAction.setEnabled(false);
		cancelDeleteRowAction.setEnabled(false);
    }
    /**
     * Return true if some cells of dataset table in the dsPanel have been modified.
     * @return true if there are some changes, otherwise return false.
     */
    private boolean hasModified(DataSetPanel dsPanel)
    {
    	if(dsPanel==null)
    		return false;
    	
    	if(dsPanel.getContent() instanceof DataSetTable)
    	{
    		DataSetTable dsTable=(DataSetTable)dsPanel.getContent();
    		return dsTable.hasModified();
    	}else
    		return false;
    }
    /**
     * To check whether some of selected cells have been modified.
     * @return
     */
    protected boolean isSelectedCellsModified()
    {
       	Component component=getDisplayComponent();
    	if(component instanceof DataSetPanel)
    	{
    		DataSetPanel dsPanel=(DataSetPanel)component;
    		if(!(dsPanel.getContent() instanceof DataSetTable))
    		{
    			return false;
    		}
    		DataSetTable dsTable=(DataSetTable)dsPanel.getContent();
    		int[] selectedRows=dsTable.getSelectedRows();
    		int[] selectedColumns=dsTable.getSelectedColumns();
    		for(int i=0;i<selectedRows.length;i++)
    		{
    			for(int j=0;j<selectedColumns.length;j++)
    			{
    				if(dsTable.isModified(selectedRows[i], selectedColumns[j]))
    				{
    					return true;
    				}
    			}
    		}
    		return false;
    	}else
    	{
    		return false;
    	}
    	
    }
    /**
     * 
     * @author liu_xlin 完成tab控件的页删除以及改变组件选择事件的监听处理
     */
    protected class TabChangeListener implements ChangeListener {

        /*
         * （非 Javadoc）
         * 
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            if (isRemoveOrAdd) //如果是删除改变，不做检验
            {
                setRemoveOrAddFlag(false);
                return;
            }
            checkButtonAvailable();
        }

    }

    /**
     * 
     * @author liu_xlin 结果表控件鼠标右键弹出菜单处理,该数据表监听处理类只是针对结果数据显示表控件(DataSetTable)使用的
     */
    private class DataTableMouseListener extends PopMenuMouseListener {
        public DataTableMouseListener() {

        }

        public void mouseReleased(MouseEvent e) {
            if (isPopupTrigger(e)) {
                menuManage.setComponent((JComponent) e.getSource()); //更新菜单管理器所属组件

                /**
                 * 便于事件处理时，能够方便的获取被操作的组件对象
                 */
                menuManage.getNoRenderMenu().putClientProperty(DataTable,
                        menuManage.getComponent());

                menuManage.getPopMenu().show((JComponent) e.getSource(),
                        e.getX(), e.getY());
            }
        }
    }

    private class ResultSetOfDataPaneListener implements PropertyChangeListener {

        /*
         * （非 Javadoc）
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("sqlResult")) //如果数据面板的执行结果发生变化,对按钮可用性进行校验
            {
                if (evt.getSource() == tab.getSelectedComponent())
                    checkButtonAvailable();
                	
            } else if (evt.getPropertyName().equals("sql")) //更新tab的提示信息
            {
                if (evt.getOldValue().equals(evt.getNewValue()))
                    return;
                JComponent com = (JComponent) evt.getSource();
                int index = tab.indexOfComponent(com);
                tab.setToolTipTextAt(index, evt.getNewValue().toString());
            }
        }

    }
    private class DataSetTableSelectionListener implements ListSelectionListener{

		/* (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel tableSelectionModel=(ListSelectionModel)e.getSource();
			JComponent com = (JComponent) tab.getSelectedComponent();
	        if (com instanceof DataSetPanel) { //该判断条件只是针对结果集数据编辑按钮
	        	DataSetPanel dataPane = (DataSetPanel) com;
	        	JComponent content=dataPane.getContent();
	        	if(!(content instanceof DataSetTable))
	        		return;
	        
	        	DataSetTable table=(DataSetTable)content;
	        	//if table changed on selection is not in the current display tab, it'll do nothing.
	        	if(table.getSelectionModel()!=tableSelectionModel&&
	        			table.getColumnModel().getSelectionModel()!=tableSelectionModel)
	        		return ;
	        	
				copyAsInsertSQLAction.setEnabled(table.getSelectedRowCount()>0);
				restoreCellAction.setEnabled(table.getSelectedRowCount()>0&&isSelectedCellsModified());
				editInDialogAction.setEnabled(table.getSelectedColumnCount()==1&&table.getSelectedRowCount()==1);
				updateRowAction.setEnabled(table.getSelectedRowCount()==1);
				
				if (changeStatusAction.isEnabled() && ((CheckCsAction)changeStatusAction).isSelected()) {
					validateDeleteButton(dataPane);
				} else {
					disableDeleteAction();
				}
	        }else
	        {
	        	copyAsInsertSQLAction.setEnabled(false);
	        	restoreCellAction.setEnabled(false);
	        	editInDialogAction.setEnabled(false);
	        	updateRowAction.setEnabled(false);
	        }
		}
    	
    }

    public TableHeaderMenuManage getHeaderMenu() {
        return headerMenu;
    }

    public HeaderMouseListener getHeaderPopMenuListener() {
        return headerPopMenuListener;
    }
    @Override
    public String getTabViewTitle()
    {
    	return PublicResource.getString("resultView.tabtitle");
    }
    @Override
    public int getTabViewIndex()
    {
    	return 0;
    }
	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		
	}
}
