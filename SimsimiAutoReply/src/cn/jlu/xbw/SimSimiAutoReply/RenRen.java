package cn.jlu.xbw.SimSimiAutoReply;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 利用httpclient操作人人网 比如登陆，发状态，模拟你访问任何人主页等等
 * 
 */
public class RenRen {
	private String userName = "";
	private String password = "";
	private String requestToken = null;
	public static final String rtk = "5571e2a1";

	private static String redirectURL = "http://www.renren.com/home";
	private static String renRenLoginURL = "http://www.renren.com/PLogin.do";
	private HttpResponse response;
	private DefaultHttpClient httpclient = null;

	public RenRen(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 登陆
	 * 
	 * @return
	 */
	public boolean login() {
		if (httpclient != null) {
			return true;
		}
		httpclient = null;
		httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(renRenLoginURL);
		// All the parameters post to the web site
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("origURL", redirectURL));
		nvps.add(new BasicNameValuePair("domain", "renren.com"));
		nvps.add(new BasicNameValuePair("autoLogin", "true"));
		nvps.add(new BasicNameValuePair("formName", ""));
		nvps.add(new BasicNameValuePair("method", ""));
		nvps.add(new BasicNameValuePair("submit", "登录"));
		nvps.add(new BasicNameValuePair("email", userName));
		nvps.add(new BasicNameValuePair("password", password));
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			httpost.abort();
		}

		String redirectLocation = getRedirectLocation();
		if (redirectLocation != null) {
			// 跳到首页，登录完成
			getRequestToken(getText(redirectLocation));
		}

		return true;
	}

	private String getRedirectLocation() {
		Header locationHeader = response.getFirstHeader("Location");
		if (locationHeader == null) {
			return null;
		}
		return locationHeader.getValue();
	}

	private String getText(String redirectLocation) {
		HttpGet httpget = new HttpGet(redirectLocation);
		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}

	/*
	 * 获取提醒。(@,回复等等)
	 */
	public String getNotificationsJson() {
		HttpGet httpget = new HttpGet(
				"http://notify.renren.com/rmessage/get?getbybigtype=1&bigtype=1&limit=999&begin=0&view=16&random="
						+ (int) (Math.random() * 10000));
		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}

		return responseBody;
	}

	// GET. 获取@自己的那个人发布的所有状态（包含有@自己的那条）。
	public String getDoingsJson(String uid) {
		HttpGet httpget = new HttpGet(
				"http://status.renren.com/GetSomeomeDoingList.do?userId=" + uid
						+ "&curpage=0");
		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		return responseBody;
	}

	/**
	 * 获取某条状态的回复列表
	 * 
	 * @return
	 */
	public String getDoingCommentsJson(String id_doing, String id_owner) {
		HttpPost httpost = new HttpPost(
				"http://status.renren.com/feedcommentretrieve.do");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("doingId", id_doing));
		nvps.add(new BasicNameValuePair("source", id_doing));
		nvps.add(new BasicNameValuePair("owner", id_owner));
		nvps.add(new BasicNameValuePair("t", "3"));
		nvps.add(new BasicNameValuePair("requestToken", requestToken));
		nvps.add(new BasicNameValuePair("feedId", ""));
		nvps.add(new BasicNameValuePair("_rtk", rtk));
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
			String results = EntityUtils.toString(response.getEntity());
			// 这里返回的是unicode编码的
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		} finally {
			httpost.abort();
		}
	}

	// 直接状态中@的，回复那个状态
	public boolean addComments(String doing_id, String owner_id, String msg) {
		if (login()) {
			HttpPost post = new HttpPost(
					"http://status.renren.com/feedcommentreply.do");
			List<NameValuePair> cp = new ArrayList<NameValuePair>();
			cp.add(new BasicNameValuePair("c", msg));
			cp.add(new BasicNameValuePair("t", "3"));
			cp.add(new BasicNameValuePair("rpLayer", "0"));
			cp.add(new BasicNameValuePair("source", doing_id));
			cp.add(new BasicNameValuePair("owner", owner_id));
			cp.add(new BasicNameValuePair("requestToken", requestToken));
			cp.add(new BasicNameValuePair("feedId", ""));
			cp.add(new BasicNameValuePair("_rtk", rtk)); // 抓包得到的rtk值，似乎会随着登陆用户不同而变动。没弄清变动规律

			try {
				post.setEntity(new UrlEncodedFormEntity(cp, HTTP.UTF_8));
				response = httpclient.execute(post);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				post.abort();
			}
		}
		return response.toString().contains("200 OK");
	}
 

	/**
	 * 访问
	 * 
	 * @param id
	 * @return
	 */
	public boolean visit(int id) {

		HttpPost post = new HttpPost(
				"http://www.renren.com/profile.do?portal=profileFootprint&ref=profile_footprint&id="
						+ id);
		try {
			response = httpclient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("访问成功！id号：" + id);
		post.abort();
		return true;
	}

	/**
	 * 获取requestToken。 在登陆成功后的主页面中用正则获取
	 * 
	 * @param homeHtml
	 * @return
	 */
	private void getRequestToken(String homeHtml) {
		Pattern pattern = Pattern.compile("requesttoken=(-?[1-9][0-9]*)");
		Matcher m = pattern.matcher(homeHtml);
		if (m.find()) {
			requestToken = m.group(1);
		} else {
			System.out.println("获取requestToken失败！");
		}
	}

 
 
}