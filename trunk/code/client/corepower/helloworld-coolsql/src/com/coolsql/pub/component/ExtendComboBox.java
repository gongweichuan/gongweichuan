/*
 * Created on 2007-1-22
 */
package com.coolsql.pub.component;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

/**
 * @author liu_xlin
 *对可编辑组合框组件进行扩展,将其与另外一组合框进行封装,使两者进行协调选择数据.
 */
public class ExtendComboBox extends EditComboBox {
	private static final long serialVersionUID = 1L;
	private int queryMode = 0;

    public ExtendComboBox() {
        super();
    }

    public class SearchModeSelect extends JComboBox implements ItemListener {
		private static final long serialVersionUID = 1L;

		public SearchModeSelect() {
            this.addItem("==");
            this.addItem("。。%");
            this.addItem("%。。");
            this.addItem("%。。%");
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
