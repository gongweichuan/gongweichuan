/*
 * 创建日期 2006-11-9
 */
package com.coolsql.view.bookmarkview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.coolsql.bookmarkBean.BookmarkEvent;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkListener;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.view.bookmarkview.model.DefaultTreeNode;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.bookmarkview.model.SQLGroupNode;
import com.coolsql.view.bookmarkview.model.SQLNode;

/**
 * @author liu_xlin 管理最近执行的sql
 */
public class RecentSQLManage {

    private static RecentSQLManage manager = null;

    /**
     * 保存的最大sql条数
     */
    private int maxSQL;

    /**
     * key：Bookmark value=LinkedList
     */
    private Map<Bookmark,LinkedList> sqlsData = null;

    /**
     * 属性变化的任务分派器
     */
    private PropertyChangeSupport pcs = null;

    /**
     * 比较器,对执行的sql对象进行排序
     */
    private RecentSQLComparator comparator=null;
    private RecentSQLManage() {
        maxSQL = Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE, 200);;
        sqlsData = Collections.synchronizedMap(new HashMap<Bookmark,LinkedList>());
        pcs = new PropertyChangeSupport(this);
        this.addPropertyChangeListener(new SQLAddListener());
        BookmarkManage.getInstance().addBookmarkListener(new BookmarkManageListener());
        
        comparator=new RecentSQLComparator();
        
