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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getSimpleName();

	private EditText login_et_username;
	private EditText login_et_password;
	private CheckBox login_cb_rempwd;
	private RadioButton login_rb_tel;
	private RadioButton login_rb_cer;
	private Button login_btn_submit;
	private Button login_btn_cancel;
	private RadioGroup login_rg_select;
	private SharedApplication application;
	private SharedPreferences pref;
	public static String PAGE;
	public static String INDEXPAGE;
	private Boolean runFlag;
	private String INFO;
	private String ERROR;
	private TextView AUTHOR;
	private static final String AUT = "By Zirconi";

	private ProgressDialog t;
	private Boolean isNetwork;
	private Async TASK;

	private static final String ERROR_PATTERN = "alert\\('(.+)'\\)";
	private static final String NAME_PATTERN = "<span id=\"lbXM\">(.+)</span>";
	private static final String MAJOR_PATTERN = "<span id=\"lbZYMC\">(.+)</span>";
	private static final String CLASSNUM_PATTERN = "<span id=\"lbBJMC\">(.+)</span>";
	private static final String NUM_PATTERN = "<span id=\"lbXH\">(.+)</span>";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		application = (SharedApplication) this.getApplication();
		pref = getSharedPreferences("LOGIN", MODE_PRIVATE);
		PAGE = SharedApplication.TEL_PAGE;
		INDEXPAGE = SharedApplication.HTTP_TEL_ADDR;
		runFlag = false;
		AUTHOR = (TextView) this.findViewById(R.id.author);
		AUTHOR.setText(AUT);

		this.login_et_username = (EditText) this
				.findViewById(R.id.login_et_username);
		this.login_et_password = (EditText) this
				.findViewById(R.id.login_et_password);
		this.login_cb_rempwd = (CheckBox) this
				.findViewById(R.id.login_cb_rempwd);
		this.login_rb_tel = (RadioButton) this.findViewById(R.id.login_rb_tel);
		this.login_rb_cer = (RadioButton) this.findViewById(R.id.login_rb_cer);
		this.login_btn_submit = (Button) this
				.findViewById(R.id.login_btn_submit);
		this.login_btn_cancel = (Button) this
				.findViewById(R.id.login_btn_cancel);
		this.login_rg_select = (RadioGroup) this
				.findViewById(R.id.login_rg_select);

		if (pref.getBoolean("TEL", true)) {
			this.login_rb_tel.setChecked(true);
			PAGE = SharedApplication.TEL_PAGE;
			INDEXPAGE = SharedApplication.HTTP_TEL_ADDR;
		} else {
			this.login_rb_cer.setChecked(true);
			PAGE = SharedApplication.CER_PAGE;
			INDEXPAGE = SharedApplication.HTTP_CER_ADDR;
		}

		if (pref.getBoolean("PASSWORD", false)) {
			this.login_cb_rempwd.setChecked(true);
		} else {
			this.login_cb_rempwd.setChecked(false);
		}

		if (this.login_cb_rempwd.isChecked()
				&& !pref.getString("user", "").equals("")) {
			this.login_et_username.setText(pref.getString("user", ""));
			this.login_et_password.setText(pref.getString("pwd", ""));
		}

		this.login_cb_rempwd
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							Editor tmp = pref.edit();
							tmp.putBoolean("PASSWORD", true);
							tmp.commit();
						} else {
							Editor tmp = pref.edit();
							tmp.putBoolean("PASSWORD", false);
							tmp.putString("user", "");
							tmp.putString("pwd", "");
							tmp.commit();
						}
					}
				});

		this.login_rg_select
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.login_rb_cer) {
							Editor tmp = pref.edit();
							tmp.putBoolean("TEL", false);
							tmp.commit();
							PAGE = SharedApplication.CER_PAGE;
							INDEXPAGE = SharedApplication.HTTP_CER_ADDR;
							Log.d(TAG, "SLELCT CER " + PAGE);
						} else {
							Editor tmp = pref.edit();
							tmp.putBoolean("TEL", true);
							tmp.commit();
							PAGE = SharedApplication.TEL_PAGE;
							INDEXPAGE = SharedApplication.HTTP_TEL_ADDR;
							Log.d(TAG, "SLELCT TEL " + PAGE);
						}
					}
				});

		this.login_btn_cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});

		this.login_btn_submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (login_et_username.getText().toString().equals("")
						|| login_et_password.getText().toString().equals("")) {
					Toast.makeText(LoginActivity.this, "学号和密码不能为空",
							Toast.LENGTH_LONG).show();
				} else {
					if (login_cb_rempwd.isChecked()) {
						Editor tmp = pref.edit();
						tmp.putString("user", login_et_username.getText()
								.toString());
						tmp.putString("pwd", login_et_password.getText()
								.toString());
						tmp.commit();

						isNetwork = SharedApplication
								.networkIsAvailable(LoginActivity.this);
						if (isNetwork) {
							if (runFlag == false) {
								t = new ProgressDialog(LoginActivity.this);
								t.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								t.setTitle("正在连接");
								t.setMessage("请稍候···");
								t.show();
								TASK = new Async();
								TASK.execute(PAGE);
								runFlag = true;
							}
						} else {
							Toast.makeText(LoginActivity.this, "无网络连接",
									Toast.LENGTH_LONG).show();
						}
					} else {
						// 不为空，不记住密码
						isNetwork = SharedApplication
								.networkIsAvailable(LoginActivity.this);
						if (isNetwork) {
							if (runFlag == false) {
								t = new ProgressDialog(LoginActivity.this);
								t.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								t.setTitle("正在连接");
								t.setMessage("请稍候···");
								t.show();
								TASK = new Async();
								TASK.execute(PAGE);
								runFlag = true;
							}
						} else {
							Toast.makeText(LoginActivity.this, "无网络连接",
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		});
		Log.i(TAG, "ONCREATE");
	}

	private class Async extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String URL = params[0];
			String result = new String();

			HttpGet httpGet = new HttpGet(URL);
			try {
				HttpResponse getResponse = SharedApplication.client
						.execute(httpGet);
				if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Log.d("ConnectionOK", "OK");
					LoginActivity.this.ERROR = EntityUtils.toString(getResponse
							.getEntity());
				} else {
					return "FAIL";
					//
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			} finally {
				httpGet.abort();
			}

			// ///////////////////

			HttpPost httpPost = new HttpPost(URL);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair(SharedApplication.VIEWSTATE,
					SharedApplication.VIEWSTATE_KEY));
			param.add(new BasicNameValuePair(SharedApplication.USERNAME,
					login_et_username.getText().toString()));
			param.add(new BasicNameValuePair(SharedApplication.PASSWORD,
					login_et_password.getText().toString()));
			param.add(new BasicNameValuePair(SharedApplication.IDENTITY, "学生"));
			param.add(new BasicNameValuePair(SharedApplication.IMG_X, "0"));
			param.add(new BasicNameValuePair(SharedApplication.IMG_Y, "0"));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, "GB2312"));
				HttpResponse httpResponse = SharedApplication.client
						.execute(httpPost);

				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					result = EntityUtils.toString(httpResponse.getEntity());

				} else {
					return "FAIL";
					//
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			} finally {
				httpPost.abort();
			}

			HttpGet GetINFO = new HttpGet(LoginActivity.INDEXPAGE
					+ SharedApplication.INFO_PAGE);
			try {
				HttpResponse INFOResponse = SharedApplication.client
						.execute(GetINFO);
				if (INFOResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Log.d("ConnectionOK", "ok");
					LoginActivity.this.INFO = EntityUtils.toString(INFOResponse
							.getEntity());
				} else {
					return "FAIL";
					//
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			} finally {
				httpGet.abort();
			}

			// ////////////////////
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			Pattern p = Pattern.compile(ERROR_PATTERN);
			Matcher m = p.matcher(result);
			if (m.find()) {
				Log.d("ERROR", m.group(1));
				t.dismiss();
				new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
						.setMessage(m.group(1)).setPositiveButton("确定", null)
						.show();
				runFlag = false;
			} else if (result.equals("FAIL")) {
				t.dismiss();
				new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
						.setMessage("无法连接服务器").setPositiveButton("确定", null)
						.show();
				runFlag = false;
			} else if (result.length() == 0 || result == null) {
				t.dismiss();
				new AlertDialog.Builder(LoginActivity.this).setTitle("错误")
						.setMessage("网络错误").setPositiveButton("确定", null)
						.show();
				runFlag = false;
			} else if (ERROR.indexOf("系统警告") != -1
					|| result.indexOf("系统警告") != -1
					|| INFO.indexOf("系统警告") != -1) {
				t.dismiss();
				Toast.makeText(LoginActivity.this, "系统警告，你懂的",
						Toast.LENGTH_LONG).show();
				runFlag = false;
			} else if (result != null) {
				t.dismiss();
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG)
						.show();
				Log.d("LOGIN", result);
				runFlag = false;
				Pattern p1 = Pattern.compile(NAME_PATTERN);
				Matcher m1 = p1.matcher(INFO);
				Pattern p2 = Pattern.compile(MAJOR_PATTERN);
				Matcher m2 = p2.matcher(INFO);
				Pattern p3 = Pattern.compile(CLASSNUM_PATTERN);
				Matcher m3 = p3.matcher(INFO);
				Pattern p4 = Pattern.compile(NUM_PATTERN);
				Matcher m4 = p4.matcher(INFO);
				if (m1.find() && m2.find() && m3.find() && m4.find()) {
					Bundle bundle = new Bundle();
					bundle.putString("NAME", m1.group(1));
					bundle.putString("MAJOR", m2.group(1));
					bundle.putString("CLASSNUM", m3.group(1));
					bundle.putString("NUM", m4.group(1));
					Log.d("INFO",
							m1.group(1) + m2.group(1) + m3.group(1)
									+ m4.group(1));
					Log.d("ADDR", INDEXPAGE);
					Intent intent = new Intent(LoginActivity.this,
							SelectActivity.class);
					intent.putExtra("INFORMATION", bundle);
					LoginActivity.this.startActivity(intent);
				}
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
