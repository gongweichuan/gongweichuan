/*
 * 创建日期 2006-7-2
 *
 */
package com.coolsql.view.bookmarkview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;

import com.coolsql.action.bookmarkmenu.AddEntityDataAction;
import com.coolsql.action.bookmarkmenu.AddSqlToEditorAction;
import com.coolsql.action.bookmarkmenu.CearTableAction;
import com.coolsql.action.bookmarkmenu.ConnectAction;
import com.coolsql.action.bookmarkmenu.CopyAction;
import com.coolsql.action.bookmarkmenu.CopyCommand;
import com.coolsql.action.bookmarkmenu.DeleteBookMarkAction;
import com.coolsql.action.bookmarkmenu.DisconnectAction;
import com.coolsql.action.bookmarkmenu.PropertyAction;
import com.coolsql.action.bookmarkmenu.QueryAllDataOfTableAction;
import com.coolsql.action.bookmarkmenu.ReNameAction;
import com.coolsql.action.bookmarkmenu.RefreshAction;
import com.coolsql.action.bookmarkmenu.SearchAction;
import com.coolsql.action.bookmarkmenu.SetAsDefaultBookmarkAction;
import com.coolsql.action.bookmarkmenu.ViewCountOfTableAction;
import com.coolsql.action.common.Command;
import com.coolsql.action.common.PublicAction;
import com.coolsql.action.framework.CsAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.model.Entity;
import com.coolsql.system.Setting;
import com.coolsql.system.menu.action.NewBookmarkMenuAction;
import com.coolsql.system.menubuild.IconResource;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.View;
import com.coolsql.view.bookmarkview.model.Identifier;

/**
 * @author liu_xlin
 *  
 */
public class BookMarkMenuManage extends BaseMenuManage {

    private BasePopupMenu bookMarkMenu = null; //popup menu of bookmark node

    private BasePopupMenu pubMenu = null; //popup menu of root,tabletype,column node

    private BasePopupMenu sqlMenu = null;  ////popup menu of sql node

    private BasePopupMenu tableMenu = null;//popup menu of table node

    //************************************************************bookmark
    /**
     * bookmark的删除，重命名
     */
    private JMenuItem deleteBookMark = null;

    private JMenuItem renameBookMark = null;

    private JMenuItem copyBookMark = null;

    private JMenuItem setAsDefault = null;

    /**
     * bookmark的连接和断开连接
     */
    private JMenuItem connect = null;

    private JMenuItem disconnect = null;

    /**
     * bookmark的刷新和属性
     */
    private JMenuItem refresh = null;

    private JMenuItem property = null;

    //*************************************************************pubmenu
    private JMenuItem copyForPub = null;

    private JMenuItem refreshForPub = null;

    private JMenuItem propertyForPub = null;

    //*************************************************************sqlmenu
    private JMenuItem copyForSql = null;

    private JMenuItem openSql = null;

    //    private JMenuItem execute=null;
    private JMenuItem propertyForSql = null;

    //*************************************************************tablemenu
    private JMenuItem copyForTable = null;

    private JMenuItem copyQualifiedName=null;
    private JMenuItem viewAllRows = null;

    private JMenuItem refreshForTable = null;

    private JMenuItem propertyForTable = null;
    
    /**
     * View the count of data in selected table.
     */
    private JMenuItem viewCountOfTable;
    
    private JMenuItem addData=null;  //添加新数据
    
    private JMenuItem clearAll=null;//清空所有数据

    /**
     * 公共的复制和属性查看事件处理
     */
    private CsAction newBookmarkAction = null;

    private PublicAction copyAction = null;

    private PublicAction propertyAction = null;

    private CsAction searchAction = null;

    private PublicAction refreshAction = null;

    private DeleteBookMarkAction deleteAction=null;
    /**
     * @param view
     */
    public BookMarkMenuManage(View view) {
        super(view);
        newBookmarkAction = Setting.getInstance().getShortcutManager().getActionByClass(NewBookmarkMenuAction.class);
        
        copyAction = new CopyAction((BookmarkView) view);
        propertyAction = new PropertyAction((BookmarkView) view);

        //查找事件处理
        searchAction =Setting.getInstance().getShortcutManager().getActionByClass(SearchAction.class);
//        bindKey("control F", searchAction, true);

        //刷新事件处理
        refreshAction = new RefreshAction((BookmarkView) view);
        bindKey(((BookmarkView) view).getConnectTree(), "F5",
                refreshAction, false);

        deleteAction=new DeleteBookMarkAction((View)getComponent());
        bindKey(((BookmarkView) view).getConnectTree(), "DELETE",
                deleteAction, false);
        
        this.createPubMenu();
    }

