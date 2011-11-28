/*
 * Created on 2007-5-15
 */
package com.coolsql.system.menubuild;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.coolsql.action.framework.CsAction;
import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.xml.XMLException;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.system.ActionCollection;
import com.coolsql.system.Setting;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 1、读取menu bar的配置信息，创建菜单栏。
 *         2、同时提供对扩展插件菜单的装载，装载按照一定的规则进行，插件类库必须在程序目录下的plugin目录下，并且必须创建扩展目录，
 *         在该目录下必须包含menu.xml文件，否则不予装载。
 */
public class MenuBuilder {
	/**
	 * 配置文件解析时，元素相关的信息不正确的类型
	 */
	public static final int ERROR_TYPE_ATTRIBUTE=0; //属性不正确
	public static final int ERROR_TYPE_ELEMENT=0; //元素名不正确
	
    private static MenuBuilder builder = null;
    /**
     * gui中的菜单栏对象
     */
    private JMenuBar mainBar = null;

    /**
     * 菜单排序对象
     */
    private MenuSorter sorter = null;

    private MenuBuilder() {
        mainBar = new JMenuBar();
        sorter = new MenuSorter();
    }

    /**
     * 创建该类为singleton类型
     * 
     * @return
     */
    public synchronized static MenuBuilder getInstance() {
        if (builder == null)
            builder = new MenuBuilder();
        return builder;
    }

    /**
     * 装载系统菜单
     *  
     */
    public void loadSystemMenu() {
        try {
            List sysMenu = parse("com/coolsql/resource/systemmenu.xml","com.coolsql.resource.menuresource");
            for (int i = 0; i < sysMenu.size(); i++) {
                mainBar.add((JMenu) sysMenu.get(i));
            }
        } catch (XMLException e) {
            //            e.printStackTrace();
            LogProxy.errorReport(e);
        }
    }
    private Properties getMenuResource(String baseName)
    {
    	return PublicResource.getPropertiesAssociateWithLocal(baseName, Locale.getDefault());
    }
    /**
     * 该方法对给定的文件进行解析，并返回JMenu类型对象的列表，该列表按照配置文件中定义的位置属性进行排列。
     * 在第一层上不允许出现menuItem类型的元素标记。
     * 
     * @param path
     *            --配置文件的路径
     * @return
     * @throws XMLException
     */
    private List parse(String path,String resourcePath) throws XMLException {
    	
    	Properties menuResource=getMenuResource(resourcePath);
    	VelocityContext vc=new VelocityContext(menuResource);
    	String xmlCode;
    	try {
			Template template=Velocity.getTemplate(path,System.getProperty("file.encoding"));
			StringWriter sw=new StringWriter();
			template.merge(vc, sw);
			xmlCode=sw.toString();
		}catch (Exception e1) {
			throw new XMLException("Loading menu resource failed!",e1);
		}
    	
    	
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(new StringReader(xmlCode));
        } catch (JDOMException e) {
            throw new XMLException(PublicResource
                    .getSystemString("system.menubar.loadconfigfile.loaderror")
                    + e.getMessage());
        } catch (IOException e) {
        	throw new XMLException("io error!",e);
		}
        Element root = doc.getRootElement();
        if (!root.getName().toLowerCase().equalsIgnoreCase(MenuXMLConstants.TAG_MENUS))
            throw new XMLException(
                    PublicResource
                            .getSystemString("system.menubar.loadconfigfile.parseerror.rootelementerror")
                            + root.getName());
        checkMenusTagLegal(root);
        /**
         * parse resource tag
         */
        Element resourceElement=root.getChild(MenuXMLConstants.TAG_RESOURCE);
        if(resourceElement!=null)
        {
        	String resourceFile=StringUtil.trim(resourceElement.getText());
        	if(!resourceFile.equals(""))
        	{
        		menuResource=LoadJar.loadResource(resourceFile, this.getClass());
        	}
        }
        
        List children = root.getChildren();
        List<MenuElement> menuList = new ArrayList<MenuElement>();

