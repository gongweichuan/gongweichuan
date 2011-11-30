package com.coolsql.pub.display;

import java.util.EventListener;

public interface TextSelectionListener
	extends EventListener
{
	void selectionChanged(int newStart, int newEnd);
}
