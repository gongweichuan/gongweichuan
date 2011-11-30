/*
 * 创建日期 2006-8-19
 */
package com.coolsql.search;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.bookmarkBean.BookmarkUpdateOfComboBoxListener;
import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.ExtendComboBox;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.model.Schema;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 对数据库信息的查找窗口，提供信息的模糊查询
 */
public class SearchInfoDialog extends BaseDialog implements ActionListener {

	private static final Logger logger=Logger.getLogger(SearchInfoDialog.class);
	
	private static final long serialVersionUID = 1L;

	private static SearchInfoDialog instance = null;

    /**
     * 对上一次查找，关闭窗口后，将查询条件保存，便于以后查找时，自动显示设置为上一次的查询条件
     */
    private static String oldAliasName = null; //书签别名

    private static String oldCatalogName=null;  //分类名称
    private static Object oldSchemaName = ""; //模式名

    private static String oldEntityName = ""; //实体名

    private static String oldColumnName = ""; //列名

    private static boolean isEntityQuery = true; //是否是实体查询

    /**
     * 曾经使用的查询关键字
     */
    private static List<String> pastedEntity=null;  //曾使用的实体关键字
    private static List<String> pastedColumn=null;  //曾使用的列关键字
    private static int maxSavedEntity=15;   //实体关键字的最大保存数量
    private static int maxSavedColumn=15;   //列关键字的最大保存数量
    /**
     * 数据库的选择
     */
    private JComboBox db = null;

    /**
     * 分类的选择
     */
    private ExtendComboBox catalog = null;
    private JLabel catalogLabel=null; //分类的标签
    /**
     * 模式的选择
     */
    private ExtendComboBox schema = null;
    private JLabel schemaLabel=null; //label of component that select schema;
    
    /**
     * 表名输入框
     */
    private ExtendComboBox table = null;

    /**
     * 列名输入框
     */
    private ExtendComboBox column = null;

    private ExtendComboBox.SearchModeSelect columnMode = null;//定义为全局变量，便于界面友好的控制(使其是否可用)

    private QueryInfoThread thread = null; //查询线程，帮助查询结果窗口的弹出，而不影响主窗口的执行

    private BookmarkUpdateOfComboBoxListener listener = null;//书签别名监听

