/*
 * 创建日期 2006-12-6
 *
 */
package com.coolsql.pub.component;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;

import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin 显示当天日期,同时也能够被选中
 */
public class DateLabel extends JLabel {

	private Date date = null;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat dayFormat = new SimpleDateFormat("d");
	
	private String describe = PublicResource.getUtilString("date.currentdescribe");

	/**
	 * 该标签是否显示当天日期
	 */
	private boolean isDayDisplay = false;

	public DateLabel(Date date) {
		this(date, true);
	}

	/**
	 * 
	 * @param date
	 * @param isTodayDay --true 显示日信息 false:显示当前日期信息
	 */
	public DateLabel(Date date, boolean isDayDisplay) {
		super();
		this.date = date;
		this.isDayDisplay = isDayDisplay;
		this.setPreferredSize(new Dimension(40, 20));
	     
		updateLabel();
		if(isDayDisplay)
			setHorizontalAlignment(JLabel.CENTER);
		else
			setHorizontalAlignment(JLabel.LEFT);
	}

	/**
	 * @return 返回 date。
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            要设置的 date。
	 */
	public void setDate(Date date) {
		this.date = date;

		updateLabel();

	}

	/**
	 * 更新标签的信息显示
	 *
	 */
	private void updateLabel() {
		if (date == null)
			return;
		if (!isDayDisplay) {
			String dateStr = dateFormat.format(date);
			this.setText("<html><body>" + describe + "<font color=red>"
					+ dateStr + "</font></body></html>");
		}else
		{
			this.setText(dayFormat.format(date));
		}
		setToolTipText(dateFormat.format(date));
	}
}
