/*
 * Created on 2007-4-26
 */
package com.coolsql.pub.display;

/**
 * @author liu_xlin
 *对组件进行信息查找时，需要制定匹配的方式，例如是否区分大小写，是否全词匹配，查找方向（向前或向后）
 */
public class FindProcessConfig {

    /**
     * 向前查找
     */
    public static final int FORWARD=0;
    /**
     * 向后查找
     */
    public static final int BACKFORWARD=1;
    
    /**
     * 忽略大小写
     */
    public static final int IGNORECASE=0;
    /**
     * 大小写敏感
     */
    public static final int SENSITIVECASE=1;
    /**
     * 全词匹配
     */
    public static final int MATCH_FULL=1;
    /**
     * 部分匹配
     */
    public static final int MATCH_PART=0;   
    
    //查找方式
    private int forward;
    //大小写是否敏感
    private int caseMatch;
    //匹配模式
    private int matchMode;
    
    /**
     * 是否循环查找
     */
    private boolean isCircle;
    /**
     * 查询关键字
     */
    private String keyWord;
    /**
     * 默认：向前查找，忽略大小写，部分匹配,非循环查找
     *
     */
    public FindProcessConfig()
    {
        this("",0,0,0,false);
    }
    public FindProcessConfig(String keyWord,int forward,int caseMatch,int matchMode,boolean isCircle)
    {
        this.forward=forward;
        this.caseMatch=caseMatch;
        this.matchMode=matchMode;
        this.keyWord=keyWord;
        this.isCircle=isCircle;
    }
    /**
     * @return Returns the caseMatch.
     */
    public int getCaseMatch() {
        return caseMatch;
    }
    /**
     * @param caseMatch The caseMatch to set.
     */
    public void setCaseMatch(int caseMatch) {
        this.caseMatch = caseMatch;
    }
    /**
     * @return Returns the forward.
     */
    public int getForward() {
        return forward;
    }
    /**
     * @param forward The forward to set.
     */
    public void setForward(int forward) {
        this.forward = forward;
    }
    /**
     * @return Returns the matchMode.
     */
    public int getMatchMode() {
        return matchMode;
    }
    /**
     * @param matchMode The matchMode to set.
     */
    public void setMatchMode(int matchMode) {
        this.matchMode = matchMode;
    }
    /**
     * @return Returns the keyWord.
     */
    public String getKeyWord() {
        return keyWord;
    }
    /**
     * @param keyWord The keyWord to set.
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    /**
     * @return Returns the isCircle.
     */
    public boolean isCircle() {
        return isCircle;
    }
    /**
     * @param isCircle The isCircle to set.
     */
    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }
}
