/*
Copyright 2013 Zirconi

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


package com.zirconi.huaxiaclient;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DetailActivity extends Activity {
	private static final String[] FROM = { "name", "times", "time", "teacher",
			"room" };
	private static final int[] TO = { R.id.de_name, R.id.de_times,
			R.id.de_time, R.id.de_teacher, R.id.de_room };

	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);
		int flag = this.getIntent().getIntExtra("day", 0);
		lv = (ListView) this.findViewById(R.id.detail_lv_item);
		ArrayList<Map<String, String>> row_1 = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> row_2 = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> row_3 = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> row_4 = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> row_5 = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> row_6 = new ArrayList<Map<String, String>>();
		ArrayList<Map<String, String>> row_7 = new ArrayList<Map<String, String>>();

		switch (flag) {
		case 0:
			row_1.add(TableActivity.item_1.get(0));
			row_1.add(TableActivity.item_2.get(0));
			row_1.add(TableActivity.item_3.get(0));
			row_1.add(TableActivity.item_4.get(0));

			SimpleAdapter adapter = new SimpleAdapter(DetailActivity.this,
					row_1, R.layout.table_item, FROM, TO);
			lv.setAdapter(adapter);
			break;
		case 1:
			row_2.add(TableActivity.item_1.get(1));
			row_2.add(TableActivity.item_2.get(1));
			row_2.add(TableActivity.item_3.get(1));
			row_2.add(TableActivity.item_4.get(1));

			SimpleAdapter adapter_2 = new SimpleAdapter(DetailActivity.this,
					row_2, R.layout.table_item, FROM, TO);
			lv.setAdapter(adapter_2);
			break;
		case 2:
			row_3.add(TableActivity.item_1.get(2));
			row_3.add(TableActivity.item_2.get(2));
			row_3.add(TableActivity.item_3.get(2));
			row_3.add(TableActivity.item_4.get(2));

			SimpleAdapter adapter_3 = new SimpleAdapter(DetailActivity.this,
					row_3, R.layout.table_item, FROM, TO);
			lv.setAdapter(adapter_3);
			break;
		case 3:
			row_4.add(TableActivity.item_1.get(3));
			row_4.add(TableActivity.item_2.get(3));
			row_4.add(TableActivity.item_3.get(3));
			row_4.add(TableActivity.item_4.get(3));

			SimpleAdapter adapter_4 = new SimpleAdapter(DetailActivity.this,
					row_4, R.layout.table_item, FROM, TO);
			lv.setAdapter(adapter_4);
			break;
		case 4:
			row_5.add(TableActivity.item_1.get(4));
			row_5.add(TableActivity.item_2.get(4));
			row_5.add(TableActivity.item_3.get(4));
			row_5.add(TableActivity.item_4.get(4));

			SimpleAdapter adapter_5 = new SimpleAdapter(DetailActivity.this,
					row_5, R.layout.table_item, FROM, TO);
			lv.setAdapter(adapter_5);

			break;
		case 5:
			row_6.add(TableActivity.item_1.get(5));
			row_6.add(TableActivity.item_2.get(5));
			row_6.add(TableActivity.item_3.get(5));
			row_6.add(TableActivity.item_4.get(5));

			SimpleAdapter adapter_6 = new SimpleAdapter(DetailActivity.this,
					row_6, R.layout.table_item, FROM, TO);
			lv.setAdapter(adapter_6);
			break;
		case 6:
			row_7.add(TableActivity.item_1.get(6));
			row_7.add(TableActivity.item_2.get(6));
			row_7.add(TableActivity.item_3.get(6));
			row_7.add(TableActivity.item_4.get(6));

			SimpleAdapter adapter_7 = new SimpleAdapter(DetailActivity.this,
					row_7, R.layout.table_item, FROM, TO);
			lv.setAdapter(adapter_7);
			break;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			new AlertDialog.Builder(this)
					.setTitle("About")
					.setMessage(
							"VERSION:0.05 BETA\nJust for fun!\nAuthor:Zirconi\nMail:Geekkou@gmail.com")
					.setPositiveButton("OK", null).show();

			break;
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}
}
