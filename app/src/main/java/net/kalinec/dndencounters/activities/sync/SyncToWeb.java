package net.kalinec.dndencounters.activities.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.modules.Module;
import net.kalinec.dndencounters.modules.Modules;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.Players;
import net.kalinec.dndencounters.sync.SyncData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyncToWeb extends AppCompatActivity
{
	private final static String oAuthClientSecret = "UAa3x2MsvC7oEJPUz8TzuUlTLYFguuL3leC1UVmK";
	private final static int oAuthClientId = 2;
	private final static String oAuthGrantPassword = "password";
	private final static String oAuthGrantRefreh = "refresh_token";
	private final static String webUrl = "http://192.168.10.10";
	private EditText emailEt, passwordEt;
	private TextView resultsTv;
	private LinearLayout loginContainer, loggedInLy;
	private RelativeLayout loadingPanel;

	public Handler mHandler;

	private SharedPreferences prefs;
	private final static String PREF_KEY = "net.kalinec.dnd";
	private final static String PREF_EMAIL_KEY = "net.kalinec.dnd.email";
	private final static String PREF_ACCESS_TOKEN_KEY = "net.kalinec.dnd.access_token";
	private final static String PREF_REFRESH_TOKEN_KEY = "net.kalinec.dnd.refresh_token";
	private final static String PREF_TOKEN_EXPIRATION_KEY = "net.kalinec.dnd.expires_in";

	private final static String BUNDLE_ACTION = "action";
	private final static String BUNDLE_MSG = "message";
	private final static int ACTION_UPDATE_MSG = 1;
	private final static int ACTION_LOAD_TOKEN = 2;
	private final static int ACTION_UPDATE_LINKS = 3;
	private final static int ACTION_LOAD_SYNC_DATA = 4;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_to_web);
		prefs = getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
		emailEt = findViewById(R.id.emailEt);
		emailEt.setText(prefs.getString(PREF_EMAIL_KEY, ""));
		passwordEt = findViewById(R.id.passwordEt);
		resultsTv = findViewById(R.id.resultsTv);
		loginContainer = findViewById(R.id.loginContainer);
		loggedInLy = findViewById(R.id.loggedInLy);
		loadingPanel = findViewById(R.id.loadingPanel);

		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Bundle bundle = msg.getData();
				int action = bundle.getInt(BUNDLE_ACTION);
				if(action == ACTION_UPDATE_MSG)
					Log.d("SyncToWeb", "from server: " + bundle.getString(BUNDLE_MSG));
				else if(action == ACTION_LOAD_TOKEN)
				{
					readToken(bundle.getString(BUNDLE_MSG));
					refreshScreen();
				}
				else if(action == ACTION_UPDATE_LINKS)
				{
					Log.d("SyncToWeb", "from server: " + bundle.getString(BUNDLE_MSG));
					updateLinks(bundle);
					refreshScreen();
				}
				else if(action == ACTION_LOAD_SYNC_DATA)
				{
					Log.d("SyncToWeb", "from server: " + bundle.getString(BUNDLE_MSG));
					loadSyncData(bundle);
					refreshScreen();
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
			resultsTv.setText("Sync to Web Successful!");
		}
		catch (JSONException e)
		{
			resultsTv.setText("error decrypting json: " + e.toString());
		}
	}


	private void loadSyncData(Bundle data)
	{
		try
		{
			JSONObject json = new JSONObject(data.getString(BUNDLE_MSG));
			JSONArray players_json = json.getJSONArray("players");
			for(int i = 0; i < players_json.length(); i++)
			{
				JSONObject player_json = players_json.getJSONObject(i);
				//try to load the player.
				Player myPlayer = Players.getPlayerByUuid(getApplicationContext(), player_json.getString("uuid"));
				if(myPlayer == null)
				{
					//in this case, we must add it.

				}
				else
				{
					//in this case, we update it.
				}
			}


			JSONArray monsters_json = json.getJSONArray("custom_monsters");
			for(int i = 0; i < monsters_json.length(); i++)
			{
				JSONObject monster_json = monsters_json.getJSONObject(i);
				//try to load the monster.
				CustomMonster myMonster = CustomMonsters.getMonsterByUuid(getApplicationContext(), monster_json.getString("uuid"));
				if(myMonster == null)
				{
					//in this case, we must add it.

				}
				else
				{
					//in this case, we update it.
				}
			}


			JSONArray encounter_json = json.getJSONArray("encounters");
			JSONArray token_json = json.getJSONArray("monster_tokens");
			JSONArray module_json = json.getJSONArray("modules");
		}
		catch (JSONException e)
		{
			resultsTv.setText("error decrypting json: " + e.toString());
		}
	}

	private void refreshScreen()
	{
		String aToken = prefs.getString(PREF_ACCESS_TOKEN_KEY, null);
		loadingPanel.setVisibility(View.GONE);
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
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			prefs.edit().putString(PREF_ACCESS_TOKEN_KEY, null).apply();
			prefs.edit().putString(PREF_REFRESH_TOKEN_KEY, null).apply();
			prefs.edit().putString(PREF_TOKEN_EXPIRATION_KEY, null).apply();
			return;
		}
		//access token
		try
		{
			String oAuthAccessToken = json.getString("access_token");
			prefs.edit().putString(PREF_ACCESS_TOKEN_KEY, oAuthAccessToken).apply();
		}
		catch (JSONException e)
		{
			prefs.edit().putString(PREF_ACCESS_TOKEN_KEY, null).apply();
		}
		//refhresh token
		try
		{
			String oAuthRefreshToken = json.getString("refresh_token");
			prefs.edit().putString(PREF_REFRESH_TOKEN_KEY, oAuthRefreshToken).apply();
		}
		catch (JSONException e)
		{
			prefs.edit().putString(PREF_REFRESH_TOKEN_KEY, null).apply();
		}
		//expiration
		try
		{
			int oAuthExpiresIn = Integer.parseInt(json.getString("expires_in"));
			Log.d("SyncToWeb", "oAuthExpiresIn=" + Integer.toString(oAuthExpiresIn));
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, oAuthExpiresIn);
			Log.d("SyncToWeb", "saving val=" + Long.toString(cal.getTime().getTime()));
			prefs.edit().putLong(PREF_TOKEN_EXPIRATION_KEY, cal.getTime().getTime()).apply();
		}
		catch (JSONException e)
		{
			prefs.edit().putString(PREF_TOKEN_EXPIRATION_KEY, null).apply();
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
		loadingPanel.setVisibility(View.VISIBLE);
		resultsTv.setText("Start refreshToken\n");

		Runnable aRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				Message msg;
				Bundle rBundle;
				try
				{
					URL url = new URL(webUrl + "/oauth/token");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setDoOutput(true);
					conn.setDoInput(true);

					JSONObject jsonParam = new JSONObject();
					jsonParam.put("grant_type", oAuthGrantRefreh);
					jsonParam.put("client_id", oAuthClientId);
					jsonParam.put("client_secret", oAuthClientSecret);
					jsonParam.put("refresh_token", prefs.getString(PREF_REFRESH_TOKEN_KEY, ""));
					jsonParam.put("scope", "");

					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					os.writeBytes(jsonParam.toString());
					os.flush();
					os.close();
					int status = conn.getResponseCode();

					if(status == HttpURLConnection.HTTP_OK)
					{
						BufferedReader is =
								new BufferedReader(new InputStreamReader(conn.getInputStream()));
						StringBuilder sb = new StringBuilder();

						String line = null;
						while ((line = is.readLine()) != null)
						{
							sb.append(line);
						}
						String rsp = sb.toString();
						JSONObject json = new JSONObject(rsp);
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "JSON Response: " + rsp);
						msg = mHandler.obtainMessage();
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
						//now read the token
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_LOAD_TOKEN);
						rBundle.putString(BUNDLE_MSG, rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					else
					{
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "Request returned error: " +
						                            conn.getResponseMessage());
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error with URL: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
				catch (IOException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error opening the connection: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
				catch (JSONException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error adding to json: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();

	}

	public void attemptLogin(View v)
	{
		loadingPanel.setVisibility(View.VISIBLE);
		prefs.edit().putString(PREF_EMAIL_KEY, emailEt.getText().toString()).apply();
		resultsTv.setText("Start attemptLogin\n");

		Runnable aRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				Message msg;
				Bundle rBundle;
				try
				{
					URL url = new URL(webUrl + "/oauth/token");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
					int status = conn.getResponseCode();

					if(status == HttpURLConnection.HTTP_OK)
					{
						BufferedReader is =
								new BufferedReader(new InputStreamReader(conn.getInputStream()));
						StringBuilder sb = new StringBuilder();

						String line = null;
						while ((line = is.readLine()) != null)
						{
							sb.append(line);
						}
						String rsp = sb.toString();
						JSONObject json = new JSONObject(rsp);
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "JSON Response: " + rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
						//now read the token
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_LOAD_TOKEN);
						rBundle.putString(BUNDLE_MSG, rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					else
					{
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "Request returned error: " +
						                            conn.getResponseMessage());
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error with URL: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
				catch (IOException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error opening the connection: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
				catch (JSONException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error adding to json: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();
	}

	public void uploadData(View v)
	{
		loadingPanel.setVisibility(View.VISIBLE);
		resultsTv.setText("Start uploadData\n");
		final SyncData syncData = new SyncData(getApplicationContext());

		Runnable aRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				Message msg;
				Bundle rBundle;
				try
				{
					URL url = new URL(webUrl + "/api/sync/upload");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setRequestProperty("authorization", "Bearer " + prefs.getString(PREF_ACCESS_TOKEN_KEY, ""));
					conn.setDoOutput(true);
					conn.setDoInput(true);

					Gson gson = new Gson();


					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					os.writeBytes(gson.toJson(syncData));
					os.flush();
					os.close();
					int status = conn.getResponseCode();

					if(status == HttpURLConnection.HTTP_OK)
					{
						BufferedReader is =
								new BufferedReader(new InputStreamReader(conn.getInputStream()));
						StringBuilder sb = new StringBuilder();

						String line = null;
						while ((line = is.readLine()) != null)
						{
							sb.append(line);
						}
						String rsp = sb.toString();
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "JSON Response: " + rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
						//now read the token
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_LINKS);
						rBundle.putString(BUNDLE_MSG, rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					else
					{
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "Request returned error: " +
						                              conn.getResponseMessage());
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error with URL: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
				catch (IOException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error opening the connection: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();
	}

	public void downloadData(View v)
	{
		loadingPanel.setVisibility(View.VISIBLE);
		resultsTv.setText("Start downloadData\n");

		Runnable aRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				Message msg;
				Bundle rBundle;
				try
				{
					URL url = new URL(webUrl + "/api/sync/download");
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
					conn.setRequestProperty("Accept","application/json");
					conn.setRequestProperty("authorization", "Bearer " + prefs.getString(PREF_ACCESS_TOKEN_KEY, ""));
					conn.setDoInput(true);

					int status = conn.getResponseCode();

					if(status == HttpURLConnection.HTTP_OK)
					{
						BufferedReader is =
								new BufferedReader(new InputStreamReader(conn.getInputStream()));
						StringBuilder sb = new StringBuilder();

						String line = null;
						while ((line = is.readLine()) != null)
						{
							sb.append(line);
						}
						String rsp = sb.toString();
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "JSON Response: " + rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
						//now read the sync data
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_LOAD_SYNC_DATA);
						rBundle.putString(BUNDLE_MSG, rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					else
					{
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "Request returned error: " +
						                              conn.getResponseMessage());
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
					}
					conn.disconnect();
				}
				catch (MalformedURLException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error with URL: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
				catch (IOException e)
				{
					msg = mHandler.obtainMessage();
					rBundle = new Bundle();
					rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
					rBundle.putString(BUNDLE_MSG, "Error opening the connection: " + e.toString());
					msg.setData(rBundle);
					mHandler.sendMessage(msg);
				}
			}
		};
		Thread thread = new Thread(aRunnable);
		thread.start();

	}
}
