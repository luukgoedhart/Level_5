package nl.hva.lg.game.localdatabase;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import nl.hva.lg.game.Model.Game;

public class GameRepo implements GameDAO{

    private GameDatabase gameDatabase;
    private GameDAO gameDAO;
    private LiveData<List<Game>> gameList;
    private Executor executor = Executors.newSingleThreadExecutor();

    public GameRepo(Context context) {
        this.gameDatabase = GameDatabase.getDatabase(context);
        this.gameDAO = gameDatabase.gameDao();
        this.gameList = gameDAO.getAllGames();
    }

    @Override
    public LiveData<List<Game>> getAllGames() {
        return gameList;
    }

    @Override
    public void insertGame(final Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDAO.insertGame(game);
            }
        });
    }

    @Override
    public void deleteGame(final Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDAO.deleteGame(game);
            }
        });
    }

    @Override
    public void updateGame(final Game game) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                gameDAO.updateGame(game);
            }
        });
    }
}
