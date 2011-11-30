/*
 * Created on 2007-1-17
 */
package com.coolsql.modifydatabase;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.coolsql.exportdata.Actionable;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.Inputer;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.ISQLDatabaseMetaData;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 实体信息显示面板，包含了实体所属书签、模式信息
 */
public class EntityDisplayPanel extends JPanel implements Inputer {

	private static final long serialVersionUID = 3126339103327334525L;

	/**
     * 当前被选中的实体对象
     */
    private Entity entityObject = null;

    private TextEditor bookmark = null; //书签

    private JLabel catalogLabel=null;// label for catalog text component
    private TextEditor catalog=null;  //the component that dispays catalog name
    
    private JLabel schemaLabel; //label for schema text component
    private TextEditor schema = null; //模式

    private TextEditor entity = null; //实体

    /**
     * 实体改变时，通知保存在该集合中的所有Actionable接口
     */
    private List<Actionable> entityChangeActions = null;

    public EntityDisplayPanel() {
        this(null);
    }

    public EntityDisplayPanel(Entity entityObject) {
        super();
        this.entityObject = entityObject;
        entityChangeActions = new ArrayList<Actionable>();
        createGUI();
        displayEntityInfo(entityObject);
    }

    private void createGUI() {

        //        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        //书签
        bookmark = new TextEditor(10);
        add(new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.bookmark")));
        bookmark.setEditable(false);
        add(bookmark);

        add(Box.createHorizontalStrut(6));

        //catalog 
        catalog = new TextEditor(10);
        catalogLabel=new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.catalog"));
        add(catalogLabel);
        catalog.setEditable(false);
        add(catalog);
        add(Box.createHorizontalStrut(6));
        
        //模式
        schema = new TextEditor(10);
        schemaLabel=new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.schema"));
        add(schemaLabel);
        schema.setEditable(false);
        add(schema);

        add(Box.createHorizontalStrut(6));

        //实体
        entity = new TextEditor(10);
        add(new JLabel(PublicResource
                .getSQLString("rowupdate.entitydisplay.entity")));
        entity.setEditable(false);
        add(entity);

        add(Box.createHorizontalStrut(6));

        if (isDisplayChangeBtn()) {
            RenderButton btn = new RenderButton(PublicResource
                    .getSQLString("rowupdate.entitydisplay.selectentity"));
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    selectEntity();
                }

            });
            add(btn);
        }

        setMinimumSize(new Dimension(600, 50));
    }

    /**
     * 添加实体改变的监听
     * 
     * @param action
     *            --实体变化时，需要通知的接口
     */
    public void addEntityChangeListener(Actionable action) {
        entityChangeActions.add(action);
    }

    /**
     * 删除实体改变监听
     * 
     * @param action
     *            --需要被删除的监听接口
     */
    public void removeEntityChangeListener(Actionable action) {
        entityChangeActions.remove(action);
    }

    private void notifyActions() {
        for (int i = 0; i < entityChangeActions.size(); i++) {
            Actionable action = (Actionable) entityChangeActions.get(i);
            action.action();
        }
    }

    /**
     * 显示实体信息
     * 
     * @param en
     *            --需要被显示的实体对象
     */
    public void displayEntityInfo(Entity en) {
        setEntityInfo(en);
    }

    /**
     * 获取实体对象
     * 
     * @return
     */
    public Entity getEntity() {
        return entityObject;
    }

    /**
     * 设置实体各信息字段的显示值
     * 
     * @param bookmarkStr
     *            --书签别名
     * @param catalogStr --catalog name
     * @param schemaStr
     *            --模式名
     * @param entityNameStr
     *            --实体名称
     */
    private void setEntityInfo(String bookmarkStr,String catalogStr, String schemaStr,
            String entityNameStr) {
    	
        bookmark.setText(bookmarkStr);
        catalog.setText(catalogStr);
        schema.setText(schemaStr);
        entity.setText(entityNameStr);
    }
    /**
     * set entity property ,display information on components ,such as catalog,schema and so on.
     * And it will set the visibility of catalog component and schema component
     * @param en -- entity object that will be display.
     */
    public void setEntityInfo(Entity en)
    {
    	if(en==null)
    		return;
    	setEntityInfo(en.getBookmark().getAliasName(),en.getCatalog(),en.getSchema(),en.getName());
    	try {
			ISQLDatabaseMetaData metaData=en.getBookmark().getDbInfoProvider().getDatabaseMetaData();
			boolean isSupportCatalog=metaData.supportsCatalogs();
			catalogLabel.setVisible(isSupportCatalog);
			catalog.setVisible(isSupportCatalog);
			
			boolean isSupportSchema=metaData.supportsSchemas();
			schemaLabel.setVisible(isSupportSchema);
			schema.setVisible(isSupportSchema);
		} catch (UnifyException e) {
			LogProxy.errorMessage(this,e.getMessage());
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(e);
		}
    	
    }
    /**
     * 选择实体
     *  
     */
    protected void selectEntity() {
        EntitySelectDialog entitySelector = null;
        Container con = GUIUtil.getUpParent(this, javax.swing.JDialog.class);
        if (con instanceof javax.swing.JDialog)
            entitySelector = new EntitySelectDialog((javax.swing.JDialog) con,
                    entityObject, this);
        else
            entitySelector = new EntitySelectDialog(entityObject, this);

        entitySelector.setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.data.display.Inputer#setData(java.lang.Object)
     */
    public void setData(Object value) {
        boolean isNeedNotify = true; //
        if (value instanceof Entity) {
            isNeedNotify = isNeedNotify((Entity) value);
            entityObject = (Entity) value;
        }
        if (isNeedNotify) //如果实体对象值发生改变
        {
            displayEntityInfo(entityObject);
            notifyActions();
        }
    }

    /**
     * 该方法定义了实体更新按钮的显示与否
     * 
     * @return --如果
     */
    public boolean isDisplayChangeBtn() {
        return true;
    }

    /**
     * 检验给定实体是否与当前实体对象相同，如果相同返回false，如果不同返回true。该方法用于判断是否需要更新界面信息以及通知监听事件
     * 
     * @param newValue
     *            --给定新实体对象
     * @return --true:如果当前实体对象不同于给定实体（newValue），否则返回false
     */
    private boolean isNeedNotify(Entity newValue) {
        if (newValue == null) {
            if (newValue == entityObject)
                return false;
            else
                return true;
        } else if (entityObject == null) {
            return true;
        } else {
//            if (newValue.getBookmark().getAliasName().equals(
//                    entityObject.getBookmark().getAliasName())
//                    && newValue.getSchema().equals(entityObject.getSchema())
//                    && newValue.getName().equals(entityObject.getName()))
//                return false;
//            else
//                return true;
        	return !entityObject.equals(newValue);
        }
    }
}
