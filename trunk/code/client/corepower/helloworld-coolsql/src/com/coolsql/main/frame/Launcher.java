/*
 * Created on 2006-6-8
 *
 */
package com.coolsql.main.frame;

import java.awt.Frame;
import java.util.Locale;

import com.coolsql.pub.component.BaseFrame;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.system.PropertyManage;
import com.coolsql.system.SplashWindow;
import com.coolsql.system.TaskFactory;
import com.coolsql.system.lookandfeel.SystemLookAndFeel;

/**
 * System launcher.
 * @author liu_xlin
 */
public class Launcher {
    /**
     * Status flag indicate that it means application is initializing when value is false,
     *  and initialization has been finished when true.
     */
    public static boolean isInitializing = false;
	public static void main(String[] args) {
	    isInitializing=true;
	    
	    Locale defaultLocale=Locale.getDefault();
	    String value=PropertyManage.getLaunchSetting().getLanguage();
	    if(value!=null)
	    {
	    	Locale.setDefault(new Locale(value,defaultLocale.getCountry()));
	    }
	    
	    SystemLookAndFeel.getInstance().installLookAndFeel(SystemLookAndFeel.getInstance().getCurrentLAFName());
	    
	    SplashWindow splash=new SplashWindow(getTemporaryFrame(),TaskFactory.getLaunchTasks(),null);
	    splash.setVisible(true);
	    splash.toFront();
	    splash.start();
	    splash.dispose();
	    
	    isInitializing=false;
	    GUIUtil.getMainFrame().setVisible(true);
	    GUIUtil.getMainFrame().toFront();
	}
	private static Frame getTemporaryFrame()
	{
	    return new BaseFrame();
	}
}
