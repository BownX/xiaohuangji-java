package cn.jlu.xbw.SimSimiAutoReply;
public class Notifications {

	private String nid;
	private String userid;
	private String doingid;
	private String sourceid;
	private String replyid;
	private int type;

	public String getReplyid() {
		return replyid;
	}

	public void setReplyid(String replyid) {
		this.replyid = replyid;
	}

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public Notifications() {

	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDoingid() {
		return doingid;
	}

	public void setDoingid(String doingid) {
		this.doingid = doingid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
