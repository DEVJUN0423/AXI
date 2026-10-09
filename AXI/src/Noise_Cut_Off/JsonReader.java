package Noise_Cut_Off;

import java.util.ArrayList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class JsonReader {
	
	// 파일경로 저장 문자열변수
	private String firstlink;
	private String lastlink;
	
	public JsonReader (String firstlink, String lastlink) {
		this.firstlink = firstlink;
		this.lastlink = lastlink;
	}
	
	public String getFirstlink() {
		return firstlink;
	}
	public String getLastlink() {
		return lastlink;
	}
	// json 파일 보낸 메시지 불러오기 함수
	public String getJsonType (String type, int getnum) throws IOException, ParseException {

		JSONParser parser = new JSONParser();
		
		try (Reader reader = new FileReader(getFirstlink() + getnum + getLastlink());) {
//			/Users/yanghyunjun/git/AXI/AXI/src/Noise_Cut_Off/json/test1.json 임시 상대 경로
			JSONObject jsonObject = (JSONObject) parser.parse(reader);	
			
			String rawtext = (String) jsonObject.get(type);
			return rawtext;
		}
	}
	// 파일 개수 세기 함수
    public int fileCount() {
    	int fileCount;
    	// 파일명 저장 문자열
    	String fileName = "src/Noise_Cut_Off/json";
        File dir = new File(fileName);
        
        // 파일만 필터링해서 개수 세기 (폴더는 제외)
        File[] files = dir.listFiles((d, name) -> name.matches("test\\d+\\.json"));
        fileCount = (files != null) ? files.length : 0;
        return fileCount;
    }
}