/**
 * 
 */
package com.coolsql.system.menu.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;

import com.coolsql.action.framework.CsAction;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.commonoperator.SQLScriptExecuteOperator;
import com.coolsql.sql.execute.IMultiStatementExecute;
import com.coolsql.sql.execute.ScriptStatementExecute;
import com.coolsql.system.PropertyManage;
import com.coolsql.view.SqlEditorView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.FileLogger;
import com.coolsql.view.log.ILogger;
import com.coolsql.view.log.LogProxy;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-4-1 create
 */
public class ExecuteScriptAction extends CsAction {

	private static final long serialVersionUID = 1L;
	private IMultiStatementExecute executer;
	public ExecuteScriptAction()
	{
		initMenuDefinitionById("ExecuteScriptAction");
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void executeAction(ActionEvent e) {
		String importDataPath=PropertyManage.getSystemProperty().getSelectFile_importData();
		File[] file=GUIUtil.selectFileNoFilter(GUIUtil.findLikelyOwnerWindow(),importDataPath,true,true,false);
		if(file==null||file.length==0)
		{
			return;
		}
		importDataPath=file[0].getParent();
		PropertyManage.getSystemProperty().setSelectFile_importData(importDataPath);
		
		ILogger log=new FileLogger(new File(importDataPath,"script.log"));
		executer=new ScriptStatementExecute(BookmarkManage.getInstance().getDefaultBookmark(),file);
		executer.setExecuteLogger(log);

		Operatable operator;
		try {
			operator=OperatorFactory.getOperator(SQLScriptExecuteOperator.class);
		} catch (Exception e1) {
			LogProxy.errorReport("create sqlOperator error!"+e1.getMessage(), e1);
			return;
		}
		try {
			operator.operate(executer);
		} catch (UnifyException e2) {
            LogProxy.errorReport(e2);
        } catch (SQLException e2) {
            LogProxy.SQLErrorReport(e2);
        }

	}

}
