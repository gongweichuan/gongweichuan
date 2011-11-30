/*
 * DefaultBookmarkChangeListener.java
 *
 * This file is part of CoolSQL, http://coolsql.dev.java.net.
 *
 * Copyright 2008-2010, kenny liu
 *
 * To contact the author please send an email to: mailforlxl@gmail.com
 *
 */
package com.coolsql.bookmarkBean;

import java.util.EventListener;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-5-28 create
 */
public interface DefaultBookmarkChangeListener extends EventListener {

	public void defaultChanged(DefaultBookmarkChangeEvent e);
}
