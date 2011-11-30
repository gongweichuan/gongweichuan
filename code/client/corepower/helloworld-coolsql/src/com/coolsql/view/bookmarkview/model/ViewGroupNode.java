/*
 * 创建日期 2006-9-8
 */
package com.coolsql.view.bookmarkview.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.model.Entity;
import com.coolsql.sql.model.IDatabaseMode;
import com.coolsql.sql.model.Schema;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.coolsql.view.bookmarkview.INodeFilter;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 视图组合节点
 */
public class ViewGroupNode extends Identifier {
	
	private static final long serialVersionUID = 1L;
	/**
     * 所属分类/模式
     */
    private IDatabaseMode dm;

    public ViewGroupNode() {
        super();
        super.setType(BookMarkPubInfo.NODE_VIEWS);
    }

    public ViewGroupNode(String content, Bookmark bookmark) {
        super(BookMarkPubInfo.NODE_VIEWS, content, bookmark);
    }

    public ViewGroupNode(String content, Bookmark bookmark, boolean isHasChild) {
        super(BookMarkPubInfo.NODE_VIEWS, content, bookmark, isHasChild);
    }

    public ViewGroupNode(String content, Bookmark bookmark, IDatabaseMode dm) {
        super(BookMarkPubInfo.NODE_VIEWS, content, bookmark);
        this.dm = dm;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent, INodeFilter filter) throws SQLException, UnifyException {
        //获取视图节点
        Entity[] entitys = getEntities();;
        if (entitys == null)
            return;

        //生成节点标识数组
        List<Identifier> list=new ArrayList<Identifier>();
        for (int i = 0; i < entitys.length; i++) {
        	Identifier id = new ViewNode(entitys[i].getName(), getBookmark(),
                    entitys[i]);
        	if(filter==null||filter.filter(id))
        		list.add(id);
        }

        //添加节点
        parent.addChildren(list.toArray(new Identifier[list.size()]));
    }

    /**
     * 节点的刷新处理实现 （非 Javadoc）
     * 
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#refresh(com.coolsql.view.bookmarkview.model.DefaultTreeNode)
     */
    public void refresh(DefaultTreeNode parent, INodeFilter filter) throws SQLException, UnifyException {
        if (!parent.isExpanded())
            return;
        boolean changed = false; //是否有变化
        Map<String, DefaultTreeNode> temp = new HashMap<String, DefaultTreeNode>();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            DefaultTreeNode tmp = (DefaultTreeNode) parent.getChildAt(i);
            temp.put(((Identifier) tmp.getUserObject()).getContent(), tmp);
        }

        //获取视图信息
        Entity[] entitys = getEntities();
        for (int i = 0; i < entitys.length; i++) {
            DefaultTreeNode node = (DefaultTreeNode) temp.remove(entitys[i].getName());
            if (node == null) {
                ViewNode tmpNode = new ViewNode(entitys[i].getName(), this
                        .getBookmark(), entitys[i]);
                if(filter!=null&&!filter.filter(tmpNode))
                	continue;
                
                parent.addChild(tmpNode);
                changed = true;
            }else
            {
            	if(filter!=null&&!filter.filter(node.getUserObject()))//The node will be removed
				{
					temp.put(entitys[i].getName(), node);
				}
            }
        }

        for (Iterator<DefaultTreeNode> it = temp.values().iterator(); it.hasNext();) //删除不存在的模式
        {
            parent.remove((DefaultTreeNode) it.next());
            changed = true;
        }
        if (changed)
        	BookmarkTreeUtil.getInstance().refreshBookmarkTree(parent); //刷新节点树模型

        for (int i = 0; i < parent.getChildCount(); i++) //递归刷新
        {
            DefaultTreeNode tmpNode = (DefaultTreeNode) parent.getChildAt(i);
            Identifier id = (Identifier) tmpNode.getUserObject();
            id.refresh(tmpNode,tmpNode.getNodeFilter());
        }
    }

    /**
     * 重写Identifier的方法
     */
    public Object getDataObject() {
        return dm;
    }
    private Entity[] getEntities() {
    	IDatabaseMode parentObj = ((EntityGroup) dm).getParentObject();
    	Schema schema = null;
    	if (parentObj.getType() == BookMarkPubInfo.NODE_CATALOG) {
    		schema = new Schema(parentObj.getName(), null);
    	} else if (parentObj.getType() == BookMarkPubInfo.NODE_SCHEMA) {
    		schema = (Schema) parentObj;
    	} else {
    		return null;
    	}
    	 Entity[] entities = null;
		try {
			entities = this.getBookmark().getDbInfoProvider().getEntities(
			         getBookmark(), schema, new String[]{"VIEW"});
		} catch (Exception e) {
			LogProxy.errorReport("Retrieving entities failed: " + e.getMessage(), e);
		}
    	 
    	 return entities;
    }
}
