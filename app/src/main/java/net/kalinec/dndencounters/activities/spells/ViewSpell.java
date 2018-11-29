package net.kalinec.dndencounters.activities.spells;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.spells.Spell;

public class ViewSpell extends AppCompatActivity {
    private Spell selectedSpell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        selectedSpell = (Spell)bundle.getSerializable(Spell.PASSED_SPELL);
        setContentView(R.layout.activity_view_spell);

    }
}
