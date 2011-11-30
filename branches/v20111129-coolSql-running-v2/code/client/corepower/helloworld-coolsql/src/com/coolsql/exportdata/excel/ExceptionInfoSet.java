package com.coolsql.exportdata.excel;

/**
 *该类中定义了异常类UserException中的错误信息和错误种类
 * @author CMIS项目组
 */
public class ExceptionInfoSet {
	
	/**
	 * 数据库操作异常类，涉及到数据库操作出现的异常。
	 */
	public static String DB_Category = "DB";
	
	/**
	 * 应用操作异常类，例如金额不足、权限不够，以及通过字符串构造函数构造的通用异常，均为该类型。
	 */
	public static String App_Category = "App";
	
	/**
	 * 调用期间发生的系统异常类，一般涉及到应用服务器相关的异常，都定义为系统异常类型。
	 */
	public static String System_Category = "System";
	
	/**
	 * 数据库不存在时的错误信息，errorCode=-1013
	 */
	public static String DB_NotFound = "该数据库不存在！";
	
	/**
	 * 用户不存在时的错误信息，errorCode＝-30082
	 */
	public static String DB_InvalidUser =  "输入的用户名或密码错误！";
	
	/**
	 * 数据库连接已经关闭的异常信息,errorCode = -99999
	 */
	public static String DB_ConnectionClosed = "数据库连接已关闭！";
	
	/**
	 * 执行操作的数据库中不存在该表,errorCode=-204
	 */
	public static String DB_TableNotExist = "执行操作的数据表不存在！";
	
	/**
	 * 向数据库中插入一条已经存在的记录时的异常信息,errorCode=-803
	 */
	public static String DB_RecordExits = "该记录已经存在！";
	
	/**
	 * 向数据库中插入记录发生错误的异常信息
	 */
	public static String DB_InsertFailed = "向数据库中插入记录错误！";
	
	/**
	 * 执行数据库查询时发生错误的异常信息
	 */
	public static String DB_QueryFailed = "数据库查询错误！";
	
	/**
	 * 从数据库中删除记录时发生错误时的异常信息
	 */
	public static String DB_DeleteFailed = "数据库删除记录错误！";
	
	/**
	 * 缺省的数据库错误信息！
	 */
	public static String DB_Default_Error = "执行数据库操作错误!";

}