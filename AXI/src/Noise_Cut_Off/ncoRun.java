package Noise_Cut_Off;

import java.io.IOException;

import org.json.simple.parser.ParseException;

public class ncoRun {
	  JsonReader reader = new JsonReader("src/Noise_Cut_Off/json/test", ".json");
	  FileManager manager = new FileManager("src/Noise_Cut_Off/json/test", ".json");
	  TextFilter filter = new TextFilter("src/Noise_Cut_Off/NoiseCutOff.json");
	  Regex worldNormal = new Regex("src/Noise_Cut_Off/json/test", ".json");
	
	public void NcoRun() throws IOException, ParseException {
		  
			// 텍스트 정보 정규화
			for(int i =1; i<=reader.fileCount(); i++) {
				// rawtext 문자열에 json 파일에서 raw_text type 에 저장됨 문자열 불러오는 함수 호출 후 저장
				String rawtext = reader.getJsonType("raw_text", i);
				// json rawtext 보는 print
//		      System.out.println(i + " text: " + rawtext);
				
//				정규화 탈락 파일 제거 + 파일 이름 정리
				if(filter.textsum(i, rawtext) == true) {
					manager.removeFile(i);
					manager.renameFile(i);
				}
			}

			//정규화 통과한 문자열 조사 제거 함수
			worldNormal.worldSelecter();
			
			//최종 파일 저장
			worldNormal.renamerawtext();
			
	        System.out.println("파일 NCO 완료");
	}
}
