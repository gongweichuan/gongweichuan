/*
 * 创建日期 2006-9-26
 */
package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

/**
 * @author liu_xlin
 *清空组件内容处理
 */
public class ClearTextAction extends AbstractAction {
    private JTextComponent txt;
    public ClearTextAction(JTextComponent txt)
    {
        this.txt=txt;
    }
    /* （非 Javadoc）
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if(txt!=null)
            txt.setText("");
    }

}
