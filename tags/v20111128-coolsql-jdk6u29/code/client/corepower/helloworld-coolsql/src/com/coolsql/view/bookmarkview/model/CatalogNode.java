/**
 * 
 */
package com.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.Database;
import com.coolsql.sql.ISQLDatabaseMetaData;
import com.coolsql.sql.model.Catalog;
import com.coolsql.sql.model.Schema;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.coolsql.view.bookmarkview.INodeFilter;

/**
 * @author 刘孝林
 * 
 * 2008-1-1 create
 */
public class CatalogNode extends Identifier {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类对象
	 */
	private Catalog catalog = null;
	public CatalogNode(String content, Bookmark bookmark) {
		super(BookMarkPubInfo.NODE_CATALOG, content, bookmark);
		catalog=new Catalog(content);
	}
	public CatalogNode(String content, Bookmark bookmark, boolean isHasChild) {
		super(BookMarkPubInfo.NODE_CATALOG, content, bookmark, isHasChild);
		catalog=new Catalog(content);
	}
	public CatalogNode(String content, Bookmark bookmark, Catalog catalog) {
		super(BookMarkPubInfo.NODE_CATALOG, content, bookmark);
		this.catalog = catalog;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
	 */
	public void expand(DefaultTreeNode parent,INodeFilter filter) throws SQLException, UnifyException {
		Bookmark bookmark = getBookmark();
		Database db = bookmark.getDbInfoProvider();
		ISQLDatabaseMetaData dbmd = db.getDatabaseMetaData();

		List<Identifier> list=new ArrayList<Identifier>();
		if (dbmd.supportsSchemas()) {
			Schema[] schemas = db.getSchemas(getContent()); // 获取数据库的所有模式名
			/**
			 * 创建节点内容标示对象
			 */
			for (int i = 0; i < schemas.length; i++) {
				String schemaName = schemas[i].getName();
				SchemaNode node= new SchemaNode(schemaName, bookmark, schemas[i]);
				if(filter==null||filter.filter(node))
					list.add(node);
			}
			
		} else {  //不支持模式(schema)
			String[] types=bookmark.getDbInfoProvider().getDatabaseMetaData().getTableTypes();
			for (int i = 0; i < types.length; i++) {
				Identifier id = null;
				EntityGroup eg = new EntityGroup(new Catalog(catalog.getName(), catalog.getName()), "");
				if (types[i].equals(TYPE_VIEW)) {
					eg.setType(BookMarkPubInfo.NODE_VIEWS);
					id = new ViewGroupNode(groupView, getBookmark(), eg);
				} else if (types[i].equals(TYPE_TABLE)) {
					eg.setType(BookMarkPubInfo.NODE_TABLES);
					id = new TableGroupNode(groupTable, getBookmark(), eg);
				} else {
					eg.setType(BookMarkPubInfo.NODE_SYSTEM_TABLES);
					id = new TableTypeNode(types[i], getBookmark(), eg);
				}
				if(filter==null||filter.filter(id))
					list.add(id);

			}
		}
		parent.addChildren(list.toArray(new Identifier[list.size()]));
		
	}
	public void refresh(DefaultTreeNode node,INodeFilter filter) throws SQLException, UnifyException {
		if(!node.isExpanded())
            return ;
		
		boolean changed = false; // 是否有变化
		HashMap<Object,Object> temp = new HashMap<Object,Object>();
		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			DefaultTreeNode tmp = (DefaultTreeNode) node.getChildAt(i);
			if (tmp.getUserObject() instanceof SchemaNode) // 如果该节点为模式节点，将最近执行sql组合节点除外
				temp.put(((Identifier) tmp.getUserObject()).getContent(), tmp);
		}

		Bookmark bookmark = getBookmark();
		Database db = bookmark.getDbInfoProvider();
		ISQLDatabaseMetaData dbmd = db.getDatabaseMetaData();

		if (dbmd.supportsSchemas()) {  //如果数据库支持模式，对模式进行刷新
			Schema[] schemas = db.getSchemas(null); // 获取数据库的所有模式名
			for (int i = 0; i < schemas.length; i++) {
				DefaultTreeNode tmp = (DefaultTreeNode) (temp.remove(schemas[i].getName()));
				if (tmp == null)// 该模式为新增的
				{
					String s = schemas[i].getName();
					SchemaNode sh = new SchemaNode(s, getBookmark(),schemas[i]);
					if(filter!=null&&!filter.filter(sh))
	                	continue;
					
					node.addChild(sh, 1); // 过滤掉recentsql节点
					changed = true;
				}else
				{
					if(filter!=null&&!filter.filter(tmp.getUserObject()))//The node will be removed
					{
						temp.put(schemas[i].getName(), tmp);
					}
				}
			}

			for (Iterator<Object> it = temp.values().iterator(); it.hasNext();) // 删除不存在的模式
			{
				node.remove((DefaultTreeNode) it.next());
				changed = true;
			}
		}else //如果不支持模式，那么其子节点为实体类型节点
		{
	        for(int i=0;i<node.getChildCount();i++)  //递归刷新
	        {
	            DefaultTreeNode tmpNode=(DefaultTreeNode)node.getChildAt(i);
	            Identifier id=(Identifier)tmpNode.getUserObject();
	            id.refresh(tmpNode,tmpNode.getNodeFilter());
	        }
		}
		if (changed)
			BookmarkTreeUtil.getInstance().refreshBookmarkTree(node); // 刷新节点树模型

		for (int i = 0; i < node.getChildCount(); i++) // 递归刷新
		{
			DefaultTreeNode tmpNode = (DefaultTreeNode) node.getChildAt(i);
			Identifier id = (Identifier) tmpNode.getUserObject();
			id.refresh(tmpNode,tmpNode.getNodeFilter());
		}
	}
	/**
	 * 重写ObjectHolder的方法
	 */
	public Object getDataObject() {
		return catalog;
	}

}
