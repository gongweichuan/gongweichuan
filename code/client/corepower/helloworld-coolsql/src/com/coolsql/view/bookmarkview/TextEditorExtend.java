/*
 * 创建日期 2006-8-19
 */
package com.coolsql.view.bookmarkview;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import com.coolsql.pub.component.TextEditor;

/**
 * @author liu_xlin 查找数据库窗口专用的单行文本输入框
 */
public class TextEditorExtend extends TextEditor {
    private int queryMode = 0;

    public TextEditorExtend() {
        super();
    }

    public TextEditorExtend(int length) {
        super(length);
    }

    public class SearchModeSelect extends JComboBox implements ItemListener {
        public SearchModeSelect() {
            this.addItem("==");
            this.addItem("..%");
            this.addItem("%..");
            this.addItem("%..%");
            this.setSelectedIndex(3);
            queryMode=3;
            this.addItemListener(this);
        }

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) { //如果指定项被选中以后,才作相应的调整
                queryMode = this.getSelectedIndex();
            }
        }

    }

    /**
     * @return 返回 queryMode。
     */
    public int getQueryMode() {
        return queryMode;
    }

    /**
     * @param queryMode
     *            要设置的 queryMode。
     */
    public void setQueryMode(int queryMode) {
        this.queryMode = queryMode;
    }
}
