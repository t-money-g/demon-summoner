package com.jam.demon;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class OrthogonalTiledMapRendererWithSprites extends OrthogonalTiledMapRenderer
{
	private Sprite sprite;
	private final List<Sprite> sprites;
	private final int drawSpritesAfterLayer = 1;

	public OrthogonalTiledMapRendererWithSprites(final TiledMap map)
	{
		super(map);
		sprites = new ArrayList<Sprite>();
	}

	public void addSprite(final Sprite sprite)
	{
		sprites.add(sprite);
	}

	@Override
	public void render()
	{
		beginRender();
		int currentLayer = 0;
		for (final MapLayer layer : map.getLayers())
		{
			if (layer.isVisible())
			{
				if (layer instanceof TiledMapTileLayer)
				{
					renderTileLayer((TiledMapTileLayer) layer);
					currentLayer++;
					if (currentLayer == drawSpritesAfterLayer)
					{
						for (final Sprite sprite : sprites)
						{

							sprite.draw(this.getBatch());

						}
					}
				}
				else
				{
					for (final MapObject object : layer.getObjects())
					{
						renderObject(object);
					}
				}
			}
		}
		endRender();
	}
}
