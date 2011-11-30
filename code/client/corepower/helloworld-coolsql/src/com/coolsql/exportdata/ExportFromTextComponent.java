/*
 * 创建日期 2006-10-18
 */
package com.coolsql.exportdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.text.JTextComponent;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin 完成将文本控件的内容导出的功能类
 */
public class ExportFromTextComponent extends ExportComponentData {

    public ExportFromTextComponent(JTextComponent source) {
        super(source);
    }

    /**
     * 导出为文本文件
     */
    public void exportToTxt() throws UnifyException {
        File file = this.selectFile(null);
        if (file != null) {
            FileOutputStream out = null;
            try {
                GUIUtil.createDir(file.getAbsolutePath(),false, false);
                out = new FileOutputStream(file);

                byte[] info = ((JTextComponent) this.getSource()).getText()
                        .getBytes();
                out.write(info);
            } catch (FileNotFoundException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filenotfound")+e.getMessage());
            } catch (IOException e) {
                throw new UnifyException(PublicResource
                        .getSQLString("export.filewriteerror"));
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

    }

}
