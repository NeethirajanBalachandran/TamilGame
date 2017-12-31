package tamil.developers.tamilgame;

import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;

public class Exit extends Activity {

	Convert conn = new Convert();
	Constants constants = new Constants();
	Typeface tf;
	static SQLiteDatabase myDB1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exit);
		connectDatabase();
		conn = new Convert();
		tf = Typeface.createFromAsset(getAssets(),constants.fontPath);
		
		TextView txt = findViewById(R.id.exitmsg);
		txt.setTypeface(tf);
		txt.setTextColor(Color.BLACK);
		txt.setText(conn.convertText(txt.getText().toString()));
		txt.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=tamil.developers.tamilgamepaid");
	            startActivity(new Intent(Intent.ACTION_VIEW, uri));
	        }
	    });
		
		TextView txt1 = findViewById(R.id.tryagain);
		txt1.setTypeface(tf);
		txt1.setTextColor(Color.BLACK);
		txt1.setText(conn.convertText(txt1.getText().toString()));
		txt1.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	myDB1.execSQL("DROP TABLE IF EXISTS " + constants.resultTable);
	        	myDB1.execSQL("CREATE TABLE IF NOT EXISTS " + constants.resultTable + " (id INT(4), time INT(4), trial INT(3), flag BIT);");
	        	Intent openMainList = new Intent(Exit.this, Game.class);
	    		startActivity(openMainList);
	    		finish();
	        }
	    });
	}
	private void connectDatabase() {
		String dbName = this.getFilesDir().getPath() + constants.dbName;
		myDB1 = openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		myDB1.setVersion(constants.dbVersion);
		myDB1.setLocale(Locale.getDefault());
	}
	@Override
    public void onBackPressed(){
		startActivity(new Intent(Exit.this, Menu.class));
		finish();
    }
}
