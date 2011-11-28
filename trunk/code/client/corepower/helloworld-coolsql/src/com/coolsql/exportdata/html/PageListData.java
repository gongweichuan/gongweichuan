package com.coolsql.exportdata.html;

import java.util.Vector;

import com.coolsql.pub.exception.UnifyException;

/**
 * Process html page.
 * 
 * @author kenny liu
 */
public class PageListData {
	
    private Vector dataArray = null; //保存分页数据对象

    private Vector headArray = null; //保存表头的定义

    private int pageSize = 50; //每页显示记录数

    private int pageCount = 0; //总页数

    private int page = 0; //当前页数

    //数据指针，用来遍历所有导出数据
    private int pointer = -1;

    //文件名,网页文件的名称
    private String fileName = null;

    /**
     * 对数据信息的描述，用于网页标题的显示
     */
    private String title = "";

    public PageListData(Vector header, Vector data,String fileName,String title) {
        newDataArray();
        if (header != null)
            headArray = header;
        else
            headArray = new Vector();

        if (data != null)
            dataArray = data;
        else
            dataArray = new Vector();
        
        this.fileName=fileName;
        this.title=title;
        
        reset();
    }

    public PageListData() {
        this(null, null,"","");
    }

    public Vector getDataArray() {
        return dataArray;
    }

    public void setDataArray(Vector dataArray) {
        this.dataArray = dataArray;
    }

    public Object getData(int i) {
        return getDataArray().get(i);
    }

    public void newDataArray() {
        if (dataArray == null) {
            dataArray = new Vector();
        }
    }

    public void clearDataArray() {
        if (dataArray != null) {
            dataArray.clear();
        }
    }

    /**
     * 计算记录总数
     * 
     * @return int 总行数
     */
    public int updatePageCount() {
        if (dataArray.size() % pageSize == 0)
            pageCount = dataArray.size() / pageSize;
        else
            pageCount = dataArray.size() / pageSize + 1;
        return pageCount;
    }
    /**
     * 返回总的页数
     * @return
     */
    public int getPageCount()
    {
        return pageCount;
    }
    /**
     * 获取每页显示记录数
     * 
     * @return int 每页显示的记录数
     */
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取当前页数
     * 
     * @return int 当前的页数
     */
    public int getPage() {
        // 当前页不大于总页数 by xia_lei on 2005-06-14
        if (page > updatePageCount() && updatePageCount() != 0)
            page = updatePageCount();

        return page;
    }

    private void setPage(int page) {
        this.page = page;
    }

    /**
     * 分页栏函数 必需被包含在某个Form之中
     * 
     * @return String 分栏字符串
     * @throws UnifyException
     */
    public String getFooter() throws HtmlExportException {
        StringBuilder str = new StringBuilder("");
        int next, prev;
        prev = page - 1;
        next = page + 1;
        if (page > 1) {
            str
                    .append("<INPUT type=\"button\" value='First Page' name=first onclick=\"toPage('"
                            + filterFileName(fileName) + "')\">");
        } else {
            str.append("<INPUT type=submit value='First Page' name=first disabled>");
        }
        if (page > 1) {
            str
                    .append("<INPUT type=\"button\" value='Previous Page' name=prev onclick=\"toPage('"
                            + filterFileName(fileName) + ((page - 1)==1?"":("','"+(page-2))) + "')\">");
        } else {
            str.append("<INPUT type=\"button\" value='Previous Page' name=prev disabled>");
        }
        if (page < pageCount) {
            str
                    .append("<INPUT type=\"button\" value='Next Page' name=next onclick=\"toPage('"
                            + filterFileName(fileName)+"','" + (page ) + "')\">");
        } else {
            str.append("<INPUT type=\"button\" value='Next Page' name=next disabled>");
        }
        if (pageCount > 1 && page != pageCount) {
            str
                    .append("<INPUT type=\"button\" value='Last Page' name=last onclick=\"toPage('"
                            + filterFileName(fileName)+"','"
                            + (this.getPageCount()-1)
                            + "')\">");
        } else {
            str.append("<INPUT type=\"button\" value='Last Page' name=last disabled>");
        }
        str.append(" Total size:" + dataArray.size());
        str.append(", Page count:" + updatePageCount() + " , To");
        str
                .append("<SELECT size=1 name=Pagelist onchange=\"toPage('"
                        + filterFileName(fileName)
                        + "',this.value-1)\">");
        for (int i = 1; i < pageCount + 1; i++) {
            if (i == page) {
                str.append("<OPTION value=" + i + " selected>" + i
                        + "</OPTION>");
            } else {
                str.append("<OPTION value=" + i + ">" + i + "</OPTION>");
            }
        }
        str.append("</SELECT>, Current Page size:"+getCurrentPageRows()+"<br>\r\n");
        return str.toString();
    }
    protected String getCss()
    {
        return "<style type=\"text/css\">\r\n"
        +"body,table,tr,th,td,input,button,a,a:hover {font-size: 12px;font-family: 宋体;}\r\n"
        +"table.border {border-width: 0px;width: 80%;}\r\n"
        +"BODY {color: #000033;background-color: #E7E7E8;scrollbar-base-color: color;scrollbar-track-color: #afafb0;\r\n"
        +"scrollbar-face-color: #8b97bc;scrollbar-highlight-color: #e7e7e7;scrollbar-shadow-color: #e7e7e7;scrollbar-3dlight-color: #000000;\r\n"
        +"scrollbar-darkshadow-color: #000000;scrollbar-arrow-color: #000000;text-align: center;};\r\n"
        +"table {border-collapse: collapse;border-color: #000000;border-width: 1px;}\r\n"
        +"TH {border-color: #000000;font-weight: normal;background-color: #B2B7CC;height: 20;padding-left: 4px;padding-right: 4px;}\r\n"
        +"TD {border-color: #000000;word-wrap: normal;word-break: keep-all;}\r\n</style>\r\n";
    }
    protected String getHead() {
        return "<html>\r\n<HEAD>\r\n<META http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">\r\n"
                + "<META http-equiv=\"Content-Style-Type\" content=\"text/css\">\r\n";

    }

