/**
 * 
 */
package com.coolsql.action.bookmarkmenu;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import com.coolsql.action.common.Command;

/**
 * @author 刘孝林
 *
 * 2008-1-9 create
 */
public class CopyCommand implements Command{

	private String copyStr=null;
	public CopyCommand(String copyStr)
	{
		this.copyStr=copyStr;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.action.common.Command#execute()
	 */
	public void execute() {
		if(copyStr==null||copyStr.equals(""))
			return ;
		StringSelection ss = new StringSelection(copyStr);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
	}

	
}
