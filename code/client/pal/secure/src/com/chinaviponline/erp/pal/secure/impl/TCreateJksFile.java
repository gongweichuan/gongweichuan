package com.chinaviponline.erp.pal.secure.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.pal.secure.i.ICreateJksFile;

/**
 * <p>文件名称：TCreateJksFile.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2012-4-16</p>
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
public class TCreateJksFile implements ICreateJksFile
{
    private Logger log=Logger.getLogger(this.getClass());
    @Override
    public boolean createJksFile(Map map)
    {
        String strStart="cmd.exe /c start";
        String strKeytool=(String)map.get("keytool");
        String strDir=(String)map.get("dir");
        //keytool -genkey -dname "CN=Pingan Bank, OU=Pingan, O=Pingan, L=Shenzhen, ST=Shenzhen, C=CN" 
        //-alias pinganmer -keyalg RSA -keysize 1024 -keystore pinganmer.jks -keypass 12345678 -storepass 12345678 -validity 365
        String strGenkeyDname=(String)map.get("dname");
        String strAlias=(String)map.get("alias");
        String strKeyalg=(String)map.get("keyalg");
        String strKeysize=(String)map.get("keysize");
        String strKeystore=(String)map.get("keysotre");
        String strKeypass=(String)map.get("keypass");
        String strStorepass=(String)map.get("storepass");
        String strValidity=(String)map.get("validity");
        
        String strGenkey=strStart+" "+strKeytool+" ";
        strGenkey+="-genkey -dname"+" "+strGenkeyDname+" ";
        strGenkey+="-alias"+" "+strAlias+" ";
        strGenkey+="-keyalg"+" "+strKeyalg+" ";
        strGenkey+="-keysize"+" "+strKeysize+" ";
        strGenkey+="-keystore"+" "+strKeystore+" ";
        strGenkey+="-keypass"+" "+strKeypass+" ";
        strGenkey+="-storepass"+" "+strStorepass+" ";
        strGenkey+="-validity"+" "+strValidity;
        
        log.info("CmdLine keygen:"+strGenkey);
        
        //keytool -export -alias pinganmer -file pinganmer.cer  -keystore pinganmer.jks -storepass 12345678 -rfc
        String strExport=strStart+" "+strKeytool+" ";;
        strExport+="-export -alias"+" "+strAlias+" ";
        
        String strFile=(String)map.get("file");
        strExport+="-file"+" "+strFile+" ";
        strExport+="-keystore"+" "+strKeystore+"";
        strExport+="-storepass"+" "+strGenkey+" -rfc";
        
        log.info("CmdLine export:"+strExport);
        try
        {
            Process genkey=Runtime.getRuntime().exec(strGenkey, null, new File(strDir));
            Process export=Runtime.getRuntime().exec(strExport, null, new File(strDir));
        }
        catch (IOException e)
        {
            log.warn("keytool.exe:", e);
            return false;
        }
        
        
        return true;
    }

}
