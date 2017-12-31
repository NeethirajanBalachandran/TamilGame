package tamil.developers.tamilgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;

public class FinishList extends Activity {

	Convert conn = new Convert();
	Constants constants = new Constants();
	Typeface tf;
	static SQLiteDatabase myDB1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_list);
		
		database_connect();
		tf = Typeface.createFromAsset(getAssets(),constants.fontPath);
		
		Cursor c = myDB1.rawQuery("SELECT * FROM " + constants.resultTable + " ", null);
		if (c.getCount() > 0) {
			ListView listView = findViewById(R.id.listView1);
			final ArrayList<Map<String, String>> list = buildData();
		    String[] from = { "name", "purpose" };
		    int[] to = {R.id.text1, R.id.text2 }; 
		    SimpleAdapter adapter = new SimpleAdapter(this, list,R.layout.list_layout, from, to){
		            @Override
			        public View getView(int pos, View convertView, ViewGroup parent){
			            View v = convertView;
			            if(v== null){
			                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			                v=vi.inflate(R.layout.list_layout, null);
			            }
			            Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/vellore.ttf");		
			            TextView tv = v.findViewById(R.id.text1);
			            String temp = list.get(pos).get("name");
			            String[] tempSplit = temp.split("::");
			            tv.setText((tempSplit[0] + ". " + conn.convertText(tempSplit[1].replace(":", ""))));
			            tv.setTypeface(tf);
			            TextView tvs = v.findViewById(R.id.text2);
			            String temp1 = list.get(pos).get("purpose");
			            String[] tempSplit1 = temp1.split(":");
			            tvs.setText((tempSplit1[0] + "-Kaw;rp" + "---" + tempSplit1[1] + "-beho"));
			            tvs.setTypeface(tf);
			            return v;
			        }
			};
			listView.setAdapter(adapter);
		} else {
			TextView txt = findViewById(R.id.finish_zero_msg);
            txt.setText(conn.convertText(getResources().getString(R.string.finish_zero_msg)));
            txt.setTextColor(Color.BLACK);
            txt.setTypeface(tf);
		}
		c.close();
	}
	private void database_connect() {
		String dbName = this.getFilesDir().getPath() + constants.dbName;
	    myDB1 = openOrCreateDatabase(dbName , Context.MODE_PRIVATE, null);
	    myDB1.setVersion(constants.dbVersion);
	    myDB1.setLocale(Locale.getDefault());
	    
	    myDB1.execSQL("CREATE TABLE IF NOT EXISTS " + constants.wordTable + " (id INT(4), word varchar(50));");
	    myDB1.execSQL("CREATE TABLE IF NOT EXISTS " + constants.resultTable + " (id INT(4), time INT(4), trial INT(3), flag BIT);");
	    
	}
	private ArrayList<Map<String, String>> buildData() {
		ArrayList<Map<String, String>> list = new ArrayList<>();
	    
		Cursor c = myDB1.rawQuery("SELECT * FROM " + constants.resultTable + " where flag = 1", null);

		Cursor c1 = myDB1.rawQuery("SELECT * FROM " + constants.wordTable, null);
		
		int Column1 = c1.getColumnIndex("word");
		int Column2 = c1.getColumnIndex("id");
		int Column_1 = c.getColumnIndex("trial");
		int Column_2 = c.getColumnIndex("time");
		if (c.getCount() > 0) {
			c1.moveToFirst();
			c.moveToFirst();
			do
	    	{
				list.add(putData(c1.getInt(Column2) + "::" + c1.getString(Column1),c.getInt(Column_1) + ":" + c.getInt(Column_2)));
	    	}while(c1.moveToNext() && c.moveToNext());
		}
	    c1.close();
	    c.close();
	    return list;
	}
	private HashMap<String, String> putData(String name, String purpose) {
	    HashMap<String, String> item = new HashMap<>();
	    item.put("name", name);
	    item.put("purpose", purpose);
	    return item;
	}
	@Override
    public void onBackPressed(){
		startActivity(new Intent(FinishList.this, Menu.class));
		finish();
    }
}
