package net.kalinec.dndencounters.monster_tokens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import net.kalinec.dndencounters.R;

public class MonsterTokenSpinnerAdapter extends BaseAdapter
{
    private List<MonsterToken> monsterTokens = new ArrayList<>();
    private Context context;

    public MonsterTokenSpinnerAdapter(Context context) {
        this.context = context;
        monsterTokens = MonsterTokens.getAllMonsterTokens(context);
    }

    public List<MonsterToken> getMonsterTokens() {
        return monsterTokens;
    }

    @Override
    public int getCount() {
        return monsterTokens.size();
    }

    @Override
    public Object getItem(int position) {
        return monsterTokens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_list_monster_tokens, parent, false);
        MonsterToken monsterToken = (MonsterToken)getItem(position);
        TextView MonsterTokenName = (TextView) convertView.findViewById(R.id.MonsterTokenName);
        ImageView MonsterTokenPortrait = (ImageView) convertView.findViewById(R.id.MonsterTokenPortrait);
        MonsterTokenName.setText(monsterToken.getTokenName());
        monsterToken.makePortrait(MonsterTokenPortrait);
        return convertView;
    }
}
