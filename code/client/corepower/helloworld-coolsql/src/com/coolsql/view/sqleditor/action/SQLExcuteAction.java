/*
 * 创建日期 2006-10-16
 */
package com.coolsql.view.sqleditor.action;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.commonoperator.SQLScriptExecuteOperator;
import com.coolsql.sql.execute.IMultiStatementExecute;
import com.coolsql.sql.execute.MultiStatementExecute;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;
import com.coolsql.system.menu.action.BaseEditorSelectAction;
import com.coolsql.view.SqlEditorView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin
 * sql执行的处理类，调用了sql执行处理线程来实现该功能
 */
public class SQLExcuteAction extends BaseEditorSelectAction {

	private static final long serialVersionUID = 1L;
	public SQLExcuteAction(){
		super(true);
		initMenuDefinitionById("SQLExcuteAction");
	}
    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
	@Override
    public void executeAction(ActionEvent e) {
        SqlEditorView view=ViewManage.getInstance().getSqlEditor();
        Bookmark bookmark=BookmarkManage.getInstance().getDefaultBookmark();
        if(bookmark==null)
        {
            LogProxy.infoMessage(PublicResource.getSQLString("sql.execute.nobookmark"));
            return;
        }
        if(!bookmark.isConnected())
        {
            LogProxy.errorMessage(PublicResource.getSQLString("database.notconnected")+bookmark.getAliasName());
            return;
        }
        List list= view.getQueries();
        
        if(list.size()<1)
        {
            LogProxy.message(PublicResource.getSQLString("sql.execute.nosql"),JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(bookmark==null)
        {
            LogProxy.message(PublicResource.getSQLString("sql.execute.nobookmark"),JOptionPane.WARNING_MESSAGE);
            return;
        }
        int scriptThreshold=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_SCRIPT_SQLTHRESHOLD, 2);
        if(list.size()>=scriptThreshold)
        {
        	JCheckBox checkbox = new JCheckBox("Don't show this dialog again.");
    		checkbox.setMnemonic('D');
        	String message=PublicResource.getSQLString("sql.execute.scriptprompt", new Object[]{list.size(),scriptThreshold})+"\n";
        	int result=JOptionPane.showConfirmDialog(GUIUtil.findLikelyOwnerWindow(), message,
        			"confirm",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        	if(result==JOptionPane.CANCEL_OPTION)
        		return;
        	else if(result==JOptionPane.YES_OPTION)
        	{
        		Operatable operator;
        		try {
					operator=OperatorFactory.getOperator(SQLScriptExecuteOperator.class);
				} catch (Exception e1) {
					LogProxy.errorReport("create sqlOperator error!"+e1.getMessage(), e1);
					return;
				}
				IMultiStatementExecute se=new MultiStatementExecute(bookmark,list);
        		try {
					operator.operate(se);
				} catch (UnifyException e2) {
	                LogProxy.errorReport(e2);
	            } catch (SQLException e2) {
	                LogProxy.SQLErrorReport(e2);
	            }
	            return;
        	}
        }
        
        Operatable operator = null;
        try {
            operator = OperatorFactory
                    .getOperator("com.coolsql.sql.commonoperator.SQLResultProcessOperator");
        } catch (Exception e1) {
            LogProxy.internalError(e1);
            return;
        }
        for(int i=0;i<list.size();i++)
        {
            List<Object> argList = new ArrayList<Object>();
            argList.add(bookmark);
            argList.add(list.get(i));
            argList.add(new Integer(ResultSetDataProcess.EXECUTE));//设置sql的执行处理类型：首次执行
            try {
                operator.operate(argList);
            } catch (UnifyException e2) {
                LogProxy.errorReport(e2);
            } catch (SQLException e2) {
                LogProxy.SQLErrorReport(e2);
            }
        }

    }

}
