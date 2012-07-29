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
	//private Texture texture;
	private Texture texture2;
	//private Sprite sprite;
	private LinkedList<Sprite> drawSprites;
	private Sprite princess;
	private float w;
	private float h;
	private Sprite background;
	private Texture backgroundTexture;
	//private Sprite collisionLayer;
	public float posx = -1;
	public float posy = -1;
	public float currentWalkFrame = 0;
	public float stillTime = 0;
	public int lastDirection = 0; // 0 = south, 1 = north, 2 = east, 3 = west
	public float waitTime = 0;
	
	
	private World world;
	private Body jumper;
	
	private Box2DDebugRenderer debugRenderer;
	
	private float moveSpeed = 0.003f;
	
	public static final float PIXELS_PER_METER = 60.0f;
	
	public Party party;
	
	private int gameMode = 0; // 0 = walk, 1 = combat, 2 = special/Minigame
	
	private Combat currentCombat;
	
	public static final String[] backgrounds = new String[]{
		"hometown.png", // 0
		"myhouse.png", // 1
		"church.png", // 2
		"hometown-night.png", // 3
		"battle1.png" // 4
	};
	
	
	/*TileMapRenderer tileMapRenderer;
    TiledMap map;
    TileAtlas atlas;*/
	
	public void loadCombat(int background, MonsterArea monsterArea){
		this.drawSprites.clear();
		this.waitTime = 5;
		//TODO: set background
		
		this.backgroundTexture = new Texture(Gdx.files.internal("data/"+backgrounds[background]));
		this.background = new Sprite(backgroundTexture, 0, 0, 480, 320);
		
		drawSprites.add(this.background);
		
		//TODO: position player
		int bposx = (int)(w/6);
		int bposy = (int)(h/2);
		
		{
			texture2 = new Texture(Gdx.files.internal("data/princess.png"));
			//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			princess = new Sprite(texture2, 0, 64*2, 64, 64);
			//princess.setSize(64, 64);
			//sprite.setSize(sprite.getWidth(),sprite.getHeight());
			//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			princess.setPosition(bposx-32, bposy);
			drawSprites.add(princess);
		}
		
		currentCombat = new Combat(monsterArea.getRandomSetup(), party, 5);

		
		{
			for(int i = 0; i < currentCombat.getNumEnemies(); i++){
				Texture trollTex = new Texture(Gdx.files.internal("data/"+currentCombat.getMonster(i).textureName));
				//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			
				Sprite trollSprite = new Sprite(trollTex, 0, 0, 64, 64);
				
				currentCombat.getMonster(i).setSprite(trollSprite);
				//princess.setSize(64, 64);
				//sprite.setSize(sprite.getWidth(),sprite.getHeight());
				//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
				int[] xy = currentCombat.getMonsterPosition(i, (int)w, (int)h);
				trollSprite.setPosition(xy[0], xy[1]);
				drawSprites.add(trollSprite);
				System.out.println("Added monster:x"+xy[0]+",y"+xy[1]);
			}
		}
		
		//TODO: position monsters
		
		// Timing and stuff is taken care of in the Combat class (which is queried from the render loop)
		
		gameMode = 1;
	}
	
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
		

		
		party = new Party();
		
		party.addMember(new PartyMember(0,"Leoric",100,100,5,5,0)); // Male hero (swordsman)
		/*party.addMember(new PartyMember(1,"Tolinai",25,25,80,80,0)); // Female, hero gf (black mage)
		this.addMember(new PartyMember(2,"Bert",50,50,10,10,0)); // Male, archer
		this.addMember(new PartyMember(3,"Berzenor",40,40,60,60,0)); // Male, white mage
		this.addMember(new PartyMember(4, "Kiriko",70,70,30,30,0)); // Female, rogue*/
		
		//loadLevel(3,1775,305,1); //hometown night
		//loadLevel(2,522,414,1); church
		
		
		MonsterArea area = new MonsterArea(0,0,20,20,0.05f);
		
		Monster troll = new Monster("Troll1","TrollOgre.png",5,100,15,3,1.25f);
		Monster troll2 = new Monster("Troll2","TrollOgre.png",5,100,15,3,1.25f);
		CombatAction bite = new CombatAction("Bite",0, -4f, CombatAction.TARGET_ENEMY_SINGLE);
		CombatAction punch = new CombatAction("Punch",0, -5f, CombatAction.TARGET_ENEMY_SINGLE);
		CombatAction twinFist = new CombatAction("TwinFist",3,-10f,CombatAction.TARGET_ENEMY_SINGLE);
		CombatAction regrowth = new CombatAction("Regrowth",5,50f,CombatAction.TARGET_SELF);
		troll.addCombatAction(twinFist, 0.2f);
		troll2.addCombatAction(twinFist, 0.2f);
		troll.addCombatAction(regrowth, 0.2f);
		troll2.addCombatAction(regrowth, 0.2f);
		troll.addCombatAction(punch, 0.2f);
		troll2.addCombatAction(punch, 0.2f);
		troll.addCombatAction(bite, 0.2f);
		troll2.addCombatAction(bite, 0.2f);
		MonsterSetup twoTrolls = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
		twoTrolls.appendMonster(troll);
		twoTrolls.appendMonster(troll2);
		area.addMonsterSetup(twoTrolls, 0.7f);
		
		loadCombat(4,area);

		// uncomment to enable box2d debug render mode, MAJOR SLOWDOWN! 
		//debugRenderer = new Box2DDebugRenderer();
		
		
	}
	/**
	 * create a vector array from the given positions
	 * @param xValues x values
	 * @param yValues y values
	 * @return array of vector2 points
	 */
	public Vector2[] vectorize(float[] xValues, float[] yValues){
		Vector2[] vec = new Vector2[xValues.length];
		for(int i = 0; i < xValues.length; i++){
			vec[i] = new Vector2(xValues[i]/PIXELS_PER_METER, yValues[i]/PIXELS_PER_METER);
		}
		return vec;
	}

	@Override
	public void dispose() {
		batch.dispose();
		backgroundTexture.dispose();
		//texture.dispose();
		texture2.dispose();
	}
	
	public void loadLevel(int levelCode, int posx, int posy, int direction){
		if(backgroundTexture != null)
			backgroundTexture.dispose();
		backgroundTexture = new Texture(Gdx.files.internal("data/"+backgrounds[levelCode]));
		//TextureRegion backgroundTex = new TextureRegion(texture, 0, 0, 3200, 3200);
		
		this.posx = posx;
		this.posy = posy;
		this.lastDirection = direction;
		
		switch (levelCode) {
		case 0://hometown
			background = new Sprite(backgroundTexture, 0, 0, 3200, 3200);
			/*lastDirection = 1;
			posx = 1775;
			posy = 305;
			break;*/
			// TODO: add music
		case 1://my house
			background = new Sprite(backgroundTexture, 0, 0, 3200, 3200);
			/*lastDirection = 1;
			posx = 1775;
			posy = 305;*/
			break;
			// TODO: add music
		case 2: //church
			background = new Sprite(backgroundTexture, 0, 0, 1024, 1024);
			/*lastDirection = 1;
			posx = 522;
			posy = 414;*/
			break;
		case 3: //hometown night
			background = new Sprite(backgroundTexture, 0, 0, 3200, 3200);
			/*lastDirection = 1;
			posx = 1775;
			posy = 305;*/
			// TODO: add rain effect
			// TODO: add wind effect
			// TODO: add ambient sounds
			break;
		default:
			System.err.println("Case Switched, Learn2Code talas!");
		}
		
		drawSprites.add(background);
		/*
		texture = new Texture(Gdx.files.internal("data/col1-test.png"));
		collisionLayer = new Sprite(texture, 0, 0, 3200, 3200);
		drawSprites.add(collisionLayer);
		*/
		
		/*{
			texture = new Texture(Gdx.files.internal("data/libgdx.png"));
			//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
			
			sprite = new Sprite(region);
			sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
			sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			sprite.setPosition(20, 20);
			drawSprites.add(sprite);
		}*/
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
		jumperShape.setAsBox(princess.getWidth() / (6 * PIXELS_PER_METER),
				princess.getHeight() / (6 * PIXELS_PER_METER));
		
		jumper.setFixedRotation(true);
		
		FixtureDef jumperFixtureDef = new FixtureDef();
		jumperFixtureDef.shape = jumperShape;
		jumperFixtureDef.density = 0.1f;
		jumperFixtureDef.friction = 1.0f;

		jumper.createFixture(jumperFixtureDef);
		jumper.setLinearDamping(9.0f);
		jumperShape.dispose();
		
		if(levelCode == 0 || levelCode == 3){
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.type = BodyDef.BodyType.StaticBody;
			Body groundBody = world.createBody(groundBodyDef);
			{
				ChainShape environmentShape = new ChainShape();
				
				Vector2[] vertices = vectorize(
						new float[]{1473,1473,1507,1700,1775,1861,1950,1985,1985,1795,1795,1758,1758},
						new float[]{322,450,480,480,565,480,480,450,322,322,342,342,322});
				/*vertices[0] = new Vector2(1473/PIXELS_PER_METER,322/PIXELS_PER_METER);
				vertices[1] = new Vector2(1473/PIXELS_PER_METER,480/PIXELS_PER_METER);
				vertices[2] = new Vector2(1985/PIXELS_PER_METER,480/PIXELS_PER_METER);
				vertices[3] = new Vector2(1985/PIXELS_PER_METER,322/PIXELS_PER_METER);*/
	
				environmentShape.createLoop(vertices);
				groundBody.createFixture(environmentShape, 0);
				environmentShape.dispose();
			}
		}
		gameMode = 0;
	}

	@Override
	public void render() {
		
		
		
		
		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;
		
		if(gameMode == 0){
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

		if(gameMode == 0){
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
			
			posx = jumper.getPosition().x*this.PIXELS_PER_METER;
			posy = jumper.getPosition().y*this.PIXELS_PER_METER;
			princess.setX(posx-32);
			princess.setY(posy-10);
			
			camera.position.x = princess.getX()+32;
			camera.position.y = princess.getY()+16;
			
			if(Gdx.input.isKeyPressed(Keys.B)){
				System.out.println("position: x="+posx+", y="+posy);
			}
			
			
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
		}
		
		boolean waiting = false; //TODO: move this and use this
		
		if(waitTime > 0)
		{
			waiting = true;
			waitTime -= Gdx.graphics.getDeltaTime();
		}
		
		if(gameMode == 1){
			
			// Re-position all creatures..
			for(int i = 0; i < currentCombat.getLiveCombatants().size(); i++){
				Combatant current = currentCombat.getLiveCombatants().get(i);
				if(current.posx != current.drawSprite.getX()){
					
				}
				if(current.health <= 0){
					// Render as dead/fainted
				}
				switch(current.getState()){
					case Combat.STATE_STONE:
						// render as grey (as stone)
						break;
					case Combat.STATE_POISONED:
						// render as green
						break;
					case Combat.STATE_FURY:
						// render as red
						break;
					case Combat.STATE_WEAKNESS:
						// render as pale yellow
						break;
					case Combat.STATE_DOOM:
						// render as dark
						break;
					
				}
			}
			
			//check if battle is over.
			if(!waiting){
				byte state = currentCombat.getBattleState();
				
				if(state == 1){
					// player has won
					waiting = true;
					//TODO: win combat (properly)
					System.out.println("VICTORY! :>");
					//TODO: happy trumpet
					gameMode = 2;
				}
				else if(state == 2){
					// player has lost
					waiting = true;
					// TODO: GAME OVER (properly)
					System.out.println("GAME OVER. :'(");
					// TODO: sad flute
					gameMode = 2;
				}
			}
			
			if(!waiting){
				//TODO: render all dead/fainted characters as such
				
				//TODO: render status changes (those that are visible)
				
				for(int i = 0; i < currentCombat.getLiveMonsters().size(); i++){
					//currentCombat.getLiveMonsters().get(i).setMoveAhead(false);
				}
				
				Monster readyMonster = currentCombat.getFirstReadiedMonster();
				if(readyMonster != null){
					
					CurrentAction myAction = readyMonster.getMonsterAction(party, currentCombat);
					
					currentCombat.applyAction(myAction);
					
					if(myAction.action != null){
						// Move monster against player to represent the attack
						
						// TODO: some sort of graphical representation of the attack.. effects and such
						//myAction.caster.setMoveAhead(true);
					}
					
					readyMonster.actionTimer = readyMonster.getBaseDelay()*(2f*Math.random());// TODO: randomize better?
					
					waiting = true;
					waitTime = 3;
					// Only 1 attacker per turn.
				}
				if(!waiting){
					//TODO: give players the options they have (if its their turn)
				}
			}
			
			princess.setRegion(0, 64*3, 64, 64);
			camera.position.x = w/2f;
			camera.position.y = h/2f;
			camera.update();
			if(!waiting){
				currentCombat.tick(Gdx.graphics.getDeltaTime()*5*2);
				//TODO: some sort of representation of combat timers (atleast for players..)
			}
		}
		
		
		//background = new Sprite(backgroundTexture, (int)(posx-w), (int)(posy-h), (int)(posx+w), (int)(posy+h));
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Sprite s : drawSprites)
		 s.draw(batch);
		batch.end();
		
		/*debugRenderer.render(world, camera.combined.scale(
				PIXELS_PER_METER,
				PIXELS_PER_METER,
				PIXELS_PER_METER));*/
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
