/*
 * Created on 2007-1-31
 */
package com.coolsql.action.common;

import com.coolsql.modifydatabase.insert.InsertDataDialog;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.sql.model.Entity;

/**
 * @author liu_xlin
 *谈出添加实体数据的窗口
 */
public class InsertToEntityCommand implements ActionCommand {

    private Entity entity;  //要添加新数据的实体
    public InsertToEntityCommand()
    {
        this(null);
    }
    public InsertToEntityCommand(Entity entity)
    {
        this.entity=entity;
    }
    /* (non-Javadoc)
     * @see com.coolsql.action.common.ActionCommand#exectue()
     */
    public void exectue() {
        
        InsertDataDialog dialog=new InsertDataDialog(GUIUtil.getMainFrame(),false,entity);
        dialog.setVisible(true);
    }

}
