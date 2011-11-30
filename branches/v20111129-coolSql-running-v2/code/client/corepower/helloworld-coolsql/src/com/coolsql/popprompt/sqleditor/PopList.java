/*
 * Created on 2007-2-7
 */
package com.coolsql.popprompt.sqleditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.coolsql.action.common.PublicAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.EntityFactory;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.sqleditor.pop.BaseListCell;
import com.coolsql.view.sqleditor.pop.FieldListCell;
import com.coolsql.view.sqleditor.pop.PopListCellRenderer;
import com.coolsql.view.sqleditor.pop.PopListModel;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;

/**
 * @author liu_xlin 弹出list组件
 */
public class PopList extends PopPanel {

	/**
	 * Define constant for type of Object selected currently
	 */
	public static final int SELECTEDTYPE_ENTITY=0; //entity type
	public static final int SELECTEDTYPE_COLUMN=1;  //column type
	
	private static final String ARROW_LEFT="leftarrow";
	private static final String ARROW_RIGHT="rightarrow";
	
	private static final long serialVersionUID = 1L;

	private JTabbedPane tabPane;

    private JList entityList; //实体列表

    private JList fieldList; //选中实体对应字段列表

    private boolean entitySelectionChanged;//实体选择是否发生变化标志


    public PopList(Object[] items) {
        super();
        createGUI(Arrays.asList(items));
    }

    public PopList(List<Object> items) {
        super();
        createGUI(items);
    }

    protected void createGUI(List<Object> items) {
        entitySelectionChanged = false;
        tabPane = new JideTabbedPane(JTabbedPane.BOTTOM);

        PopListCellRenderer listCellRender = new PopListCellRenderer();
        ListSelectChangeListener selectionChange = new ListSelectChangeListener();
        /**
         * 初始化实体列表
         */
        ListModel model = new PopListModel(items);
        entityList = new JList(model);
        entityList.setCellRenderer(listCellRender);
        entityList
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        entityList.addListSelectionListener(selectionChange);
        
//        new Sticky(entityList);

        tabPane.addTab(PublicResource
                .getSQLString("sqledite.popprompt.tab.entity"),
                new JideScrollPane(entityList));

        /**
         * 初始化字段列表控件
         */
        model = new PopListModel(new ArrayList<Object>());
        fieldList = new JList(model);
        fieldList.setCellRenderer(listCellRender);
        fieldList
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabPane.addTab(PublicResource
                .getSQLString("sqledite.popprompt.tab.field"), new JideScrollPane(
                fieldList));

//        new Sticky(fieldList);
        ArrowPressedAction arrowAction=new ArrowPressedAction();
        KeyStroke leftKey=KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
        KeyStroke rightKey=KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
        entityList.getInputMap(JComponent.WHEN_FOCUSED).put(leftKey, ARROW_LEFT);
        entityList.getInputMap(JComponent.WHEN_FOCUSED).put(rightKey, ARROW_RIGHT);
        entityList.getActionMap().put(ARROW_LEFT, arrowAction);
        entityList.getActionMap().put(ARROW_RIGHT, arrowAction);
        fieldList.getInputMap(JComponent.WHEN_FOCUSED).put(leftKey, ARROW_LEFT);
        fieldList.getInputMap(JComponent.WHEN_FOCUSED).put(rightKey, ARROW_RIGHT);
        fieldList.getActionMap().put(ARROW_LEFT, arrowAction);
        fieldList.getActionMap().put(ARROW_RIGHT, arrowAction);
        
        tabPane.addChangeListener(new FieldInfoDisplayListener());
        setLayout(new BorderLayout());
        add(tabPane, BorderLayout.CENTER);
    }
    public JList getCurrentList()
    {
    	if (tabPane.getSelectedIndex() == 0)
        {
            return entityList;
        } else if (tabPane.getSelectedIndex() == 1)
        {
        	return fieldList;
        }else
        	return null;
    }
    public void addMouseListener(MouseListener l) {
    	entityList.addMouseListener(l);
    	fieldList.addMouseListener(l);
    }

    public void removeMouseListener(MouseListener l) {
    	entityList.removeMouseListener(l);
    	fieldList.removeMouseListener(l);
    }
    public void setListData(Object[] items) {
        PopListModel model = (PopListModel) entityList.getModel();
        model.setData(items);
        tabPane.setSelectedIndex(0);
        if(entityList.getModel().getSize()>0)
        {
        	entityList.setSelectedIndex(0);
        	entityList.ensureIndexIsVisible(0);
        }
        else
        	entityList.setSelectedIndex(-1);
        fieldList.setSelectedIndex(-1);
        entitySelectionChanged = true;

        model = (PopListModel) fieldList.getModel();
        model.clear();
    }

