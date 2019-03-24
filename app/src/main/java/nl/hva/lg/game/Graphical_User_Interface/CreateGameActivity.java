package nl.hva.lg.game.Graphical_User_Interface;

import android.annotation.SuppressLint;
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

public class CreateGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        // Set action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Create_Game_Action_Bar);
        setSupportActionBar(toolbar);

        final Spinner dropdown = findViewById(R.id.spinnerCreate);
        final EditText titleInput = findViewById(R.id.titleInput);
        final EditText platformInput = findViewById(R.id.platformInput);

        FloatingActionButton fab = findViewById(R.id.saveGame);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dropdownChoice = dropdown.getSelectedItem().toString();
                String title = titleInput.getText().toString();
                String platform = platformInput.getText().toString();

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.dateFormat));
                Calendar calendar = Calendar.getInstance();
                String date = simpleDateFormat.format(calendar.getTime());

                Intent returnInten = new Intent();
                returnInten.putExtra("title", title);
                returnInten.putExtra("platform", platform);
                returnInten.putExtra("status", dropdownChoice);
                returnInten.putExtra("date", date);
                setResult(Activity.RESULT_OK, returnInten);
                finish();

            }

        });
        //for the dropdown spinner
        String[] items = new String[]{"","Want to play", "Playing", "Stalled", "Dropped"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

}
