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

import com.talas777.ZombieLord.Items.ConsumeableItem;
import com.talas777.ZombieLord.Levels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

public class ZombieLord implements ApplicationListener, InputProcessor {
	
	// Global Graphics stuff
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private SpriteBatch fontBatch;
	
	private Texture texture2;
	private LinkedList<Sprite> drawSprites;
	private Sprite leoricSprite;
	
	private float w; // width of resoultion
	private float h; // height of resolution
	
	private Sprite background;
	private Texture backgroundTexture;
	private Sprite foreground;
	
	public Sprite mover; // TODO: replace temporary hack with a LevelObject
	public float moverTimer = 0;
	
	// Position
	public float posx = -1;
	public float posy = -1;
	public int lastDirection = 0;
	
	public static final int DIR_SOUTH = 1;
	public static final int DIR_NORTH = 0;
	public static final int DIR_EAST = 2;
	public static final int DIR_WEST = 3;
	
	// Animation variables
	public float currentWalkFrame = 0;
	public float stillTime = 0;
	
	
	
	public int camMinY = 0;
	public int camMaxY = 0;
	
	public int camMinX = 0;
	public int camMaxX = 0;
	
	public float nextCombat = 0;
	
	private Sprite battleWindow;
	private Texture hpTex;
	private Texture mpTex;
	private Texture tmTex;
	private Texture selectionHand;
	
	// Dialog Stuff
	BitmapFont font;
	
	private String curSentence = null;
	private String curSpeaker = null;
	public Dialog currentDialog;
	public float dialogWait = 0;
	
	// Combat Stuff
	public LinkedList<CombatOption> currentCombatOptions;
	public CombatOption selectedCombatOption;
	public boolean finishedChoosing;
	public PartyMember combatOptionCaster;
	public Combatant selectedTarget;
	public boolean finishedTargeting;
	public LinkedList<Combatant> validTargets;
	
	private LinkedList<Sprite> combatEffects;
	private Combat currentCombat;
	public float waitTime = 0;
	private Targeting targeting;
	
	
	
	
	public static final int MODE_MOVE = 0;
	public static final int MODE_FIGHT = 1;
	public static final int MODE_VICTORY = 2;
	public static final int MODE_DIALOG = 7;
	public static final int MODE_GAMEOVER = 99;
	
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
	
	private Music currentMusic;
	
	private String announcement;
	private float announcementTimeout;

	

	
	private Box2DDebugRenderer debugRenderer;
	
	private float moveSpeed = 0.003f;
	
	public static final float PIXELS_PER_METER = 60.0f;
	
	public Party party;
	
	private int gameMode = MODE_MOVE;
	
	
	
	public Sound hitSound; // = Gdx.audio.newSound(Gdx.files.internal("data/sound/woodenstickattack.wav"));
	public Sound biteSound; // = Gdx.audio.newSound(Gdx.files.internal("data/sound/zombiebite.wav"));
	public Sound cutSound; // = Gdx.audio.newSound(Gdx.files.internal("data/sound/cut.wav"));
	
	public TimeTracker timeTracker;
	public QuestTracker questTracker;
	
	private LinkedList<MonsterArea> activeMonsterAreas;
	
	private LinkedList<Dialog> activeDialogs;
	
	private LinkedList<LevelObject> levelObjects;
	
	
	/**
	 * used to let new levels load before starting dialogs..
	 * otherwise things will get quite clunky
	 */
	public float levelLoadGraceTime = 0;
	
	/**
	 * Which level to return to after combat.
	 */
	private Level returnLevel;
	

	// Player characters
	public static PartyMember Leoric;
	public static PartyMember Tolinai;
	public static PartyMember Bert;
	public static PartyMember Berzenor;
	public static PartyMember Kuriko;
	
	// Combat Action categories
	public static final ActionCategory OFFENSIVE_MAGIC = new ActionCategory((byte)0);
	public static final ActionCategory DEFENSIVE_MAGIC = new ActionCategory((byte)1);
	public static final ActionCategory SPECIAL = new ActionCategory((byte)2);
	public static final ActionCategory SUMMON = new ActionCategory((byte)3);
	public static final ActionCategory ATTACK = new ActionCategory((byte)4);
	public static final ActionCategory ITEM_ACTION = new ActionCategory((byte)5);
	public static final ActionCategory MONSTER_ABILITY = new ActionCategory((byte)7);
	
