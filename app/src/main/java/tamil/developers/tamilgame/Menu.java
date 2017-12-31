package tamil.developers.tamilgame;

import java.util.Locale;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class Menu extends Activity {

	private AdView mAdView;
	Convert conn = new Convert();
	Constants constants = new Constants();
	Typeface tf;
	static SQLiteDatabase myDB1;
	String[] words = {"தமிழ்","புத்தகம்","சுதந்திரம்","அறிவியல்","சுத்தம்","வாழ்த்துக்கள்","இதயம்","மதியம்","விவசாயி","சிங்கம்",
		    "கடவுள்","உலகம்","கைக்கடிகாரம்","விமானம்","குளிர்காலம்","நீச்சல்குளம்","பிறந்தநாள்","வணக்கம்","சமாதானம்","அகராதி",
		    "வரைபடம்","திருத்தம்","இயக்குநர்","சிறிதளவு","அமைச்சரவை","கடிகாரம்","அஞ்சல்","நடுக்கம்","நினைவகம்","முட்டைக்கோஸ்",
		    "முட்டாள்","வர்த்தகம்","மின்னஞ்சல்","நீதிக்கதை","மண்டலம்","வசிப்பிடம்","ஒழுக்கம்","கட்டாயம்","சம்மதம்","பலாப்பழம்",
		    "விளையாட்டு","அடையாளம்","ஆய்வுக்கூடம்","தொழிலாளர்","போராட்டம்","அரசாங்கம்","நட்சத்திரம்","உபகரணம்","கட்டுக்கதை","முட்டாள்தனம்",
		    "இயந்திரம்","கத்தரிக்கோல்","மரியாதை","உறுதிமொழி","கீழ்ப்படிதல்","அடித்தளம்","துரிதம்","பொதிகட்டு","நிலநடுக்கம்","தகுதிகள்",
		    "சதுப்புநிலம்","நாற்கரம்","நாற்க்கோணம்","பந்தயம்","களஞ்சியம்","தடுப்பூசி","காலியிடம்","வெற்றிடம்","சம்பளம்","எக்ஸ்கதிர்",
		    "சேனைக்கிழங்கு","திடீரென்று","கொட்டாவி","அங்குலம்","இசைக்கருவி","பந்தயத்திடல்","புதைகுழி","கிரகம்","சுத்தியல்","இயந்திரங்கள்",
		    "மிளகாய்","சதித்திட்டம்","வரிக்குதிரை","பூச்சியம்","உற்சாகம்","விடுமுறை","பித்தன்","நிழல்படம்","வள்ளிக்கிழங்கு","ராஜினாமா",
		    "நெருப்பு","தடுப்புஊசி","சுழியம்","அசிங்கம்","வாய்ப்புண்","மேசைவிளக்கு","மாத்திரை","அட்டவணை","பந்தயத்தடம்","தலைவன்",
			"வளையாபதி","அநியாயம்","சகோதரன்","அறிவாளி","வேப்பமரம்","அசோகம்","விடியற்காலம்","பத்திரிகை","வேர்கடலை","ஆகாசத்தாமரை",
    		"சுயம்வரம்","தனிமம்","ஐந்தாம்படை","நட்புணர்வு","சாம்பார்","கடலைமிட்டாய்","சுடுகாடு","அக்கினிகுண்டம்","செயற்கை","வினையெச்சம்",
    		"துக்கம்","நல்லறிவு","உடன்பிறப்பு","நவக்கிரகம்","ஒட்டகசிவிங்கி","உரையாடல்","அரண்மனை","கத்திரிக்கோல்","சங்கதி","சிகைக்காய்",
    		"சீமந்தபுத்திரி","தாகம்","வேலைவாய்ப்பு","தனித்தன்மை","ஆவேசம்","தமிழினம்","தகப்பனார்","நெருப்புக்கோழி","ஊமத்தம்","இயற்பியல்",
    		"தேவாரம்","நிச்சயம்","சத்தியம்","பழிசுமத்துதல்","நல்லொழுக்கம்","ஒழுக்கம்","சங்ககாலம்","வேற்றுமை","கிளிப்பிள்ளை","திடகாத்திரம்",
    		"தனிமஅட்டவணை","ஒலிப்பான்","பதக்கம்","புழுதிப்புயல்","அதிரசம்","ஓங்காரம்","தனித்தமிழ்","கேளிக்கை","மாங்குயில்","இடைச்சொல்",
    		"கணிதக்குறியீடு","பட்டப்பகல்","முந்திரிப்பழம்","தென்னங்கன்று","சதுரம்","இராகுகாலம்","பருவக்காற்று","உலோகம்","வல்லினம்","ஆதித்தன்",
    		"கதிரியக்கம்","புளிச்சோறு","திருக்குறள்","எல்லைக்கல்","அறத்துப்பால்","நெய்தல்","மோட்சம்","உட்கோபம்","தன்னிச்சை","சிறுதானியம்",
    		"வைத்தியம்","திடீரென்று","மகுடம்","சந்தனம்","அமிர்தம்","விசித்திரம்","கன்றுக்குட்டி","சுருக்கம்","வசந்தகாலம்","புயல்காற்று",
    		"சமுத்திரம்","தரைவிரிப்பு","வெப்பம்","வேலைக்காரன்","வெண்கலம்","மதுவிலக்கு","துன்பம்","தெம்மாங்கு","தையல்எந்திரம்","பெட்ரோல்"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		setAds();
		tf = Typeface.createFromAsset(getAssets(),constants.fontPath);
		connectDatabase();
		set_buttons();
	}
	private void setAds(){
		MobileAds.initialize(this, "ca-app-pub-1233786554019860~9205333431");
		mAdView = findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
	}
	private void set_buttons() {
		set_btn(R.id.button1);
		set_btn(R.id.button2);
		set_btn(R.id.button3);
	}
	private void set_btn(final int id) {
		Button btn = findViewById(id);
		btn.setText(conn.convertText(btn.getText().toString()));
		btn.setTypeface(tf);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				switch (id) {
				case R.id.button1:
					startActivity(new Intent(getBaseContext(), Game.class));
					break;
				case R.id.button2:
					startActivity(new Intent(getBaseContext(), FinishList.class));
					break;
				case R.id.button3:
					startActivity(new Intent(getBaseContext(), ComingSoon.class));
					break;
				default: break;
				}
				finish();
			}
		});
	}
	private void connectDatabase() {
		String dbName  = this.getFilesDir().getPath() + constants.dbName;
	    myDB1 = openOrCreateDatabase(dbName , Context.MODE_PRIVATE, null);
	    myDB1.setVersion(constants.dbVersion);
	    myDB1.setLocale(Locale.getDefault());

		myDB1.execSQL("CREATE TABLE IF NOT EXISTS " + constants.wordTable + " (id INT(4), word varchar(30));");
		myDB1.execSQL("CREATE TABLE IF NOT EXISTS " + constants.resultTable + " (id INT(4), time INT(4), trial INT(3), flag INT(2));");
		for (int i = 1; i <= 200; i++){
			myDB1.execSQL("INSERT INTO " + constants.wordTable + " (id, word) Values" + " (" + i + ",'"+ words[i-1] +"');");
		}
	}
	@Override
	public void onResume() {
	    super.onResume();
	}
	@Override
	public void onPause() {
	    super.onPause();
	}
	@Override
	public void onBackPressed() {
	    finish();
	    super.onBackPressed();
	}
}
