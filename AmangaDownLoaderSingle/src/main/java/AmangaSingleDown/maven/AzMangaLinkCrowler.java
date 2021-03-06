/**
 *
 */
package AmangaSingleDown.maven;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author sigre
 *
 */
public class AzMangaLinkCrowler {

	//Aマンガのタイムアウト回数
	private static int timeoutCountAz = 0;
	//Uploadedのタイムアウト回数
	private static int timeoutCountUp = 0;
	//AマンガにUploadedのURLがない場合、Uploadedが死んでいる場合などにAマンガのリンクが入る
	private static List<String> uncorrectList = new ArrayList<String>();

	/**
	 * マンガ情報のリストを取得。これをメインに使用してください。
	 * マンガ情報：Aマンガのマンガ名、Uploadedリンク、Uploadedファイル名のリスト
	 * @param category Aマンガのカテゴリ
	 * @param start 開始ページ番号
	 * @param end 終了ページ番号
	 * @return
	 * @throws IOException ネット接続不可によるURLへのアクセス失敗
	 * @throws InterruptedException Waitが吐く例外。謎。
	 */
	public List<List<String>> getSingleMangaList(String category, int start, int end,gamenDTO DTO)
			throws IOException, InterruptedException, IllegalArgumentException {

		if (start > end) {
			System.out.println("異常終了；開始ページと終了ページの指定が逆ではないですか？");
			return new ArrayList<List<String>>();
		}

		List<List<String>> result = new ArrayList<List<String>>();
		String strUploaderName = "Uploaded";

		//URLを1ページから順番に処理する。
		for (int i = start; i <= end; i++) {
			String strUrl = "http://www.a-zmanga.net/archives/category/" + category + "/page/" + i;
//			System.out.println("StrURL: " + strUrl);
			result.addAll(getSingleMangaList(strUrl, strUploaderName,DTO));
		}

		return result;
	}

	/**
	 * マンガ情報のリストを取得。。
	 * マンガ情報：Aマンガのマンガ名、Uploadedリンク、Uploadedファイル名のリスト
	 * @param strUrl Aマンガの一覧ページ
	 * （http://www.a-zmanga.net/archives/category/一般漫画/page/2 など）
	 * @param strUploaderName アップローダの名前（Uploadsなど）
	 * @return
	 * @throws IOException ネット接続不可によるURLへのアクセス失敗
	 * @throws InterruptedException Waitが吐く例外。謎。
	 */
	public List<List<String>> getSingleMangaList(String strUrl, String strUploaderName,gamenDTO DTO)
			throws IOException, InterruptedException, HttpStatusException, IllegalArgumentException {

		List<String> listUrlInIchiran = new ArrayList<String>();

		try {
			
			//URLにあるすべてのブログページを取得
			
			
			//漫画ページ内で漫画URLを取得
			
			
			listUrlInIchiran= getAzMangaPageList(strUrl,DTO);
		} catch (HttpStatusException e) {
//			System.out.println("Link is Dead :" + strUrl);
			commonAP.writeLog(strUrl + ",aaaaa",DTO.getReNameListFileCreateFolderPath(),CONST.DEADFILE);
			commonAP.writeInErrLog(e,DTO.getReNameListFileCreateFolderPath(),CONST.FATAL_ERR	);
			return new ArrayList<List<String>>();
		}

		List<List<String>> result = new ArrayList<List<String>>();

		for(int i = 0; i < listUrlInIchiran.size(); i++) {
			String strPageUrl = listUrlInIchiran.get(i);
			List<String> singleManga = new ArrayList<String>();

			try {
				singleManga = getSingleMangaData(strPageUrl, strUploaderName);

			} catch (MultiUrlException e) {
				//Uploadedのリンクが複数ある
//				System.out.println("MultUrl : " + strPageUrl);
				commonAP.writeLog(strPageUrl,DTO.getReNameListFileCreateFolderPath(),CONST.MULTIFILE);
//				commonAP.writeLog(writing, logFileFolderPath, fileName);
				continue;
			} catch (HttpStatusException e) {
				//Webページ全体としては生きているが、ページ単体が死んでいる。
//				System.out.println("Link is dead: " + strPageUrl);
				commonAP.writeLog(strUrl,DTO.getReNameListFileCreateFolderPath(),CONST.DEADFILE);
				commonAP.writeInErrLog(e,DTO.getReNameListFileCreateFolderPath(),CONST.FATAL_ERR	);
				continue;
			}

			String uploaderUrl = singleManga.get(1);

			//アップローダの文字列が空文字。指定したアップローダのURLがないので別枠に抜き出し

			if(uploaderUrl.equals("")) {
				uncorrectList.add(strPageUrl);
				continue;
			}

			try {
				String uploadedFileName = getUploadedFileName(uploaderUrl);

				if(uploadedFileName.equals("")) {
					uncorrectList.add(strPageUrl);
					continue;
				}

				singleManga.add(uploadedFileName);
			} catch (HttpStatusException e) {
				System.out.println("Uploader's Link is dead: " + uploaderUrl);
				System.out.println("Uploader's Link is in " + strPageUrl);
				continue;
			}


			result.add(singleManga);

		}

		return result;
	}