	// Combat Actions
	public static final CombatAction BITE = new CombatAction("Bite",MONSTER_ABILITY,0, -4f, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction PUNCH = new CombatAction("Punch",ATTACK,0, -5f, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction TWINFIST = new CombatAction("TwinFist",MONSTER_ABILITY,3,-10f,Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction REGROWTH = new CombatAction("Regrowth",MONSTER_ABILITY,5,50f,Targeting.TARGET_SELF);
	public static final CombatAction SLASH = new CombatAction("Slash",ATTACK,0, -3f, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction CYCLONE_SLASH = new CombatAction("Cyclone Slash",OFFENSIVE_MAGIC,9,-20f,Targeting.TARGET_ENEMY_ALL);
	public static final CombatAction MAGIC_ARROW = new CombatAction("Magic Arrow",OFFENSIVE_MAGIC,8, -12f, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction STAFF_STRIKE = new CombatAction("Staff Strike",ATTACK,0, -1f, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction ROULETTE_STING = new CombatAction("Roulette Sting",MONSTER_ABILITY,10, -50f, Targeting.TARGET_RANDOM);
	public static final CombatAction GRAND_CLAW = new CombatAction("Grand Claw",MONSTER_ABILITY,0, -5f, Targeting.TARGET_ENEMY_ALL);
	
	// HardCoded CombatOptions
	public static final CombatOption escape = new CombatOption("escape");
	public static final CombatOption item = new CombatOption("item");
	public static final CombatOption defend = new CombatOption("defend");
	
	// Item textures
	
	
	private void returnFromCombat(){
		this.loadLevel(returnLevel, (int)posx, (int)posy, lastDirection);
	}
	

	
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
		
		String[] backgrounds = this.returnLevel.getBattleBackgrounds();
		
		this.backgroundTexture = new Texture(Gdx.files.internal("data/prerenders/"+backgrounds[(int)(Math.random()*backgrounds.length)  ]));
		this.background = new Sprite(backgroundTexture, 0, 0, 480, 320);
		
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
		this.combatEffects = new LinkedList<Sprite>();

		
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
				//drawSprites.add(monsterSprite);
				System.out.println("Added monster:x"+xy[0]+",y"+xy[1]);
			}
		}
		
		// Timing and stuff is taken care of in the Combat class (which is queried from the render loop)
		
		gameMode = MODE_FIGHT;
	}
	
	public void loadCombat(MonsterArea monsterArea){
		loadCombat(monsterArea.getRandomSetup());
	}
	
	private void loadCombatEffects(){
		// bite
		BITE.effect = new Texture(Gdx.files.internal("data/combateffects/biteAttack.png"));
		
		// slash
		SLASH.effect = new Texture(Gdx.files.internal("data/combateffects/normalCut.png"));
		
		// Cyclone Slash
		CYCLONE_SLASH.effect = new Texture(Gdx.files.internal("data/combateffects/cutAttack.png"));
		
		// staff strike
		STAFF_STRIKE.effect = new Texture(Gdx.files.internal("data/combateffects/blunt.png"));
		
		// magic arrow
		MAGIC_ARROW.effect = new Texture(Gdx.files.internal("data/combateffects/magicAttack.png"));
		
		GRAND_CLAW.effect = new Texture(Gdx.files.internal("data/combateffects/cutAttack.png"));
		
		ROULETTE_STING.effect = new Texture(Gdx.files.internal("data/combateffects/blunt.png"));
		
	}
	
	public LinkedList<Sprite> getCombatUISprites(){
		LinkedList<Sprite> list = new LinkedList<Sprite>();
		
		//first make sprites for leoric.. hes always there and always nr.1 (atleast for now)
		

		PartyMember[] activeMembers = this.party.getActiveMembers();
		
		int num = 0;
		
		for(PartyMember m : activeMembers){
			
			int healthPerdeca = Math.max(0, Math.round(((m.getHealth()+0.0f) / m.health_max)*10));
			
			Sprite myHp = new Sprite(hpTex,0,0+19*healthPerdeca,106,19);
			myHp.setX(102);
			myHp.setY(67-32*num);
			
			list.add(myHp);
			
			int manaPerdeca = Math.max(0, Math.round(((m.getMana()+0.0f) / m.mana_max)*10));
			
			Sprite myMp = new Sprite(mpTex,0,0+19*manaPerdeca,106,19);
			myMp.setX(230);
			myMp.setY(67-32*num);
			
			list.add(myMp);
			
			
			
			int timePerdeca = (int) Math.max(0, Math.round(((m.actionTimer+0.0f) / 100f)*10));
			
			Sprite myTime = new Sprite(tmTex,0,0+19*timePerdeca,106,19);
			myTime.setX(358);
			myTime.setY(67-32*num);
			
			list.add(myTime);
			num++;
		}
		
		return list;
	}
	
	@Override
	public void create() {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		
		camera = new OrthographicCamera(w, h);
		camera.position.set(0, 0, 0);
		batch = new SpriteBatch();
		fontBatch = new SpriteBatch();
		drawSprites = new LinkedList<Sprite>();
		
		hitSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/woodenstickattack.wav"));
		biteSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/zombiebite.wav"));
		cutSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/cut.wav"));

		
		Gdx.input.setInputProcessor(this);
		
		// Load up the font
		
		//fontAtlas = new TextureAtlas("data");
		font = new BitmapFont(Gdx.files.internal("data/fonts/PressStart2P/PressStart2P.fnt"),false);
		//fontAtlas.findRegion("PressStart2P"), false);
		
		loadCombatEffects();
		
		// load battle window stuffs
		Texture battleTex = new Texture(Gdx.files.internal("data/ui/battlewindow.png"));
		this.battleWindow = new Sprite(battleTex, 0,0,480,100);
		hpTex = new Texture(Gdx.files.internal("data/ui/hp.png"));
		mpTex = new Texture(Gdx.files.internal("data/ui/mp.png"));
		tmTex = new Texture(Gdx.files.internal("data/ui/time.png"));
		
		this.selectionHand = new Texture(Gdx.files.internal("data/ui/selectionhand.png"));
		
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
		
		// TODO: story is only done untill here roughly 1/8 of story only :<
		
		timeTracker.addEvent("entered inn");
		
		timeTracker.addEvent("talked with Bert");
		
		timeTracker.addEvent("accepted quest");
		timeTracker.addEvent("entered troll cave");
		timeTracker.addEvent("killed troll");
		
		timeTracker.addEvent("returned to town");
		timeTracker.addEvent("Bert joins");
		
		timeTracker.addEvent("left second town");
		
		timeTracker.addEvent("went to west caves");
		
		timeTracker.addEvent("killed manticore");
		
		timeTracker.addEvent("entered castle inn");
		timeTracker.addEvent("rented a room");
		timeTracker.addEvent("went inside room");
		
		
		timeTracker.addEvent("nightmare1");
		timeTracker.addEvent("awoken");
		
		timeTracker.addEvent("fight1");
		timeTracker.addEvent("fight2");
		
		timeTracker.addEvent("left caste");
		
		timeTracker.addEvent("entered beach town");
		timeTracker.addEvent("???");
		// although the events beyond are planned, they arent written here because of spoiling..
		// guess ill try to keep them away from the gitlog also, when it finaly comes to that..
		
		timeTracker.addEvent("THE END"); // Keep this last or bugs be onto ye!
		
		questTracker = new QuestTracker();
		questTracker.registerQuest("First potion.");
		
		questTracker.registerQuest("MyHouse-ChestN");
		questTracker.registerQuest("MyHouse-ChestW");
		
		Leoric = new PartyMember(0,"Leoric",100,100,10,10,0); // Male hero (swordsman)
		Leoric.addCombatAction(SLASH);
		Leoric.addCombatAction(CYCLONE_SLASH);
		
		party.addMember(Leoric);
		
		Tolinai = new PartyMember(1,"Tolinai",50,50,40,40,0); // Female, hero gf (offensive mage)
		Tolinai.addCombatAction(STAFF_STRIKE);
		Tolinai.addCombatAction(MAGIC_ARROW);
		
		Bert = new PartyMember(2,"Bert",250,250,5,5,0); // Male, archer
		Bert.addCombatAction(PUNCH);
		
		Berzenor = new PartyMember(3,"Berzenor",250,250,5,5,0); // Male, defensive mage
		Berzenor.addCombatAction(PUNCH);
		
		Kuriko = new PartyMember(4,"Kuriko",250,250,5,5,0); // Female, rogue
		Kuriko.addCombatAction(PUNCH);
		
		
		party.giveItem(Item.Potion, (byte)3); // give some potions, to make the start easier
		
		loadLevel(new Church(),522,414,1);// church the real start point
		
		/*
		this.addMember(new PartyMember(2,"Bert",50,50,10,10,0)); // Male, archer
		this.addMember(new PartyMember(3,"Berzenor",40,40,60,60,0)); // Male, defensive mage
		this.addMember(new PartyMember(4, "Kiriko",70,70,30,30,0)); // Female, rogue*/
		
		//loadLevel(new HomeTownNight(), 3005, 1326,1); //TODO: remove debugging stuffs
		//party.addMember(Tolinai); // TODO: remove this!!
		//timeTracker.setTime("east house?"); // TODO: remove debug test
		
		
		//loadLevel(new SecondTown(),117,1949,this.DIR_SOUTH);// second town
		//loadLevel(new SecondTownInn1(),177,1814,this.DIR_NORTH);// second town inn
		
		/*MonsterArea area = new MonsterArea(0,0,20,20,0.05f);
		
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
		*/
		//loadCombat(4,area);

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
		fontBatch.dispose();
		backgroundTexture.dispose();
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
		
		this.levelLoadGraceTime = 0.5f;
		
		this.camMinX = level.getCamMinX();
		this.camMaxX = level.getCamMaxX();
		this.camMinY = level.getCamMinY();
		this.camMaxY = level.getCamMaxY();
		
		this.posx = posx;
		this.posy = posy;
		this.lastDirection = direction;
		
		background = level.background(backgroundTexture);
		
		this.activeMonsterAreas = level.getMonsterAreas(timeTracker);
		this.activeDialogs = level.getLevelDialogs();
		this.levelObjects = level.getLevelObjects(questTracker);

		
		
		// Hard Coded strange things goes here.. SPOLIER ALERT, etc..
		
		if(level instanceof HomeTownNight)
			this.fallingTexture = new Texture(Gdx.files.internal("data/raindrop.png"));
		// TODO: add wind effect
		// TODO: add ambient sounds
		

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
		
		if(this.timeTracker.getTime().equals("left hometown")){
			// second town
			if(this.currentMusic != null)
				this.currentMusic.stop();
			this.currentMusic = Gdx.audio.newMusic(Gdx.files.internal("data/music/Renich_-_Rola_Z.ogg"));
			this.currentMusic.setLooping(true);
			this.currentMusic.play();
		}
		else if(this.timeTracker.getTime().equals("start")){
			// inside hometown
			if(this.currentMusic != null)
				this.currentMusic.stop();
			this.currentMusic = Gdx.audio.newMusic(Gdx.files.internal("data/music/Mark_Subbotin_-_Phoenix.ogg"));
			this.currentMusic.setLooping(true);
			this.currentMusic.play();
		}
		
		/*
		if(this.currentMusic != null)
			this.currentMusic.stop();
		
		if(level.getMusic() != null){
			this.currentMusic = Gdx.audio.newMusic(Gdx.files.internal(level.getMusic()));
			this.currentMusic.setLooping(true);
			this.currentMusic.play();
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
		
		level.applyCollisionBoundaries(world, PIXELS_PER_METER); // Added level collision data (except dynamic objects)
		
		if(this.levelObjects != null){
			// add dynamic level object collision data
			BodyDef groundBodyDef = new BodyDef();
			groundBodyDef.type = BodyDef.BodyType.StaticBody;
			Body groundBody = world.createBody(groundBodyDef);
			for(LevelObject object : this.levelObjects){
				ChainShape s = object.getCollisionBoundary();
				if(s == null)
					continue;
				groundBody.createFixture(s, 0);
				s.dispose();
			}
		}

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
		
		
		gameMode = MODE_MOVE;
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
		
		
		
		if(gameMode == MODE_DIALOG){
			// a Dialog is active;
			if(this.dialogWait > 0){
				if(Gdx.input.isButtonPressed(Keys.ENTER))// Speed up the dialog a bit
					dialogWait -= 17f*Gdx.graphics.getDeltaTime();
				else
					dialogWait -= Gdx.graphics.getDeltaTime();
				
			}
			else if(this.currentDialog.hasNextUtterance()){
				//TODO: this has to be done more nicely somehow..
				
				Utterance u = this.currentDialog.getNextUtterance();
				
				System.out.print(u.speaker+": ");
				System.out.println(u.sentence);
				
				this.curSpeaker = u.speaker;
				this.curSentence = u.sentence;

				dialogWait = (u.sentence.length()*0.07f); // TODO: hum hum
				if(dialogWait < 1.7f)
					dialogWait = 1.7f;

			}
			else {
				// apply secondary effects and change gameMode back to whatever normal is
				// TODO: would be nice if dialogs could be had inside combat also..
				// but not strictly required.
				this.curSpeaker = null;
				this.curSentence = null;
				
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
					this.gameMode = MODE_MOVE;
					this.currentDialog = null;
					return;
				}
				
			}
		}
		
		
		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;
		
		if(gameMode == MODE_MOVE){
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
		

		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) //TODO: confirm dialog
			Gdx.app.exit();
		
		//camera.lookAt(princess.getX(), princess.getY(), 0);
		//background.scroll(posx, posy);
		//background.
		
		/*background.setX(posx);
		background.setY(posy);
		collisionLayer.setX(posx);
		collisionLayer.setY(posy);
		*/

		if(gameMode == MODE_MOVE){
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
			
			posx = jumper.getPosition().x*PIXELS_PER_METER;
			posy = jumper.getPosition().y*PIXELS_PER_METER;
			leoricSprite.setX(posx-32);
			leoricSprite.setY(posy-10);
			pointer.setTransform(jumper.getPosition().x, jumper.getPosition().y, 0);
			
			camera.position.x = leoricSprite.getX()+32;
			camera.position.y = leoricSprite.getY()+16;
			
			
			
			
			if((left || right || up || down) && this.levelLoadGraceTime <= 0){
				// Only attract monsters and dialogs when moving
				
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
								this.gameMode = MODE_DIALOG;
								this.currentDialog = d;
								return;
							}
							
						}
					}
				}
				
				if(this.activeMonsterAreas != null && this.activeMonsterAreas.size() > 0){
					
					if(this.nextCombat > 0){ // Try to limit how often you have to fight somewhat..
						nextCombat -= Gdx.graphics.getDeltaTime();
					}
					else {
						for(MonsterArea area : activeMonsterAreas){
							// check if player is inside it
							if(area.isInside((int)posx, (int)posy)){
								
								// roll a dice to find out if the player should be attacked.
								float num = (float)Math.random();
								
								//if(Math.random()*100>90)
									//System.out.println(num+" v.s. "+area.encounterChance);
								
								if(num < area.encounterChance){
									// we have a winner..
									nextCombat = 10f; // aprox 10 seconds to next fight?
									this.loadCombat(area);
									return; // TODO: not sure if this is a good idea or bad
									// either return or break..
								}
								
							}
							
						}
					}
				}
			}
			else if(this.levelLoadGraceTime > 0){
				this.levelLoadGraceTime -= Gdx.graphics.getDeltaTime();
			}
			
			
			if(Gdx.input.isKeyPressed(Keys.B)){
				System.out.println("position: x="+posx+", y="+posy+", time="+timeTracker.getTime() + ", cam:["+camera.position.x+","+camera.position.y+"].");
			}
			
			if(Gdx.input.isKeyPressed(Keys.D)){
				this.debug = ! this.debug;
				System.out.println("debug :"+(this.debug?"On":"Off"));
				if(debug)
					this.moveSpeed += 0.02f;
				else
					this.moveSpeed -= 0.02f;
			}
			
			
			
			
			// Keep the camera inside the level.. Maybe too aggressive?
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
		
		boolean waiting = false; //TODO: move this? guess not
		
		if(waitTime > 0)
		{
			waiting = true;
			waitTime -= Gdx.graphics.getDeltaTime();
		}
		if(gameMode == MODE_FIGHT && this.currentCombat.waitingForPlayerCommand)
			waiting = true;
		
		if(gameMode == MODE_VICTORY){
			// "After Combat" screen
			if(currentCombat != null){
				// clean up!
				for(Sprite s : drawSprites){
					//s.getTexture().dispose();
				}
				drawSprites.clear();
				
				//TODO: display some info about the fight..
				//TODO: give items and stuffs
				party.addExperience(currentCombat.setup.exp);
				
				currentCombat.cleanUp();
				currentCombat = null;
				waitTime = 4f;
				waiting = true;
				
				
				
				this.backgroundTexture = new Texture(Gdx.files.internal("data/victory.png"));
				this.background = new Sprite(backgroundTexture, 0, 0, 480, 320);
				
				
			}
			
			
			if(!waiting || this.debug){
				waiting = false;
				waitTime = 0;
				this.returnFromCombat();
				return; // TODO: not sure if this is a good idea or bad
			}
		}
		
		else if(gameMode == MODE_GAMEOVER){
			// Game over
			//TODO: write something 'nice' to the screen?
			this.backgroundTexture = new Texture(Gdx.files.internal("data/gameover.png"));
			this.background = new Sprite(backgroundTexture, 0, 0, 480, 320);
			
		}
		
		else if(gameMode == MODE_FIGHT){
			
			// Re-position all creatures..
			for(int i = 0; i < currentCombat.getLiveCombatants().size(); i++){
				Combatant current = currentCombat.getLiveCombatants().get(i);
				
				switch(current.getState()){
					case Combat.STATE_STONE:
						current.getSprite().setColor(Color.GRAY);
						// render as grey (as stone)
						break;
					case Combat.STATE_POISONED:
						current.getSprite().setColor(Color.GREEN);
						// render as green
						break;
					case Combat.STATE_FURY:
						current.getSprite().setColor(Color.RED);
						// render as red
						break;
					case Combat.STATE_WEAKNESS:
						current.getSprite().setColor(Color.YELLOW);
						// render as pale yellow
						break;
					case Combat.STATE_DOOM:
						current.getSprite().setColor(Color.BLACK);
						// render as dark
						break;
					default:
						current.getSprite().setColor(Color.WHITE);
						break;
				}
			}
			
			// carry out player commands
			if(this.currentCombat.waitingForPlayerCommand && this.finishedChoosing == true){
				
				
				if(this.selectedCombatOption.subGroup == false && this.selectedCombatOption.hardCoded){
					// player chose a hardcoded option.
					if(this.selectedCombatOption == this.defend){
						// TODO: defend if possible
						// For now, we just wait a turn when defending (so its near useless).
						this.currentCombat.resetActionTimer(this.combatOptionCaster);
						
						waiting = true;
						waitTime = 0.3f;
						this.currentCombat.waitingForPlayerCommand = false;
						this.selectedCombatOption = null;
						this.currentCombatOptions = null;
						this.combatOptionCaster = null;
						this.finishedTargeting = false;
						this.finishedChoosing = false;
						this.validTargets = null;
						this.selectedTarget = null;
					}
					else if(this.selectedCombatOption == this.escape){
						if(Math.random() < this.currentCombat.getEscapeChance()){
							// escape!
							this.returnFromCombat();
							return;
						}
						else {
							waiting = true;
							waitTime = 0.3f;
							this.currentCombat.waitingForPlayerCommand = false;
							this.selectedCombatOption = null;
							this.currentCombatOptions = null;
							this.combatOptionCaster = null;
							this.finishedTargeting = false;
							this.finishedChoosing = false;
							this.validTargets = null;
							this.selectedTarget = null;
						}
					}
					else if(this.selectedCombatOption == this.item){
						// have to pull up a submenu with all combat items.
						this.currentCombatOptions = new LinkedList<CombatOption>();
						Inventory inventory = party.getInventory();
						LinkedList<Item> items = inventory.getCombatItems();
						for(Item i : items){
							this.currentCombatOptions.add(new CombatOption(i));
						}
						
						this.selectedCombatOption = this.currentCombatOptions.getFirst();
						this.finishedChoosing = false;
						System.out.println("subgroup");		
						
						
						// TODO: make combatoptions for each combat item (+ count)
						// then serve to player just like with magics
					}
				}
				else if(this.selectedCombatOption.subGroup == false){
					// player has selected an option which we can carry out.. so lets!
					
					
					if(!this.finishedTargeting && this.selectedTarget == null){
						// TODO: well?
						
						if(this.selectedCombatOption.item == null){
							// Not trying to use an item
							CombatAction action = this.selectedCombatOption.associatedActions.getFirst();
							
							
							this.validTargets = this.currentCombat.getValidTargets(action,
									this.combatOptionCaster);
							// NOTE: for attacks that target 'all *' or 'random *', this is also fine and handled elsewhere.
							this.selectedTarget = validTargets.getFirst();
							
							
							
							this.targeting = new Targeting(action.targetType,validTargets,this.combatOptionCaster,action.offensive);
							
						}
						else {
							// Trying to use an item
							// targeting for items is a little strange i guess..
							if(this.selectedCombatOption.item instanceof ConsumeableItem){
								ConsumeableItem item = (ConsumeableItem)this.selectedCombatOption.item;
								CombatAction action = item.getEffect();
								
								this.validTargets = new LinkedList<Combatant>();
								for(Combatant c : this.currentCombat.getLiveCombatants())
									this.validTargets.add(c);
								if(item.offensive){
									// target first enemy..
									this.selectedTarget = this.currentCombat.getLiveMonsters().getFirst();
								}
								else {
									// target self
									this.selectedTarget = this.combatOptionCaster;
								}
								System.out.println("item");
								
								this.targeting = new Targeting(action.targetType,validTargets,this.combatOptionCaster,item.offensive);
								
							}
							else {
								// TODO: non-consumable items in combat?
								System.err.println("[ERROR] Non consumeable item in combat? Have to write the code first then! 1");
							}
						}
					}
					
					else if(this.finishedTargeting){
						if(this.selectedCombatOption.item != null){
							// player wants to use an item
							if(this.selectedCombatOption.item instanceof ConsumeableItem){
								ConsumeableItem i = (ConsumeableItem) this.selectedCombatOption.item;
								CurrentAction myAction = new CurrentAction(i.getEffect(),
										this.combatOptionCaster, this.targeting);
								LinkedList<Combatant> affected = currentCombat.applyAction(myAction);
								
								myAction.caster.setMoveAhead(true);
								
								if(myAction.action.effect != null && affected != null){
									
									for(Combatant c : affected){
										Sprite effect = new Sprite(myAction.action.effect);
										effect.setX(c.getSprite().getX()+16);
										effect.setY(c.getSprite().getY()+16);
										this.combatEffects.add(effect);
									}
								}
								
								// take the item
								party.getInventory().removeItem(i,(byte)1);
								
								this.currentCombat.resetActionTimer(this.combatOptionCaster);
								
								waiting = true;
								waitTime = 2;
								this.currentCombat.waitingForPlayerCommand = false;
								this.selectedCombatOption = null;
								this.currentCombatOptions = null;
								this.combatOptionCaster = null;
								this.finishedTargeting = false;
								this.finishedChoosing = false;
								this.validTargets = null;
								this.selectedTarget = null;
								
								
							}
							else {
								System.err.println("[ERROR] Non consumeable item in combat? Have to write the code first then! 2");
							}
						}
						else {
							CurrentAction myAction = new CurrentAction(this.selectedCombatOption.associatedActions.getFirst(),
									this.combatOptionCaster, this.targeting);
							
							LinkedList<Combatant> affected = currentCombat.applyAction(myAction);
							
							if(myAction.action != null && affected != null){
								// Move player against monster to represent the attack
								
								
								if(myAction.caster == Tolinai){ // TODO: simple hack to get sound..
									this.hitSound.play();
								}
								if(myAction.caster == Leoric){ // TODO: simple hack to get sound..
									this.cutSound.play();
								}
								
								this.announce(myAction.action.name);
								myAction.caster.setMoveAhead(true);
								
								if(myAction.action.effect != null && affected != null){
									
									for(Combatant c : affected){
										Sprite effect = new Sprite(myAction.action.effect);
										effect.setX(c.getSprite().getX()+16);
										effect.setY(c.getSprite().getY()+16);
										this.combatEffects.add(effect);
									}
								}
							}
							if(this.debug){
								// time to stop this nonsense!
								for(Monster m : currentCombat.getLiveMonsters()){
									m.health = 0;
								}
							}
							
							this.currentCombat.resetActionTimer(this.combatOptionCaster);
							
							waiting = true;
							waitTime = 2;
							this.currentCombat.waitingForPlayerCommand = false;
							this.selectedCombatOption = null;
							this.currentCombatOptions = null;
							this.combatOptionCaster = null;
							this.finishedTargeting = false;
							this.finishedChoosing = false;
							this.validTargets = null;
							this.selectedTarget = null;
						}
					}
				}
				else {
					// this action has a subgroup.. player needs to choose from these also
					LinkedList<CombatAction> actions = this.selectedCombatOption.associatedActions;
					this.currentCombatOptions = new LinkedList<CombatOption>();
					for(CombatAction act : actions)
						this.currentCombatOptions.add(new CombatOption(act.name,act));
					
					this.selectedCombatOption = this.currentCombatOptions.getFirst();
					this.finishedChoosing = false;
					System.out.println("subgroup");
					
					
				}
				
			}
			
			for(Combatant c : this.currentCombat.getCombatants()){
				if(c.health <= 0){
					// dead..
					if(c instanceof PartyMember){
						// TODO: some effect here
					}
					else if(c instanceof Monster)
					{
						Monster m = (Monster)c;
						Color d = Color.BLACK;
						
						int i = (int)Float.MAX_VALUE;
						if(m.alpha == Monster.ALPHA_START){
							Color red = Color.RED;
							m.getSprite().setColor(red);
						}
						if(m.alpha > 0f){
							m.alpha -= m.fadeSpeed * Gdx.graphics.getDeltaTime();
						}
						if(m.alpha < 0){
							m.alpha = 0;
						}
					}
				}
			}
			
			//check if battle is over.
			if(!waiting){
				byte state = currentCombat.getBattleState();
				
				for(int i = 0; i < currentCombat.getLiveCombatants().size(); i++){
					currentCombat.getLiveCombatants().get(i).setMoveAhead(false);
				}
				this.combatEffects.clear();
				
				if(state == 1){
					// player has won
					// check if all monsters are done fading out..
					boolean fadingDone = true;
					for(Combatant c : currentCombat.getCombatants())
						if(c instanceof Monster && ((Monster)c).alpha != 0)
							fadingDone = false;
					
					if(fadingDone){ // WIN
						waiting = true;
						System.out.println("VICTORY! :>");
						//TODO: happy trumpet
						gameMode = MODE_VICTORY;
						this.combatEffects.clear();
					}
					// else, wait for fading to finish..
					
				}
				else if(state == 2){
					// player has lost
					waiting = true;
					System.out.println("GAME OVER. :'(");
					
					
					if(currentMusic != null)
						currentMusic.stop();
					
					currentMusic = Gdx.audio.newMusic(Gdx.files.internal("data/music/Renich_-_Rola_Z.ogg"));
					currentMusic.setLooping(true);
					currentMusic.play();

					gameMode = MODE_GAMEOVER;
					this.combatEffects.clear();
				}
				
				
				
				//TODO: render status changes (those that are visible)
				
				Monster readyMonster = currentCombat.getFirstReadiedMonster();
				if(readyMonster != null && currentCombat.getLivePlayers().size() > 0){
					
					CurrentAction myAction = readyMonster.getMonsterAction(party, currentCombat);
					
					LinkedList<Combatant> affected = currentCombat.applyAction(myAction);
					
					if(myAction.action != null){
						// Move monster against player to represent the attack
						
						this.announce(myAction.action.name);
						myAction.caster.setMoveAhead(true);
						
						if(myAction.action.effect != null && affected != null){
							
							if(myAction.action == BITE){ // TODO: simple hack to get sound..
								this.biteSound.play();
							}
							if(myAction.action == PUNCH){ // TODO: simple hack to get sound..
								this.hitSound.play();
							}
							
							for(Combatant c : affected){
								Sprite effect = new Sprite(myAction.action.effect);
								effect.setX(c.getSprite().getX()+16);
								effect.setY(c.getSprite().getY()+16);
								this.combatEffects.add(effect);
							}
						}
						
					}
					
					readyMonster.actionTimer = readyMonster.getBaseDelay()*(1.5f*Math.random()+0.5f);// TODO: randomize better?
					
					waiting = true;
					waitTime = 2;
					// Only 1 attacker per turn.
				}
				if(!waiting && currentCombat.getLiveMonsters().size() > 0){
					PartyMember readyMember = currentCombat.getFirstReadiedPlayer();
					if(readyMember != null){
						
						// Find the options..
						
						// find out which 'global groups are available..
						boolean haveItem = currentCombat.isItemAllowed();
						boolean canEscape = currentCombat.isEscapeAllowed();
						boolean canDefend = false; // TODO: maybe have some setting to allow some of the characters to defend..
						
						CombatAction attack = null;
						
						LinkedList<CombatAction> magic = new LinkedList<CombatAction>();
						
						LinkedList<CombatAction> summons = new LinkedList<CombatAction>();
						
						for(CombatAction ca : readyMember.getCombatActions()){
							
							if(ca.category == this.OFFENSIVE_MAGIC || ca.category == this.DEFENSIVE_MAGIC)
								magic.add(ca);
							else if(ca.category == this.ATTACK)
								attack = ca;
							else if(ca.category == this.SUMMON)
								summons.add(ca);
						}
						
						this.currentCombatOptions = new LinkedList<CombatOption>();
						this.selectedCombatOption = null;
						if(attack != null)
							currentCombatOptions.add(new CombatOption("Attack",attack));
						if(canDefend)
							currentCombatOptions.add(this.defend);
						if(magic.size() > 0)
							currentCombatOptions.add(new CombatOption("Magic",magic));
						if(summons.size() > 0)
							currentCombatOptions.add(new CombatOption("Summon",summons));
						if(haveItem)
							currentCombatOptions.add(this.item);
						if(canEscape)
							currentCombatOptions.add(this.escape);
						
						this.selectedCombatOption = this.currentCombatOptions.getFirst();
						this.finishedChoosing = false;
						this.combatOptionCaster = readyMember;
						
						this.currentCombat.waitingForPlayerCommand = true;
						
						this.selectedTarget = null;
						
						this.finishedTargeting = false;
						this.validTargets = null;
						this.targeting = null;
					}
				}
			}
			
			leoricSprite.setRegion(0, 64*3, 64, 64);
			camera.position.x = w/2f;
			camera.position.y = h/2f;
			camera.update();
			if(!waiting){
				currentCombat.tick(Gdx.graphics.getDeltaTime()*5*2);
			}
		}
		
		
		//background = new Sprite(backgroundTexture, (int)(posx-w), (int)(posy-h), (int)(posx+w), (int)(posy+h));
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		background.draw(batch);
		
		if(this.gameMode == MODE_MOVE){
			// draw level objects
			if(this.levelObjects != null){
				for(LevelObject object : this.levelObjects){
					object.draw(batch);
				}
			}
		}
		
		
		for(Sprite s : drawSprites)
		 s.draw(batch);
		
		
		if(gameMode == MODE_FIGHT){
			
			this.battleWindow.draw(batch);
			
			LinkedList<Sprite> uiElements = this.getCombatUISprites();
			for(Sprite s : uiElements)
				s.draw(batch);
		}
		
		


		
		if(gameMode == MODE_MOVE && this.foreground != null) // drawing foreground last = ontop of everything else
			foreground.draw(batch);
		
		if(gameMode == MODE_FIGHT){
			
			for(Combatant monst : this.currentCombat.getCombatants()){
				if(monst instanceof Monster){
					Monster m = (Monster)monst;
					// draw the monster with correct alpha..
					// thus we can fade out dead monsters
					m.getSprite().draw(batch,m.alpha);
				}
			}
			
			// draw battle effects last.. unless you dont want them ontop anymore..
			for(Sprite effect : this.combatEffects)
				effect.draw(batch);
		}
		

		
		batch.end();
		

		if(this.announcement != null){
			if(this.announcementTimeout > 0){
				fontBatch.begin();
				font.setColor(Color.WHITE);
				font.draw(fontBatch, announcement, w/2-announcement.length()*6, h-24);
				announcementTimeout -= Gdx.graphics.getDeltaTime();
				fontBatch.end();
			}
		}
		
		if(gameMode == MODE_FIGHT && this.currentCombat.waitingForPlayerCommand){
			fontBatch.begin();
			if(this.finishedChoosing && ! this.finishedTargeting){
				// NOTE, for 'all * and random *' selectedTarget is wrong, but. we know how to draw those i guess..
				
				LinkedList<Combatant> targets = targeting.getCurrentTargets();
				
				//one hand for each target
				for(Combatant c : targets){
					Sprite cursor = new Sprite(this.selectionHand,0,0,32,32);
					cursor.setX(c.getSprite().getX());
					cursor.setY(c.getSprite().getY());
					cursor.draw(fontBatch);
				}
			}
			
			int offset = 16;
			int curOffset = 0;
			for(CombatOption opt : this.currentCombatOptions){
				
				font.setColor(Color.WHITE);
				
				if(this.currentCombat.canUse(opt, this.combatOptionCaster)){
					if(this.selectedCombatOption == opt)
						font.setColor(Color.YELLOW);
					else
						font.setColor(Color.WHITE);
				}
				else {
					if(this.selectedCombatOption == opt)
						font.setColor(Color.RED);
					else
						font.setColor(Color.GRAY);
				}
				
				String t = opt.name;
				if(opt.item != null)
					t = party.getInventory().getItemCount(opt.item)+"x "+opt.name;
				
				font.draw(fontBatch, t, w/4, h/2-curOffset);
				curOffset += offset;
			}
			fontBatch.end();
			font.setColor(Color.WHITE);
		}
		
		if(gameMode == MODE_DIALOG && this.curSpeaker != null){
			fontBatch.begin();
			// Ongoing dialog, draw the talkstuffs
			// TODO: char by char?
			
			int length = this.curSpeaker.length()+2+this.curSentence.length();
			float cposx = w/2;
			float cposy = h/2;
			if(length > 32){
				// needs to be split
				int remain = length;
				int num = 0;
				int start = 0;

				while(remain > 0){
					int printed = Math.min(32, remain);
					
					font.draw(fontBatch, this.curSpeaker+": "+this.curSentence, cposx-w/2+(num==0?0:30), cposy+h/2-h/4-(num*16), start, start+printed);
					remain -= printed;
					start += printed;
					num ++;
				}
			}
			else {
				// print everything in one go
				font.draw(fontBatch, this.curSpeaker+": "+this.curSentence, cposx-w/2, cposy+h/2-h/4);
			}
			fontBatch.end();
			
		}
		
		if(debug && world != null && debugRenderer != null)
			debugRenderer.render(world, camera.combined.scale(
					PIXELS_PER_METER,
					PIXELS_PER_METER,
					PIXELS_PER_METER));
	}

