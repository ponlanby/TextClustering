package cluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import vsm.CreateVSM;

public class TF_IDF {
	private static final String SOURCE_PATH = "C:\\Users\\Administrator\\Desktop\\segResult";
	private static final String RESULT_PATH = "C:\\Users\\Administrator\\Desktop\\TD_IDF";
	private static final String WORD_SPLITER = ",";	
	
	public static void test(HashMap<Integer, HashMap<String, Integer>> TF_IDF_List){
		Iterator it1 = TF_IDF_List.entrySet().iterator();
//		System.out.println(TF_IDF_List.size());
		int totalTxtNum = TF_IDF_List.size();
		while(it1.hasNext()){
			Map.Entry<Integer, HashMap<String, Integer>> entry= (Entry<Integer, HashMap<String, Integer>>) it1.next();
			Integer id = entry.getKey();
			System.out.println("TXT: " + id);
			HashMap<String, Integer> vsm = entry.getValue();
			Iterator it2 = vsm.entrySet().iterator();
			while(it2.hasNext()){
				Map.Entry<String, Integer> entry2 = (Entry<String, Integer>) it2.next();
				int numOfWord = entry2.getValue();
				int totalWordsInTxt = vsm.size();
			}
			System.out.println();
		}
	}
	

	public static HashMap<Integer, HashMap<String, Integer>> generateTD_IDF(File subRoot, HashSet<String> totalWords) throws IOException, FileNotFoundException{
//		File subRoot = new File(subRootPath);
		File[] directory = subRoot.listFiles();
		InputStreamReader isr = null;
		BufferedReader br = null;
		CreateVSM creator = new CreateVSM();
		HashMap<Integer, HashMap<String, Integer>> TF_IDF_List = new HashMap<Integer, HashMap<String, Integer>>();
		int txtID = 0;
		for(File dir:directory){
			File[] txts = dir.listFiles();
			for(File txt:txts){
				StringBuilder sb = new StringBuilder();
				isr = new InputStreamReader(new FileInputStream(txt), "UTF-8");
				br = new BufferedReader(isr);
				while(br.ready()){
					sb.append(br.readLine());
				}
				HashMap<String, Integer> vsm = creator.createVSM(totalWords, sb.toString());
				TF_IDF_List.put(Integer.valueOf(txtID++), vsm);
				System.out.println(txtID);
				System.out.println(sb.toString());
				isr.close();
				br.close();
			}
		}
		
		return TF_IDF_List;
		
		//test output
//		Iterator it1 = TF_IDF_List.entrySet().iterator();
//		System.out.println(TF_IDF_List.size());
//		while(it1.hasNext()){
//			Map.Entry<Integer, HashMap<String, Integer>> entry= (Entry<Integer, HashMap<String, Integer>>) it1.next();
//			Integer id = entry.getKey();
//			System.out.println("TXT: " + id);
//			HashMap<String, Integer> vsm = entry.getValue();
//			Iterator it2 = vsm.entrySet().iterator();
//			while(it2.hasNext()){
//				Map.Entry<String, Integer> entry2 = (Entry<String, Integer>) it2.next();
//				String word = entry2.getKey();
//				Integer count = entry2.getValue();
//				System.out.print(word + ": " + count + "    ");				
//			}
//			System.out.println();
//		}
	}
	
	public static HashSet<String> getTotalWordsInDir(File subRoot) throws IOException, FileNotFoundException{
		File[] directory = subRoot.listFiles();
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		BufferedReader br = null;
		HashSet<String> totalWords = new HashSet<String>();
		for(File dir:directory){
			File[] txts = dir.listFiles();
			for(File txt:txts){
				isr = new InputStreamReader(new FileInputStream(txt), "UTF-8");
				br = new BufferedReader(isr);
				while(br.ready()){
					sb.append(br.readLine());
				}
				String[] words = sb.toString().split(WORD_SPLITER);
				for(String word:words){
					word = word.replace("[", "");
					word = word.replace("]", "");
					totalWords.add(word);
				}
				isr.close();
				br.close();
			}
		}
		return totalWords;
	}
	
	public static double calculateTF(int numOfWord, int totalWordsInTxt){
		double TF = ((double)numOfWord)/((double)totalWordsInTxt);
		return TF;
	}
	
	public static double calculateIDF(int totalTxtNum, int txtNumWithTheWord){
		double IDF = java.lang.Math.log(((double)totalTxtNum)/((double)(txtNumWithTheWord+1)));
		return IDF;
	}
	
	public static double calculateTF_IDF(double TF, double IDF){
		double TF_IDF = TF * IDF;
		return TF_IDF;
	}
	
	public static void main(String[] args){
		try{
			File root = new File(SOURCE_PATH);
			File[] subRoots = root.listFiles();
			for(File subRoot:subRoots){
				HashSet<String> totalWords = getTotalWordsInDir(subRoot);
				generateTD_IDF(subRoot, totalWords);
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
