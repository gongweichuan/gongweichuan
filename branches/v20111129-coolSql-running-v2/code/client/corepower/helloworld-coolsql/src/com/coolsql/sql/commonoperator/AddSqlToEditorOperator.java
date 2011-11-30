/*
 * 创建日期 2006-11-30
 */
package com.coolsql.sql.commonoperator;

import java.sql.SQLException;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.SqlEditorView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.sqleditor.EditorUtil;

/**
 * @author liu_xlin
 *将最近执行的sql添加到sql编辑器中
 */
public class AddSqlToEditorOperator implements Operatable {

    /* （非 Javadoc）
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object)
     */
    public void operate(Object arg) throws UnifyException, SQLException {
        if(arg==null)
            throw new UnifyException("no argument");
        if(!(arg instanceof String))
            throw new UnifyException("class type of argument is incorrect:"+arg.getClass());

        String sql=(String)arg;
        SqlEditorView view=ViewManage.getInstance().getSqlEditor();
//        EditorDocument doc=(EditorDocument)view.getEditorPane().getDocument();
        PlainDocument doc=(PlainDocument)view.getEditorPane().getDocument();
        try {
            doc.insertString(view.getEditorPane().getCaretPosition(),sql,EditorUtil.NORMAL_SET);
        } catch (BadLocationException e) {
            throw new UnifyException(PublicResource.getSQLString("sql.addtoeditor.inserterror"));
        }
    }

    /* （非 Javadoc）
     * @see com.coolsql.sql.commonoperator.Operatable#operate(java.lang.Object, java.lang.Object)
     */
    public void operate(Object arg0, Object arg1) throws UnifyException,
            SQLException {

    }

}
