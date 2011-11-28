/*
 * 创建日期 2006-9-17
 */
package com.coolsql.gui.property.database;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.ISQLDatabaseMetaData;
import com.coolsql.sql.model.Column;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 列的属性
 */
public class ColumnProperty extends BaseDialog {
    private Column column;
    
    private TextEditor columnName = null; //列名

    private TextEditor entityName = null; //实体名

    private JLabel catalogLabel=null;
    private TextEditor catalogName=null;//catalog name
    
    private JLabel schemaLabel=null;
    private TextEditor schemaName = null; //模式

    private TextEditor aliasName = null; //书签

    private TextEditor typeName = null; //数据类型

    private TextEditor size = null; //长度

    private TextEditor digits = null; //小数

    private TextEditor primarykey = null; 

    private TextEditor position=null; //位置
    
    private TextEditor nullable = null; 

    private TextEditor remark = null; //注释

    private TextEditor defaultValue = null;
    private Bookmark bookmark = null;
    
    public ColumnProperty(JDialog con) {
        this(con, null, null);
    }

    public ColumnProperty(JFrame con) {
        this(con, null, null);
    }

    public ColumnProperty(JDialog con, Column column, Bookmark bookmark) {
        super(con, false);
        this.column = column;
        this.bookmark = bookmark;
        init();
    }

    public ColumnProperty(JFrame con, Column column, Bookmark bookmark) {
        super(con, false);
        this.column = column;
        this.bookmark = bookmark;
        init();
    }

    private void init() {
        this.setTitle("column detail information");
        JPanel mainPane=(JPanel) this.getContentPane();
        
        JPanel topPane=new JPanel();
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getSQLString("sql.propertyset.columninfo.bordertitle.entity"));
        topPane.setBorder(border);
        topPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.weightx=0;
		gbc.weighty=0;
		gbc.insets=new Insets(2,2,2,2);
		
