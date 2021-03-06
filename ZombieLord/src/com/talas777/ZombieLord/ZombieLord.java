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

import com.talas777.ZombieLord.Audio.MusicInstance;
import com.talas777.ZombieLord.Audio.SoundInstance;
import com.talas777.ZombieLord.Items.ConsumeableItem;
import com.talas777.ZombieLord.Items.Weapons;
import com.talas777.ZombieLord.Levels.*;
import com.talas777.ZombieLord.Minigames.ZombieDefense;
import com.talas777.ZombieLord.Minigames.TowerDefense.Attacker;
import com.talas777.ZombieLord.Minigames.TowerDefense.Defender;

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
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Array;

public class ZombieLord implements ApplicationListener, InputProcessor {
	
	public static final int MAX_PLAYER_LEVEL = 99;
	
	public String questHint = "???";
	
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
        BitmapFont smallFont;
	
	private String[] curSentence = null;
	private String curSpeaker = null;
	public Dialog currentDialog;
	public float dialogWait = 0;
        public double dialogTicker = 1.0;
	
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
	
	// Zombie Defense stuff
	private ZombieDefense zombieDefense;
	
	
	
	
	public static final int MODE_MOVE = 0;
	public static final int MODE_FIGHT = 1;
	public static final int MODE_VICTORY = 2;
	public static final int MODE_ZOMBIE_DEFENSE = 3;
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

        // debugging vars
	private boolean debug = false;
        private boolean debugStart = false;


        /** the volume of music globally, 1 = 100%, 0 = 0% */
        private static float globalMusicVolume = 0f;
       /** the volume of sound globally, 1 = 100%, 0 = 0% */
        private static float globalSoundVolume = 0f;
	
	
	private World world;
	private Body jumper;
	private Body pointer;
	
	private MusicInstance currentMusic;
	private MusicInstance combatMusic;
	
	private static String announcement;
	private static float announcementTimeout;

	

	
	private Box2DDebugRenderer debugRenderer;
	
	private float moveSpeed = 0.003f;
	
	public static final float PIXELS_PER_METER = 60.0f;
	
	public Party party;
	
	private int gameMode = MODE_MOVE;
	

	private NinePatch dialogBackground;
	private NinePatch announcementBackground;
	private NinePatch menuBackground;
	
	private NinePatch minibarHp;
        private NinePatch barHp;
        private NinePatch barMp;
        private NinePatch barTime;
	
    /*public Sound hitSound; // = Gdx.audio.newSound(Gdx.files.internal("data/sound/woodenstickattack.wav"));
	public Sound biteSound; // = Gdx.audio.newSound(Gdx.files.internal("data/sound/zombiebite.wav"));
	public Sound cutSound; // = Gdx.audio.newSound(Gdx.files.internal("data/sound/cut.wav"));
    */
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
	
	public static final Element ELEM_PHYSICAL = new Element("physical");
	public static final Element ELEM_FIRE = new Element("fire");
	public static final Element ELEM_ICE = new Element("ice");
	public static final Element ELEM_LIGHTNING = new Element("lightning");
	public static final Element ELEM_EARTH = new Element("earth");
	public static final Element ELEM_WATER = new Element("water");
	public static final Element ELEM_WIND = new Element("wind");
	public static final Element ELEM_POISON = new Element("poison");
	public static final Element ELEM_HOLY = new Element("holy");
	public static final Element ELEM_EVIL = new Element("evil");
	public static final Element ELEM_NULL = new Element("non-elemental");
	

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
	
	// Combat Effects
	
