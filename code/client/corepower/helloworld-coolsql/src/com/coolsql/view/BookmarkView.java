/*
 * 创建日期 2006-5-31
 *
 */
package com.coolsql.view;

import java.util.Iterator;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import com.coolsql.action.bookmarkmenu.AddSqlToEditorAction;
import com.coolsql.action.bookmarkmenu.ConnectAction;
import com.coolsql.action.bookmarkmenu.SearchAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkEvent;
import com.coolsql.bookmarkBean.BookmarkListener;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.bookmarkBean.DefaultBookmarkChangeEvent;
import com.coolsql.bookmarkBean.DefaultBookmarkChangeListener;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.DoubleClickListener;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.system.Setting;
import com.coolsql.system.menu.action.NewBookmarkMenuAction;
import com.coolsql.view.bookmarkview.BookMarkMenuManage;
import com.coolsql.view.bookmarkview.BookmarkTree;
import com.coolsql.view.bookmarkview.BookmarkTreeRender;
import com.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.coolsql.view.bookmarkview.actionOfIconButton.CollapseNodesAction;
import com.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.bookmarkview.model.RootNode;
import com.coolsql.view.mouseEventProcess.PopupAction;
import com.coolsql.view.sqleditor.action.NextDefaultBookmarkAction;
import com.coolsql.view.sqleditor.action.PreviousDefaultBookmarkAction;

/**
 * @author liu_xlin 数据库连接信息的展示面板
 */
public class BookmarkView extends View implements BookmarkListener {
	private static final long serialVersionUID = -8019702055986761450L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(BookmarkView.class);
	
	private JTree connectTree = null;

    private DefaultTreeNode root = null;

    private BaseMenuManage menuManage = null;

    public BookmarkView() {
        super();
        root = new DefaultTreeNode(new RootNode(PublicResource.getString("bookmark.rootName"), null));
        root.setAllowsChildren(true);
        init();
    }

    public BookmarkView(DefaultTreeNode info) {
        super();
        if (info == null) {
            root = new DefaultTreeNode(new RootNode(PublicResource.getString("bookmark.rootName"), null));
            root.setAllowsChildren(true);
        } else {
            root = info;
        }
        init();
    }

    public BookmarkView(DefaultTreeNode connectNode[]) {
        super();
        root = new DefaultTreeNode(new RootNode(PublicResource.getString("bookmark.rootName"), null, true));
        root.setAllowsChildren(true);
        if (connectNode != null) {
            for (int i = 0; i < connectNode.length; i++) {
                root.add(connectNode[i]);
            }
        }
        init();
    }

    private void init() {
        connectTree = new BookmarkTree(root);
        connectTree.setCellRenderer(new BookmarkTreeRender());
        connectTree.setDragEnabled(false);
        connectTree.setShowsRootHandles(true);

        /**
         *当双击书签树时，执行的动作
         */
        connectTree.addMouseListener(new DoubleClickListener(new AddSqlToEditorAction(this)));
        connectTree.addMouseListener(new DoubleClickListener(new ConnectAction(this)));
        connectTree.addMouseListener(new PopupAction(this));
        DefaultTreeModel model = (DefaultTreeModel) connectTree.getModel();
        model.addTreeModelListener(new NodeStrutListener()); //添加树模型监听
        DefaultTreeSelectionModel selectModel = (DefaultTreeSelectionModel) connectTree
                .getSelectionModel();
        selectModel
                .setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION); //设置只允许选择单行
        loadBookmarksInfo();
        menuManage = new BookMarkMenuManage(this);
        this.setContent(new JScrollPane(connectTree));

        addIconButtons();
        
