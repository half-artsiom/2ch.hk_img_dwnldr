import java.io.*;
import java.util.*;
import java.net.URL;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.helper.Validate;

public class ImgDownloader {
	public static void main(String[] args) throws IOException {
		//check if board exist
		Scanner inputBoard = new Scanner(System. in );
		System.out.print("Input board name (shortened version): ");
		getBoardList(inputBoard.next());
		
		//input thread URL
		Scanner inputURL = new Scanner(System. in );
		System.out.print("Enter URL: ");
		String urlPath = inputURL.next();
		String tempURL;
		List < String > imgsURL = new ArrayList < String > ();
		Document doc = Jsoup.connect(urlPath).get();
		Elements imgs = doc.select("figcaption.file-attr > a.desktop");
		for (Element src: imgs) {
			imgsURL.add(src.attr("abs:href"));
		}
		for (int i = 0; i < imgs.size(); i++) {
			tempURL = imgsURL.get(i).toString();
			saveImage(tempURL, "D:\\2ch", tempURL.substring(tempURL.lastIndexOf('/')));
			if (i == imgs.size() - 1) {
				System.out.println("All files were saved");
			}
		}
	}

	public static void saveImage(String imageURL, String destination, String fileName) throws IOException {
		File directory = new File(destination);
		if (!directory.exists()) {
			directory.mkdir();
		}
		URL url = new URL(imageURL);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destination + fileName);
		byte[] b = new byte[2048];
		int length;
		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		is.close();
		os.close();
		System.out.println("File: " + imageURL + " saved to: " + destination);
	}
	
	public static void getBoardList(String boardName) throws IOException {
		boardName = "/" + boardName + "/";
		Document doc = Jsoup.connect("https://2ch.hk/").get();
		Elements boards = doc.select("optgroup option[value]");
		List < String > boardNames = new ArrayList < String > ();
		for (Element src: boards) {
			boardNames.add(src.attr("value"));
		}
		
		if (boardNames.contains(boardName)) {
			List < String > threadList = new ArrayList < String > (); 
			threadList = getThreadList(boardName);
			for (int i = 0; i < threadList.size(); i++) {
				System.out.println("https://2ch.hk" + boardName + "res/" + threadList.get(i).toString().replace("thread-", "") + ".html");
			}
		}
		else {
			System.out.println("There is no such board");
		}

	}
	
	public static List< String > getThreadList(String boardName) throws IOException {
		Document doc = Jsoup.connect("https://2ch.hk" + boardName).get();
		Elements threads = doc.select("div.thread");
		List < String > threadList = new ArrayList < String > ();
		for (Element src: threads) {
			threadList.add(src.attr("id"));
		}
		return threadList;		
	}
}