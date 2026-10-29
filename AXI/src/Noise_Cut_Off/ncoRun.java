package Noise_Cut_Off;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

public class ncoRun extends FileManager {
	
	private final Regex worldNormal;
	private final TextFilter filter;
	
	public ncoRun(String firstlink, String lastlink, String filterPath) {
		super(firstlink, lastlink);
		this.worldNormal = new Regex(firstlink, lastlink);
		this.filter = new TextFilter(filterPath);
	}
	
	public List<Integer> NcoRun() throws IOException, ParseException {		
		int totalFileCount = fileCount();
        List<Integer> fileDeleteNum = new ArrayList<>();
		int temp = 0;
		  
		// 텍스트 정보 정규화
		for(int i =1; i<=totalFileCount; i++) {
			// rawtext 문자열에 json 파일에서 raw_text type 에 저장됨 문자열 불러오는 함수 호출 후 저장
			String rawtext = getJsonType("raw_text", i);
			// json rawtext 보는 print
//		     System.out.println(i + " text: " + rawtext);
				
//			정규화 탈락 파일 제거 + 파일 이름 정리
			if(filter.textsum(i, rawtext) == true) {
				removeFile(i);
				fileDeleteNum.add(i);
				temp ++;
//				renameFile(i);
			}
			if(fileDeleteNum.contains(i)) {
				System.out.println(i + "번 파일 삭제됨 문자열 정규화 건너뜀");
			} else {
				//정규화 통과한 문자열 조사 제거 함수
				String cleanedText = worldNormal.worldSelecter(i);
				//최종 파일 저장
				worldNormal.renamerawtext(i, cleanedText);
			}
		}
		System.out.println("제거한 파일 번호 리스트");
		System.out.println(fileDeleteNum);
		System.out.println("제거한 파일개수 " + temp);
        System.out.println("파일 NCO 완료");
        
		return fileDeleteNum;
	}
}