	/**
	 * ブログの単作品URLから作品名と指定したアップローダのURLを取得する
	 * @param strUrl ブログの単作品URL
	 * @param strUploaderName アップローダの名前
	 * @return リスト（0:作品名、1:アップローダのURL）
	 * @throws IOException ネット接続不可によるURLへのアクセス失敗
	 * @throws MultiUrlException 指定したアップローダのURLが複数ある場合
	 * @throws InterruptedException Waitが吐く例外。謎。
	 * @throws UnknownHostException URLのホストが不正。URLが死んでいると思われる。
	 */
	protected List<String> getSingleMangaData(String strUrl, String strUploaderName)
			throws IOException, MultiUrlException, InterruptedException, UnknownHostException, HttpStatusException, IllegalArgumentException {

		System.out.println("ブログページ ; " + strUrl);
		Document document = accessUrl(strUrl, false);

		String uploaderUrl = getSingleMangaUrl(document, strUploaderName);

		String strTitleInAz = getSingleMangaTitleInAZ(document);

		List<String> result = new ArrayList<String>();
		result.add(strTitleInAz);
		result.add(uploaderUrl);
		System.out.println(result);
		return result;
	}

	private String getSingleMangaTitleInAZ(Document document) {
		Elements elem = document.select("h1.entry-title-single");
		if (!elem.isEmpty()) return elem.text();

		return "";
	}

	private String getSingleMangaUrl(Document document, String strUploaderName) throws MultiUrlException {
		Elements elements = document.select("p");

		for (Element e : elements ) {
			if (e.text().contains(strUploaderName)) {
				int aSize = e.select("a").size();
				if (aSize == 1) return e.select("a").attr("href");
				else if (aSize >= 2) throw new MultiUrlException();
			}
		}
		return "";
	}

	/**
	 * Uploadedからファイル名を取得する。
	 * TODO;ファイルが削除されている場合など、ファイル名が取得できない（はずな）ので、別枠に抜き出す。
	 * 例外はgetSingleMangaDataの説明を参照のこと。
	 * @param strUrl UploadedのURL
	 * @return ファイル名
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 */
	protected String getUploadedFileName(String strUrl)
			throws IOException, InterruptedException, UnknownHostException, HttpStatusException, IllegalArgumentException {

		Document document = accessUrl(strUrl, true);

		Element fileNameElem = document.getElementById("filename");

		String result = fileNameElem == null ? "" : fileNameElem.text();

		return result;


	}

	/**
	 * Aマンガの単作品ページリストを取得
	 * @param strUrlstrUrl Aマンガの一覧ページ
	 * （http://www.a-zmanga.net/archives/category/一般漫画/page/2 など）
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnknownHostException
	 */
	protected List<String> getAzMangaPageList(String strUrl,gamenDTO DTO)
			throws IOException, InterruptedException, UnknownHostException, HttpStatusException, IllegalArgumentException {

		Document document = accessUrl(strUrl, false);

		Elements listUrls = document.getElementsByClass("entry-title");
		List<String> result = new ArrayList<String>();
		for (Element e : listUrls) {
			result.add(e.select("a").attr("href"));
		}
		;
		return result;


	}

