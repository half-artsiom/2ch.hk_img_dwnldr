import java.io.*;
import java.util.*;
import java.net.URL;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ImgDownloader {
	private static Scanner userMode;
	private static Scanner inputURL;
	private static Scanner inputBoard;

	public static void main(String[] args) throws IOException {
		boolean answer;
		do {
		userMode = new Scanner(System.in);
		System.out.print("Choose mode: download all threads on first page (press X/x) or download thread(Y/y): ");
		String userModeAnswer = userMode.next();
			switch(userModeAnswer.toLowerCase()) {
				case "x":
					getBoardList();
					answer = true;
					break;
				case "y":
					downloadThreadMenu();
					answer = true;
					break;
				default:
					System.out.println("Please choose mode (answer X or Y)");
					answer = false;
					break;
			}
		}
		while (!answer);
	}
	
	public static void downloadThreadMenu() throws IOException {
		inputURL = new Scanner(System. in );
		System.out.print("Enter URL: ");
		String urlPath = inputURL.next();
		List < String > imgsURL = new ArrayList < String > ();
		imgsURL = getImgUrlPath( urlPath );
		for (int i = 0; i < imgsURL.size(); i++) {
			saveImage(imgsURL.get(i).toString(), imgsURL.get(i).toString().substring(imgsURL.get(i).toString().lastIndexOf('/')));
			if (i == imgsURL.size() - 1) {
				System.out.println("All files were saved");
			}
		}
	}

	public static void saveImage(String imageURL, String fileName) throws IOException {
		String destination = "D:\\2ch" + 
					imageURL.substring(imageURL.indexOf("hk")+2, imageURL.indexOf("src")) +
					imageURL.substring(imageURL.indexOf("src")+4, imageURL.lastIndexOf('/'));
		destination = destination.replace('/', '\\');
		File directory = new File(destination);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		URL url = new URL(imageURL);
		try {
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
		catch (java.io.FileNotFoundException e) {
			System.out.println("File: " + imageURL + " not found!");
		}
	}

	public static void getBoardList() throws IOException {
		inputBoard = new Scanner(System. in );
		System.out.print("Input board name (shortened version): ");
		String boardName = inputBoard.next().toLowerCase();
		boardName = "/" + boardName + "/";
		Document doc = Jsoup.connect("https://2ch.hk/").get();
		Elements boards = doc.select("optgroup option[value]");
		List < String > boardNames = new ArrayList < String > ();
		for (Element src: boards) {
			boardNames.add(src.attr("value"));
		}
		if (boardNames.contains(boardName)) {
			List < String > threadList = new ArrayList < String > (); 
			List < String > imgsURL = new ArrayList < String > ();
			String threadURL;
			threadList = getThreadList(boardName);
			for (int i = 0; i < threadList.size(); i++) {
				threadURL = "https://2ch.hk" + boardName + "res/" + threadList.get(i).toString().replace("thread-", "") + ".html";
				imgsURL = getImgUrlPath(threadURL);
				for (int j = 0; j < imgsURL.size(); j++) {
					saveImage(imgsURL.get(j).toString(), imgsURL.get(j).toString().substring(imgsURL.get(j).toString().lastIndexOf('/')));
				}
			}
		}
		else {
			System.out.println("There is no such board");
		}

	}
	
	public static List< String > getImgUrlPath(String threadURL) throws IOException {
		List < String > imgPathList = new ArrayList < String > ();
		Document doc = Jsoup.connect(threadURL).get();
		Elements imgs = doc.select("figcaption.file-attr > a.desktop");
		for (Element src: imgs) {
			imgPathList.add(src.attr("abs:href"));
		}
		return imgPathList;
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