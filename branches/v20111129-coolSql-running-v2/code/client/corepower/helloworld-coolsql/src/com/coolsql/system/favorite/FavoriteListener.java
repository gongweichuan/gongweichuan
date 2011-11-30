/**
 * 
 */
package com.coolsql.system.favorite;

import java.util.EventListener;

/**
 * @author 刘孝林(kenny liu)
 *
 * 2008-3-26 create
 */
public interface FavoriteListener extends EventListener {

	public void addFavorite(FavoriteEvent e);
	
	public void deleteFavorite(FavoriteEvent e);
	
	public void updateFavorite(FavoriteEvent e);
}
