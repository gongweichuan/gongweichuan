/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tomcat;

import javax.swing.Action;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Embedded;
import org.apache.log4j.Logger;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

/**
 * <p>文件名称：EmbeddedTomcatFrame.java</p>
 * <p>文件描述：嵌入式Tomcat</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-14</p>
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
public class EmbeddedTomcatFrame
{
    private static final Logger log = Logger
            .getLogger(EmbeddedTomcatFrame.class);//日志

    private static final String LOCALIP = "127.0.0.1";//localhost

    private static final String APPNM = "/webserver";//app path

    private static final String APPCONTEXT = "/webserver";//app name

    private static final String LOCALHOST = "localhost";

    private static final String WEBAPPS = "/webapps";//Tomcat默认path

    private static final String WEBROOT = "/webapps/ROOT";//Tomcat默认APP

    private Embedded tomcat = null;

    private boolean isBoot = false;

    private Action stopAction = null;

    private Action startAction = null;

    private String tomcatEmbedDir = "tomcatembed";//安装目录

    public Action getStartAction()
    {
        return startAction;
    }

    public void setStartAction(Action startAction)
    {
        startAction = startAction;
    }

    public Action getStopAction()
    {
        return stopAction;
    }

    public void setStopAction(Action stopAction)
    {
        stopAction = stopAction;
    }

    public void start()
    {
        String path = tomcatEmbedDir;   //Activator.getPlugPath("tomcat-embed");

        Embedded tc = createTomcat(path);
        log.debug("Tomcat Server start.");
        try
        {
            tc.start();
        }
        catch (LifecycleException e)
        {
            log
                    .error("========== error on starting tomcat server,the error reason is=========");
            log.error(e.getLocalizedMessage());
        }
        log.debug("Tomcat Server start over.");
    }

    public void stop() throws Exception
    {
        try
        {
            tomcat.stop();
        }
        catch (LifecycleException lcex)
        {
            lcex.printStackTrace();
        }
    }

    private Embedded createTomcat(String path)
    {
        log.info(path);
        tomcat = new Embedded();

        tomcat.setCatalinaHome(path);
        Engine engine = tomcat.createEngine();
        Host host = tomcat.createHost("localhost", path + WEBAPPS);
        engine.addChild(host);
        engine.setDefaultHost("localhost");
        Context context = tomcat.createContext("", path + WEBROOT);
        host.addChild(context);
        String CUPSecureOfflineTest = host.getAppBase() + APPNM;

        Context ctxtCupSecureOffline = tomcat.createContext(APPCONTEXT,
                CUPSecureOfflineTest);

        host.addChild(ctxtCupSecureOffline);
        tomcat.addEngine(engine);
        Connector conn = tomcat.createConnector(LOCALHOST, 8080, false);
        Connector connSSL = tomcat.createConnector(LOCALHOST, 8443, false);
        connSSL.setScheme("https");
        connSSL.setSecure(true);
        connSSL.setProtocol("TLS");
        connSSL.setAttribute("clientAuth", Boolean.valueOf(true));
        connSSL.setAttribute("keystoreFile", "conf/server/server_keystore");
        connSSL.setAttribute("keystorePass", "changeit");
        tomcat.addConnector(connSSL);

        tomcat.addLifecycleListener(new LifecycleListener()
        {
            public void lifecycleEvent(LifecycleEvent arg)
            {
                if (arg.getType().equals("start"))
                {
                    startAction.setEnabled(false);
                    stopAction.setEnabled(true);
                    setBoot(true);
                }
                else
                {
                    if (!(arg.getType().equals("stop")))
                        return;
                    startAction.setEnabled(true);
                    stopAction.setEnabled(false);
                    setBoot(false);
                }
            }
        });
        tomcat.addConnector(conn);

        return tomcat;
    }

    public boolean isBoot()
    {
        return isBoot;
    }

    public void setBoot(boolean isBoot)
    {
        isBoot = isBoot;
    }

    public void reboot() throws Exception
    {
        stop();
        start();
    }

    public String getTomcatEmbedDir()
    {
        return tomcatEmbedDir;
    }

    public void setTomcatEmbedDir(String tomcatEmbedDir)
    {
        this.tomcatEmbedDir = tomcatEmbedDir;
    }

}
