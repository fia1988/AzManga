package AmangaSingleDown.maven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class commonAP {
	public static void writeLog(String writing,String logFileFolderPath,String fileName){

		Calendar now = Calendar.getInstance(); //インスタンス化
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String nowTime = sdf.format(now.getTime());
		nowTime = nowTime + ",";

		if (fileName.contains(CONST.OUTPUT)){
			nowTime = "";
		}


		File file = new File(logFileFolderPath + File.separator + fileName);
//			File folder = new File(file_name);
//			folder.mkdirs();
//		System.out.println(writing);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		try{
		//				File file = new File(newFile);

//			FileWriter filewriter = new FileWriter(file,true);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
			        new FileOutputStream(logFileFolderPath + File.separator + fileName, true),"utf-8")));
			pw.write(nowTime + writing + "\r\n" );
			pw.close();
//			filewriter.write(writing + "\r\n" );
//			filewriter.close();
		}catch(IOException e){
			System.out.println(e);
		}


	}


	public static void writeInErrLog(Exception e,String logFileFolderPath,String fileName) {

        StringWriter sw = null;
        PrintWriter  pw = null;

        sw = new StringWriter();
        pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String trace = sw.toString();
        commonAP.writeLog(trace,logFileFolderPath,fileName);

        try {
            if ( sw != null ) {
                sw.flush();
                sw.close();
            }
            if ( pw != null ) {
                pw.flush();
                pw.close();
            }
        } catch (IOException ignore){}

	}

}