	@Override
	public void resize(int width, int height) {
		// Verboten?
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		
		if(this.gameMode == MODE_MOVE){
			if(keycode == Keys.I){
				// bring up inventory.
				// TODO: I guess its a shortcut, there will be a catch-all menu one day.
				Inventory i = party.getInventory();
				StringBuilder sb = new StringBuilder();
				sb.append("Items: ");
				for(Item item : i.getItems()){
					sb.append(i.getItemCount(item)+"x "+item.name+" ");
				}
				System.out.println(sb.toString());
			}
			if(this.levelObjects != null && keycode == Keys.ENTER){
				// check if there are any objects to interract with..
				ListIterator<LevelObject> iter = this.levelObjects.listIterator();
				while(iter.hasNext()){
					LevelObject object = iter.next();
					if(object.isNear((int)posx, (int)posy)){
						object.interact(party, questTracker);
						if(object instanceof LevelItem){
							if(object.shouldBeDeleted()){
								// TODO: play sound to confirm the player got some items
								// TODO: draw some text that tells the player he acquired some items (ingame).
								this.announce("Acquired a " + ((LevelItem)object).getItemType().name+"!");
								System.out.println("Acquired a " + ((LevelItem)object).getItemType().name+"!");
								iter.remove();
							}
							else {
								// TODO: draw some text to tell the player he can't carry more (ingame)..
								this.announce("Can't pick up the " + ((LevelItem)object).getItemType().name+", inventory is full!");
								System.out.println("Can't pick up the " + ((LevelItem)object).getItemType().name+", inventory is full!");
							}
						}
						else if(object instanceof Chest){
							Chest chest = (Chest)object;
							String msg = chest.getDialogString();
							if(msg != null){
								// TODO: proper ingame announcement
								this.announce(msg);
								System.out.println(msg);
							}
						}
					}
				}
			}
		}
		
		// if we are in combat mode and waiting for user and user has not selected yet.
		//   and keycode == up or down, then move selection
		//   if keycode == enter, then finalize selection
		
		if(this.gameMode == MODE_FIGHT){
			if(this.finishedChoosing && !this.finishedTargeting && this.currentCombat.waitingForPlayerCommand){
				if(keycode == Keys.ENTER){
					finishedTargeting = true;
				}
				else if(keycode == Keys.UP){
					// next
					targeting.next();
				}
				else if(keycode == Keys.DOWN){
					// prev
					targeting.previous();
				}
				else if(keycode == Keys.BACKSPACE){
					// cancel?
					this.finishedChoosing = false;
					this.validTargets = null;
					this.selectedTarget = null;
				}
			}
			else if(this.currentCombat.waitingForPlayerCommand && this.finishedChoosing == false && this.currentCombat.waitingForPlayerCommand){
				
				// moved up or down?
				if(keycode == Keys.UP){
					// select the previous option
					CombatOption prev = null;
					for(CombatOption o : this.currentCombatOptions){
						if(o == this.selectedCombatOption){
							// found it..
							if(prev != null)
								this.selectedCombatOption = prev;
							else
								this.selectedCombatOption = this.currentCombatOptions.getLast();
							return true;
						}
						else
							prev = o;
					}
				}
				else if(keycode == Keys.DOWN){
					// select the next option
					boolean found = false;
					for(CombatOption o : this.currentCombatOptions){
						if(o == this.selectedCombatOption){
							// found it..
							found = true;
						}
						else if (found){
							this.selectedCombatOption = o;
							return true;
						}
					}
					if(found){
						// the current was the last.. so select the first
						this.selectedCombatOption = this.currentCombatOptions.getFirst();
					}
				}
				else if(keycode == Keys.ENTER){
					if(!this.currentCombat.canUse(this.selectedCombatOption, this.combatOptionCaster)){
						// TODO: play some "DERP!" sound, to make the player feel stupid/confused/angry..
						System.out.println("Nop!");
					}
					else
						this.finishedChoosing = true; // done selecting, yay!
				}
				else if(keycode == Keys.BACKSPACE){
					// cancel?
					this.currentCombatOptions = null;
					this.currentCombat.waitingForPlayerCommand = false;
					this.selectedCombatOption = null;
					this.finishedChoosing = false;
				}
				else {
					//TODO: anything else, or dont care??
				}
				
			}
			
			return true;
		}
		
		// TODO Auto-generated method stub
		return false;
	}
	
	private void announce(String text){
		this.announcement = text;
		this.announcementTimeout = 2f;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
