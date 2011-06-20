/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.mainframe;

import java.awt.Component;

import org.jdom.Element;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 * <p>文件名称：MainFrameService.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-2</p>
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
public interface MainFrameService
{
    public abstract JFrame getContainerFrame(String s);

    public abstract void openClientView(String s);

    public abstract int synOpenClientView(String s);

    public abstract int getLoginState();

    public abstract boolean hasDeployed(String s);

    public abstract void refreshMenus(String s, JMenu ajmenu[]);

    public abstract void refreshToolButtons(String s, JComponent ajcomponent[]);

    public abstract JMenu getMenu(String s, String s1);

    public abstract JMenu[] getMenus(String s);

    public abstract JComponent[] getTools(String s);

    public abstract void requestActionEnabled(String s, String s1, boolean flag);

    public abstract void requestActionSelected(String s, String s1, boolean flag);

    public abstract void requestActionInvoked(String s, String s1);

    public abstract void destroyClientView(String s);

    public abstract void writeHintMessage(String s, String s1);

    public abstract void writeErrorMessage(String s, String s1);

    public abstract void writeStatusMessage(String s, String s1, boolean flag);

    public abstract void requestTabHideableEnabled(String s, boolean flag);

    public abstract void showHelp(Component component, String s);

    public abstract Element getProfileElement(String s);

    public abstract void saveProfileElement(String s, Element element);

    public abstract JToolBar getMainToolBar();

    public abstract JTabbedPane getTabbedPane();

    public abstract void registerFrame(JFrame jframe);

    public abstract void unregisterFrame(JFrame jframe);

    public abstract void hideClientView(String s);

    //TODO 打印相关
//    public abstract PageFormat getSavedPageFormat();

    public abstract void showDynamicClientView(String s, String s1, ImageIcon imageicon, Class class1);

    public abstract void addHiddenStatusListener(HiddenStatusListener hiddenstatuslistener);

    public abstract void removeHiddenStatusListener(HiddenStatusListener hiddenstatuslistener);

    public abstract boolean getHiddenStatus();

    public abstract void addHelpFiles(String s, String as[]);

    public abstract void removeHelpFiles(String s);

    public abstract void refreshAppToolBtnBar(String s, JComponent jcomponent);

    public abstract void refreshAppViewMenuItem(String s, JMenuItem ajmenuitem[]);

    public abstract void refreshAppToolMenuItem(String s, JMenuItem ajmenuitem[]);

    public static final String ROLE = "mainframe-service";

}
