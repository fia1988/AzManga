package AmangaSingleDown.maven;


public class gamenDTO {

	String reNameListFilePath;
	String reNameListFileCreateFolderPath;
	String reNamedFolderPath;
	String mangaURL;
	String hashKey;
	int mangURL_page;

	boolean checkErrLog = false;




	public String getHashKey() {
		return hashKey;
	}
	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}
	public boolean isCheckErrLog() {
		return checkErrLog;
	}
	public void setCheckErrLog(boolean checkErrLog) {
		this.checkErrLog = checkErrLog;
	}
	public String getReNameListFileCreateFolderPath() {
		return reNameListFileCreateFolderPath;
	}
	public void setReNameListFileCreateFolderPath(
			String reNameListFileCreateFolderPath) {
		this.reNameListFileCreateFolderPath = reNameListFileCreateFolderPath;
	}
	public String getReNameListFilePath() {
		return reNameListFilePath;
	}
	public void setReNameListFilePath(String reNameListFilePath) {
		this.reNameListFilePath = reNameListFilePath;
	}

	public String getReNamedFolderPath() {
		return reNamedFolderPath;
	}
	public void setReNamedFolderPath(String reNamedFolderPath) {
		this.reNamedFolderPath = reNamedFolderPath;
	}
	public String getMangaURL() {
		return mangaURL;
	}
	public void setMangaURL(String mangaURL) {
		this.mangaURL = mangaURL;
	}
	public int getMangURL_page() {
		return mangURL_page;
	}
	public void setMangURL_page(int mangURL_page) {
		this.mangURL_page = mangURL_page;
	}




}

