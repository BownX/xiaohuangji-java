package cn.jlu.xbw.SimSimiAutoReply;
import java.util.Scanner;

public class Main {// 入口
	public static void main(String[] args) {

		RenRen rr = null;
		rr = new RenRen("YOUR EMAIL", "YOUR PASSWORD");
		rr.login();
		
		NotificationHandler handler = new NotificationHandler(rr, true);
		new Thread(handler).start();
		System.out.println("已启动： 按任意键停止。");
		//一定！ ！ 要 ！！ 用按键的方式关闭！！ 直接点Eclipse的红点终止的话，已回复的id数据无法写入文件，下次的登陆会回复所有
		Scanner s = new Scanner(System.in);
		if (s.nextLine() != null) {
			handler.setRunning(false);
		}
	}
}
