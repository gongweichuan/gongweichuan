package com.chinaviponline.erp.pal.secure.impl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.chinaviponline.erp.pal.secure.i.ICreateJksFileJPanel;

/**
 * <p>文件名称：TCreateJksFileJPanel.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2012-4-20</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：

 *  版本号：
 *  修改人：
 *  修改内容：

 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川

 * @email  gongweichuan(AT)gmail.com
 */
public class TCreateJksFileJPanel implements ICreateJksFileJPanel
{
  //keytool -genkey -dname "CN=Pingan Bank, OU=Pingan, O=Pingan, L=Shenzhen, ST=Shenzhen, C=CN" 
    //-alias pinganmer -keyalg RSA -keysize 1024 -keystore pinganmer.jks -keypass 12345678 -storepass 12345678 -validity 365
    
    private String dName="CN=Pingan Bank, OU=Pingan, O=Pingan, L=Shenzhen, ST=Shenzhen, C=CN";
    private String alias="pinganmer";
    private String keystore="gg.jks";
    private String keypass="gowithchallenger";
    private String validity="365";
    
    private JPanel mainPanel;//主面板
    private JLabel labDName;
    private JTextField  txtDName;
    private JLabel labAlias;
    private JTextField txtAlias;
    private JLabel labPass;
    private JLabel labPass2;
    private JPasswordField pasPassInput;
    private JPasswordField pasPassConfirm;
    private JLabel labValidity;
    private JTextField txtValidity;
    
    private JLabel labKeytool;
    private JTextField txtKeytool;
    private JButton butdotdotdot;
    private JFileChooser fileKeytool;
    
    
    
    private JButton butOk;
    private void initData()
    {
        mainPanel=new JPanel();
        labDName=new JLabel("组织");
        txtDName=new JTextField("CN=Pingan Bank, OU=Pingan, O=Pingan, L=Shenzhen, ST=Shenzhen, C=CN");
        labAlias=new JLabel("Alias");
        txtAlias=new JTextField("pinganmer");
        labPass=new JLabel("密码");
        labPass2=new JLabel("重复密码");
        pasPassInput=new JPasswordField();
        pasPassConfirm=new JPasswordField();
        labValidity=new JLabel("有效期（天）");
        txtValidity=new JTextField();
        
        labKeytool=new JLabel("keytool.exe位置");
        txtKeytool=new JTextField();
        butdotdotdot=new JButton("...");
        fileKeytool=new JFileChooser();
        
        
        
        butOk=new JButton("确认");
        
    }
    
    private void initGUI()
    {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder("RSA"));
        GridBagConstraints c = new GridBagConstraints();
        
        mainPanel.add(labDName, new GridBagConstraints(1,1,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        mainPanel.add(txtDName, new GridBagConstraints(4,1,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        
        mainPanel.add(labAlias, new GridBagConstraints(1,2,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        mainPanel.add(txtAlias, new GridBagConstraints(4,2,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        
        mainPanel.add(labPass, new GridBagConstraints(1,3,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        mainPanel.add(pasPassInput, new GridBagConstraints(4,3,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        
        mainPanel.add(labPass2, new GridBagConstraints(1,4,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        mainPanel.add(pasPassConfirm, new GridBagConstraints(4,4,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        
        mainPanel.add(labValidity, new GridBagConstraints(1,5,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        mainPanel.add(txtValidity, new GridBagConstraints(4,5,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        
        mainPanel.add(labKeytool,new GridBagConstraints(1,6,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        mainPanel.add(txtKeytool,new GridBagConstraints(4,6,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        mainPanel.add(butdotdotdot,new GridBagConstraints(7,6,1,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        
        //mainPanel.add(fileKeytool,new GridBagConstraints(4,6,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
        
        mainPanel.add(butOk,new GridBagConstraints(4,7,3,1,0.4,0.4,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(4,4,4,4),4,2));
    }
    
    private void initListeners()
    {
        butdotdotdot.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                //JOptionPane.showMessageDialog(null, "请选择keytool.exe文件!");
                int result = fileKeytool.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION)
                {

                /*获得你选择的文件绝对路径。并输出。当然，我们获得这个路径后还可以做很多的事。*/
                   String path = fileKeytool.getSelectedFile().getAbsolutePath();
                   //System.out.println(path);
                   txtKeytool.setText(path);
                }
                else
                {
                    //System.out.println("你已取消并关闭了窗口！");
                    JOptionPane.showMessageDialog(null, "请选择keytool.exe文件!");
                   }
                }
        });
        
        butOk.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                TCreateJksFile createJKS=new TCreateJksFile();
                Map<String, String> map=new HashMap<String, String>();
                map.put("keytool", txtKeytool.getText());
                map.put("dir", "D:\\");
                map.put("dname", txtDName.getText());
                map.put("alias",txtAlias.getText());
                map.put("keyalg", "RSA");
                map.put("keysize", "1024");
                map.put("keysotre", txtAlias.getText()+".jks");
                //JOptionPane.showMessageDialog(null, new String(pasPassInput.getPassword()));
                map.put("keypass",new String(pasPassInput.getPassword()));
                map.put("storepass",new String(pasPassInput.getPassword()));
                map.put("validity", txtValidity.getText());
                map.put("file", txtAlias.getText()+".cer");
                
                Boolean b=createJKS.createJksFile(map);
                JOptionPane.showMessageDialog(null, "执行结果:"+b);
            }
        });
    }
    
    
    public void init()
    {
        initData();
        initGUI();
        initListeners();
    }
    
    
    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel)
    {
        this.mainPanel = mainPanel;
    }

    /**
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2012-4-20</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
    
     *  修改内容：
    
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param args
     */
    public static void main(String[] args)
    {
        TCreateJksFileJPanel p=new TCreateJksFileJPanel();
        p.init();
        JFrame frame=new JFrame("Test");
        frame.getContentPane().add(p.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        //frame.show();
        frame.setVisible(true);

    }

}
