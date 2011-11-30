package com.coolsql.bookmarkBean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * @author liu_xlin
 *  
 */
public class BookmarkManage {
	private static BookmarkManage instance=null;
	/**
	 * key= bookmarkname ,value= bookmark information
	 */
	private  Map<String ,Object> map = null;

	/**
	 * Bookmark listener list.
	 */
	private Vector<BookmarkListener> listeners = null;
    
	private List<Bookmark> aliasList;
	private AliasChanged changeListener=null;
	
	private Bookmark defaultBookmark;
	
	/** List of listeners */
    protected Vector<DefaultBookmarkChangeListener> defaultListeners = new Vector<DefaultBookmarkChangeListener>();
    
	private BookmarkManage() {
		super();
		map=new HashMap<String,Object>();
		listeners = new Vector<BookmarkListener>();
		changeListener=new AliasChanged();
		aliasList=new ArrayList<Bookmark>();
	}
	
    public synchronized void addBookmark(Bookmark bookmark)
    {
    	bookmark.addPropertyListener(changeListener);
    	map.put(bookmark.getAliasName(),bookmark);
    	aliasList.add(bookmark);
    	Collections.sort(aliasList);
    	fireBookmarkAdd(bookmark);
    }
    public synchronized void removeBookmark(Bookmark bookmark)
    {
    	bookmark.removePropertyListener(changeListener);
    	map.remove(bookmark.getAliasName());
    	aliasList.remove(bookmark);
    	fireBookmarkDelete(bookmark);
    }
    /**
     * 获取书签数量
     * @return
     */
    public int getBookmarkCount()
    {
    	return map.size();
    }
    /**
     * 通过书签别名获取书签对象
     * @param key
     * @return
     */
    public Bookmark get(Object key)
    {
    	return (Bookmark)map.get(key);
    }
    /**
     * 获取所有别名
     * @return
     */
    public Set<String> getAliases()
    {
    	return map.keySet();
    }
    /**
     * 获取所有的数签对象
     * @return
     */
    public List<Bookmark> getBookmarks()
    {
        return aliasList;
    }
	/**
	 * 添加书签监听事件
	 * @param listener
	 */
	public void addBookmarkListener(BookmarkListener listener) {
		listeners.add(listener);
	}
    /**
     * 删除书签监听事件
     * @param listener
     */
	public void removeBookmarkListener(BookmarkListener listener) {
		listeners.remove(listener);
	}
	public void addDefaultBookmarkListener(DefaultBookmarkChangeListener l)
	{
		if(l!=null)
			defaultListeners.add(l);
	}
	public void removeDefaultBookmarkListener(DefaultBookmarkChangeListener l)
	{
		if(l!=null)
			defaultListeners.remove(l);
	}
	public synchronized void setDefaultBookmark(Bookmark bookmark)
	{
//		if(bookmark==null)
//			return;
		Bookmark oldB=this.defaultBookmark;
		this.defaultBookmark=bookmark;
		fireDefaultBookmarkChange(oldB,this.defaultBookmark);
	}
	protected void fireDefaultBookmarkChange(Bookmark oldB,Bookmark newB)
	{
		if(oldB==newB)
			return;
		
		DefaultBookmarkChangeEvent e=new DefaultBookmarkChangeEvent(this,oldB,newB);
		for(DefaultBookmarkChangeListener l:defaultListeners)
		{
			l.defaultChanged(e);
		}
	}
	public Bookmark getDefaultBookmark()
	{
		return defaultBookmark;
	}
	public synchronized void nextBookmarkAsDefault()
	{
		if(aliasList.size()==1)
		{
			if(defaultBookmark==null)
			{
				setDefaultBookmark(aliasList.get(0));
			}
			return;
		}
		
		if(defaultBookmark==null)
		{
			if(aliasList.size()>0)
			{
				setDefaultBookmark(aliasList.get(0));
			}
		}else
		{
			
			int i=aliasList.indexOf(defaultBookmark);
			if(i==(aliasList.size()-1))
			{
				setDefaultBookmark(aliasList.get(0));
			}else
			{
				setDefaultBookmark(aliasList.get(i+1));
			}
		}
	}
	/**
	 * This method will find next bookmark that should be as default selected and has connected to database.
	 */
	public synchronized void nextConnectedBookmarkAsDefault() {
		if (aliasList.size() == 0) {
			setDefaultBookmark(null);
			return;
		}
		if (aliasList.size() == 1) {
			setDefaultBookmark(aliasList.get(0));
			return;
		}

		int startIndex = 0;
		if (defaultBookmark == null) {
			startIndex = -1;
		} else {
			startIndex = aliasList.indexOf(defaultBookmark);
		}

		int i = startIndex + 1;
		while (i != startIndex) {
			if (i == aliasList.size()) {
				i = 0;
				continue;
			}
			Bookmark bookmark = aliasList.get(i);
			i++;
			if (bookmark.isConnected()) {
				setDefaultBookmark(bookmark);
			} else {
				continue;
			}
		}
	}
	public synchronized void previousBookmarkAsDefault()
	{
		if(aliasList.size()==1)
		{
			if(defaultBookmark==null)
			{
				setDefaultBookmark(aliasList.get(0));
			}
			return;
		}
		
		if(defaultBookmark==null)
		{
			if(aliasList.size()>0)
			{
				setDefaultBookmark(aliasList.get(0));
			}
		}else
		{
			
			int i=aliasList.indexOf(defaultBookmark);
			if(i==0)
			{
				setDefaultBookmark(aliasList.get(aliasList.size()-1));
			}else
			{
				setDefaultBookmark(aliasList.get(i-1));
			}
		}
	}
	/**
	 * 通知事件监听器，发生了书签添加动作
	 * @param bookmark
	 */
	protected void fireBookmarkAdd(Bookmark bookmark)
	{
		BookmarkEvent event=new BookmarkEvent(this,BookmarkEvent.BOOKMARK_ADD,bookmark);
		for(int i=0;i<listeners.size();i++)
		{
			BookmarkListener listener=(BookmarkListener)listeners.elementAt(i);
			listener.bookmarkAdded(event);
		}
	}
	/**
	 * 通知事件监听器，发生了书签删除动作
	 * @param bookmark
	 */
	protected void fireBookmarkDelete(Bookmark bookmark)
	{
		BookmarkEvent event=new BookmarkEvent(this,BookmarkEvent.BOOKMARK_DELETE,bookmark);
		for(int i=0;i<listeners.size();i++)
		{
			BookmarkListener listener=(BookmarkListener)listeners.elementAt(i);
			listener.bookmarkDeleted(event);
		}
	}
	/**
	 * 检验书签别名是否存在
	 * @param aliasName
	 * @return
	 */
	public boolean isExist(String aliasName)
	{
		return map.containsKey(aliasName);
	}
	/**
	 * 获取书签管理器实例
	 * @return
	 */
	public static synchronized BookmarkManage getInstance()
	{
		if(instance==null)
			instance=new BookmarkManage();
		return instance;
	}
	private class AliasChanged implements PropertyChangeListener
	{

		/* （非 Javadoc）
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if(evt.getPropertyName().equals("aliasName"))
			{
			    if(evt.getOldValue().equals(evt.getNewValue()))
			        return;
				Object ob=map.get(evt.getOldValue());
				map.remove(evt.getOldValue());
				map.put((String)evt.getNewValue(),ob);
				Collections.sort(aliasList);
			}else if (evt.getPropertyName().equals(Bookmark.PROPERTY_CONNECTED))
			{
				 boolean newValue = ((Boolean) evt.getNewValue()).booleanValue();
				 if (newValue) {
					 setDefaultBookmark((Bookmark)evt.getSource());
				 }
			}
		}
		
	}
}
