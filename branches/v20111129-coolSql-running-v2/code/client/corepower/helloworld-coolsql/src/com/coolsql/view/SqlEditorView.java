/*
 * 创建日期 2006-6-8
 *
 */
package com.coolsql.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.BadLocationException;

import com.coolsql.action.common.SQLHistoryAction;
import com.coolsql.action.framework.CsAction;
import com.coolsql.action.sqleditormenu.AutoSelectAction;
import com.coolsql.bookmarkBean.BookmarkEvent;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkListener;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.bookmarkBean.DefaultBookmarkChangeEvent;
import com.coolsql.bookmarkBean.DefaultBookmarkChangeListener;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.SplitButton;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.system.Setting;
import com.coolsql.view.bookmarkview.BookMarkPubInfo;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.mouseEventProcess.PopupAction;
import com.coolsql.view.sqleditor.EditorPanel;
import com.coolsql.view.sqleditor.FormatSQLAction;
import com.coolsql.view.sqleditor.SqlEditorMenuManage;
import com.coolsql.view.sqleditor.SqlPanel;
import com.coolsql.view.sqleditor.action.CommitAction;
import com.coolsql.view.sqleditor.action.EditorViewSqlExecuteListener;
import com.coolsql.view.sqleditor.action.EnableAutoCommitAction;
import com.coolsql.view.sqleditor.action.RollbackAction;
import com.coolsql.view.sqleditor.pop.SelectedListener;

/**
 * @author liu_xlin sql语句编辑面板
 */
public class SqlEditorView extends View {
	private static final long serialVersionUID = 1L;

	private static final StringManager stringMgr=StringManagerFactory.getStringManager(SqlEditorView.class);
	/**
     * sql编辑面板
     */
    private SqlPanel pane = null;

    /**
     * 菜单管理器
     */
    private BaseMenuManage menuManage = null;

    /**
     * 组合按钮,用于执行sql
     */
    private SplitButton runBtn = null;

    private BookmarkChangedListener listener = null;
    
    
	/**
	 * Action variant
	 */
	private CsAction commitAction;
	private CsAction rollbackAction;
    private CsAction enableAutoCommitAction;
	
