package net.kalinec.dndencounters.playsessions;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PlaySessionManager
{
    private final static String completedFname = "completed_sessions.srl";
    private final static String activeFname = "active_sessions.srl";
    private final static String currentFname = "current_session.srl";

    public static PlaySession getCurrentSession(Context context)
    {
        PlaySession activeSession;
        try
        {
            File fout = new File(context.getFilesDir(), currentFname);
            if(!fout.exists())
                return null;
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput(currentFname));
            activeSession = (PlaySession)ois.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            return null;
        }
    
        return activeSession;
    }
    
    static void saveCurrentSession(Context context, PlaySession activeSession)
    {
        File fout = new File(context.getFilesDir(), currentFname);
        try {
            if (!fout.exists())
                fout.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(currentFname, Context.MODE_PRIVATE));
            oos.writeObject(activeSession);
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<PlaySession> getActiveSessions(Context context)
    {
        ArrayList<PlaySession> activeSessions = new ArrayList<>();
        try
        {
            File fin = new File(context.getFilesDir(), activeFname);
            if(fin.exists() && fin.length() > 0)
            {
                ObjectInputStream in = new ObjectInputStream(context.openFileInput(activeFname));
                Integer numSessions = (Integer)in.readObject();
                for(int i = 0; i < numSessions; i++)
                    activeSessions.add((PlaySession) in.readObject());
                in.close();
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            return new ArrayList<>();
        }
    
        return activeSessions;
    }
    
    private static void saveActiveSession(Context context, PlaySession activeSession)
    {
        ArrayList<PlaySession> activeSessions = PlaySessionManager.getActiveSessions(context);
        activeSessions.add(activeSession);
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(activeFname, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(activeSessions.size());
            for(PlaySession e: activeSessions)
                oos.writeObject(e);
            oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static ArrayList<PlaySession> getCompletedSessions(Context context)
    {
        ArrayList<PlaySession> completedSessions = new ArrayList<>();
        try
        {
            File fin = new File(context.getFilesDir(), completedFname);
            if(fin.exists() && fin.length() > 0)
            {
                ObjectInputStream in = new ObjectInputStream(context.openFileInput(completedFname));
                Integer numSessions = (Integer)in.readObject();
                for(int i = 0; i < numSessions; i++)
                    completedSessions.add((PlaySession) in.readObject());
                in.close();
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            return new ArrayList<>();
        }
    
        return completedSessions;
    }

    public static void saveCurrentSession(Context context)
    {
        PlaySession currentSession = PlaySessionManager.getCurrentSession(context);
        PlaySessionManager.saveActiveSession(context, currentSession);
        File fin = new File(context.getFilesDir(), completedFname);
        fin.delete();
    }

    public static void completeCurrentSession(Context context)
    {
        PlaySession currentSession = PlaySessionManager.getCurrentSession(context);
        assert currentSession != null;
        currentSession.completeSession();
        ArrayList<PlaySession> completedSessions = PlaySessionManager.getCompletedSessions(context);
        completedSessions.add(currentSession);
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(completedFname, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(completedSessions.size());
            for(PlaySession e: completedSessions)
                oos.writeObject(e);
            oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void removeFromActiveSessions(Context context, PlaySession toRemove)
    {
        ArrayList<PlaySession> activeSessions = PlaySessionManager.getActiveSessions(context);
        int pos = 0;
        for(PlaySession session: activeSessions)
        {
            if(toRemove.getUuid().equals(session.getUuid()))
                break;
            pos++;
        }
        activeSessions.remove(pos);
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(activeFname, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(activeSessions.size());
            for(PlaySession e: activeSessions)
                oos.writeObject(e);
            oos.flush();
            oos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
