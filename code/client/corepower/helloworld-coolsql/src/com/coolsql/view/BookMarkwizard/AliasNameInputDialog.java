/*
 * 创建日期 2006-6-30
 *
 */
package com.coolsql.view.BookMarkwizard;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.coolsql.action.common.OkButtonAction;
import com.coolsql.action.common.PreButtonAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.View;


/**
 * 别名输入对话框
 * @author liu_xlin
 *
 */
@SuppressWarnings("serial")
public class AliasNameInputDialog extends CommonFrame {

	private TextEditor editor;
	private Bookmark bookMark;
	/**
	 * @param d
	 * @param b
	 * @param view
	 */
	public AliasNameInputDialog(JFrame d, View view,Bookmark bookMark) {
		super(d, true, view);
		this.bookMark=bookMark;
		init();
	}	
	public AliasNameInputDialog(JDialog d, View view,Bookmark bookMark) {
		super(d, true, view);
		this.bookMark=bookMark;
		init();
	}
	public void init()
	{
		if(bookMark!=null)
		   editor.setText(bookMark.getAliasName());
		this.setTitle(PublicResource.getString("aliasnameinputdialog.title"));
		this.setDescribeText(PublicResource.getString("aliasnameinputdialog.describe"));
		this.enableOkButton(new OkButtonAction(this),true);
		this.enablePreButton(new PreButtonAction(this),true);
	}
	public Bookmark getBookMark()
	{
		if(bookMark==null)
			bookMark=new Bookmark();
		bookMark.setAliasName(editor.getText());
		return bookMark;
	}
	/**
	 * 校验数据是否正确输入
	 * @return
	 */
	public boolean checkData()
	{
		if(!editor.getText().equals(editor.getText().trim()))
		{
			JOptionPane.showMessageDialog(this,PublicResource.getString("aliasnamechangedialog.havespaces"),"warning",2);
			return false;
		}
		if(editor.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this,PublicResource.getString("aliasnameinputdialog.noinput"),"warning",2);
			return false;
		}
		return true;
	}
	/**
	 */
	public void setBookmark(Bookmark bookmark) {
		this.bookMark = bookmark;
		editor.setText(bookMark.getAliasName());
	}
	/* （非 Javadoc）
	 * @see src.pub.window.CommonFrame#initDialog()
	 */
	public JComponent initDialog() {
		JPanel main=new JPanel();
		JLabel l=new JLabel(PublicResource.getString("aliasnameinputdialog.label"));
		editor=new TextEditor(40);
		main.setLayout(new FlowLayout(FlowLayout.LEFT));
		main.add(l);
		main.add(editor);
		return main;
	}
	/**
	 * 是否显示下一步按钮
	 * 
	 * @return
	 */
	protected boolean displayNextButton() {
		return false;
	}
	/**
	 * ok按钮的事件响应处理
	 */
	public void shutDialogProcess(Object ob) {
		Bookmark tmp=getBookMark();
		if(!checkData())
			return;
		BookmarkManage bm=BookmarkManage.getInstance();
		if(!bm.isExist(tmp.getAliasName()))
		{
			this.dispose();
		    bm.addBookmark(tmp);
		}else
		{
			JOptionPane.showMessageDialog(this,PublicResource.getString("aliasnameinputdialog.aliasexist"),"warning",2);
		}
	}
	/**
	 * 定义上一步按钮点击后的处理接口
	 * @param ob
	 */
	public void preButtonProcess(Object ob)
	{		
		getBookMark();
		dispose();
		ConnectPropertyDialog cpd=new ConnectPropertyDialog((JFrame)(this.getParent()),getView(),bookMark);
		cpd.setVisible(true);
	}
}