	public static final CombatEffect biteEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-4f,false, ELEM_PHYSICAL);
	public static final CombatEffect punchEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-5f,false, ELEM_PHYSICAL);
	public static final CombatEffect twinFistEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-10f,false, ELEM_PHYSICAL);
	public static final CombatEffect regrowthEffect = new CombatEffect(CombatEffect.TYPE_MAGICAL,50f,false, ELEM_NULL);
	public static final CombatEffect slashEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-3f,false, ELEM_PHYSICAL);
	public static final CombatEffect cycloneSlashEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-20f,false, ELEM_PHYSICAL);
	public static final CombatEffect magicArrowEffect = new CombatEffect(CombatEffect.TYPE_MAGICAL,-12f,false, ELEM_NULL);
	public static final CombatEffect staffStrikeEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-1f,false, ELEM_PHYSICAL);
	public static final CombatEffect rouletteStingEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-50f,false, ELEM_POISON);
	public static final CombatEffect grandClawEffect = new CombatEffect(CombatEffect.TYPE_PHYSICAL,-5f,false, ELEM_EARTH);
	
	// Combat Actions
	public static final CombatAction BITE = new CombatAction("Bite",MONSTER_ABILITY,0, biteEffect, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction PUNCH = new CombatAction("Punch",ATTACK,0, punchEffect, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction TWINFIST = new CombatAction("TwinFist",MONSTER_ABILITY,3,twinFistEffect,Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction REGROWTH = new CombatAction("Regrowth",MONSTER_ABILITY,5,regrowthEffect,Targeting.TARGET_SELF);
	public static final CombatAction SLASH = new CombatAction("Slash",ATTACK,0, slashEffect, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction CYCLONE_SLASH = new CombatAction("Cyclone Slash",OFFENSIVE_MAGIC,9, cycloneSlashEffect,Targeting.TARGET_ENEMY_ALL);
	public static final CombatAction MAGIC_ARROW = new CombatAction("Magic Arrow",OFFENSIVE_MAGIC,8, magicArrowEffect, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction STAFF_STRIKE = new CombatAction("Staff Strike",ATTACK,0, staffStrikeEffect, Targeting.TARGET_ENEMY_SINGLE);
	public static final CombatAction ROULETTE_STING = new CombatAction("Roulette Sting",MONSTER_ABILITY,10, rouletteStingEffect, Targeting.TARGET_RANDOM);
	public static final CombatAction GRAND_CLAW = new CombatAction("Grand Claw",MONSTER_ABILITY,0, grandClawEffect, Targeting.TARGET_ENEMY_ALL);
	
	// HardCoded CombatOptions
	public static final CombatOption escape = new CombatOption("escape");
	public static final CombatOption item = new CombatOption("item");
	public static final CombatOption defend = new CombatOption("defend");
	
	// Item textures
	
	
	private void returnFromCombat(){
		this.loadLevel(returnLevel, (int)posx, (int)posy, lastDirection);
	}
	
	private static LinkedList<SoundInstance> sounds = new LinkedList<SoundInstance>();
	
	public static void playSound(String file, float volume){
		sounds.add(new SoundInstance(file,volume*globalSoundVolume));
	}
	
	private static double musicPause = 0;
	
	public static void pauseMusic(double time){
		musicPause = time;
	}
	

	
	public void loadCombat(MonsterSetup setup){
		
		for(SoundInstance s : sounds)
			s.dispose();
		sounds.clear();
		
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
		
		
		if(this.currentMusic != null){
			this.currentMusic.pause();
		}
		this.combatMusic = new MusicInstance("data/music/Vadim_Danilenko_-_Lunapark.ogg");
			
		this.combatMusic.setLooping(true);
		this.combatMusic.setVolume(this.globalMusicVolume);
		this.combatMusic.play();
		
		
		if(party.isActive(Tolinai)){
			Texture texture2 = new Texture(Gdx.files.internal("data/princess.png"));
			//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			Sprite tolinai = new Sprite(texture2, 0, 64*3, 64, 64);
			//princess.setSize(64, 64);
			//sprite.setSize(sprite.getWidth(),sprite.getHeight());
			//sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
			tolinai.setPosition(bposx-32, bposy);
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
			leoricSprite.setPosition(bposx-32, bposy+50);
			Leoric.setSprite(leoricSprite);
			drawSprites.add(leoricSprite);
		}

		
		currentCombat = new Combat(setup, party, 5);
		this.combatEffects = new LinkedList<Sprite>();

		
		{
			for(int i = 0; i < currentCombat.getNumEnemies(); i++){
				Monster m = currentCombat.getMonster(i);
				Texture monsterTexture = new Texture(Gdx.files.internal("data/"+m.textureName));
				//texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			
				Sprite monsterSprite = new Sprite(monsterTexture, 0, 0, m.monsterType.getImageSizeX(), m.monsterType.getImageSizeY());
				
				m.setSprite(monsterSprite);
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
	
	public void loadZombieDefense(ZombieDefense zd){
		
		this.fallingTexture = null;
		for(Sprite s : drawSprites){
			s.getTexture().dispose();
		}
		this.drawSprites.clear();
		if(backgroundTexture != null)
			backgroundTexture.dispose();
		backgroundTexture = new Texture(Gdx.files.internal("data/prerenders/hometown/zombiedefense.png"));
		
		background = new Sprite(backgroundTexture, 480, 320);
		
		if(this.foreground != null){
			this.foreground.getTexture().dispose();
			this.foreground = null;
		}
		
		for(SoundInstance s : sounds)
			s.dispose();
		sounds.clear();
		
		
		if(this.currentMusic != null){
			this.currentMusic.pause();
		}
		this.combatMusic = new MusicInstance(zd.getMusic());

		this.combatMusic.setLooping(true);
		this.combatMusic.setVolume(0.7f*this.globalMusicVolume);
		this.combatMusic.play();
		
		
		this.gameMode = MODE_ZOMBIE_DEFENSE;
		
		
		this.zombieDefense = zd;
		this.zombieDefense.attackers = this.zombieDefense.getWave(this.zombieDefense.getCurrentWaveNumber());
		
		
		
		// Zombie Defense. A variant of tower defense where the player must defend a town against waves of
		// zombies coming from the edges of the map, trying to reach the town entrance.
		// The player can place walls, warriors and traps.
		// In the first level, there is only one wall type (wooden), one warrior type (archer) and one trap type (resetting bear trap).
		// If the player tries to completely wall off the entrance, the zombie lord will randomly make a hole in the players wall.
		// The player gains points for each zombie that didnt reach the entrance.
		

	}
	
	private void gameOver(){
		System.out.println("GAME OVER. :'(");
		
		if(currentMusic != null)
			currentMusic.stop();
		
		if(combatMusic != null)
			combatMusic.stop();
		
		currentMusic = new MusicInstance("data/music/Renich_-_Rola_Z.ogg");
		currentMusic.setLooping(true);
		currentMusic.setVolume(this.globalMusicVolume);
		currentMusic.play();

		gameMode = MODE_GAMEOVER;
		if(this.combatEffects != null)
			this.combatEffects.clear();
	}
	
	private void tickZombieDefense(float deltaTime){
		// TODO: moving targeting cursor around with arrow keys
		// TODO: target cursor snaps to 32x32 grid? 480/32 = 16.87, 320/32 = 10
		
		// TODO: selecting things using the targeting cursor and an action button (enter?)
		// TODO: choosing actions for the selected thing in a menu
		
		// TODO: spawn zombies from the edge that try to get to the gate.
		// TODO: zombie lord magically destroys walls that completely block zombie access.
		
		// TODO: earn random amount of monies from killing zombies..
		// TODO: go back to gamemode MODE_MOVE after winning
		// TODO: go to gamemode MODE_GAMEOVER after losing
		
		if(this.zombieDefense.getHealthLeft() <= 0){
			// lost..
			if(this.zombieDefense.isDefeatAllowed()){
				// whatever, go back to MODE_MOVE
				this.loadLevel(this.zombieDefense.getNextLevel(),
						this.zombieDefense.getNextLevelX(), this.zombieDefense.getNextLevelY(), this.lastDirection);
				return;
			}
			else {
				// game over, this one may not be lost.
				this.gameOver();
				return;
			}
		}
		
		
		if(this.zombieDefense.attackers.isEmpty()){
			// all the zombies are dead..
			// goto next wave if it exists
			// win if not
			if(this.zombieDefense.getCurrentWaveNumber() < this.zombieDefense.getNumWaves()){
				// next level
				this.zombieDefense.gotoNextWave();
				this.zombieDefense.attackers = this.zombieDefense.getWave(this.zombieDefense.getCurrentWaveNumber());
			}
			else {
				// wiin!
				this.loadLevel(this.zombieDefense.getNextLevel(),
						this.zombieDefense.getNextLevelX(), this.zombieDefense.getNextLevelY(), this.lastDirection);
				return;
			}
		}
		else {
			// move zig!
			for(Attacker attacker : this.zombieDefense.attackers){
				attacker.move(deltaTime, this.zombieDefense);
			}
			for(Defender defender : this.zombieDefense.defenders){
				defender.act(this.zombieDefense.attackers, deltaTime);
			}
			this.zombieDefense.attackers = this.zombieDefense.handleAttackers(this.zombieDefense.attackers);
			
		}
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
	
	public void drawCombatUISprites(SpriteBatch batch){
	    //LinkedList<Sprite> list = new LinkedList<Sprite>();
		
		//first make sprites for leoric.. hes always there and always nr.1 (atleast for now)
		

		PartyMember[] activeMembers = this.party.getActiveMembers();
		
		int num = 0;
		int fontOffset = 14;
		
		for(PartyMember m : activeMembers){
			
		        // draw characters name
		    
		    
		        int healthPercent = (int)Math.min(100,Math.max(0, Math.ceil(((m.getHealth()+0.0f) / m.getHealthMax())*100)));
		        Color fontColor = Color.WHITE;
			if(m.getHealth() == 0)
			    fontColor = Color.GRAY;
			else if(healthPercent <= 10)
			    fontColor = Color.YELLOW;
			

			font.setColor(fontColor);
			smallFont.setColor(fontColor);
			font.draw(batch, m.getName(), 10,67-32*num+fontOffset);
		        
			
			// draw the red inside the hp bar
		        if(m.getHealth() > 0)
			    barHp.draw(batch,102+4,67-32*num+1,healthPercent,17);

		    //Sprite myHp = new Sprite(hpTex,0,0+19*healthPerdeca,106,19);
		        Sprite myHp = new Sprite(hpTex,0,0,106,19);
			myHp.setX(102);
			myHp.setY(67-32*num);
			myHp.draw(batch);
			String healthstr = m.getHealth()+"/"+m.getHealthMax();
			smallFont.draw(batch, healthstr, 102+60-(healthstr.length()/2)*13,67-32*num+fontOffset+2);
			

			
			int manaPercent = (int)Math.min(100,Math.max(0, Math.ceil(((m.getMana()+0.0f) / m.getManaMax())*100)));
			if(m.getMana() > 0)
			    barMp.draw(batch,230+4,67-32*num+1,manaPercent,17);
			Sprite myMp = new Sprite(mpTex,0,0,106,19);
			myMp.setX(230);
			myMp.setY(67-32*num);
			myMp.draw(batch);
			String manastr = m.getMana()+"/"+m.getManaMax();
			smallFont.draw(batch, manastr, 230+60-(manastr.length()/2)*13,67-32*num+fontOffset+2);
			
			
			
			int timePercent = (int) Math.min(100,Math.max(0, Math.round(((m.actionTimer+0.0f) / 100f)*100)));
			if(m.getHealth() > 0)
			    barTime.draw(batch,358+4,67-32*num+1,100-timePercent,17);			
			Sprite myTime = new Sprite(tmTex,0,19*10,106,19);
			myTime.setX(358);
			myTime.setY(67-32*num);
			myTime.draw(batch);

			num++;
		}
		font.setColor(Color.WHITE);
		smallFont.setColor(Color.WHITE);
	}
	
	@Override
	public void create() {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		
		Monsters.initiate();
		Item.initiate();
		
		
		dialogBackground = new NinePatch(new Texture(Gdx.files.internal("data/ui/dialog_background.png")), 12, 12, 12, 12);
		announcementBackground = new NinePatch(new Texture(Gdx.files.internal("data/ui/announcement_background.png")), 11, 11, 11, 11);
		menuBackground = new NinePatch(new Texture(Gdx.files.internal("data/ui/menu_background.png")), 11, 11, 11, 11);
		
		minibarHp = new NinePatch(new Texture(Gdx.files.internal("data/ui/minibar_hp.png")), 2, 2, 2, 2);
		
		barHp = new NinePatch(new Texture(Gdx.files.internal("data/ui/microbar_hp.png")), 1, 1, 1, 1);
		barMp = new NinePatch(new Texture(Gdx.files.internal("data/ui/microbar_mp.png")), 1, 1, 1, 1);
		barTime = new NinePatch(new Texture(Gdx.files.internal("data/ui/microbar_time.png")), 1, 1, 1, 1);

		camera = new OrthographicCamera(w, h);
		camera.position.set(0, 0, 0);
		batch = new SpriteBatch();
		fontBatch = new SpriteBatch();
		drawSprites = new LinkedList<Sprite>();
		
		/*hitSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/woodenstickattack.wav"));
		biteSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/zombiebite.wav"));
		cutSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/cut.wav"));
		*/
		
		Gdx.input.setInputProcessor(this);
		
		// Load up the fonts
		
		//fontAtlas = new TextureAtlas("data");
		font = new BitmapFont(Gdx.files.internal("data/fonts/PressStart2P/PressStart2P.fnt"),false);
		smallFont = new BitmapFont(Gdx.files.internal("data/fonts/Consola Mono/ConsolaMono.fnt"),false);
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
		timeTracker.addEvent("zombie defense 1"); // first obligatory tower defense minigame
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
		questTracker.registerQuest("home1");
		questTracker.registerQuest("First potion.");
		
		questTracker.registerQuest("MyHouse-ChestN");
		questTracker.registerQuest("MyHouse-ChestW");
		
		
		Leoric = new PartyMember(0,"Leoric",50,1,200,
				20, 20, 10, 10, 10, 10, 15); // Male hero (swordsman)
		Leoric.addCombatAction(SLASH);
		Leoric.addCombatAction(CYCLONE_SLASH);
		Leoric.equipWeapon(Weapons.shortSword);
		//Leoric.mana = 1;
		party.addMember(Leoric);
		
		Tolinai = new PartyMember(1,"Tolinai",5,5,175,
				6, 10, 10, 20, 15, 17, 5); // Female, hero gf (offensive mage)
		Tolinai.addCombatAction(STAFF_STRIKE);
		Tolinai.addCombatAction(MAGIC_ARROW);
		Tolinai.equipWeapon(Weapons.woodenStick);
		
		Bert = new PartyMember(2,"Bert",150,50,0,
				15, 12, 20, 13, 12, 12, 10); // Male, archer
		Bert.addCombatAction(PUNCH);
		
		//party.addMember(Bert);
		
		Berzenor = new PartyMember(3,"Berzenor",50,50,0,
				5, 10, 10, 18, 16, 20, 10); // Male, defensive mage
		Berzenor.addCombatAction(PUNCH);
		
		Kuriko = new PartyMember(4,"Kuriko",100,50,0,
				16, 12, 20, 13, 14, 14, 16); // Female, rogue
		Kuriko.addCombatAction(PUNCH);
		
		
		party.giveItem(Item.Potion, (byte)3); // give some potions, to make the start easier
		
		party.giveItem(Item.Antidote, (byte)1);
		/*party.giveItem(Item.Hi_Potion, (byte)20);
		party.giveItem(Item.Grenade, (byte)2);
		party.giveItem(Item.Ash, (byte)1);
		party.giveItem(Item.Banana, (byte)1);
		party.giveItem(Item.Buster_Sword, (byte)1);
		party.giveItem(Item.Cloud, (byte)1);
		party.giveItem(Item.Ether, (byte)1);
		party.giveItem(Item.Hi_Ether, (byte)1);
		party.giveItem(Item.Hot_Soup, (byte)1);
		party.giveItem(Item.Panties, (byte)1);
		party.giveItem(Item.Phoenix_Feather, (byte)1);
		party.giveItem(Item.Super_Ether, (byte)1);
		party.giveItem(Item.Tissue, (byte)1);*/
		//party.giveItem(Item.StrUp, (byte) 10);
		loadLevel(new Church(),522,414,1);// church the real start point
		
		/*
		this.addMember(new PartyMember(2,"Bert",50,50,10,10,0)); // Male, archer
		this.addMember(new PartyMember(3,"Berzenor",40,40,60,60,0)); // Male, defensive mage
		this.addMember(new PartyMember(4, "Kiriko",70,70,30,30,0)); // Female, rogue*/
		
		if(debugStart){
		    loadLevel(new HomeTownNight(), 3005, 1326,1); //TODO: remove debugging stuffs
		    party.addMember(Tolinai); // TODO: remove this!!
		    timeTracker.setTime("east house?"); // TODO: remove debug test
		}
		
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
	
	private static LinkedList<AnimatedMissile> animatedMissiles = new LinkedList<AnimatedMissile>();
	
	public static void sendAnimatedMissile(Sprite s, int fromx, int tox, int fromy, int toy, float speed){
		AnimatedMissile missile = new AnimatedMissile(s, speed, fromx, fromy, tox, toy);
		
		animatedMissiles.add(missile);
	}

        private static LinkedList<TemporaryAnimation> animations = new LinkedList<TemporaryAnimation>();

        public static void addAnimation(Sprite s, int x, int y, int time){
	    animations.add(new TemporaryAnimation(s,x,y,time/10f));
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
		
		for(SoundInstance s : sounds)
			s.dispose();
		sounds.clear();
		
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
		
		String newMusic = level.getMusic();
		
		if(this.combatMusic != null){
			this.combatMusic.stop();
			this.combatMusic.dispose();
			this.combatMusic = null;
			this.currentMusic.play();
		}
		
		if(this.currentMusic != null){
			if(this.currentMusic.isSame(newMusic)){
				// just keep playing
			}
			else {
				// stop this one
				// TODO: fading out would be nicer..
				this.currentMusic.stop();
				this.currentMusic.dispose();
				this.currentMusic = null;
				
				if(level.getMusic() != null){
					this.currentMusic = new MusicInstance(newMusic);
					this.currentMusic.setVolume(this.globalMusicVolume);
					this.currentMusic.setLooping(true);
					this.currentMusic.play();
				}
			}
		}
		else {
			if(level.getMusic() != null){
				this.currentMusic = new MusicInstance(newMusic);
				this.currentMusic.setLooping(true);
				this.currentMusic.setVolume(this.globalMusicVolume);
				this.currentMusic.play();
			}
		}
		
		
		
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
	
	private static float musicVolume = 1f;
	private static MusicInstance fadingMusic;
	
	public static void fadeMusicIn(MusicInstance music){
		System.out.println("fading music in..");
		music.playNow();
		music.setVolume(0.1f);
		musicVolume = 0.1f;
		fadingMusic = music;
	}

	@Override
	public void render() {
		
		if(this.menu != null){
			if(this.menu.isDead()){
				// menu is scheduled for deletion, so carry it out
				this.menu.dispose();
				this.menu = null;
				this.gameMode = MODE_MOVE;
			}
		}
		
		if(musicVolume < 1f){
			musicVolume += Gdx.graphics.getDeltaTime()/10f;
			if(musicVolume > 1f){
				musicVolume = 1f;
				System.out.println("Done fading..");
			}
			fadingMusic.setVolume(musicVolume*this.globalMusicVolume);
		}
		
		
		if(musicPause > 0){
			musicPause -= Gdx.graphics.getDeltaTime();
			if(musicPause <= 0){
				musicPause = 0;
				// resume music
				if(this.combatMusic != null)
					this.combatMusic.play();
				
				else if(this.currentMusic != null)
					this.currentMusic.play();
			}
			
			else {
				// pause music or keep it paused
				if(this.combatMusic != null)
					this.combatMusic.pause();
				
				if(this.currentMusic != null)
					this.currentMusic.pause();
			}
		}
		
		
		
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
				if(posy+h/2-s.getY() > this.minFallDist){
					// OK to delete?
					if(posy+h/2-s.getY() >= this.maxFallDist){
						this.drawSprites.remove(s);
						iter.remove();
					}
					else if(Math.random()*100 > 90){
					    addAnimation(new Sprite(new Texture(Gdx.files.internal("data/rain_splash.png"))), (int)s.getX(), (int)s.getY(), 3);
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
			// a Dialog is active or using the menus?
			
			if(this.currentDialog == null){
				// menu
				// TODO: ???
			}
			else {
				// dialog
				if(this.dialogWait > 0){
				        this.dialogTicker += Gdx.graphics.getDeltaTime()*20;
					if(Gdx.input.isButtonPressed(Keys.ENTER))// Speed up the dialog a bit
						dialogWait = 0;
					else
						dialogWait -= Gdx.graphics.getDeltaTime();
					
				}
				else if(this.currentDialog.hasNextUtterance()){
					//TODO: this has to be done more nicely somehow..
					
					Utterance u = this.currentDialog.getNextUtterance();
					
					System.out.print(u.speaker+": ");
					//System.out.println(u.sentence);
					
					this.curSpeaker = u.speaker;
					this.curSentence = u.getSentence();
					this.dialogTicker = 1;
					//for(int i = 0; i < this.curSentence.length; i++)
					//	System.out.println("_"+this.curSentence[i]+"_");

					dialogWait = (u.length*0.07f); // TODO: hum hum
					if(dialogWait < 1.7f)
						dialogWait = 1.7f;

				}
				else {
					// apply secondary effects and change gameMode back to whatever normal is
					// TODO: would be nice if dialogs could be had inside combat also..
					// but not strictly required.
					this.curSpeaker = null;
					this.curSentence = null;
					
					if(this.currentDialog.questHint != null){
						this.questHint = this.currentDialog.questHint;
					}
					
					if(this.currentDialog.getTimeChange() != null){
						this.timeTracker.setTime( this.currentDialog.getTimeChange()  );
					}
					
					ZombieDefense zd = this.currentDialog.getZombieDefense();
					if(zd != null){
						// Start zombie defense by going to the correct game mode..
						this.gameMode = MODE_ZOMBIE_DEFENSE;
						this.loadZombieDefense(zd);
						
						
						// Zombie Defense has highest priority.
						this.currentDialog = null;
						return;
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
						return;
					}
					this.currentDialog = null;
					
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
				//for(Sprite s : drawSprites){
					//s.getTexture().dispose();
				//}
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
			this.drawSprites.clear();
			
		}
		
		else if(gameMode == MODE_FIGHT){
			
			// Re-position all creatures..
			for(int i = 0; i < currentCombat.getLiveCombatants().size(); i++){
				Combatant current = currentCombat.getLiveCombatants().get(i);
				
				switch(current.getTerminalState()){
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
					if(this.selectedCombatOption == ZombieLord.defend){
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
					else if(this.selectedCombatOption == ZombieLord.escape){
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
					else if(this.selectedCombatOption == ZombieLord.item){
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
								    ZombieLord.playSound("data/sound/woodenstickattack.wav", 1f);
								}
								if(myAction.caster == Leoric){ // TODO: simple hack to get sound..
								    ZombieLord.playSound("data/sound/cut.wav", 1f);
								}
								
								announce(myAction.action.name);
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
					this.gameOver();
				}
				
				Monster readyMonster = currentCombat.getFirstReadiedMonster();
				if(readyMonster != null && currentCombat.getLivePlayers().size() > 0){
					
					CurrentAction myAction = readyMonster.getMonsterAction(party, currentCombat);
					
					LinkedList<Combatant> affected = currentCombat.applyAction(myAction);
					
					if(myAction.action != null){
						// Move monster against player to represent the attack
						
						announce(myAction.action.name);
						myAction.caster.setMoveAhead(true);
						
						if(myAction.action.effect != null && affected != null){
							
							if(myAction.action == BITE){ // TODO: simple hack to get sound..
							    ZombieLord.playSound("data/sound/zombiebite.wav", 1f);
							}
							if(myAction.action == PUNCH){ // TODO: simple hack to get sound..
							    ZombieLord.playSound("data/sound/woodenstickattack.wav", 1f);
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
							
							if(ca.category == ZombieLord.OFFENSIVE_MAGIC || ca.category == ZombieLord.DEFENSIVE_MAGIC)
								magic.add(ca);
							else if(ca.category == ZombieLord.ATTACK)
								attack = ca;
							else if(ca.category == ZombieLord.SUMMON)
								summons.add(ca);
						}
						
						this.currentCombatOptions = new LinkedList<CombatOption>();
						this.selectedCombatOption = null;
						if(attack != null)
							currentCombatOptions.add(new CombatOption("Attack",attack));
						if(canDefend)
							currentCombatOptions.add(ZombieLord.defend);
						if(magic.size() > 0)
							currentCombatOptions.add(new CombatOption("Magic",magic));
						if(summons.size() > 0)
							currentCombatOptions.add(new CombatOption("Summon",summons));
						if(haveItem)
							currentCombatOptions.add(ZombieLord.item);
						if(canEscape)
							currentCombatOptions.add(ZombieLord.escape);
						
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
			if(!waiting){
				currentCombat.tick(Gdx.graphics.getDeltaTime()*5*2);
			}
		}
		
		if(this.gameMode == MODE_ZOMBIE_DEFENSE || this.gameMode == MODE_FIGHT){
			camera.position.x = w/2f;
			camera.position.y = h/2f;
			camera.update();
		}
		
		
		//background = new Sprite(backgroundTexture, (int)(posx-w), (int)(posy-h), (int)(posx+w), (int)(posy+h));
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		background.draw(batch);
		
		if(this.gameMode == MODE_MOVE || this.gameMode == MODE_DIALOG){
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
			this.drawCombatUISprites(batch);
			//LinkedList<Sprite> uiElements = this.getCombatUISprites();
			//for(Sprite s : uiElements)
			//	s.draw(batch);
		}
		
		if(animations.size() > 0) {
		    ListIterator<TemporaryAnimation> lit = animations.listIterator();
		    while(lit.hasNext()) {
			    TemporaryAnimation ta = lit.next();
			    
			    if(ta.timeLeft <= 0){
				lit.remove();
				continue;
			    }

			    ta.sprite.setPosition(ta.x,ta.y);
			    ta.sprite.draw(batch);
			    ta.timeLeft -= Gdx.graphics.getDeltaTime();
		    }
		}

		if(animatedMissiles.size() > 0){
			ListIterator<AnimatedMissile> it = animatedMissiles.listIterator();
			while(it.hasNext()){
				AnimatedMissile missile = it.next();
				
				if(missile.hasArrived()){
					// delete it..
					missile.sprite = null;
					it.remove();
					continue;
				}
				// TODO: use box2d to move the missiles instead?
				float xChange = 0;
				float yChange = 0;
				
				float reqX = missile.sprite.getX() - missile.toX;
				float reqY = missile.sprite.getY() - missile.toY;
				
				if(reqY == 0)
					reqY = 0.001f;
				
				float xyRate = Math.abs(reqX)/Math.abs(reqY);
				
				xChange = xyRate*missile.speed;
				yChange = 1*missile.speed;
				
				if(reqX > 0)
					xChange *= -1;
				
				if(reqY > 0)
					yChange *= -1;

				missile.sprite.setX(missile.sprite.getX()+xChange);
				missile.sprite.setY(missile.sprite.getY()+yChange);
				
				missile.sprite.draw(batch);
				
			}
		}
		
		if(gameMode == MODE_ZOMBIE_DEFENSE){
			
			// draw monies and helps
			//fontBatch.begin();
			
			font.setColor(Color.WHITE);
			font.draw(batch, "Money: "+this.zombieDefense.money+", Life: "+this.zombieDefense.getHealthLeft()+", Wave: "+this.zombieDefense.getCurrentWaveNumber()+".", 3, 18);
			
			//fontBatch.end();
			
			// draw all the attackers..
			this.tickZombieDefense(Gdx.graphics.getDeltaTime());
			for(Attacker atk : this.zombieDefense.attackers){
				atk.draw(batch, Gdx.graphics.getDeltaTime());
				barHp.draw(batch, atk.getX()*32+8, atk.getY()*32, (float)((atk.health+0.0)/atk.healthMax)*20, 3);
			}
			for(Defender def : this.zombieDefense.defenders){
				def.draw(batch, Gdx.graphics.getDeltaTime());
				barHp.draw(batch, def.getX()*32+8, def.getY()*32, (float)((def.health+0.0)/def.healthMax)*20, 3);
			}
			this.zombieDefense.cursor.draw(batch);
			
			
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
		
		if(!floatingNumbers.isEmpty()){
		    ListIterator<FloatingNumber> lit = floatingNumbers.listIterator();
		    fontBatch.begin();
		    while(lit.hasNext()){
			FloatingNumber f = lit.next();
			// draw, then tick, then maybe delete
			Color myColor = new Color(f.color);
			
			// TODO: fade when timeleft is close to zero
			if(f.timeLeft < 1)
			    myColor.a = f.timeLeft;
			
			font.setColor(myColor);
			font.draw(fontBatch, ""+f.number, (int)f.posx, (int)f.posy);
			f.tick(Gdx.graphics.getDeltaTime());
			font.setColor(Color.WHITE);
			if(f.deleteNow)
			    lit.remove();
		    }
		    fontBatch.end();
		}

		if(announcement != null){
			if(announcementTimeout > 0){
				fontBatch.begin();
				announcementBackground.draw(fontBatch, w/2-announcement.length()*6-4, h-24-13, announcement.length()*13, 20+2);
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
			// TODO: draw current speakers portrait?
			
			int slength = -1;
			for (int i = 0; i < this.curSentence.length; i++){
				slength += this.curSentence[i].length()+1;
				//System.out.println("_"+this.curSentence[i]+"_");
			}
			int length = this.curSpeaker.length()+2+slength;
			float cposx = w/2;
			float cposy = h/3;

			if(this.curSpeaker.equals("Leoric")){
			    Sprite face = new Sprite(Leoric.getFace());
			    //face.scale(-0.4f);
			    face.setPosition(5,cposy+32+32);
			    face.draw(fontBatch);
			}
			else if(this.curSpeaker.equals("Tolinai")){
			    Sprite face = new Sprite(Tolinai.getFace());
			    //face.scale(-0.4f);
			    face.setPosition(5,cposy+32+32);
			    face.draw(fontBatch);
			}
			
			if(length > 32){
				// needs to be split
				//int remain = length;
				//int num = 0;
				//int start = 0;
				int startWord = 0;
				int maxLength = 0;
				int remainWords = this.curSentence.length;
				LinkedList<StringBuilder> sbl = new LinkedList<StringBuilder>();

				
				while(remainWords > 0){
					/*int printed = Math.min(32, remain);
					
					int first = start;
					int lastMinusOne = start+printed;*/
					StringBuilder sb = new StringBuilder();
					
					int numLetters = 0;
					int numWords = 0;
					for(int i = startWord; i < this.curSentence.length; i++){
						if(numLetters+this.curSentence[i].length()+(startWord == 0? this.curSpeaker.length():1) > 32){
							break;
						}
						else {
							if(i != 0 && numWords != 0)
								sb.append(" ");
							sb.append(this.curSentence[i]);
							numLetters = sb.length();
							numWords++;
						}
					}
					
					sbl.add(sb);

					
					/*remain -= printed;
					start += printed;*/
					remainWords -= numWords;
					startWord += numWords;

					if(numLetters > maxLength)
					    maxLength = numLetters;
					
				}

				dialogBackground.draw(fontBatch, 0, cposy+h/2-h/3-16-(16*(int)Math.floor(length/32)), 32*13+5, 20+(16*(length/32))+2 );
				int tickerLimit = (int)this.dialogTicker;
				for(int i = 0; i < sbl.size(); i++){
				    if(tickerLimit <= 0){
					break;
				    }
				    String mystr = sbl.get(i).toString();
				    if(mystr.length() > tickerLimit){
					// delete overflowing text
					StringBuilder tmp = new StringBuilder();
					for(int j = 0; j < tickerLimit; j++){
					    tmp.append(mystr.charAt(j));
					}
					mystr = tmp.toString();
					tickerLimit = 0;
				    }
				    tickerLimit -= mystr.length();
				    font.draw(fontBatch, (i == 0?this.curSpeaker+": ":"")+mystr, cposx-w/2+(i==0?0:30), cposy+h/2-h/3-(i*16));
				}
			}
			else {
				// print everything in one go
				StringBuilder all = new StringBuilder();
				for(int i = 0; i < this.curSentence.length; i++){
				        if(i > 0){
						all.append(" ");
					}
					all.append(curSentence[i]);
				}
				dialogBackground.draw(fontBatch, 0, cposy+h/2-h/3-16, length*13, 20+2);
				int tickerLimit = (int)this.dialogTicker;
				String mystr = all.toString();
				if(mystr.length() > tickerLimit){
				    // delete overflowing text
				    StringBuilder tmp = new StringBuilder();
				    for(int j = 0; j < tickerLimit; j++){
					tmp.append(mystr.charAt(j));
				    }
				    mystr = tmp.toString();
				}
				font.draw(fontBatch, this.curSpeaker+": "+mystr, cposx-w/2, cposy+h/2-h/3);
			}
			fontBatch.end();
			
		}
		else if(this.menu != null){
			fontBatch.begin();
			menu.draw(fontBatch);
			fontBatch.end();
		}
		
		if(debug && world != null && debugRenderer != null)
			debugRenderer.render(world, camera.combined.scale(
					PIXELS_PER_METER,
					PIXELS_PER_METER,
					PIXELS_PER_METER));
	}
	
	private Menu menu;


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
		
		if(this.menu != null){
			// inside a menu, allow moving the selection cursor..
			if(keycode == Keys.UP)
				menu.selectPrevious();
			if(keycode == Keys.DOWN)
				menu.selectNext();
			if(keycode == Keys.ENTER)
				menu.confirmChoice(this);
			if(keycode == Keys.BACKSPACE)
				menu.exit(this);
		}
		
		if(this.gameMode == MODE_ZOMBIE_DEFENSE){
			// cursor control
			if(keycode == Keys.UP){
				if(this.zombieDefense.cursor.y >= this.zombieDefense.maxy-1)
					System.out.println("nop");
				else
					this.zombieDefense.cursor.y ++;
			}
			if(keycode == Keys.DOWN){
				if(this.zombieDefense.cursor.y <= this.zombieDefense.miny+1)
					System.out.println("nop");
				else
					this.zombieDefense.cursor.y --;
			}
			if(keycode == Keys.LEFT){
				if(this.zombieDefense.cursor.x <= this.zombieDefense.minx+1)
					System.out.println("nop");
				else
					this.zombieDefense.cursor.x --;
			}
			if(keycode == Keys.RIGHT){
				if(this.zombieDefense.cursor.x >= this.zombieDefense.maxx-1)
					System.out.println("nop");
				else
					this.zombieDefense.cursor.x ++;
			}
			// cycle defender type to place
			if(keycode == Keys.BACKSPACE){
				this.zombieDefense.cyclePlaceableDefenders();
			}
			// place defender
			if(keycode == Keys.ENTER){
				boolean success = this.zombieDefense.addDefense();
				if(!success)
					System.out.println("Tile not empty or not enough money!");
			}
		}
		
		if(this.gameMode == MODE_MOVE){
			if(keycode == Keys.M){
				// bring up the fabled catch-all menu
				this.currentDialog = null;
				this.gameMode = MODE_DIALOG;
				menu = new Menu(this.menuBackground,this.font,20, "data/ui/selectionhand.png",party);
			}
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
								announce("Acquired a " + ((LevelItem)object).getItemType().name+"!");
								System.out.println("Acquired a " + ((LevelItem)object).getItemType().name+"!");
								ZombieLord.playSound("data/sound/pickupItem.wav",1.5f);
								iter.remove();
							}
							else {
								// TODO: draw some text to tell the player he can't carry more (ingame)..
								announce("Can't pick up the " + ((LevelItem)object).getItemType().name+", inventory is full!");
								System.out.println("Can't pick up the " + ((LevelItem)object).getItemType().name+", inventory is full!");
							}
						}
						else if(object instanceof Chest){
							Chest chest = (Chest)object;
							String msg = chest.getDialogString();
							if(msg != null){
								// TODO: proper ingame announcement
								announce(msg);
								ZombieLord.playSound("data/sound/pickupItem.wav",1.5f);
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
	
	public static void announce(String text){
		announcement = text;
		announcementTimeout = 2f;
	}

        private static class FloatingNumber {
	    public int number;
	    public float timeLeft;
	    public float posx;
	    public float posy;
	    public Color color;
	    public boolean deleteNow;
	    
	    public void tick(float delta){
		// move the numbers upwards

		posy += delta*12;
		timeLeft -= delta*1.5;
		
		if(this.timeLeft <= 0)
		    this.deleteNow = true;
	    }
	}

        private static LinkedList<FloatingNumber> floatingNumbers = new LinkedList<FloatingNumber>();
        
        public static void addFloatingNumbers(int number, float posx, float posy, Color color){
	    FloatingNumber num = new FloatingNumber();
	    num.number = number;
	    num.timeLeft = 3;
	    num.posx = posx;
	    num.posy = posy;
	    num.color = color;
	    num.deleteNow = false;
	    floatingNumbers.add(num);
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
