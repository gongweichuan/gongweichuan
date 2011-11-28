/*
 * 创建日期 2006-6-2
 */
package com.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.coolsql.action.common.QuitAction;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.View;


/**
 * @author liu_xlin 对话框公共类
 */
public abstract class CommonFrame extends BaseDialog {
    private JPanel description = null;

    private JPanel buttonPanel = null;

    protected RenderButton preButton = null;

    protected RenderButton nextButton = null;

    protected RenderButton quitButton = null;

    protected RenderButton okButton = null;

    protected View view = null;

    public CommonFrame(View view) {
        super();
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JFrame f, boolean b, View view) {
        super(f, b);
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JDialog d, boolean b, View view) {
        super(d, b);
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JFrame f, String title, boolean b, View view) {
        super(f, title, b);
        this.view = view;
        initPub(initDialog());
    }

    public CommonFrame(JDialog f, String title, boolean b, View view) {
        super(f, title, b);
        this.view = view;
        initPub(initDialog());
    }

    public void initPub(JComponent content) {
        JLabel l = new JLabel();
        l.setOpaque(false);
        l.setBackground(Color.white);

        /**
         * 面板头，显示提示信息
         */
        description = new JPanel();
        description.setBackground(Color.white);
        description.setLayout(new BorderLayout());
        description.add("Center", l);
        description.setPreferredSize(new Dimension(400, 50));

        /**
         * 导航按钮面板
         */
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(400, 40));
        addButtons();

