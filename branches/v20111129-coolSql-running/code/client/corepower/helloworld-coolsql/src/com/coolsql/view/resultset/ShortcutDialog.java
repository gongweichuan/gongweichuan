/*
 * 创建日期 2006-12-15
 */
package com.coolsql.view.resultset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.DefinableToolBar;
import com.coolsql.pub.component.IconButton;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.ViewManage;
import com.coolsql.view.resultset.action.EditDataSetTableAction;
import com.coolsql.view.resultset.action.QueryAllRowsOfShortcutAction;

/**
 * @author liu_xlin 以弹出窗口的形式展示结果集视图中某一页的信息
 */
public class ShortcutDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	/**
     * 图标按钮定义
     */
    private IconButton prePage = null; //前一页按钮

    private IconButton nextPage = null; //下一页按钮

    private IconButton queryAllPage = null; //查询所有行

    private IconButton refresh = null; //刷新数据的获取

    /**
     * 数据结果集展示表组件
     */
    private DataSetPanel dataPane = null;

    private DataUpdateListener listener = null; //结果集数据发生变化的监听器

    public ShortcutDialog(DataSetPanel dataPane) {
        super(GUIUtil.getMainFrame(), false);
        this.dataPane = dataPane;
        initPane();
    }

    public void addDataUpdateListener() {
        dataPane.addPanelPropertyChangeListener(listener);
    }

    public void removeDataUpdateListener() {
        dataPane.removePanelPropertyChangeListener(listener);
    }

    protected void initPane() {
        JPanel pane = (JPanel) getContentPane();
        pane.setLayout(new BorderLayout());

        JToolBar toolBar = createTools();
        pane.add(toolBar, BorderLayout.NORTH);

        if (dataPane != null) {
            pane.add(dataPane, BorderLayout.CENTER);
            this.setTitle(PublicResource.getString("shortcutdialog.title")
                    + dataPane.getBookmark().getAliasName() + ")");
        }

        /**
         * 操作面板
         */
        JPanel taskPane = new JPanel();
        taskPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        //返回按钮
        RenderButton returnTab = new RenderButton(PublicResource
                .getString("shortcutdialog.btn.returntab"));
        returnTab.setToolTipText(PublicResource
                .getString("shortcutdialog.btn.returntab.tip"));
        returnTab.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeDilaogAndReturn();
            }

        });
        taskPane.add(returnTab);

        //关闭按钮
        RenderButton quit = new RenderButton(PublicResource
                .getString("shortcutdialog.btn.quit"));
        quit.setToolTipText(PublicResource
                .getString("shortcutdialog.btn.quit.tip"));
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }

        });
        taskPane.add(quit);

        pane.add(taskPane, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //选择关闭后是否返回结果集视图中
                int result = JOptionPane.showConfirmDialog(ShortcutDialog.this,
                        PublicResource.getString("shortcutdialog.closeprompt"),
                        "confirm close", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) //选择是：关闭窗口，并返回结果集
                    closeDilaogAndReturn();
                else if (result == JOptionPane.NO_OPTION) //选择否：只关闭窗口
                    closeDialog();
            }
        });

        GUIUtil.setFrameSizeToScreen(this,(float)7/8,(float)3/4);
        
        listener = new DataUpdateListener();
        if (dataPane != null)
        {
            checkButtonAvailable();
            addDataUpdateListener();           
        }
    }

    /**
     * 创建工具按钮条，与结果集视图中的图标按钮类似
     * 
     * @return --工具按钮条对象
     */
    private JToolBar createTools() {
        DefinableToolBar toolBar = new DefinableToolBar("shortcutFrameTools");
//        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
//        toolBar.setBorderPainted(true);
//        toolBar.setFloatable(true);
//        toolBar.setRollover(true);
//        toolBar.setMargin(new Insets(0,0,0,0));
        toolBar.setPreferredSize(new Dimension(200,30));
        
        prePage = toolBar
                .addIconButton(
                        PublicResource
                                .getIcon("resultView.iconbutton.previouspage.icon"),
                        new EditDataSetTableAction(ResultSetDataProcess.PREVIOUS,dataPane),
                        PublicResource
                                .getString("resultView.iconbutton.previouspage.tooltip")); //前一页数据按钮
        nextPage = toolBar.addIconButton(PublicResource
                .getIcon("resultView.iconbutton.nextpage.icon"),
                new EditDataSetTableAction(ResultSetDataProcess.NEXT,dataPane),
                PublicResource
                        .getString("resultView.iconbutton.nextpage.tooltip")); //后一页数据按钮

        queryAllPage = toolBar
                .addIconButton(
                        PublicResource
                                .getIcon("resultView.iconbutton.queryallpage.icon"),
                        new QueryAllRowsOfShortcutAction(dataPane),
                        PublicResource
                                .getString("resultView.iconbutton.queryallpage.tooltip")); //查询所有数据按钮

        refresh = toolBar.addIconButton(PublicResource
                .getIcon("resultView.iconbutton.refresh.icon"),
                new EditDataSetTableAction(ResultSetDataProcess.REFRESH,dataPane),
                PublicResource
                        .getString("resultView.iconbutton.refresh.tooltip")); //刷新数据按钮

        return toolBar;
    }

    /**
     * 关闭对话框窗口
     *  
     */
    public void closeDialog() {
        clearDialog();
        dataPane.setRemoving(true);
        if (dataPane != null) //彻底删除时,清空各监听
        {
            dataPane.removeComponent();
        }
        dataPane = null;
    }

    /**
     * 关闭窗口，并且将数据返回到结果集视图中
     *  
     */
    public void closeDilaogAndReturn() {
        clearDialog();
        dataPane.setVisible(true); //保持数据面板的可视性
        dataPane.setRemoving(true); //恢复为彻底删除状态
        //添加对结果对象的监听
        dataPane.addPanelPropertyChangeListener(ViewManage.getInstance().getResultView()
                .getResultSetListener());
        //添加至结果集视图中
        ViewManage.getInstance().getResultView().addTab(dataPane,
                dataPane.getBookmark().getAliasName(), dataPane.getSql());
        dataPane = null;
    }

    private void clearDialog() {
        removeDataUpdateListener();
        
        removeAll();
        dispose();

    }

    private void checkButtonAvailable() {
        if (!dataPane.isReady()) { //如果数据面板未准备好,将向前、后,以及查询所有、刷新按钮置为不可用
            GUIUtil.setComponentEnabled(false, prePage);
            GUIUtil.setComponentEnabled(false, nextPage);
            GUIUtil.setComponentEnabled(false, queryAllPage);
            GUIUtil.setComponentEnabled(false, refresh);
        } else {
            if (dataPane.getSqlResult().isResultSet()) { //如果为查询类型的结果集
                if (dataPane.hasPrevious()) //如果能够向前滚动数据
                    GUIUtil.setComponentEnabled(true, prePage);
                else
                    GUIUtil.setComponentEnabled(false, prePage);

                if (dataPane.hasNext()) //能够向后滚动数据
                    GUIUtil.setComponentEnabled(true, nextPage);
                else
                    GUIUtil.setComponentEnabled(false, nextPage);

                GUIUtil.setComponentEnabled(true, refresh);//将刷新按钮置为可用
                GUIUtil.setComponentEnabled(true, queryAllPage); //同上
            } else //如果为更新或者删除类型结果集,将向前,向后,查询所有数据,刷新等按钮置为不可用
            {
                GUIUtil.setComponentEnabled(false, prePage);
                GUIUtil.setComponentEnabled(false, nextPage);
                GUIUtil.setComponentEnabled(false, queryAllPage);
                GUIUtil.setComponentEnabled(false, refresh);
            }
        }
    }

    private class DataUpdateListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("sqlResult")) //如果数据面板的执行结果发生变化,对按钮可用性进行校验
            {
                checkButtonAvailable();
            }
        }
    }
}
