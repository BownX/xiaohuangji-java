package cn.jlu.xbw.SimSimiAutoReply;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class Simsimi {

	// public static final String url_base =
	// "http://0.hustac.duapp.com/simsimi.php?msg=";
	//
	// public static String talkToSimsimi(String message){
	// BufferedReader in = null;
	//
	// String content = null;
	// try {
	// // 定义HttpClient
	// HttpClient client = new DefaultHttpClient();
	// // 实例化HTTP方法
	// HttpGet request = new HttpGet();
	// request.setURI(new URI(url_base + message));
	// HttpResponse response = client.execute(request);
	//
	// in = new BufferedReader(new InputStreamReader(response.getEntity()
	// .getContent()));
	// StringBuffer sb = new StringBuffer("");
	// String line = "";
	// String NL = System.getProperty("line.separator");
	// while ((line = in.readLine()) != null) {
	// sb.append(line + NL);
	// }
	// in.close();
	// content = sb.toString();
	// } finally {
	// if (in != null) {
	// try {
	// in.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return content;
	// }
	// }

	// 不用别人的API 了， 自己抓包设个Header就ok -_-//
	public static String talk(String msg) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://www.simsimi.com/func/req?msg="
				+ msg + "&lc=ch");
		httpget.setHeader("Accept",
				"application/json, text/javascript, */*; q=0.01");
		httpget.setHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		httpget.setHeader("Connection", "keep-alive");
		httpget.setHeader("Content-Type", "application/json; charset=utf-8");
		httpget.setHeader(
				"Cookie",
				"sagree=true; selected_nc=ch; JSESSIONID=C1D3ECE57CB583F1AFA7C697FA533E87; __utma=119922954.587639896.1357958085.1357976113.1358233074.4; __utmb=119922954.6.9.1358233117598; __utmc=119922954; __utmz=119922954.1357976113.2.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided)");
		httpget.setHeader("Host", "www.simsimi.com");
		httpget.setHeader("Referer", "http://www.simsimi.com/talk.htm?lc=ch");
		httpget.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER");
		httpget.setHeader("X-Requested-With", "XMLHttpRequest");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = "";
		try {
			responseBody = client.execute(httpget, responseHandler);
		} catch (Exception e) {
			e.printStackTrace();
			responseBody = null;
		} finally {
			httpget.abort();
		}
		Map reply = JSONObject.fromObject(responseBody);
		int result = (Integer) reply.get("result");
		if (result == 100)
			return (String) reply.get("response");
		else
			return "死鸡了！！！";
	}

}
