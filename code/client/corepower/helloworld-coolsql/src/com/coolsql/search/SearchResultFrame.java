/*
 * 创建日期 2006-8-19
 */
package com.coolsql.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.display.CheckModel;
import com.coolsql.pub.display.CommonDataTable;
import com.coolsql.pub.display.CommonTableModel;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.TableMenu;
import com.coolsql.pub.display.TableScrollPane;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.bookmarkview.BookmarkTreeUtil;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 数据库查找结果展示窗口
 */
public abstract class SearchResultFrame extends BaseDialog {
	private JLabel kwLabel;  //关键字标签
	private TextEditor kwText; //显示关键字内容的文本域
    private JLabel l; //书签名

    private JLabel processInfo;//数据的获取进度

    private TextEditor count = null; //查询结果总数

    private CommonDataTable table = null;

    private Bookmark bookmark = null;

    public SearchResultFrame(JFrame con, Bookmark bookmark) {
        super(con, false);
        this.bookmark = bookmark;
        init();
    }

    public SearchResultFrame(JDialog con, Bookmark bookmark) {
        super(con, false);
        this.bookmark = bookmark;
        init();
    }

    public void init() {
        l = new JLabel("");
        kwLabel=new JLabel(PublicResource
                .getSQLString("searchinfo.result.kw.label"));
        JPanel content = (JPanel) this.getContentPane();

        //初始化提示信息面板 ，该面板中包含了书签信息和结果总数信息
        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
        topPane.add(kwLabel);
        kwText=new TextEditor(15);
        kwText.setEditable(false);
        kwText.setBorder(null);
        topPane.add(kwText);
        
        topPane.add(l);
        topPane
                .add(new JLabel("     "
                        + PublicResource
                                .getSQLString("searchinfo.result.count.label")));
        count = new TextEditor(15);
        count.setEditable(false);
        count.setBorder(null);
        topPane.add(count);

        content.setLayout(new BorderLayout());
        content.add(topPane, BorderLayout.NORTH);
        table = initContent();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ((TableMenu)table.getPopMenuManage()).setExportable(false); //不显示数据导出菜单
        ((TableMenu)table.getPopMenuManage()).setPrintable(false); //不显示打印菜单项

        //添加属性查看菜单项
        JMenuItem item = table
                .getPopMenuManage()
                .addMenuItem(
                        PublicResource
                                .getSQLString("searchinfo.result.menu.moreproertylable"),
                        new MoreProperty(),
                        PublicResource
                                .getSQLIcon("searchinfo.result.menu.moreproertyicon"),
                        false);
        table.getPopMenuManage().addMenuCheck(new DetailMenuCheck(item));//添加该菜单项的可用规则
        /**
         * 添加展开对应节点的菜单项
         */
        ActionListener redirectAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Identifier id = getSelectObject();
                    BookmarkTreeUtil util = BookmarkTreeUtil.getInstance();

                    util.selectNode(id,true);
                } catch (UnifyException e1) {
                    LogProxy.errorReport(e1);
                }
            }

        };
        item = table.getPopMenuManage().addMenuItem(
                PublicResource
                        .getSQLString("searchinfo.result.menu.redirectlabel"),
                redirectAction,
                PublicResource
                        .getSQLIcon("searchinfo.result.menu.redirecticon"),
                false);
        table.getPopMenuManage().addMenuCheck(new DetailMenuCheck(item));//添加该菜单项的可用规则
        content.add(new TableScrollPane(table), BorderLayout.CENTER);

        //添加关闭按钮
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        RenderButton shutDown = new RenderButton(PublicResource
                .getSQLString("searchinfo.result.dialog.button.close"));
        shutDown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                closeFrame();
            }

        });
        buttonPane.add(shutDown);
        processInfo = new JLabel("");
        buttonPane.add(processInfo); //添加进度信息
        content.add(buttonPane, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeFrame();
            }
        });

        setSize(getDefaultSize());
        centerToOwner();
    }

    public void closeFrame() {
        this.removeAll();
        this.dispose();
    }

    /**
     * 将结果以表格的样式展示
     */
    protected abstract CommonDataTable initContent();

    /**
     * 将选中的实体或者列信息转化为Identifier对象
     * 
     * @return
     */
    protected abstract Identifier getSelectObject() throws UnifyException;

    /**
     * 设置缺省的窗口大小
     * 
     * @return
     */
    private Dimension getDefaultSize() {
        Dimension d = new Dimension(400, 250);
        return d;
    }

    /**
     * 查询结果的描述
     * 
     * @param txt
     */
    public void setPrompt(String txt) {
        if (l != null)
            l.setText("  "+txt);
    }
    /**
     * 设置关键字
     * @param str
     */
    public void setKeyword(String str)
    {
    	if(str==null||str.trim().equals(""))
    		return;
    	kwText.setForeground(Color.blue);
    	kwText.setText(str);
    }
    /**
     * 获取更新数据进度信息
     * 
     * @param txt
     */
    public void setProcessInfo(String txt) {
        processInfo.setText(txt);
        processInfo.updateUI();
    }

    /**
     * 设置查询结果总数
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count.setText(String.valueOf(count));
    }

    /**
     * 使当前窗口居中
     *  
     */
    public void centerToOwner() {
        GUIUtil.centerFrameToFrame(this.getOwner(), this);
    }

    /**
     * @return 返回 table。
     */
    public CommonDataTable getTable() {
        return table;
    }

    /**
     * 通知数据表，数据结构发生变化
     *  
     */
    public void notifyTable() {
        CommonTableModel model = (CommonTableModel) table.getModel();
        model.fireTableStructureChanged();
    }

    /**
     * 添加一行数据
     * 
     * @param row
     */
    public abstract void addRow(Object[] row);
    
    /**
     * 重新调整用户界面
     *
     */
    public abstract void adjustGUI();

    //    /**
    //     * 添加一条数据，但是不改变表的展示
    //     * @param row
    //     */
    //    public abstract void addRowNoFire(Object[] row);
    /**
     * 该方法用于查看查询结果中单条结果的详细信息
     *  
     */
    public abstract void detailInfo() throws UnifyException, SQLException;

    protected class MoreProperty implements ActionListener {

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            try {
                detailInfo();
            } catch (UnifyException e1) {
                LogProxy.errorReport(e1);
            } catch (SQLException e1) {
                LogProxy.SQLErrorReport(e1);
            }
        }

    }

    /**
     * 
     * @author liu_xlin 列详细菜单的校验
     */
    private class DetailMenuCheck extends CheckModel {
        public DetailMenuCheck(JMenuItem item) {
            super(item);
        }

        /*
         * （非 Javadoc）
         * 
         * @see com.coolsql.data.display.MenuCheckable#check(javax.swing.JTable,
         *      javax.swing.JMenuItem)
         */
        public void check(Component table) {
            if (!(table instanceof JTable))
                return;
            JTable tmpTable = (JTable) table;
            int[] selected = tmpTable.getSelectedRows();
            if (selected == null || selected.length != 1) //如果未选中，或者选中多行，该菜单项不可用
            {
                if (this.getMenu().isEnabled())
                    this.getMenu().setEnabled(false);
            } else {
                if (!this.getMenu().isEnabled())
                    this.getMenu().setEnabled(true);
            }
        }

    }

    /**
     * @return 返回 bookmark。
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    /**
     * @param bookmark
     *            要设置的 bookmark。
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }
}
