package net.kalinec.dndencounters;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.kalinec.dndencounters.activities.spells.SearchSpells;
import net.kalinec.dndencounters.activities.sync.SyncToWeb;
import net.kalinec.dndencounters.dice.DiceParser;
import net.kalinec.dndencounters.dice.DiceRollerDialog;

public class DnDEncountersActivity extends AppCompatActivity {
    private DiceRollerDialog diceRollerDialog;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_spells)
        {
            Intent myIntent = new Intent(this, SearchSpells.class);
            startActivity(myIntent);
            return true;
        }
        else if(id == R.id.action_dice_roller)
        {
            Intent myIntent = new Intent(this, DiceRollerDialog.class);
            startActivityForResult(myIntent, DiceRollerDialog.REQUEST_DICE_ROLL);
            return true;
        }
        else if(id == R.id.action_sync_to_web)
        {
            Intent myIntent = new Intent(this, SyncToWeb.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == DiceRollerDialog.REQUEST_DICE_ROLL)
        {
            if(resultCode == RESULT_OK)
            {
                assert data != null;
                String rollResult = data.getStringExtra(DiceRollerDialog.PASSED_ROLL);
                DiceParser diceParser = new DiceParser(rollResult);
                final AlertDialog rollResultAlert = new AlertDialog.Builder(this).create();
                rollResultAlert.setTitle("Roll Result");
                rollResultAlert.setMessage("Rolled " + rollResult + ": " + Integer.toString(diceParser.result()));
                rollResultAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rollResultAlert.dismiss();
                    }
                });
                rollResultAlert.show();
            }
        }
    }
}
