package com.coolsql.action.framework;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.Action;
import javax.swing.KeyStroke;

import com.coolsql.system.ActionCollection;


/**
 * @author kenny liu
 *
 */
public class ShortcutManager
{

	private String filename;

	// key to the map is the action's class name,
	// the actual object in it will be a ShortcutDefinition
	private HashMap<String, ShortcutDefinition> keyMap;

	private HashMap<String, String> actionNames;
	
	// we need the list of registered actions, in order to be able to
	// display the label for the action for the customization dialog
	private List<CsAction> allActions = new LinkedList<CsAction>();
	
//	private HashMap<KeyStroke, CsAction> keyDebugMap;
	
	@SuppressWarnings("unchecked")
	public ShortcutManager(String aFilename)
	{
		this.filename = aFilename;
		try
		{
			CSPersistence reader = new CSPersistence(this.filename);
			this.keyMap = (HashMap)reader.readObject();
		}
		catch (Exception e)
		{
			this.keyMap = null;
		}

		if (this.keyMap == null)
		{
			this.keyMap = new HashMap<String, ShortcutDefinition>(30);
			this.actionNames = new HashMap<String, String>(30);
		}
		else
		{
			this.actionNames = new HashMap<String, String>(this.keyMap.size());
		}
	}

	public void removeShortcut(String clazz)
	{
		this.assignKey(clazz, null);
	}
	public CsAction getActionByClass(Class<? extends CsAction> actionClass)
	{
		if(actionNames.containsKey(actionClass.getName()))
		{
			for (CsAction action : allActions)
			{
				if(action.getClass()==actionClass)
					return action;
			}
		}
		try {
			
			CsAction newAction=(CsAction)ActionCollection.getInstance().get(actionClass);
			registerAction(newAction);
			return newAction;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void registerAction(CsAction anAction)
	{
		String clazz = anAction.getClass().getName();
		ShortcutDefinition def = this.getDefinition(clazz);
		if (def == null)
		{
			def = new ShortcutDefinition(clazz);
			this.keyMap.put(clazz, def);
		}

		if (!actionNames.containsKey(clazz))
		{
			String label = anAction.getMenuLabel();

			if (label == null) label = anAction.getActionName();
			if (label != null)
			{
				this.actionNames.put(clazz, label);
			}
		}

		if (!def.hasDefault())
		{
			KeyStroke defaultkey = anAction.getDefaultAccelerator();
			if (defaultkey != null)
			{
				def.assignDefaultKey(defaultkey);
			}
		}

		// a list of all instances is needed when updating
		// the shortcuts at runtime
		this.allActions.add(anAction);
	}

	public String getTooltip(String clazz)
	{
		Iterator<CsAction> itr = allActions.iterator();
		while (itr.hasNext())
		{
			CsAction action = (CsAction)itr.next();
			String actionClass = action.getClass().getName();
			if (actionClass.equals(clazz))
			{
				return action.getTooltipText();
			}
		}		
		return null;
	}
	
	/**
	 * Return the class name of the action to which the passed key is mapped.
	 * @param key
	 * @return the action's class name or null if the key is not mapped
	 */
	public String getActionClassForKey(KeyStroke key)
	{
		String clazz = null;
		Iterator<ShortcutDefinition> itr = this.keyMap.values().iterator();

		while (itr.hasNext())
		{
			ShortcutDefinition def = (ShortcutDefinition)itr.next();
			if (def.isMappedTo(key))
			{
				clazz = def.getActionClass();
				break;
			}
		}

		return clazz;
	}

	public String getActionNameForKey(KeyStroke key)
	{
		String clazz = this.getActionClassForKey(key);
		String name = null;

		if (clazz != null)
		{
			name = this.actionNames.get(clazz);
			if (name == null) name = clazz;
		}
		return name;
	}

	public void resetToDefault(String actionClass)
	{
		ShortcutDefinition def = this.getDefinition(actionClass);
		if (def == null) return;
		def.resetToDefault();
	}

	public void assignKey(String actionClass, KeyStroke key)
	{
		ShortcutDefinition def = this.getDefinition(actionClass);
		if (def == null) return;
		if (key == null)
		{
			def.setShortcutRemoved(true);
		}
		else
		{
			def.assignKey(key);
		}
	}

	public void updateActions()
	{
		for (CsAction action : allActions)
		{
			String actionClass = action.getClass().getName();
			ShortcutDefinition def = this.getDefinition(actionClass);
			KeyStroke key = def.getActiveKeyStroke();
			action.setAccelerator(key);
		}
	}

	public String getActionNameForClass(String className)
	{
		return this.actionNames.get(className);
	}
	/**
	 * Saves the current key assignments into an external file.
	 * Only mappings which are different from the default are saved.
	 * If a definition file exists, and the current map contains only
	 * default mappings, then the existing file is deleted.
	 */
	public void saveSettings()
	{
		// we only want to save those definitions where a different mapping is defined
		HashMap<String, ShortcutDefinition> toSave = new HashMap<String, ShortcutDefinition>(this.keyMap);
		for (Entry<String, ShortcutDefinition> entry : keyMap.entrySet())
		{
			ShortcutDefinition def = entry.getValue();
			if (!def.isCustomized())
			{
				toSave.remove(entry.getKey());
			}
		}
		// if no mapping at all is defined, then don't save it
		if (toSave.size() > 0)
		{
			CSPersistence writer = new CSPersistence(this.filename);
			try { writer.writeObject(toSave); } catch (Throwable th) {}
		}
		else
		{
			// but we need to make sure that, an existing definition
			// is removed, otherwise we'll load it at the next startup
			File f = new File(this.filename);
			if (f.exists())
			{
				f.delete();
			}
		}
	}

	public ShortcutDefinition[] getDefinitions()
	{
		ShortcutDefinition[] list = new ShortcutDefinition[this.keyMap.size()];
		Iterator<ShortcutDefinition> itr = this.keyMap.values().iterator();
		int i = 0;
		while (itr.hasNext())
		{
			list[i] = (ShortcutDefinition)itr.next();
			i++;
		}
		return list;
	}

	public KeyStroke getCustomizedKeyStroke(Action anAction)
	{
		ShortcutDefinition def = this.getDefinition(anAction);
		if (def == null) return null;
		return def.getActiveKeyStroke();
	}

	public void setCustomizedKeyStroke(String aClassName, KeyStroke aKey)
	{
		String oldclass = this.getActionClassForKey(aKey);
		if (oldclass != null)
		{
			ShortcutDefinition old = this.keyMap.get(oldclass);
			if (old != null) old.clearKeyStroke();
		}
		ShortcutDefinition def = this.keyMap.get(aClassName);
		if (def == null) return;
		def.assignKey(aKey);
	}

	private ShortcutDefinition getDefinition(Action anAction)
	{
		return this.getDefinition(anAction.getClass().getName());
	}

	private ShortcutDefinition getDefinition(String aClassname)
	{
		ShortcutDefinition def = this.keyMap.get(aClassname);
		return def;
	}


}
