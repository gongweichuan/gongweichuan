package com.chinaviponline.erp.pal.secure.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.log4j.Logger;



import com.chinaviponline.erp.pal.secure.i.IDescPublicKeyInfo;

/**
 * <p>文件名称：TDescPublicKeyInfo.java</p>
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
public class TDescPublicKeyInfo implements IDescPublicKeyInfo
{
    private Logger log = Logger.getLogger(this.getClass());
    
    @Override
    public String showPublicKeyModulus(String filePathName) throws CertificateException, FileNotFoundException
    {
        CertificateFactory certInfo = CertificateFactory.getInstance("x.509");
        X509Certificate cert = (X509Certificate) certInfo .generateCertificate(new FileInputStream(filePathName));
                
        PublicKey publicKey = cert.getPublicKey();
        int index = publicKey.toString().indexOf("modulus:");
        String keyString = publicKey.toString().substring(index + 8,
                index + 8 + 75 * 4 + 4);

        log.info("index:" + index);        

        keyString = keyString.replaceAll(" ", "");
        keyString = keyString.replaceAll("\n", "");
        log.info("keyString:" + keyString);
        log.info(publicKey);
        log.info(publicKey.getFormat());
        log.info(publicKey.getEncoded());
        log.info(publicKey.getAlgorithm());
        log.info(keyString.toUpperCase());
        String dd = "2";

        int s = new Integer(dd).intValue();
        log.info(s);   

        int ss = 8;
        log.info(String.valueOf(8));

        return keyString;
    }

}
