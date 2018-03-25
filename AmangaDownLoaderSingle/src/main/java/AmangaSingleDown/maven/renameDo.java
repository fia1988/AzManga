package AmangaSingleDown.maven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class renameDo {

	public String renameDoMain(gamenDTO DTO){


		List<String> csvStringList = new ArrayList<String>();
		csvStringList = getRenameList (DTO);


		System.out.println(DTO.getReNamedFolderPath());
		System.out.println(DTO.getReNameListFilePath());

//		if ( checkStringList(DTO,csvStringList)==false ){
//			return "ファイルに不正な値がある様子";
//		}


		renameDoreal(DTO,csvStringList);
		csvStringList = new ArrayList<String>();
		return "成功";
	}

	private List<String> getRenameList (gamenDTO DTO){
		List<String> csvStringList = new ArrayList<String>();
		File file = new File(DTO.getReNameListFilePath());


		BufferedReader br;
		try {
            FileInputStream input = new FileInputStream(file);
            InputStreamReader stream = new InputStreamReader(input,"UTF-8");
            br = new BufferedReader(stream);
//			br = new BufferedReader(new FileReader(file));
			String str = br.readLine();
			csvStringList.add(str);
			str = br.readLine();
			while(str != null){
				csvStringList.add(str);
				str = br.readLine();

			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (Exception e) {
		}


		return csvStringList;
	}



	private void renameDoreal(gamenDTO DTO,List<String> csvStringList){

		//Aマンガのマンガ名、Uploadedリンク、Uploadedファイル名のリスト
		String oldFilePath = "";
		String newFilePath = "";

//		String a = DTO.getReNamedFolderPath() + File.separator;


		for (String a:csvStringList){
			String[] a_sp = a.split(",", 0);
			oldFilePath = DTO.getReNamedFolderPath() + File.separator + a_sp[2];
			newFilePath = DTO.getReNamedFolderPath() + File.separator + a_sp[0] + ".rar";
			File fOld = new File(oldFilePath);
	        File fNew = new File(newFilePath);

	        if (fOld.exists()) {
	            //ファイル名変更実行
	            fOld.renameTo(fNew);
	        } else {
	            File file = new File(DTO.getReNamedFolderPath());
	            File files[] = file.listFiles();

	            try {
	            	String check27 = a_sp[2].substring(0,27);
	            	int checkCounter = 0;
		            for (int i = 0; i < files.length; i++) {
		            	System.out.println("check27           :" + check27);
			            System.out.println("files[i].getName():" + files[i].getName());
		            	if (files[i].getName().startsWith(check27)){
		            		System.out.println("files[i].getName()aaaaaaaaaaaaaaaa:" + files[i].getName());
		            		checkCounter++;
		            	}
		            }

		            for (int i = 0; i < files.length; i++) {

		            	if (checkCounter == 1 ){
		            		if (files[i].getName().startsWith(check27)){
			            		fOld = new File(DTO.getReNamedFolderPath() + File.separator + files[i].getName());
			            		fOld.renameTo(fNew);
			            	}
		            	}
		            }

		        	if (checkCounter > 1 ){
		        		commonAP.writeLog("テキストファイル内のファイル名：" + a_sp[2] + "：が失敗：" + a,DTO.getReNamedFolderPath(),CONST.MAKEFILE_ERR);
		        	}
				} catch (StringIndexOutOfBoundsException e) {

				}



	        }
		}


	}

}