    protected String getScriptCode() {
        return "<script language=\"JavaScript\">\r\n function toPage(pageName,index){\r\n\tvar tmpName;\r\n\tif(index==null||index==0)\r\n\t\ttmpName=pageName+'.html';\r\n\telse\r\n\ttmpName=pageName+index+'.html';\r\n\tlocation.href=tmpName;\r\n}\r\n</script>\r\n";
    }

    protected String getBodyHead() throws HtmlExportException {
        return "</head>\r\n<body>\r\n<form name=\"nplSelectForm\" method=\"post\" action=\"\">\r\n<table class=\"border\">"
                + "\r\n\t<tr>\r\n\t<td>\r\n\t"
//                +"<p class=\"title\">"
//                + title
//                + "</p>\r\n\t"
                + getFooter();
//                + "\t<DIV class=\"tab-pane\">\r\n\t<DIV class=\"tab-page\">"
//                + "\r\n\t<DIV class=\"tab\">查询信息</DIV>\r\n";
    }

    protected String getBodyData() {
        StringBuffer buffer = new StringBuffer("");
        buffer.append("\t<table border=1 class=\"border\">\r\n\t<tr>");
        for (int i = 0; i < headArray.size(); i++) {
            buffer.append("\t<th nowrap>" + headArray.get(i) + "</th>\r\n");
        }
        buffer.append("\t</tr>\r\n");
        
        for (int i = 0; i < pageSize && pointer < dataArray.size(); i++) {

            buffer.append("\t<tr>\r\n");
            Vector v = (Vector) dataArray.get(pointer);
            //            Vector v=(Vector)dataArray.get(i+(page-1)*pageSize);
            for (int j = 0; j < headArray.size(); j++) {
                buffer.append("\t<td align=\"center\">" + v.get(j) + "</td>\r\n");
            }
            buffer.append("\t</tr>\r\n");
            pointer++; //指针后移
        }
        buffer.append("\t</table>\r\n");
        return buffer.toString();
    }

    protected String getBodyFooter() {
        return "\t</td></tr></table>\r\n\t</form></BODY></html>";
    }
    /**
     * 获取当前页的记录数
     * @return
     */
    private int getCurrentPageRows()
    {
        int tmp=pointer;
        int count=0;
        for (int i = 0; i < pageSize && tmp < dataArray.size(); i++,tmp++) {
            count++;
        }
        return count;
    }
    public String getNextPageCode() throws HtmlExportException {
        nextPage();
        return this.getHead() +this.getCss()+ getScriptCode() + this.getBodyHead()
                + getBodyData() + getBodyFooter();
    }

    public boolean hasNext() {
        return page < pageCount;
    }

    private void nextPage() throws HtmlExportException {
        if (!hasNext())
            throw new HtmlExportException("has no page available");
        else
            page++;
    }

    /**
     * 重置数据位置
     *  
     */
    public void reset() {
        pointer = 0;
        page = 0;
        updatePageCount();
    }

    /**
     * 返回指定文件类型的文件名
     * 
     * @param oldName
     * @param fileType
     * @return
     * @throws UnifyException
     */
    public static String filterFileName(String file) throws HtmlExportException {
        int index = -1;

        if (file.toUpperCase().endsWith("HTML")) {
            index = file.toUpperCase().lastIndexOf("HTML");
            return file.substring(0, index-1);
        } else
            throw new HtmlExportException("file type is error!");
    }

    public String toString() {
        try {
            return getFooter();
        } catch (HtmlExportException e) {
            return "";
        }
    }
    /**
     * @return 返回 fileName。
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName 要设置的 fileName。
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return 返回 headArray。
     */
    public Vector getHeadArray() {
        return headArray;
    }
    /**
     * @param headArray 要设置的 headArray。
     */
    public void setHeadArray(Vector headArray) {
        this.headArray = headArray;
    }
    /**
     * @return 返回 pointer。
     */
    public int getPointer() {
        return pointer;
    }
    /**
     * @param pointer 要设置的 pointer。
     */
    public void setPointer(int pointer) {
        this.pointer = pointer;
    }
    /**
     * @return 返回 title。
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title 要设置的 title。
     */
    public void setTitle(String title) {
        this.title = title;
    }
}