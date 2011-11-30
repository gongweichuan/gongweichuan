/*
 * 创建日期 2006-9-12
 */
package com.coolsql.gui.property.database;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.coolsql.action.common.PublicAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.DriverInfo;
import com.coolsql.gui.property.PropertyInterface;
import com.coolsql.gui.property.PropertyPane;
import com.coolsql.pub.component.CommonFrame;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.StringManager;
import com.coolsql.pub.parse.StringManagerFactory;
import com.coolsql.view.BookMarkwizard.BookMarkWizardFrame;

/**
 * Driver information property panel
 * @author liu_xlin
 */
public class DriverProperty extends PropertyPane implements PropertyInterface,
        ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final StringManager stringMgr=StringManagerFactory.getStringManager(DriverProperty.class);

	private TextEditor driverName;

    private TextEditor driverVer;

    private TextEditor driverClass;

    private TextEditor path;

    private TextEditor type;

    private Bookmark bookmark;

    private boolean isChanged=false;
    
    private RenderButton changeDriver;
    /**
     * @param ob
     */
    public DriverProperty() {
        super();
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyPane#initContent(java.lang.Object)
     */
    public JPanel initContent() {
        JPanel pane = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        pane.setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.drivername"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        driverName = new TextEditor();
        driverName.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(driverName, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.driverversion"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        driverVer = new TextEditor();
        driverVer.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(driverVer, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.driverclass"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        driverClass = new TextEditor();
        driverClass.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(driverClass, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.driverpath"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        path = new TextEditor();
        path.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(path, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.drivertype"),SwingConstants.RIGHT), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx++;
        type = new TextEditor();
        type.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        pane.add(type, gbc);
        gbc.gridy++;
        gbc.gridx--;

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        changeDriver= new RenderButton("changeDriver");
        changeDriver.addActionListener(this);
        pane.add(changeDriver, gbc);
        return pane;
    }

    private void initData(Bookmark bm) {
        DriverInfo driver = bm.getDriver();
        setField(driver);
        
        changeDriver.setEnabled(!bm.isConnected());
    }

    /**
     * 设置文本域的值
     * 
     * @param driver
     */
    private void setField(DriverInfo driver) {
        driverName.setText(driver.getDriverName() == null ? "" : driver
                .getDriverName());
        driverVer.setText(driver.getDriverVersion() == null ? "" : driver
                .getDriverVersion());
        driverClass.setText(driver.getClassName() == null ? "" : driver
                .getClassName());
        path.setText(driver.getFilePath() == null ? "" : driver.getFilePath());
        type.setText(driver.getType() == null ? "" : driver.getType());
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#set()
     */
    public boolean set() {
        if(isChanged)
        {
           
			int result=JOptionPane.showConfirmDialog(this, PublicResource
					.getSQLString("sql.propertyset.driverchange")
					, "confirm!", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION)
			{
			    getBookMark().setClassName(driverClass.getText());
			    isChanged=false;
			    
			    JOptionPane.showMessageDialog(this, stringMgr.getString("property.driver.promptrewrite"));
				return false;
			}else
			    getBookMark().getDriver().setClassName(driverClass.getText());
			isChanged=false;
        }
        return true;
    }

    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#apply()
     */
    public void apply() {
        getBookMark().setClassName(driverClass.getText());
        isChanged=false;
        JOptionPane.showMessageDialog(this, stringMgr.getString("property.driver.promptrewrite"));
    }
    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#quit()
     */
    public void cancel() {

    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.gui.property.PropertyInterface#setData(java.lang.Object)
     */
    public void setData(Object ob) {
        if (ob == null)
            return;
        bookmark = (Bookmark) ob;
        initData(bookmark);
    }

    public Bookmark getBookMark() {
        return this.bookmark;
    }

    /*
     * （非 Javadoc）
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        if (bookmark == null) {
            JOptionPane.showMessageDialog(this, "no bookmark information!",
                    "error", 0);
            return;
        }
        
        //重新定义驱动选择窗口
        BookMarkWizardFrame bwf = new BookMarkWizardFrame(
                (JDialog)CommonFrame.getParentWindow(this,1) , null, getBookMark()) {
					private static final long serialVersionUID = 1L;

			public void shutDialogProcess(Object ob) {
				
				JTable driverTable=driverPanel.getDriverList();
                int row = driverTable.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this, PublicResource
                            .getString("bookmark.noselectdriver"), "warning!",
                            2);
                    return;
                }
                String className = (String) driverTable.getValueAt(row, 0);
                String oldClass=driverClass.getText();
//                	bookmark.getDriver().getClassName();
                if(className.equals(oldClass))
                {
                    this.dispose();
                    return;
                }
                isChanged=true;
                DriverInfo driver = new DriverInfo(className);
                setField(driver);
                this.dispose();
            }

            /**
             * 不显示上一步按钮
             * 
             * @return
             */
            protected boolean displayPreButton() {
                return false;
            }

            /**
             * 不显示下一步按钮
             * 
             * @return
             */
            protected boolean displayNextButton() {
                return false;
            }
        };
        bwf.enableOkButton(new DriverSelectAction(bwf), true);
        bwf.setVisible(true);

    }
    private class DriverSelectAction extends PublicAction
    {
		private static final long serialVersionUID = 1L;
		
		private BookMarkWizardFrame driverSelect;
        public DriverSelectAction(BookMarkWizardFrame b)
        {
            driverSelect=b;
        }
        /* （非 Javadoc）
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            driverSelect.shutDialogProcess(null);
            
        }
        
    }
    /* （非 Javadoc）
     * @see com.coolsql.gui.property.PropertyInterface#isNeedApply()
     */
    public boolean isNeedApply() {
        return true;
    }
}
