/**
 * 
 */
package com.coolsql.pub.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.coolsql.system.PropertyConstant;
import com.coolsql.system.Setting;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-3-29 create
 */
public class DataUtil {
	public static final DecimalFormat createTimingFormatter()
	{
		DecimalFormatSymbols symb = new DecimalFormatSymbols();
		String sep = Setting.getInstance().getProperty(PropertyConstant.PROPERTY_COMMON_DATAFORMATER_SEP, ".");
		symb.setDecimalSeparator(sep.charAt(0));		
		DecimalFormat numberFormatter = new DecimalFormat("0.#s", symb);
		numberFormatter.setMaximumFractionDigits(2);
		return numberFormatter;
	}
}
