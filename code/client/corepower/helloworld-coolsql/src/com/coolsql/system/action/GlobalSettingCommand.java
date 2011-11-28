/**
 * 
 */
package com.coolsql.system.action;

import com.coolsql.action.common.ActionCommand;
import com.coolsql.gui.property.NodeKey;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.log.LogProxy;

/**
 * setting command for global system
 * @author 刘孝林(kenny liu)
 *
 * 2008-1-22 create
 */
public class GlobalSettingCommand implements ActionCommand {
	/**
	 * default node that displayed ,when dialog is shown.
	 */
	private NodeKey defaultKey;
	public GlobalSettingCommand(NodeKey key)
	{
		defaultKey=key;
	}
	/* (non-Javadoc)
	 * @see com.coolsql.action.common.ActionCommand#exectue()
	 */
	public void exectue() {
		try {
			Operatable operator=OperatorFactory.getOperator("com.coolsql.sql.commonoperator.SystemPropertySettingOperator");
			operator.operate(defaultKey);
		} catch (Exception e) {
			LogProxy.errorReport("getting operator:com.coolsql.sql.commonoperator.SystemPropertySettingOperator error"+e.getMessage(), e);
		}
		
	}

}
