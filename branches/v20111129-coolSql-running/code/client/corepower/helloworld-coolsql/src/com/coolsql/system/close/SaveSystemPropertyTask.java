/**
 * 
 */
package com.coolsql.system.close;

import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.xml.XMLException;
import com.coolsql.system.PropertyManage;
import com.coolsql.system.Setting;
import com.coolsql.system.Task;
import com.coolsql.view.log.LogProxy;

/**
 * @author kenny liu
 *
 * 2007-10-30 create
 */
public class SaveSystemPropertyTask implements Task {

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#execute()
	 */
	public void execute() {
		try {
			PropertyManage.saveSystemProperty();
		} catch (XMLException e) {
			LogProxy.errorMessage(e.getMessage());
		}
		try
		{
			Setting.getInstance().saveSetting();
		}catch(Exception e)
		{
			LogProxy.errorMessage(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getDescribe()
	 */
	public String getDescribe() {
		return PublicResource.getString("system.closetask.savesystemproperties.describe");
	}

	/* (non-Javadoc)
	 * @see com.coolsql.system.Task#getTaskLength()
	 */
	public int getTaskLength() {
		return 2;
	}

}
