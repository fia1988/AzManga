import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class haihunFolder {
	public void haihunRename(File FD){
		//FDは【ハイフンフォルダ】
		//dirの中にはFDの中のフォルダが入っている
		File dir[] = FD.listFiles();
		int size = dir.length;



		for(int i = 0;i<size;i++){
			if(dir[i].isDirectory()){
				try{
					String fileName = dir[i].getName();
					int index = fileName.indexOf("―");

					String BEFORE = fileName.substring(0,index);
					String AFTER = fileName.substring(index+1);
					int DAI = BEFORE.lastIndexOf("第");
					int KAN = AFTER.indexOf("巻");
					String startKAN = BEFORE.substring(DAI+1,BEFORE.length());
					String endKAN = AFTER.substring(0 ,KAN);
					String BEFOREDAI = BEFORE.substring(0,DAI);

//					System.out.println(BEFORE);
//					System.out.println(AFTER);
//					System.out.println(startKAN);
//					System.out.println(endKAN);
					int intSTART = Integer.parseInt(startKAN);
					int intEND = Integer.parseInt(endKAN);
					if(intSTART < intEND){

						File haihunComic[] = dir[i].listFiles();

						for(int p = 0;p<haihunComic.length;p++){

							if(haihunComic[p].getName().length() == 1){

								File zeroFile  = new File(dir[i].toString() + File.separator +  "0" + haihunComic[p].getName());

								haihunComic[p].renameTo(zeroFile);

								haihunComic[p] = new File(zeroFile.getParent() + File.separator +  "0" + haihunComic[p].getName());

							}

						}

						Arrays.sort(haihunComic);

						for (int p = 0;p<haihunComic.length;p++){
							String KANSU = String.valueOf(intSTART + p);
							if(KANSU.length() == 1){
								KANSU = "0" + KANSU;
							}


							File file2 = new File(dir[i].getParent() + File.separator + BEFOREDAI + "第"+ KANSU + "巻");
							haihunComic[p].renameTo(file2);
							LOG(FD.toString(),haihunComic[p].getName() + "を" + file2.getName());
						}
					}

				}catch(StringIndexOutOfBoundsException e){

				}
				zeroDelete(dir[i]);

			}
		}
//		int i = 05;
//		指定した文字列の前後が数字であるかの確認





//		文字列から数字を取り出す

//		try{
//		String fileName = "[伊賀大晃×月山可也] エリアの騎士 第39―40巻 [Area no Kishi vol.39―40]";
//		 int xxx = Integer.parseInt(fileName.replaceAll("[^0-9]",""));
//		 System.out.println("aaaa;" + xxx);
//		}catch(NumberFormatException e){
//			System.out.println("数字がない");
//		}
//
//
//
//		// 指定した文字より後ろの文字取り出し
//	    int index = fileName.indexOf("―");
//	    System.out.println("前：" + fileName.substring(0,index));
//	    System.out.println("後：" + fileName.substring(index+1));
//
//	    try{
//	    	System.out.println("前後：" + fileName.substring(index-2,index+3));
//	    }catch(StringIndexOutOfBoundsException e){
//	    	System.out.println("だめだ");
//	    	return;
//	    }
//
//		System.out.println(fileName.indexOf("―"));
//		System.out.println(String.valueOf(i).length());
	}







	//引数：フォルダ(File型)
		//―を含めばfalse、含まなければtrue
		public boolean haihunChecker(File FD){
			if(FD.isDirectory()){
					if(FD.toString().contains("―")){
						return false;
					}
				}
			return true;
		}

















		//【未完成】
		//引数フォルダパス(File)
		//戻り値なし。
		//指定したフォルダのなかに存在する0バイトフォルダを削除する。
		//0バイトフォルダ以外が存在した場合、デスクトップにそのフォルダのパスを書いたtxtファイルを生成
		public void zeroDelete(File folder){
			if (folder.exists() == false){
				return;
			}

			File dir[] = folder.listFiles();

			if(dir.length == 0){
				folder.delete();
			}

			//この辺にログの話を書く。なんか消せないー。とかね。
		}





	//第一引数：ログを作りたいフォルダ名(String)
	//第二引数；ログに書き込む内容(String)
	//ログが書き込める
	public void LOG(String file_name,String WRITING){
		File file = new File(file_name + ".log");
//		File folder = new File(file_name);
//		folder.mkdirs();

		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

		try{
//			File file = new File(newFile);

			if (checkBeforeWritefile(file)){
				FileWriter filewriter = new FileWriter(file,true);

				filewriter.write(WRITING + "\r\n");

				filewriter.close();
			}else{
				System.out.println("ファイルに書き込めません");
			}
		}catch(IOException e){
			System.out.println(e);
		}
	}

	  private static boolean checkBeforeWritefile(File file){
		    if (file.exists()){
		      if (file.isFile() && file.canWrite()){
		        return true;
		      }
		    }

		    return false;
		  }









}
