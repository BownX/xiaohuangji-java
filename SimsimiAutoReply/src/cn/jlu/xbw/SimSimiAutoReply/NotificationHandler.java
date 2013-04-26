package cn.jlu.xbw.SimSimiAutoReply;
import java.io.IOException;
import java.util.List;

/**
 * 处理notifications
 * 
 * @author Mr.Xu
 * 
 */
public class NotificationHandler implements Runnable {

	public static final int TIME_CYCLE = 15000;
	
	RenRen renren;
	boolean isRunning;
	List<String> handled;

	public NotificationHandler(RenRen r, boolean ir) {
		this.renren = r;
		this.isRunning = ir;
		try {
			handled = FileUtils.getHandledList();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			List<Notifications> list = JsonUtils.generateNotifications(renren
					.getNotificationsJson());
			System.out.println("NOTIFICATION数 ：" + list.size());
			for (Notifications noti : list) {
				if (!isHandled(noti)) {
					HandlerNoti(noti, renren);
					handled.add(noti.getNid());
				}
			}

			try {
				Thread.sleep(TIME_CYCLE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 停止时将处理过的nid写入文件
		try {
			FileUtils.WriteHandledList(handled);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	private void HandlerNoti(Notifications noti, RenRen renren) {
		String str = JsonUtils.getContentFromReplies(renren
				.getDoingCommentsJson(noti.getDoingid(), noti.getUserid()),
				noti.getReplyid());
		String reply = Simsimi.talk(msgFormat(str));
		System.out.println("当前处理 ：nid:" + noti.getNid() + " doingid:"
				+ noti.getDoingid() + " content:" + msgFormat(str) + " reply:" + replyFormat(reply));
		if(renren.addComments(noti.getDoingid(), noti.getUserid(), reply)){
			System.out.println("回复 " +  noti.getNid() + "成功！");
		}else{
			System.out.println("回复 " +  noti.getNid() + "失败！");
		}
	}
	
	private String msgFormat(String msg){
		msg = msg.replaceAll("<(.*?)>", "");		//去掉@人的话会有的a标签
		msg = msg.replaceAll("回复(.*?):", "");
		msg = msg.replaceAll("回复(.*?)：", "");		//不确定是全角还是半角的冒号，都匹配一下吧
		msg = msg.replace("+","");				//剩下就是去掉一些放入url里面可能出错的字符
		msg = msg.replace(" ","");
		msg = msg.replace("/","");
		msg = msg.replace("?","");
		msg = msg.replace("%","");
		msg = msg.replace("#","");
		msg = msg.replace("&","");
		msg = msg.replace("=",""); 
		return msg;
	}
	
	private String replyFormat(String reply){
		return reply.replace("黄鸡", "排长");
	}

	private boolean isHandled(Notifications nf) {
		for (String s : handled) {
			if (s.equals(nf.getNid()))
				return true;
		}
		return false;
	} 
}