        Setting.getInstance().addPropertyChangeListener(new PropertyChangeListener()
        {
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE))
				{
					try
					{
						maxSQL=Integer.parseInt(evt.getNewValue().toString());
					}catch(Exception e)
					{
						//Do nothing.
						return;
					}
					GUIUtil.processOnSwingEventThread(new Runnable(){
						public void run()
						{
							Iterator<Bookmark> bookmarks=sqlsData.keySet().iterator();
							while(bookmarks.hasNext())
							{
								Bookmark b=bookmarks.next();
								LinkedList list=sqlsData.get(b);
								int additionSize=list.size()-maxSQL;
								if(additionSize<=0)
									continue;
								for(int i=0;i<additionSize;i++)
								{
									pcs.firePropertyChange("removesql", null, list.removeLast());
								}
							}
						}
					});
				}
			}
        	
        }
        , PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SQL_HISTORYSIZE);
    }

    public static synchronized RecentSQLManage getInstance() {
        if (manager == null)
            manager = new RecentSQLManage();
        return manager;
    }
    /**
     * 获取对应书签下的所有最近执行的sql
     * 
     * @param bookmark
     * @return --List
     */
    public List getRecentSQLList(Bookmark bookmark) {
        return (LinkedList) sqlsData.get(bookmark);
    }
    public List getRecentSQLList(String bookmark) {
        Bookmark tmpBm=BookmarkManage.getInstance().get(bookmark);
        if(tmpBm==null)
            return null;
        return getRecentSQLList(tmpBm);
    }
    public int getMaxSQL() {
        return maxSQL;
    }

    /**
     * 添加sql对象数据
     * 
     * @param sql
     * @param bookmark
     */
    public void addSQL(RecentSQL sql, String aliasName) {
        Bookmark bookmark = BookmarkManage.getInstance().get(aliasName);
        addSQL(sql, bookmark);
    }

    public void addSQL(RecentSQL sql, Bookmark bookmark) {
        if (bookmark == null)
            return;
        LinkedList sqls = (LinkedList)getRecentSQLList(bookmark);
        if (sqls == null) {
            sqls = new LinkedList();
            sqlsData.put(bookmark, sqls);
        }
        synchronized (sqls) {
            addSQLToList((LinkedList) sqls, sql);
        }
    }
    /**
     * 向链表添加sql对象
     * 
     * @param sqls
     * @param sql
     */
    private void addSQLToList(LinkedList sqls, RecentSQL sql) {
        /**
         * 如果保存的sql对象超过了最大数，删除保留时间最长（最后位置的数据）
         */
        if (sqls.size() >= maxSQL) {
            Object ob = sqls.removeLast();
            pcs.firePropertyChange("removesql", null, ob);
        }
        sqls.addFirst(sql);
        pcs.firePropertyChange("addsql", null, sql);
    }

    /**
     * 属性变化分派对象的代理方法
     * 
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    /**
     * 获取书签对应的sql对象列表，然后对该列表进行排序
     * @param bookmark
     */
    public void adjustOrder(Bookmark bookmark)
    {
        List tmp=this.getRecentSQLList(bookmark);
        if(tmp==null)
            return;
        sortRecentSQLs(tmp);
    }
    /**
     * 对执行的sql按开始时间进行排序
     * @param list
     */
    public void sortRecentSQLs(List list)
    {
        Collections.sort(list,comparator);
    }
    /**
     * 将所有sql对象进行排序
     *
     */
    public void adjustAll()
    {
        Set keys=sqlsData.keySet();
        Iterator it=keys.iterator();
        while(it.hasNext())
        {
            adjustOrder((Bookmark)it.next());
        }
    }
    /**
     * 
     * @author liu_xlin
     *当sql管理器中添加新的sql对象或者删除sql对象时，更新书签树结构
     */
    private class SQLAddListener implements PropertyChangeListener
    {
        private SQLAddListener(){}

        /* （非 Javadoc）
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String name=evt.getPropertyName();
            if(name.equals("addsql"))   //如果是添加sql对象
            {
                RecentSQL data=(RecentSQL)evt.getNewValue();
                
                //获取对应书签的sql组合节点
                DefaultTreeNode groupNode=BookmarkTreeUtil.getInstance().getSQLGroupNode(data.bookmark());
                if(groupNode==null)
                    return;
                if(!groupNode.isExpanded())  //如果sql组合节点没有展开，直接返回
                    return;
                
                SQLNode sqlData=new SQLNode(data.getSql(),data.bookmark(),data);
                DefaultTreeNode child=new DefaultTreeNode(sqlData);
                BookmarkTreeUtil.getInstance().addNode(child,groupNode,0); 
                
                Identifier id=(Identifier)groupNode.getUserObject();
                if(!id.isHasChildren())
                    id.setHasChildren(true);
                
                /**
                 * 如果sql组和节点子节点数大于定义的数值，将最早执行的sql节点删除
                 */
                int childCount=groupNode.getChildCount();
                if(childCount>SQLGroupNode.MAXCHILDCOUNT)
                {
                	BookmarkTreeUtil.getInstance().removeNode(childCount-1,groupNode);
                }
                
                BookmarkTreeUtil.getInstance().repaintTree();
            }else if(name.equals("removesql"))
            {
                RecentSQL data=(RecentSQL)evt.getNewValue();
                SQLNode sqlData=new SQLNode(data.getSql(),data.bookmark(),data);
                
                //获取对应书签的sql组合节点
                DefaultTreeNode groupNode=BookmarkTreeUtil.getInstance().getSQLGroupNode(data.bookmark());
                if(groupNode==null)
                    return;
                if(!groupNode.isExpanded())  //如果sql组合节点没有展开，直接返回
                    return;
                
                BookmarkTreeUtil.getInstance().removeNode(sqlData,groupNode); 
                
                Identifier id=(Identifier)groupNode.getUserObject();
                if(id.isHasChildren()&&groupNode.getChildCount()<1)
                    id.setHasChildren(false);
                BookmarkTreeUtil.getInstance().repaintTree();
            }
        }
        
    }
    /**
     * 
     * @author liu_xlin
     * 对书签管理器的监听类。当书签被删除的时候，删除该书签所对应的sql链表
     */
    private class BookmarkManageListener implements BookmarkListener
    {

        /* （非 Javadoc）
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkAdded(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookmarkAdded(BookmarkEvent e) {                       
        }

        /* （非 Javadoc）
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkDeleted(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookmarkDeleted(BookmarkEvent e) {
           Bookmark bookmark=e.getBookmark();
           sqlsData.remove(bookmark);
        }

        /* （非 Javadoc）
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkUpdated(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookMarkUpdated(BookmarkEvent e) {            
        }
        
    }
    /**
     * 
     * @author liu_xlin
     *对RecentSQL对象进行排序，排序的依据是执行时间点,后执行的排在前面
     */
    protected class RecentSQLComparator implements Comparator
    {

        /* （非 Javadoc）
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            if (o1 == null && o2 == null)
                return 0;
            if(o1==null||o1 instanceof RecentSQL)
                return 1;
            if(o2==null||o2 instanceof RecentSQL)
                return -1;
            
            RecentSQL data1=(RecentSQL)o1;
            RecentSQL data2=(RecentSQL)o2;
            return (int)(data2.getTime()-data1.getTime());
        }
        
    }
}
