package Noise_Cut_Off;

import java.io.FileReader;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TextFilter {
	String text;
	
	private String link;
	
	public TextFilter (String link) {
		this.link = link;
	}
	public String getlink() {
		return link;
	}
	// 텍스트 공백 감지 함수
	boolean textNull(int getnum, String rawtext) {
		text = rawtext;
	    if (text == null || text.trim().isEmpty()) {
	    	// System.out.println("[DEBUG] 공백 감지: " + getnum + "번째 파일 제거필요");
	    	// 성공시 true 반환
	    	return true;
	    }else {
	    	return false;
	    }
	}
	
	//한단어 감지 함수 
	boolean textlength(int getnum, String rawtext) {
		text = rawtext;
		text = text.replaceAll("\\s+", "");
		
	    if (text.length() == 1) {
	        // System.out.println("[DEBUG] 1글자 감지: " + getnum + "번째 파일 제거필요");
	        //예외 발생
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	//특정 단어 감지 함수
	boolean textremove(int getnum, String rawtext) throws Exception{
		text = rawtext.replaceAll("\\s+", "");
		JSONParser parser = new JSONParser();
		// try() 괄호 안에 넣으면 자동으로 close() 해줍니다.
		Reader reader = new FileReader(getlink());
		JSONObject jsonObject = (JSONObject) parser.parse(reader);
		JSONArray agreement = (JSONArray) jsonObject.get("agreement");
			for(Object filter : agreement) {
				if(text.equals(filter.toString())) {
					return true;
				}
	        }
			return false;
	}
	
	//text filterong class 일괄 실행 함수 제거할 파일이면 true 반환
	public boolean textsum (int getnum, String rawtext) {
		boolean pass = textNull(getnum, rawtext);
		if (textNull(getnum, rawtext) == true) {
			return true;
		}else if (textlength(getnum, rawtext) == true) {
			return true;
		} else
			try {
				if (textremove(getnum, rawtext) == true) {
					return true;
				}else {
					return false;
					/*System.out.println("	[DEBUG] 컨텍스트 정규화 할게 없습니다.");*/
				}
			} catch (Exception e) {
				e.printStackTrace(); }
		return false;
	}
}
