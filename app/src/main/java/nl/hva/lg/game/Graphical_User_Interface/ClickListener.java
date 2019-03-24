package nl.hva.lg.game.Graphical_User_Interface;

import android.view.View;

public interface ClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
