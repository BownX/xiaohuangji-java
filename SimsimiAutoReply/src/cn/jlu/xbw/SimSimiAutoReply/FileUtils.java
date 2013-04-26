package cn.jlu.xbw.SimSimiAutoReply;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件是用来存已经处理过的Noti的nid
 * @author Mr.Xu
 *
 */
public class FileUtils {

	private final static String DEFAULT_FILE = "data.txt";

	public static List<String> getHandledList() throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(DEFAULT_FILE));
		String data = br.readLine();
		while (data != null) {
			list.add(data);
			data = br.readLine();
		}
		return list;
	}

	public static void WriteHandledList(List<String> list) throws IOException {
		PrintWriter output = new PrintWriter(new FileWriter(DEFAULT_FILE));
		for (String s : list) {
			output.println(s);
		}
		output.close();
	}

}
