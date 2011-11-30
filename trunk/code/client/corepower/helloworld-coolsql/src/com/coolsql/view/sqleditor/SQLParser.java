/*
 * 创建日期 2006-10-16
 */
package com.coolsql.view.sqleditor;

/**
 * @author liu_xlin sql编辑器的sql语句解析类
 */
import java.util.Vector;

import com.coolsql.view.log.LogProxy;

public class SQLParser {

    public SQLParser() {
    }

    public static Vector parse(String query) {
        Vector commands = new Vector();
        Vector groups = new Vector();
        try {
            Vector tokens = SQLLexx.parse(query);
            StringBuffer buffer = new StringBuffer();
            StringBuffer groupBuffer = new StringBuffer();
            for (int i = 0; i < tokens.size(); i++) {
                Token t = (Token) tokens.elementAt(i);
                if (t.getType() != Token.COMMENT)
                    if (t.getType() == Token.SEPARATOR) {
                        groupBuffer.append(t.getValue());
                        String newCommand = buffer.toString().trim();
                        if (!newCommand.equals(""))
                            commands.addElement(newCommand);
                        buffer = new StringBuffer();
                    } else {
                        buffer.append(t.getValue());
                        groupBuffer.append(t.getValue());
                    }
            }

            String newCommand = buffer.toString().trim();
            if (!newCommand.equals(""))
                commands.addElement(newCommand);
        } catch (Throwable e) {
            
                LogProxy.outputErrorLog(e);
        }
        Vector result = new Vector();
        result.addAll(groups);
        result.addAll(commands);
        return result;
    }

    /**
     * 解析文本中有效的信息
     * @param query
     * @param offset
     * @return  --返回有效的起始位置和结束位置
     */
    public static int[] parseValidatePosition(String query, int offset) {
        boolean startFlag = false;
        int startPosition = -1;  //开始位置
        int endPosition = -1;   //结束位置
        try {
            Vector tokens = SQLLexx.parse(query);
            StringBuffer buffer = new StringBuffer();
            Token t=null;
            for (int i = 0; i < tokens.size(); i++) {
                t = (Token) tokens.elementAt(i);
                if (t.getType() != Token.COMMENT)
                {
                    if (t.getType() == Token.SEPARATOR) {
                        String newCommand = buffer.toString().trim();
                        if (!newCommand.equals("")) {
                            if (startFlag) {
                                endPosition = t.getEnd();
                            }
                        }
                        startFlag = false;
                    } else {
                        buffer.append(t.getValue());
                        if (!startFlag) {
                            startPosition = t.getStart();
                            startFlag = true;
                        }
                    }
                }
            }

            if (startFlag) {  //没有结束符号:';','/*','--'的情况
                String newCommand = buffer.toString().trim();
                if (!newCommand.equals(""))
                    
                    endPosition = t.getEnd();
                
            }
        } catch (Throwable e) {
            
                LogProxy.outputErrorLog(e);
        }
        if(startPosition!=-1&&endPosition!=-1)
        {
            int[] info=new int[2];
            info[0]=startPosition+offset;
            info[1]=endPosition+offset;
            return info;
        }else
            return null;
    }
}
