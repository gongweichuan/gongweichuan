package com.chinaviponline.erp.pal.secure.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.pal.secure.i.IConvPfx2JksFile;

/**
 * <p>文件名称：TConvPfx2JksFile.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2012-4-9</p>
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
public class TConvPfx2JksFile implements IConvPfx2JksFile
{
    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public boolean convPfx2Jks(String inCertFileNm, String KSPwd,
            String outCertFileNm)
    {
        try
        {
            KeyStore inputKeyStore = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(inCertFileNm);
            char[] nPassword = (char[]) null;

            if ((KSPwd == null) || (KSPwd.trim().equals("")))
            {
                nPassword = (char[]) null;
            }
            else
            {
                nPassword = KSPwd.toCharArray();
            }
            inputKeyStore.load(fis, nPassword);
            fis.close();

            KeyStore outputKeyStore = KeyStore.getInstance("JKS");
            outputKeyStore.load(null, KSPwd.toCharArray());

            Enumeration enums = inputKeyStore.aliases();
            while (enums.hasMoreElements())
            {
                String keyAlias = (String) enums.nextElement();

                if (inputKeyStore.isKeyEntry(keyAlias))
                {
                    Key key = inputKeyStore.getKey(keyAlias, nPassword);
                    Certificate[] certChain = inputKeyStore
                            .getCertificateChain(keyAlias);
                    outputKeyStore.setKeyEntry(keyAlias, key,
                            KSPwd.toCharArray(), certChain);
                }
            }
            FileOutputStream out = new FileOutputStream(outCertFileNm);
            outputKeyStore.store(out, nPassword);
            out.close();
        }
        catch (IOException e)
        {
            //e.printStackTrace();
            log.error(e.getMessage());
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            log.error(e.getMessage());
        }

        return false;
    }

    public static void main(String[] args)
    {
        Logger log = Logger.getLogger(TConvPfx2JksFile.class);
        TConvPfx2JksFile cp2j = new TConvPfx2JksFile();

        try
        {
            String srcCertPath = args[0];
            if ((srcCertPath == null) || (srcCertPath.equals("")))
            {
                log.error("error, src cert path is null !");
            }
            String srcCertPwd = args[1];
            if ((srcCertPwd == null) || (srcCertPwd.equals("")))
            {
                log.error("error, src cert password is null !");
            }
            String desCertPath = args[2];
            if ((desCertPath == null) || (desCertPath.equals("")))
            {
                log.error("error, des cert path is null !");
            }

            log.info("begin transfer cert ...");
            boolean b = cp2j.convPfx2Jks(srcCertPath, srcCertPwd, desCertPath);
            log.info("transfer cert success");
        }
        catch (Exception ex)
        {
            log.error("transfer cert fail");
            log.error(ex.getMessage());
        }
    }

}
