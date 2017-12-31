package tamil.developers.tamilgame;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int height = metrics.heightPixels;
		Start();
		ImageView img = findViewById(R.id.logo);
		img.getLayoutParams().height = height/2;

	}
	private void Start() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(MainActivity.this, Menu.class));
				finish();
			}
		}, 3000);
	}
    @Override
    public void onBackPressed(){
    }
}
