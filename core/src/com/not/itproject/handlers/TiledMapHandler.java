package com.not.itproject.handlers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.not.itproject.objects.Checkpoint;
import com.not.itproject.objects.GameWorld;
import com.not.itproject.objects.Obstacle;
import com.not.itproject.objects.PowerUpContainer;

public class TiledMapHandler {
	// Map rendering
	public TiledMap tiledMap;
	public int mapHeight;
	public int mapWidth;
	public int tileHeight;
	public int tileWidth;

	public TiledMapHandler(GameWorld gameWorld, int selectionChoice)
	{
		initialiseMap(gameWorld, selectionChoice);
	}
	
	// -------------------- Map initialisation --------------------------------------- //
	private void initialiseMap(GameWorld gameWorld, int selectionChoice)
	{
		// Load the map and set up the obstacles
		tiledMap = new TmxMapLoader().load(AssetHandler.maps[selectionChoice]);
		tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);
		tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
		mapHeight = tiledMap.getProperties().get("height", Integer.class) * tileHeight; 
		mapWidth = tiledMap.getProperties().get("width", Integer.class) * tileWidth;
		
		for(MapObject object : tiledMap.getLayers().get("Obstacles").getObjects()) {
			if(object instanceof RectangleMapObject) {
				float x = ((RectangleMapObject) object).getRectangle().x;
				float y = ((RectangleMapObject) object).getRectangle().y;
				float width = ((RectangleMapObject) object).getRectangle().width;
				float height = ((RectangleMapObject) object).getRectangle().height;
				gameWorld.staticObjects.add(new Obstacle(gameWorld.worldBox2D, x, y, width * 15, height * 15, 0));
			}
			else if(object instanceof EllipseMapObject){
				float x = ((EllipseMapObject) object).getEllipse().x;
				float y = ((EllipseMapObject) object).getEllipse().y;
				float width = ((EllipseMapObject) object).getEllipse().width;
				float height = ((EllipseMapObject) object).getEllipse().height;
				gameWorld.staticObjects.add(new PowerUpContainer(gameWorld.worldBox2D, x, y, width * 15, height * 15, 0));
			}
		}
		for(MapObject object : tiledMap.getLayers().get("Checkpoints").getObjects()) {
			if(object instanceof RectangleMapObject) {
				float x = ((RectangleMapObject) object).getRectangle().x;
				float y = ((RectangleMapObject) object).getRectangle().y;
				float width = ((RectangleMapObject) object).getRectangle().width;
				float height = ((RectangleMapObject) object).getRectangle().height;
				String id = ((RectangleMapObject) object).getName();
				gameWorld.checkpoints.add(new Checkpoint(gameWorld.worldBox2D, x, y, width * 15, height * 15, 0, id));
			}
		}
	}
	
	// ------------------------- Tile access functions  ---------------------------//
	public void getCellFromPosition(Vector2 position)
	{
		
	}
	
	public float getFrictionFromCell(TiledMapTileLayer.Cell cell)
	{
		return 1;
	}
	
	// ---------------------------- Getters and setters --------------------------//
	public int getMapHeight() 
	{
		return this.mapHeight;
	}
	
	public int getMapWidth()
	{
		return this.mapWidth;
	}
	
	public TiledMap getTiledMap()
	{
		return this.tiledMap;
	}
	
	public int getTileHeight()
	{
		return this.tileHeight;
	}
	
	public int getTileWidth()
	{
		return this.tileWidth;
	}
	
	
}