	protected Document accessUrl(String url, boolean isUploader)
			throws InterruptedException, IOException, HttpStatusException, IllegalArgumentException {
		Document result = null;
		int timeoutCount = 0;

		try {
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\ノボル\\git\\AzManga\\AmangaDownLoaderSingle\\lib\\chromedriver.exe");
			
			
			WebDriver webDriver = new ChromeDriver();
			System.out.println("stay");
			
			webDriver.get(url);
			
			System.out.println("ここを通った１");
	        // 最大5秒間、ページが完全に読み込まれるまで待つ
	        webDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
	        
	        //10秒待つ。それでも「Just a moment...」がでたらもう一回トライ
	        Thread.sleep(10000);

	        // 検証
	        System.out.println(webDriver.getTitle());
	        List<WebElement> elements = webDriver.findElements(By.className("entry-title"));
	        WebElement aTag = elements.get(0).findElement(By.tagName("a"));
	        url = aTag.getAttribute("href");
	        webDriver.get(url);
	        //[吾峠呼世晴] 鬼滅の刃 第01-20巻 - a-zmanga.net
	        //[小武] 女主任・岸見栄子 第01-05巻 - a-zmanga.net
	        Thread.sleep(10000);
	        webDriver.get("http://www.a-zmanga.net/archives/59506");
//	        Thread.sleep(10000);
	        //UPLOADのURLの一覧を取得
	        List<WebElement> listelements = webDriver.findElements(By.className("entry-content"));
	        List<WebElement> targetElement = listelements.get(0).findElements(By.tagName("p"));
	        Thread.sleep(10000);
	        WebElement titeleElement = webDriver.findElement(By.id("content"));
	        String[] title = titeleElement.getText().split("\n", 0);
	        System.out.println("タイトル："+title[0]);
	        
	        for (WebElement element: targetElement ) {
	        	
	        	
	            if (element.getText().contains("Alfafile")) {
	            	List<WebElement> urlElement = element.findElements(By.tagName("a"));
	            	String[] fileNameAllay = element.getText().split("\n", 0);
	            	for (int i=0; i < fileNameAllay.length ; i++){
	            		if ( i != fileNameAllay.length - 1){
	            			System.out.println(urlElement.get(i).getAttribute("href") + "," + (fileNameAllay.length - 1) + "," + "Alfafile" + "," + "[生ファイル名]," + fileNameAllay[i+1] + "," +webDriver.getTitle() + "," + "[blogURL]" + "," + "[ファイルキー],[ファイル生死]");
	            		}
	            	}
	            }
	            
	            
	            if (element.getText().contains("Uploaded")) {
	            	List<WebElement> urlElement = element.findElements(By.tagName("a"));
	            	String[] fileNameAllay = element.getText().split("\n", 0);
	            	for (int i=0; i < fileNameAllay.length ; i++){
	            		if ( i != fileNameAllay.length - 1){
	            			System.out.println(urlElement.get(i).getAttribute("href") + "," + (fileNameAllay.length - 1) + "," + "Uploaded" + "," + "[生ファイル名]," + fileNameAllay[i+1] + "," +webDriver.getTitle() + "," + "[blogURL]" + "," + "[ファイルキー],[ファイル生死]");
	            		}
	            	}
	            }
	            
	            
	        }
	        
	        //WebElement listaTag = listelements.get(0).findElement(By.tagName("a"));
	 
	        //UPLOADED
	        //死亡時はnull
	        //死亡時はorg.openqa.selenium.NoSuchElementException
	        //webDriver.getTitle()==uploaded.net
	        webDriver.get("http://ul.to/ngk51s4g");
//	        webDriver.get("http://ul.to/aug3808g");
	        System.out.println(webDriver.getTitle());
	        List<WebElement> UpLelements = webDriver.findElements(By.className("title"));
	        WebElement UpaTag = UpLelements.get(0).findElement(By.id("filename"));
	        System.out.println("aaaaa:" + UpaTag.getText());
	        
	        //Alfafile
	        //webDriver.getTitle() == ページが見つかりません | Alfafile.net
	        webDriver.get("https://alfafile.net/file/8vKmS");
//	        webDriver.get("http://alfafile.net/file/8ea6u");
	        List<WebElement> Alelements = webDriver.findElements(By.className("title"));
	        System.out.println(webDriver.getTitle());
	        WebElement AlaTag = Alelements.get(0).findElement(By.tagName("strong"));
	        System.out.println(AlaTag.getAttribute("title"));
	        
	        //webDriver.close();
	        //webDrivera.close();
	        System.out.println("ここを通った２；" + url );
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

		return result;
	}

	public List<String> getUncorrectList() {
		return uncorrectList;
	}

	public void resetUnCorrectList() {
		uncorrectList = new ArrayList<String>();
	}
}
