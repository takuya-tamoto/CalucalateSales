package demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CalculateSales {

	public static void main(String[] args) {

		HashMap<String, String> branchName = new HashMap<String, String>();
		HashMap<String, Long> branchSale = new HashMap<String, Long>();


		/*
		 * 支店定義ファイル読み込み後、
		 * BranchNameにハッシュマップで格納
		 */
		File file = new File(args[0], "branch.lst");
		if(!file.exists()) {
			System.out.println("定義ファイルが存在しません");
			return;
		}
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null) {
				String[] branch = line.split(",");
				if(!line.matches("[0-9]{3},.+$")) {
					System.out.println("定義ファイルのフォーマットが不正です");
					return;
				}
				branchName.put(branch[0], branch[1]);
				branchSale.put(branch[0], 0L);
			}
		} catch(IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;

		} finally {
			try {
				br.close();
			} catch(IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}

		/*
		 * 売上集計を行う。支店定義ファイルと同様に読み込みを行う
		 * 読み込んだ売上部分をBranchSaleのハッシュマップに格納する処理
		 */
		File dir = new File(args[0]); //指定したディレクトリへ
		File[] list = dir.listFiles(); //ファイル一覧を配列で取得
		ArrayList<String> getList = new ArrayList<String>(); //読み取りするファイルをリストで取得
		for(int i = 0; i < list.length; i++) {
			if(list[i].getName().matches("[0-9]{8}.rcd")) { //8桁.rcdであればgetListに追加
				getList.add(list[i].getName());
			}
		}
		//getListファイルの読み込み(支店コードと売上）
		for(int i = 0; i < getList.size(); i++) {
			try {
				File totalSale = new File(args[0], getList.get(i)); //指定したディレクトリから
				FileReader fr = new FileReader(totalSale);
				br = new BufferedReader(fr);
				String saleKey = br.readLine(); //一行読み込み後saleKeyへ代入。

				//キーが有るかどうかを調べるcontainsKey
				if (!branchName.containsKey(saleKey)) {
					System.out.println(getList.get(i) + "の支店コードが不正です");
					return;
				}
				//現在の売上＋売上ファイルの計算したものを代入
				long sale = branchSale.get(saleKey) + Long.parseLong(br.readLine());

				if(sale > 9999999999L) {
					System.out.println("合計金額が10桁を超えました");
					return;
				}

				//マップに支店コードと売上格納
				branchSale.put(saleKey, sale);

				if((br.readLine()) != null) {
					System.out.println(getList.get(i) + "のフォーマットが不正です");
					return;
				}
			} catch(IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;

			} finally {
				try {
					br.close();
				} catch(IOException e) {
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
			}
		}

		/*
		 * 支店コード、支店名、売上を書き出す処理
		 */
		BufferedWriter bw = null;
		try {
			File outFile = new File(args[0], "branch.out");
			FileWriter fw = new FileWriter(outFile);
			bw = new BufferedWriter(fw);

			for(String key: branchSale.keySet()){ //HashMapに格納しているものからの取り出しを行う
				long value = branchSale.get(key);
				String name = branchName.get(key);

				bw.write(key + "," + name + "," + value);
				bw.newLine(); //改行
			}

		} catch(IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;

		} finally {
			try {
				bw.close();
			} catch(IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}
	}
}