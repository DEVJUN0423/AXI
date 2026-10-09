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
	JsonReader reader = new JsonReader("src/Noise_Cut_Off/json/test", ".json");
    
    // 바깥쪽 ArrayList는 파일들, 안쪽 ArrayList는 그 파일의 단어들을 담는 2차원 가변 배열
    ArrayList<ArrayList<String>> fileWordList = new ArrayList<>();
    
    // 제거할 조사 리스트
    String RegexWolrd = "(은|는|이|가|을|를|과|와|에|에서|로|으로)$";

    // 2차원 가변 문자열 배열에 조사와 뜨어쓰기를 제거한 단어만 저장하는 함수
    public void worldSelecter() throws IOException, ParseException {
        fileWordList.clear(); // 실행 전 초기화

        for(int i = 1; i <= reader.fileCount(); i++) {
            // 이번 파일의 단어들을 담을 '새로운 1차원 가변 배열'을 생성
            ArrayList<String> singleFileWords = new ArrayList<>();
            String fullText = reader.getJsonType("raw_text", i);
            String[] tokens = fullText.split("\\s+"); // 띄어쓰기로 분리
            
            for(String token : tokens) {
                String cleanedWord = token.replaceAll(RegexWolrd, "");
                if(!cleanedWord.isEmpty()) {
                    // 현재 파일 배열에 조사가 제거된 단어 추가 
                    singleFileWords.add(cleanedWord);
                }
            }
            // 파일 한 개의 단어 수집이 끝 -> 이를 전체 2차원 배열에 한 행(Row)으로 추가
            fileWordList.add(singleFileWords);
        }
        // 최종 2차원 배열 출력
        // System.out.println(fileWordList);
        for(int i =0; i<=reader.fileCount()-1; i++) {
//            System.out.println(i + " " + fileWordList.get(i));
        }
    }
    // rawtext 정규화 통과 파일 저장 함수
    public void renamerawtext() {
    	for(int i = 1; i<= reader.fileCount(); i++) {
            try {
                // 1. 파일 경로 지정 및 ObjectMapper 생성
                File jsonFile = new File(reader.getFirstlink() + i + reader.getLastlink());
                ObjectMapper mapper = new ObjectMapper();

                // 2. JSON 파일을 ObjectNode로 읽어오기
                ObjectNode rootNode = (ObjectNode) mapper.readTree(jsonFile);

                // 3. 'raw_text' 키의 값만 수정
                rootNode.put("raw_text", fileWordList.get(i-1).toString());

                // 4. 수정된 내용을 다시 파일에 쓰기 (들여쓰기 포함해서 이쁘게 저장)
                mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, rootNode);

//                System.out.println("test " + i + ".JSON 파일의 raw_text가 성공적으로 수정되었습니다.");

            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    }
}