package net.kalinec.dndencounters.lib;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SerializableJsonObj extends JSONObject implements Serializable
{
	public SerializableJsonObj()
	{
		super();
	}
	
	public SerializableJsonObj(String json) throws JSONException
	{
		super(json);
	}
}
