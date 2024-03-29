/**
 * Create date:2008-5-3
 */
package com.coolsql.gui.property.global;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.coolsql.gui.property.BasePropertyPane;
import com.coolsql.gui.property.CustomComponentListener;
import com.coolsql.gui.property.PropertySettingCustomComponentListener;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.display.GUIUtil;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-5-3 create
 */
@SuppressWarnings("serial")
public abstract class BasePropertySettingPane extends BasePropertyPane {

//	private static final StringManager stringMgr=StringManagerFactory.getStringManager(BasePropertySettingPane.class);
	/**
	 * 
	 */
	public BasePropertySettingPane() {
	}
	/**
	 * Restore all property displayed in the current panel to default value.
	 */
	protected abstract void restoreToDefault();
	
	protected JButton createRestoreButton()
	{
		JButton restoreButton=new RenderButton("restore");
		restoreButton.setToolTipText("Restore setting to the default!");
		restoreButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				if(!GUIUtil.getYesNo(GUIUtil.findLikelyOwnerWindow(),"Are you sure to restore to default setting?"))
					return;
				boolean oldValue=isIgnoreEvents();
				setIgnoreEvents(true);
				restoreToDefault();
				setIgnoreEvents(oldValue);
				reset();
			}
			
		}
		);
		return restoreButton;
	}
	protected CustomComponentListener createCustomComponentListener()
	{
		return new PropertySettingCustomComponentListener(this);
	}
}
