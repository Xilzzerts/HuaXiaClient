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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TableActivity extends Activity {
	private static final String TABLE_PAGE = "xsgrkb.aspx";
	private static final String EXTRA = "&type=xs";
	private static final int DAY_PER_WEEK = 7;
	// UNICODE 第一节，第三节，第五节，第七节
	private static final String TABLE_PATTERN = "<td align=\"Center\"( rowspan=\"2\")?( width=\"14%\")?>(.*?)</td>";
	private static final String FIRST_LINE = "<td width=\"1%\">[\u7B2C][\u4E00][\u8282]</td>.+";
	private static final String SECOND_LINE = "<td>[\u7B2C][\u4E09][\u8282]</td>.+";
	private static final String THIRD_LINE = "<td>[\u7B2C][\u4E94][\u8282]</td>.+";
	private static final String FOURTH_LINE = "<td>[\u7B2C][\u4E03][\u8282]</td>.+";
	private static final String DETAIL_PATTERN = "(.*?)<br>(.*?)<br>(.*?)[z|j]\\[(.+)\\]<br>(.+)";
	private static final String[] WEEK = { "星期一", "星期二", "星期三", "星期四", "星期五",
			"星期六", "星期天" };
	private String FinalPAGE;
	private String SERVER;
	private String NUM;
	private AsyncTable TASK;
	private String[] T_1;
	private String[] T_2;
	private String[] T_3;
	private String[] T_4;
	public static ArrayList<Map<String, String>> item_1;
	public static ArrayList<Map<String, String>> item_2;
	public static ArrayList<Map<String, String>> item_3;
	public static ArrayList<Map<String, String>> item_4;

	private ListView lv;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.table_layout);

		dialog = new ProgressDialog(this);
		dialog.setTitle("获取中");
		dialog.setMessage("正在获取");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		SharedPreferences tmp = this
				.getSharedPreferences("LOGIN", MODE_PRIVATE);
		NUM = this.getIntent().getStringExtra("NUM");
		T_1 = new String[DAY_PER_WEEK];
		T_2 = new String[DAY_PER_WEEK];
		T_3 = new String[DAY_PER_WEEK];
		T_4 = new String[DAY_PER_WEEK];
		item_1 = new ArrayList<Map<String, String>>();
		item_2 = new ArrayList<Map<String, String>>();
		item_3 = new ArrayList<Map<String, String>>();
		item_4 = new ArrayList<Map<String, String>>();

		lv = (ListView) this.findViewById(R.id.table_lv_week);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, WEEK);

		lv.setAdapter(adapter);

		if (tmp.getBoolean("TEL", true)) {
			this.SERVER = SharedApplication.HTTP_TEL_ADDR;
		} else {
			this.SERVER = SharedApplication.HTTP_CER_ADDR;
		}

		FinalPAGE = SERVER + TABLE_PAGE + SharedApplication.STU_NUM + NUM
				+ EXTRA;
		Log.d("FINALPAGE", FinalPAGE);
		TASK = new AsyncTable();
		TASK.execute(FinalPAGE);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("POSITION", Integer.toString(position));
				switch (position) {
				case 0:
					Intent intent_1 = new Intent(TableActivity.this,
							DetailActivity.class);
					intent_1.putExtra("day", 0);
					startActivity(intent_1);
					break;
				case 1:
					Intent intent_2 = new Intent(TableActivity.this,
							DetailActivity.class);
					intent_2.putExtra("day", 1);
					startActivity(intent_2);
					break;
				case 2:
					Intent intent_3 = new Intent(TableActivity.this,
							DetailActivity.class);
					intent_3.putExtra("day", 2);
					startActivity(intent_3);
					break;
				case 3:
					Intent intent_4 = new Intent(TableActivity.this,
							DetailActivity.class);
					intent_4.putExtra("day", 3);
					startActivity(intent_4);
					break;
				case 4:
					Intent intent_5 = new Intent(TableActivity.this,
							DetailActivity.class);
					intent_5.putExtra("day", 4);
					startActivity(intent_5);
					break;
				case 5:
					Intent intent_6 = new Intent(TableActivity.this,
							DetailActivity.class);
					intent_6.putExtra("day", 5);
					startActivity(intent_6);
					break;
				case 6:
					Intent intent_7 = new Intent(TableActivity.this,
							DetailActivity.class);
					intent_7.putExtra("day", 6);
					startActivity(intent_7);
					break;
				}
			}
		});

	}

	private class AsyncTable extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String URL = params[0];
			String result = new String();
			Log.d("URL", URL);
			HttpGet httpGet = new HttpGet(URL);
			try {
				HttpResponse scoreResponse = SharedApplication.client
						.execute(httpGet);
				if (scoreResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Log.d("TAG", "ConnectionOK");
					result = EntityUtils.toString(scoreResponse.getEntity());
				} else {
					return "FAIL";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			} finally {
				httpGet.abort();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("FAIL")) {
				dialog.dismiss();
				new AlertDialog.Builder(TableActivity.this).setTitle("错误")
						.setMessage("无法连接服务器").setPositiveButton("确定", null)
						.show();
			} else if (result.equals("") || result == null
					|| result.length() == 0) {
				dialog.dismiss();
				new AlertDialog.Builder(TableActivity.this).setTitle("错误")
						.setMessage("网络错误").setPositiveButton("确定", null)
						.show();
			} else if (result.indexOf("系统警告") != -1) {
				dialog.dismiss();
				Toast.makeText(TableActivity.this, "系统警告，你懂的",
						Toast.LENGTH_LONG).show();
			} else if (result != null) {
				// Log.d("RES",result);
				String LINE_1 = null;
				String LINE_3 = null;
				String LINE_5 = null;
				String LINE_7 = null;
				Pattern l_1 = Pattern.compile(FIRST_LINE);
				Matcher ml_1 = l_1.matcher(result);
				if (ml_1.find()) {
					LINE_1 = ml_1.group();
				}

				Pattern l_2 = Pattern.compile(SECOND_LINE);
				Matcher ml_2 = l_2.matcher(result);
				if (ml_2.find()) {
					LINE_3 = ml_2.group();
				}

				Pattern l_3 = Pattern.compile(THIRD_LINE);
				Matcher ml_3 = l_3.matcher(result);
				if (ml_3.find()) {
					LINE_5 = ml_3.group();
				}

				Pattern l_4 = Pattern.compile(FOURTH_LINE);
				Matcher ml_4 = l_4.matcher(result);
				if (ml_4.find()) {
					LINE_7 = ml_4.group();
				}

				int i = 0;
				Pattern XX = Pattern.compile(TABLE_PATTERN);
				Matcher fin = XX.matcher(LINE_1);
				while (fin.find()) {
					T_1[i] = fin.group(3);
					++i;
				}
				i = 0;
				Pattern XX2 = Pattern.compile(TABLE_PATTERN);
				Matcher fin2 = XX2.matcher(LINE_3);
				while (fin2.find()) {
					T_2[i] = fin2.group(3);
					++i;
				}
				i = 0;
				Pattern XX3 = Pattern.compile(TABLE_PATTERN);
				Matcher fin3 = XX3.matcher(LINE_5);
				while (fin3.find()) {
					T_3[i] = fin3.group(3);
					++i;
				}
				i = 0;
				Pattern XX4 = Pattern.compile(TABLE_PATTERN);
				Matcher fin4 = XX4.matcher(LINE_7);
				while (fin4.find()) {
					T_4[i] = fin4.group(3);
					++i;
				}

				for (int x = 0; x < T_1.length; ++x) {

					String name = "";
					String times = "";
					String time = "";
					String teacher = "";
					String room = "";

					Pattern detail = Pattern.compile(DETAIL_PATTERN);
					Matcher mdetail = detail.matcher(T_1[x]);
					if (mdetail.find()) {
						name = mdetail.group(1);
						times = mdetail.group(2);
						time = mdetail.group(3);
						teacher = mdetail.group(4);
						room = mdetail.group(5);
					} else {
						name = "无";
						times = "无";
						time = "无";
						teacher = "无";
						room = "无";
					}

					Log.d("TEST", name + "~" + times + "~" + time + "~"
							+ teacher + "~" + room);

					Map<String, String> map = new HashMap<String, String>();
					map.put("name", name);
					map.put("times", times);
					map.put("time", time);
					map.put("teacher", teacher);
					map.put("room", room);
					item_1.add(map);
				}

				for (int x = 0; x < T_1.length; ++x) {

					String name = "";
					String times = "";
					String time = "";
					String teacher = "";
					String room = "";

					Pattern detail = Pattern.compile(DETAIL_PATTERN);
					Matcher mdetail = detail.matcher(T_2[x]);
					if (mdetail.find()) {
						name = mdetail.group(1);
						times = mdetail.group(2);
						time = mdetail.group(3);
						teacher = mdetail.group(4);
						room = mdetail.group(5);
					} else {
						name = "无";
						times = "无";
						time = "无";
						teacher = "无";
						room = "无";
					}

					Log.d("TEST", name + "~" + times + "~" + time + "~"
							+ teacher + "~" + room);

					Map<String, String> map = new HashMap<String, String>();
					map.put("name", name);
					map.put("times", times);
					map.put("time", time);
					map.put("teacher", teacher);
					map.put("room", room);
					item_2.add(map);
				}

				for (int x = 0; x < T_1.length; ++x) {

					String name = "";
					String times = "";
					String time = "";
					String teacher = "";
					String room = "";

					Pattern detail = Pattern.compile(DETAIL_PATTERN);
					Matcher mdetail = detail.matcher(T_3[x]);
					if (mdetail.find()) {
						name = mdetail.group(1);
						times = mdetail.group(2);
						time = mdetail.group(3);
						teacher = mdetail.group(4);
						room = mdetail.group(5);
					} else {
						name = "无";
						times = "无";
						time = "无";
						teacher = "无";
						room = "无";
					}

					Log.d("TEST", name + "~" + times + "~" + time + "~"
							+ teacher + "~" + room);

					Map<String, String> map = new HashMap<String, String>();
					map.put("name", name);
					map.put("times", times);
					map.put("time", time);
					map.put("teacher", teacher);
					map.put("room", room);
					item_3.add(map);
				}

				for (int x = 0; x < T_1.length; ++x) {

					String name = "";
					String times = "";
					String time = "";
					String teacher = "";
					String room = "";

					Pattern detail = Pattern.compile(DETAIL_PATTERN);
					Matcher mdetail = detail.matcher(T_4[x]);
					if (mdetail.find()) {
						name = mdetail.group(1);
						times = mdetail.group(2);
						time = mdetail.group(3);
						teacher = mdetail.group(4);
						room = mdetail.group(5);
					} else {
						name = "无";
						times = "无";
						time = "无";
						teacher = "无";
						room = "无";
					}

					Log.d("TEST", name + "~" + times + "~" + time + "~"
							+ teacher + "~" + room);

					Map<String, String> map = new HashMap<String, String>();
					map.put("name", name);
					map.put("times", times);
					map.put("time", time);
					map.put("teacher", teacher);
					map.put("room", room);
					item_4.add(map);
				}
				// 行
				dialog.dismiss();
				Toast.makeText(TableActivity.this, "查询成功", Toast.LENGTH_SHORT)
						.show();
			}
			dialog.dismiss();
			// ???
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
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
