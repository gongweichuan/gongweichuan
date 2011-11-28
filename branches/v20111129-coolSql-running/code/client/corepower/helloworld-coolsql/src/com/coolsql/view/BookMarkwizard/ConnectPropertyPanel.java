/*
 * 创建日期 2006-6-27
 *
 */
package com.coolsql.view.BookMarkwizard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.parse.PublicResource;


/**
 * @author liu_xlin 连接数据库时所需信息面板
 */
public class ConnectPropertyPanel extends JPanel {
	private static final long serialVersionUID = -8058570152213571487L;

	private TextEditor userText = null;

	private JPasswordField pwdText = null;
    
	//是否提示保存密码
	private JCheckBox box = null;
	//自动提交设置
    private JCheckBox autoCommitSet=null;
    
	private URLInfoPanel urlPane=null;

    private Bookmark bookmark;
	public ConnectPropertyPanel() {	
	    this(null);
	}
	public ConnectPropertyPanel(Bookmark bookmark) {
		super();
		this.bookmark=bookmark;
		setLayout(new BorderLayout());
        JPanel pane=new JPanel();
        pane.setLayout(new GridBagLayout());
		
        //公共网袋布局的限制条件
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.0D;
        
		//用户名
		JLabel user = new JLabel(PublicResource.getString("connectpropertypanel.input.user"),SwingConstants.RIGHT);
		userText = new TextEditor();
		pane.add(user,gbc);
		gbc.weightx = 1.0D;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(userText,gbc);
		gbc.gridwidth = 1; //恢复横向网格占据数
		gbc.weightx = 0.0D;
		
		//密码
		gbc.anchor = GridBagConstraints.EAST;
		JLabel pwd = new JLabel(PublicResource.getString("connectpropertypanel.input.password"),SwingConstants.RIGHT);
		pwdText = new JPasswordField();
		pane.add(pwd,gbc);
		gbc.weightx = 1.0D;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(pwdText,gbc);

		//是否提示输入密码
		box = new JCheckBox(PublicResource
				.getString("connectpropertypanel.promptpwdtext"));
		pane.add(box);
        box.setHorizontalTextPosition(SwingConstants.LEFT);
        JPanel p31=new JPanel();
        p31.setLayout(new FlowLayout(FlowLayout.LEFT));
        p31.add(new JLabel(PublicResource.getString("connectpropertypanel.autocommitset")));
        autoCommitSet=new JCheckBox();
//        autoCommitSet.addItem("Always True");
//        autoCommitSet.addItem("Always False");
//        autoCommitSet.addItem("Last Saved");
        p31.add(autoCommitSet);
        pane.add(p31);
        
		//连接url
		if(bookmark==null)
			urlPane=new URLInfoPanel();
	    else
	    {
	    	if(bookmark.getDriver().getParams().size()==0)
	    	{
	    		bookmark.getDriver().getParams().put("dbName", bookmark.getConnectUrl());
	    	}
		    urlPane=new URLInfoPanel(bookmark.getDriver().getOriginalURL(),bookmark.getDriver().getParams());
	    }

		Border border=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		border=BorderFactory.createTitledBorder(border,PublicResource.getString("connectpropertypanel.input.safeinfo"));
		pane.setBorder(border);
		
		add(pane,BorderLayout.NORTH);
		add(urlPane,BorderLayout.CENTER);
		
		setConnectInfo(bookmark);
	}

	/**
	 * @param bookmark
	 *            要设置的 bookmark。
	 */
	public void setConnectInfo(Bookmark bookmark) {
      
		if(bookmark==null)
			return;
	    this.setUserText(bookmark.getUserName());
	    this.setPwdText(bookmark.getPwd());
	    this.setBoxSelected(bookmark.isPromptPwd());
	    this.setAutoCommitSet(bookmark.isAutoCommit());
	    if(bookmark.getDriver().getParams().size()==0)
    	{
    		bookmark.getDriver().getParams().put("dbName", bookmark.getConnectUrl());
    	}
	    urlPane.updateAll(bookmark.getDriver().getOriginalURL(),bookmark.getDriver().getParams());
	    validate();
	}
	/**
	 * 校验参数输入框是否都已经填写
	 * @return
	 */
    public boolean checkData()
    {
    	return urlPane.checkData();
    }
	/**
	 * @return 返回 box。
	 */
	public boolean getBoxSelected() {
		
		return box.getModel().isSelected();
	}
	/**
	 * @param box
	 *            要设置的 box。
	 */
	public void setBoxSelected(boolean selected) {
		this.box.getModel().setSelected(selected);
	}
	/**
	 * @param box
	 *            要设置的 box。
	 */
	public void setBoxText(String boxText) {
		this.box.setText(boxText);
	}
	/**
	 * @param box
	 *            要设置的 box。
	 */
	public String getBoxText() {
		return this.box.getText();
	}
	/**
	 * @return 返回 pwdText。
	 */
	public String getPwdText() {
		return new String(pwdText.getPassword());
	}

	/**
	 * @param pwdText
	 *            要设置的 pwdText。
	 */
	public void setPwdText(String pwdText) {
		this.pwdText.setText(pwdText);
	}

	/**
	 * @return 返回 url。
	 */
	public String getUrl() {
		return urlPane.getConnectUrl();
	}

	/**
	 * @param url
	 *            要设置的 url。
	 */
	public void setUrl(String url) {
		urlPane.setConnectUrl(url);
	}

	/**
	 * @return 返回 userText。
	 */
	public String getUserText() {
		return userText.getText();
	}

	/**
	 * @param userText
	 *            要设置的 userText。
	 */
	public void setUserText(String userText) {
		this.userText.setText(userText);
	}
	
    /**
     * @return 返回 autoCommitSet。
     */
    public boolean getAutoCommitSet() {
        return autoCommitSet.isSelected();
    }
    /**
     * @param autoCommitSet 要设置的 autoCommitSet。
     */
    public void setAutoCommitSet(boolean isSelected) {
        this.autoCommitSet.setSelected(isSelected);
    }
    /**
     * @return 返回 bookmark。
     */
    public Bookmark getBookmark() {
        return bookmark;
    }
}
