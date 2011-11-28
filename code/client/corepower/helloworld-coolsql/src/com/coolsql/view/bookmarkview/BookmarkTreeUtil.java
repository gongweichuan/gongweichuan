/*
 * 创建日期 2006-8-18
 *
 */
package com.coolsql.view.bookmarkview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.selectabletree.SelectableTreeNode;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.ISQLDatabaseMetaData;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.BookmarkView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.bookmarkview.model.SQLGroupNode;

/**
 * @author liu_xlin 数签视图中数签树的操作管理类
 */
public class BookmarkTreeUtil {
	private static BookmarkTreeUtil util=null;
	
	private BookmarkView view = null;

	private NodeContentListener contentSort = null;

	private BookmarkChangedListener pcl = null;

	public static BookmarkTreeUtil getInstance()
	{
		if(util==null)
		{
			util=new BookmarkTreeUtil();
		}
		return util;
	}
	/**
	 * To validate a tree node with specified types.
	 * @return return true if the type of tree node is in types, otherwise return false;
	 */
	public static boolean validateNodeType(DefaultTreeNode treeNode,int[] types)
	{
		if(treeNode==null)
			return false;
		Object ob=treeNode.getUserObject();
		if(ob instanceof Identifier)
		{
			Identifier id=(Identifier)ob;
			for(int i=0;i<types.length;i++)
			{
				if(id.getType()==types[i])
					return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Expand all node of specified tree.
	 * @param tree The JTree object.
	 */
	public static void expandAllNodes(JTree tree) {
		if (tree == null) {
			return;
		}
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		
		TreePath path = new TreePath(root);
		
		for (Enumeration<?> en = root.children(); en.hasMoreElements();) {
			TreeNode treeNode = (TreeNode) en.nextElement();
			TreePath tp = path.pathByAddingChild(treeNode);
			setExpand(tp, tree, true);
		}
	}
	
	/**
	 * To expand or collapse specified path and sub path according to the given parameters. 
	 * @param path --treepath object
	 * @param tree --jtree object
	 * @param isExpand if true treeepath will be expanded,otherwise it will be collapsed.
	 */
	public static void setExpand(TreePath path, JTree tree, boolean isExpand) {
		TreeNode node = (TreeNode) path.getLastPathComponent();
		if (node.getChildCount() > 0)
		{
			for (Enumeration<?> en = node.children(); en.hasMoreElements();) {
				TreeNode treeNode = (TreeNode) en.nextElement();
				TreePath tp = path.pathByAddingChild(treeNode);
				setExpand(tp, tree, isExpand);
			}
		}
		if (isExpand)
		{
//			if (!tree.isVisible(path))
//				tree.expandPath(path);
			if (node.getChildCount() > 0) {
				tree.expandPath(path);
			}
		} else if (tree.isVisible(path))
			tree.collapsePath(path);
	}
	
	private BookmarkTreeUtil() {
		this.view = ViewManage.getInstance().getBookmarkView();
		contentSort = new NodeContentListener();
		pcl = new BookmarkChangedListener();
	}
	/**
	 * 刷新书签树的指定节点
	 * @param node
	 */
	public void refreshBookmarkTree(DefaultTreeNode node)
	{
	    DefaultTreeModel model=(DefaultTreeModel)view.getConnectTree().getModel();
	    model.nodeStructureChanged(node);
	}
	/**
	 * 删除书签对象
	 * 
	 * @param bookmark
	 */
	public void removeBookMarkNode(Bookmark bookmark) {
		bookmark.removePropertyListener(contentSort);
		this.removeBookMarkNode(bookmark.getAliasName());
	}

	/**
	 * 根据书签别名删除书签
	 * 
	 * @param bookmarkName
	 */
	public void removeBookMarkNode(String bookmarkName) {
		int count = view.getRoot().getChildCount();
		for (int i = 0; i < count; i++) {
			DefaultTreeNode node = (DefaultTreeNode) (view.getRoot().getChildAt(i));
			Bookmark bookmark = (Bookmark) node.getUserObject();
			String tmpName = bookmark.getAliasName();
			if (tmpName.equals(bookmarkName)) {
				DefaultTreeModel model = (DefaultTreeModel) view
						.getConnectTree().getModel();
				model.removeNodeFromParent(node);
				bookmark.removePropertyListener(pcl);
				bookmark.removePropertyListener(contentSort);
				return;
			}
		}
	}
	/**
	 * 根据给定的数据对象（child），在父节点（parent）中查找相应的节点，然后删除
	 * 
	 * @param child
	 * @param parent
	 * @return 返回被删除的节点对象
	 */
	public DefaultTreeNode removeNode(Object child, DefaultTreeNode parent) {
		int count = parent.getChildCount();
		for (int i = 0; i < count; i++) {
			DefaultTreeNode node = (DefaultTreeNode) parent.getChildAt(i);
			if (node.getUserObject().equals(child)) // 如果子节点中的数据对象与给定的数据对象一致，删除该子节点
			{
				DefaultTreeModel model = (DefaultTreeModel) view
						.getConnectTree().getModel();
				model.removeNodeFromParent(node);
				return node;
			}
		}
		return null;
	}
	public void removeNode(DefaultTreeNode child, DefaultTreeNode parent) {
		int count = parent.getChildCount();
		for (int i = 0; i < count; i++) {
			DefaultTreeNode node = (DefaultTreeNode) parent.getChildAt(i);
			if (node.equals(child)) {
				DefaultTreeModel model = (DefaultTreeModel) view
						.getConnectTree().getModel();
				model.removeNodeFromParent(node);
				return;
			}
		}
	}
	/**
	 * 删除父节点下指定索引的子节点
	 * 
	 * @param index
	 *            --子节点索引
	 * @param parent
	 *            --父节点
	 */
	public void removeNode(int index, DefaultTreeNode parent) {
		if (index > parent.getChildCount() - 1) {
			return;
		}

		DefaultTreeNode node = (DefaultTreeNode) parent.getChildAt(index);
		DefaultTreeModel model = (DefaultTreeModel) view.getConnectTree()
				.getModel();
		model.removeNodeFromParent(node);
	}
	/**
	 * 添加一个书签节点
	 * 
	 * @param bookmark
	 */
	public void addBookMarkNode(Bookmark bookmark) {
		DefaultTreeNode node = new DefaultTreeNode(bookmark);
		DefaultTreeModel model = (DefaultTreeModel) view.getConnectTree()
				.getModel();
		DefaultTreeNode tmp = (DefaultTreeNode) model.getRoot();
		int location = getInsertLocation(bookmark, tmp);
		model.insertNodeInto(node, tmp, location);
		// 添加两个属性监听
		bookmark.addPropertyListener(contentSort);
		bookmark.addPropertyListener(pcl);
	}
	/**
	 * 在父节点的尾部添加子节点
	 * 
	 * @param child
	 * @param parent
	 * @param index
	 *            --子节点被添加的位置，如果index小于0，则将子节点添加到父节点的末尾
	 */
	public void addNode(DefaultTreeNode child, DefaultTreeNode parent, int index) {
		DefaultTreeModel model = (DefaultTreeModel) view.getConnectTree()
				.getModel();
		model.insertNodeInto(child, parent, index > -1 ? index : parent
				.getChildCount());
	}
	/**
	 * 按数签名称排序，获取合适的位置
	 * 
	 * @param id
	 * @param parent
	 * @return
	 */
	public int getInsertLocation(Identifier id, DefaultTreeNode parent) {
		int count = parent.getChildCount();
		for (int i = 0; i < count; i++) {
			Identifier tmp = (Identifier) ((DefaultTreeNode) parent.getChildAt(i))
					.getUserObject();
			if (id.compareTo(tmp) <= 0) {
				return i;
			}
		}
		return count;
	}

	/**
	 * 根据书签别名获取节点对象
	 * 
	 * @param aliasName
	 * @return
	 */
	public DefaultTreeNode getBookMarkNodeByAlias(String aliasName) {
		int count = view.getRoot().getChildCount();
		for (int i = 0; i < count; i++) {
			DefaultTreeNode node = (DefaultTreeNode) view.getRoot().getChildAt(i);
			String tmpName = ((Bookmark) node.getUserObject()).getAliasName();
			if (tmpName.equals(aliasName)) {
				return node;
			}
		}
		return null;
	}
	/**
	 * 获取指定书签节点下的sql组合节点
	 * 
	 * @param aliasName
	 * @return --如果找不到sql组合节点，返回null值
	 */
	public DefaultTreeNode getSQLGroupNode(String aliasName) {
		DefaultTreeNode bookmarkNode = getBookMarkNodeByAlias(aliasName);
		int count = bookmarkNode.getChildCount();
		for (int i = 0; i < count; i++) {
			DefaultTreeNode node = (DefaultTreeNode) bookmarkNode.getChildAt(i);
			if (node.getUserObject() instanceof SQLGroupNode) // 如果节点数据为sql组合节点，返回
			{
				return node;
			}
		}
		return null;
	}
	public DefaultTreeNode getSQLGroupNode(Bookmark bookmark) {
		if (bookmark == null)
			return null;
		return this.getSQLGroupNode(bookmark.getAliasName());
	}
	/**
	 * 获取别名所对应书签对象的位置
	 * 
	 * @param aliasName
	 * @return
	 */
	public int getBookMarkIndex(String aliasName) {
		int count = view.getRoot().getChildCount();
		for (int i = 0; i < count; i++) {
			DefaultTreeNode node = (DefaultTreeNode) view.getRoot().getChildAt(i);
			String tmpName = ((Bookmark) node.getUserObject()).getAliasName();
			if (tmpName.equals(aliasName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 对指定别名的节点进行位置排序
	 * 
	 * @param aliasName
	 */
	public void sortNode(String aliasName) {
		DefaultTreeNode node = getBookMarkNodeByAlias(aliasName);
		DefaultTreeModel model = (DefaultTreeModel) view.getConnectTree()
				.getModel();
		model.removeNodeFromParent(node);
		int location = getInsertLocation((Identifier) node.getUserObject(),
				view.getRoot());
		model.insertNodeInto(node, view.getRoot(), location);
	}

	/**
	 * @return 返回 view。
	 */
	public BookmarkView getView() {
		return view;
	}

	/**
	 * @param view
	 *            要设置的 view。
	 */
	public void setView(BookmarkView view) {
		this.view = view;
	}

	/**
	 * 删除给定树节点（node）下所有的子节点
	 * 
	 * @param node
	 *            --被给定删除子节点的父节点
	 */
	public static void removeChildrenOfNode(DefaultTreeNode node) {
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultTreeNode tmp = (DefaultTreeNode) node.getChildAt(i);
			removeChildrenOfNode(tmp);

		}
		node.removeAllChildren();
	}

	/**
	 * 合并所有的节点
	 * 
	 */
	public void collapseAllNodes() {
		TreeNode root = (TreeNode) view.getConnectTree().getModel().getRoot();
		TreePath path = new TreePath(root);

		for (Enumeration<?> en = root.children(); en.hasMoreElements();) {
			TreeNode treeNode = (TreeNode) en.nextElement();
			TreePath tp = path.pathByAddingChild(treeNode);
			setExpand(tp, view.getConnectTree(), false);
		}

	}
	/**
	 * return the root node of tree
	 */
	public DefaultMutableTreeNode getRootNode()
	{
		return (DefaultMutableTreeNode) view
		.getConnectTree().getModel().getRoot();
	}
	/**
	 * 该方法用于根据指定的节点标示对象选中相应的节点 1、获取根节点后，获取对应的书签 2、获取模式节点
	 * 3、获取实体组合节点（根据节点所属类型，判断选择） 4、获取实体节点 5、如果是列节点，继续搜寻，然后选中对应的节点
	 * 
	 * @param id
	 *            --查询与id相对应的节点
	 * @param isExpand --node according to id should be expand or not.
	 * @throws UnifyException
	 *             --如果找不到对应的节点，抛出该异常进行提示
	 */
	public void selectNode(Identifier id, boolean isExpand)
			throws UnifyException {
		Bookmark bookmark = id.getBookmark();
		if (bookmark == null)
			return;

		/**
		 * 获取实体对象
		 */
		Entity entity = null;
		if (id.getDataObject() instanceof Entity) // 如果查询节点为实体对象
		{
			entity = (Entity) id.getDataObject();
		} else {
			Column column = (Column) id.getDataObject();
			entity = column.getParentEntity();
		}
		String catalog = entity.getCatalog(); // catalog name
		String schema = entity.getSchema(); // 模式名
		DefaultMutableTreeNode root = getRootNode();
		TreePath path = new TreePath(root);

		DefaultMutableTreeNode tmp = getNode(root, bookmark.getAliasName()); // 获取对应的书签节点
		if (tmp == null)
			throw new UnifyException(PublicResource
					.getString("bookmarkView.selectnode.nobookmark"));
		path = path.pathByAddingChild(tmp);
		expandNode(path);

		ISQLDatabaseMetaData metaData = bookmark.getDbInfoProvider()
				.getDatabaseMetaData();
		/**
		 * if current database supports catalog,it will find catalog node by catalog name.
		 */
		boolean isSupportCatalog;
		try {
			isSupportCatalog = metaData.supportsCatalogs();
		} catch (SQLException e) {
			throw new UnifyException(e);
		}
		if (isSupportCatalog) {
			tmp = getNode(tmp, catalog);
			if (tmp == null)
				throw new UnifyException(PublicResource
						.getString("bookmarkView.selectnode.nocatalog"));
			path = path.pathByAddingChild(tmp);
			expandNode(path);
		}
		/**
		 * 获取模式节点
		 */
		boolean isSupportSchema;
		try {
			isSupportSchema = metaData.supportsSchemas();
		} catch (SQLException e) {
			throw new UnifyException(e);
		}
		if (isSupportSchema) {
			tmp = getNode(tmp, schema);
			if (tmp == null)
				throw new UnifyException(PublicResource
						.getString("bookmarkView.selectnode.noschema"));
			path = path.pathByAddingChild(tmp);
			expandNode(path);
		}
		/**
		 * 获取实体节点
		 */
		if (entity.getType().equals("TABLE")) {
			tmp = getNode(tmp, Identifier.groupTable); // 获取表组合节点
		} else if (entity.getType().equals("VIEW")) {
			tmp = getNode(tmp, Identifier.groupView); // 获取视图组合节点
		} else
			tmp=getNode(tmp,entity.getType());
//			throw new UnifyException("can't support entity type:"
//					+ entity.getType());

		if (tmp == null)
			throw new UnifyException("can't support entity type:"+ entity.getType());
		
		path = path.pathByAddingChild(tmp);
		expandNode(path);
		tmp = getNode(tmp, entity.getName()); // 获取实体节点
		if (tmp == null) {
			String msg = "";
			if (entity.getType().equals("TABLE"))
				msg = PublicResource
						.getString("bookmarkView.selectnode.notable");
			else
				msg = PublicResource
						.getString("bookmarkView.selectnode.noview");
			throw new UnifyException(msg);
		}
		path = path.pathByAddingChild(tmp);

		if (id.getDataObject() instanceof Entity) // 如果查询节点为实体对象,直接选中后返回
		{
			if (isExpand)
				expandNode(path);
			view.getConnectTree().setSelectionPath(path); // 选中指定的节点
			view.getConnectTree().scrollPathToVisible(path);
			return;
		} else
			expandNode(path);

		/**
		 * 列信息搜寻
		 */
		Column column = (Column) id.getDataObject();
		tmp = getNode(tmp, column.getName()); // 获取列节点
		if (tmp == null)
			throw new UnifyException(PublicResource
					.getString("bookmarkView.selectnode.nocolumn"));
		path = path.pathByAddingChild(tmp);
		view.getConnectTree().setSelectionPath(path); // 选中指定的节点
		view.getConnectTree().scrollPathToVisible(path);
	}
	/**
	 * find all treepath of node according to id, items should be matched with node include type and name.
	 */
	public List<TreePath> findTreePathsMatchedWithName(Identifier id)
	{
		List<TreePath> list=new ArrayList<TreePath>();
		Bookmark bookmark = id.getBookmark();
		if (bookmark == null)
			return list;
		
		/**first step:build the treepath between root node and bookmark node */
		
		DefaultTreeNode bookmarkNode=getBookMarkNodeByAlias(bookmark.getAliasName());
		TreePath commonPath= new TreePath(getRootNode()).pathByAddingChild(bookmarkNode);
		list.add(commonPath);
		if(BookMarkPubInfo.isBookmarkNode(id.getType()))
		{
			return list;	
		}

		List<DefaultMutableTreeNode> childNodes=getChildMatchedWithName(bookmarkNode, id);
		for(int i=0;i<childNodes.size();i++)
		{
			DefaultMutableTreeNode tNode=childNodes.get(i);
			list.add(new TreePath(tNode.getPath()));
		}
		return list;
	}
	/**
	 * 展开节点
	 * 
	 * @param path
	 * @return 如果该路径已经展开，返回false，未展开，返回true
	 */
	private boolean expandNode(TreePath path) {
		DefaultTreeNode node = (DefaultTreeNode) path.getLastPathComponent();
		node.expand();
		if (view.getConnectTree().isCollapsed(path)) {
			view.getConnectTree().expandPath(path);
			return true;
		} else
			return false;
	}
	/**
	 * 根据给定的条件，查找指定节点的子节点
	 * 
	 * @param parent
	 * @param key
	 * @return
	 */
	private DefaultMutableTreeNode getNode(DefaultMutableTreeNode parent,
			String key) {
		if (key == null)
			return null;

		DefaultMutableTreeNode tmp = null;
		Enumeration<?> en = parent.children(); // 获取书签节点集合
		while (en.hasMoreElements()) {
			tmp = (DefaultMutableTreeNode) en.nextElement();
			Object ob = tmp.getUserObject();
			if (ob == null)
				continue;

			Identifier id = (Identifier) ob;
			if (key.equals(id.getContent())) // 如果所属书签一致
			{
				return tmp;
			}
		}
		return null; // 如果为找不到对应的节点，返回null
	}
	/**
	 * get user data array included in tree node selected currently
	 *
	 */
	public List<Identifier> getCurrentSelectedIdentifiers()
	{
		TreePath[] paths = view.getConnectTree().getSelectionPaths();
		List<Identifier> list = new ArrayList<Identifier>();
		if (paths != null)
		{
			for (int i = 0; i < paths.length; ++i)
			{
				Object obj = paths[i].getLastPathComponent();
				if (obj instanceof DefaultTreeNode)
				{
					obj=((DefaultTreeNode)obj).getUserObject();
					if(obj instanceof Identifier)
						list.add((Identifier)obj);
				}
			}
		}
		return list;
	}
	public DefaultTreeNode getLastSelectedPathComponent()
	{
		DefaultTreeNode node = (DefaultTreeNode) (view
				.getConnectTree().getLastSelectedPathComponent());
		return node;
	}
	/**
	 * repaint tree component.
	 * 
	 */
	public void repaintTree() {
		view.getConnectTree().repaint();
	}
	public SelectableTreeNode generateSelectableTreeNodes(DefaultTreeNode originalNode,INodeFilter filter)
	{
		if(originalNode==null)
			return null;
		Identifier id=(Identifier)originalNode.getUserObject();
		if(!isSelectableNode(id.getType()))
			return null;
		SelectableTreeNode rootNode;
		if(id.getType()==BookMarkPubInfo.NODE_TABLE)  //return directly if originalNode is a table node
		{
			rootNode=new SelectableTreeNode(id,false);
			rootNode.setNodeFilter(filter);
			return rootNode;
		}
		else
			rootNode=new SelectableTreeNode(id);
		
		rootNode.setExpanded(originalNode.isExpanded());
		int count=originalNode.getChildCount();
		for(int i=0;i<count;i++)
		{
			SelectableTreeNode childrenNode=generateSelectableTreeNodes((DefaultTreeNode)originalNode.getChildAt(i),filter);
			if(childrenNode==null)
				continue;
			rootNode.add(childrenNode);
		}
		return rootNode;
	}
	/**
	 * get all selected nodes which can match with matcher.
	 * @param tree --Jtree object
	 * @param matcher 
	 * @return
	 */
	public List<?> getSelectedSuitableNodes(JTree tree,ISelectableNodeMatch matcher)
	{
		if(tree==null)
			return null;
		
		Object root=tree.getModel().getRoot();
		if(root instanceof SelectableTreeNode)
		{
			return findSelectedNodes((SelectableTreeNode)root,matcher);
		}else
			return null;
	}
	@SuppressWarnings("unchecked")
	private List<?> findSelectedNodes(SelectableTreeNode node,ISelectableNodeMatch matcher)
	{
		if(node==null)
			return null;
		List<Object> list=new ArrayList<Object>();
		Object result=matcher.match(node);
		if(result!=null) {
			if (result instanceof Collection) {
				list.addAll((Collection)result);
			} else {
				list.add(result);
			}
		}
		for(int i=0;i<node.getChildCount();i++)
		{
			if(node instanceof SelectableTreeNode)
			{
				List<?> tmp=findSelectedNodes((SelectableTreeNode)node.getChildAt(i),matcher);
				if(tmp!=null)
					list.addAll(tmp);
			}
		}
		return list;
	}
	/**
	 * validate whether the node according to type should be add into select tree .
	 * @param type --node type 
	 */
	public boolean isSelectableNode(int type)
	{
		if(type==BookMarkPubInfo.NODE_HEADER||
				type==BookMarkPubInfo.NODE_RECENTSQLGROUP||
				type==BookMarkPubInfo.NODE_RECENTSQL||
				type==BookMarkPubInfo.NODE_COLUMN||
				type==BookMarkPubInfo.NODE_KEYCOLUMN||
				type==BookMarkPubInfo.NODE_VIEWS||
				type==BookMarkPubInfo.NODE_VIEW||
				type==BookMarkPubInfo.NODE_UNDEFINED)
			return false;
		else
			return true;
	}
	/**
	 * get all children of parent node according to id . 
	 * the act of finding will be performed recursively .
	 */
	public List<DefaultMutableTreeNode> getChildMatchedWithName(DefaultMutableTreeNode parent,Identifier id)
	{
		List<DefaultMutableTreeNode> list=new ArrayList<DefaultMutableTreeNode>();
		if(parent==null||id==null)
			return list;
		Object parentUserObj=parent.getUserObject();
		if(parentUserObj instanceof Identifier)
		{
			Identifier pid=(Identifier)parentUserObj;
			if(pid.getType()==id.getType())  //return an empty list directly if typeid equals the type of parent
			{
				return list;
			}
			int childCount=parent.getChildCount();
			for(int i=0;i<childCount;i++)
			{
				TreeNode tn=parent.getChildAt(i);
				if(tn instanceof DefaultMutableTreeNode)
				{
					DefaultMutableTreeNode dtn=(DefaultMutableTreeNode)tn;
					Object dtnUserObj=dtn.getUserObject();
					if(dtnUserObj instanceof Identifier)
					{
						Identifier dtnId=(Identifier)dtnUserObj;
						if(id.getType()==dtnId.getType())
						{
							String st=id.getContent();
							if(st==null)
							{
								if(dtnId.getContent()!=null)
								{
									continue;
								}
							}else
							{
								if(!st.equals(dtnId.getContent()))
									continue;
							}
							list.add(dtn);
						}else
							list.addAll(getChildMatchedWithName(dtn,id));  //find child nodes recursively
					}
				}
			}
		}
		return list;
	}
	/**
	 * 
	 * @author liu_xlin 书签别名发生变化的事件监听处理类
	 */
	private class NodeContentListener implements PropertyChangeListener {

		/*
		 * （非 Javadoc）
		 * 
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			String name = evt.getPropertyName();
			if (name.equals("Content")) {
				Identifier source = (Identifier) evt.getSource();
				if (BookMarkPubInfo.isBookmarkNode(source.getType())) {
					sortNode(source.getBookmark().getAliasName());
				}
			}
		}

	}
}
