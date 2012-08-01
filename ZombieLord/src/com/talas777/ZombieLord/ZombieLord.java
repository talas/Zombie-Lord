/* Zombie Lord - A story driven roleplaying game
* Copyright (C) 2012  Talas (talas777@gmail.com)
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package com.talas777.ZombieLord;

import com.talas777.ZombieLord.Levels.*;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Array;

public class ZombieLord implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	//private Texture texture;
	private Texture texture2;
	//private Sprite sprite;
	private LinkedList<Sprite> drawSprites;
	private Sprite leoricSprite;
	private float w;
	private float h;
	private Sprite background;
	private Texture backgroundTexture;
	private Sprite foreground;
	public Sprite mover;
	public float moverTimer = 0;
	//private Sprite collisionLayer;
	public float posx = -1;
	public float posy = -1;
	public float currentWalkFrame = 0;
	public float stillTime = 0;
	public int lastDirection = 0;
	public float waitTime = 0;
	
	public int camMinY = 0;
	public int camMaxY = 0;
	
	public int camMinX = 0;
	public int camMaxX = 0;
	
	
	public static final int DIR_SOUTH = 1;
	public static final int DIR_NORTH = 0;
	public static final int DIR_EAST = 2;
	public static final int DIR_WEST = 3;
	
	/**
	 * For rain or meteor showers etc.
	 */
	public Array<Sprite> falling;
	public float fallSpeed = 700;
	public int minFallDist = 50;
	public int maxFallDist = 500;
	public Texture fallingTexture;
	public int fallingDensity = 100;
	
	private boolean debug = false;
	
	
	private World world;
	private Body jumper;
	private Body pointer;
	

	
	private Box2DDebugRenderer debugRenderer;
	
	private float moveSpeed = 0.003f;
	
	public static final float PIXELS_PER_METER = 60.0f;
	
	public Party party;
	
	private int gameMode = 0; // 0 = walk, 1 = combat, 2 = special/Minigame
	
	private Combat currentCombat;
	
	public Sound hitSound;
	
	public TimeTracker timeTracker;
	
	private LinkedList<MonsterArea> activeMonsterAreas;
	
	private LinkedList<Dialog> activeDialogs;
	
	public Dialog currentDialog;
	

	
	public static PartyMember Leoric;
	public static PartyMember Tolinai;
	public static PartyMember Bert;
	public static PartyMember Berzenor;
	public static PartyMember Kuriko;
	
	
	public static final CombatAction bite = new CombatAction("Bite",0, -4f, CombatAction.TARGET_ENEMY_SINGLE);
	public static final CombatAction punch = new CombatAction("Punch",0, -5f, CombatAction.TARGET_ENEMY_SINGLE);
	public static final CombatAction twinFist = new CombatAction("TwinFist",3,-10f,CombatAction.TARGET_ENEMY_SINGLE);
	public static final CombatAction regrowth = new CombatAction("Regrowth",5,50f,CombatAction.TARGET_SELF);
	public static final CombatAction slash = new CombatAction("Slash",0, -3f, CombatAction.TARGET_ENEMY_SINGLE);
	public static final CombatAction cycloneSlash = new CombatAction("Cyclone Slash",9,-20f,CombatAction.TARGET_ENEMY_ALL);
	public static final CombatAction magicArrow = new CombatAction("Magic Arrow", 8, -12f, CombatAction.TARGET_ENEMY_SINGLE);
	public static final CombatAction staffStrike = new CombatAction("Staff Strike",0, -1f, CombatAction.TARGET_ENEMY_SINGLE);
	
	public static final CombatOption escape = new CombatOption("escape");
	public static final CombatOption item = new CombatOption("item");
	public static final CombatOption defend = new CombatOption("defend");
	
	
	/*public static final String[] backgrounds = new String[]{
		"hometown.png", // 0
		"myhouse.png", // 1
		"church.png", // 2
		"hometown-night.png", // 3
		"battle1.png" // 4
	};*/
	
	
	/*TileMapRenderer tileMapRenderer;
    TiledMap map;
    TileAtlas atlas;*/
	
	private void returnFromCombat(){
		// TODO: figure out where we are..
		
		this.loadLevel(returnLevel, (int)posx, (int)posy, lastDirection);
	}
	
	private Level returnLevel;
	
	public void loadCombat(MonsterSetup setup){
		this.fallingTexture = null;
		for(Sprite s : drawSprites){
			s.getTexture().dispose();
		}
		if(this.foreground != null){
			this.foreground.getTexture().dispose();
			this.foreground = null;
		}
		this.drawSprites.clear();
		this.waitTime = 5;
		//TODO: set background
		
		String[] backgrounds = this.returnLevel.getBattleBackgrounds();
		
		this.backgroundTexture = new Texture(Gdx.files.internal("data/prerenders/"+backgrounds[(int)(Math.random()*backgrounds.length)  ]));
		this.background = new Sprite(backgroundTexture, 0, 0, 480, 320);
		
		drawSprites.add(this.background);
		
		//TODO: position player
		int bposx = (int)(w/6);
		int bposy = (int)(h/2);
		
		if(party.isActive(Tolinai)){
			Texture texture2 = new Texture(Gdx.files.internal("data/princess.png"));
			//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			Sprite tolinai = new Sprite(texture2, 0, 64*3, 64, 64);
			//princess.setSize(64, 64);
			//sprite.setSize(sprite.getWidth(),sprite.getHeight());
			//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			tolinai.setPosition(bposx-32, bposy+50);
			Tolinai.setSprite(tolinai);
			drawSprites.add(tolinai);
		}
		{// Leoric is always active
			//if(returnLevel instanceof HomeTownNight)
				//texture2 = new Texture(Gdx.files.internal("data/BRivera-malesoldier-dark.png"));
			//else
				texture2 = new Texture(Gdx.files.internal("data/BRivera-malesoldier.png"));
			//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			leoricSprite = new Sprite(texture2, 0, 64*2, 64, 64);
			//princess.setSize(64, 64);
			//sprite.setSize(sprite.getWidth(),sprite.getHeight());
			//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			leoricSprite.setPosition(bposx-32, bposy);
			Leoric.setSprite(leoricSprite);
			drawSprites.add(leoricSprite);
		}

		
		currentCombat = new Combat(setup, party, 5);

		
		{
			for(int i = 0; i < currentCombat.getNumEnemies(); i++){
				Texture monsterTexture = new Texture(Gdx.files.internal("data/"+currentCombat.getMonster(i).textureName));
				//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			
				Sprite monsterSprite = new Sprite(monsterTexture, 0, 0, 64, 64);
				
				currentCombat.getMonster(i).setSprite(monsterSprite);
				//princess.setSize(64, 64);
				//sprite.setSize(sprite.getWidth(),sprite.getHeight());
				//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
				int[] xy = currentCombat.getMonsterPosition(i, (int)w, (int)h);
				monsterSprite.setPosition(xy[0], xy[1]);
				drawSprites.add(monsterSprite);
				System.out.println("Added monster:x"+xy[0]+",y"+xy[1]);
			}
		}
		
		// Timing and stuff is taken care of in the Combat class (which is queried from the render loop)
		
		gameMode = 1;
	}
	
	public void loadCombat(MonsterArea monsterArea){
		loadCombat(monsterArea.getRandomSetup());
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
		
		timeTracker = new TimeTracker();
		timeTracker.addEvent("zero");
		timeTracker.addEvent("start");
		timeTracker.incrementTime();
		timeTracker.addEvent("go home");
		timeTracker.addEvent("talk with gf"); // talk
		timeTracker.addEvent("leave home"); // leave
		
		timeTracker.addEvent("east house?"); // goto house
		timeTracker.addEvent("east house!"); // enter house
		timeTracker.addEvent("east house-combat");
		timeTracker.addEvent("leave east house");
		
		timeTracker.addEvent("south east house?");
		
		timeTracker.addEvent("south west house?");
		timeTracker.addEvent("south west house!");
		timeTracker.addEvent("leave sw house");
		
		timeTracker.addEvent("west house?");
		timeTracker.addEvent("west house!");
		timeTracker.addEvent("leave west house");
		
		timeTracker.addEvent("mayors house?");
		timeTracker.addEvent("mayors house!");
		timeTracker.addEvent("leave mayors house");
		
		timeTracker.addEvent("leave hometown");
		
		timeTracker.addEvent("left hometown");
		
		
		timeTracker.setTime("left hometown"); // TODO: remove debug test
		
		
		Leoric = new PartyMember(0,"Leoric",250,250,5,5,0); // Male hero (swordsman)
		Leoric.addCombatAction(slash);
		Leoric.addCombatAction(cycloneSlash);
		
		party.addMember(Leoric);
		
		Tolinai = new PartyMember(1,"Tolinai",50,50,40,40,0); // Female, hero gf (offensive mage)
		Tolinai.addCombatAction(staffStrike);
		Tolinai.addCombatAction(magicArrow);
		
		Bert = new PartyMember(2,"Bert",250,250,5,5,0); // Male, archer
		Bert.addCombatAction(punch);
		
		Berzenor = new PartyMember(3,"Berzenor",250,250,5,5,0); // Male, defensive mage
		Berzenor.addCombatAction(punch);
		
		Kuriko = new PartyMember(4,"Kuriko",250,250,5,5,0); // Female, rogue
		Kuriko.addCombatAction(punch);
		
		/*
		this.addMember(new PartyMember(2,"Bert",50,50,10,10,0)); // Male, archer
		this.addMember(new PartyMember(3,"Berzenor",40,40,60,60,0)); // Male, defensive mage
		this.addMember(new PartyMember(4, "Kiriko",70,70,30,30,0)); // Female, rogue*/
		
		//loadLevel(new HomeTownNight(),1775,305,1); //hometown night
		//loadLevel(new Church(),522,414,1);// church the real start point
		
		loadLevel(new SecondTown(),117,1949,1);// second town
		
		MonsterArea area = new MonsterArea(0,0,20,20,0.05f);
		
		Monster troll = new Monster("Troll1","TrollOgre.png",5,100,15,3,1.25f);
		Monster troll2 = new Monster("Troll2","TrollOgre.png",5,100,15,3,1.25f);

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
		
		//loadCombat(4,area);

		// uncomment to enable box2d debug render mode, MAJOR SLOWDOWN! 
		debugRenderer = new Box2DDebugRenderer();
		
		
	}
	
	/*
	 * create a vector array from the given positions
	 * @param xValues x values
	 * @param yValues y values
	 * @return array of vector2 points
	 */
	/*public Vector2[] vectorize(float[] xValues, float[] yValues){
		Vector2[] vec = new Vector2[xValues.length];
		for(int i = 0; i < xValues.length; i++){
			vec[i] = new Vector2(xValues[i]/PIXELS_PER_METER, yValues[i]/PIXELS_PER_METER);
		}
		return vec;
	}*/

	@Override
	public void dispose() {
		batch.dispose();
		backgroundTexture.dispose();
		//texture.dispose();
		texture2.dispose();
	}
	
	public void loadLevel(Level level, int posx, int posy, int direction){
		this.fallingTexture = null;
		for(Sprite s : drawSprites){
			s.getTexture().dispose();
		}
		this.drawSprites.clear();
		if(backgroundTexture != null)
			backgroundTexture.dispose();
		//backgroundTexture = new Texture(Gdx.files.internal("data/"+backgrounds[levelCode]));
		backgroundTexture = new Texture(Gdx.files.internal("data/prerenders/"+level.getBackground()));
		
		if(this.foreground != null){
			this.foreground.getTexture().dispose();
			this.foreground = null;
		}
		
		if(level.getForeground() != null){
			
			// Level has a foreground image
			Texture foregroundTexture = new Texture(Gdx.files.internal("data/prerenders/"+level.getForeground()));
			
			foreground = level.foreground(foregroundTexture);
		}
		
		//TextureRegion backgroundTex = new TextureRegion(texture, 0, 0, 3200, 3200);
		
		this.camMinX = level.getCamMinX();
		this.camMaxX = level.getCamMaxX();
		this.camMinY = level.getCamMinY();
		this.camMaxY = level.getCamMaxY();
		
		this.posx = posx;
		this.posy = posy;
		this.lastDirection = direction;
		
		//switch (levelCode) {
		//case 0://hometown
			//background = new Sprite(backgroundTexture, 0, 0, 3200, 3200);
			/*lastDirection = 1;
			posx = 1775;
			posy = 305;
			break;*/
			// TODO: add music
		//case 1://my house
			//background = new Sprite(backgroundTexture, 0, 0, 3200, 3200);
			/*lastDirection = 1;
			posx = 1775;
			posy = 305;*/
			//break;
			// TODO: add music
		//case 2: //church
			//background = new Sprite(backgroundTexture, 0, 0, 1024, 1024);
			/*lastDirection = 1;
			posx = 522;
			posy = 414;*/
			//break;
		//case 3: //hometown night
			//background = new Sprite(backgroundTexture, 0, 0, 3200, 3200);

			/*lastDirection = 1;
			posx = 1775;
			posy = 305;*/
			// TODO: add wind effect
			// TODO: add ambient sounds
			//break;
		//default:
			//System.err.println("Case Switched, Learn2Code talas!");
		//}
		background = level.background(backgroundTexture);
			
		drawSprites.add(background);
		
		this.activeMonsterAreas = level.getMonsterAreas(timeTracker);
		this.activeDialogs = level.getLevelDialogs();
		
		
		// Hard Coded strange things goes here.. SPOLIER ALERT, etc..
		
		if(level instanceof HomeTownNight)
			this.fallingTexture = new Texture(Gdx.files.internal("data/raindrop.png"));
		

		if(level instanceof Church){
			Texture to1 = new Texture(Gdx.files.internal("data/FBI_hurt.png"));
			this.mover = new Sprite(to1, 64*1,0,64,64);
			this.mover.setX(583-32);
			this.mover.setY(483);
			drawSprites.add(mover);
			this.moverTimer = 0.4f;
		}
		if(level instanceof MyHouse){
			Texture to1 = new Texture(Gdx.files.internal("data/princess2.png"));
			this.mover = new Sprite(to1, 0,64*2,64,64);
			this.mover.setX(475);
			this.mover.setY(326);
			drawSprites.add(mover);
			this.moverTimer = 0.4f;
		}
		
		
		
		
		
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
			if(level instanceof HomeTownNight)
				texture2 = new Texture(Gdx.files.internal("data/BRivera-malesoldier-dark.png"));
			else
				texture2 = new Texture(Gdx.files.internal("data/BRivera-malesoldier.png"));
			//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			if(direction == DIR_NORTH)
				leoricSprite = new Sprite(texture2, 0, 64*0, 64, 64);
			else if(direction == DIR_SOUTH)
				leoricSprite = new Sprite(texture2, 0, 64*2, 64, 64);
			else if(direction == DIR_EAST)
				leoricSprite = new Sprite(texture2, 0, 64*3, 64, 64);
			else
				leoricSprite = new Sprite(texture2, 0, 64*1, 64, 64);
			//princess.setSize(64, 64);
			//sprite.setSize(sprite.getWidth(),sprite.getHeight());
			//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			leoricSprite.setPosition(posx-32, posy);
			drawSprites.add(leoricSprite);
		}
		
		world = new World(new Vector2(0.0f, 0.0f), true);
		
		level.applyCollisionBoundaries(world, PIXELS_PER_METER);

		BodyDef jumperBodyDef = new BodyDef();
		jumperBodyDef.type = BodyDef.BodyType.DynamicBody;
		jumperBodyDef.position.set(posx/PIXELS_PER_METER, posy/PIXELS_PER_METER);

		jumper = world.createBody(jumperBodyDef);
		
		PolygonShape jumperShape = new PolygonShape();
		jumperShape.setAsBox(leoricSprite.getWidth() / (6 * PIXELS_PER_METER),
				leoricSprite.getHeight() / (6 * PIXELS_PER_METER));
		
		jumper.setFixedRotation(true);
		
		FixtureDef jumperFixtureDef = new FixtureDef();
		jumperFixtureDef.shape = jumperShape;
		jumperFixtureDef.density = 0.1f;
		jumperFixtureDef.friction = 1.0f;

		jumper.createFixture(jumperFixtureDef);
		jumper.setLinearDamping(9.0f);
		jumperShape.dispose();
		
		BodyDef jt = new BodyDef();
		jt.type = BodyDef.BodyType.DynamicBody;
		jt.position.set(posx/PIXELS_PER_METER, posy/PIXELS_PER_METER);
		
		
		pointer = world.createBody( jt  );
		
		PolygonShape js = new PolygonShape();
		js.setAsBox(0.01f, 0.01f);
		
		FixtureDef jf = new FixtureDef();
		jf.shape = js;
		jf.filter.maskBits = 2;
		
		
		pointer.createFixture(jf);
		
		
		gameMode = 0;
		this.returnLevel = level; // incase we get into a fight, we want a way back :p
	}

	@Override
	public void render() {
		
		if(this.fallingTexture != null){
			if(this.falling == null){
				this.falling = new Array<Sprite>();
			}
			//System.out.println("rain: "+this.falling.size);
			// pour de rain (Count de money)
			//for(int i = 0; i < this.fallingDensity; i++){
			if(this.falling.size < this.fallingDensity){
				Sprite newDrop = new Sprite(fallingTexture,0,0,8,8);
				newDrop.setY(posy+h);
				newDrop.setX(posx-20-w/2+(float)Math.random()*(w+20));
				this.falling.add(newDrop);
				this.drawSprites.add(newDrop);
			}
			Iterator<Sprite> iter = falling.iterator();
			while(iter.hasNext()){
				Sprite s = iter.next();
				s.setY(s.getY()-this.fallSpeed*Gdx.graphics.getDeltaTime());
				if(h-s.getY() > this.minFallDist){
					// OK to delete?
					if(h-s.getY() >= this.maxFallDist){
						this.drawSprites.remove(s);
						iter.remove();
					}
					else if(Math.random()*100 > 90){
						this.drawSprites.remove(s);
						iter.remove();
					}
					
				}
			}
		}
		else if(this.falling != null){
			// Clean up rain..
			Iterator<Sprite> iter = falling.iterator();
			while(iter.hasNext()){
				Sprite s = iter.next();
				this.drawSprites.remove(s);
				iter.remove();
			}
			this.falling = null;
		}
		
		
		
		if(gameMode == 7){
			// a Dialog is active;
			if(this.currentDialog.hasNextUtterance()){
				//TODO: this has to be done more nicely somehow..
				Utterance u = this.currentDialog.getNextUtterance();
				
				System.out.print(u.speaker+": ");
				System.out.println(u.sentence);
			}
			else {
				// apply secondary effects and change gameMode back to whatever normal is
				// TODO: would be nice if dialogs could be had inside combat also..
				// but not strictly required.
				
				if(this.currentDialog.getTimeChange() != null){
					this.timeTracker.setTime( this.currentDialog.getTimeChange()  );
				}
				
				MonsterSetup setup = this.currentDialog.getFight();
				

				for(PartyMember joiner : this.currentDialog.getMemberJoins()){
					this.party.addMember(joiner);
				}

				
				if(this.currentDialog.nextLevel != null){
					// Level change..
					
					if(setup != null){
						// after fight..
						this.returnLevel = this.currentDialog.nextLevel;
						this.posx = this.currentDialog.posx;
						this.posy = this.currentDialog.posy;
						this.lastDirection = this.currentDialog.direction;
					}
				}
				if(setup != null){
					// fight!
					this.loadCombat(setup);
				}
				else if(this.currentDialog.nextLevel != null){
					// change level
					
					this.loadLevel(this.currentDialog.nextLevel, this.currentDialog.posx, this.currentDialog.posy,
							this.currentDialog.direction);
				}
				else {
					// No combat, no Level change..
					// Guess we just have to go back to normal then :<
					this.gameMode = 0;
					this.currentDialog = null;
					return;
				}
				
			}
		}
		
		
		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;
		
		if(gameMode == 0){
			if(returnLevel instanceof MyHouse){
				if(moverTimer > 0){
					// load princess
					this.moverTimer -= Gdx.graphics.getDeltaTime();
					
					if(moverTimer <= 0){
						this.mover.setTexture(new Texture(Gdx.files.internal("data/princess.png")));
						mover.setRegion(0, 64*2, 64, 64);
					}
				}
				else if(this.timeTracker.hasOccured("talk with gf")){
					// unload princess
					this.mover.getTexture().dispose();
					this.drawSprites.remove(mover);
				}
			}
			if(returnLevel instanceof Church){
				if(this.timeTracker.hasOccured(("zero"))){
					// unload dead priest
					this.mover.getTexture().dispose();
					this.drawSprites.remove(mover);
				}
			}
			
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
			leoricSprite.setX(posx-32);
			leoricSprite.setY(posy-10);
			pointer.setTransform(jumper.getPosition().x, jumper.getPosition().y, 0);
			
			camera.position.x = leoricSprite.getX()+32;
			camera.position.y = leoricSprite.getY()+16;
			
			
			
			// TODO: check if player has entered some interesting area
			
			if(left || right || up || down){
				// Only attract monsters when moving
				
				if(this.activeDialogs != null && this.activeDialogs.size() > 0){
					for(Dialog d : activeDialogs){
						
						if(d.isInside((int)posx, (int)posy, timeTracker)){
							boolean activate = false;
							// button?
							if(d.button != 0){
								activate = Gdx.input.isKeyPressed(d.button);
							}
							else
								activate = true;
							
							if(activate){
								// Start the dialog
								this.gameMode = 7;
								this.currentDialog = d;
								return;
							}
							
						}
					}
				}
				
				if(this.activeMonsterAreas != null && this.activeMonsterAreas.size() > 0){
					
						
					for(MonsterArea area : activeMonsterAreas){
						// check if player is inside it
						if(area.isInside((int)posx, (int)posy)){
							
							// roll a dice to find out if the player should be attacked.
							float num = (float)Math.random();
							
							//if(Math.random()*100>90)
								//System.out.println(num+" v.s. "+area.encounterChance);
							
							if(num < area.encounterChance){
								// we have a winner..
								this.loadCombat(area);
								return; // TODO: not sure if this is a good idea or bad
								// either return or break..
							}
							
						}
						
					}
				}
			}
			
			
			if(Gdx.input.isKeyPressed(Keys.B)){
				System.out.println("position: x="+posx+", y="+posy+", time="+timeTracker.getTime() + ", cam:["+camera.position.x+","+camera.position.y+"].");
			}
			
			if(Gdx.input.isKeyPressed(Keys.D)){
				this.debug = ! this.debug;
				System.out.println("debug :"+(this.debug?"On":"Off"));
			}
			
			
			// Keep the camera inside the level..
			if(camera.position.y <= h/2+this.camMinY)
				camera.position.y = h/2+1+this.camMinY;
			else if(camera.position.y >= this.camMaxY-h/2)
				camera.position.y = this.camMaxY-h/2-1;
			
			if(camera.position.x <= w/2+this.camMinX)
				camera.position.x = w/2+1+this.camMinX;
			else if(camera.position.x >= this.camMaxX-w/2)
				camera.position.x = this.camMaxX-w/2-1;
			
			camera.update();
			
			
			if(stillTime > 0)
				stillTime -= Gdx.graphics.getDeltaTime();
	
			int frame = (int)Math.ceil(currentWalkFrame)+0;
			if(up == true){
				lastDirection = 0;
				leoricSprite.setRegion(64*frame, 0, 64, 64);
				//princess = new Sprite(texture2, 0, 64*2, 64, 0);
			}
			else if(down == true){
				lastDirection = 1;
				leoricSprite.setRegion(64*frame, 64*2, 64, 64);
				//princess = new Sprite(texture2, 0, 64*2, 64, 64);
			}
			else if (left == true){
				lastDirection = 3;
				leoricSprite.setRegion(64*frame, 64*1, 64, 64);
				//princess = new Sprite(texture2, 0, 64*2, 64, 128);
			}
			else if (right == true){
				leoricSprite.setRegion(64*frame, 64*3, 64, 64);
				lastDirection = 2;
				//princess = new Sprite(texture2, 0, 64*2, 64, 128+64);
			}
			
			if(left || right){
				currentWalkFrame = (float)(currentWalkFrame >= 7-0.20? 0 : currentWalkFrame+0.10);
				stillTime = 0.1f;
			}
			else if(up || down || left || right){
				currentWalkFrame = (float)(currentWalkFrame >= 8-0.10? 1 : currentWalkFrame+0.10);
				stillTime = 0.1f;
			}
			else if(stillTime <= 0)
			{ // stop the animation.
				currentWalkFrame = 0;
				switch(lastDirection){
				case 0:
					//north
					leoricSprite.setRegion(0, 0, 64, 64);
					break;
				case 2:
					//east
					leoricSprite.setRegion(0, 64*3, 64, 64);
					break;
				case 3:
					//west
					leoricSprite.setRegion(0, 64*1, 64, 64);
					break;
				default:
					//south
					leoricSprite.setRegion(0, 64*2, 64, 64);
					break;
						
				}
			}
		}
		
		boolean waiting = false; //TODO: move this?
		
		if(waitTime > 0)
		{
			waiting = true;
			waitTime -= Gdx.graphics.getDeltaTime();
		}
		
		if(gameMode == 2){
			// "After Combat" screen
			if(currentCombat != null){
				// clean up!
				for(Sprite s : drawSprites){
					//s.getTexture().dispose();
				}
				drawSprites.clear();
				
				currentCombat.cleanUp();
				currentCombat = null;
				waitTime = 10f;
				waiting = true;
				//TODO: display some info about the fight..
				
				this.backgroundTexture = new Texture(Gdx.files.internal("data/victory.png"));
				this.background = new Sprite(backgroundTexture, 0, 0, 480, 320);
				
				drawSprites.add(this.background);
				
			}
			
			
			if(!waiting || this.debug){
				waiting = false;
				waitTime = 0;
				this.returnFromCombat();
				return; // TODO: not sure if this is a good idea or bad
			}
		}
		
		else if(gameMode == 99){
			// Game over
			//TODO: write something 'nice' to the screen?
			//TODO: nice musics?
			this.backgroundTexture = new Texture(Gdx.files.internal("data/gameover.png"));
			this.background = new Sprite(backgroundTexture, 0, 0, 480, 320);
			
			drawSprites.add(this.background);
		}
		
		else if(gameMode == 1){
			
			// Re-position all creatures..
			for(int i = 0; i < currentCombat.getLiveCombatants().size(); i++){
				Combatant current = currentCombat.getLiveCombatants().get(i);
				
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
				
				for(int i = 0; i < currentCombat.getLiveCombatants().size(); i++){
					currentCombat.getLiveCombatants().get(i).setMoveAhead(false);
				}
				
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
					gameMode = 99;
				}
				
				//TODO: render all dead/fainted characters as such
				
				//TODO: render status changes (those that are visible)
				
				Monster readyMonster = currentCombat.getFirstReadiedMonster();
				if(readyMonster != null){
					
					CurrentAction myAction = readyMonster.getMonsterAction(party, currentCombat);
					
					currentCombat.applyAction(myAction);
					
					if(myAction.action != null){
						// Move monster against player to represent the attack
						
						// TODO: some sort of graphical representation of the attack.. effects and such
						myAction.caster.setMoveAhead(true);
					}
					
					readyMonster.actionTimer = readyMonster.getBaseDelay()*(2f*Math.random());// TODO: randomize better?
					
					waiting = true;
					waitTime = 2;
					// Only 1 attacker per turn.
				}
				if(!waiting){
					PartyMember readyMember = currentCombat.getFirstReadiedPlayer();
					if(readyMember != null){
						
						// Find the options..
						
						LinkedList<CombatOption> combatOptions = new LinkedList<CombatOption>();
						

						
						if(currentCombat.isEscapeAllowed()){
							combatOptions.add(escape); // RUN AWAAY!!
						}
						if(currentCombat.isItemAllowed() && party.hasCombatItem()){
							combatOptions.add(item); // use some item (potion, etc..)
						}
						combatOptions.add(defend); // spend the turn to increase defense (+50% def to ALL damage)
						
						for(CombatAction ca : readyMember.getCombatActions()){
							if(ca.mpCost <= readyMember.getMana()) {
								combatOptions.add(new CombatOption(ca.name, ca));
							}
						}
						
						//TODO: serve combat options to player
						//TODO: in some sort of menu
						//TODO: that lets the player select
						//TODO: and then uses the selected option/action
						
						System.out.print("Actions available for "+readyMember.getName()+":");
						for(CombatOption co : combatOptions){
							System.out.print(" "+co.name);
						}
						System.out.println(".");
						
						int chosen = (int)(Math.random()*readyMember.getCombatActions().size());
						
						CurrentAction myAction = new CurrentAction(readyMember.getCombatActions().get(chosen), readyMember, currentCombat.getLiveMonsters().getFirst());
						currentCombat.applyAction(myAction);
						
						if(myAction.action != null){
							// Move player against monster to represent the attack
							
							// TODO: some sort of graphical representation of the attack.. effects and such
							myAction.caster.setMoveAhead(true);
						}
						if(this.debug){
							// time to stop this nonsense!
							for(Monster m : currentCombat.getLiveMonsters()){
								m.health = 0;
							}
						}
						
						readyMember.actionTimer = readyMember.getBaseDelay()*(2f*Math.random());// TODO: randomize better?
						
						waiting = true;
						waitTime = 2;
						
					}
					
				}
			}
			
			leoricSprite.setRegion(0, 64*3, 64, 64);
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
		
		if(gameMode == 0 && this.foreground != null) // drawing foreground last = ontop of everything else
			foreground.draw(batch);
		
		batch.end();
		
		if(debug && world != null && debugRenderer != null)
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
