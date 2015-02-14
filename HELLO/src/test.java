import java.io.File;

public class test {
	public static void main(String[] args){
		System.out.println("aaa");
		File file = new File("C:\\Users\\NOBORU1988\\Desktop\\順番 - コピー - コkgkghkghjkghjukghjuiピー (2)\\sub");

		System.out.println(file);
		sample AAA = new sample();
		String A = "2015-02-03,24150,24190,24000,24000,285,6882940";
		String B = "";
		String C = "2015-02-02";
		String D = "2015-02-04,24150,24190,24000,24000,285,6882940";
		String E = "";
			System.out.println(A.compareTo(B));
			System.out.println(B.compareTo(A));
			System.out.println(B.compareTo(C));
			System.out.println(B.compareTo(D));
			System.out.println(B.compareTo(E));
			
			String skipTime = null;
			String skipDay = null;
			skipTime = skipDay + "," + skipTime;
			System.out.println(skipTime);

			//BがskipDay、Aがこれから挿入する文字
			if(B.compareTo(A)<0){
				//入れる
			}
	}




}





