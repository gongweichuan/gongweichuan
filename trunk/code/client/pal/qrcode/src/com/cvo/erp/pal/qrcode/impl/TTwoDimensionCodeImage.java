package com.cvo.erp.pal.qrcode.impl;

import jp.sourceforge.qrcode.data.QRCodeImage;
import java.awt.image.BufferedImage;

/**
 * <p>文件名称：TTwoDimensionCodeImage.java</p>
 * <p>文件描述：二维码图片对象</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2013-4-8</p>
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
public class TTwoDimensionCodeImage implements QRCodeImage
{

    BufferedImage bufImg;

    public TTwoDimensionCodeImage(BufferedImage bufImg)
    {
        this.bufImg = bufImg;
    }

    @Override
    public int getHeight()
    {
        return bufImg.getHeight();
    }

    @Override
    public int getPixel(int x, int y)
    {
        return bufImg.getRGB(x, y);
    }

    @Override
    public int getWidth()
    {
        return bufImg.getWidth();
    }

}
