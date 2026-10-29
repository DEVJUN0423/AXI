package Noise_Cut_Off;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Regex extends JsonReader {
	
	public Regex(String firstlink, String lastlink) {
		super(firstlink, lastlink);
	}
    
    // 제거할 조사 리스트
    String RegexWolrd = "(은|는|이|가|을|를|과|와|에|에서|로|으로)$";

    public String worldSelecter(int i) throws IOException, ParseException {
        ArrayList<String> singleFileWords = new ArrayList<>();
        String fullText = getJsonType("raw_text", i);
        String[] tokens = fullText.split("\\s+"); // 띄어쓰기로 분리
        
        for(String token : tokens) {
            String cleanedWord = token.replaceAll(RegexWolrd, "");
            if(!cleanedWord.isEmpty()) {
                singleFileWords.add(cleanedWord);
            }
        }
        
        // 정제된 단어 리스트를 문자열(예: [단어1, 단어2, 단어3]) 형태로 직접 반환
        return singleFileWords.toString();
    }

    public void renamerawtext(int i, String cleanedText) {
        try {
            // 1. 파일 경로 지정 및 ObjectMapper 생성
            File jsonFile = new File(getFirstlink() + i + getLastlink());
            ObjectMapper mapper = new ObjectMapper();

            // 2. JSON 파일을 ObjectNode로 읽어오기
            ObjectNode rootNode = (ObjectNode) mapper.readTree(jsonFile);

            // 3. 'raw_text' 키의 값만 수정
            rootNode.put("raw_text", cleanedText);

            // 4. 수정된 내용을 다시 파일에 쓰기 (들여쓰기 포함해서 이쁘게 저장)
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, rootNode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
