package com.not.itproject.handlers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.not.itproject.objects.Checkpoint;
import com.not.itproject.objects.GameVariables;
import com.not.itproject.objects.GameWorld;
import com.not.itproject.objects.Obstacle;
import com.not.itproject.objects.PowerUpContainer;

public class TiledMapHandler {
	// Map rendering
	private TiledMap tiledMap;
	private int mapHeight;
	private int mapWidth;
	private int tileHeight;
	private int tileWidth;
	private int numTilesWidth;
	private int numTilesHeight;

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
		numTilesHeight = tiledMap.getProperties().get("height", Integer.class);
		numTilesWidth = tiledMap.getProperties().get("width", Integer.class);
		mapHeight = numTilesHeight * tileHeight; 
		mapWidth = numTilesWidth * tileWidth;
		
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
	public float getFrictionFromPosition(Vector2 position)
	{
		int i = (int)position.x / tileWidth;
		int j = (int)position.y / tileHeight;
		i = MathUtils.clamp(i, 0, numTilesWidth - 1);
		j = MathUtils.clamp(j, 0, numTilesHeight - 1);
		
		TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("Grass");
		TiledMapTileLayer.Cell cell = (TiledMapTileLayer.Cell) layer.getCell(i, j);
		if(cell != null)
		{
			return GameVariables.GRASS_FRICTION_MODIFIER;
		}

		layer = (TiledMapTileLayer) tiledMap.getLayers().get("Road");
		cell = (TiledMapTileLayer.Cell) layer.getCell(i, j);
		if(cell != null)
		{
			return GameVariables.ROAD_FRICTION_MODIFIER;
		}
		return 1.0f;
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
	
	public MapObjects getStartingPositions()
	{
		return tiledMap.getLayers().get("Starting position").getObjects();
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
