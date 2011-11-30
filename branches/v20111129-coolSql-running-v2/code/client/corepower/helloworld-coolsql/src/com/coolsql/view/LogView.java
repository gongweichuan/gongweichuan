/*
 * 创建日期 2006-6-8
 *
 */
package com.coolsql.view;

import javax.swing.JScrollPane;

import com.coolsql.action.common.ClearTextAction;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.view.log.LogPopMenu;
import com.coolsql.view.log.LogTextPane;
import com.coolsql.view.mouseEventProcess.PopupAction;


/**
 * @author liu_xlin
 *日志打印窗口
 */
public class LogView extends TabView {

	private static final long serialVersionUID = 1L;
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(LogView.class);
	
	private LogTextPane log=null;
	private LogPopMenu popMenu=null;
	public LogView()
	{
		super();
		init();
	}
	public void init()
	{
		log=new LogTextPane(); 
		log.addMouseListener(new PopupAction(this));
		this.setContent(new JScrollPane(log));
		popMenu=new LogPopMenu(log);
		
		createIconButton();
		
	}
    /**
     * 创建图标按钮
     *  
     */
    private void createIconButton() {
        this.addIconButton(PublicResource
                .getIcon("logView.popmenu.icon.clearall"),
                new ClearTextAction(log), PublicResource
                        .getString("logView.iconbutton.clear.tooltip")); //前一页数据按钮
    }
	/* （非 Javadoc）
	 * @see java.awt.Component#getName()
	 */
	public String getName() {
		return stringMgr.getString("view.log.title");
	}
	/* （非 Javadoc）
	 * @see src.view.Display#dispayInfo()
	 */
	public void dispayInfo() {
		
	}
	/* （非 Javadoc）
	 * @see src.view.Display#popupMenu()
	 */
	public void popupMenu(int x,int y) {
		popMenu.getPopMenu().show(log, x, y);
		
	}
    /* (non-Javadoc)
     * @see com.coolsql.view.View#createActions()
     */
    protected void createActions() {
        
    }
    @Override
    public String getTabViewTitle()
    {
    	return PublicResource.getString("logView.tabtitle");
    }
    @Override
    public int getTabViewIndex()
    {
    	return 1;
    }
	/* (non-Javadoc)
	 * @see com.coolsql.view.View#doAfterMainFrame()
	 */
	@Override
	public void doAfterMainFrame() {
		
	}
}
