/*
 * 创建日期 2006-12-6
 */
package com.coolsql.pub.component;

import java.awt.BorderLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

import com.jidesoft.swing.JideComboBox;
import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

/**
 * @author liu_xlin 日期选择控件
 */
public class DateSelector extends JComboBox {

	private static final long serialVersionUID = 1L;

	/**
     * 日期格式类型
     */
    public static final String STYLE_DATE = "yyyy/MM/dd";

    public static final String STYLE_DATE1 = "yyyy-MM-dd";

    public static final String STYLE_DATETIME = "yyyy/MM/dd HH:mm:ss";

    public static final String STYLE_DATETIME1 = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式类型
     */
    private String formatStyle = STYLE_DATE1;

    /**
     * 当前设置日期格式
     */
    private SimpleDateFormat dateFormat = null;

    /**
     * 日期选择控件的模型对象
     */
    private DateSelectorModel model = null;

    public DateSelector() throws UnsupportedOperationException {
        this(STYLE_DATE1);
    }

    public DateSelector(String formatStyle)
            throws UnsupportedOperationException {
        this(formatStyle, new Date());
    }

    public DateSelector(String formatStyle, Date initialDatetime)
            throws UnsupportedOperationException {

        this.setStyle(formatStyle);
        //设置可编辑
        this.setEditable(true);

        JTextField textField = ((JTextField) getEditor().getEditorComponent());
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        //设置Model为单值Model
        model = new DateSelectorModel();
        this.setModel(model);
        //设置当前选择日期
        this.setSelectedItem(initialDatetime == null ? new Date()
                : initialDatetime);
    }

    /**
     * 设置日期显示格式
     * 
     * @param style
     */
    public void setStyle(String style) {
        this.formatStyle = style;
        if (style == null)
            return;
        if (dateFormat == null)
            dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(style);
    }
    /**
     * 返回日期展示的格式
     * @return
     */
    public String getStyle()
    {
        return formatStyle;
    }
    /**
     * 更新UI
     */
    public void updateUI() {
        ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
        if (cui instanceof MetalComboBoxUI) {
            cui = new MetalDateComboBoxUI();
        } else if (cui instanceof MotifComboBoxUI) {
            cui = new MotifDateComboBoxUI();
        } else {
            cui = new WindowsDateComboBoxUI();
        }
        setUI(cui);
    }
    /**
     * 取得当前选择的日期
     * @return Date
     */
    public Date getSelectedDate() throws ParseException{
        return dateFormat.parse(getSelectedItem().toString());
    }

    /**
     * 设置当前选择的日期
     * @return Date
     */
    public void setSelectedDate(Date date) throws ParseException{
        this.setSelectedItem(dateFormat.format(date));
    }

    public void setSelectedItem(Object anObject){
        model.setSelectedItem(anObject);
        super.setSelectedItem(anObject);
    }
    /**
     * 
     * @author liu_xlin 对于metal外观时，comboBox组件对应的ui
     */
    private class MetalDateComboBoxUI extends MetalComboBoxUI {
        protected ComboPopup createPopup() {
            return new DatePopup(comboBox);
        }
    }

    /**
     * 
     * @author liu_xlin 对于windows外观时，comboBox组件对应的ui
     */
    private class WindowsDateComboBoxUI extends WindowsComboBoxUI {
        protected ComboPopup createPopup() {
            return new DatePopup(comboBox);
        }
    }

    /**
     * 
     * @author liu_xlin 对于motif外观时，comboBox组件对应的ui
     */
    private class MotifDateComboBoxUI extends MotifComboBoxUI {
        protected ComboPopup createPopup() {
            return new DatePopup(comboBox);
        }
    }

    /**
     * 
     * @author liu_xlin 基本的组合控件弹出面板。重写了属性变化通知方法
     */
    protected class DatePopup extends BasicComboPopup implements ChangeListener {
        DateSelectPanel calendarPanel = null;

        public DatePopup(JComboBox box) {
            super(box);
            setLayout(new BorderLayout());
            calendarPanel = new DateSelectPanel();
            calendarPanel.addDateChangeListener(this);
            add(calendarPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder());
        }

        /**
         * 显示弹出面板
         */
        protected void firePropertyChange(String propertyName, Object oldValue,
                Object newValue) {
            if (propertyName.equals("visible")) {
                if (oldValue.equals(Boolean.FALSE)
                        && newValue.equals(Boolean.TRUE)) { //SHOW
                    try {
                        String strDate = comboBox.getSelectedItem().toString();
                        Date selectionDate = dateFormat.parse(strDate);
                        calendarPanel.setSelectedDate(selectionDate);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (oldValue.equals(Boolean.TRUE)
                        && newValue.equals(Boolean.FALSE)) { //HIDE
                }
            }
            super.firePropertyChange(propertyName, oldValue, newValue);
        }

        public void stateChanged(ChangeEvent e) {
            Date selectedDate = (Date) e.getSource();
            String strDate = dateFormat.format(selectedDate);
            if (comboBox.isEditable() && comboBox.getEditor() != null) {
                comboBox.configureEditor(comboBox.getEditor(), strDate);
            }
            comboBox.setSelectedItem(strDate);
            comboBox.setPopupVisible(false);
        }
    }

}
