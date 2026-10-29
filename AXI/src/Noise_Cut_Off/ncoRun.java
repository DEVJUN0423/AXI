package Noise_Cut_Off;

import java.io.IOException;

import org.json.simple.parser.ParseException;

public class ncoRun extends FileManager {
	
	private final Regex worldNormal;
	private final TextFilter filter;
	
	public ncoRun(String firstlink, String lastlink, String filterPath) {
		super(firstlink, lastlink);
		this.worldNormal = new Regex(firstlink, lastlink);
		this.filter = new TextFilter(filterPath);
	}
	
	public void NcoRun() throws IOException, ParseException {
		  
			// 텍스트 정보 정규화
			for(int i =1; i<=fileCount(); i++) {
				// rawtext 문자열에 json 파일에서 raw_text type 에 저장됨 문자열 불러오는 함수 호출 후 저장
				String rawtext = getJsonType("raw_text", i);
				// json rawtext 보는 print
//		      System.out.println(i + " text: " + rawtext);
				
//				정규화 탈락 파일 제거 + 파일 이름 정리
				if(filter.textsum(i, rawtext) == true) {
					removeFile(i);
					renameFile(i);
				}
			}

			//정규화 통과한 문자열 조사 제거 함수
			worldNormal.worldSelecter();
			
			//최종 파일 저장
			worldNormal.renamerawtext();
			
	        System.out.println("파일 NCO 완료");
	}
}
