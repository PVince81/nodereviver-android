package net.vincentpetry.nodereviver;

import android.app.Activity;
import android.os.Bundle;

public class NodeReviverActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GameSurfaceView view = (GameSurfaceView) this.findViewById(R.id.gameView1);
    }
}