package wordSegmenter;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

public class Test {
	public static void main(String[] args){
		List<Word> words = WordSegmenter.seg("���д���APDPlatӦ�ü���Ʒ����ƽ̨������");
		System.out.println(words);
	}
}
