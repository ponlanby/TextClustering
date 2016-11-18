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
	
	public static void test(File subRoot, ArrayList<String> totalWords) throws IOException, FileNotFoundException{
		File[] directory = subRoot.listFiles();
		InputStreamReader isr = null;
		BufferedReader br = null;
		CreateVSM creator = new CreateVSM();
		
//		int txtID = 0;
		for(File dir:directory){
			File[] txts = dir.listFiles();
			ArrayList<String> txtNameList = new ArrayList<String>();
			ArrayList<int[]> vsmMatrix = new ArrayList<int[]>();
			for(File txt:txts){
				String fileName = txt.getName();
				txtNameList.add(fileName);
				StringBuilder sb = new StringBuilder();
				isr = new InputStreamReader(new FileInputStream(txt), "UTF-8");
				br = new BufferedReader(isr);
				while(br.ready()){
					sb.append(br.readLine());
				}
				int[] vsmVector = creator.createVSM(totalWords, sb.toString());
				vsmMatrix.add(vsmVector);
				isr.close();
				br.close();
			}
			//calculate TF_IDF matrix
			calculateTFIDFMatrix(vsmMatrix);
			
		}
	}
	
	public static void calculateTFIDFMatrix(ArrayList<int[]> vsmMatrix){
		ArrayList<int[]> tfidfMatrix = new ArrayList<int[]>();
		http://www.chepoo.com/tf-idf-java-implementation.html
	}
	

	public static HashMap<Integer, HashMap<String, Integer>> generateTD_IDF(File subRoot, HashSet<String> totalWords) throws IOException, FileNotFoundException{
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
	}
	
	public static ArrayList<String> getTotalWordsInDir(File subRoot) throws IOException, FileNotFoundException{
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
		return new ArrayList<String>(totalWords);
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
