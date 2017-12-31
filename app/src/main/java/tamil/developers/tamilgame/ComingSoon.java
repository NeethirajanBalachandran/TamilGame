package tamil.developers.tamilgame;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

public class ComingSoon extends Activity {

	Convert conn = new Convert();
	Constants constants = new Constants();
	Typeface tf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coming_soon);

		tf = Typeface.createFromAsset(getAssets(),constants.fontPath);
		
		(findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=tamil.developers.tamilgame2");
	            startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});

		(findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=tamil.developers.tamilgamepaid");
	            startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
		
		Button btn = findViewById(R.id.button1);
		btn.setText(conn.convertText(btn.getText().toString()));
		btn.setTypeface(tf);
		btn = findViewById(R.id.button2);
		btn.setText(conn.convertText(btn.getText().toString()));
		btn.setTypeface(tf);
	}
	@Override
	public void onBackPressed() {
		startActivity(new Intent(ComingSoon.this, Menu.class));
		finish();
	}
}