    public void setListData(List<?> items) {
        PopListModel model = (PopListModel) entityList.getModel();
        model.setData(items);
        tabPane.setSelectedIndex(0);
        if(entityList.getModel().getSize()>0)
        {
        	entityList.setSelectedIndex(0);
        	entityList.ensureIndexIsVisible(0);
        }
        else
        	entityList.setSelectedIndex(-1);
        entitySelectionChanged = true;

        model = (PopListModel) fieldList.getModel();
        model.clear();
    }
    public void clearList()
    {
    	((PopListModel)entityList.getModel()).clear();
    	((PopListModel)fieldList.getModel()).clear();
    }
    protected void setFieldData(List<Object> data) {
        PopListModel model = (PopListModel) fieldList.getModel();
        model.setData(data);
        if(data.size()>0)
            fieldList.setSelectedIndex(0);//选中第一项
    }
    public void resetSelection()
    {
    	entityList.getSelectionModel().setAnchorSelectionIndex(-1);
    	entityList.getSelectionModel().setLeadSelectionIndex(-1);
    	entityList.getSelectionModel().clearSelection();
    	
    	fieldList.getSelectionModel().setAnchorSelectionIndex(-1);
    	fieldList.getSelectionModel().setLeadSelectionIndex(-1);
    	fieldList.getSelectionModel().clearSelection();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.view.sqleditor.pop.SelectValueListener#selected()
     */
    public Object getSelectedValue() {
        String tmp = "";
        Object[] selectedValue = null;
        if (tabPane.getSelectedIndex() == 0)//如果选中了实体
        {
            selectedValue = entityList.getSelectedValues();
        } else if (tabPane.getSelectedIndex() == 1)//如果选中了字段
        {
            selectedValue = fieldList.getSelectedValues();
        }
        if (selectedValue == null||selectedValue.length==0)
            return null;
        int i = 0;
        for (; i < selectedValue.length - 1; i++) {
            tmp += selectedValue[i].toString() + ",";
        }
        tmp += selectedValue[i].toString();

        return tmp;
    }
    /**
     * Return type of object selected currently.
     * Return -1 if type is unknown.
     * @see {@link #SELECTEDTYPE_ENTITY},{@link #SELECTEDTYPE_COLUMN}
     */
    public int getSelectType()
    {
    	if (tabPane.getSelectedIndex() == 0)
    	{
    		return SELECTEDTYPE_ENTITY;
    	}else if (tabPane.getSelectedIndex() == 1)
        {
            return SELECTEDTYPE_COLUMN;
        }else
        	return -1;
    }
    /**
     * 
     * @author liu_xlin 一旦实体列表JList控件选择的值发生改变，确保选中的项被显示出来
     */
    private class ListSelectChangeListener implements ListSelectionListener {

        /*
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent e) {
            entitySelectionChanged = true;
            entityList.ensureIndexIsVisible(entityList.getSelectedIndex());
        }

    }
    /**
     * Execute when left/right arrow is Pressed , this action will change the index of TabbedPane
     * @author Kenny liu
     *
     * 2008-4-10 create
     */
    private class ArrowPressedAction extends PublicAction
    {

		private static final long serialVersionUID = 1L;

		/*
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if(ARROW_LEFT.equals(e.getActionCommand()))
			{
				int index=tabPane.getSelectedIndex();
				index--;
				if(index<0)
					index=tabPane.getTabCount()-1;
				tabPane.setSelectedIndex(index);
			}else if(ARROW_RIGHT.equals(e.getActionCommand()))
			{
				int index=tabPane.getSelectedIndex();
				index++;
				if(index>tabPane.getTabCount()-1)
					index=0;
				tabPane.setSelectedIndex(index);
			}
		}
    	
    }
    /**
     * Listen to Tabbed component, display the columns information of selected entity if field tab is selected.
     * @author liu_xlin 
     */
    private class FieldInfoDisplayListener implements ChangeListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            if (tabPane.getSelectedIndex() == 1 && entitySelectionChanged)//如果选中至字段列表页,并且实体选择发生改变
            {
                refreshFieldInfo();
                entitySelectionChanged = false; //恢复标志
            }
        }

        private void refreshFieldInfo() {
            Object[] selectedValue = entityList.getSelectedValues();
            if (selectedValue == null)
                return;
            Bookmark bookmark = BookmarkManage.getInstance().getDefaultBookmark();
            List<Object> list = new ArrayList<Object>();//放置字段列表的集合
            for (int i = 0; i < selectedValue.length; i++) {
                BaseListCell cell = (BaseListCell) selectedValue[i];
                Entity entity = EntityFactory.getInstance().create(bookmark,cell.getCatalog(),
                        cell.getSchema(), cell.getEntity(), cell.getTypeName(),
                        "", false);

                Column[] columns = null;
                try {
                    columns = entity.getColumns();
                } catch (UnifyException e) {
                    LogProxy.errorReport(e);
                    continue;
                } catch (SQLException e) {
                    LogProxy.SQLErrorReport(e);
                    continue;
                }
                for (int j = 0; j < columns.length; j++) {
                    int fieldType = columns[j].isPrimaryKey() ? BookMarkPubInfo.NODE_KEYCOLUMN
                            : BookMarkPubInfo.NODE_COLUMN;
                    FieldListCell fieldCell = new FieldListCell(cell.getCatalog(),cell
                            .getSchema(), cell.getEntity(), columns[j]
                            .getName(), fieldType);
                    list.add(fieldCell);
                }
            }
            setFieldData(list);
        }
    }
}
