package AmangaSingleDown.maven;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class mainGamen extends JFrame {

	private JPanel contentPane;
	private JTextField renameListFileCreateFolderPath;
	private JTextField mangaURL;
	private JTextField pageNum;
	private JTextField reNameListFilePath;
	private JTextField renamedFolderPath;
	JLabel renameCreateResult = new JLabel("結果表示");
	JLabel renameDoresult = new JLabel("結果表示");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainGamen frame = new mainGamen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public mainGamen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 552);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		renameListFileCreateFolderPath = new JTextField("D:\\01.kabu_backup\\00.dropbox\\Dropbox\\03.漫画");
		renameListFileCreateFolderPath.setBounds(49, 130, 334, 25);

		contentPane.add(renameListFileCreateFolderPath);
		renameListFileCreateFolderPath.setColumns(10);

		mangaURL = new JTextField("一般漫画");
		mangaURL = new JTextField("一般漫画,少女漫画,小説,画集,成年書籍,その他,小説-成年書籍");
		mangaURL = new JTextField("一般漫画,少女漫画,小説,画集,成年書籍,その他,小説-成年書籍,同人誌,連載漫画");

		mangaURL.setColumns(10);
		mangaURL.setBounds(49, 55, 334, 25);
		contentPane.add(mangaURL);

		pageNum = new JTextField("15");
		pageNum.setColumns(10);
		pageNum.setBounds(462, 55, 50, 25);
		contentPane.add(pageNum);

		JLabel lblurl = new JLabel("漫画URL");
		lblurl.setBounds(49, 21, 73, 19);
		contentPane.add(lblurl);

		JLabel label = new JLabel("参照ページ数");
		label.setBounds(459, 21, 151, 19);
		contentPane.add(label);

		JLabel lblrenamefiletxt = new JLabel("ログとかの作成フォルダ");
		lblrenamefiletxt.setBounds(49, 95, 319, 19);
		contentPane.add(lblrenamefiletxt);

		JLabel label_1 = new JLabel("cf_clearanceのキー");
		label_1.setBounds(49, 252, 334, 19);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("置換フォルダパス");
		label_2.setBounds(49, 327, 260, 19);
		contentPane.add(label_2);

		reNameListFilePath = new JTextField();
		reNameListFilePath.setColumns(10);
		reNameListFilePath.setBounds(49, 276, 334, 25);
		reNameListFilePath.setText("ここにキーをいれる");
		contentPane.add(reNameListFilePath);

		renamedFolderPath = new JTextField("D:\\01.kabu_backup\\00.dropbox\\Dropbox\\03.漫画");
		renamedFolderPath.setColumns(10);
		renamedFolderPath.setBounds(49, 355, 334, 25);
		contentPane.add(renamedFolderPath);


		JButton button = new JButton("「01.output.txt」作成");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamenDTO DTO = new gamenDTO();
				DTO.setCheckErrLog(false);
				String DTOresult = setDTO(DTO,true);
				renameCreateResult.setText(DTOresult);
//				reNameListFilePath.setText(DTO.getReNameListFileCreateFolderPath() + File.separator + CONST.MAKEFILE);
				renamedFolderPath.setText(DTO.getReNameListFileCreateFolderPath());
				System.out.println("「01.output.txt」作成処理");

				if (!DTOresult.equals("成功")){
					return;
				}

				try {
//					makeText makeT = new makeText();
//					renameCreateResult.setText(makeT.makeTextMain(DTO));

					//ここから漫画などの取り込みを開始する
					AzMangaLinkGetV2 azmanga = new AzMangaLinkGetV2(DTO);
					renameCreateResult.setText(azmanga.mainGetURL_main(DTO));
//					makeT = new makeText();
				} catch (Exception e2) {
					commonAP.writeInErrLog(e2,DTO.getReNameListFileCreateFolderPath(),CONST.FATAL_ERR	);
					// TODO: handle exception
				}


				reNameListFilePath.setText(DTO.getReNameListFileCreateFolderPath() + File.separator + CONST.MAKEFILE);
				renamedFolderPath.setText(DTO.getReNameListFileCreateFolderPath());
			}
		});
		button.setBounds(48, 181, 216, 27);
		contentPane.add(button);


		JButton button_1 = new JButton("テスト用：個別ページ処理開始");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gamenDTO DTO = new gamenDTO();
				DTO.setCheckErrLog(true);
				String DTOresult = setDTO(DTO,false);

				System.out.println("テスト用：個別ページ処理開始");



				renameDoresult.setText(DTOresult);
				if (!DTOresult.equals("成功")){
					return;
				}

				AzMangaLinkGetV2 azmanga = new AzMangaLinkGetV2(DTO);
				azmanga.getURL_Page_Individual(DTO);



//				renameDo aaa = new renameDo();
//				String resultRename = aaa.renameDoMain(DTO);
				renameDoresult.setText("ここに結果");
//				aaa = new renameDo();

			}
		});
		button_1.setBounds(48, 404, 216, 27);
		contentPane.add(button_1);

//		JLabel renameCreateResult = new JLabel("結果表示");
		renameCreateResult.setBounds(439, 185, 203, 19);
		contentPane.add(renameCreateResult);

//		JLabel renameDoresult = new JLabel("結果表示");
		renameDoresult.setBounds(439, 408, 203, 19);
		contentPane.add(renameDoresult);
	}



	//true:置き換えファイル作成,false:置き換え実行
	private String setDTO(gamenDTO DTO,boolean bottonCheak){

		if(bottonCheak){
			try {
				DTO.setMangURL_page(Integer.parseInt(pageNum.getText()));
			} catch (Exception e) {
				return "参照ページ数に数じゃない値がある";
			}
			DTO.setMangaURL(mangaURL.getText());
			DTO.setReNameListFileCreateFolderPath(renameListFileCreateFolderPath.getText());
			File file =  new File(DTO.getReNameListFileCreateFolderPath());
			if (file.isDirectory()==false){
				return "フォルダが存在しない";
			}

			DTO.setHashKey(reNameListFilePath.getText());



		}else{

			DTO.setReNamedFolderPath(renamedFolderPath.getText());
			DTO.setReNameListFilePath(reNameListFilePath.getText());
			DTO.setReNameListFileCreateFolderPath(renamedFolderPath.getText());
			File file =  new File(DTO.getReNamedFolderPath());

			if (file.isDirectory()==false){
				return "フォルダが存在しない";

			}
			file =  new File(DTO.getReNameListFilePath());
			if (file.isFile()==false){
				return "成功";
			}

		}





		return "成功";

	}

}
