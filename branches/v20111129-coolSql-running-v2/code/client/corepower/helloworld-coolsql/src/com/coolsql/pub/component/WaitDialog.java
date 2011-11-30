/*
 * 创建日期 2006-10-27
 */
package com.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.coolsql.exportdata.Actionable;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.jidesoft.swing.MultilineLabel;

/**
 * @author liu_xlin 当数据正在处理时，如果处理时间过长，那么使用该对话框，进行处理进度的提示
 */
public class WaitDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private JTextArea label = null;

    /**
     * 中部面板
     */
    private JPanel centerPane = null;

    private RenderButton quit = null;

//    /**
//     * 体现了当前等待对话框是否显示
//     */
//    private boolean isDisplayed = false;
//
//    private boolean isLock=false;
    /**
     * 该等待对话框的取消按钮被点击后，所有需要执行的处理，保存在该向量中
     */
    private Vector<Actionable> quitActions = new Vector<Actionable>();

    protected WaitDialog(Frame owner)
    {
        super(owner, PublicResource
                .getString("waitdialog.title"), true);
        initPanel();
    }
    protected WaitDialog(Dialog owner) {
        super(owner, PublicResource
                .getString("waitdialog.title"), true);
        initPanel();
    }
    /**
     * 初始化内容面板
     *
     */
    private void initPanel()
    {
        
        JPanel content = (JPanel) this.getContentPane();  
        content.setLayout(new BorderLayout());

        JPanel displayPanel=new JPanel();
        displayPanel.setLayout(new GridLayout(2,1));
        displayPanel.setBackground(getBackgroundColor());
       
        label = new MultilineLabel();
        
        centerPane = new JPanel();
        centerPane.setLayout(new BorderLayout());
        centerPane.setBackground(getBackgroundColor());
        
//        displayPanel.add(centerPane);
//        displayPanel.add(label);
        content.add(centerPane, BorderLayout.NORTH);
        content.add(label, BorderLayout.CENTER);

        JPanel pane = new JPanel();
        pane.setBackground(getBackgroundColor());
        pane.setLayout(new FlowLayout(FlowLayout.CENTER));
        quit = new RenderButton(PublicResource.getString("waitdialog.button.quit"));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setEnabled(false);

                runQuit();

                dispose();
            }

        });
        pane.add(quit);
        content.add(pane, BorderLayout.SOUTH);

        content.setBackground(getBackgroundColor());
        this.setUndecorated(true);//去除窗口装饰
        Border border = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(
                24, 163, 83));
        content.setBorder(border);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 150);
        GUIUtil.centerFrameToFrame(this.getOwner(), this);
        
        displayProgress();
    }
    private Color getBackgroundColor()
    {
        return new Color(218, 221, 222); 
    }
    /**
     * 等待对话框中显示任务进度条
     *  
     */
    private void displayProgress() {
        JProgressBar progress = addProgressBar();

        progress.setStringPainted(true);
        progress.setIndeterminate(true);
        progress.setMaximum(0);
        progress.setValue(0);
    }

//    /**
//     * 隐藏进度条
//     *  
//     */
//    public void hiddenProgress() {
//        Component com = null;
//        if (centerPane.getComponentCount() > 0)
//            com = centerPane.getComponent(0);
//        if (com!=null&&com instanceof JProgressBar) {
//            centerPane.removeAll();
//            JPanel pane = (JPanel) this.getContentPane();
//            pane.validate();
//            pane.repaint();
//        }
//    }

    /**
     * 设置任务量的最大值
     * 
     * @param max
     */
    public void setTaskLength(int max) {
        JProgressBar progress = null;
        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com!=null&&com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return;
        }
        progress.setMaximum(max);
        progress.setIndeterminate(false);
    }
    /**
     * Get the task length.
     */
    public int getTaskLength()
    {
    	JProgressBar progress = null;
        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com!=null&&com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return -1;
        }
        
        return progress.getMaximum();
    }
    /**
     * 添加进度条
     * 
     * @return
     */
    private JProgressBar addProgressBar() {
        JProgressBar progress = null;
        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com == null || !(com instanceof JProgressBar)) {
            centerPane.removeAll();
            progress = new JProgressBar();
            centerPane.add(progress,BorderLayout.CENTER);

            JPanel pane = (JPanel) this.getContentPane();
            pane.validate();
            pane.repaint();
        }
        return progress;
    }

    /**
     * 设置进度，如果等待对话框中没有加载进度条，那么不作操作
     * 
     * @param value
     */
    public void setProgressValue(int value) {
        JProgressBar progress = null;

        Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return;
        }
        if (progress.isIndeterminate())
            return;
        progress.setValue(value);
        progress.setString(value * 100 / progress.getMaximum() + "%");
    }
    /**
     * Return the progress value. Return -1 if progress bar is indeterminate or there is no progress bar in the dialog.
     */
    public int getProgressValue()
    {
    	JProgressBar progress = null;
    	Component com = null;
        if (centerPane.getComponentCount() > 0)
            com = centerPane.getComponent(0);
        if (com instanceof JProgressBar) {
            progress = (JProgressBar) com;
        } else {
            return -1;
        }
        if(progress.isIndeterminate())
        	return -1;
        
        return progress.getValue();
        
    }
    /**
     * 重写方法
     */
    @Override
    public void dispose() {
        removeAll();
        quitActions.clear();
        super.dispose();
    }

    /**
     * 设置提示信息
     * 
     * @param info
     */
    public void setPrompt(String info) {
        label.setText(info);
    }

    /**
     * 添加取消事件处理
     * 
     * @param action
     */
    public void addQuitAction(Actionable action) {
        if (action != null)
            quitActions.add(action);
    }

    /**
     * 点击取消按钮后的事件处理
     *  
     */
    private void runQuit() {
        for (int i = 0; i < quitActions.size(); i++) {
            Actionable action = (Actionable) quitActions.get(i);
            action.action();
        }
    }
}
