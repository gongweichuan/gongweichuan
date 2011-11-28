/*
 * Created on 2007-1-12
 */
package com.coolsql.modifydatabase;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.Database;
import com.coolsql.sql.ISQLDatabaseMetaData;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.Schema;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 该面板用于选择实体
 */
public class EntitySelectPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
     * 书签选择组件
     */
    private JComboBox bookmarkSelector = null;

    private JComboBox catalogSelector=null; //component that selects catalog
    /**
     * 模式选择组件
     */
    private JComboBox schemaSelector = null;

    /**
     * 表选择组件
     */
    private JComboBox entitySelector = null;

    /**
     * 模式选择下拉框的菜单项的添加或者删除处理标志，true:正在处理;false:没有处理
     */
    private boolean isCatalogProcessing = false;

    public EntitySelectPanel() {
        this(null, null,null, null);
    }

    public EntitySelectPanel(String bookmark,String catalog, String schema, String entity) {
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        /**
         * 书签选择器的选择组件初始化
         */
        JPanel bookmarkPane = new JPanel();
        bookmarkPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        bookmarkSelector = new JComboBox();

        bookmarkPane.add(new JLabel(PublicResource
                .getSQLString("entityselect.label.bookmark")));
        bookmarkPane.add(bookmarkSelector);
        //        add(Box.createHorizontalStrut(12));

        //catalog selector 
        JPanel catalogPane = new JPanel();
        catalogPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        catalogSelector = new JComboBox();

        catalogPane.add(new JLabel(PublicResource
                .getSQLString("entityselect.label.catalog")));
        catalogPane.add(catalogSelector);
        catalogPane.setVisible(false);
        
        
        /**
         * 模式选择组件
         */
        JPanel schemaPane = new JPanel();
        schemaPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        schemaSelector = new JComboBox();

        schemaPane.add(new JLabel(PublicResource
                .getSQLString("entityselect.label.schema")));
        schemaPane.add(schemaSelector);
        schemaPane.setVisible(false);
        //        add(Box.createHorizontalStrut(12));

        /**
         * 表选择组件
         */
        JPanel entityPane = new JPanel();
        entityPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        entitySelector = new JComboBox();
        entityPane.add(new JLabel(PublicResource
                .getSQLString("entityselect.label.entity")));
        entityPane.add(entitySelector);

        //添加各选择组件
        add(bookmarkPane);
        add(catalogPane);
        add(schemaPane);
        add(entityPane);
        
        
        /**
         * 装载书签列表
         */
        GUIUtil.loadBookmarksToComboBox(bookmarkSelector);
        bookmarkSelector.setSelectedItem(null);
        bookmarkSelector.addItemListener(new BookmarkSelectorListener());
        bookmarkSelector.setSelectedItem(bookmark);
        
        /**
         * load catalog information
         */
        catalogSelector.setSelectedItem(null);
        catalogSelector.addItemListener(new catalogSelectorListener());
        if(catalog!=null&&!catalog.trim().equals(""))
        	catalogSelector.setSelectedItem(catalog);
        
        /**
         * load schema and entity information
         */
        if(schema==null||schema.trim().equals(""))
        	schemaSelector.setSelectedItem(null);
        else
        	schemaSelector.setSelectedItem(new Schema(catalog,schema));
        if (schema != null) //如果模式存在，装在实体信息
        {
            try {
                loadEntity((Schema)schemaSelector.getSelectedItem());
            } catch (SQLException e) {
                LogProxy.errorMessage(GUIUtil.getMainFrame(), PublicResource
                        .getSQLString("entityselect.getentityinfoerror")
                        + e.getMessage());
            } catch (UnifyException e) {
                LogProxy.errorMessage(GUIUtil.getMainFrame(), PublicResource
                        .getSQLString("entityselect.getentityinfoerror")
                        + e.getMessage());
            }
        }
        setSelectedEntity(entity);
        schemaSelector.addItemListener(new SchemaSelectorListener());
        
    }

    /**
     * 选中与给定实体名相同的项,如果给定实体名不存在,那么将选中值设为null
     * 
     * @param entityName
     *            --实体名
     */
    private void setSelectedEntity(String entityName) {
        if (entityName == null) {
            entitySelector.setSelectedItem(null);
            return;
        }
        for (int i = 0; i < entitySelector.getItemCount(); i++) {
            if (entityName.equals(StringUtil.trim(entitySelector.getItemAt(i)
                    .toString()))) {
                entitySelector.setSelectedIndex(i);
                return;
            }
        }
        entitySelector.setSelectedItem(null);
    }

    /**
     * 获取选择的书签别名
     * 
     * @return --书签别名
     */
    public String getSelectedBookmark() {
    	Object ob=bookmarkSelector.getSelectedItem();
        return ob==null?null:ob.toString();
    }

    /**
     * 获取选择的模式名
     * 
     * @return --模式名
     */
    public String getSelectedSchema() {
        return schemaSelector.getSelectedItem() == null ? null : schemaSelector
                .getSelectedItem().toString();
    }

    /**
     * 获取选择的表名
     * 
     * @return --表名
     */
    public Entity getSelectedEntity() {
        return (Entity) entitySelector.getSelectedItem();
    }

    /**
     * 重新设置被选择的值。该方法用于复位已经选择的值，设置为给定的值
     * 
     * @param catalog 设置分类
     * @param bookmark
     *            --将书签选中值设为给定值
     * @param schema
     *            --将模式选中值设为给定值
     * @param entity
     *            --将实体选中值设为给定值
     */
    public void resetSelected(String bookmark,String catalog, String schema, String entity) {
        bookmarkSelector.setSelectedItem(bookmark);
        
        if(schema==null||schema.trim().equals(""))
        	schemaSelector.setSelectedIndex(0);
        else
        	schemaSelector.setSelectedItem(new Schema(catalog,schema));

        entitySelector.setSelectedItem(entity);
    }

    /**
     * 装载模式列表
     * 
     * @param sc
     *            --已经获取到的模式列表
     */
    private void loadSchema(Schema[] sc) {
        if (sc == null)
            return;

        schemaSelector.removeAllItems();
        
        for (int i = 0; i < sc.length; i++) {
            schemaSelector.addItem(sc[i]);
        }
    }
    /**
     * load catalogs gave by parameter
     * @param catalogs
     */
    private void loadCatalog(String[] catalogs)
    {
    	if(catalogs==null)
    		return;
    	
    	catalogSelector.removeAllItems();
    	for(int i=0;i<catalogs.length;i++)
    	{
    		catalogSelector.addItem(catalogs[i]);
    	}
    }
    /**
     * 装载给定模式下的实体信息。
     * 
     * @param schema
     *            --给定的模式名，此参数不能为null
     * @throws SQLException
     * @throws UnifyException
     */
    private void loadEntity(Schema schema) throws SQLException, UnifyException {
        if (schema == null)
            return;

        Object bo = bookmarkSelector.getSelectedItem();
        Bookmark bookmark = BookmarkManage.getInstance().get(bo);
        if (bookmark == null) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("entityselect.nobookmark"), "warning", 2);
            return;
        }

        entitySelector.removeAllItems();
        Database db = bookmark.getDbInfoProvider();
        Entity[] entitys = db.getEntities(bookmark, schema,null);
