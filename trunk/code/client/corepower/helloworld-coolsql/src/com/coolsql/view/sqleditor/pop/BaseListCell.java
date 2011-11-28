/*
 * Created on 2007-2-7
 */
package com.coolsql.view.sqleditor.pop;

import com.coolsql.view.bookmarkview.BookMarkPubInfo;

/**
 * @author liu_xlin
 *实体信息弹出窗口中list控件的元素类型
 */
public class BaseListCell {

    private String catalog;//分类名
    private String schema; //模式名
    private String entity;  //实体名
    protected int type;    //实体类型所对应的BookMarkPubInfo中定义类型序号
    
    private String typeName;  //实体类型名称

    public BaseListCell(String catalog,String schema,String entity,String typeName)
    {
        this.catalog=catalog;
        this.schema=schema;
        this.entity=entity;
        this.typeName=typeName;
        if(typeName!=null)
        {
            if(typeName.equals("VIEW"))
            {
                type=BookMarkPubInfo.NODE_VIEW;
            }else if(typeName.equals("TABLE"))
            {
                type=BookMarkPubInfo.NODE_TABLE;
            }else
                type=BookMarkPubInfo.NODE_UNDEFINED;
        }
    }
    public String getDisplayLabel()
    {
        return this.toString();
    }
    public String toString()
    {
    	StringBuilder sb=new StringBuilder();
    	if(catalog!=null)
    	{
    		sb.append(catalog).append(".");
    	}
    	if(schema!=null)
    	{
    		sb.append(schema).append(".");
    	}
    	sb.append(entity);
        return sb.toString();
    }
    /**
     * @return Returns the entity.
     */
    public String getEntity() {
        return entity;
    }
    /**
     * @param entity The entity to set.
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }
    /**
     * @return Returns the schema.
     */
    public String getSchema() {
        return schema;
    }
    /**
     * @param schema The schema to set.
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }
    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }
    /**
     * @return Returns the typeName.
     */
    public String getTypeName() {
        return typeName;
    }
    /**
     * @param typeName The typeName to set.
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
	/**
	 * @return the catalog
	 */
	public String getCatalog() {
		return this.catalog;
	}
	/**
	 * @param catalog the catalog to set
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
}
