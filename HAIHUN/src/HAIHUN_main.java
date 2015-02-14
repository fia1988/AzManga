import java.io.File;


public class HAIHUN_main {
	public static void main(String[] args){
		haihunFolder HF = new haihunFolder();

		String rootFolder = args[0];
		File folder = new File(rootFolder);
		File dir[] = folder.listFiles();
		File TIFFOLDER = new File(rootFolder + File.separator + "【ハイフンフォルダ】");
		TIFFOLDER.mkdir();

		for(int i = 0;i<dir.length;i++){
			if(dir[i].isDirectory()){
				//trueだと無事、falseだとハイフンを含んだファイルである。
				if(HF.haihunChecker(dir[i])==false){
					File IDO = new File(TIFFOLDER.toString() + File.separator + dir[i].getName());
					dir[i].renameTo(IDO);

				}
			}
		}

		HF.haihunRename(TIFFOLDER);



	}
}
