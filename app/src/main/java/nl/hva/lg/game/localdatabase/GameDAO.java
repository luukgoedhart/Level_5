package nl.hva.lg.game.localdatabase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import nl.hva.lg.game.Model.Game;

@Dao
public interface GameDAO {

    @Query("SELECT * FROM game")
    //List<Reminder> getAllReminders();
    public LiveData<List<Game>> getAllGames();

    @Insert
    void insertGame(Game game);

    @Delete
    void deleteGame(Game game);

    @Update
    void updateGame(Game game);
}
