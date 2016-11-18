package wordSegmenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

public class Segmenter {
	
	private static final String ROOT_PATH = "D:\\Documents\\Tencent Files\\545852570\\FileRecv\\corp2\\";
	private static final String RESULT_PATH = "C:\\Users\\Administrator\\Desktop\\segResult\\";

	public static void main(String[] args) throws Exception{
//		String rootPath = "C:\\Users\\Administrator\\Desktop\\corp\\canyinmeishi\\";
//		File root = new File(rootPath);
//		File[] directory = root.listFiles();
//		
//		for(File dir:directory){
//			TxtSegmenter(dir);
//		}
		
		File root = new File(ROOT_PATH);
		File[] subRoots = root.listFiles();
		for(File subRoot:subRoots){
			File[] directory = subRoot.listFiles();
			for(File dir:directory){
				TxtSegmenter(dir);
			}
		}
		System.out.println("Finish");
		
	}
	
	public static void TxtSegmenter(File directory) throws Exception{
		File[] txts = directory.listFiles();
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try{
			for(File txt:txts){
				String txtPath = txt.getPath();
				String resultTxtPath = txtPath.replace(ROOT_PATH, RESULT_PATH);
				//convert file name to utf-8
				resultTxtPath = new String(resultTxtPath.getBytes(), "utf-8");
				//for those file without extension name
//				if(resultTxtPath.contains("_txt")){
//					resultTxtPath.replace("_txt", ".txt");
//				}
				if(!resultTxtPath.contains(".txt")){
					resultTxtPath += ".txt";					
				}
				File output = new File(resultTxtPath);
				//if the directory does not exist, create one
				if(!output.getParentFile().exists()){
					output.getParentFile().mkdirs();					
				}
				osw = new OutputStreamWriter(new FileOutputStream(output),"UTF-8");
				bw = new BufferedWriter(osw);
//				System.out.println(segmentResult);				
				String segmentResult = Segment(txt);
				bw.write(segmentResult);
				bw.flush();
			}
//			if(bw==null){
//				bw.close();				
//			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			bw.close();
		}
	}
	
	public static String Segment(File txt) throws Exception, FileNotFoundException{
		InputStreamReader isr = new InputStreamReader(new FileInputStream(txt), "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		while(br.ready()){
			sb.append(br.readLine());
		}
		List<Word> words = WordSegmenter.seg(sb.toString());
		br.close();
		String result = words.toString();
		result = result.replace("[", "");
		result = result.replace("]", "");
		return result;
	}
}
