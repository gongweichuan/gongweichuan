/*
 * 创建日期 2006-6-8
 *
 */
package com.coolsql.main.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import com.coolsql.pub.component.BaseFrame;
import com.coolsql.pub.component.MainFrameStatusBar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.CloseProgressDialog;
import com.coolsql.system.DoOnClosingSystem;
import com.coolsql.system.PropertyConstant;
import com.coolsql.system.PropertyManage;
import com.coolsql.system.Setting;
import com.coolsql.system.SystemThread;
import com.coolsql.view.TabView;
import com.coolsql.view.View;
import com.coolsql.view.ViewManage;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 主界面设计
 */
public class MainFrame extends BaseFrame {

	private static final long serialVersionUID = -9055062945934379296L;

	/**
     * split1=bookmarkview+split2
     */
    private JSplitPane split1 = null;

    /**
     * split2=sqlEditor+JTabbedPane
     */
    private JSplitPane split2 = null;

    private ViewManage vm = null;

    public MainFrame() {
        super();
        setTitle(PublicResource.getString("mainFrame.title"));//设置窗口的标题
        
        vm = ViewManage.getInstance();
        split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
        split1.setDividerSize(3);
        split1.setOneTouchExpandable(false);
        View tmpView = vm.getBookmarkView();
        tmpView.setTopText(PublicResource.getString("bookmarkView.title"));
        tmpView.setTopTextIcon(PublicResource.getIcon("bookmarkView.icon"));
        split1.setTopComponent(tmpView); //添加书签视图

        split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
        split2.setDividerSize(3);
        split2.setOneTouchExpandable(false);

        
        JTabbedPane tab =TabView.getTabPane();
        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tab.setTabPlacement(JTabbedPane.BOTTOM);
        TabView resultView = vm.getResultView();
        resultView.setTopText(PublicResource.getString("resultView.title"));
        tab.addTab(resultView.getTabViewTitle(),resultView.getTabViewIcon(), resultView); //添加结果集视图
        tab.setToolTipTextAt(tab.indexOfComponent(resultView), resultView.getTabViewTip());
        
        TabView logView = vm.getLogView();
        logView.setTopText(PublicResource.getString("logView.title"));
        tab.addTab(logView.getTabViewTitle(),logView.getTabViewIcon(), logView); //添加日志试图
        tab.setToolTipTextAt(tab.indexOfComponent(logView), logView.getTabViewTip());
        
        tmpView = vm.getSqlEditor();
        tmpView.setTopText(PublicResource.getString("sqlEditorView.title"));
        split2.setTopComponent( tmpView); //添加sql编辑视图
        split2.setBottomComponent(tab);

        split1.setRightComponent(split2);
        split1.resetToPreferredSizes();

        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(split1, BorderLayout.CENTER);
        
        if(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_SYSTEM_STATUSBAR_DISPLAY, true))
        {
        	MainFrameStatusBar statusBar=new MainFrameStatusBar();
    		pane.add(statusBar,BorderLayout.SOUTH);
        }
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeSystem();
            }
        });
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(d);
        try {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (Throwable e) {
        }
        int a = (int) d.getWidth() * 2 / 10;
        int b = (int) d.getHeight() / 3;
        split1.setDividerLocation(a);
        split2.setDividerLocation(b);
        split1.setContinuousLayout(false);
        split2.setContinuousLayout(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setToolTip();
    }
    /**
     * 设置提示信息的初始化时间和等待时间
     *  
     */
    private void setToolTip() {
        ToolTipManager toolTipManage = ToolTipManager.sharedInstance();
        toolTipManage.setInitialDelay(100); //设置弹出时间为0.1秒
        toolTipManage.setDismissDelay(100000); //等待时间100秒
    }

    /**
     * 获取jdk版本
     * 
     * @return
     */
    public String getJDKVersion() {
        return System.getProperty("java.version	");
    }

    /**
     * 将系统关闭
     *  
     */
    public void closeSystem() {
        int result = JOptionPane.showConfirmDialog(this, PublicResource
                .getString("system.closetask.confirm"), "close confirm",
                JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION)
            return;

        PropertyManage.saveLaunchSetting();
        DoOnClosingSystem closer = DoOnClosingSystem.getInstance();
        CloseProgressDialog progress = closer.getProgressDialog();
        SystemThread st=new SystemThread(progress);
        try {
            closer.close();
            progress.toCenter();
            progress.setVisible(true);
            progress.toFront();
            st.start();
        } catch (Exception e1) {
            LogProxy.outputErrorLog(e1);
        } finally {
//            progress.dispose();
//            System.exit(0);
        }
    }
}
