﻿package com.coolsql.action.common;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.UndoManager;
/**
 * 
 * @author liu_xlin
 *撤销事件处理
 */
public class UndoAction extends AbstractAction
{

    public UndoAction(UndoManager undo)
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
        if(_undo.canUndo())
            _undo.undo();
    }

    private UndoManager _undo;
}
