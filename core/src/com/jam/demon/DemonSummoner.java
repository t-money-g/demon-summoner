package com.jam.demon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.css.Rect;


public class DemonSummoner extends ApplicationAdapter implements InputProcessor
{
	SpriteBatch batch;
	Texture img;
	TiledMap tiledMap;
	OrthographicCamera camera;
	Sprite sprite;
	Viewport viewport;
	Array<Rectangle> collideRects;
	Vector2 playerDirection;

	//camera map width and height
	private static final float VIRTUAL_WIDTH = 384.0f;
	private static final float VIRTUAL_HEIGHT = 216.0f;

	private static final float CAMERA_SPEED = 100.0f;

	OrthogonalTiledMapRendererWithSprites tiledMapRenderer;
	Vector2 direction;

	boolean isMovingX = false;
	boolean isMovingY = false;

	private final boolean renderInterrupted = true;

	Vector2 targetPos;

	float ScreenWidth = 800;
	float ScreenHeight = 480;

	float playerHeight = 29;
	float playerWidth = 24;

	float movementSpeed = 100;

	@Override
	public void create()
	{
		final float w = Gdx.graphics.getWidth();
		final float h = Gdx.graphics.getHeight();
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);

        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        batch = new SpriteBatch();

		img = new Texture(Gdx.files.internal("playerSprite_1.png"));


		// create a new sprite based on our image
		sprite = new Sprite(img);

		sprite.setPosition(ScreenWidth / 2 - playerWidth, ScreenHeight / 2 - playerHeight);
		tiledMap = new TmxMapLoader().load("ritualsite.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);
		tiledMapRenderer.addSprite(sprite);

		Gdx.input.setInputProcessor(this);
		targetPos = new Vector2(sprite.getX(), sprite.getY());

		//create a sprite batch
		batch = new SpriteBatch();

		direction = new Vector2();
		playerDirection = new Vector2();
		getCollidingRects();
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.

		updateCamera();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		if (Gdx.input.isTouched())
		{
			final Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			targetPos.x = touchPos.x - sprite.getWidth() / 2;
			targetPos.y = touchPos.y - sprite.getHeight() / 2;

		}

		updateTargetPosition();

	}

	private void updateTargetPosition()
	{

		//checkBoundsX();

		if ((targetPos.x - sprite.getX() > 1 || targetPos.x - sprite.getX() < -1) && isMovingY == false)
		{
			//Gdx.app.log("Target Pos", "Target pos is" + targetPos.toString());
			//Gdx.app.log("Sprite is at", "Target pos is" + sprite.getX() + ":" + sprite.getY());
			moveToPosition(sprite, targetPos.x, 0);
			isMovingX = true;

		}
		else
		{
			isMovingX = false;
		}
		if ((targetPos.y - sprite.getY() > 1 || targetPos.y - sprite.getY() < -1) && isMovingX == false)
		{

			moveToPosition(sprite, 0, targetPos.y);
			isMovingY = true;
		}
		else
		{
			isMovingY = false;
		}

	}

	public void moveToPosition(Sprite sprite, float x, float y)
	{
		final float currentX = sprite.getX();
		final float currentY = sprite.getY();

		//go to x first
		if (x > currentX && x != 0)
		{
			sprite.setX(currentX + (movementSpeed * Gdx.graphics.getDeltaTime()));
		}
		else if (x < currentX && x != 0)
		{
			sprite.setX(currentX - (movementSpeed * Gdx.graphics.getDeltaTime()));

		}

		if (y > currentY && y != 0)
		{

			sprite.setY(currentY + movementSpeed * Gdx.graphics.getDeltaTime());

		}
		else if (y < currentY && y != 0)
		{

			sprite.setY(currentY - movementSpeed * Gdx.graphics.getDeltaTime());
		}

	}

	public void getCollidingRects()
	{
		MapObjects objects = tiledMap.getLayers().get("phyics").getObjects();
		collideRects = new Array<Rectangle>();

		for (MapObject object : objects) {
			RectangleMapObject rectangleObject = (RectangleMapObject)object;
			collideRects.add(rectangleObject.getRectangle());

		}



	}

	public void checkBoundsX() {

		for(Rectangle rect : collideRects) {

			if(sprite.getBoundingRectangle().overlaps(rect)) {

				float boundCalc = 0.0f;
				if(direction.x < 0) {


					boundCalc = rect.x + rect.getWidth() + 0.01f;
				}
				else {

					boundCalc = rect.x - sprite.getWidth() - 0.01f;
				}

				//sprite.setX(boundCalc - .2f);
				targetPos.x = boundCalc - .2f;
				direction.x = 0;
				Gdx.app.log("SpriteOverlap", "Target pos is" + sprite.getX() + ":" + sprite.getY());

			}
		}


	}

    private void updateCamera() {
        direction.set(0.0f,0.0f);
        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && mouseX < width * 0.75f) ) {
            direction.x = -1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && mouseX > width * 0.75f)) {
            direction.x = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isTouched() && mouseY < height * 0.75f)) {
            direction.y = 1;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isTouched() && mouseY > height * 0.75f)) {
            direction.y = -1;
        }

        direction.nor().scl(CAMERA_SPEED * Gdx.graphics.getDeltaTime());

        camera.position.x += direction.x;
        camera.position.y += direction.y;

        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);

        float cameraMinX = viewport.getWorldWidth() * 0.5f;
        float cameraMinY = viewport.getWorldHeight() * 0.5f;
        float cameraMaxX = layer.getWidth() * layer.getTileWidth()  + (playerWidth - cameraMinX);
        float cameraMaxY = layer.getHeight() * layer.getTileHeight() - cameraMinY;

        camera.position.x = MathUtils.clamp(sprite.getX(), cameraMinX, cameraMaxX);
        camera.position.y= MathUtils.clamp(sprite.getY(), cameraMinY, cameraMaxY);

        camera.update();
    }

	private void processMapMetadata() {
		// Load music

		/*String songPath = map.getProperties().get("music", String.class);
		song = Gdx.audio.newMusic(Gdx.files.internal(songPath));
		song.setLooping(true);
		song.play();*/

		// Load entities
		System.out.println("Searching for game entities...\n");




		}
	@Override
	public void resize(final int width, final int height)
	{
        viewport.update(width, height);
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public boolean keyDown(final int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(final int keycode)
	{


		return false;
	}

	@Override
	public boolean keyTyped(final char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
	{/*
	  * final Vector3 clickCoordinates = new Vector3(screenX, screenY, 0); final Vector3 position =
	  * camera.unproject(clickCoordinates);
	  *
	  * final float currentx = sprite.getX(); final float currenty = sprite.getY();
	  *
	  * ;
	  *
	  * //sprite.setposition(position.x, position.y);
	  *
	  * //sprite.setV2(p);
	  *
	  */
		return false;
	}



	@Override
	public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, final int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(final int screenX, final int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(final int amount)
	{
		return false;
	}

	@Override
	public void dispose()
	{
		img.dispose();
		tiledMap.dispose();
		batch.dispose();
	}
}

