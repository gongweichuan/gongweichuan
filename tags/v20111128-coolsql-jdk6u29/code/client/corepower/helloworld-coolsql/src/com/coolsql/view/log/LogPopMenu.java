/*
 * 创建日期 2006-7-13
 *
 */
package com.coolsql.view.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import com.coolsql.action.common.ClearTextAction;
import com.coolsql.action.common.PublicAction;
import com.coolsql.action.common.TextCopyAction;
import com.coolsql.action.common.TextSelectAllAction;
import com.coolsql.exportdata.ExportData;
import com.coolsql.exportdata.ExportFactory;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin
 * 日志编辑菜单
 */
public class LogPopMenu extends BaseMenuManage {
    /**
     * 复制
     */
	private JMenuItem copy = null;
	
	/**
	 * 清空
	 */
	private JMenuItem clearAll = null;

	/**
	 * 全选
	 */
	private JMenuItem selectAll = null;
	
	/**
	 * 是否换行显示
	 */
	private JCheckBoxMenuItem wrapSet=null;
	
	/**
	 * 导出日志内容
	 */
	private JMenuItem export=null;
	public LogPopMenu(JTextComponent com) {
		super(com);
		createPopMenu();
	}
    /* （非 Javadoc）
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        JTextComponent tCom=(JTextComponent)this.getComponent();
		if (tCom.getSelectedText() == null
				||tCom.getSelectedText().trim().equals("")) 
		{
		    if(copy.isEnabled())
		    {
		        copy.setEnabled(false);
		    }
		}else
		{
		    if(!copy.isEnabled())
		    {
		        copy.setEnabled(true);
		    }
		}
		if(tCom.getText().equals(""))
		{
		    if(clearAll.isEnabled())
		        clearAll.setEnabled(false);
		}else
		{
		    if(!clearAll.isEnabled())
		        clearAll.setEnabled(true);
		}
        return popMenu;
    }
    /* （非 Javadoc）
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        return itemCheck();
    }
	protected void createPopMenu() {
		popMenu = new BasePopupMenu();

		//复制菜单项
		Action copyAction = new TextCopyAction((JTextComponent)this.getComponent());
		copy = this.createMenuItem(PublicResource
				.getString("TextEditor.popmenu.copy"), PublicResource.getIcon("TextMenu.icon.copy"),
				copyAction);
		popMenu.add(copy);

		//清空菜单项
		clearAll=this.createMenuItem(PublicResource
				.getString("logView.popmenu.clearall"), PublicResource.getIcon("logView.popmenu.icon.clearall"),
				new ClearTextAction((JTextComponent)this.getComponent()));
		popMenu.add(clearAll);
		
		
		popMenu.addSeparator();
		
		//导出日志菜单项
		Action exportAction=new PublicAction()
		{

            public void actionPerformed(ActionEvent e) {
                ExportData exporter=ExportFactory.createExportForTextComponent((JTextComponent)LogPopMenu.super.getComponent()); 
                try {
                    exporter.exportToTxt();
                } catch (UnifyException e1) {
                    LogProxy.errorReport(e1);
                }
            }
		     
		};
		export=this.createMenuItem(PublicResource
				.getString("logView.log.exportlog"), PublicResource.getIcon("logView.log.exportlog.icon"),
				exportAction);
		popMenu.add(export);
		
		//是否自动换行菜单项
		wrapSet=new JCheckBoxMenuItem(PublicResource.getString("logView.popmenu.iswrap"));
		wrapSet.addActionListener(new ActionListener()
		        {

                    public void actionPerformed(ActionEvent e) {
                        if(wrapSet.getState())
                        {
                            ((LogTextPane)getComponent()).setWrap(true);                           
                        }else
                            ((LogTextPane)getComponent()).setWrap(false); 
                    }
		            
		        }
		);
		popMenu.add(wrapSet);
		
		popMenu.addSeparator();
		
		//全选菜单项
		Action selectAllAction = new TextSelectAllAction((JTextComponent)this.getComponent());
		selectAll = this.createMenuItem(PublicResource
				.getString("TextEditor.popmenu.selectAll"), PublicResource.getIcon("popmenu.icon.blank"),
				selectAllAction);
		popMenu.add(selectAll);
		

	}
}
