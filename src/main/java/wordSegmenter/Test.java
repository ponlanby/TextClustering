package wordSegmenter;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

public class Test {
	public static void main(String[] args){
		List<Word> words = WordSegmenter.seg("杨尚川是APDPlat应用级产品开发平台的作者");
		System.out.println(words);
	}
}