        for (int i = 0; i < children.size(); i++) {
            Element e = (Element) children.get(i);
            String type = e.getName();
            if (!type.toLowerCase().equalsIgnoreCase(MenuXMLConstants.TAG_MENU)) //如果顶层元素为菜单项标记，抛出异常提示
                continue;

            //            String label = e
            //                    .getAttributeValue(MenuXMLConstants.ATTRIBUTE_LABEL);
            //            if (StringUtil.trim(label).equals("")) //如果菜单中未指定标签属性，抛出异常提示
            //                throw new XMLException(
            //                        PublicResource
            //                                .getSystemString("system.menubar.loadconfigfile.parseerror.nolabel")
            //                                + MenuXMLConstants.ATTRIBUTE_LABEL);

            JMenuItem m = parseMenu(e);
            if (m != null) {
                menuList.add(new MenuElement(getElementLoaction(e), m));
            }
        }

        if (menuList.size() > 0) {
            Collections.sort(menuList, sorter);
            List realList = new ArrayList();
            for (int i = 0; i < menuList.size(); i++) {
                MenuElement me = (MenuElement) menuList.get(i);
                realList.add(me.com);
            }
            menuList.clear();
            children.clear();
            return realList;
        }
        return menuList;
    }
    /**
     * validate whether all children are legal.
     * @param e --menus tag element object
     * @throws XMLException  --it'll throw this kind of exception if any child element is illegal.
     */
    private void checkMenusTagLegal(Element e) throws XMLException
    {
    	if(!e.getName().equalsIgnoreCase(MenuXMLConstants.TAG_MENUS))
    		return;
    	//check all children type
    	Iterator it =e.getChildren().iterator();
    	while(it.hasNext())
    	{
    		Element c=(Element)it.next();
    		if(c.getName().equalsIgnoreCase(MenuXMLConstants.TAG_MENU)||
    				c.getName().equalsIgnoreCase(MenuXMLConstants.TAG_RESOURCE))
    			continue;
    		else
    			throw new XMLException("subelement of \"menus\" is illegal,error type is:"+c.getName()); //NOI18N
    	}
    	
    }
    /**
     * 检查菜单元素之属性的合法性，目前只针对属性label,icon,loaction,loadlistener,clientproperty进行检查
     * @param e Menu元素
     * @throws XMLException
     */
    private void checkMenuLegal(Element e) throws XMLException
    {
    	if(!e.getName().equalsIgnoreCase(MenuXMLConstants.TAG_MENU))
    		return;
    	List attris=e.getAttributes();
    	for(int i=0;i<attris.size();i++)
    	{
    		if(((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_LABEL)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_LOADLISTENER)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_LOCATION)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_ICON)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_CLIENTPROPERTY)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_MNEMONIC))
    			continue;
    		else
    			throw new XMLException("property of element(menu) is illegal,error property is "+((Attribute)attris.get(i)).getName());
    	}
    }
    /**
     * 校验icon元素的正确性
     * @param e
     * @throws XMLException
     */
    private void checkIconElementLegal(Element e) throws XMLException {
		if (!e.getName().equalsIgnoreCase(MenuXMLConstants.TAG_ICON))
			return;
		if (e.getChildren().size() > 0)
			throw new XMLException(
					" element(icon) must not have any sub element ");

		List attris = e.getAttributes();
		for (int i = 0; i < attris.size(); i++) {
			if (((Attribute) attris.get(i)).getName().equalsIgnoreCase(
					MenuXMLConstants.ATTRIBUTE_ICON_TYPE)) {
				continue;
			} else
				throw new XMLException(
						"property of element(icon) is illegal,error property is:"
								+ ((Attribute) attris.get(i)).getName());
		}
	}
    /**
     * 检查菜单或菜单项元素之属性的合法性，目前只针对属性label,type,icon,loaction,loadlistener,clientproperty,isregister进行检查
     * @param e Menu元素
     * @throws XMLException
     */
    private void checkMenuItemLegal(Element e) throws XMLException
    {
    	if(!e.getName().equalsIgnoreCase(MenuXMLConstants.TAG_MENUITEM))
    		return;
    	List attris=e.getAttributes();
    	for(int i=0;i<attris.size();i++)
    	{
    		if(((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_LABEL)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_LOADLISTENER)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_LOCATION)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_ICON)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_TYPE)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_CLIENTPROPERTY)||
