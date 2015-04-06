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
}