        //书签
        gbc.anchor = GridBagConstraints.EAST;
        topPane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.aliasname"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        aliasName = new TextEditor();
        aliasName.setEditable(false);
        topPane.add(aliasName, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx --; 
        
        //catalog name
        gbc.weightx=0;
        gbc.gridwidth=1;
        gbc.anchor = GridBagConstraints.EAST;
        catalogLabel=new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.catalogname"),SwingConstants.RIGHT);
        topPane.add(catalogLabel, gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        catalogName = new TextEditor();
        catalogName.setEditable(false);
        topPane.add(catalogName, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;
        
        //模式名
        gbc.weightx=0;
        gbc.gridwidth=1;
        gbc.anchor = GridBagConstraints.EAST;
        schemaLabel=new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.schemaname"),SwingConstants.RIGHT);
        topPane.add(schemaLabel, gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        schemaName = new TextEditor();
        schemaName.setEditable(false);
        topPane.add(schemaName, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;
        
        //实体名
        gbc.weightx=0;
        gbc.gridwidth=1;
        gbc.anchor = GridBagConstraints.EAST;
        topPane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.entityname"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        entityName = new TextEditor();
        entityName.setEditable(false);
        topPane.add(entityName, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;
        
		mainPane.add(topPane,BorderLayout.NORTH);
        
		
		
        JPanel pane = new JPanel();
        border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getSQLString("sql.propertyset.columninfo.bordertitle.column"));
        pane.setBorder(border);
        pane.setLayout(new GridBagLayout());
		
        //列名
        gbc.weightx=0;
        gbc.gridwidth=1;
		gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.name"),SwingConstants.RIGHT), gbc);      
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        columnName = new TextEditor();
        columnName.setEditable(false);
        gbc.anchor = GridBagConstraints.WEST;
        pane.add(columnName,gbc);
        gbc.gridy++; //转下一行
        gbc.gridx --; 

        //数据类型
        gbc.weightx=0;
        gbc.gridwidth=1;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.typename"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        typeName = new TextEditor();
        typeName.setEditable(false);
        pane.add(typeName, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;

        //size
        gbc.gridwidth=1;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.size"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        size = new TextEditor();
        size.setEditable(false);
        pane.add(size, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;

        //digits
        gbc.gridwidth=1;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.digits"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        digits = new TextEditor();
        digits.setEditable(false);
        pane.add(digits, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;

        //primarykey
        gbc.gridwidth=1;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.primarykey"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        primarykey = new TextEditor();
        primarykey.setEditable(false);
        pane.add(primarykey, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;

        //nullable
        gbc.gridwidth=1;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.nullable"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        nullable = new TextEditor();
        nullable.setEditable(false);
        pane.add(nullable, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;
        
        //defaultposition
        gbc.gridwidth=1;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.position"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        position = new TextEditor();
        position.setEditable(false);
        pane.add(position, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;
        
        //defaultValue
        gbc.gridwidth=1;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.defaultValue"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        defaultValue = new TextEditor();
        defaultValue.setEditable(false);
        pane.add(defaultValue, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;
        
        //remark
        gbc.gridwidth=1;
        gbc.weightx=0;
        gbc.anchor = GridBagConstraints.EAST;
        pane.add(new JLabel(PublicResource
                .getSQLString("sql.propertyset.columninfo.remark"),SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        remark = new TextEditor();
        remark.setEditable(false);
        pane.add(remark, gbc);
        gbc.gridy++; //转下一行
        gbc.gridx--;
       
        mainPane.add(pane,BorderLayout.CENTER);
        
        RenderButton button=new RenderButton(PublicResource.getSQLString("sql.propertyset.columninfo.closebutton"));
        button.addActionListener(new ActionListener()
                {

                    public void actionPerformed(ActionEvent e) {
                        closeFrame();
                        
                    }
                       
                }
        );
        JPanel btnPane=new JPanel();
        btnPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnPane.add(button);
        mainPane.add(btnPane,BorderLayout.SOUTH);
        
        setDefaultSize();
        getRootPane().setDefaultButton(button);
        GUIUtil.centerFrameToFrame(this.getOwner(),this);
        this.addWindowListener(new WindowAdapter()
                {
                     public void windowClosing(WindowEvent e)
                     {
                         closeFrame();
                     }
                }
        );
    }

    public void initData(Column column) {
        Entity entity = column.getParentEntity();
        if (entity == null) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("searchinfo.query.column.nobookmark"),
                    "no bookmark information", 2);
            return;
        } else {
            if (entity.getBookmark() == null) {
                JOptionPane.showMessageDialog(this, PublicResource
                        .getSQLString("searchinfo.query.column.nobookmark"),
                        "no bookmark information", 2);
                return;
            }else
            {
                this.bookmark=entity.getBookmark();
            }
        }
        ISQLDatabaseMetaData dbmd=null;
        try {
			dbmd=bookmark.getDbInfoProvider().getDatabaseMetaData();
			boolean isSupportCatalog=dbmd.supportsCatalogs();
	        if(isSupportCatalog)
	        {
	        	catalogName.setText( StringUtil.trim(entity.getCatalog()));
	        }else
	        {
	        	catalogLabel.setVisible(isSupportCatalog);
	        	catalogName.setVisible(isSupportCatalog);
	        }
	        boolean isSupportSchema=dbmd.supportsSchemas();
	        if(isSupportSchema)
	        {
	        	schemaName.setText( StringUtil.trim(entity.getSchema()));
	        }else
	        {
	        	schemaLabel.setVisible(isSupportSchema);
	        	schemaName.setVisible(isSupportSchema);
	        }
		} catch (UnifyException e) {
			LogProxy.errorReport(this, e);
			return;
		} catch (SQLException e) {
			LogProxy.SQLErrorReport(this, e);
		}
        

        columnName.setText(StringUtil.trim(column.getName())); //列名
        entityName.setText( StringUtil.trim(entity.getName())); //实体名
        aliasName.setText(StringUtil.trim(bookmark.getAliasName())); //书签
        typeName.setText(StringUtil.trim(column.getTypeName())); //数据类型
        size.setText(String.valueOf(column.getSize())); //长度
        digits.setText(String.valueOf(column.getNumberOfFractionalDigits())); 
        primarykey.setText(column.isPrimaryKey()?"yes":"no"); //是否主键
        nullable.setText(column.isNullable()?"yes":"no"); //是否允许为空
        position.setText(String.valueOf(column.getPosition()));
        remark.setText(StringUtil.trim(column.getRemarks())); //注释

    }
    /**
     * 关闭窗口
     *
     */
    public void closeFrame()
    {
        bookmark=null;
        this.removeAll();
        this.dispose();
    }
    public void setDefaultSize() {
        this.setSize(getDefaultSize());
    }

    public Dimension getDefaultSize() {
        return new Dimension(370, 450);
    }
}
