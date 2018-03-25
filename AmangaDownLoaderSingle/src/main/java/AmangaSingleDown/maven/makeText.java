package AmangaSingleDown.maven;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class makeText {


	public String makeTextMain(gamenDTO DTO){
		
		
		List<String> csvStringList = new ArrayList<String>();
		
		AzMangaLinkCrowler umeTool = new AzMangaLinkCrowler();
		
		System.out.println(DTO.getReNameListFileCreateFolderPath());

		List<List<String>> umeResult = new ArrayList <List<String>>();
		try {

			umeResult = umeTool.getSingleMangaList(DTO.getMangaURL(), 1, DTO.getMangURL_page(),DTO);

		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		//Aマンガのマンガ名、Uploadedリンク、Uploadedファイル名のリスト
		for (List<String> a:umeResult){
			String umeLetter = "";
			for (String BBB:a){
				umeLetter = umeLetter + BBB + ",";
			}
			umeLetter = umeLetter.substring(0, umeLetter.length()-1);;
			commonAP.writeLog(umeLetter,DTO.getReNameListFileCreateFolderPath(),CONST.MAKEFILE	);

			csvStringList.add(umeLetter);
		}

		umeTool = new AzMangaLinkCrowler();
		csvStringList = new ArrayList<String>();

//		renameDoreal(DTO,csvStringList);

		return "成功";
	}

}
