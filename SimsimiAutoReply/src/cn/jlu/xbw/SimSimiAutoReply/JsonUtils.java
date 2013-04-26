package cn.jlu.xbw.SimSimiAutoReply;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtils {

	/**
	 * 返回的json -> Notification
	 * 
	 * @param jsonstr
	 * @return
	 */
	public static List<Notifications> generateNotifications(String jsonstr) {
		List<Notifications> list = new ArrayList<Notifications>();
		JSONArray arr = JSONArray.fromObject(jsonstr);
		int size = arr.size();
		for (int i = 0; i < size; i++) {
			Map map = arr.getJSONObject(i);
			Notifications ntf = new Notifications();
			String source = (String) map.get("source");
			String sourcesplit[] = source.split("-");
			int type = Integer.parseInt(sourcesplit[0]);
			if (!(type == 16 || type == 196)) // 16是被回复，196是被@。
			{
				continue;
			}
			ntf.setType(type);
			Pattern pattern = Pattern.compile("repliedId=([1-9][0-9]*)");
			Matcher m = pattern.matcher((String) map.get("content"));
			if (m.find()) {
				ntf.setReplyid(m.group(1));
			}
			ntf.setNid((String) map.get("nid"));
			Pattern pattern2 = Pattern.compile("uid=([1-9][0-9]*)");
			Matcher m2 = pattern2.matcher((String) map.get("callback"));
			if (m2.find()) {
				ntf.setUserid(m2.group(1));
			}
			ntf.setDoingid(sourcesplit[1]);
			ntf.setSourceid(sourcesplit[1]);
			list.add(ntf);
		}
		return list;
	}
	
	/**
	 * 从json值中取出
	 * @param jsonstr
	 * @return
	 */
	public static String getContentFromReplies(String jsonstr , String replyid){
		Map map = JSONObject.fromObject(jsonstr);
		JSONArray arr = (JSONArray) map.get("replyList");
		int size = arr.size();
		for(int i = 0 ; i < size ; i++){
			Map rep = arr.getJSONObject(i);
			String rid = new BigDecimal(rep.get("id").toString()).toPlainString();
			if(rid.equals(replyid)){
				return (String)rep.get("replyContent");
			}
		}
		return "NULL";
	}

}
