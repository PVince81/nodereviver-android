package net.vincentpetry.nodereviver;

import android.app.Activity;
import android.os.Bundle;

public class NodeReviverActivity extends Activity {

    private GameSurfaceView gameView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gameView = (GameSurfaceView)this.findViewById(R.id.gameView1);
    }

    @Override
    public void onBackPressed() {
        if ( gameView != null ){
            if ( gameView.getGameThread().actionBack() ){
                return;
            }
        }
        super.onBackPressed();
    }
}