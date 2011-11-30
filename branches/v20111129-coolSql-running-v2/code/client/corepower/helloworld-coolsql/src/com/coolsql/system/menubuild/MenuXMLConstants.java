/*
 * Created on 2007-5-15
 */
package com.coolsql.system.menubuild;

/**
 * @author liu_xlin
 *定义了菜单配置文件中的元素名称
 */
public class MenuXMLConstants {
	public static final String MENU_CLIENTPROPERTY_NAME="name"; 
	
	/**
     * root标记
     */
    public static final String TAG_MENUS="menus";
    
    /**
     * resource tag ,locationing resource file in which menubuilder find all resources
     */
    public static final String TAG_RESOURCE="resource";
    /**
     * 菜单标记
     */
    public static final String TAG_MENU="menu";
    
    /**
     * 菜单项标记
     */
    public static final String TAG_MENUITEM="menuitem";
    
    /**
     * 菜单项的提示标记名
     */
    public static final String TAG_TOOLTIP="tooltip";
    
    /**
     * 菜单项事件监听类
     */
    public static final String TAG_ACTIONLISTENER="action";
    /**
     * 菜单项快捷键定义
     */
    public static final String TAG_SHORTCUT="shortcut";
    /**
     * 校验菜单项的可用性
     */
    public static final String TAG_ENABLECHECK="enablecheck";
    
    /**
     * 菜单的分割线
     */
    public static final String TAG_SEPARATOR="separator";
    
    /**
     * 菜单/菜单项的图标元素
     */
    public static final String TAG_ICON="icon";
    
    /**
     * 菜单项或菜单的标签属性
     */
    public static final String ATTRIBUTE_LABEL="label";
    
    public static final String ATTRIBUTE_MNEMONIC="mnemonic";
    /**
     * 菜单项的位置属性，用于菜单/菜单项的位置排序
     */
    public static final String ATTRIBUTE_LOCATION="location";
    /**
     * Indicate whether register CsAction to ShortcutManager.
     */
//    public static final String ATTRIBUTE_ISREGISTER="isregister";
    /**
     * 菜单/菜单项初始是否可用
     */
    public static final String ATTRIBUTE_DISABLED="disabled";
    
    /**
     * 菜单/菜单项的图标
     */
    public static final String ATTRIBUTE_ICON="icon";
    public static final String ATTRIBUTE_ICON_TYPE="type"; //icon元素的属性：resource(资源绑定的文件资源),file(从本地文件中获取的资源)
    /**
     * 菜单、菜单项的内置属性JComponent.putClientProperty(),对应的名字为"name"
     */
    public static final String ATTRIBUTE_CLIENTPROPERTY="clientproperty";
    
    /**
     * 菜单项类型
     * 1、普通菜单项(JMenuItem) :plain
     * 2、单选菜单项(JRadioButtonMenuItem)  :radio
     * 3、复选菜单项(JCheckBoxMenuItem)   :check
     */
    public static final String ATTRIBUTE_TYPE="type";
    
    /**
     * 菜单加载时，可设置监听器，该监听器必须实现ActionListener接口，该属性可用于menu和menuitem两种类型的元素
     */
    public static final String ATTRIBUTE_LOADLISTENER="loadlistener";
    
    /**
     * 用于icon属性和icon元素的内容值判断
     */
    public static final String BLANK="blank";
}