        /**
         * 内容，录入信息部分
         */
        JPanel tmp = new JPanel();
        tmp.setLayout(new BorderLayout());
        tmp.add("North", new JSeparator(SwingConstants.HORIZONTAL));
        content.setPreferredSize(getContentSize());
        tmp.add("Center", content);
        tmp.add("South", new JSeparator(SwingConstants.HORIZONTAL));

        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add("North", description);
        pane.add("Center", tmp);
        pane.add("South", buttonPanel);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        if(quitButton!=null)
           this.addWindowListener(new QuitAction(this));
        setDefaultSize();
        this.getRootPane().setDefaultButton(okButton);
        centerToScreen();
    }

    /**
     * 设置面板头的显示信息
     * 
     * @param s
     */
    public void setDescribeText(String s) {
        JLabel l = (JLabel) description.getComponent(0);
        l.setText(s);
    }

    /**
     * 设置面板头的图标
     * 
     * @param icon
     */
    public void setDescribeImage(Icon icon) {
        JLabel l = (JLabel) description.getComponent(0);
        l.setIcon(icon);
    }

    /**
     * 添加3个公共按钮 previous next quit
     *  
     */
    private void addButtons() {
        if (displayPreButton()) {
            preButton = new RenderButton(PublicResource
                    .getString("wizardbutton.previous"));
            preButton.setEnabled(false);
            buttonPanel.add(preButton);
        }
        if (displayNextButton()) {
            nextButton = new RenderButton(PublicResource
                    .getString("wizardbutton.next"));
            nextButton.setEnabled(false);
            buttonPanel.add(nextButton);
        }

        okButton = new RenderButton(PublicResource.getString("wizardbutton.finish"));
        okButton.setEnabled(false);
        buttonPanel.add(okButton);

        if (displayQuitButton()) {
            quitButton = new RenderButton(PublicResource
                    .getString("wizardbutton.quit"));

            buttonPanel.add(quitButton);
            quitButton.addActionListener(new QuitAction(this));
        }
    }

    /**
     * 是否显示上一步按钮
     * 
     * @return
     */
    protected boolean displayPreButton() {
        return true;
    }

    /**
     * 是否显示下一步按钮
     * 
     * @return
     */
    protected boolean displayNextButton() {
        return true;
    }

    /**
     * 是否显示取消按钮
     * 
     * @return
     */
    protected boolean displayQuitButton() {
        return true;
    }

    /**
     * 使上一步按钮有效
     * 
     * @param action
     */
    protected void enablePreButton(Action action, boolean b) {
        if(preButton==null)
            return;
        preButton.setEnabled(b);
        if (action != null)
            preButton.addActionListener(action);
    }

    /**
     * 使下一步按钮有效
     * 
     * @param action
     */
    protected void enableNextButton(Action action, boolean b) {
        if(nextButton==null)
            return;
        nextButton.setEnabled(b);
        if (action != null)
            nextButton.addActionListener(action);
    }

    /**
     * 使下一步按钮有效
     * 
     * @param action
     */
    public void enableOkButton(Action action, boolean b) {
        if(okButton==null)
            return;
        okButton.setEnabled(b);
        if (action != null)
            okButton.addActionListener(action);
    }

    /**
     * 对话框的默认窗口大小
     *  
     */
    public void setDefaultSize() {
        this.setSize(500, 430);
    }

    /**
     * 重写该方法，可以自定义内容窗口的大小
     * 
     * @return
     */
    protected Dimension getContentSize() {
        return new Dimension(400, 200);
    }

    /**
     * 居于屏幕中间
     *  
     */
    public void centerToScreen() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) ((d.getWidth() - this.getWidth()) / 2), (int) ((d
                .getHeight() - this.getHeight()) / 2), this.getWidth(), this
                .getHeight());
    }

    /**
     * 居于指定frame中间
     * 
     * @param f
     */
    public void centerToFrame(Window f) {
        Rectangle rect = f.getBounds();
        this
                .setBounds((int) (rect.getX() + (rect.getWidth() - this
                        .getWidth()) / 2), (int) (rect.getY() + (rect
                        .getHeight() - this.getHeight()) / 2), this.getWidth(),
                        this.getHeight());
    }

    /**
     * 居于指定frame中间
     * 
     * @param f
     */
    public void centerToFrame(JDialog f) {
        Rectangle rect = f.getBounds();
        this
                .setBounds((int) (rect.getX() + (rect.getWidth() - this
                        .getWidth()) / 2), (int) (rect.getY() + (rect
                        .getHeight() - this.getHeight()) / 2), this.getWidth(),
                        this.getHeight());
    }

    /**
     * 
     * 渲染控件字体,可选择使用
     *  
     */
    private void renderFont() {
        Font f = new Font(PublicResource.getString("default.font.family"),
                Integer
                        .parseInt(PublicResource
                                .getString("default.font.style")),
                Integer.parseInt(PublicResource.getString("default.font.size")));
        UIManager.put("TextField.font", f);
        UIManager.put("Label.font", f);
        UIManager.put("ComboBox.font", f);
        UIManager.put("MenuBar.font", f);
        UIManager.put("Menu.font", f);
        UIManager.put("ToolTip.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("Button.font", f);
    }

    /**
     * 缺省的字体设置，暂不用
     * 
     * @return
     */
    public Font getDefaultFont() {
        return new Font(PublicResource.getString("default.font.family"),
                Integer
                        .parseInt(PublicResource
                                .getString("default.font.style")),
                Integer.parseInt(PublicResource.getString("default.font.size")));
    }

    /**
     * @return 返回 view。
     */
    public View getView() {
        return view;
    }

    /**
     * 定义完成按钮的动作接口，便于数据的逻辑处理
     *  
     */
    public void shutDialogProcess(Object ob) {
    }

    /**
     * 定义下一步按钮点击后的处理接口
     * 
     * @param ob
     */
    public void nextButtonProcess(Object ob) {
    }

    /**
     * 定义上一步按钮点击后的处理接口
     * 
     * @param ob
     */
    public void preButtonProcess(Object ob) {
    }

    /**
     * 处理视图的接口定义
     * 
     * @author liu_xlin
     */
    public void processView() {
    }

    /**
     * 初始化对话框的内容面板接口
     * 
     * @return
     */
    public abstract JComponent initDialog();

    /**
     * 获取视图的上层JFrame窗口
     * 
     * @param view
     * @return
     */
    public static JFrame getParentFrame(Container view) {
        if (view == null)
            return GUIUtil.getMainFrame();
        Container con;
        for (con = view.getParent(); con != null && !(con instanceof JFrame); con = con
                .getParent())
            ;
        return (JFrame) con;
    }
    /**
     * 获取指定容器的顶层窗口对象
     * @param com  --执行容器
     * @return   --返回容器所在的窗口对象
     */
    public static Window getTopOwner(Container com)
    {
        Container con;
        for (con = com.getParent(); con != null && !(con instanceof Window); con = con
                .getParent())
            ;
        return (Window)con;
    }
    /**
     * 根据type类型获取crrent的上层容器
     * @param current
     * @param type  0: JFrame  else:JDialog获取其它
     * @return
     */
    public static Container getParentWindow(Container current,int type)
    {
        if (current == null)
            return null;
        Container con;
        for (con = current.getParent(); con != null; con = con
                .getParent())
        {
            if(type==0) //JFrame
            {
                if(con instanceof JFrame)
                    break;
            }else 
            {
                if(con instanceof JDialog)
                    break  ;
            }
        }
            
        return con;
    }
}
