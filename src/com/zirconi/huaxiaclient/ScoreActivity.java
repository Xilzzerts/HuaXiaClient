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
import java.util.List;
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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ScoreActivity extends Activity {
	private static final String TAG = ScoreActivity.class.getSimpleName();
	private static final String PATTERN = "<td>(\\d{4}-\\d{4})</td><td>(\\d{1})</td><td>(.+)</td><td>(.+)</td><td>(.+)</td><td>(.+)</td><td>(.+)</td><td>(.+)</td><td>(.+)</td><td>(.*)</td><td>(.*)</td>";
	private String SCORE_PAGE;
	private String NUM;
	private ProgressDialog dialog;
	private ListView lv_score;
	private List<String> year;
	private List<String> term;
	private List<String> name;
	private List<String> type;
	private List<String> teacher;
	private List<String> test;
	private List<String> score;
	private List<String> MKEscore;
	private List<String> rescore;
	private List<String> credit;
	private ArrayList<Map<String, String>> ITEM_INFO;
	private static final String[] FROM = { "year", "term", "name", "type",
			"teacher", "test", "score", "MKEscore", "rescore", "credit" };
	private static final int[] TO = { R.id.it_year, R.id.it_term, R.id.it_name,
			R.id.it_type, R.id.it_teacher, R.id.it_test, R.id.it_score,
			R.id.it_mkescore, R.id.it_rescore, R.id.it_credit };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_layout);
		NUM = this.getIntent().getStringExtra("NUM");
		SharedPreferences temp = this.getSharedPreferences("LOGIN",
				MODE_PRIVATE);
		lv_score = (ListView) this.findViewById(R.id.score_lv_score);
		dialog = new ProgressDialog(this);
		dialog.setTitle("获取中");
		dialog.setMessage("正在获取");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		ITEM_INFO = new ArrayList<Map<String, String>>();

		year = new ArrayList<String>();
		term = new ArrayList<String>();
		name = new ArrayList<String>();
		type = new ArrayList<String>();
		teacher = new ArrayList<String>();
		test = new ArrayList<String>();
		score = new ArrayList<String>();
		MKEscore = new ArrayList<String>();
		rescore = new ArrayList<String>();
		credit = new ArrayList<String>();

		if (temp.getBoolean("TEL", true)) {
			this.SCORE_PAGE = SharedApplication.HTTP_TEL_ADDR;
		} else {
			this.SCORE_PAGE = SharedApplication.HTTP_CER_ADDR;
		}

		new AsyncScore().execute(SCORE_PAGE);
	}

	private class AsyncScore extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String URL = params[0] + SharedApplication.QUERY_SCORE_PAGE
					+ ScoreActivity.this.NUM;
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
				new AlertDialog.Builder(ScoreActivity.this).setTitle("错误")
						.setMessage("无法连接服务器").setPositiveButton("确定", null)
						.show();
			} else if (result.equals("") || result == null
					|| result.length() == 0) {
				dialog.dismiss();
				new AlertDialog.Builder(ScoreActivity.this).setTitle("错误")
						.setMessage("网络错误").setPositiveButton("确定", null)
						.show();
			} else if (result.indexOf("系统警告") != -1) {
				dialog.dismiss();
				Toast.makeText(ScoreActivity.this, "系统警告，你懂的",
						Toast.LENGTH_LONG).show();
			} else if (result != null) {
				dialog.dismiss();
				Toast.makeText(ScoreActivity.this, "查询成功", Toast.LENGTH_SHORT)
						.show();
				Log.d(TAG, "GetOK");
				Log.d(TAG, ScoreActivity.this.SCORE_PAGE);

				Pattern pattern = Pattern.compile(PATTERN);
				Matcher matcher = pattern.matcher(result);
				while (matcher.find()) {
					String str_year = matcher.group(1);
					String str_term = matcher.group(2);
					String str_name = matcher.group(3);
					String str_type = matcher.group(4);
					String str_teacher = matcher.group(5);
					String str_test = matcher.group(6);
					String str_score = matcher.group(7);
					String str_MKEscore = matcher.group(8);
					if (str_MKEscore.equals("&nbsp;"))
						str_MKEscore = "无";
					String str_rescore = matcher.group(9);
					if (str_rescore.equals("&nbsp;"))
						str_rescore = "无";
					String str_credit = matcher.group(11);
					year.add(str_year);
					term.add(str_term);
					name.add(str_name);
					type.add(str_type);
					teacher.add(str_teacher);
					test.add(str_test);
					score.add(str_score);
					MKEscore.add(str_MKEscore);
					rescore.add(str_rescore);
					credit.add(str_credit);
				}

				for (int i = 0; i < year.size(); ++i) {
					Map<String, String> tmp = new HashMap<String, String>();
					tmp.put("year", year.get(i));
					tmp.put("term", term.get(i));
					tmp.put("name", name.get(i));
					tmp.put("type", type.get(i));
					tmp.put("teacher", teacher.get(i));
					tmp.put("test", test.get(i));
					tmp.put("score", score.get(i));
					tmp.put("MKEscore", MKEscore.get(i));
					tmp.put("rescore", rescore.get(i));
					tmp.put("credit", credit.get(i));
					ITEM_INFO.add(tmp);
				}
				SimpleAdapter adapter = new SimpleAdapter(ScoreActivity.this,
						ITEM_INFO, R.layout.score_item_layout, FROM, TO);
				lv_score.setAdapter(adapter);
			}
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
							"VERSION:0.01 BETA\nJust for fun!\nAuthor:Zirconi\nMail:Geekkou@gmail.com")
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