        BookmarkManage.getInstance().addDefaultBookmarkListener(new DefaultBookmarkChangeListener()
        {

			public void defaultChanged(DefaultBookmarkChangeEvent e) {
				connectTree.repaint();
			}
        	
        }
        );
    }
    /**
     * 添加图标按钮
     *
     */
    protected void addIconButtons()
    {
//        this.addIconButton(PublicResource
//                .getIcon("bookmarkView.popup.new.icon"),
//                getAction("newbookmark"), PublicResource
//                        .getString("bookmarkView.iconbutton.newbookmark.tooltip")); //新建书签
    	
    	addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(NewBookmarkMenuAction.class).getToolbarButton());
    	addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(PreviousDefaultBookmarkAction.class).getToolbarButton());
    	addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(NextDefaultBookmarkAction.class).getToolbarButton());
    	
        this.addIconButton(PublicResource
                .getIcon("bookmarkView.iconbutton.collapse.icon"),
                new CollapseNodesAction(this), PublicResource
                        .getString("bookmarkView.iconbutton.collapse.tooltip")); //添加合并按钮
        addIconButton(Setting.getInstance().getShortcutManager().
    			getActionByClass(SearchAction.class).getToolbarButton());
    }
    /**
     * 设置书签管理器
     * 
     * @param bookMarkManage
     */
    protected void loadBookmarksInfo() {
    	BookmarkManage bookMarkManage = BookmarkManage.getInstance();
        bookMarkManage.addBookmarkListener(this);
        Set<String> set = bookMarkManage.getAliases();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            BookmarkTreeUtil.getInstance().addBookMarkNode((Bookmark) bookMarkManage.get(name));
        }
    }

    public BaseMenuManage getBookmarkMenuManage()
    {
    	return menuManage;
    }
    /**
     * @return 返回 connectTree。
     */
    public JTree getConnectTree() {
        return connectTree;
    }
    /**
     * @return 返回 root。
     */
    public DefaultTreeNode getRoot() {
        return root;
    }

    /**
     * @param root
     *            要设置的 root。
     */
    public void setRoot(DefaultTreeNode root) {
        this.root = root;
    }

    public String getName() {
        return stringMgr.getString("view.bookmark.title");
    }

    /*
     * （非 Javadoc）
     * 
     * @see src.view.Display#dispayInfo()
     */
    public void dispayInfo() {

    }

    /*
     * （非 Javadoc）
     * 
     * @see src.view.Display#popupMenu()
     */
    public void popupMenu(int x, int y) {
        menuManage.getPopMenu().show(connectTree, x, y);
    }

    /*
     * （非 Javadoc）
     * 
     * @see src.bookmark.BookMarkListener#bookMarkAdded(src.bookmark.BookMarkEvent)
     */
    public void bookmarkAdded(BookmarkEvent e) {
        BookmarkTreeUtil.getInstance().addBookMarkNode(e.getBookmark());

    }

    /*
     * （非 Javadoc）
     * 
     * @see src.bookmark.BookMarkListener#bookMarkDeleted(src.bookmark.BookMarkEvent)
     */
    public void bookmarkDeleted(BookmarkEvent e) {
        BookmarkTreeUtil.getInstance().removeBookMarkNode(e.getBookmark());
    }

    /*
     * （非 Javadoc）
     * 
     * @see src.bookmark.BookMarkListener#bookMarkUpdated(src.bookmark.BookMarkEvent)
     */
    public void bookMarkUpdated(BookmarkEvent e) {

    }
    /**
     * 
     * @author liu_xlin 节点结构发生变化时的监听处理
     */
    protected class NodeStrutListener implements TreeModelListener {

        /*
         * （非 Javadoc）
         * 
         * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesChanged(TreeModelEvent e) {

        }

        /*
         * （非 Javadoc）
         * 
         * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesInserted(TreeModelEvent e) {

        }

        /*
         * （非 Javadoc）
         * 
         * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
         */
        public void treeNodesRemoved(TreeModelEvent e) {

        }

        /**
         * 树节点结构发生变化时，该方法进行节点是否有子节点的状态更新
         * 
         * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
         */
        public void treeStructureChanged(TreeModelEvent e) {
            TreePath path = e.getTreePath();
            DefaultTreeNode node = (DefaultTreeNode) path.getLastPathComponent();
            Object ob = node.getUserObject();
            if (ob instanceof Identifier) {
                Identifier id = (Identifier) ob;
                if (node.getChildCount() > 0) {
                    if (!id.isHasChildren())
                        id.setHasChildren(true);
                } else {
                    if (id.isHasChildren())
                        id.setHasChildren(false);
                }
            }
        }

    }
    /* 1、新建书签:newbookmark
     * @see com.coolsql.view.View#createActions()
     */
    @Override
    protected void createActions() {
       //新建书签
//       actionsMap.put("newbookmark",new NewBookMarkAction(this));
        
    }

	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		
	}
}
