package com.talas777.ZombieLord;

import java.util.LinkedList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
//import com.badlogic.gdx.graphics.g2d.tiled.*;

public class ZombieLord implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Texture texture2;
	private Sprite sprite;
	private LinkedList<Sprite> drawSprites;
	private Sprite princess;
	private float w;
	private float h;
	private Sprite background;
	private Texture backgroundTexture;
	private Sprite collisionLayer;
	public float posx = 1745;
	public float posy = 310;
	public float currentWalkFrame = 0;
	public float stillTime = 0;
	public int lastDirection = 0; // 0 = south, 1 = north, 2 = east, 3 = west
	
	
	private World world;
	private Body jumper;
	
	private Box2DDebugRenderer debugRenderer;
	
	private float moveSpeed = 0.014f;
	
	public static final float PIXELS_PER_METER = 60.0f;
	
	/*TileMapRenderer tileMapRenderer;
    TiledMap map;
    TileAtlas atlas;*/
	
	@Override
	public void create() {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		
		
		/*camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);*/
		camera = new OrthographicCamera(w, h);
		camera.position.set(0, 0, 0);
		batch = new SpriteBatch();
		drawSprites = new LinkedList<Sprite>();
		
		// Load the tmx file into map
		/*FileHandle fi = Gdx.files.internal("data/hometown.tmx");
		if(!fi.exists())
			System.err.println("no file here!");
        map = TiledLoader.createMap(fi);

        // Load the tiles into atlas
        atlas = new TileAtlas(map, Gdx.files.internal("data/"));

        // Create the renderer
        tileMapRenderer = new TileMapRenderer(map, atlas, 32, 32, 5, 5);

	    */
		backgroundTexture = new Texture(Gdx.files.internal("data/hometown.png"));
		//TextureRegion backgroundTex = new TextureRegion(texture, 0, 0, 3200, 3200);
		background = new Sprite(backgroundTexture, 0, 0, 3200, 3200);
		drawSprites.add(background);
		texture = new Texture(Gdx.files.internal("data/col1-test.png"));
		collisionLayer = new Sprite(texture, 0, 0, 3200, 3200);
		drawSprites.add(collisionLayer);
		
		{
			texture = new Texture(Gdx.files.internal("data/libgdx.png"));
			//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
			
			sprite = new Sprite(region);
			sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
			sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			sprite.setPosition(20, 20);
			drawSprites.add(sprite);
		}
		{
			texture2 = new Texture(Gdx.files.internal("data/princess.png"));
			//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			princess = new Sprite(texture2, 0, 64*2, 64, 64);
			//princess.setSize(64, 64);
			//sprite.setSize(sprite.getWidth(),sprite.getHeight());
			//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			princess.setPosition(posx-32, posy);
			drawSprites.add(princess);
		}
		
		world = new World(new Vector2(0.0f, 0.0f), true);

		BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = BodyDef.BodyType.DynamicBody;
		jumperBodyDef.position.set(posx/PIXELS_PER_METER, posy/PIXELS_PER_METER);

		jumper = world.createBody(jumperBodyDef);
		
		PolygonShape jumperShape = new PolygonShape();
		jumperShape.setAsBox(princess.getWidth() / (4 * PIXELS_PER_METER),
				princess.getHeight() / (2 * PIXELS_PER_METER));
		
		jumper.setFixedRotation(true);
		
		FixtureDef jumperFixtureDef = new FixtureDef();
		jumperFixtureDef.shape = jumperShape;
		jumperFixtureDef.density = 0.1f;
		jumperFixtureDef.friction = 1.0f;

		jumper.createFixture(jumperFixtureDef);
		jumper.setLinearDamping(9.0f);
		jumperShape.dispose();
		
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		{
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = new Vector2[3];
			vertices[0] = new Vector2(1,2);
			vertices[1] = new Vector2(4,8);
			vertices[2] = new Vector2(8,4);

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		
		debugRenderer = new Box2DDebugRenderer();
		
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		texture2.dispose();
	}

	@Override
	public void render() {
		
		
		
		
		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;
		
		
		if(Gdx.input.isKeyPressed(Keys.UP)){
			//System.out.println("damop:"+jumper.getLinearDamping());
			jumper.applyLinearImpulse(new Vector2(0.0f, moveSpeed),
					jumper.getWorldCenter());
			posy += (100 * Gdx.graphics.getDeltaTime());
			up = true;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			jumper.applyLinearImpulse(new Vector2(0.0f, -moveSpeed),
					jumper.getWorldCenter());
			posy -= (100 * Gdx.graphics.getDeltaTime());
			down = true;
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT) && !up && !down){
			jumper.applyLinearImpulse(new Vector2(-moveSpeed, 0.0f),
					jumper.getWorldCenter());
			posx -= (100 * Gdx.graphics.getDeltaTime());
			left = true;
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && !up && !down){
			jumper.applyLinearImpulse(new Vector2(moveSpeed, 0.0f),
					jumper.getWorldCenter());
			posx += (100 * Gdx.graphics.getDeltaTime());
			right = true;
		}
		
		if(Gdx.input.isKeyPressed(Keys.B)){
			System.out.println("position: x="+posx+", y="+posy);
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();
		
		//camera.lookAt(princess.getX(), princess.getY(), 0);
		//background.scroll(posx, posy);
		//background.
		
		/*background.setX(posx);
		background.setY(posy);
		collisionLayer.setX(posx);
		collisionLayer.setY(posy);
		*/

		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		
		posx = jumper.getPosition().x*this.PIXELS_PER_METER;
		posy = jumper.getPosition().y*this.PIXELS_PER_METER;
		princess.setX(posx-32);
		princess.setY(posy-32);
		
		camera.position.x = princess.getX()+32;
		camera.position.y = princess.getY()+16;
		
		
		
		if(camera.position.y <= h/2)
			camera.position.y = h/2+1;
		else if(camera.position.y >= 3200-h/2)
			camera.position.y = 3200-h/2-1;
		
		if(camera.position.x <= w/2)
			camera.position.x = w/2+1;
		else if(camera.position.x >= 3200-w/2)
			camera.position.x = 3200-w/2-1;
		
		camera.update();
		
		
		if(stillTime > 0)
			stillTime -= Gdx.graphics.getDeltaTime();

		int frame = (int)Math.ceil(currentWalkFrame)+1;
		if(up == true){
			lastDirection = 0;
			princess.setRegion(64*frame, 0, 64, 64);
			//princess = new Sprite(texture2, 0, 64*2, 64, 0);
		}
		else if(down == true){
			lastDirection = 1;
			princess.setRegion(64*frame, 64*2, 64, 64);
			//princess = new Sprite(texture2, 0, 64*2, 64, 64);
		}
		else if (left == true){
			lastDirection = 3;
			princess.setRegion(64*frame, 64*1, 64, 64);
			//princess = new Sprite(texture2, 0, 64*2, 64, 128);
		}
		else if (right == true){
			princess.setRegion(64*frame, 64*3, 64, 64);
			lastDirection = 2;
			//princess = new Sprite(texture2, 0, 64*2, 64, 128+64);
		}
		
		if(up || down || left || right){
			currentWalkFrame = (float)(currentWalkFrame >= 7-0.15? 0 : currentWalkFrame+0.15);
			stillTime = 0.1f;
		}
		else if(stillTime <= 0)
		{
			currentWalkFrame = 0;
			switch(lastDirection){
			case 0:
				//north
				princess.setRegion(0, 0, 64, 64);
				break;
			case 2:
				//east
				princess.setRegion(0, 64*3, 64, 64);
				break;
			case 3:
				//west
				princess.setRegion(0, 64*1, 64, 64);
				break;
			default:
				//south
				princess.setRegion(0, 64*2, 64, 64);
				break;
					
			}
		}
		
		//background = new Sprite(backgroundTexture, (int)(posx-w), (int)(posy-h), (int)(posx+w), (int)(posy+h));
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Sprite s : drawSprites)
		 s.draw(batch);
		batch.end();
		
		debugRenderer.render(world, camera.combined.scale(
				PIXELS_PER_METER,
				PIXELS_PER_METER,
				PIXELS_PER_METER));
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
