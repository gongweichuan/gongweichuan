/*
 * 创建日期 2006-9-10
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
import com.coolsql.sql.commonoperator.EntityPropertyOperator;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.coolsql.view.bookmarkview.INodeFilter;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 *  
 */
public class TableNode extends Identifier {
	
	private static final long serialVersionUID = 1L;
	//实体数据对象
    private Entity dataOb = null;

    public TableNode() {
        super();
        super.setType(BookMarkPubInfo.NODE_TABLE);
    }

    public TableNode(String content, Bookmark bookmark) {
        super(BookMarkPubInfo.NODE_TABLE, content, bookmark);
    }

    public TableNode(String content, Bookmark bookmark, boolean isHasChild) {
        super(BookMarkPubInfo.NODE_TABLE, content, bookmark, isHasChild);
    }

    public TableNode(String content, Bookmark bookmark, Entity dataOb) {
        super(BookMarkPubInfo.NODE_TABLE, content, bookmark);
        this.dataOb = dataOb;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.view.bookmarkview.model.NodeExpandable#expand(com.coolsql.view.bookmarkview.model.ITreeNode)
     */
    public void expand(DefaultTreeNode parent,INodeFilter filter) throws UnifyException, SQLException {
        //获取所有的列信息
        Column[] cols = dataOb.getColumns();
        if (cols == null)
            return;

        //创建节点标识
        List<Identifier> list=new ArrayList<Identifier>();
        for (int i = 0; i < cols.length; i++) {
        	String tmp=cols[i].getNumberOfFractionalDigits()>0?(","+cols[i].getNumberOfFractionalDigits()):"";
        	Identifier id = new ColumnNode(cols[i].getName(), cols[i].getName()
                    + " : " + cols[i].getTypeName() + "(" + cols[i].getSize()
					+tmp
                    + ")", getBookmark(), cols[i]);
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
    public void refresh(DefaultTreeNode parent, INodeFilter filter)
			throws SQLException, UnifyException {
		if (!parent.isExpanded())
			return;
		boolean changed = false; // 是否有变化
		Map<String, DefaultTreeNode> temp = new HashMap<String, DefaultTreeNode>();
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			DefaultTreeNode tmp = (DefaultTreeNode) parent.getChildAt(i);
			temp.put(((Identifier) tmp.getUserObject()).getDisplayLabel(), tmp);
		}
		dataOb.refresh();
		// 获取所有的列信息
		Column[] cols = dataOb.getColumns();
		for (int i = 0; i < cols.length; i++) {
			DefaultTreeNode node = (DefaultTreeNode) temp.remove(cols[i]
					.getName());
			if (node == null) {
				ColumnNode tmpNode = new ColumnNode(cols[i].getName(), cols[i]
						.getName()
						+ " : "
						+ cols[i].getTypeName()
						+ "("
						+ cols[i].getSize() + ")", this.getBookmark(), cols[i]);
				if (filter != null && !filter.filter(tmpNode))
					continue;

				parent.addChild(tmpNode);
				changed = true;
			} else {
				if (filter != null && !filter.filter(node.getUserObject()))// The
																			// node
																			// will
																			// be
																			// removed
				{
					temp.put(cols[i].getName(), node);
				} else {
					Column column = (Column) (((Identifier) node
							.getUserObject()).getDataObject());
					if (!(cols[i].getTypeName().equals(column.getTypeName()))
							|| cols[i].getSize() != column.getSize()) {
						ColumnNode tmpNode = new ColumnNode(cols[i].getName(),
								cols[i].getName() + " : "
										+ cols[i].getTypeName() + "("
										+ cols[i].getSize() + ")", this
										.getBookmark(), cols[i]);
						if (filter != null && !filter.filter(tmpNode))
							continue;

						parent.addChild(tmpNode);
						changed = true;
					}
				}

			}
		}

		for (Iterator<DefaultTreeNode> it = temp.values().iterator(); it
				.hasNext();) // 删除不存在的模式
		{
			parent.remove((DefaultTreeNode) it.next());
			changed = true;
		}
		if (changed)
			BookmarkTreeUtil.getInstance().refreshBookmarkTree(parent); // 刷新节点树模型
    }

    /**
     * 重写ObjectHolder的方法
     */
    public Object getDataObject() {
        return dataOb;
    }

    /**
     * 重写属性方法
     */
    public void property() throws UnifyException, SQLException {
        try {
            Operatable operator = OperatorFactory
                    .getOperator(EntityPropertyOperator.class);
            List<Identifier> list=new ArrayList<Identifier>();
            list.add(this);
            operator.operate(list);
        } catch (ClassNotFoundException e) {
            LogProxy.errorReport(e);
        } catch (InstantiationException e) {
            LogProxy.internalError(e);
        } catch (IllegalAccessException e) {
            LogProxy.internalError(e);
        }
    }
}
