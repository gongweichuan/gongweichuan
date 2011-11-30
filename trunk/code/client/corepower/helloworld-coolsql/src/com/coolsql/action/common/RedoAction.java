

package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.UndoManager;
/**
 * 
 * @author liu_xlin
 *重做事件响应
 */
public class RedoAction extends AbstractAction
{

    public RedoAction(UndoManager undo)
    {
        if(undo == null)
        {
            throw new IllegalArgumentException("UndoManager == null");
        } else
        {
            _undo = undo;
            return;
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(_undo.canRedo())
            _undo.redo();
    }

    private UndoManager _undo;
}
