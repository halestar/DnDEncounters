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
import net.kalinec.dndencounters.sync.SyncData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class SyncToWeb extends AppCompatActivity
{
	private final static String oAuthClientSecret = "7BKUT1OZ1KXELV3MQe5fZWr2i7wjQ2UogSEni2Mp";
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
					resultsTv.append(bundle.getString(BUNDLE_MSG));
				else if(action == ACTION_LOAD_TOKEN)
				{
					readToken(bundle.getString(BUNDLE_MSG));
					refreshScreen();
				}
			}
		};


		refreshScreen();

		Calendar cal = Calendar.getInstance();
		if(cal.getTime().getTime() >= prefs.getLong(PREF_TOKEN_EXPIRATION_KEY, -1))
			refreshToken();


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
			resultsTv.setText("have access token, expires in: " + Long.toString(prefs.getLong(PREF_TOKEN_EXPIRATION_KEY, -1)) + "time now: " + Calendar.getInstance().getTime().getTime());
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
						JSONObject json = new JSONObject(rsp);
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "JSON Response: " + rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
						//now read the token
						/*msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_LOAD_TOKEN);
						rBundle.putString(BUNDLE_MSG, rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);*/
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
						JSONObject json = new JSONObject(rsp);
						msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_UPDATE_MSG);
						rBundle.putString(BUNDLE_MSG, "JSON Response: " + rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);
						//now read the token
						/*msg = mHandler.obtainMessage();
						rBundle = new Bundle();
						rBundle.putInt(BUNDLE_ACTION, ACTION_LOAD_TOKEN);
						rBundle.putString(BUNDLE_MSG, rsp);
						msg.setData(rBundle);
						mHandler.sendMessage(msg);*/
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
}
