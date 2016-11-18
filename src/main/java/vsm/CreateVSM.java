package vsm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CreateVSM {
	private static final String SOURCE_PATH = "C:\\Users\\Administrator\\Desktop\\segResult";
	private static final String RESULT_PATH = "C:\\Users\\Administrator\\Desktop\\vsm";
	private static final String WORD_SPLITER = ",";
	
	public HashMap<String, Integer> createVSM(HashSet<String> totalWordList, String txtContent){
		HashMap<String, Integer> vsm = new HashMap<String, Integer>();
		txtContent = txtContent.replace(" ", "");
		txtContent = txtContent.replace("[", "");
		txtContent = txtContent.replace("]", "");
		String[] wordList = txtContent.split(WORD_SPLITER);	
		//put all words from txt file into vsm first
		for(String word:wordList){
			if(!vsm.containsKey(word)){
				vsm.put(word, 1);
			}
			else{
				vsm.put(word, vsm.get(word)+1);
			}
		}
		//add rest words from totalWordList to vsm
		Iterator<String> it = totalWordList.iterator();	
		while(it.hasNext()){
			String key = it.next();
			key = key.trim();
			if(!vsm.containsKey(key)){
				vsm.put(key, 0);
			}
		}
		return vsm;
		
		//output result for test use
//		Iterator it2 = vsm.entrySet().iterator();
//		while(it2.hasNext()){
//			Map.Entry<String, Integer> entry = (Entry<String, Integer>) it2.next();
//			String key = entry.getKey();
//			Integer value = entry.getValue();
////			System.out.println(key + " " + value);
//		}
//		
//		return vsm;
	}
	
	/*	for test use
	public static void main(String[] args) throws FileNotFoundException, IOException{
		File root = new File(SOURCE_PATH);
		File[] subRoots = root.listFiles();
		for(File subRoot:subRoots){
			File[] directory = subRoot.listFiles();
			for(File dir:directory){
				File[] txts = dir.listFiles();
				for(File txt:txts){
					String txtContent = ReadContent(txt);
					//temp
					txtContent = txtContent.replace("[", "");
					txtContent = txtContent.replace("]", "");					
//					System.out.println(txtContent);
					createVSM(txtContent);
					
				}
			}
		}
		
	}	
	
	public static String ReadContent(File txtFile) throws IOException, FileNotFoundException{
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(new FileInputStream(txtFile), "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		while(br.ready()){
			sb.append(br.readLine());
		}
		br.close();
		return sb.toString();
	}
	*/
}
