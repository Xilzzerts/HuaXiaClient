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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectActivity extends Activity {
	private static final String TAG = SelectActivity.class.getSimpleName();
	private TextView select_tv_name;
	private TextView select_tv_major;
	private TextView select_tv_classnum;
	private TextView select_tv_num;
	private ListView select_lv_item;
	private static final String[] items = new String[] { "成绩查询", "学分统计", "班级课表" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.select_layout);
		this.select_tv_name = (TextView) this.findViewById(R.id.select_tv_name);
		this.select_tv_major = (TextView) this
				.findViewById(R.id.select_tv_major);
		this.select_tv_classnum = (TextView) this
				.findViewById(R.id.select_tv_classnum);
		this.select_tv_num = (TextView) this.findViewById(R.id.select_tv_num);
		Bundle temp = this.getIntent().getBundleExtra("INFORMATION");
		this.select_tv_name.setText(temp.getString("NAME"));
		this.select_tv_major.setText(temp.getString("MAJOR"));
		this.select_tv_classnum.setText(temp.getString("CLASSNUM"));
		this.select_tv_num.setText(temp.getString("NUM"));

		this.select_lv_item = (ListView) this
				.findViewById(R.id.select_lv_items);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);
		select_lv_item.setAdapter(adapter);
		select_lv_item.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent intent = new Intent(SelectActivity.this,
							ScoreActivity.class);
					intent.putExtra("NUM", select_tv_num.getText());
					startActivity(intent);
					break;
				case 1:
					Toast.makeText(SelectActivity.this, "BUILDING...", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Intent intent_T = new Intent(SelectActivity.this,
							TableActivity.class);
					intent_T.putExtra("NUM", select_tv_num.getText());
					startActivity(intent_T);
					break;
				}
			}
		});
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
