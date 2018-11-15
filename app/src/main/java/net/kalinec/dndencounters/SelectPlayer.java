package net.kalinec.dndencounters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.characters.PcListAdapter;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.CharacterDao;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.PlayerListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayer extends AppCompatActivity {

    public static final int REQUEST_SELECT_PLAYER = 42;
    private LiveData<List<Player>> players;
    private PlayerListAdapter playerListAdapter;
    private RecyclerView playerSearchRv;
    private SearchView playerSearchSv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);

        PlayerDao playerDao = AppDatabase.getDatabase(getApplicationContext()).playerDao();
        players = playerDao.getAllPlayers();
        playerListAdapter = new PlayerListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                Player p = playerListAdapter.get(position);
                selectPlayer(p);
            }
        });
        players.observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable List<Player> players) {
                playerListAdapter.setPlayerList(players);
            }
        });

        playerSearchRv = findViewById(R.id.playerSearchRv);
        playerSearchRv.setAdapter(playerListAdapter);
        playerSearchRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        playerSearchSv = (SearchView)findViewById(R.id.playerSearchSv);
        playerSearchSv.setActivated(true);
        playerSearchSv.onActionViewExpanded();
        playerSearchSv.setIconified(false);
        playerSearchSv.clearFocus();
        playerSearchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            private List<Player> filter(List<Player> models, String query) {
                final String lowerCaseQuery = query.toLowerCase();

                final List<Player> filteredModelList = new ArrayList<>();
                for (Player model : models) {
                    final String text = model.getName().toLowerCase();
                    if (text.contains(lowerCaseQuery)) {
                        filteredModelList.add(model);
                    }
                }
                return filteredModelList;
            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                List<Player> filteredPlayers = filter(playerListAdapter.getPlayerList(), newText);
                playerListAdapter.setPlayerList(filteredPlayers);
                playerSearchRv.scrollToPosition(0);
                return true;
            }
        });
    }

    private void selectPlayer(Player player)
    {
        Intent data = new Intent();
        data.putExtra(Player.PASSED_PLAYER, player);
        setResult(RESULT_OK, data);
        finish();
    }
}
