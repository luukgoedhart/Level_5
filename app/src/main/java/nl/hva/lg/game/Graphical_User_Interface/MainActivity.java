package nl.hva.lg.game.Graphical_User_Interface;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import nl.hva.lg.game.Model.Game;
import nl.hva.msi.gamebacklog.R;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, Serializable {

    private GameRecyclerViewAdapter gameRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private ViewModelMain viewModelMain;
    private List<Game> games;
    private List<Game> tempGames;
    private View.OnClickListener getAllGamesBack;
    private View.OnClickListener getGetMyGameBack;
    private Game tempGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabMain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateGameActivity.class);
                startActivityForResult(i, 1);
            }
        });

        //first the recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialize all lists
        games = new LinkedList<Game>();
        tempGames = new LinkedList<>();

        //create the ViewModel
        viewModelMain = ViewModelProviders.of(this).get(ViewModelMain.class);
        viewModelMain.getAllGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> gameList) {
                games = gameList;
                updateUI();
            }
        });

        //touchlistener for the recyclerView
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, EditGameActivity.class);
                Game editGame = games.get(position);
                intent.putExtra("GAME", editGame);
                startActivityForResult(intent, 2);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //Swiping with a ItemTouchHelper
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int pos = viewHolder.getAdapterPosition();

                tempGame = games.get(pos);
                viewModelMain.deleteGame(games.get(pos));
                games.remove(pos);
                gameRecyclerViewAdapter.notifyItemRemoved(pos);

                Snackbar.make(findViewById(android.R.id.content), tempGame.getTitle() + " has been deleted", Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_undo, getGetMyGameBack)
                        .setActionTextColor(Color.MAGENTA)
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(this);


        //obviously we need Listeners for the Snackbar
        getGetMyGameBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelMain.insertGame(tempGame);
                updateUI();
            }
        };

        getAllGamesBack = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < tempGames.size(); i++) {
                    viewModelMain.insertGame(tempGames.get(i));
                }
                updateUI();
            }
        };
    }

    private void updateUI() {
        if (gameRecyclerViewAdapter == null) {
            gameRecyclerViewAdapter = new GameRecyclerViewAdapter(games);
            recyclerView.setAdapter(gameRecyclerViewAdapter);
        } else {
            gameRecyclerViewAdapter.swapList(games);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_item) {

            tempGames.addAll(games);

            for (int i = 0; i < games.size(); ) {

                viewModelMain.deleteGame(games.get(i));
                games.remove(i);
            }

            updateUI();

            Snackbar.make(findViewById(android.R.id.content), R.string.snackbar_text_delete_all, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_undo, getAllGamesBack)
                    .setActionTextColor(Color.RED)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String title = data.getStringExtra("title");
                String status = data.getStringExtra("status");
                String platform = data.getStringExtra("platform");
                String date = data.getStringExtra("date");

                Game newGame = new Game(title, status, platform, date);
                // New timestamp: timestamp of the update time
                viewModelMain.insertGame(newGame);
                updateUI();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                final Game editedGame = (Game) data.getSerializableExtra("GAME");

                // New timestamp: timestamp of update
                viewModelMain.updateGame(editedGame);
                updateUI();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
