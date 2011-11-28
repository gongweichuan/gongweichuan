/**
 * 
 */
package com.coolsql.plugin;

/**
 * throw this exception when processing plugins
 * @author 刘孝林
 *
 * 2008-1-10 create
 */
public class PluginException extends Exception {

	/**
	 * 
	 */
	public PluginException() {
	}

	/**
	 * @param message
	 */
	public PluginException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PluginException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PluginException(String message, Throwable cause) {
		super(message, cause);
	}

}
