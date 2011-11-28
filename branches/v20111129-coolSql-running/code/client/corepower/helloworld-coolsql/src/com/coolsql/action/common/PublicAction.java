/*
 * 创建日期 2006-5-31
 *
 */
package com.coolsql.action.common;

import javax.swing.AbstractAction;
import javax.swing.JComponent;



/**
 * @author liu_xlin
 *
 * 公共事件处理类
 */
public abstract class PublicAction extends AbstractAction {

	 private JComponent com=null;
	 public PublicAction(JComponent com)
	 {
	 	super();
	 	this.com=com;
	 }
     public PublicAction()
     {
         this(null);
     }
	/**
	 * @param view 要设置的 view。
	 */
	public void setComponent(JComponent com) {
		this.com = com;
	}
	/**
	 * @return 返回 view。
	 */
	public JComponent getComponent() {
		return com;
	}
}