//    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_ISREGISTER)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_MNEMONIC)||
    				((Attribute)attris.get(i)).getName().equalsIgnoreCase(MenuXMLConstants.ATTRIBUTE_DISABLED))
    			continue;
    		else
    			throw new XMLException("property of menu(menuitem) is illegal,error property is "+((Attribute)attris.get(i)).getName());
    	}
    }
    /**
     * 检验menuitem子元素是否合法。
     * @param e menuitem元素
     * @throws XMLException
     */
    private void checkMenuItemChildren(Element e) throws XMLException
    {
    	if(!e.getName().equalsIgnoreCase(MenuXMLConstants.TAG_MENUITEM))
    		return;
    	Iterator it =e.getChildren().iterator();
    	while(it.hasNext())
    	{
    		Element c=(Element)it.next();
    		if(c.getName().equalsIgnoreCase(MenuXMLConstants.TAG_TOOLTIP)||
    				c.getName().equalsIgnoreCase(MenuXMLConstants.TAG_ACTIONLISTENER)||
    						c.getName().equalsIgnoreCase(MenuXMLConstants.TAG_SHORTCUT)||
    								c.getName().equalsIgnoreCase(MenuXMLConstants.TAG_ENABLECHECK)||
    								c.getName().equalsIgnoreCase(MenuXMLConstants.TAG_ICON))
    			continue;
    		else
    			throw new XMLException("subelement of menuitem is illegal,error type is:"+c.getName());
    	}
    	
    }
    /**
     * 菜单在加载是将执行其定义的loadlistener属性指定的类
     * @param listenerName --加载监听类名
     * @param menu 已经被加载完成的菜单对象
     * @throws XMLException
     */
    private void executeLoadListener(String listenerName,JMenuItem menu) throws XMLException {
		try {
			Class c = LoadJar.getInstance().getClassLoader().loadClass(
					listenerName);
			if (!IMenuLoadListener.class.isAssignableFrom(c)) // 如果类型不是ActionListener类型，抛出异常进行提示
			{
				throw new XMLException(
						PublicResource
								.getSystemString("system.menubar.loadconfigfile.parseerror.listenerinvalid")
								+ listenerName);
			}
			IMenuLoadListener listener=(IMenuLoadListener)c.newInstance();
			listener.action(menu);
		} catch (ClassNotFoundException e1) {
			throw new XMLException(
					PublicResource
							.getSystemString("system.menubar.loadconfigfile.parseerror.notfindlistener")
							+ listenerName);
		} catch (InstantiationException e1) {
            throw new XMLException(
                    PublicResource
                            .getSystemString("system.menubar.loadconfigfile.parseerror.instanceListenererror")
                            + listenerName);
        } catch (IllegalAccessException e1) {
            throw new XMLException(
                    PublicResource
                            .getSystemString("system.menubar.loadconfigfile.parseerror.instanceListenererror")
                            + listenerName);
        }
	}
    /**
	 * 该方法将对给定的元素对象进行解析，实现了对菜单嵌套的解析，并且设定了相关规则。 1、如果为菜单项必须指定正确的事件监听类.
	 * 
	 * @param e
	 * @return
	 * @throws XMLException
	 */
    private JMenuItem parseMenu(Element e) throws XMLException {
        String name = e.getName();        	
        
        if (name.toLowerCase().equalsIgnoreCase(MenuXMLConstants.TAG_MENUITEM)) //如果为菜单项
        {
        	checkMenuItemLegal(e);
        	checkMenuItemChildren(e);
        	
        	 CsAction csAction=null;
             
// 			String isRegisterStr = e
// 						.getAttributeValue(MenuXMLConstants.ATTRIBUTE_ISREGISTER);
// 				if (isRegisterStr == null) // default is true.
// 					isRegisterStr = "true";
// 				if (StringUtil.trim(isRegisterStr).toLowerCase()
// 						.equals("false"))
// 					isRegisterStr = "false";
// 				else
// 					isRegisterStr = "true";

             //actionListener
             Element al = e.getChild(MenuXMLConstants.TAG_ACTIONLISTENER);
             Action listener = null;
             if (al != null) {
                 String alText = al.getTextTrim();
                 try {
                     Class c =LoadJar.getInstance().getClassLoader().loadClass(
                             alText);
                     if (!Action.class.isAssignableFrom(c)) //如果类型不是Action类型，抛出异常进行提示
                     {
                         throw new XMLException(
                                 PublicResource
                                         .getSystemString("system.menubar.loadconfigfile.parseerror.listenerinvalid")
                                         + alText);
                     }
                     
                     if (!CsAction.class.isAssignableFrom(c)) {
 						listener = ActionCollection.getInstance().get(c);
 						csAction=new CsAction();
 						csAction.setOriginal(new CsAction(listener));
 					} else
 					{
// 						if(isRegisterStr.equals("false"))
// 							csAction=(CsAction)ActionCollection.getInstance().get(c);
// 						else
 						csAction=Setting.getInstance().getShortcutManager().getActionByClass(c);
 					}
                     	
                 } catch (ClassNotFoundException e1) {
                     throw new XMLException(
                             PublicResource
                                     .getSystemString("system.menubar.loadconfigfile.parseerror.notfindlistener")
                                     + alText);
                 }
             } else
                 throw new XMLException(
                         PublicResource
                                 .getSystemString("system.menubar.loadconfigfile.parseerror.nolistener"));
        	 
            //menu type
            if(csAction.getType()==null)
            {
	            String type = e.getAttributeValue(MenuXMLConstants.ATTRIBUTE_TYPE);
	            
	            if (type == null)
	                type = "plain";
	            else
	                type = StringUtil.trim(type).toLowerCase();
	            
	            csAction.setType(type);
            }
            
            //label
            String label = e
                    .getAttributeValue(MenuXMLConstants.ATTRIBUTE_LABEL);
            if(label!=null)
            	csAction.setMenuText(label);

            Icon icon=null;
            if(csAction.getIcon()==null)
            {
            	//icon by property
            	icon=getMenuIconByProperty(e);
            	
                //icon by sub element
                if (icon == null)
    			{			
    				icon=getMenuIconByTag(e);
    			}
                if(icon instanceof ImageIcon)
                	csAction.setIcon((ImageIcon)icon);
            }
            
            //initalizing isenable 
            String disabled = e
                    .getAttributeValue(MenuXMLConstants.ATTRIBUTE_DISABLED);
            if (disabled != null&&csAction.isEnabled()) {
				if (StringUtil.trim(disabled).toLowerCase().equals("true"))
					csAction.setEnabled(false);
				else
					csAction.setEnabled(true);
			}

            // tooltip
            Element tt = e.getChild(MenuXMLConstants.TAG_TOOLTIP);
            if (tt != null) {
                String tipText = tt.getTextTrim();
                csAction.setTooltip(tipText);
            }
            //shortcut
            Element sc = e.getChild(MenuXMLConstants.TAG_SHORTCUT);
            if (sc != null) {
                String scText = sc.getTextTrim();
                KeyStroke ks = KeyStroke.getKeyStroke(scText);
                if (ks != null) {
                	csAction.setDefaultAccelerator(ks);
                	csAction.setAccelerator(ks);
                }

            }
        	
            JMenuItem item=csAction.getMenuItem();
            //loadlistener
            String loadListener =e.getAttributeValue(MenuXMLConstants.ATTRIBUTE_LOADLISTENER);
            if(loadListener!=null)
            	executeLoadListener(loadListener.trim(),item);
            
//            boolean isEnable=csAction.isEnabled();
//            csAction.setEnabled(true);
//            item.setEnabled(isEnable);
           
            return item;
        } else if (name.toLowerCase().equalsIgnoreCase(MenuXMLConstants.TAG_MENU)) //如果为菜单
        {
        	checkMenuLegal(e);
            JMenu item = new JMenu();

            //标签
            String label = e
                    .getAttributeValue(MenuXMLConstants.ATTRIBUTE_LABEL);
            item.setText(label);
            //icon by property
            Icon icon=getMenuIconByProperty(e);
            if(icon!=null)
            	item.setIcon(icon);
            
            List children = e.getChildren();
            List menuList = new ArrayList();
            
            String mnemonic=e.getAttributeValue(MenuXMLConstants.ATTRIBUTE_MNEMONIC);
            if(mnemonic!=null)
            	item.setMnemonic(mnemonic.charAt(0));
            /**
             * 将clientproperty属性在之前设置，在后续的属性处理中可能需要使用该属性
             */
            //clientproperty
//            String clientpro=e.getAttributeValue(MenuXMLConstants.ATTRIBUTE_CLIENTPROPERTY);
//            if(clientpro!=null)
//            	item.putClientProperty(MenuXMLConstants.MENU_CLIENTPROPERTY_NAME, clientpro);
            for (int i = 0; i < children.size(); i++) {
                Element c = (Element) children.get(i);
                String cName = c.getName().trim().toLowerCase();

                if (cName.equalsIgnoreCase(MenuXMLConstants.TAG_MENUITEM)) {

                    JMenuItem tmpItem = parseMenu(c);
                    if (tmpItem == null)
                        continue;
                    menuList.add(new MenuElement(getElementLoaction(e), tmpItem));
                } else if (cName.equalsIgnoreCase(MenuXMLConstants.TAG_ENABLECHECK)) //菜单同样也可以拥有可用性校验
                {
                    String alText = c.getTextTrim();
                    MenuItemEnableCheck checker = getEnableCheck(alText);
                    item.putClientProperty(MenubarAvailabilityManage.CHECKER,
                            checker);
                } else if (cName.equalsIgnoreCase(MenuXMLConstants.TAG_SEPARATOR)) //如果为分割线元素
                {
                    menuList.add(new MenuElement(getElementLoaction(e),new JPopupMenu.Separator()));
                }else if(cName.equalsIgnoreCase(MenuXMLConstants.TAG_MENU)) //如果为菜单元素，可以通过递归的形式进行解析
                {
                	menuList.add(new MenuElement(getElementLoaction(e), parseMenu(c)));
                }else if(cName.equalsIgnoreCase(MenuXMLConstants.TAG_ICON))
                {
                    //icon
                    if (icon == null) // 过在元素属性中已经定义了icon，忽略子元素<icon>中定义的图标
        			{			
        				icon=getMenuIconByTag(e);
        			}
                    item.setIcon(icon);
                }
            }

            //排序
            if (menuList.size() > 0) {
                Collections.sort(menuList, sorter);

                for (int i = 0; i < menuList.size(); i++) {
                    MenuElement me = (MenuElement) menuList.get(i);
                    item.add(me.com);
                }
                menuList.clear();
                children.clear();

            }
            String loadListener =e.getAttributeValue(MenuXMLConstants.ATTRIBUTE_LOADLISTENER);
            if(loadListener!=null)
            	executeLoadListener(loadListener.trim(),item);
            return item;
        } else
            //其他类型的标记不做处理
            return null;

    }
    /**
     * 获取菜单或者分隔线的位置(location)属性
     * @param e --菜单或者分隔线元素对象
     * @return 如果未定义，返回父菜单的最后一个位置
     */
    private int getElementLoaction(Element e) {
		String location = e
				.getAttributeValue(MenuXMLConstants.ATTRIBUTE_LOCATION);
		int l = -1;
		if (location != null) {
			location = location.trim();
			try {
				l = Integer.parseInt(location);
			} catch (NumberFormatException ex) {
				l = -1;
			}
		}
		 if (l == -1)
		 l = e.getParent().getContentSize();
		 
		 return l;
	}
    /**
     * 从菜单或者菜单项的属性中获取其对应的图标信息，如果没有定义，将返回null
     * @param e  菜单或者菜单项元素对象
     * @return Icon 如果没有定义，将返回null
     */
    private Icon getMenuIconByProperty(Element e)
    {
		String iconPath = e
		.getAttributeValue(MenuXMLConstants.ATTRIBUTE_ICON);
		Icon icon=null;
		if(iconPath!=null)
		{
			if(iconPath.equals(MenuXMLConstants.BLANK))
				return IconResource.getBlankIcon();
			icon= IconResource.getIcon(iconPath);
		}
		return icon;
    }
    /**
     * 从菜单或者菜单项的子元素中获取icon信息
     * @param e 菜单或者菜单项元素对象
     * @return
     * @throws XMLException 
     */
    private Icon getMenuIconByTag(Element e) throws XMLException
    {
    	if(!e.getName().equalsIgnoreCase(MenuXMLConstants.TAG_MENU)&&
    			!e.getName().equalsIgnoreCase(MenuXMLConstants.TAG_MENUITEM))
    		return null;
    	
    	Element eIcon=e.getChild(MenuXMLConstants.TAG_ICON);
    	Icon icon = null;
    	if (eIcon != null) {
    		checkIconElementLegal(eIcon);
    		String iconPath = StringUtil.trim(eIcon.getText()); // 获取icon元素的值
    		if(iconPath.equals(MenuXMLConstants.BLANK))
    			return IconResource.getBlankIcon();
			String type = e
					.getAttributeValue(MenuXMLConstants.ATTRIBUTE_ICON_TYPE); // 获取icontype的属性值
			
			
			if (type == null || type.equals("resource")) // 从资源绑定中获取icon信息
				icon = IconResource.getIcon(iconPath);
			else if (type.equals("file"))
				icon = IconResource.getLocalIcon(iconPath);
		}
    	
    	return icon;
		
    }
    /**
	 * 获取菜单校验对象
	 * 
	 * @param alText
	 * @return
	 * @throws XMLException
	 */
    private MenuItemEnableCheck getEnableCheck(String alText)
            throws XMLException {
        try {
            Class c = LoadJar.getInstance().getClassLoader().loadClass(alText);
            if (!MenuItemEnableCheck.class.isAssignableFrom(c)) //如果类型不是MenuItemEnableCheck类型，抛出异常进行提示
            {
                throw new XMLException(
                        PublicResource
                                .getSystemString("system.menubar.loadconfigfile.parseerror.checkerinvalid")
                                + alText);
            }
            return (MenuItemEnableCheck) c.newInstance();

        } catch (ClassNotFoundException e1) {
            throw new XMLException(
                    PublicResource
                            .getSystemString("system.menubar.loadconfigfile.parseerror.notfindchecker")
                            + alText);
        } catch (InstantiationException e1) {
            throw new XMLException(
                    PublicResource
                            .getSystemString("system.menubar.loadconfigfile.parseerror.instancecheckererror")
                            + alText);
        } catch (IllegalAccessException e1) {
            throw new XMLException(
                    PublicResource
                            .getSystemString("system.menubar.loadconfigfile.parseerror.instancecheckererror")
                            + alText);
        }
    }

    /**
     * 为方便排列菜单位置，定义此类
     * 
     * @author liu_xlin
     *  
     */
    private class MenuElement {
        int location;

        JComponent com;

        public MenuElement(int location, JComponent com) {
            this.location = location;
            this.com = com;
        }
    }

    /**
     * 按照菜单位置进行排序。
     * 
     * @author liu_xlin
     */
    class MenuSorter implements Comparator {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
            MenuElement e1 = (MenuElement) o1;
            MenuElement e2 = (MenuElement) o2;

            return e1.location - e2.location;
        }

    }

    /**
     * @return Returns the mainBar.
     */
    public JMenuBar getMenuBar() {
        return mainBar;
    }
}