    private BookmarkConnectedPropertyListener connectedListener=null;//listener bookmark'connect state
    static {
        pastedEntity=new ArrayList<String>();
        pastedColumn=new ArrayList<String>();
    }
    private SearchInfoDialog(JFrame main) {
        super(main, false);
        this.setTitle("query database information!");
        initCondition(main);

        listener = new BookmarkUpdateOfComboBoxListener(db);
        connectedListener=new BookmarkConnectedPropertyListener();
        BookmarkManage.getInstance().addBookmarkListener(listener);
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].addPropertyListener(listener);
            bookmarks[i].addPropertyListener(connectedListener);
        }

        //初始化查询线程，并启动该线程
        thread = new QueryInfoThread(new QueryDBInfo(this));
        thread.start();
    }

    public static synchronized SearchInfoDialog getInstance(JFrame main) {
        if (instance == null)
        {
            instance = new SearchInfoDialog(main);
        }
        return instance;
    }

    /**
     * 检验该窗口是否已经实例化，避免重复弹出
     * 
     * @return
     */
    public static boolean checkInstanced() {
        return instance != null;
    }

    private void initCondition(JFrame parent) {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel content = (JPanel) this.getContentPane();
        JPanel main = new JPanel();
        main.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.entityselect.label")), gbc);
        gbc.gridx++;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel entitySelect = new JPanel();
        entitySelect.setLayout(new FlowLayout(FlowLayout.LEFT));
        JRadioButton radioTable = new JRadioButton("Entity");
        JRadioButton radioColumn = new JRadioButton("Column");
        RadioListener itemListener = new RadioListener();
        radioTable.addItemListener(itemListener);
        radioColumn.addItemListener(itemListener);

        ButtonGroup group = new ButtonGroup();
        group.add(radioTable);
        group.add(radioColumn);
        entitySelect.add(radioTable);
        entitySelect.add(radioColumn);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(entitySelect, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.databaseselect.label")), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        db = new JComboBox();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(db, gbc);

        //88888
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        catalogLabel=new JLabel(PublicResource
                .getSQLString("searchinfo.catalogselect.label"));
        main.add(catalogLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        catalog = new ExtendComboBox();
        catalog.setEditable(true);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(catalog, gbc);
        //888888
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        schemaLabel=new JLabel(PublicResource
                .getSQLString("searchinfo.schemaselect.label"));
        main.add(schemaLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        schema = new ExtendComboBox();
//        schema.addSubmitListener(this);
        schema.setEditable(true);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        main.add(schema, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.table.label")), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        table = new ExtendComboBox();
//        table.addSubmitListener(this);
        table.setEditable(true);
        main.add(table, gbc);
        gbc.gridx = 2;
        ExtendComboBox.SearchModeSelect tableMode = table.new SearchModeSelect();
        tableMode.setToolTipText(PublicResource
                .getSQLString("searchinfo.tablemodeprompt"));
        tableMode.setEditable(false);
        main.add(tableMode, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        main.add(new JLabel(PublicResource
                .getSQLString("searchinfo.column.label")), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        column = new ExtendComboBox();
//        column.addSubmitListener(this);
        column.setEditable(true);
        main.add(column, gbc);
        columnMode = column.new SearchModeSelect();
        columnMode.setEditable(false);
        columnMode.setToolTipText(PublicResource
                .getSQLString("searchinfo.columnmodeprompt"));
        gbc.gridx = 2;
        main.add(columnMode, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getSQLString("searchinfo.title"));
        main.setBorder(border);

        JPanel buttons = new JPanel();
        RenderButton query = new RenderButton(PublicResource
                .getSQLString("searchinfo.command.query"));
        query.addActionListener(this);
        RenderButton quit = new RenderButton(PublicResource
                .getSQLString("searchinfo.command.exit"));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quitQuery();
            }
        });
        buttons.add(query);
        buttons.add(quit);

        /**
         * 根据上次查询的类型，设置类型
         */
        if (isEntityQuery)
            radioTable.setSelected(true);
        else
            radioColumn.setSelected(true);

        content.add(main, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        this.getRootPane().setDefaultButton(query);
        //        pack();
        this.setSize(350, 330);
        centerToFrame(parent);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitQuery();
            }
        });
//        this.setResizable(false);
        this.setVisible(true);
        //查询窗口初始化完成后，开始装载数据
        loadAliasData();
    }

    /**
     * 将已有的数据库配置添加到数据库下拉选择控件中
     *  
     */
    private void loadAliasData() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) db.getModel();
        Set<String> set = BookmarkManage.getInstance().getAliases();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            model.addElement(it.next());
        }
        String selectedBookmark = (String) model.getSelectedItem(); //选中的书签别名
        loadCatalogData(selectedBookmark);

        db.addItemListener(new BookmarkItemListener());
        catalog.addItemListener(new CatalogItemListener());
        
        String selectedCcatalog=(String)catalog.getSelectedItem();
        loadSchemaData(selectedBookmark,selectedCcatalog);
        loadOldCondition();
    }

    /**
     * 设置成上次保存的查询条件
     *  
     */
    private void loadOldCondition() {
        if (db != null&&oldAliasName!=null) {
            db.setSelectedItem(oldAliasName);
        }else
        {
            
            Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
            if(bookmark!=null)
                db.setSelectedItem(bookmark.getAliasName());
        }
        if (catalog != null&&catalog.isVisible()) {
        	catalog.setSelectedItem(oldCatalogName);
        }
        if (schema != null&&schema.isVisible()) {
            schema.setSelectedItem(oldSchemaName);
        }
        if (table != null) {
            table.setSelectedItem(oldEntityName);
        }
        if (column != null) {
            column.setSelectedItem(oldColumnName);
        }
        
        /**
         * 将以前查询的关键字进行加载
         */
        for(int i=pastedEntity.size()-1;i>-1;i--)
        {
            table.addItem(pastedEntity.get(i));
        }
        for(int i=pastedColumn.size()-1;i>-1;i--)
        {
            column.addItem(pastedColumn.get(i));
        }
    }
    /**
     * 设置分类选择组件是否被应藏
     * @param isVisible 是否可视true:可视，false：不可视
     */
    private void setCatalog(boolean isVisible)
    {
    	if(!isVisible)
    	{
    		catalog.removeAllItems();
    		catalog.setVisible(false);
    		catalogLabel.setVisible(false);
    	}else
    	{
    		catalog.setVisible(true);
    		catalogLabel.setVisible(true);
    	}
    }
    /**
     * 加载分类数据
     * @param bookmarkAlias
     */
    protected void loadCatalogData(String bookmarkAlias)
    {
        if (catalog.getSelectedItem() != null)
            oldCatalogName = (String)catalog.getSelectedItem();//清空模式之前，保存选择的值
        catalog.removeAllItems();
        
    	Bookmark bookmark=BookmarkManage.getInstance().get(bookmarkAlias);
    	if(bookmark==null||!bookmark.isConnected())
    		return ;
    	try {
    		boolean isSupported=bookmark.getDbInfoProvider().getDatabaseMetaData().supportsCatalogs();
			setCatalog(isSupported);
			if(!isSupported) //不支持分类，直接返回
				return;
			
			String[] catalogs=bookmark.getDbInfoProvider().getDatabaseMetaData().getCatalogs();
			if(catalogs==null)
				return;
			
			for(String c:catalogs)
				catalog.addItem(c);
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(e);
		} catch (UnifyException e) {
			LogProxy.errorReport(logger,e);
		}
    }
    /**
     * Set whether component that select schema is not visible.
     * @param isVisible true:is visible,false:is not visible
     */
    private void setSchema(boolean isVisible)
    {
    	if(!isVisible)
    	{
    		schema.removeAllItems();
    		
    	}
    	schema.setVisible(isVisible);
    	schemaLabel.setVisible(isVisible);
    }
    /**
     * 装载数据库对应的模式列表
     * 
     * @param alias
     */
    protected void loadSchemaData(String alias,String selectedCatalog) {
        if (schema.getSelectedItem() != null)
            oldSchemaName = schema.getSelectedItem();//清空模式之前，保存选择的值

        schema.removeAllItems();
        Bookmark tmp = BookmarkManage.getInstance().get(alias);
        if(tmp==null||!tmp.isConnected())
        	return ;
        
        try {
			boolean isSupportSchema=tmp.getDbInfoProvider().getDatabaseMetaData().supportsSchemas();
			setSchema(isSupportSchema);
			if(!isSupportSchema)  //not support schema ,so return directly
				return;
			
			Schema[] schemas=tmp.getDbInfoProvider().getSchemas(selectedCatalog);
			if(schemas==null)
				return;
			
			for(int i=0;i<schemas.length;i++)
			{
				schema.addItem(schemas[i]);
			}
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(e);
		} catch (UnifyException e) {
			LogProxy.errorReport(logger, e);
		}
    }
    public void actionPerformed(ActionEvent e) {
        query();
    }

    /**
     * 获取查询的对象类型 0：实体 1：列信息
     * 
     * @return
     */
    public int getQueryType() {
        if (!column.isEnabled()) // 实体查询
            return 0;
        else
            //查询列信息
            return 1;
    }

    /**
     * 查询信息
     *  
     */
    public void query() {
        Bookmark bookmark = getSelectBookmark();
        if (bookmark == null)
            return;

        if (!column.isEnabled()) // 实体查询
        {
            saveEntityKeyWord();

            SearchResultFrame resultFrame = new SearchOfEntityFrame(this,
                    bookmark);
            resultFrame.setKeyword(getQueryEntity());
            thread.setOperateWindow(resultFrame); //更新操作窗口为查询结果窗口
            thread.launch(); //启动线程
            resultFrame.setVisible(true);

        } else //列查询
        {
            /**
             * 需要将实体和列的关键字都保存
             */
            saveEntityKeyWord();
            saveColumnKeyWord();
            
            SearchResultFrame resultFrame = new SearchOfColumnFrame(this,
                    bookmark);
            resultFrame.setKeyword(getQueryColumn());
            thread.setOperateWindow(resultFrame); //更新操作窗口为查询结果窗口
            thread.launch(); //启动线程
            resultFrame.setVisible(true);

        }
    }
    /**
     * 将实体关键字保存
     *
     */
    protected void saveEntityKeyWord()
    {
        Object ob=table.getSelectedItem();
        String tmp=ob==null?"":ob.toString();
        tmp=StringUtil.trim(tmp);
        if(tmp.equals(""))
            return;
        
        if(addQueryKeyWord(pastedEntity,tmp,maxSavedEntity))////将当前关键字保存
        {
            table.removeItem(tmp);
        }else
        {
            if(table.getItemCount()>=maxSavedEntity) //如果超出最大保存数量，将末尾元素删除
            {
                table.removeItemAt(table.getItemCount()-1);
            }
        }
        table.insertItemAt(tmp,0);
        table.setSelectedItem(ob);
    }
    /**
     * 将列关键字保存
     *
     */
    protected void saveColumnKeyWord()
    {        
        Object ob=column.getSelectedItem();
        String tmp=ob==null?"":ob.toString();
        tmp=StringUtil.trim(tmp);
        if(tmp.equals(""))
            return;
        
        if(addQueryKeyWord(pastedColumn,tmp,maxSavedColumn))  //将当前关键字保存
        {    
            column.removeItem(tmp);           
        }else
        {
            if(column.getItemCount()>=maxSavedColumn)  //如果超出最大保存数量，将末尾元素删除
            {
                column.removeItemAt(column.getItemCount()-1);
            }
        }
        column.insertItemAt(tmp,0);//将关键字加至下拉框的第一个位置
        column.setSelectedItem(ob);
    }
    /**
     * 将查询关键字保存至相应列表中.如果不存在,添加至列表的第一个位置;如果已经存在,将该关键字移到第一个位置
     * @param list  --保存关键字的列表
     * @param keyWord  --关键字
     * @param limit --列表的最大长度
     */
    private boolean addQueryKeyWord(List<String> list,String keyWord,int limit)
    {
        boolean isContained=false;
        if(list.contains(keyWord))  //如果该关键字已经存在,删除之
        {
           list.remove(keyWord);  
           isContained=true;
        }else 
        {
            if(list.size()>=limit ) //如果列表已经达到最大长度,将第一个位置的元素删除
                list.remove(0);
        }
        list.add(keyWord);  //将关键字添加至末尾
        return isContained;
    }
    /**
     * 获取分类条件
     * @return
     */
    protected String getQueryCatalog()
    {
    	Object ob=catalog.getSelectedItem();
    	String catalogName=null;
    	 if(ob!=null)
         {
    		 catalogName= StringUtil.trim(ob.toString());
         }
         if ("".equals(catalogName))
        	 catalogName = null;

         return catalogName;
    }
    /**
     * 获取有效的模式名，以便于数据的查找
     * 
     * @return
     */
    protected String getQuerySchema() {
    	Object ob=schema.getSelectedItem();//模式
    	
        String schemaName = null;
        if(ob!=null)
        {
        	if(ob instanceof Schema)
        	{
        		Schema tmpSchema=(Schema)ob;
        		if(!tmpSchema.isDefault())  //如果该模式是缺省模式，将模式条件视为null
        			schemaName=StringUtil.trim(tmpSchema.getName());
        	}else
        		schemaName=StringUtil.trim(ob.toString());
        }
        if ("".equals(schemaName)) //如果没有输入模式，那么将模式名赋值为null
            schemaName = null;

        return schemaName;
    }

    /**
     * 获取实体查询字段
     * 
     * @return
     */
    protected String getQueryEntity() {
        String tmp=table.getSelectedItem()==null?"":table.getSelectedItem().toString();
        String entityName = StringUtil.trim(tmp);
        if (entityName.equals(""))
            return null;
        int mode = table.getQueryMode(); //获取实体查询模式
        if (mode == 0) //完全相等,不用进行处理
        {

        } else if (mode == 1) //以关键字开头
        {
            entityName = entityName + "%";
        } else if (mode == 2) //以关键字结尾
        {
            entityName = "%" + entityName;
        } else if (mode == 3) //包含关键字
        {
            entityName = "%" + entityName + "%";
        }
        return entityName;
    }

    /**
     * 获取列查询字段
     * 
     * @return
     */
    protected String getQueryColumn() {
        String tmp=column.getSelectedItem()==null?"":column.getSelectedItem().toString();
        String columnName = StringUtil.trim(tmp);
        if (columnName.equals(""))
            return null;
        int mode = column.getQueryMode(); //获取实体查询模式
        if (mode == 0) //完全相等,不用进行处理
        {

        } else if (mode == 1) //以关键字开头
        {
            columnName = columnName + "%";
        } else if (mode == 2) //以关键字结尾
        {
            columnName = "%" + columnName;
        } else if (mode == 3) {
            columnName = "%" + columnName + "%";
        }
        return columnName;
    }

    /**
     * 获取选中的书签
     * 
     * @return 如果找不到，返回null
     */
    public Bookmark getSelectBookmark() {
        String aliasName = StringUtil.trim((String) db.getSelectedItem());
        if (aliasName.equals("")) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("searchinfo.query.noalias"), "warning", 2);
            return null;
        }
        BookmarkManage bmm = BookmarkManage.getInstance();
        return bmm.get(aliasName);
    }

    /**
     * 退出查询
     *  
     */
    public void quitQuery() {
        thread.dispose(); //结束查询线程

        /**
         * 除去对各书签的监听
         */
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].removePropertyListener(listener);
            bookmarks[i].removePropertyListener(connectedListener);
        }
        BookmarkManage.getInstance().removeBookmarkListener(listener);

        /**
         * 保存本次的查询条件
         */
        oldAliasName = db.getSelectedItem() == null ? "" : db.getSelectedItem()
                .toString();
        oldCatalogName=catalog.getSelectedItem()==null?"":catalog.getSelectedItem().toString();
        oldSchemaName = schema.getSelectedItem() == null ? "" : schema
                .getSelectedItem();
        oldEntityName = table.getSelectedItem()==null?"":table.getSelectedItem().toString();
        oldColumnName = column.getSelectedItem()==null?"":column.getSelectedItem().toString();

        this.dispose();
        instance.dispose();
        instance = null;

    }

    /**
     * 居于指定frame中间
     * 
     * @param f
     */
    public void centerToFrame(JFrame f) {
        Rectangle rect = f.getBounds();
        this
                .setBounds((int) (rect.getX() + (rect.getWidth() - this
                        .getWidth()) / 2), (int) (rect.getY() + (rect
                        .getHeight() - this.getHeight()) / 2), this.getWidth(),
                        this.getHeight());
    }

    /**
     * 
     * @author liu_xlin 查询对象的选择的监听器，根据查询对象的不同，对列条件的选择进行是否可用的控制
     */
    private class RadioListener implements ItemListener {

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) { //如果指定项被选中以后,才作相应的调整
                JRadioButton bu = (JRadioButton) e.getSource();
                if (bu.getText().equals("Entity")) {
                    if (column.isEnabled()) {
                        column.setEnabled(false);
                    }
                    if (columnMode.isEnabled())
                        columnMode.setEnabled(false);
                    isEntityQuery = true;
                } else {
                    if (!column.isEnabled()) {
                        column.setEnabled(true);
                    }
                    if (!columnMode.isEnabled())
                        columnMode.setEnabled(true);

                    isEntityQuery = false;
                }

            }
        }
    }

    private class BookmarkItemListener implements ItemListener
    {

		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getStateChange() == ItemEvent.SELECTED) { //如果指定项被选中以后,才作相应的调整
		        String selectedBookmark = (String) db.getSelectedItem(); //选中的书签别名
		        loadCatalogData(selectedBookmark);
		        if(catalog.isVisible())
		        	catalog.setSelectedItem(oldCatalogName);
		        
		        String selectedCcatalog=(String)catalog.getSelectedItem();
		        loadSchemaData(selectedBookmark,selectedCcatalog);
	        }
		}
    	
    }
    private class CatalogItemListener implements ItemListener
    {
		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getStateChange() == ItemEvent.SELECTED) { //如果指定项被选中以后,才作相应的调整
				 if(!schema.isVisible())  //if the schema selector is not visible,it means that current bookmark don't support schema.
		            	return;
	            String alias = (String) db.getSelectedItem();
	            String selectedCatalog=(String)catalog.getSelectedItem();
	           
	            loadSchemaData(alias,selectedCatalog);
	            schema.setSelectedItem(oldSchemaName);
	        }
		}
    	
    }
    private class BookmarkConnectedPropertyListener implements PropertyChangeListener
    {

		/* (non-Javadoc)
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			String name=evt.getPropertyName();
			if(name.equals(Bookmark.PROPERTY_CONNECTED))
			{
				Bookmark source=(Bookmark)evt.getSource();
				if(source.isConnected())
				{
					String selectedBookmark=(String)db.getSelectedItem();
					if(source.getAliasName().equals(selectedBookmark))
					{
						loadCatalogData(selectedBookmark);
				        if(catalog.isVisible())
				        	catalog.setSelectedItem(oldCatalogName);
				        	
				        String selectedCcatalog=(String)catalog.getSelectedItem();
				        loadSchemaData(selectedBookmark,selectedCcatalog);
				        if(schema.isVisible())
				        	schema.setSelectedItem(oldSchemaName);
					}
				}
			}
			
		}
    	
    }
}