//                new String[] { "TABLE", "VIEW" });

        if (entitys != null) {
            for (int i = 0; i < entitys.length; i++) {
                entitySelector.addItem(entitys[i]);
            }
        }
        entitySelector.setSelectedItem(null); //重新装载实体列表后，不选中任何项
    }
    /**
     * set the visibility of components(catalog,schema) in terms of current database
     * @param dbmd --metadata of current database
     * @throws SQLException
     */
    protected void setComponentsVisible(ISQLDatabaseMetaData dbmd) throws SQLException
    {
    	 boolean isSupportCatalog=dbmd.supportsCatalogs();
         if(!isSupportCatalog) 
         {
         	catalogSelector.removeAllItems();
         }
         JPanel pane=(JPanel)catalogSelector.getParent();
         pane.setVisible(isSupportCatalog);

         boolean isSupportSchema=dbmd.supportsSchemas();
         if(!isSupportSchema)
         {
          	schemaSelector.removeAllItems();
         }
         pane=(JPanel)schemaSelector.getParent();
       	 pane.setVisible(isSupportSchema);
    }
    /**
     * return whether catalog is supported
     * @return
     */
    protected boolean isCatalogEnable()
    {
    	JPanel pane=(JPanel)catalogSelector.getParent();
    	return pane.isVisible();
    }
    /**
     * return whether schema is supported
     * @return
     */
    protected boolean isSchemaEnable()
    {
    	JPanel pane=(JPanel)schemaSelector.getParent();
    	return pane.isVisible();
    }
    /**
     * 
     * @author liu_xlin 书签选择组件在更换书签时，重新装载模式信息
     */
    private class BookmarkSelectorListener implements ItemListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {

            if (e.getStateChange() == ItemEvent.SELECTED) {
                Bookmark bm = BookmarkManage.getInstance().get(e.getItem());
                if (bm == null)
                    return;

                Database db=null;
                try {
                    db = bm.getDbInfoProvider();
                } catch (UnifyException e2) {
                    LogProxy.errorReport(EntitySelectPanel.this, e2);
                }
                if (db == null) { //无数据库连接
                	catalogSelector.removeAllItems();
                    schemaSelector.removeAllItems();
                    entitySelector.removeAllItems();
                    return;
                }
                ISQLDatabaseMetaData dbmd=db.getDatabaseMetaData();
                try {
					setComponentsVisible(dbmd);

                	if(isCatalogEnable())
                	{
                		String[] catalogs=dbmd.getCatalogs();
                		try
                		{
                			isCatalogProcessing=true;
                			loadCatalog(catalogs);
                		}finally
                		{
                			isCatalogProcessing=false;
                		}
                	}
                	if(isSchemaEnable())
                	{
                		Schema[] schemas=dbmd.getSchemas();
                		loadSchema(schemas);
                	}
                } catch (SQLException e2) {
					LogProxy.SQLErrorReport(e2);
					return;  //return directly
				}

            }
        }

    }
    /**
     * item listener of catalog selector
     * @author 刘孝林
     *
     * 2008-1-5 create
     */
    private class catalogSelectorListener implements ItemListener
    {

		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			if(isCatalogProcessing)
				return;
			if (e.getStateChange() != ItemEvent.SELECTED)
				return;
				
			Bookmark bm = BookmarkManage.getInstance().get(bookmarkSelector.getSelectedItem());
			if(bm==null)
				return;
            
			String selectedCatalog=(String)catalogSelector.getSelectedItem();
			try
			{
//				ISQLDatabaseMetaData dbmd=bm.operatorOfDB().getDatabaseMetaData();
				if(isSchemaEnable())
				{
					Schema[] schemas=bm.getDbInfoProvider().getSchemas(selectedCatalog);
					loadSchema(schemas);
				}else  //do not support schema,and load entitys
				{
					loadEntity(new Schema(selectedCatalog,null));
				}
			}catch(SQLException e1)
			{
				LogProxy.SQLErrorReport(e1);
			} catch (UnifyException e1) {
				LogProxy.errorReport(EntitySelectPanel.this, e1);
			}
		}
    	
    }
    /**
     * 
     * @author liu_xlin 模式选择组件在更换模式时，重新装载模式下的实体信息
     */
    private class SchemaSelectorListener implements ItemListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
//                if (isProcessing)
//                    return;
                if (e.getItem() == null) {
                    entitySelector.setSelectedItem(null);
                    return;
                }

                try {
                    loadEntity((Schema) e.getItem());
                } catch (SQLException e1) {
                    LogProxy.SQLErrorReport(e1);
                } catch (UnifyException e1) {
                    LogProxy.errorReport( e1);
                }
            }

        }

    }
}
