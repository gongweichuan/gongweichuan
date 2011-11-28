/*
 * 创建日期 2006-8-4
 *
 */
package com.coolsql.view.sqleditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.text.DefaultStyledDocument;

import com.coolsql.pub.util.StringUtil;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 将编辑器内容元素设置成高亮
 */
public class SyntaxHighlighter extends Thread {
    /**
     * key:键对象，可能是文档模型对象   value:高亮线程（SyntaxHighlighter）
     */
    private static Map threadMap=null;
    static
    {
        threadMap=new HashMap();
    }
    public synchronized void updateText(String text, int start) {
        if (StringUtil.trim(text).equals(""))
            return;

        requests.add(new UpdateRequest(text, start));
        notify();
    }

    public void run() {
        while (running)
            try {
                synchronized (this) {
                    if (requests.size() <= 0)
                        wait();
                    else
                        Thread.sleep(10L);
                }

                if (!running)
                    break;

                UpdateRequest request = (UpdateRequest) requests.removeFirst();
                String text = request.text.toUpperCase();
                List tokens = SQLLexx.parse(text);
                List styles = new ArrayList();
                int min = Integer.MAX_VALUE;
                int max = 0;
                for (int i = 0; i < tokens.size(); i++) {
                    Token t = (Token) tokens.get(i);
                    String value = t.getValue();
                    int start = t.getStart();
                    int length = t.getEnd() - t.getStart();
                    int realStart = request.getOffset() + start; //实际位置

                    min = Math.min(start, min);
                    max = Math.max(max, t.getEnd());
                    if (t.getType() == Token.IDENTIFIER) {
                        boolean keyword = false;
                        for (int index = 0; index < EditorUtil.KEYWORDS.length; index++)
                            if (value.equals(EditorUtil.KEYWORDS[index]))
                                keyword = true;

                        if (keyword) {
                            doc.setCharacterAttributes(realStart, length,
                                    EditorUtil.KEYWORD_SET, true);
                        } else {
                            doc.setCharacterAttributes(realStart, length,
                                    EditorUtil.NORMAL_SET, true);
                        }
                    } else if (t.getType() == Token.COMMENT) {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.COMMENT_SET, true);
                    } else if (t.getType() == Token.LITERAL) {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.VALUE_SET, true);
                    } else if (t.getType() == Token.NUMERIC) {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.NUMBER_SET, true);
                    } else {
                        doc.setCharacterAttributes(realStart, length,
                                EditorUtil.NORMAL_SET, true);
                    }
                }
            } catch (NoSuchElementException nosuchelementexception) {
                LogProxy.outputErrorLog(nosuchelementexception);
            } catch (InterruptedException interruptedexception) {
                LogProxy.outputErrorLog(interruptedexception);
            }
    }

    //控制线程的运行和停止
    private boolean running;

    //更改的保存
    private LinkedList requests;

    //被监控的文档模型
    private DefaultStyledDocument doc;

    public SyntaxHighlighter(DefaultStyledDocument doc) {
        running = true;
        requests = new LinkedList();
        this.doc = doc;
        setPriority(1);
        start();
    }

    /**
     * 停止高亮显示线程的运行
     *  
     */
    public void stopRun() {
        running = false;
        requests.clear();
        requests=null;
        doc=null;
        synchronized (this) {
            notify();
        }
    }
    /**
     * 根据指定的键对象获取高亮线程
     * @param ob  --键对象
     * @return  --高亮线程
     */
    public static SyntaxHighlighter getThread(Object ob)
    {
        return (SyntaxHighlighter)threadMap.get(ob);
    }
    /**
     * 保存当前的高亮线程
     * @param ob  --键值对象
     * @param highlighter  --高亮线程
     */
    public static void addThread(Object ob,SyntaxHighlighter highlighter)
    {
        threadMap.put(ob,highlighter);
    }
    /**
     * 终止指定键值对象所对应的高亮线程。
     * @param ob  --键值对象
     */
    public static void stopThread(Object ob)
    {
        SyntaxHighlighter highlight=getThread(ob);
        if(highlight!=null)
            highlight.stopRun();
    }
    /**
     * 
     * @author liu_xlin 每一次内容更改，将更改请求保存，包含了文本内容，更改起始点和更改文本长度
     */
    protected class UpdateRequest {

        private String text;

        /**
         * 文本相对于整体编辑文档的偏移
         */
        private int offset;

        //		private int length;

        public UpdateRequest(String text, int offset) {
            this.text = text;
            this.offset = offset;
        }

        //        public int getLength() {
        //            return length;
        //        }
        //        public void setLength(int length) {
        //            this.length = length;
        //        }
        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
