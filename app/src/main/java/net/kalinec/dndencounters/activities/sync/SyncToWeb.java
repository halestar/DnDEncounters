package net.kalinec.dndencounters.activities.sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.dice.DiceParser;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.modules.Modules;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterAbility;
import net.kalinec.dndencounters.monsters.Monsters;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.Players;
import net.kalinec.dndencounters.sync.LinkData;
import net.kalinec.dndencounters.sync.SyncData;
import net.kalinec.dndencounters.sync.SyncPayload;
import net.kalinec.dndencounters.sync.SyncPlayerPayload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncToWeb extends AppCompatActivity
{
	private final static int oAuthClientId = 2;
	private final static String oAuthGrantPassword = "password";
	private final static String oAuthGrantRefreh = "refresh_token";
	private final static String webUrl = "http://192.168.10.10";
	private EditText emailEt, passwordEt;
	private TextView resultsTv;
	private LinearLayout loginContainer, loggedInLy;

	public Handler mHandler;

	private SharedPreferences prefs;
	private final static String PREF_KEY = "net.kalinec.dnd";
	private final static String PREF_EMAIL_KEY = "net.kalinec.dnd.email";
	private final static String PREF_CLIENT_SECRET_KEY = "net.kalinec.dnd.client_secret";
	private final static String PREF_ACCESS_TOKEN_KEY = "net.kalinec.dnd.access_token";
	private final static String PREF_REFRESH_TOKEN_KEY = "net.kalinec.dnd.refresh_token";
	private final static String PREF_TOKEN_EXPIRATION_KEY = "net.kalinec.dnd.expires_in";

	private final static String BUNDLE_ACTION = "action";
	private final static String BUNDLE_MSG = "message";
	private final static int ACTION_UPDATE_MSG = 1;
	private final static int ACTION_LOAD_TOKEN = 2;
	private final static int ACTION_UPDATE_LINKS = 3;
	private final static int ACTION_LOAD_SYNC_DATA = 4;
	private final static int ACTION_READ_CLIENT_SECRET = 5;
	private final static int ACTION_ERROR = 6;
	private final static int ACTION_FINISH_UPDATE_LINKS = 7;
	private final static int ACTION_SHOW_PROGRESS = 8;
	private ProgressBar pdBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_to_web);
		prefs = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
		emailEt = findViewById(R.id.emailEt);
		emailEt.setText(prefs.getString(PREF_EMAIL_KEY, ""));
		passwordEt = findViewById(R.id.passwordEt);
		loginContainer = findViewById(R.id.loginContainer);
		loggedInLy = findViewById(R.id.loggedInLy);
		pdBar = findViewById(R.id.pdBar);
		resultsTv = findViewById(R.id.resultsTv);

		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Bundle bundle = msg.getData();
				int action = bundle.getInt(BUNDLE_ACTION);
				if(action == ACTION_UPDATE_MSG)
					resultsTv.setText(bundle.getString(BUNDLE_MSG));
				else if(action == ACTION_LOAD_TOKEN)
				{
					readToken(bundle.getString(BUNDLE_MSG));
					refreshScreen();
				}
				else if(action == ACTION_UPDATE_LINKS)
				{
					updateLinks(bundle);
					refreshScreen();
				}
				else if(action == ACTION_LOAD_SYNC_DATA)
				{
					loadSyncData(bundle);
					refreshScreen();
				}
				else if(action == ACTION_READ_CLIENT_SECRET)
				{
					readClientSecret(bundle.getString(BUNDLE_MSG));
				}
				else if(action == ACTION_ERROR)
				{
					pdBar.setVisibility(View.INVISIBLE);
					resultsTv.setVisibility(View.INVISIBLE);
					Toast toast = Toast.makeText(getApplicationContext(), "Error: " + bundle.getString(BUNDLE_MSG), Toast.LENGTH_LONG);
					toast.show();
				}
				else if(action == ACTION_FINISH_UPDATE_LINKS)
				{
					pdBar.setVisibility(View.INVISIBLE);
					resultsTv.setVisibility(View.INVISIBLE);
					Toast toast = Toast.makeText(getApplicationContext(), "Successfully synced data from web!", Toast.LENGTH_LONG);
					toast.show();
				}
				else if(action == ACTION_SHOW_PROGRESS)
				{
					pdBar.setVisibility(View.VISIBLE);
					resultsTv.setVisibility(View.VISIBLE);
				}
			}
		};


		refreshScreen();

		Calendar cal = Calendar.getInstance();
		if(prefs.getString(PREF_REFRESH_TOKEN_KEY, null) != null && cal.getTime().getTime() >= prefs.getLong(PREF_TOKEN_EXPIRATION_KEY, -1))
			refreshToken();
	}

	private void updateLinks(Bundle data)
	{
		try
		{
			resultsTv.setText("Reading reply from server...");
			JSONObject json = new JSONObject(data.getString(BUNDLE_MSG));
			//update cuscom monsters
			JSONArray custom_monsters = json.getJSONArray("custom_monsters");
			List<CustomMonster> customMonsterList = CustomMonsters.getCustomMonsters(getApplicationContext());
			for(int i = 0; i < custom_monsters.length(); i++)
			{
				JSONObject monster_json = custom_monsters.getJSONObject(i);
				for(int j = 0; j < customMonsterList.size(); j++)
				{
					CustomMonster customMonster = customMonsterList.get(j);
					if(customMonster.getUuid().toString() == monster_json.getString("uuid"))
					{
						customMonster.setDbId(Long.parseLong(monster_json.getString("dbId")));
						CustomMonsters.updateCustomMonster(getApplicationContext(), customMonster);
					}
				}
			}
			//update encounters
			JSONArray encounters = json.getJSONArray("encounters");
			List<Encounter> encounterList = Encounters.getAllEncounters(getApplicationContext());
			for(int i = 0; i < encounters.length(); i++)
			{
				JSONObject encounter_json = encounters.getJSONObject(i);
				for(int j = 0; j < encounterList.size(); j++)
				{
					Encounter myEncounter = encounterList.get(j);
					if(myEncounter.getUuid().toString() == encounter_json.getString("uuid"))
					{
						myEncounter.setDbId(Long.parseLong(encounter_json.getString("dbId")));
						Encounters.updateEncounter(getApplicationContext(), myEncounter);
					}
				}
			}
			//update monster tokens
			JSONArray tokens = json.getJSONArray("monster_tokens");
			List<MonsterToken> monsterTokenList = MonsterTokens.getAllMonsterTokens(getApplicationContext());
			for(int i = 0; i < tokens.length(); i++)
			{
				JSONObject token_json = tokens.getJSONObject(i);
				for(int j = 0; j < monsterTokenList.size(); j++)
				{
					MonsterToken myToken = monsterTokenList.get(j);
					if(myToken.getUuid().toString() == token_json.getString("uuid"))
					{
						myToken.setDbId(Long.parseLong(token_json.getString("dbId")));
						MonsterTokens.updateMonsterToken(getApplicationContext(), myToken);
					}
				}
			}
			//update players
			JSONArray players = json.getJSONArray("players");
			List<Player> playerList = Players.getAllPlayers(getApplicationContext());
			for(int i = 0; i < players.length(); i++)
			{
				JSONObject player_json = players.getJSONObject(i);
				for(int j = 0; j < playerList.size(); j++)
				{
					Player myPlayer = playerList.get(j);
					if(myPlayer.getUuid().toString() == player_json.getString("uuid"))
					{
						myPlayer.setDbId(Long.parseLong(player_json.getString("dbId")));
						Players.updatePlayer(getApplicationContext(), myPlayer);
						//next, we do all the characters.
						JSONArray pcs = player_json.getJSONArray("pcs");
						List<Character> pcList = myPlayer.getPcs();
						for(int k = 0; k < pcs.length(); k++)
						{
							JSONObject pc_json = pcs.getJSONObject(k);
							for(int l = 0; l < pcList.size(); l++)
							{
								Character myPc = pcList.get(l);
								if(myPc.getUuid().toString() == pc_json.getString("uuid"))
								{
									myPc.setDbId(Integer.parseInt(pc_json.getString("dbId")));
									Players.updatePc(getApplicationContext(), myPc);
								}
							}
						}
					}
				}
			}

			//update players
			JSONArray modules = json.getJSONArray("modules");
			List<Module> moduleList = Modules.getAllModules(getApplicationContext());
			for(int i = 0; i < modules.length(); i++)
			{
				JSONObject module_json = modules.getJSONObject(i);
				for(int j = 0; j < moduleList.size(); j++)
				{
					Module myModule = moduleList.get(j);
					if(myModule.getUuid().toString() == module_json.getString("uuid"))
					{
						myModule.setDbId(Long.parseLong(module_json.getString("dbId")));
						Modules.updateModule(getApplicationContext(), myModule);
						//next, we do all the characters.
						JSONArray module_encounter = module_json.getJSONArray("encounters");
						List<Encounter> moduleEncountersList = myModule.getEncounters();
						for(int k = 0; k < module_encounter.length(); k++)
						{
							JSONObject module_encounter_json = module_encounter.getJSONObject(k);
							for(int l = 0; l < moduleEncountersList.size(); l++)
							{
								Encounter myModuleEncounter = moduleEncountersList.get(l);
								if(myModuleEncounter.getUuid().toString() == module_encounter_json.getString("uuid"))
								{
									myModuleEncounter.setDbId(Integer.parseInt(module_encounter_json.getString("dbId")));
									Encounters.updateEncounter(getApplicationContext(), myModuleEncounter);
								}
							}
						}
					}
				}
			}

			Toast toast = Toast.makeText(getApplicationContext(), "Successfully synced data to the web!", Toast.LENGTH_LONG);
			toast.show();
		}
		catch (JSONException e)
		{

			Toast toast = Toast.makeText(getApplicationContext(), "Error syncing data!", Toast.LENGTH_LONG);
			toast.show();
		}
		pdBar.setVisibility(View.INVISIBLE);
		resultsTv.setVisibility(View.INVISIBLE);
	}
	
	private SyncPayload addCustomMonster(JSONObject stats) throws JSONException
	{
		CustomMonster monster = new CustomMonster(stats.getString("name"));
		monster.setDbId(stats.getInt("id"));
		monster.setCr(stats.getString("cr"));
		monster.setMonsterType(stats.getString("monsterType"));
		monster.setMonsterSize(stats.getString("monsterSize"));
		monster.setAlignment(stats.getString("alignment"));
		monster.setResistances(stats.getString("resistances"));
		monster.setImmunities(stats.getString("immunities"));
		monster.setVulnerabilities(stats.getString("vulnerabilities"));
		monster.setLanguages(stats.getString("languages"));
		monster.setSenses(stats.getString("senses"));
		monster.setStr(stats.getInt("str"));
		monster.setDex(stats.getInt("dex"));
		monster.setCon(stats.getInt("con"));
		monster.setIntel(stats.getInt("int"));
		monster.setWis(stats.getInt("wis"));
		monster.setCha(stats.getInt("cha"));
		monster.setHp(stats.getInt("hp"));
		monster.setAc(stats.getInt("ac"));
		monster.setSpeed(stats.getString("speed"));
		monster.setHitDice(new DiceParser(stats.getString("hd")));
		//special abilities
		ArrayList<MonsterAbility> abilities = new ArrayList<>();
		JSONArray special_abilities_json = stats.getJSONArray("special_abilities");
		for(int i = 0; i < special_abilities_json.length(); i++)
			abilities.add(new MonsterAbility(special_abilities_json.getJSONObject(i).getString("name"), special_abilities_json.getJSONObject(i).getString("description")));
		monster.setSpecialAbilities(abilities);
		//actions
		abilities = new ArrayList<>();
		JSONArray actions_json = stats.getJSONArray("actions");
		for(int i = 0; i < actions_json.length(); i++)
			abilities.add(new MonsterAbility(actions_json.getJSONObject(i).getString("name"), actions_json.getJSONObject(i).getString("description")));
		monster.setActions(abilities);
		//special abilities
		abilities = new ArrayList<>();
		JSONArray legendary_abilities_json = stats.getJSONArray("legendary_abilities");
		for(int i = 0; i < legendary_abilities_json.length(); i++)
			abilities.add(new MonsterAbility(legendary_abilities_json.getJSONObject(i).getString("name"), legendary_abilities_json.getJSONObject(i).getString("description")));
		monster.setLegendaryAbilities(abilities);
		//save the monster
		CustomMonsters.addCustomMonster(getApplicationContext(), monster);
		return new SyncPayload(monster.getUuid().toString(), monster.getDbId());
	}
	
	private SyncPayload updateCustomMonster(CustomMonster monster, JSONObject stats) throws JSONException
	{
		monster.setMonsterName(stats.getString("name"));
		monster.setDbId(stats.getInt("id"));
		monster.setCr(stats.getString("cr"));
		monster.setMonsterType(stats.getString("monsterType"));
		monster.setMonsterSize(stats.getString("monsterSize"));
		monster.setAlignment(stats.getString("alignment"));
		monster.setResistances(stats.getString("resistances"));
		monster.setImmunities(stats.getString("immunities"));
		monster.setVulnerabilities(stats.getString("vulnerabilities"));
		monster.setLanguages(stats.getString("languages"));
		monster.setSenses(stats.getString("senses"));
		monster.setStr(stats.getInt("str"));
		monster.setDex(stats.getInt("dex"));
		monster.setCon(stats.getInt("con"));
		monster.setIntel(stats.getInt("int"));
		monster.setWis(stats.getInt("wis"));
		monster.setCha(stats.getInt("cha"));
		monster.setHp(stats.getInt("hp"));
		monster.setAc(stats.getInt("ac"));
		monster.setSpeed(stats.getString("speed"));
		monster.setHitDice(new DiceParser(stats.getString("hd")));
		//special abilities
		ArrayList<MonsterAbility> abilities = new ArrayList<>();
		JSONArray special_abilities_json = stats.getJSONArray("special_abilities");
		for(int i = 0; i < special_abilities_json.length(); i++)
			abilities.add(new MonsterAbility(special_abilities_json.getJSONObject(i).getString("name"), special_abilities_json.getJSONObject(i).getString("description")));
		monster.setSpecialAbilities(abilities);
		//actions
		abilities = new ArrayList<>();
		JSONArray actions_json = stats.getJSONArray("actions");
		for(int i = 0; i < actions_json.length(); i++)
			abilities.add(new MonsterAbility(actions_json.getJSONObject(i).getString("name"), actions_json.getJSONObject(i).getString("description")));
		monster.setActions(abilities);
		//special abilities
		abilities = new ArrayList<>();
		JSONArray legendary_abilities_json = stats.getJSONArray("legendary_abilities");
		for(int i = 0; i < legendary_abilities_json.length(); i++)
			abilities.add(new MonsterAbility(legendary_abilities_json.getJSONObject(i).getString("name"), legendary_abilities_json.getJSONObject(i).getString("description")));
		monster.setLegendaryAbilities(abilities);
		//save the monster
		CustomMonsters.updateCustomMonster(getApplicationContext(), monster);
		return new SyncPayload(monster.getUuid().toString(), monster.getDbId());
	}
	
	private ArrayList<SyncPayload> syncCustomMonsters(JSONArray monsters) throws JSONException
	{
		ArrayList<SyncPayload> ids = new ArrayList<>();
		for(int i = 0; i < monsters.length(); i++)
		{
			JSONObject monster_json = monsters.getJSONObject(i);
			//try to load the monster.
			CustomMonster myMonster = CustomMonsters.getMonsterByUuid(getApplicationContext(), monster_json.getString("uuid"));
			if(myMonster == null)
			{
				//in this case, we must add it.
				ids.add(addCustomMonster(monster_json));
			}
			else
			{
				//in this case, we update it.
				ids.add(updateCustomMonster(myMonster, monster_json));
			}
		}
		return ids;
	}
	
	private SyncPayload addMonsterToken(JSONObject stats) throws JSONException
	{
		MonsterToken token;
		if(stats.getString("token_type").equals(MonsterToken.WEB_TOKEN_NUMBER))
		{
			token = new MonsterToken(MonsterToken.TOKEN_TYPE_NUMBER);
			token.setTokenNumber(stats.getInt("token_number"));
		}
		else if(stats.getString("token_type").equals(MonsterToken.WEB_TOKEN_COLOR))
		{
			token = new MonsterToken(MonsterToken.TOKEN_TYPE_COLOR);
			token.setTokenColor(Color.parseColor(stats.getString("token_color")));
		}
		else if(stats.getString("token_type").equals(MonsterToken.WEB_TOKEN_COLORED_NUMBER))
		{
			token = new MonsterToken(MonsterToken.TOKEN_TYPE_COLORED_NUMBER);
			token.setTokenNumber(stats.getInt("token_number"));
			token.setTokenColor(Color.parseColor(stats.getString("token_color")));
		}
		else
		{
			token = new MonsterToken(MonsterToken.TOKEN_TYPE_MINI);
			byte[] decodedImage = Base64.decode(stats.getString("mini"), Base64.DEFAULT);
			token.setMiniPortrait(BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length));
		}
		token.setDbId(stats.getInt("id"));
		token.setTokenName(stats.getString("name"));
		//save the token
		MonsterTokens.addMonsterToken(getApplicationContext(), token);
		return new SyncPayload(token.getUuid().toString(), token.getDbId());
	}
	
	private SyncPayload updateMonsterToken(MonsterToken token, JSONObject stats) throws JSONException
	{
		if(stats.getString("token_type").equals(MonsterToken.WEB_TOKEN_NUMBER))
		{
			token.setTokenType(MonsterToken.TOKEN_TYPE_NUMBER);
			token.setTokenNumber(stats.getInt("token_number"));
		}
		else if(stats.getString("token_type").equals(MonsterToken.WEB_TOKEN_COLOR))
		{
			token.setTokenType(MonsterToken.TOKEN_TYPE_COLOR);
			token.setTokenColor(Color.parseColor(stats.getString("token_color")));
		}
		else if(stats.getString("token_type").equals(MonsterToken.WEB_TOKEN_COLORED_NUMBER))
		{
			token.setTokenType(MonsterToken.TOKEN_TYPE_COLORED_NUMBER);
			token.setTokenNumber(stats.getInt("token_number"));
			token.setTokenColor(Color.parseColor(stats.getString("token_color")));
		}
		else
		{
			token.setTokenType(MonsterToken.TOKEN_TYPE_MINI);
			byte[] decodedImage = Base64.decode(stats.getString("mini"), Base64.DEFAULT);
			token.setMiniPortrait(BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length));
		}
		token.setDbId(stats.getInt("id"));
		token.setTokenName(stats.getString("name"));
		//save the token
		MonsterTokens.updateMonsterToken(getApplicationContext(), token);
		return new SyncPayload(token.getUuid().toString(), token.getDbId());
	}

	private ArrayList<SyncPayload> syncMonsterTokens(JSONArray tokens) throws JSONException
	{
		ArrayList<SyncPayload> ids = new ArrayList<>();
		for(int i = 0; i < tokens.length(); i++)
		{
			JSONObject token_json = tokens.getJSONObject(i);
			//try to load the monster.
			MonsterToken myToken = MonsterTokens.getMonsterByUuid(getApplicationContext(), token_json.getString("uuid"));
			if(myToken == null)
			{
				//in this case, we must add it.
				ids.add(addMonsterToken(token_json));
			}
			else
			{
				//in this case, we update it.
				ids.add(updateMonsterToken(myToken, token_json));
			}
		}
		return ids;
	}
	
	private SyncPayload addEncounter(JSONObject stats) throws JSONException
	{
		Encounter encounter = new Encounter();
		encounter.setEncounterName(stats.getString("name"));
		encounter.setDbId(stats.getInt("id"));
		
		ArrayList<Monster> encounterMonsters = new ArrayList<>();
		//sr monsters first
		List<Monster> srMonsters = Monsters.monsterList(getApplicationContext());
		JSONArray sr_monsters_json = stats.getJSONArray("sr_monsters");
		for(int i = 0; i < sr_monsters_json.length(); i++)
		{
			JSONObject monster_json = sr_monsters_json.getJSONObject(i);
			if(monster_json.getInt("idx") >= 0)
				encounterMonsters.add(srMonsters.get(monster_json.getInt("idx")));
		}
		
		JSONArray custom_monsters_json = stats.getJSONArray("custom_monsters");
		for(int i = 0; i < custom_monsters_json.length(); i++)
		{
			JSONObject monster_json = custom_monsters_json.getJSONObject(i);
			CustomMonster cMonster = CustomMonsters.getMonsterByDbId(getApplicationContext(), monster_json.getInt("id"));
			if(cMonster != null)
				encounterMonsters.add(cMonster);
		}
		
		encounter.setMonsters(encounterMonsters);
		//save the encounter
		Encounters.addEncounter(getApplicationContext(), encounter);
		return new SyncPayload(encounter.getUuid().toString(), encounter.getDbId());
	}
	
	private SyncPayload updateEncounter(Encounter encounter, JSONObject stats) throws JSONException
	{
		encounter.setEncounterName(stats.getString("name"));
		encounter.setDbId(stats.getInt("id"));
		ArrayList<Monster> encounterMonsters = new ArrayList<>();
		//sr monsters first
		List<Monster> srMonsters = Monsters.monsterList(getApplicationContext());
		JSONArray sr_monsters_json = stats.getJSONArray("sr_monsters");
		for(int i = 0; i < sr_monsters_json.length(); i++)
		{
			JSONObject monster_json = sr_monsters_json.getJSONObject(i);
			if(monster_json.getInt("idx") >= 0)
				encounterMonsters.add(srMonsters.get(monster_json.getInt("idx")));
		}
		
		JSONArray custom_monsters_json = stats.getJSONArray("custom_monsters");
		for(int i = 0; i < custom_monsters_json.length(); i++)
		{
			JSONObject monster_json = custom_monsters_json.getJSONObject(i);
			CustomMonster cMonster = CustomMonsters.getMonsterByDbId(getApplicationContext(), monster_json.getInt("id"));
			if(cMonster != null)
				encounterMonsters.add(cMonster);
		}
		
		encounter.setMonsters(encounterMonsters);
		//save the encounter
		Encounters.updateEncounter(getApplicationContext(), encounter);
		return new SyncPayload(encounter.getUuid().toString(), encounter.getDbId());
	}
	
	private ArrayList<SyncPayload> syncEncounters(JSONArray encounters) throws JSONException
	{
		ArrayList<SyncPayload> ids = new ArrayList<>();
		for(int i = 0; i < encounters.length(); i++)
		{
			JSONObject encounter_json = encounters.getJSONObject(i);
			//try to load the monster.
			Encounter myEncounter = Encounters.getEncounterByUuid(getApplicationContext(), encounter_json.getString("uuid"));
			if(myEncounter == null)
			{
				//in this case, we must add it.
				ids.add(addEncounter(encounter_json));
			}
			else
			{
				//in this case, we update it.
				ids.add(updateEncounter(myEncounter, encounter_json));
			}
		}
		return ids;
	}
	
	private SyncPlayerPayload addPlayer(JSONObject stats) throws JSONException
	{
		Player player = new Player(stats.getString("name"));
		player.setDbId(stats.getInt("id"));
		player.setDci(stats.getString("dci"));
		
		if(!stats.getString("portrait").equals("null") && !stats.getString("portrait").equals(""))
			player.setPortrait(Base64.decode(stats.getString("portrait"), Base64.DEFAULT));
		
		ArrayList<Character> playerPcs = new ArrayList<>();
		ArrayList<SyncPayload> pcPayload = new ArrayList<>();
		JSONArray pcs_json = stats.getJSONArray("pcs");
		for(int i = 0; i < pcs_json.length(); i++)
		{
			JSONObject pc_json = pcs_json.getJSONObject(i);
			Character pc = new Character();
			pc.setDbId(pc_json.getInt("id"));
			pc.setName(pc_json.getString("name"));
			pc.setCharacterClass(pc_json.getString("characterClass"));
			pc.setCharacterRace(pc_json.getString("characterRace"));
			pc.setAc(pc_json.getInt("ac"));
			pc.setHp(pc_json.getInt("hp"));
			pc.setPp(pc_json.getInt("pp"));
			pc.setLevel(pc_json.getInt("level"));
			pc.setSpellDc(pc_json.getInt("spellDc"));
			playerPcs.add(pc);
			pcPayload.add(new SyncPayload(pc.getUuid().toString(), pc.getDbId()));
		}
		player.setPcs(playerPcs);
		//save the player
		Players.addPlayer(getApplicationContext(), player);
		SyncPlayerPayload
				payload = new SyncPlayerPayload(player.getUuid().toString(), player.getDbId());
		payload.pcs = pcPayload;
		return payload;
	}
	
	private SyncPlayerPayload updatePlayer(Player player, JSONObject stats) throws JSONException
	{
		player.setName(stats.getString("name"));
		player.setDbId(stats.getInt("id"));
		player.setDci(stats.getString("dci"));
		
		if(!stats.getString("portrait").equals("null") && !stats.getString("portrait").equals(""))
			player.setPortrait(Base64.decode(stats.getString("portrait"), Base64.DEFAULT));
		
		ArrayList<Character> playerPcs = new ArrayList<>();
		ArrayList<SyncPayload> pcPayload = new ArrayList<>();
		JSONArray pcs_json = stats.getJSONArray("pcs");
		for(int i = 0; i < pcs_json.length(); i++)
		{
			JSONObject pc_json = pcs_json.getJSONObject(i);
			Character pc = null;
			boolean pcFound = false;
			for(int j = 0; j < player.getPcs().size(); j++)
			{
				pc = player.getPcs().get(j);
				if(pc.getDbId() == pc_json.getInt("id"))
				{
					pcFound = true;
					break;
				}
				pc = null;
			}
			if(pc == null)
				pc = new Character();
			pc.setDbId(pc_json.getInt("id"));
			pc.setName(pc_json.getString("name"));
			pc.setCharacterClass(pc_json.getString("characterClass"));
			pc.setCharacterRace(pc_json.getString("characterRace"));
			pc.setAc(pc_json.getInt("ac"));
			pc.setHp(pc_json.getInt("hp"));
			pc.setPp(pc_json.getInt("pp"));
			pc.setLevel(pc_json.getInt("level"));
			pc.setSpellDc(pc_json.getInt("spellDc"));
			if(!pcFound)
				playerPcs.add(pc);
			pcPayload.add(new SyncPayload(pc.getUuid().toString(), pc.getDbId()));
		}
		player.setPcs(playerPcs);
		//save the player
		Players.updatePlayer(getApplicationContext(), player);
		SyncPlayerPayload
				payload = new SyncPlayerPayload(player.getUuid().toString(), player.getDbId());
		payload.pcs = pcPayload;
		return payload;
	}
	
	private ArrayList<SyncPlayerPayload> syncPlayers(JSONArray players) throws JSONException
	{
		ArrayList<SyncPlayerPayload> ids = new ArrayList<>();
		for(int i = 0; i < players.length(); i++)
		{
			JSONObject player_json = players.getJSONObject(i);
			//try to load the monster.
			Player myPlayer = Players.getPlayerByUuid(getApplicationContext(), player_json.getString("uuid"));
			if(myPlayer == null)
			{
				//in this case, we must add it.
				ids.add(addPlayer(player_json));
			}
			else
			{
				//in this case, we update it.
				ids.add(updatePlayer(myPlayer, player_json));
			}
		}
		return ids;
	}
	
	private SyncPayload addModule(JSONObject stats) throws JSONException
	{
		Module module = new Module(stats.getString("name"));
		module.setDbId(stats.getInt("id"));
		module.setModuleDescription(stats.getString("description"));
		module.setOptimizedLevel(stats.getInt("optimized_level"));
		module.setTier(stats.getInt("tier"));
		
		//encounters
		ArrayList<Encounter> moduleEncounters = new ArrayList<>();
		JSONArray encounters_json = stats.getJSONArray("encounters");
		for(int i = 0; i < encounters_json.length(); i++)
		{
			JSONObject encounter_json = encounters_json.getJSONObject(i);
			Encounter encounter = Encounters.getMonsterByDbId(getApplicationContext(), encounter_json.getInt("id"));
			if(encounter != null)
				moduleEncounters.add(encounter);
		}
		module.setEncounters(moduleEncounters);
		//save the module
		Modules.addModule(getApplicationContext(), module);
		return new SyncPayload(module.getUuid().toString(), module.getDbId());
	}
	
	private SyncPayload updateModule(Module module, JSONObject stats) throws JSONException
	{
		module.setModuleName(stats.getString("name"));
		module.setDbId(stats.getInt("id"));
		module.setModuleDescription(stats.getString("description"));
		module.setOptimizedLevel(stats.getInt("optimized_level"));
		module.setTier(stats.getInt("tier"));
		
		//encounters
		ArrayList<Encounter> moduleEncounters = new ArrayList<>();
		JSONArray encounters_json = stats.getJSONArray("encounters");
		for(int i = 0; i < encounters_json.length(); i++)
		{
			JSONObject encounter_json = encounters_json.getJSONObject(i);
			Encounter encounter = Encounters.getMonsterByDbId(getApplicationContext(), encounter_json.getInt("id"));
			if(encounter != null)
				moduleEncounters.add(encounter);
		}
		module.setEncounters(moduleEncounters);
		//save the module
		Modules.updateModule(getApplicationContext(), module);
		return new SyncPayload(module.getUuid().toString(), module.getDbId());
	}
	
	private ArrayList<SyncPayload> syncModules(JSONArray modules) throws JSONException
	{
		ArrayList<SyncPayload> ids = new ArrayList<>();
		for(int i = 0; i < modules.length(); i++)
		{
			JSONObject module_json = modules.getJSONObject(i);
			//try to load the monster.
			Module myModule = Modules.getModuleByUuid(getApplicationContext(), module_json.getString("uuid"));
			if(myModule == null)
			{
				//in this case, we must add it.
				ids.add(addModule(module_json));
			}
			else
			{
				//in this case, we update it.
				ids.add(updateModule(myModule, module_json));
			}
		}
		return ids;
	}


	private void loadSyncData(Bundle data)
	{
		try
		{
			JSONObject json = new JSONObject(data.getString(BUNDLE_MSG));
			LinkData linkData = new LinkData();

			resultsTv.setText("Reading Player Data...");
			JSONArray players_json = json.getJSONArray("players");
			linkData.players = syncPlayers(players_json);

			resultsTv.setText("Reading Custom Monster Data...");
			JSONArray monsters_json = json.getJSONArray("custom_monsters");
			linkData.custom_monsters = syncCustomMonsters(monsters_json);

			resultsTv.setText("Reading Encounter Data...");
			JSONArray encounter_json = json.getJSONArray("encounters");
			linkData.encounters = syncEncounters(encounter_json);

			resultsTv.setText("Reading Monster Token Data...");
			JSONArray token_json = json.getJSONArray("monster_tokens");
			linkData.monster_tokens = syncMonsterTokens(token_json);

			resultsTv.setText("Reading Module Data...");
			JSONArray module_json = json.getJSONArray("modules");
			linkData.modules = syncModules(module_json);

			uploadLinks(linkData);
			
		}
		catch (JSONException e)
		{
			resultsTv.setVisibility(View.INVISIBLE);
			pdBar.setVisibility(View.INVISIBLE);
			Toast toast = Toast.makeText(getApplicationContext(), "Error syncing data", Toast.LENGTH_LONG);
			toast.show();
		}
	}

	private void refreshScreen()
	{
		String aToken = prefs.getString(PREF_ACCESS_TOKEN_KEY, null);
		if(aToken == null)
		{
			loginContainer.setVisibility(View.VISIBLE);
			loggedInLy.setVisibility(View.GONE);
		}
		else
		{
			loginContainer.setVisibility(View.GONE);
			loggedInLy.setVisibility(View.VISIBLE);
		}
	}

	private void readToken(String jsonString)
	{
		JSONObject json;
		try
		{
			json = new JSONObject(jsonString);
			String oAuthAccessToken = json.getString("access_token");
			prefs.edit().putString(PREF_ACCESS_TOKEN_KEY, oAuthAccessToken).apply();

			String oAuthRefreshToken = json.getString("refresh_token");
			prefs.edit().putString(PREF_REFRESH_TOKEN_KEY, oAuthRefreshToken).apply();

			int oAuthExpiresIn = Integer.parseInt(json.getString("expires_in"));
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, oAuthExpiresIn);
			prefs.edit().putLong(PREF_TOKEN_EXPIRATION_KEY, cal.getTime().getTime()).apply();

			Toast toast = Toast.makeText(getApplicationContext(), "Successfully logged in!", Toast.LENGTH_LONG);
			toast.show();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			prefs.edit().putString(PREF_ACCESS_TOKEN_KEY, null).apply();
			prefs.edit().putString(PREF_REFRESH_TOKEN_KEY, null).apply();
			prefs.edit().putString(PREF_TOKEN_EXPIRATION_KEY, null).apply();
			Toast toast = Toast.makeText(getApplicationContext(), "Error authenticating", Toast.LENGTH_LONG);
			toast.show();
		}
		pdBar.setVisibility(View.INVISIBLE);
		resultsTv.setVisibility(View.INVISIBLE);
	}

	private void readClientSecret(String jsonString)
	{
		JSONObject json;
		try
		{
			json = new JSONObject(jsonString);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			prefs.edit().putString(PREF_CLIENT_SECRET_KEY, null).apply();
			return;
		}
		//access token
		try
		{
			String oAuthClientSecret = json.getString("data");
			prefs.edit().putString(PREF_CLIENT_SECRET_KEY, oAuthClientSecret).apply();
		}
		catch (JSONException e)
		{
			prefs.edit().putString(PREF_CLIENT_SECRET_KEY, null).apply();
		}
	}

	public void logout(View v)
	{
		prefs.edit().putString(PREF_ACCESS_TOKEN_KEY, null).apply();
		prefs.edit().putString(PREF_REFRESH_TOKEN_KEY, null).apply();
		prefs.edit().putString(PREF_TOKEN_EXPIRATION_KEY, null).apply();
		refreshScreen();
	}

	private void refreshToken()
	{
		Runnable aRunnable = new Runnable()
		{
			private void sendMessage(int action, String message)
			{
				Message msg = mHandler.obtainMessage();
				Bundle rBundle = new Bundle();
				rBundle.putInt(BUNDLE_ACTION, action);
				rBundle.putString(BUNDLE_MSG, message);
				msg.setData(rBundle);
				mHandler.sendMessage(msg);
			}

			private String readString(InputStream in) throws IOException
			{
				BufferedReader is =
						new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = is.readLine()) != null)
				{
					sb.append(line);
				}
				return sb.toString();
			}
			@Override
			public void run()
			{
				URL url;
				HttpURLConnection conn;
				String rsp;
				int status;
				try
				{
					//bring up the bar
					pdBar.setVisibility(View.VISIBLE);
					resultsTv.setVisibility(View.VISIBLE);
					//show start
					sendMessage(ACTION_UPDATE_MSG, "Reconnecting...");

					url = new URL(webUrl + "/oauth/token");
					conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setDoOutput(true);
					conn.setDoInput(true);

					JSONObject jsonParam = new JSONObject();
					jsonParam.put("grant_type", oAuthGrantRefreh);
					jsonParam.put("client_id", oAuthClientId);
					jsonParam.put("client_secret", prefs.getString(PREF_CLIENT_SECRET_KEY, ""));
					jsonParam.put("refresh_token", prefs.getString(PREF_REFRESH_TOKEN_KEY, ""));
					jsonParam.put("scope", "");

					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					os.writeBytes(jsonParam.toString());
					os.flush();
					os.close();
					status = conn.getResponseCode();

					if(status != HttpURLConnection.HTTP_OK)
						throw new SyncException(conn.getResponseMessage());

					rsp = readString(conn.getInputStream());
					sendMessage(ACTION_LOAD_TOKEN, rsp);

					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					sendMessage(ACTION_ERROR, "Malformed URL: " + e.getMessage());
				}
				catch (IOException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
				catch (JSONException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
				catch(SyncException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();

	}

	public void attemptLogin(View v)
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

		prefs.edit().putString(PREF_EMAIL_KEY, emailEt.getText().toString()).apply();

		Runnable aRunnable = new Runnable()
		{
			private void sendMessage(int action, String message)
			{
				Message msg = mHandler.obtainMessage();
				Bundle rBundle = new Bundle();
				rBundle.putInt(BUNDLE_ACTION, action);
				rBundle.putString(BUNDLE_MSG, message);
				msg.setData(rBundle);
				mHandler.sendMessage(msg);
			}

			private String readString(InputStream in) throws IOException
			{
				BufferedReader is =
						new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = is.readLine()) != null)
				{
					sb.append(line);
				}
				return sb.toString();
			}

			@Override
			public void run()
			{
				URL url;
				HttpURLConnection conn;
				String rsp;
				int status;
				try
				{
					//bring up the bar
					sendMessage(ACTION_SHOW_PROGRESS, "");
					//show start
					sendMessage(ACTION_UPDATE_MSG, "Contacting Client...");

					//get the client secret
					url = new URL(webUrl + "/api/sync/client-secret/" + oAuthClientId);
					conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setDoInput(true);

					status = conn.getResponseCode();

					if(status != HttpURLConnection.HTTP_OK)
						throw new SyncException(conn.getResponseMessage());

					rsp = readString(conn.getInputStream());
					Log.d("SyncToWeb", "rsp is " + rsp);

					//read client secret
					sendMessage(ACTION_READ_CLIENT_SECRET, rsp);
					//and keep a copy for us
					JSONObject json = new JSONObject(rsp);
					String oAuthClientSecret = json.getString("client_secret");

					//next, authenticate
					sendMessage(ACTION_UPDATE_MSG, "Authenticating...");
					url = new URL(webUrl + "/oauth/token");
					conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setDoOutput(true);
					conn.setDoInput(true);

					JSONObject jsonParam = new JSONObject();
					jsonParam.put("grant_type", oAuthGrantPassword);
					jsonParam.put("client_id", oAuthClientId);
					jsonParam.put("client_secret", oAuthClientSecret);
					jsonParam.put("username", emailEt.getText().toString());
					jsonParam.put("password", passwordEt.getText().toString());
					jsonParam.put("scope", "");

					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					os.writeBytes(jsonParam.toString());
					os.flush();
					os.close();
					status = conn.getResponseCode();

					if(status != HttpURLConnection.HTTP_OK)
						throw new SyncException(conn.getResponseMessage());

					rsp = readString(conn.getInputStream());
					//now read the token
					sendMessage(ACTION_LOAD_TOKEN, rsp);

					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					sendMessage(ACTION_ERROR, "Malformed URL: " + e.getMessage());
				}
				catch (IOException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
				catch (JSONException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
				catch(SyncException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();
	}

	public void uploadData(View v)
	{
		final SyncData syncData = new SyncData(getApplicationContext());
		Gson gson = new Gson();
		final String sData = gson.toJson(syncData);

		Runnable aRunnable = new Runnable()
		{
			private void sendMessage(int action, String message)
			{
				Message msg = mHandler.obtainMessage();
				Bundle rBundle = new Bundle();
				rBundle.putInt(BUNDLE_ACTION, action);
				rBundle.putString(BUNDLE_MSG, message);
				msg.setData(rBundle);
				mHandler.sendMessage(msg);
			}

			private String readString(InputStream in) throws IOException
			{
				BufferedReader is =
						new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = is.readLine()) != null)
				{
					sb.append(line);
				}
				return sb.toString();
			}

			@Override
			public void run()
			{
				URL url;
				HttpURLConnection conn;
				String rsp;
				int status;
				try
				{
					sendMessage(ACTION_UPDATE_MSG, "Sending data to web...");
					url = new URL(webUrl + "/api/sync/upload");
					conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setRequestProperty("authorization", "Bearer " + prefs.getString(PREF_ACCESS_TOKEN_KEY, ""));
					conn.setDoOutput(true);
					conn.setDoInput(true);
					
					BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
					
					writer.write(sData);
					writer.flush();
					writer.close();
					os.close();
					conn.connect();
					status = conn.getResponseCode();

					if(status == HttpURLConnection.HTTP_OK)
						throw new SyncException(conn.getResponseMessage());

					rsp = readString(conn.getInputStream());
					sendMessage(ACTION_UPDATE_LINKS, rsp);
					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					sendMessage(ACTION_ERROR, "Malformed URL: " + e.getMessage());
				}
				catch (IOException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
				catch(SyncException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();
	}

	public void downloadData(View v)
	{

		Runnable aRunnable = new Runnable()
		{
			private void sendMessage(int action, String message)
			{
				Message msg = mHandler.obtainMessage();
				Bundle rBundle = new Bundle();
				rBundle.putInt(BUNDLE_ACTION, action);
				rBundle.putString(BUNDLE_MSG, message);
				msg.setData(rBundle);
				mHandler.sendMessage(msg);
			}

			private String readString(InputStream in) throws IOException
			{
				BufferedReader is =
						new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = is.readLine()) != null)
				{
					sb.append(line);
				}
				return sb.toString();
			}

			@Override
			public void run()
			{
				URL url;
				HttpURLConnection conn;
				String rsp;
				int status;
				try
				{
					//bring up the bar
					sendMessage(ACTION_SHOW_PROGRESS, "");
					//show start
					sendMessage(ACTION_UPDATE_MSG, "Contacting Web...");

					url = new URL(webUrl + "/api/sync/download");
					conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setRequestProperty("authorization", "Bearer " + prefs.getString(PREF_ACCESS_TOKEN_KEY, ""));
					conn.setDoInput(true);

					status = conn.getResponseCode();

					if(status != HttpURLConnection.HTTP_OK)
						throw new SyncException(conn.getResponseMessage());

					rsp = readString(conn.getInputStream());
					sendMessage(ACTION_UPDATE_MSG, "Syncing Data...");
					sendMessage(ACTION_LOAD_SYNC_DATA, rsp);
					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					sendMessage(ACTION_ERROR, "Malformed URL: " + e.getMessage());
				}
				catch (IOException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
				catch(SyncException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();

	}
	
	public void uploadLinks(LinkData data)
	{
		Gson gson = new Gson();
		final String sData = gson.toJson(data);
		
		Runnable aRunnable = new Runnable()
		{
			private void sendMessage(int action, String message)
			{
				Message msg = mHandler.obtainMessage();
				Bundle rBundle = new Bundle();
				rBundle.putInt(BUNDLE_ACTION, action);
				rBundle.putString(BUNDLE_MSG, message);
				msg.setData(rBundle);
				mHandler.sendMessage(msg);
			}

			private String readString(InputStream in) throws IOException
			{
				BufferedReader is =
						new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = is.readLine()) != null)
				{
					sb.append(line);
				}
				return sb.toString();
			}

			@Override
			public void run()
			{
				URL url;
				HttpURLConnection conn;
				String rsp;
				int status;
				try
				{
					sendMessage(ACTION_UPDATE_MSG, "Sending reply...");
					url = new URL(webUrl + "/api/sync/db");
					conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setRequestProperty("authorization", "Bearer " + prefs.getString(PREF_ACCESS_TOKEN_KEY, ""));
					conn.setDoOutput(true);
					conn.setDoInput(true);
					
					BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
					
					writer.write(sData);
					writer.flush();
					writer.close();
					os.close();
					
					conn.connect();
					status = conn.getResponseCode();
					
					if(status != HttpURLConnection.HTTP_OK)
						throw new SyncException(conn.getResponseMessage());
					sendMessage(ACTION_FINISH_UPDATE_LINKS, "Complete!");

					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					sendMessage(ACTION_ERROR, "Malformed URL: " + e.getMessage());
				}
				catch (IOException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
				catch(SyncException e)
				{
					sendMessage(ACTION_ERROR, e.getMessage());
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();
	}
}
