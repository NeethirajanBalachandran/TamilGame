package tamil.developers.tamilgame;

import java.util.Locale;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import tamil.developers.tamilgame.DragDropManager.DropZoneListener;

public class Game extends Activity implements OnTouchListener {

	private InterstitialAd mInterstitialAd;
	Convert conn = new Convert();
	Constants constants = new Constants();
	Typeface tf;
	static SQLiteDatabase myDB1;
	public boolean done = false;
	int time = 0;
	public String word;
	public String answer;
	public int id;
	public int word_length;
	public boolean exit = false;
	int[] letterIds = {0, R.id.letter1,R.id.letter2,R.id.letter3,R.id.letter4,R.id.letter5,R.id.letter6,R.id.letter7,R.id.letter8,R.id.letter9,R.id.letter10};
	int[] backIds = {0, R.id.back1,R.id.back2,R.id.back3,R.id.back4,R.id.back5,R.id.back6,R.id.back7,R.id.back8,R.id.back9,R.id.back10};
	int countForAd = 0;
	boolean backPress = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		setAds();
		tf = Typeface.createFromAsset(getAssets(),constants.fontPath);
		connectDatabase();
		dragAndDrop();
		startWord();
	}
	private void setAds(){
		MobileAds.initialize(this, "ca-app-pub-1233786554019860~9205333431");
		AdView mAdView = findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		mInterstitialAd = new InterstitialAd(this);
		if (getResources().getString(R.string.production).equals("prod")){
			mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
		} else {
			mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id_test));
		}
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				// Load the next interstitial.
				if (backPress){
					MenuPage();
				} else {
					mInterstitialAd.loadAd(new AdRequest.Builder().build());
					startWord();
				}
			}

		});
	}
	private void connectDatabase() {
		String dbName = this.getFilesDir().getPath() + constants.dbName;
		myDB1 = openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		myDB1.setVersion(constants.dbVersion);
		myDB1.setLocale(Locale.getDefault());
	}
	private void dragAndDrop() {
		DragDropManager.getInstance().init(this);
		for (int i = 1; i <= 10; i++){
			findViewById(letterIds[i]).setOnTouchListener(this);
			DragDropManager.getInstance().addDropZone(findViewById(letterIds[i]), dropZoneListener1);
		}
		DragDropManager.getInstance().addDropZone(findViewById(R.id.layout_total), dropZoneListener1);
	}
	private void startWord(){
		((Button)findViewById(R.id.submit)).setText(getString(R.string.submit));
		btn_go_white();
		done = false;
		getWord();
		if (!exit) {
			time = 0;
			startCrono();
			splitWord();
			shuffledWord();

			String[] wordSplit = word.split(":");
			word_length = wordSplit.length;
			Button btn = findViewById(R.id.submit);
			btn.setTypeface(tf);
			for (int i = 1 ; i <= 10 ; i ++ ){
				btn = findViewById(backIds[i]);
				btn.setText("");
				btn.setVisibility(View.GONE);
				btn.setTypeface(tf);
			}
			for (int i = 1 ; i <= 10 ; i ++ ){
				btn = findViewById(letterIds[i]);
				if (i <= wordSplit.length){
					btn.setText(wordSplit[i-1]);
					btn.setText(conn.convertText(btn.getText().toString()));
					btn.setVisibility(View.VISIBLE);
					btn.setTypeface(tf);
				} else {
					btn.setText("");
					btn.setVisibility(View.GONE);
				}
			}
		} else {
			Intent openMainList = new Intent(Game.this, Exit.class);
			startActivity(openMainList);
			finish();
		}
	}
	private int createRandom(int max) {
		Random r = new Random();
		return r.nextInt(max) + 1;
	}
	private void getWord() {
		Cursor c = myDB1.rawQuery("SELECT * FROM " + constants.resultTable + " Where flag = 1", null);
		int count = c.getCount();
		c.close();

		TextView txt = findViewById(R.id.count);
		txt.setTypeface(tf);
		txt.setText(("200 ,y; " + (count + 1)));
		txt.setTextColor(Color.BLACK);

		Cursor c1 = myDB1.rawQuery("SELECT * FROM " + constants.wordTable + " Where id = " + (count + 1), null);
		id = count + 1;
		if (c1.getCount() > 0 && count < 200)
		{
			c1.moveToFirst();
			int Column_word = c1.getColumnIndex("word");
			word = c1.getString(Column_word);
			answer = word;
			exit = false;
		} else {
			exit = true;
		}
		c1.close();
	}
	private void splitWord() {
		String[] word2 = word.split("");
		StringBuilder wordAfterSplit = new StringBuilder();
		for (int j = 1 ; j < word2.length ; j ++) {
			if (j != (word2.length - 1)) {
				if (word2[j + 1].equals(getString(R.string.split1)) || word2[j + 1].equals(getString(R.string.split2)) ||
						word2[j + 1].equals(getString(R.string.split3)) || word2[j + 1].equals(getString(R.string.split4)) ||
						word2[j + 1].equals(getString(R.string.split5)) || word2[j + 1].equals(getString(R.string.split6)) ||
						word2[j + 1].equals(getString(R.string.split7)) || word2[j + 1].equals(getString(R.string.split8)) ||
						word2[j + 1].equals(getString(R.string.split9)) || word2[j + 1].equals(getString(R.string.split10)) ||
						word2[j + 1].equals(getString(R.string.split11)) || word2[j + 1].equals(getString(R.string.split12))) {
					wordAfterSplit.append(word2[j]);
					wordAfterSplit.append(word2[j + 1]);
					wordAfterSplit.append(":");
					j++;
				} else {
					wordAfterSplit.append(word2[j]);
					wordAfterSplit.append(":");
				}
			} else {
				wordAfterSplit.append(word2[j]);
				wordAfterSplit.append(":");
			}
		}
		word = wordAfterSplit.toString().substring(0,wordAfterSplit.toString().length() - 1);
		answer = word;
	}
	private void shuffledWord() {
		String[] wordSplit = word.split(":");
		int l = wordSplit.length;
		int[] ln = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		int[] lm = {0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0};
		for (int i = 0 ; i < l ; i++){
			int rn = createRandom( l - i);
			lm[i] = ln[rn-1];
			System.arraycopy(ln,rn,ln, rn-1 , ln.length-rn);
		}
		StringBuilder tempWord = new StringBuilder();
		for (int i = 0 ; i < l ; i++){
			if (i == 0) tempWord.append(wordSplit[lm[i]]);
			else {
				tempWord.append(":");
				tempWord.append(wordSplit[lm[i]]);
			}
		}
		word = tempWord.toString();
	}
	private int get_button_num(int id){
		switch (id)
		{
			case R.id.letter1: 		return 1;
			case R.id.letter2:		return 2;
			case R.id.letter3: 		return 3;
			case R.id.letter4: 		return 4;
			case R.id.letter5: 		return 5;
			case R.id.letter6: 		return 6;
			case R.id.letter7: 		return 7;
			case R.id.letter8: 		return 8;
			case R.id.letter9: 		return 9;
			case R.id.letter10: 	return 10;

			default:	return 0;
		}
	}
	DropZoneListener dropZoneListener1 = new DropZoneListener()
	{
		@Override
		public void OnDropped(View zone, Object item){
			Button t_btn = findViewById(Integer.parseInt(item.toString()));
			Button toBackButton = findViewById(backIds[get_button_num(Integer.parseInt(item.toString()))]);
			if (zone.getId() != R.id.layout_total){
				Button f_btn = (Button)zone;
				toBackButton.setVisibility(View.GONE);
				t_btn.setVisibility(View.VISIBLE);
				String f_str = f_btn.getText().toString();
				String t_str = t_btn.getText().toString();
				f_btn.setText(t_str);
				t_btn.setText(f_str);
			} else {
				set_button_visibility();
			}
			toBackButton.setVisibility(View.GONE);
			t_btn.setVisibility(View.VISIBLE);
			set_button_visibility();
		}
		@Override
		public void OnDragZoneLeft(View zone, Object item){}
		@Override
		public void OnDragZoneEntered(View zone, Object item){}
	};
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!done) {
			DragDropManager.getInstance().startDragging(v, v.getId());
			Button btn = findViewById(backIds[get_button_num(v.getId())]);
			btn.setVisibility(View.VISIBLE);
			btn = (Button)v;
			btn.setVisibility(View.GONE);
		}
		return false;
	}
	private void set_button_visibility() {
		for(int i = 1 ; i <= 10 ; i ++){
			Button btn = findViewById(letterIds[i]);
			if (i <= word_length) btn.setVisibility(View.VISIBLE);
			else btn.setVisibility(View.GONE);
			Button btn1 = findViewById(backIds[i]);
			btn1.setVisibility(View.GONE);
		}
	}
	public void show(View v){
		set_button_visibility();
	}
	public void submit(View v) {
		if (!done) {
			StringBuilder Result = new StringBuilder();
			Button btn;
			String check_answer = answer;
			String[] answerSplit = check_answer.split(":");
			for (int j = 0 ; j < answerSplit.length ; j ++){
				if (j == 0) check_answer = conn.convertText(answerSplit[j]);
				else 		check_answer = check_answer + "" + conn.convertText(answerSplit[j]);
			}
			for (int i = 1 ; i <= 10 ; i ++ ){
				if (i <= answerSplit.length){
					Result.append(((Button)findViewById(letterIds[i])).getText());
				}
			}
			btn = (Button)v;
			if (Result.toString().equals(check_answer)){
				//Toast.makeText(getBaseContext(), "Correct", Toast.LENGTH_SHORT).show();
				btn.setTextColor(Color.WHITE);
				btn.setText(R.string.next);
				done = true;
				btn_go_green();
				stopCrono();
				enter_result(true);
			} else {
				btn.setTextColor(Color.RED);
				done = false;
				enter_result(false);
			}
		} else {
			if (countForAd < 4){
				countForAd++;
				startWord();
			} else {
				if (mInterstitialAd.isLoaded()) {
					mInterstitialAd.show();
					countForAd = 0;
				} else {
					startWord();
				}
			}
		}
	}
	private void btn_go_white() {
		for (int i = 1 ; i <= 10 ; i ++ ){
			Button btn = findViewById(letterIds[i]);
			btn.setTextColor(Color.WHITE);
		}
	}
	private void btn_go_green() {
		for (int i = 1 ; i <= 10 ; i ++ ){
			Button btn = findViewById(letterIds[i]);
			btn.setTextColor(Color.GREEN);
		}
	}
	private void enter_result(boolean set_flag) {
		Cursor c = myDB1.rawQuery("SELECT * FROM " + constants.resultTable + " where id = " + id, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			int Column_trial = c.getColumnIndex("trial");
			int trial = c.getInt(Column_trial);
			int Column_time = c.getColumnIndex("time");
			int t_time = c.getInt(Column_time);
			myDB1.execSQL("UPDATE " + constants.resultTable + " SET trial = " + (trial + 1) + " where id = " + id );
			myDB1.execSQL("UPDATE " + constants.resultTable + " SET time = " + (t_time + time) + " where id = " + id );
			if (set_flag)	myDB1.execSQL("UPDATE " + constants.resultTable + " SET flag = 1 where id = " + id );
		} else {
			if (set_flag)	myDB1.execSQL("INSERT Into " + constants.resultTable + " (id, time, trial, flag) Values (" + id + "," + time + ",1,1)");
			else			myDB1.execSQL("INSERT Into " + constants.resultTable + " (id, time, trial, flag) Values (" + id + "," + time + ",1,0)");
		}
		c.close();
	}
	private void startCrono() {
		Chronometer clock = findViewById(R.id.chronometerTime);
		clock.start();
		clock.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				TextView txt = findViewById(R.id.time);
				txt.setText((time + ""));
				txt.setTypeface(tf);
				txt.setTextColor(Color.BLACK);
				time = time + 1;
			}
		});
	}
	private void stopCrono() {
		Chronometer clock = findViewById(R.id.chronometerTime);
		clock.stop();
	}
	@Override
	public void onPause() {
		super.onPause();
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onBackPressed(){
		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
			backPress = true;
		} else {
			MenuPage();
		}
	}
	private void MenuPage() {
		startActivity(new Intent(Game.this, Menu.class));
		finish();
	}
}
