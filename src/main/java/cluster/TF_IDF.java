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
	private static final String SOURCE_PATH = "C:\\Users\\Administrator\\Desktop\\segResult\\";
	private static final String RESULT_PATH = "C:\\Users\\Administrator\\Desktop\\TD_IDF\\";
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
				isr.close();
				br.close();
			}
			
//			OutputStreamWriter osw = null;
//			BufferedWriter bw = null;
//			File output = new File(RESULT_PATH + "VSM.txt");
//			osw = new OutputStreamWriter(new FileOutputStream(output),"UTF-8");
//			bw = new BufferedWriter(osw);
//			for(int i=0; i<vsmMatrix.length; i++){
//				for(int j=0; j<vsmMatrix[i].length; j++){
//					bw.write(vsmMatrix[i][j] + ",");
//				}
//				bw.write("\r\n");
//			}
//			bw.close();
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
		for(int i=0; i<vsmMatrix.length; i++){
			txtCount++;
			for(int j=0; j<vsmMatrix[i].length; j++){
				numOfWord = vsmMatrix[i][j];
				numOfTotalWords = 0;
				long starTime=System.currentTimeMillis();
				for(int k=0; k<vsmMatrix[i].length; k++){
					if(vsmMatrix[i][k]!=0){
						numOfTotalWords += 1;
					}
				}
				long endTime=System.currentTimeMillis();
				System.out.println(endTime-starTime);
				numOfTxtsContainWord = 0;
				starTime=System.currentTimeMillis();
				for(int k=0; k<vsmMatrix.length; k++){
					if(vsmMatrix[k][j]!=0){
						numOfTxtsContainWord++;
					}
				}
				endTime=System.currentTimeMillis();
				System.out.println(endTime-starTime);
				//calculate TF-IDF
				double TF = (double)numOfWord / (double)numOfTotalWords;
				double IDF = java.lang.Math.log( (double)numOfTotalTxts / (double)(numOfTxtsContainWord+1) );
				double TF_IDF = TF * IDF;
				
				tfidfMatrix[i][j] = String.valueOf(TF_IDF);
			}
			for(int l = 0; l<tfidfMatrix[i].length; l++){
				bw.write(tfidfMatrix[i][l] + ",");
			}
			bw.write("\r\n");
			System.out.println(txtCount);
		}
		
		bw.close();
		
		
//		System.out.println(tfidfMatrix);
		
		
		
	}
	
	public static ArrayList<String> getTotalWordsInDir(File subRoot) throws IOException, FileNotFoundException{
		File[] directory = subRoot.listFiles();
		InputStreamReader isr = null;
		BufferedReader br = null;
		HashSet<String> totalWords = new HashSet<String>();
		int count=0;
		
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		File output = new File(RESULT_PATH + "TotalWords.txt");
		osw = new OutputStreamWriter(new FileOutputStream(output),"UTF-8");
		bw = new BufferedWriter(osw);
		for(File dir:directory){
			File[] txts = dir.listFiles();
			for(File txt:txts){
				isr = new InputStreamReader(new FileInputStream(txt), "UTF-8");
				br = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				while(br.ready()){
					sb.append(br.readLine());
				}
				String[] words = sb.toString().split(WORD_SPLITER);
				for(String word:words){
					word = word.replace("[", "");
					word = word.replace("]", "");
					word = word.trim();
					bw.write(word + ",");
				}
				isr.close();
				br.close();
				System.out.println(count++);
				bw.flush();
				
			}
		}
		bw.close();
		
		isr = new InputStreamReader(new FileInputStream(RESULT_PATH + "TotalWords.txt"), "UTF-8");
		br = new BufferedReader(isr);
		String word = null;
		while(br.ready()){
			word = br.readLine();
		}
		word = word.replace("[", "");
		word = word.replace("]", "");
		String[] words = word.split(WORD_SPLITER);
		
		for(String w:words){
			w = w.trim();
			totalWords.add(w);
		}
		isr.close();
		br.close();
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
