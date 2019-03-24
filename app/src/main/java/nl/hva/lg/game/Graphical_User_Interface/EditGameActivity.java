package nl.hva.lg.game.Graphical_User_Interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nl.hva.msi.gamebacklog.R;
import nl.hva.lg.game.Model.Game;

public class EditGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Create_Game_Action_Bar);
        setSupportActionBar(toolbar);


        final Spinner mySpin = findViewById(R.id.spinnerEdit);
        final EditText titleEdit = findViewById(R.id.titleInput);
        final EditText platformEdit = findViewById(R.id.platformInput);

        //for the dropdown
        final Spinner myDropdown = findViewById(R.id.spinnerEdit);
        String[] items = new String[]{"", getString(R.string.dropdown_option_1), getString(R.string.dropdown_option_2), getString(R.string.dropdown_option_3), getString(R.string.dropdown_option_4)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        myDropdown.setAdapter(adapter);

        Intent intent = getIntent();
        final Game game = (Game) intent.getSerializableExtra("GAME");

        //set all inputfields from the game
        titleEdit.setText(game.getTitle());
        String status = game.getStatus();
        if (status == null) {
            status = "";
        }
        myDropdown.setSelection(getDropdownValue(status));
        platformEdit.setText(game.getPlatform());

        //the save floating button
        FloatingActionButton fab = findViewById(R.id.saveEditGame);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dropdownItem = myDropdown.getSelectedItem().toString();
                String title = titleEdit.getText().toString();
                String platform = platformEdit.getText().toString();

                //Date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.dateFormat));
                Calendar calendar = Calendar.getInstance();
                String date = simpleDateFormat.format(calendar.getTime());

                //new values of the Game
                game.setDate(date);
                game.setTitle(title);
                game.setPlatform(platform);
                game.setStatus(dropdownItem);

                Intent resultInt = new Intent();
                resultInt.putExtra("GAME", game);
                setResult(Activity.RESULT_OK, resultInt);
                finish();
            }
        });




    }

    // function to retrieve dropdown value
    private int getDropdownValue(String value) {
        int pos = 9;
        switch (value) {
            case "":
                pos = 0;
                break;
            case "Want to play":
                pos = 1;
                break;
            case "Playing":
                pos = 2;
                break;
            case "Stalled":
                pos = 3;
                break;
            case "Dropped":
                pos = 4;
                break;

        }
        return pos;
    }

}
