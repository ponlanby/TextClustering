package cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import vsm.CreateVSM;

public class TF_IDF {
	private static final String SOURCE_PATH = "E:\\segResult\\";
	private static final String RESULT_PATH = "E:\\TD_IDF\\";
	private static final String WORD_SPLITER = ",";	
	
	public static void test(File subRoot, ArrayList<String> totalWords) throws Exception{
		File[] directory = subRoot.listFiles();
		InputStreamReader isr = null;
		BufferedReader br = null;
		CreateVSM creator = new CreateVSM();
		
//		int txtID = 0;
		for(File dir:directory){
			File[] txts = dir.listFiles();
			ArrayList<String> txtNameList = new ArrayList<String>();
//			ArrayList<int[]> vsmMatrix = new ArrayList<int[]>();
			int[][] vsmMatrix = new int[txts.length][totalWords.size()];
			int txtCount = 0;
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
				vsmMatrix[txtCount] = vsmVector;
				txtCount++;
				
//				vsmMatrix.add(vsmVector);
				isr.close();
				br.close();
			}
			//calculate TF_IDF matrix
			System.out.println("calculate tfidf in " + dir.getName());
			calculateTFIDFMatrix(vsmMatrix);
			
		}
	}
	
	public static void calculateTFIDFMatrix(int[][] vsmMatrix) throws Exception, FileNotFoundException{
		String[][] tfidfMatrix = new String[vsmMatrix.length][vsmMatrix[0].length];
		//total Txt numbers in this category
		int numOfTotalTxts = tfidfMatrix.length;
		//txt numbers containing this word
		int numOfTxtsContainWord = 0;
		//the number that the word appears
		int numOfWord = 0;
		//total number of words in the txt
		int numOfTotalWords = 0;
		
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		File output = new File(RESULT_PATH + "TD_IDF.txt");
		osw = new OutputStreamWriter(new FileOutputStream(output),"UTF-8");
		bw = new BufferedWriter(osw);
		
		int txtCount = 0;
		for(int[] vsmvector:vsmMatrix){
			for(int i=0; i<vsmvector.length; i++){
				numOfWord = vsmvector[i];
				numOfTotalWords = 0;
				for(int vsm:vsmvector){
					if(vsm!=0){
						numOfTotalWords += 1;
					}
				}
				numOfTxtsContainWord = 0;
				for(int[] vsm:vsmMatrix){
					if(vsm[i]!=0){
						numOfTxtsContainWord++;
					}
				}
				//calculate TF-IDF
				double TF = (double)numOfWord / (double)numOfTotalWords;
				double IDF = java.lang.Math.log( (double)numOfTotalTxts / (double)(numOfTxtsContainWord+1) );
				double TF_IDF = TF * IDF;
				
				tfidfMatrix[txtCount][i] = String.valueOf(TF_IDF);
			}
			for(int l = 0; l<tfidfMatrix[txtCount].length; l++){
				bw.write(tfidfMatrix[txtCount][l] + ",");
			}
			bw.write("\r\n");
		}
		
		
//		System.out.println(tfidfMatrix);
		
		
		
	}
	

//	public static HashMap<Integer, HashMap<String, Integer>> generateTD_IDF(File subRoot, HashSet<String> totalWords) throws IOException, FileNotFoundException{
//		File[] directory = subRoot.listFiles();
//		InputStreamReader isr = null;
//		BufferedReader br = null;
//		CreateVSM creator = new CreateVSM();
//		HashMap<Integer, HashMap<String, Integer>> TF_IDF_List = new HashMap<Integer, HashMap<String, Integer>>();
//		int txtID = 0;
//		for(File dir:directory){
//			File[] txts = dir.listFiles();
//			for(File txt:txts){
//				StringBuilder sb = new StringBuilder();
//				isr = new InputStreamReader(new FileInputStream(txt), "UTF-8");
//				br = new BufferedReader(isr);
//				while(br.ready()){
//					sb.append(br.readLine());
//				}
//				HashMap<String, Integer> vsm = creator.createVSM(totalWords, sb.toString());
//				TF_IDF_List.put(Integer.valueOf(txtID++), vsm);
//				System.out.println(txtID);
//				System.out.println(sb.toString());
//				isr.close();
//				br.close();
//			}
//		}
//		
//		return TF_IDF_List;
//	}
	
	public static ArrayList<String> getTotalWordsInDir(File subRoot) throws IOException, FileNotFoundException{
		File[] directory = subRoot.listFiles();
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		BufferedReader br = null;
		HashSet<String> totalWords = new HashSet<String>();
		int count=0;
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
					word = word.trim();
					totalWords.add(word);
				}
				isr.close();
				br.close();
				System.out.println(count++);
			}
//			System.out.println(count++);
		}
		return new ArrayList<String>(totalWords);
	}
	
//	public static double calculateTF(int numOfWord, int totalWordsInTxt){
//		double TF = ((double)numOfWord)/((double)totalWordsInTxt);
//		return TF;
//	}
//	
//	public static double calculateIDF(int totalTxtNum, int txtNumWithTheWord){
//		double IDF = java.lang.Math.log(((double)totalTxtNum)/((double)(txtNumWithTheWord+1)));
//		return IDF;
//	}
//	
//	public static double calculateTF_IDF(double TF, double IDF){
//		double TF_IDF = TF * IDF;
//		return TF_IDF;
//	}
	
	public static void main(String[] args){
		try{
			File root = new File(SOURCE_PATH);
			File[] subRoots = root.listFiles();
			for(File subRoot:subRoots){
				ArrayList<String> totalWords = getTotalWordsInDir(subRoot);
				System.out.println("get total words in " + subRoot.getName());
//				generateTD_IDF(subRoot, totalWords);
				test(subRoot, totalWords);
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