    public SqlEditorView() {
        super();
        this.setTopTextIcon(PublicResource.getIcon("sqlEditorView.icon"));
        pane = new SqlPanel();
//        JScrollPane scroll = new JScrollPane(pane);
        this.add(pane, BorderLayout.CENTER);
        
        menuManage = new SqlEditorMenuManage(pane);
        pane.setPopupMenu(menuManage.getPopMenu());  //添加右键菜单
        
        pane.getEditor().addMouseListener(new PopupAction(this));

        
        createIconButton();
        
    }
    /**
     * 创建图标按钮
     *  
     */
    protected void createIconButton() {
        runBtn = new SplitButton(PublicResource
                .getIcon("sqlEditorView.iconbutton.run"));
        runBtn.addAction(new EditorViewSqlExecuteListener(this));
        runBtn.setToolTipText(PublicResource
                .getString("sqlEditorView.iconbutton.tooltip.run"));
        runBtn.addPropertyChangeListener(new BookmarkSelectListener());
        this.addComponentOnBar(runBtn); //sql执行按钮
        //同时需要对已存在的书签进行监听
        listenBookmarks();

        //commitAction
        addIconButton(commitAction.getToolbarButton());
        
        //rollbackAction
        addIconButton(rollbackAction.getToolbarButton());
        
        
        //查看sql执行的历史记录
        this.addIconButton(PublicResource
                .getIcon("sqlEditorView.iconbutton.history"),
                new SQLHistoryAction(), PublicResource
                        .getString("sqlEditorView.iconbutton.history.tip"));

        this.addIconButton( Setting.getInstance().getShortcutManager()
				.getActionByClass(AutoSelectAction.class).getToolbarButton());
        this.addIconButton(Setting.getInstance().getShortcutManager()
				.getActionByClass(FormatSQLAction.class).getToolbarButton());
        //清除sql编辑视图的内容
        Action clearAction=new AbstractAction()
        {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				pane.getEditor().setText("");			
			}
        	
        };
        this.addIconButton(PublicResource
                .getIcon("logView.popmenu.icon.clearall"),
                clearAction, PublicResource
                        .getString("sqlEditorView.iconbutton.tooltip.clear")); //清空所有编辑内容
    }
    /**
     * 监听已经存在的书签，同时也监听书签管理器
     *  
     */
    private void listenBookmarks() {
        listener = new BookmarkChangedListener();
        BookmarkManage manage = BookmarkManage.getInstance();
        manage.addBookmarkListener(listener); //对书签管理器添加监听器
        manage.addDefaultBookmarkListener(new DefaultBookmarkChangeListener()
        {

			public void defaultChanged(DefaultBookmarkChangeEvent e) {
				 try {
                     runBtn.changeDefaultItem(e.getNewValue(), false);
                 } catch (UnifyException e1) {
                     LogProxy.errorReport(e1);
                 }
			}
        	
        }
        );
        Collection set = manage.getBookmarks();
        Iterator it = set.iterator();
        while (it.hasNext()) //为每个书签添加监听器
        {
            Bookmark bookmark = (Bookmark) it.next();
            bookmark.addPropertyListener(listener);
        }
    }

    /**
     * 在sql编辑视图中更新当前选择的书签别名
     * 
     * @param bookmark
     *            --书签别名
     */
    public void updateSelectedBookmark(Bookmark bookmark) {
        if (bookmark == null)
            return;
        String title = PublicResource.getString("sqlEditorView.title");
        title += "("
                + PublicResource
                        .getString("sqlEditorView.title.currentbookmark")
                + bookmark.getAliasName() + ")";
        this.setTopText(title);
    }
	protected void checkAutocommit(final Bookmark bookmark)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				if (bookmark != null)
				{
					// if autocommit is enabled, then rollback and commit will
					// be disabled
					boolean flag = bookmark.isConnected()&&!bookmark.isAutoCommit();
					commitAction.setEnabled(flag);
					rollbackAction.setEnabled(flag);
					
					((EnableAutoCommitAction)enableAutoCommitAction).setSelected(bookmark.isAutoCommit());
				}
			}
		});
	}
    /**
     * 对选中的编辑内容进行解析，将选中内容拆分为单个的sql语句，以便于执行
     * 
     * @return
     */
    public List<String> getQueries() {
        return pane.getQueries();
    }

    /**
     * 获取编辑内容
     * 
     * @return
     */
    public String getEditorContent() {
        return pane.getEditor().getText();
    }

    /**
     * 设置编辑区域的文本内容
     * 
     * @param content
     */
    public void setEditorContent(String content) {
        pane.getEditor().setText(content);
    }

    /**
     * 获取选中的文本信息
     * 
     * @return
     */
    public String getSelectedText() {
        return StringUtil.trim(pane.getEditor().getSelectedText());
    }
    /**
     * 设置缺省书签
     * @param bookmark  --被设置的默认书签
     */
    public void setDefaultBookmark(Bookmark bookmark)
    {
        try {
            runBtn.changeDefaultItem(bookmark,false);
        } catch (UnifyException e) {
            LogProxy.errorReport(e);
        }
    }

    /*
     * （非 Javadoc）
     * 
     * @see src.view.View#getName()
     */
    public String getName() {
        return stringMgr.getString("view.sqleditor.title");
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

//        menuManage.getPopMenu().show(pane, x, y);
    }

    public EditorPanel getEditorPane() {
        return pane.getEditor();
    }
    public void autoSelect()
    {
    	try {
			pane.autoSelect();
		} catch (BadLocationException e) {
			LogProxy.errorLog("",e);
		}
    }
    /**
     * Find specified text in the sql editor
     */
    public void toFind()
    {
    	pane.toFind();
    }
    /**
     * 
     * @author liu_xlin 选中理想的值后，将选中的值在编辑区域显示
     */
    private class EntitySelectListener implements SelectedListener {

        /*
         * (non-Javadoc)
         * 
         * @see com.coolsql.view.sqleditor.pop.SelectedListener#selected(java.lang.Object)
         */
        public void selected(Object value) {
			int[] range = pane
					.getWordRange(pane.getEditor().getCaretPosition() - 1);
			if (range != null && range.length == 2) {
				int len = range[1] - range[0];
				if (len < 1)
					len = 0;
				else
					len++;
				pane.getEditor().setSelectionStart(range[0]);
				pane.getEditor().setSelectionEnd((range[1]));
				pane.getEditor().setSelectedText(value.toString());
			}
		}

    }

    /**
	 * 
	 * @author liu_xlin 书签选择监听器，实时更新sql编辑视图的标题信息
	 */
    private class BookmarkSelectListener implements PropertyChangeListener {

        /*
         * （非 Javadoc）
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("selectedData")) //书签数据选择改变了
            {
                Object newOb = evt.getNewValue();
                if (newOb != null && newOb instanceof Bookmark) {
                    updateSelectedBookmark((Bookmark) newOb);
                    checkAutocommit((Bookmark) newOb);
                }
            }

        }

    }

    /**
     * 
     * @author liu_xlin 对书签管理器和书签的监听
     */
    private class BookmarkChangedListener implements BookmarkListener,
            PropertyChangeListener {

        /**
         * 新增书签时，向组合按钮添加菜单项
         * 
         * @see com.coolsql.bookmarkBean.BookmarkListener#bookmarkAdded(com.coolsql.bookmarkBean.BookmarkEvent)
         */
        public void bookmarkAdded(BookmarkEvent e) {
            Bookmark bookmark = (Bookmark) e.getBookmark();
            bookmark.addPropertyListener(this);
            try {
                runBtn
                        .addDataItem(bookmark.getAliasName(), BookMarkPubInfo.
                        		getBookmarkIcon(bookmark.isConnected()),
                                bookmark);
            } catch (UnifyException e1) {
                LogProxy.messageReport(e1.getMessage(), 0);
            }

        }

        /**
         * 书签删除，则将相应的菜单删除
         * 
         * @see com.coolsql.bookmarkBean.BookmarkListener#bookmarkDeleted(com.coolsql.bookmarkBean.BookmarkEvent)
         */
        public void bookmarkDeleted(BookmarkEvent e) {
            Bookmark bookmark = (Bookmark) e.getBookmark();
            bookmark.removePropertyListener(this);
            runBtn.deleteDataItem(bookmark);
        }

        /*
         * （非 Javadoc）
         * 
         * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkUpdated(com.coolsql.bookmarkBean.BookMarkEvent)
         */
        public void bookMarkUpdated(BookmarkEvent e) {
        }

        /**
         * 更新组合按钮的菜单标签
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if (name.equals("aliasName")) //如果书签发生了别名变化，则进行适当的处理
            {
                runBtn.updateItemByData(evt.getSource(), (String) evt
                        .getNewValue(), null);
            } else if (name.equals(Bookmark.PROPERTY_CONNECTED)) //如果书签发生连接事件，更新组合按钮的缺省菜单项
            {
                boolean newValue = ((Boolean) evt.getNewValue()).booleanValue();

                runBtn.updateItemByData(evt.getSource(), 
                		((Bookmark)evt.getSource()).getAliasName(), BookMarkPubInfo.getBookmarkIcon(newValue));
                if(evt.getSource()==runBtn.getSelectData())
                {
                	checkAutocommit((Bookmark)evt.getSource());
                }
            }else if (name.equals(Bookmark.PROPERTY_AUTOCOMMIT)) 
            {
            	checkAutocommit((Bookmark)evt.getSource());
            }
        }

    }
    public void clearTrackEntity()
    {
    	pane.clearTrack();
    }
    /* (non-Javadoc)
     * @see com.coolsql.view.View#createActions()
     */
    protected void createActions() {
		commitAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(CommitAction.class);
		rollbackAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(RollbackAction.class);
		enableAutoCommitAction=Setting.getInstance().getShortcutManager()
		.getActionByClass(EnableAutoCommitAction.class);
    }

	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		pane.registerCtrlListener();
		
	}
}
