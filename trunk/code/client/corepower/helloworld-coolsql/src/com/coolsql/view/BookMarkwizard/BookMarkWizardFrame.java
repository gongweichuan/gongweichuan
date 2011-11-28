/*
 * create date: 2006-5-31
 *
 */
package com.coolsql.view.BookMarkwizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.coolsql.action.common.NextButtonAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.DriverInfo;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.View;


/**
 * @author liu_xlin
 *新建书签向导，选择驱动程序类型界面
 */
public class BookMarkWizardFrame extends CommonFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	//	protected JTable driverList;
	private Bookmark bookmark;
	
	protected DriverManagePanel driverPanel;
	
	public BookMarkWizardFrame()
	{
	   super(null);
	   init();
	}
	public BookMarkWizardFrame(JFrame owner,View view,Bookmark bookmark)
	{
        super(owner,true,view);
        this.bookmark=bookmark;
        init();

	}
    public BookMarkWizardFrame(JDialog owner,View view,Bookmark bookmark)
    {
        super(owner,true,view);	
        this.bookmark=bookmark;
        init();
    }
    public void init()
    {
        this.setDescribeText(PublicResource.getString("bookmark.selectdriver"));
        this.enableNextButton(new NextButtonAction(this),true);
        setTitle(PublicResource.getString("bookmark.selectdialogtitle"));
        this.getRootPane().setDefaultButton(nextButton);
    }
    public JComponent initDialog()
    {
    	driverPanel=new DriverManagePanel();
    	
    	return driverPanel;
    }
    public void actionPerformed(ActionEvent e)
    {
    	AddDriverFrame tmp=new AddDriverFrame(this,this.getView());
    	tmp.setVisible(true);
    }
    /**
     * 本方法已经失去原来意义，此处的定义，便于窗口类AddDriverFrame使用
     * 
     * @param ob
     */
    public void preButtonProcess(Object ob) {
    	LoadJar load=LoadJar.getInstance();
    	HashMap map=(HashMap)ob;
    	Set set=map.keySet();
    	Iterator it=set.iterator();
    	while(it.hasNext())
    	{
    		String className=(String)(it.next());
            load.addClassPath(className,(String)map.get(className));
    		this.addInfo(new DriverInfo(className));
    	}
    }
    /**
     * 定义公共接口，点击完成按钮时被调用（完成按钮在选择驱动程序对话框中，非本窗口）
     */
    public void shutDialogProcess(Object ob)
    {

    }
	/**
	 * 定义下一步按钮点击后的处理接口
	 * @param ob
	 */
	public void nextButtonProcess(Object ob)
	{	
		Bookmark tmp=getSelectedInfo();
		if(tmp==null)
			return;
		dispose();
		ConnectPropertyDialog cpd=new ConnectPropertyDialog((JFrame)(this.getParent()),getView(),tmp);
		cpd.setVisible(true);
		
	}
    /**
     * 在驱动列表中添加一条驱动信息
     * @param info
     */
    private void addInfo(DriverInfo info)
    {
    	if(info==null)
    		return ;
    	DefaultTableModel dtm=(DefaultTableModel)driverPanel.getDriverList().getModel();
    	if(checkExist(info.getClassName()))
    	{
    		return ;
    	}
    	Vector v=new Vector();
    	v.add(info.getClassName());
    	v.add(info.getType());
    	v.add(info.getFilePath());
    	dtm.addRow(v);
    }
    /**
     * 校验选择的驱动程序在列表中是否已经显示
     * @param className
     */
    private boolean checkExist(String className)
    {
    	DefaultTableModel dtm=(DefaultTableModel)driverPanel.getDriverList().getModel();
    	for(int i=0;i<dtm.getRowCount();i++)
    	{
    		String name=(String)dtm.getValueAt(i,0);
    		if(name.equals(className))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    /**
     * 获取选择的驱动类，同时返回一个书签类
     * @return
     */
    private Bookmark getSelectedInfo()
    {
    	JTable driverTable=driverPanel.getDriverList();
//    	DefaultTableModel dtm=(DefaultTableModel)driverTable.getModel();
    	int row=driverTable.getSelectedRow();
    	if(row<0)
    	{
    		JOptionPane.showMessageDialog(this,PublicResource.getString("bookmark.noselectdriver"),"warning!",2);
    		return null;
    	}
    	String className=(String)driverTable.getValueAt(row,0);
    	if(bookmark==null)
    	{
    		bookmark=new Bookmark();
    	}
    	bookmark.setClassName(className);
    	return bookmark;
    }
	/**
	 * @return 返回 bookmark。
	 */
	public Bookmark getBookmark() {
		return bookmark;
	}
	/**
	 * @param bookmark 要设置的 bookmark。
	 */
	public void setBookmark(Bookmark bookmark) {
		this.bookmark = bookmark;
	}
}
