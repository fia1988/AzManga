package AmangaSingleDown.maven;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class AzMangaLinkGetV2 {
	private final String kugiri1 = ",___,";
	private final String kugiri2 = ",____,";
	private final String UPLOADED =  "Uploaded";
	private final String Alfafile = "Alfafile";
	private final String Nitroflare = "Nitroflare";
	private final String Alfalife = "Alfalife";
	private final String Alfafile2 = "Alfafil×e";

	private final String 生ファイル名 = "あ生ファイル名あ";
//	private final String ファイルキー = "[ファイルキー]";
	private final String ファイル生死 = "あファイル生死あ";
	private final String 休憩 = "休憩";

	Random random = new Random();

	//コンストラクタ
	public AzMangaLinkGetV2(gamenDTO DTO) {

		String fileName = "chromedriver.exe";
		String folderPath = "C:\\Users\\ノボル\\git\\AzManga\\AmangaDownLoaderSingle\\lib";

		folderPath = folderPath + File.separator + fileName;

		File file = new File(folderPath);

		if(file.exists()){
			System.setProperty("webdriver.chrome.driver", folderPath);
		}else{


			String path = System.getProperty("user.dir");
			path = path + File.separator + fileName;
			System.setProperty("webdriver.chrome.driver", path);
		}

//		System.setProperty("webdriver.chrome.driver", "C:\\Users\\ノボル\\git\\AzManga\\AmangaDownLoaderSingle\\lib\\chromedriver.exe");

//		System.setProperty("webdriver.chrome.driver", DTO.getReNameListFileCreateFolderPath() + File.separator + "chromedriver.exe");
	}



	/**
	 * タイトル名でウィンドウを切り替える
	 *
	 * @param driver
	 * @param title
	 * @return
	 */
	protected boolean switchToTitleWindow(WebDriver driver, String title) {
		String currentWindow = driver.getWindowHandle();
		Set<String> availableWindows = driver.getWindowHandles();
		if (!availableWindows.isEmpty()) {
			for (String windowId : availableWindows) {
				if (driver.switchTo().window(windowId).getTitle().equals(title)) {
					return true;
				} else {
					driver.switchTo().window(currentWindow);
				}
			}
		}
		return false;
	}


	public String mainGetURL_main(gamenDTO DTO){

		//個別ブログページを格納するリストを格納する。
		List<String> kobetuBlogURL = new ArrayList<String>();
		//createFileに書きこむ内容の編集前。一時保存する。
		List<String> createListPre = new ArrayList<String>();
		//createFileに書きこむ内容を保存する。
		List<String> createList = new ArrayList<String>();

		int end = DTO.getMangURL_page();
		String result = "成功";

		//実行中ファイルを作成する
		commonAP.writeLog("実行中。。。",DTO.getReNameListFileCreateFolderPath() , CONST.MOVING	);


		//「,」区切りで一般漫画、小説、少女漫画が入っている
		String[] cateAllay = DTO.getMangaURL().split(",", 0);


		WebDriver webDriver = new ChromeDriver();



		commonAP.writeLog("",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
		commonAP.writeLog("処理開始。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

		String firstURL = "http://www.a-zmanga.net/";
		boolean checkFirstCheck = 	fierstGetURL(webDriver,firstURL,DTO,1);

		if ( checkFirstCheck == false){
			commonAP.writeLog("処理異常終了。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

			result = "クッキーERR";
		}else{
			//
			commonAP.writeLog("createFile作ります。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

			int count = 0;
			String nokori = "";
			//各カテゴリごとに漫画ファイルを作成する。
			for (String category: cateAllay){
				nokori = "";
				for (int i = count ; i < cateAllay.length;i++){
					nokori = nokori + "," + cateAllay[i];
				}
				count++;
				commonAP.writeLog("残りはこれだけあります。" + nokori + " :使ってるクッキー：" + DTO.getHashKey(),DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
				commonAP.writeLog("残りはこれだけあります。" + nokori + " :使ってるクッキー：" + DTO.getHashKey(),DTO.getReNameListFileCreateFolderPath() , CONST.MOVING	);
				//個別ブログページを格納するリストを初期化する。
				kobetuBlogURL = new ArrayList<String>();
				//createFileに書きこむ内容の編集前。初期化する。
				createListPre = new ArrayList<String>();
				//createFileに書きこむ内容を保存する。。初期化する。
				createList = new ArrayList<String>();

				//休憩するかのチェック
				if( stopRest(category,DTO) == false){
					//休憩しない場合はここをとおる
					commonAP.writeLog("mainGetURL_mainで次のカテゴリを処理します。"+category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
					mainGetURL(end,DTO,category,webDriver,kobetuBlogURL,createListPre);

					commonAP.writeLog("mainGetURL_mainでcreatePreを作ります。"+category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

					//createPreを修正してcreateを作る
					replaceCreateList(DTO,webDriver,createListPre,createList);

					commonAP.writeLog("mainGetURL_mainでcreatePreを作りました。"+category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

					for(String a:createList){
						commonAP.writeLog(a,DTO.getReNameListFileCreateFolderPath() , CONST.OUTPUT	);
					}


					commonAP.writeLog("mainGetURL_mainで次のカテゴリを処理しました。"+category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
				}

			}

			commonAP.writeLog("createFile作りました。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

		}












		webDriver.close();


		//実行中ファイルを削除する
		deleteMoving(DTO);

		//内容を初期化する。
//		createListPre = new ArrayList<String>();
//		createList = new ArrayList<String>();
//		kobetuBlogURL = new ArrayList<String>();

		commonAP.writeLog("処理終了。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

		return result;
	}

	//
	private boolean stopRest(String moji,gamenDTO DTO){
		boolean check = true;
		int stopTime = 0;




		switch (moji){
		  case "休憩1":
			  stopTime = 1;
		    break;
		  case "休憩2":
			  stopTime = 2;
			    break;

		  case "休憩3":
			  stopTime = 3;
			    break;

		  case "休憩4":
			  stopTime = 4;
			    break;
		  case "休憩5":
			  stopTime = 5;
			    break;
		  case "休憩6":
			  stopTime = 6;
			    break;

		  case "休憩7":
			  stopTime = 7;
			    break;

		  case "休憩8":
			  stopTime = 8;
			    break;
		  case "休憩9":
			  stopTime = 9;
			    break;

		  default:
			  check = false;
		  break;
	}

		if (check){
			stopTime = stopTime * 3600 * 1000;

			//引数が休憩の時は処理を止める
			commonAP.writeLog((stopTime/3600000) + "時間休憩します。。。"+moji,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
			stop(stopTime);
		}

		return check;

	}

	//実行中ファイルを削除する
	private void deleteMoving(gamenDTO DTO){
		File file = new File(DTO.getReNameListFileCreateFolderPath() + File.separator + CONST.MOVING);
		file.delete();
	}


	private void getURL_Page_con(gamenDTO DTO,String URL,WebDriver webDriver,int WaitTime,List<String> kobetuBlogURL,List<String> createListPre){
		//引数のURL＝ブログ内の親ページ
		//すべてのURL＝子ページのURLを取得する
		getURL(webDriver,URL,DTO);

		try {Thread.sleep(WaitTime);} catch (InterruptedException e) {e.printStackTrace();}
		// 最大5秒間、ページが完全に読み込まれるまで待つ
		waitURL(webDriver);

		if (webDriver.getTitle().contains("Just a moment...")){
			try {Thread.sleep(WaitTime);} catch (InterruptedException e) {e.printStackTrace();}
			getURL_Page_con(DTO,URL,webDriver,WaitTime + 5000,kobetuBlogURL,createListPre);
			return;
		}

		getURL_Page_con(DTO,webDriver,kobetuBlogURL,createListPre);
	}

	private void stop(int timer){
		try {Thread.sleep(timer);} catch (InterruptedException a) {a.printStackTrace();}
	}

	private boolean fierstGetURL(WebDriver webDriver,String URL,gamenDTO DTO,int counter){

		if (counter > 5 ){
			commonAP.writeLog("fierstGetURLでクッキーエラー。cf_clearanceを最新化してください。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
			return false;
		}

		try {

			webDriver.get(URL);
			webDriver.manage().addCookie(new Cookie("cf_clearance", DTO.getHashKey()));
			stop(5000);
		} catch (TimeoutException e) {
			stop(8000);
			fierstGetURL(webDriver,URL,DTO,counter);
		} catch (Exception e) {
			commonAP.writeLog(URL,DTO.getReNameListFileCreateFolderPath() , CONST.NO_ACCESS	);
			return false;
		}

		if (webDriver.getTitle().contains("Just a moment...")){

			commonAP.writeLog("fierstGetURLで「Just a moment...」です。15秒待ちます。："  + URL,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
			stop(15000);
			fierstGetURL(webDriver , URL , DTO , counter + 1 );
			return false;
		}

		String checkURL = "http://www.a-zmanga.net/?__cf_chl_jschl_tk__=d67c49f4bd35b03e4f23eaaae238f311bbbafb53-1595066523-0-AbJg6ikZQ2THqGjkqZFq3_N8hO8fbCs_UKw3BUDq4XrmtI_sl7_-ay1k1pbAScM4Qjb6dnExJkuddRvI_p_DXFHLhu-0azC3juplT5Y4AmFYytnWU4VpqbtfjHIoE5ARtjMUGv_oSUu9E9g7EpWIf0pgrxhoskxxs6pJrx0Irn19AlEgRCJ2sV1s30WPKBugPu2tib6hWYHqMTR4QTOy1_8Ucg02ikn4k1R1aX7Rzl5QJK6I7edwmgLxXCAmrVu_-yfL78_XBpTJDwQ_pNZvSj4";

		if (webDriver.getCurrentUrl().contains(checkURL)){

		}


		return true;
	}

	private boolean getURL(WebDriver webDriver,String URL,gamenDTO DTO){
		try {
			stop(random.nextInt(8000));
			webDriver.get(URL);
//			webDriver.manage().addCookie(new Cookie("__cfduid", "d2a6f6a0d6dafdbd5f022be81ba66b2271599901194"));
//			webDriver.manage().addCookie(new Cookie("__dtsu", "51A0158427729203B632808E7B8F80B5"));
//			webDriver.manage().addCookie(new Cookie("cf_clearance", "de55fbf658d6eb08999b15914580d8eee3509d62-1601547798-0-1z63fa72adz5414ab15z5ae89b2d-150"));
//			webDriver.manage().addCookie(new Cookie("cf_clearance", DTO.getHashKey()));
//			webDriver.manage().addCookie(new Cookie("cf_chl_1", "998ad66d3fa6c17"));
//			webDriver.manage().addCookie(new Cookie("cf_chl_1", "c9b349ec2c68103"));
//			webDriver.manage().addCookie(new Cookie("cf_chl_prog", "x15"));
//			webDriver.manage().addCookie(new Cookie("HstCfa2741643", "1584277289587"));
//			webDriver.manage().addCookie(new Cookie("HstCla2741643", "1595485842633"));
//			webDriver.manage().addCookie(new Cookie("HstCmu2741643", "1593426404138"));
//			webDriver.manage().addCookie(new Cookie("HstCns2741643", "34"));
//			webDriver.manage().addCookie(new Cookie("HstCnv2741643", "27"));
//			webDriver.manage().addCookie(new Cookie("HstPn2741643", "4"));
//			webDriver.manage().addCookie(new Cookie("HstPt2741643", "171"));


			if (webDriver.getTitle().contains("Just a moment...")){

				commonAP.writeLog("getURLで「Just a moment...」です。1分待ちます。："  + URL,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
				stop(60000);
				getURL(webDriver,URL,DTO);
				return true;
			}


		} catch (TimeoutException e) {
			stop(5000);
			getURL(webDriver,URL,DTO);
		} catch (Exception e) {
			commonAP.writeLog(URL,DTO.getReNameListFileCreateFolderPath() , CONST.NO_ACCESS	);
			return false;
		}
		return true;
	}


	//0:生ファイル名
	//1:ファイル生死
	private String[] getUploaderList(WebDriver webDriver,String uploaderName,String loaderURL,gamenDTO DTO,String fileName){

		//		生ファイル名;
		//		ファイル生死;1生きてる、２死んでる
		String rawFileName = "dead";
		String rawFileAlive = "2";
		String[] result = {rawFileName,rawFileAlive};

		String[] loaderSplit = uploaderName.split(kugiri2, -1);
		String thisLoaderName = loaderSplit[0];

		switch (thisLoaderName){
			case UPLOADED:
				result = getUPLOADED(webDriver,loaderURL,DTO,fileName);
				break;
			case Alfafile:
				result = getAlfafile(webDriver,loaderURL,DTO,fileName);
//				rawFileName = resultLoader[0];
//				rawFileAlive = resultLoader[1];

				break;
			case Alfafile2:
				result = getAlfafile(webDriver,loaderURL,DTO,fileName);
				break;
			case Alfalife:
				result = getAlfafile(webDriver,loaderURL,DTO,fileName);
				break;

			case Nitroflare:
//				stop(random.nextInt(8000));
				result = getNitroflare(webDriver,loaderURL,DTO,fileName);
				break;
			default:

				break;
		}
		return result;
	}


	private String[] getNitroflare(WebDriver webDriver,String loaderURL,gamenDTO DTO,String fileName){


//		String rawFileAlive = "2";
//		rawFileAlive = "1";
//		String[] result = {fileName,rawFileAlive};
//		return result;


		return commonUploader(DTO,fileName);

	}

	private String[] commonUploader(gamenDTO DTO,String fileName){
		String rawFileAlive = "2";
		rawFileAlive = "1";
		String[] result = {fileName,rawFileAlive};
		return result;

	}

	//0:生ファイル名
	//1:ファイル生死
	private String[] getUPLOADED(WebDriver webDriver,String loaderURL,gamenDTO DTO,String fileName){

		getURL(webDriver,loaderURL,DTO);
		//生ファイル名;
		//ファイル生死;1生きてる、２死んでる
		String rawFileName = "dead";
		String rawFileAlive = "2";
		try {
//			org.openqa.selenium.NoSuchElementException
	        List<WebElement> UpLelements = webDriver.findElements(By.className("title"));
	        WebElement UpaTag = UpLelements.get(0).findElement(By.id("filename"));
	        rawFileName = UpaTag.getText();
	        rawFileAlive = "1";
		} catch (Exception NoSuchElementException) {
			rawFileName = "dead";
			rawFileAlive = "2";
		}
		String[] result = {rawFileName,rawFileAlive};
		return result;
	}


	//0:生ファイル名
	//1:ファイル生死
	private String[] getAlfafile(WebDriver webDriver,String loaderURL,gamenDTO DTO,String fileName){


		return commonUploader(DTO,fileName);

//		getURL(webDriver,loaderURL,DTO);
//
//		//生ファイル名;
//		//ファイル生死;1生きてる、２死んでる
//		String rawFileName = "dead";
//		String rawFileAlive = "2";
//
//		try {
////			org.openqa.selenium.NoSuchElementException
//	        List<WebElement> Alelements = webDriver.findElements(By.className("title"));
//	        //webDriver.getTitle() == ページが見つかりません | Alfafile.net
//	        webDriver.getTitle();
//	        WebElement AlaTag = Alelements.get(0).findElement(By.tagName("strong"));
//	        rawFileName = AlaTag.getAttribute("title");
//	        rawFileAlive = "1";
//		} catch (Exception NoSuchElementException) {
//			rawFileName = "dead";
//			rawFileAlive = "2";
//		}
//		String[] result = {rawFileName,rawFileAlive};
//
//
//		return result;
	}


	private void replaceCreateList(gamenDTO DTO,WebDriver webDriver,List<String> createListPre,List<String> createList){
		//リストの項目を編集する入れる
		//リストの項目は「￥」
			//0:漫画ノベルチェック（漫画：m、ノベル：n、その他：o、特殊：b）
			//1:DL_URL
			//2:ページ内ファイル数
			//3:アップローダー名_雑誌チェック文字_タグ
			//4:ブログ内ファイル名
			//5:￥生ファイル名
			//6:ブログファイルタイトル
			//7:ブログUR
			//8:ファイルキー
			//9:￥ファイル生死チェック(1：生きてる、2：死んでる)

		int count = 1;
		int maxcount = createListPre.size();

		String copyLetter = "";

		String beforeDate = "";

		commonAP.writeLog("replaceCreateList処理開始。。。" + (count - 1 ) + " / " + maxcount + "。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

		for (String a:createListPre){

			beforeDate = a;
			String[] aList = a.split(kugiri1, 0);
			//copyする文字を作る
			copyLetter = copyLetter +"," + aList[1];

			//0:生ファイル名
			//1:ファイル生死
			String[] uploaderResult = getUploaderList(webDriver,aList[3],aList[1],DTO,aList[4]);
			aList[5] = uploaderResult[0];
			aList[9] = uploaderResult[1];
//			//あっぷろだ先のファイル名
			beforeDate = beforeDate.replaceAll(生ファイル名, aList[5]);

			//ファイル生死をチェックする。
			beforeDate = beforeDate.replaceAll(ファイル生死, aList[9]);

			createList.add(beforeDate);

			if (count % 100 ==0 ){
				commonAP.writeLog("replaceCreateList処理中。。。" + count + " / " + maxcount + "。タイトル：" + aList[6],DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
			}
			count++;
		}
		copyClipBoad(copyLetter, 1);
		commonAP.writeLog("replaceCreateList処理完了。。。" + (count - 1 ) + " / " + maxcount + "。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

	}

	//ブログ内の親ページを開いたwebDriverを引数に該当のURL内の個別ページのURLを取得する。
	private void getURL_Page_con(gamenDTO DTO,WebDriver webDriver,List<String> kobetuBlogURL,List<String> createListPre){
		commonAP.writeLog("getURL_Page_conで個別ページを取得します。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

        List<WebElement> elements = webDriver.findElements(By.className("entry-title"));

		for (WebElement element: elements){
			try {
				WebElement aTag = element.findElement(By.tagName("a"));
				kobetuBlogURL.add(aTag.getAttribute("href"));
			} catch (NoSuchElementException e) {

			}
		}

		commonAP.writeLog("getURL_Page_conで個別ページを取得しました。",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
	}


	private void  getURL_Page_Individual_parts (gamenDTO DTO,WebDriver webDriver,List<String> kobetuBlogURL,List<String> createListPre,String cateMoji,String blogURL) throws NoSuchElementException{

		String blogTitle = "";
		//ブログタイトルの取得
        WebElement titeleElement = webDriver.findElement(By.id("content"));
        String[] title = titeleElement.getText().split("\n", 0);
        blogTitle = title[0];

        //UPLOAD、AlfafileのURLの一覧を取得
        List<WebElement> listelements = webDriver.findElements(By.className("entry-content"));
        List<WebElement> targetElement = listelements.get(0).findElements(By.tagName("p"));

        List<WebElement> tagElements = webDriver.findElements(By.className("cat-links"));

        List<WebElement> zassiCheckElement1 = listelements.get(0).findElements(By.tagName("strong"));

        String zassiCheck = zassiCheckElement1.size() + "-";
        for (WebElement element: zassiCheckElement1 ){
        	zassiCheck = zassiCheck + element.getText();
        }

        String tagCheck = tagElements.size()+"-" + tagElements.get(0).getText();
        tagCheck = tagCheck.replaceAll("Posted in:","");
        tagCheck = tagCheck.replaceAll(" ","");
        tagCheck = tagCheck.replaceAll("　","");


        //ローダーのループ
        //getUploaderList();にはローダーの一覧が入っている
        for (String loader : getUploaderList()){

        	for (WebElement element: targetElement ){

        		if (element.getText().contains(loader)) {
        			List<WebElement> urlElement = element.findElements(By.tagName("a"));
        			String[] fileNameAllay = element.getText().split("\n", 0);
        			//for (int i=0; i < fileNameAllay.length ; i++){
        			for (int i=0; i < urlElement.size() ; i++){


        				if ( i != fileNameAllay.length - 1){
        					//リストに入れる
        					//リストの項目は以下
        					//漫画ノベルチェック（漫画：m、ノベル：n、その他：o、特殊：b）
        					//DL_URL
        					//ページ内ファイル数
        					//アップローダー名_雑誌チェック文字_タグ
        					//ブログ内ファイル名
        					//生ファイル名
        					//ブログファイルタイトル
        					//ブログUR
        					//ファイルキー＝ブログURL+ブログ内ファイル名
        					//ファイル生死チェック(1：生きてる、2：死んでる)
        					try {

        						createListPre.add(
        											cateMoji								 + kugiri1
        										+	urlElement.get(i).getAttribute("href")	 + kugiri1
//        										+	(fileNameAllay.length - 1)				 + kugiri
        										+	urlElement.size()						 + kugiri1

        										+ 	loader + kugiri2 + zassiCheck + kugiri2 + tagCheck	 + kugiri1
        										+	fileNameAllay[i+1]						 + kugiri1
        										+	生ファイル名							 + kugiri1
        										+	blogTitle								 + kugiri1
        										+	blogURL									 + kugiri1
        										+	blogURL + fileNameAllay[i+1]			 + kugiri1
        										+	ファイル生死
        								);
        					} catch (Exception e) {
        						commonAP.writeLog("getURL_Page_Individual_partsで取得できないブログページあります："+ cateMoji + ":" + blogURL,DTO.getReNameListFileCreateFolderPath() , CONST.MISS_BLOG_PAGE	);

        						if (DTO.isCheckErrLog()==true){
        							//テスト用の時はここ動く
        							System.out.println("getURL_Page_Individual_partsで例外が発生しました。");
        							System.out.println(element.getText());
        							System.out.println(i);
        							System.out.println(urlElement.get(i-1).getAttribute("href"));;
        							System.out.println(fileNameAllay[i]);
        							StringWriter sw = null;
        							PrintWriter  pw = null;

        							sw = new StringWriter();
        							pw = new PrintWriter(sw);
        							e.printStackTrace(pw);
        							String trace = sw.toString();
        							System.out.println(trace);
        						}

        					}

        				}



        			}
        		}
        	}
        }
	}


	private void copyClipBoad(String str,int stopTime){
//		String str = "Test"; // 保存するテキスト
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		stop(stopTime);
		StringSelection selection = new StringSelection(str);
		stop(stopTime);
		clipboard.setContents(selection, null);
	}

	public void getURL_Page_Individual(gamenDTO DTO){

		//個別ブログページを格納するリストを格納する。
		List<String> kobetuBlogURL = new ArrayList<String>();
		//createFileに書きこむ内容の編集前。一時保存する。
		List<String> createListPre = new ArrayList<String>();
		//createFileに書きこむ内容を保存する。
		List<String> createList = new ArrayList<String>();

		int end = DTO.getMangURL_page();


		WebDriver webDriver = new ChromeDriver();

		//URL
		String URL = DTO.getReNameListFilePath();
		kobetuBlogURL.add(URL);
		getURL_Page_con(DTO,URL,webDriver,15000,kobetuBlogURL,createListPre);
		;;

		getURL_Page_Individual(DTO,webDriver,kobetuBlogURL,createListPre,"test");

		//createPreを修正してcreateを作る
		replaceCreateList(DTO,webDriver,createListPre,createList);

		commonAP.writeLog("mainGetURL_mainでcreatePreを作りました。test",DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);

		for(String a:createList){
			commonAP.writeLog(a,DTO.getReNameListFileCreateFolderPath() , CONST.OUTPUT	);
		}

		webDriver.close();
	}

	//ブログ内の個別URLを開いたwebDriverを引数に該当のURL内のDLページ、タイトルなどを作成する。
	private void getURL_Page_Individual(gamenDTO DTO,WebDriver webDriver,List<String> kobetuBlogURL,List<String> createListPre,String cateMoji){


		int count =1;
		int maxCount = kobetuBlogURL.size();

		commonAP.writeLog("getURL_Page_Individualでブログ個別ページ取得開始："+ cateMoji + ": 0" + " / " + maxCount,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);


		for(String blogURL:kobetuBlogURL){
			//ブログ個別ページにアクセスする
//			try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}

			if(count % 100 ==0){
				commonAP.writeLog("getURL_Page_Individualでブログ個別ページ取得中："+ cateMoji + ": " + count + " / " + maxCount + ":" + blogURL,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
			}

			try {
				getURL(webDriver,blogURL,DTO);
				getURL_Page_Individual_parts(DTO, webDriver, kobetuBlogURL, createListPre, cateMoji,blogURL);
			} catch (Exception e) {
				commonAP.writeLog("getURL_Page_Individualで取得できないブログページあります："+ cateMoji + ":" + blogURL,DTO.getReNameListFileCreateFolderPath() , CONST.MISS_BLOG_PAGE	);

				if (DTO.isCheckErrLog()==true){
					//テスト用の時はここ動く
				      System.out.println("getURL_Page_Individualで例外が発生しました。");
				        StringWriter sw = null;
				        PrintWriter  pw = null;

				        sw = new StringWriter();
				        pw = new PrintWriter(sw);
				        e.printStackTrace(pw);
				        String trace = sw.toString();
				        System.out.println(trace);
				}
			}



		count++;

		}

		commonAP.writeLog("getURL_Page_Individualでブログ個別ページ取得完了： "+ cateMoji + ":" + (count - 1 ) + " / " + maxCount,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
	}


	private void mainGetURL(int end,gamenDTO DTO,String category,WebDriver webDriver,List<String> kobetuBlogURL,List<String> createListPre){

//		List<List<String>> result = new ArrayList<List<String>>();
		commonAP.writeLog("mainGetURLで親ページから個別ページの一覧を取得します。カテゴリは→：" + category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
		//URLを1ページから順番に処理し、個別ページを取得する。
		//そのページごとにブログ個別URL、アップローダーURL、タイトルなどを取得する。
		for (int i = 1; i <= end; i++) {
			String strUrl = "http://www.a-zmanga.net/archives/category/" + category + "/page/" + i;
			commonAP.writeLog("mainGetURLで親ページから個別ページを取得します。" + strUrl,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
			getURL_Page_con(DTO,strUrl,webDriver,15000,kobetuBlogURL,createListPre);

//			System.out.println("StrURL: " + strUrl);
//			result.addAll(getSingleMangaList(strUrl, strUploaderName,DTO));
		}
		commonAP.writeLog("mainGetURLで親ページから個別ページの一覧を取得しました。カテゴリは→：" + category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);


		commonAP.writeLog("mainGetURLで個別ページURLをもとにcreateFileを作成します。カテゴリは→：" + category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);
		getURL_Page_Individual(DTO,webDriver,kobetuBlogURL,createListPre,getCateLetter(category));
		commonAP.writeLog("mainGetURLで個別ページURLをもとにcreateFileを作成しました。カテゴリは→：" + category,DTO.getReNameListFileCreateFolderPath() , CONST.LOGFILE	);


		return ;
	}




	//WEBDRIVERの読み込み待ち時間
	private void waitURL(WebDriver webDriver){
		webDriver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
	}

	private String getCateLetter(String moji){
		String resultMoji = "o";

		switch (moji){
			  case "一般漫画":
				  resultMoji="m";
			    break;
			  case "少女漫画":
				  resultMoji="ms";
				    break;

			  case "小説":
				  resultMoji="n";
				    break;

			  case "画集":
				  resultMoji="g";
				    break;
			  case "成年書籍":
				  resultMoji="b";
				    break;
			  case "同人誌":
				  resultMoji="d";
				    break;

			  case "連載漫画":
				  resultMoji="rm";
				    break;

			  case "小説-成年書籍":
				  resultMoji="bn";
				    break;
			  case "その他":
				  resultMoji="o";
				    break;

			  default:

			  break;
		}

		return resultMoji;
	}


	private String[] getUploaderList(){
		String[] uploader = {Nitroflare,Alfafile,Alfalife,UPLOADED,Alfafile2};
		return uploader;
	}





}