    /**
     * 创建视图和表节点的右键菜单
     *  
     */
    protected void createTableMenu() {
        tableMenu = new BasePopupMenu();
        tableMenu.add(newBookmarkAction.getMenuItem());
        
        copyForTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyForTable.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        tableMenu.add(copyForTable);
        
        ActionListener copyQualifiedListener=new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		DefaultMutableTreeNode node = (DefaultMutableTreeNode) (((BookmarkView) getComponent())
        				.getConnectTree().getLastSelectedPathComponent());
        		Identifier userOb=(Identifier)(node.getUserObject());
        		if(userOb.getType()==BookMarkPubInfo.NODE_TABLE||BookMarkPubInfo.NODE_VIEW==userOb.getType())
        		{
        			Entity en=(Entity)userOb.getDataObject();
        			Command cp=new CopyCommand(en.getQualifiedName());
        			cp.execute();
        		}
        	}
        };
        copyQualifiedName = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copyqualifiedname"), IconResource.getBlankIcon(), copyQualifiedListener);
        tableMenu.add(copyQualifiedName);

        viewAllRows = createMenuItem(PublicResource
                .getString("bookmarkView.popup.queryrows"), PublicResource
                .getIcon("bookmarkView.popup.queryrows.icon"),
                new QueryAllDataOfTableAction((BookmarkView) this
                        .getComponent()));
        viewAllRows.setMnemonic('A');
        tableMenu.add(viewAllRows);
        
        viewCountOfTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.viewcountoftable"), IconResource.getBlankIcon(),
                new ViewCountOfTableAction((BookmarkView) this
                        .getComponent()));
        viewCountOfTable.setMnemonic('C');
        tableMenu.add(viewCountOfTable);
        
        addData=createMenuItem(PublicResource
                .getString("bookmarkView.popup.adddata"), IconResource
                .getIcon("system.icon.addrows"), new AddEntityDataAction());
        tableMenu.add(addData);
        
        CearTableAction clearAction=new CearTableAction();
        clearAll=createMenuItem(PublicResource
                .getString("bookmarkView.popup.deleteall"), IconResource.getBlankIcon(), clearAction);
        tableMenu.add(clearAll);
        
        refreshForTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.refresh"), PublicResource
                .getIcon("bookmarkView.popup.refresh.icon"), refreshAction);
        tableMenu.add(refreshForTable);

        refreshForTable.setAccelerator(KeyStroke.getKeyStroke("F5"));
        
        propertyForTable = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        tableMenu.add(propertyForTable);
    }

    /**
     * 创建sql节点专用的菜单
     *  
     */
    protected void createSqlMenu() {
        sqlMenu = new BasePopupMenu();

        sqlMenu.add(newBookmarkAction.getMenuItem());
        copyForSql = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyForSql.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        sqlMenu.add(copyForSql);

        /**
         * 打开sql菜单项
         */
        openSql = createMenuItem(PublicResource
                .getString("bookmarkView.popup.open.txt"), PublicResource
                .getIcon("bookmarkView.popup.open.icon"),
                new AddSqlToEditorAction((BookmarkView) this.getComponent()));
        sqlMenu.add(openSql);

        sqlMenu.add(searchAction.getMenuItem());
        propertyForSql = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        sqlMenu.add(propertyForSql);
    }

    /**
     * 创建公共菜单
     *  
     */
    protected void createPubMenu() {
        pubMenu = new BasePopupMenu();

        pubMenu.add(newBookmarkAction.getMenuItem());
        
        copyForPub = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyForPub.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        pubMenu.add(copyForPub);

        pubMenu.add(searchAction.getMenuItem());

        refreshForPub = createMenuItem(PublicResource
                .getString("bookmarkView.popup.refresh"), PublicResource
                .getIcon("bookmarkView.popup.refresh.icon"), refreshAction);
        pubMenu.add(refreshForPub);
        //        this.bindKey(refreshForPub, "F5", refreshAction, false);
        refreshForPub.setAccelerator(KeyStroke.getKeyStroke("F5"));

        propertyForPub = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        pubMenu.add(propertyForPub);

    }

    /**
     * 创建右键菜单(书签)
     *  
     */
    protected void createBookMarkMenu() {
        bookMarkMenu = new BasePopupMenu();

        bookMarkMenu.add(newBookmarkAction.getMenuItem());
        
        deleteBookMark = createMenuItem(PublicResource
                .getString("bookmarkView.popup.delete"), PublicResource
                .getIcon("bookmarkView.popup.delete.icon"),
                deleteAction);
        deleteBookMark.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        bookMarkMenu.add(deleteBookMark);
        renameBookMark = createMenuItem(PublicResource
                .getString("bookmarkView.popup.rename"), PublicResource
                .getIcon("bookmarkView.popup.rename.icon"), new ReNameAction(
                (View) this.getComponent()));
        bookMarkMenu.add(renameBookMark);

        copyBookMark = createMenuItem(PublicResource
                .getString("bookmarkView.popup.copy"), PublicResource
                .getIcon("bookmarkView.popup.copy.icon"), copyAction);
        copyBookMark.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        bookMarkMenu.add(copyBookMark);
        bookMarkMenu.add(new JSeparator());
        connect = createMenuItem(PublicResource
                .getString("bookmarkView.popup.connect"), PublicResource
                .getIcon("bookmarkView.popup.connect.icon"), new ConnectAction(
                (View) this.getComponent()));
        bookMarkMenu.add(connect);
        disconnect = createMenuItem(PublicResource
                .getString("bookmarkView.popup.disconnect"), PublicResource
                .getIcon("bookmarkView.popup.disconnect.icon"),
                new DisconnectAction((View) this.getComponent()));
        bookMarkMenu.add(disconnect);
        bookMarkMenu.add(new JSeparator());

        setAsDefault = createMenuItem(PublicResource
                .getString("bookmarkView.popup.setasdefault"), PublicResource
                .getIcon("bookmarkView.popup.setasdefault.icon"),
                new SetAsDefaultBookmarkAction((BookmarkView) getComponent()));
        bookMarkMenu.add(setAsDefault);

        refresh = createMenuItem(PublicResource
                .getString("bookmarkView.popup.refresh"), PublicResource
                .getIcon("bookmarkView.popup.refresh.icon"), refreshAction);
        bookMarkMenu.add(refresh);
        //        this.bindKey(refresh, "F5", refreshAction, false);
        refresh.setAccelerator(KeyStroke.getKeyStroke("F5"));

        bookMarkMenu.add(searchAction.getMenuItem());
        property = createMenuItem(PublicResource
                .getString("bookmarkView.popup.property"), PublicResource
                .getIcon("bookmarkView.popup.property.icon"), propertyAction);
        bookMarkMenu.add(property);

    }
    /**
     * Add a menuitem/menu to bookmark popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to bookmark popup menu.
     */
    public JMenuItem addMenuOnBookmark(JMenuItem m)
    {
    	BasePopupMenu pop=getBookMarkMenu();
    	if(pop==null||m==null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to bookmark popup menu.
     */
    public void addSeparatorOnBookmark() {
    	BasePopupMenu pop = getBookMarkMenu();
    	pop.addSeparator();
    }
    /**
     * add a menuitem/menu to pub popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to pub popup menu.
     */
    public JMenuItem addMenuOnPub(JMenuItem m)
    {
    	BasePopupMenu pop=getPubMenu();
    	if(pop==null||m==null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to pub popup menu.
     */
    public void addSeparatorOnPub() {
    	BasePopupMenu pop = getPubMenu();
    	pop.addSeparator();
    }
    /**
     * add a menuitem/menu to sql popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to sql popup menu.
     * @return
     */
    public JMenuItem addMenuOnSql(JMenuItem m)
    {
    	BasePopupMenu pop=getSqlMenu();
    	if(pop==null||m==null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to Sql popup menu.
     */
    public void addSeparatorOnSql() {
    	BasePopupMenu pop = getSqlMenu();
    	pop.addSeparator();
    }
    /**
     * add a menuitem/menu to table popup menu,this method is used by else projects that will extend this system.
     * @param m --menu added to table popup menu.
     */
    public JMenuItem addMenuOnTable(JMenuItem m)
    {
    	BasePopupMenu pop = getTableMenu();
    	if(pop == null || m == null)
    		return null;
    	
    	return pop.add(m);
    }
    /**
     * Add separator to Sql popup menu.
     */
    public void addSeparatorOnTable() {
    	BasePopupMenu pop = getTableMenu();
    	pop.addSeparator();
    }
    /**
     * @return 返回 pop。
     */
    protected BasePopupMenu getBookMarkMenu() {
        if (bookMarkMenu == null)
            createBookMarkMenu();
        return bookMarkMenu;
    }

    /**
     * @return 返回 pubMenu。
     */
    protected BasePopupMenu getPubMenu() {
        if (pubMenu == null)
            createPubMenu();
        return pubMenu;
    }

    /**
     * @return 返回 sqlMenu。
     */
    protected BasePopupMenu getSqlMenu() {
        if (sqlMenu == null)
            createSqlMenu();
        return sqlMenu;
    }

    /**
     * @return 返回 sqlMenu。
     */
    protected BasePopupMenu getTableMenu() {
        if (tableMenu == null)
            createTableMenu();
        return tableMenu;
    }

    /*
     * （非 Javadoc）
     * 
     * @see src.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {

        return itemCheck();
    }

    /*
     * （非 Javadoc）
     * 
     * @see src.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        BookmarkView tmpView = (BookmarkView) this.getComponent();
        DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode) tmpView
                .getConnectTree().getLastSelectedPathComponent();
        int tmpType = tmpNode == null ? -1 : ((Identifier) tmpNode
                .getUserObject()).getType();
        BasePopupMenu pop = null;
        if (BookMarkPubInfo.isBookmarkNode(tmpType)) {
            pop = getBookMarkMenu();
            Bookmark bookmark = (Bookmark) tmpNode.getUserObject();

            if (bookmark.getConnectState() == 0) //如果书签节点状态正常
            {
                if (bookmark.isConnected()) {
                    if (connect.isEnabled()) {
                        connect.setEnabled(false);
                    }
                    if (!disconnect.isEnabled()) {
                        disconnect.setEnabled(true);
                    }
                } else {
                    if (!connect.isEnabled()) {
                        connect.setEnabled(true);
                    }
                    if (disconnect.isEnabled()) {
                        disconnect.setEnabled(false);
                    }
                }

                //设置刷新菜单是否可用
                if (bookmark.isConnected()) {
                    if (!refresh.isEnabled()) {
                        refresh.setEnabled(true);
                    }
                } else {
                    if (refresh.isEnabled()) {
                        refresh.setEnabled(false);
                    }
                }
            } else { //状态不正常 ，可能正在连接，可能正在断开
                if (connect.isEnabled()) {
                    connect.setEnabled(false);
                }
                if (disconnect.isEnabled()) {
                    disconnect.setEnabled(false);
                }
                if (refresh.isEnabled()) {
                    refresh.setEnabled(false);
                }
            }
        } else if (tmpType == BookMarkPubInfo.NODE_RECENTSQL) { //最近执行的sql列表
            pop = getSqlMenu();
            if (!copyForPub.isEnabled())
                copyForSql.setEnabled(true);

            if (refreshForPub.isEnabled())
                refreshForPub.setEnabled(false);
            //				getSqlMenu();
        } else if (tmpType == BookMarkPubInfo.NODE_SCHEMA //模式节点 、列节点 、主键节点
                || tmpType == BookMarkPubInfo.NODE_COLUMN
                || tmpType == BookMarkPubInfo.NODE_KEYCOLUMN) {
            pop = getPubMenu();
            if (!copyForPub.isEnabled())
                copyForPub.setEnabled(true);

            if (tmpType == BookMarkPubInfo.NODE_SCHEMA) { //如果是模式节点，可刷新
                if (!refreshForPub.isEnabled())
                    refreshForPub.setEnabled(true);
                if (propertyForPub.isEnabled())
                    propertyForPub.setEnabled(false);
            } else { //如果是列节点，不可刷新
                if (refreshForPub.isEnabled())
                    refreshForPub.setEnabled(false);
                if (!propertyForPub.isEnabled())
                    propertyForPub.setEnabled(true);
            }
        } else if (tmpType == BookMarkPubInfo.NODE_VIEW //视图节点 、表节点
                || tmpType == BookMarkPubInfo.NODE_TABLE) {
            pop = getTableMenu();
        } else {
            pop = getPubMenu();
            if (tmpType != -1) {
                if (!copyForPub.isEnabled())
                    copyForPub.setEnabled(true);
                if (tmpType == BookMarkPubInfo.NODE_VIEWS //对于组合节点（views,tables）,使刷新可用
                        || tmpType == BookMarkPubInfo.NODE_TABLES) {
                    if (!refreshForPub.isEnabled())
                        refreshForPub.setEnabled(true);
                } else if (tmpType == BookMarkPubInfo.NODE_HEADER) {
                    if (refreshForPub.isEnabled())
                        refreshForPub.setEnabled(false);
                }

            } else //没有选中任何节点
            {
                if (copyForPub.isEnabled())
                    copyForPub.setEnabled(false);
                if (refreshForPub.isEnabled())
                    refreshForPub.setEnabled(false);
            }
            if (propertyForPub.isEnabled())
                propertyForPub.setEnabled(false);
        }
        
        menuCheck();
        return pop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#createPopMenu()
     */
    protected void createPopMenu() {

    }

}
