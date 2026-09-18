package Noise_Cut_Off;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

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

class JsonReader {
	// json 파일 보낸 메시지 불러오기 함수
	String rawText (int getnum) 
	throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		try (Reader reader = new FileReader("src/Noise_Cut_Off/json/test" + getnum + ".json");){
			JSONObject jsonObject = (JSONObject) parser.parse(reader);	
			
			String rawtext = (String) jsonObject.get("raw_text");
			return rawtext;
		}
	}
	// 파일 개수 세기 함수
    int fileCount() {
    	int fileCount;
        File dir = new File("src/Noise_Cut_Off/json");
        
        // 파일만 필터링해서 개수 세기 (폴더는 제외)
        File[] files = dir.listFiles((d, name) -> name.matches("test\\d+\\.json"));
        fileCount = (files != null) ? files.length : 0;
        return fileCount;
    }
}
class TextFilter {
	String text;
	// 텍스트 공백 제거 함수
	boolean textNull(int getnum, String rawtext) {
		text = rawtext;
	    if (text == null || text.trim().isEmpty()) {
	    	// System.out.println("[DEBUG] 공백 감지: " + getnum + "번째 파일 제거필요");
	    	// 성공시 true 반환
	    	return true;
	    }else
	    	return false;
	}
	//한단어 제거 함수 
	boolean textlength(int getnum, String rawtext) {
		text = rawtext;
		text = text.replaceAll("\\s+", "");
		
	    if (text.length() == 1) {
	        // System.out.println("[DEBUG] 1글자 감지: " + getnum + "번째 파일 제거필요");
	        //예외 발생
	    	return true;
	    } else
	    	return false;
	}
	//특정 단어 제거 함수
	boolean textremove(int getnum, String rawtext) throws Exception{
		text = rawtext.replaceAll("\\s+", "");
		JSONParser parser = new JSONParser();
		// try() 괄호 안에 넣으면 자동으로 close() 해줍니다.
		Reader reader = new FileReader("src/Noise_Cut_Off/NoiseCutOff.json");
		JSONObject jsonObject = (JSONObject) parser.parse(reader);
		JSONArray agreement = (JSONArray) jsonObject.get("agreement");
			for(Object filter : agreement) {
				if(text.equals(filter.toString())) {
					return true;
				}
	        }
			return false;
	}
}
// 위의 함수 통함 실행 관리 함수 
class TextManager extends TextFilter {
	ArrayList<Integer> removeList = new ArrayList<>();
	int removeFileSum = 0;
	int stopCount = 0;
	//일괄 실행 함수 
	void textsum (int getnum, String rawtext) {
		stopCount = 0;
		
		boolean pass = textNull(getnum, rawtext);
		if (textNull(getnum, rawtext) == true) {
			stopCount =1;
		}else if (textlength(getnum, rawtext) == true) {
			stopCount =1;
		} else
			try {
				if (textremove(getnum, rawtext) == true) {
					stopCount =1;
				}else {
					/*System.out.println("	[DEBUG] 컨텍스트 정규화 할게 없습니다.");*/
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    //제거할 파일번호 저장
		if (stopCount == 1) {
			removeFileSum ++;
			removeList.add(getnum);
			// System.out.println("[DEGUB] " + getnum + "번째 파일 제거리스트 등록 ");
			// System.out.println("DEGUB] stopcount: " + stopCount + " getnum: " + getnum + " removeFileSum: " + removeFileSum);
		}
	}
}

// 파일 관리 함수
class FileManager extends TextManager{
    JsonReader reader = new JsonReader();
	int listSize;
	
	// 제거할 파일 리스트 값 표시 함수 
	void listRemovePrint () {
		listSize = removeList.size();
		System.out.print("[DEBUG] 제거할 파일 리스트 값 [");
		for (int i =0; i <= listSize-1; i++) {
			int temp =0;
			temp = removeList.get(i);
			System.out.print(temp + ", ");
		}
		System.out.print("]");
		System.out.println();
	}
	// 파일명 출력 함수
	
	void fileNamePrint() {
	    File dir = new File("src/Noise_Cut_Off/json");
	    
	    // test+숫자.json 형식의 파일만 필터링
	    File[] files = dir.listFiles((d, name) -> name.matches("test\\d+\\.json"));
	    
	    System.out.print("[DEBUG] 파일 목록 [");
	    if (files != null && files.length > 0) {
	        for (int i = 0; i < files.length; i++) {
	            System.out.print(files[i].getName());
	            
	            // 마지막 파일이 아닐 때만 쉼표(,)를 붙여 가독성을 높입니다.
	            if (i < files.length - 1) {
	                System.out.print(", ");
	            }
	        }
	    } else {
	        System.out.print("조건에 맞는 파일이 없습니다.");
	    }
	    System.out.println("]");
    }
	//파일제거 함수 
	void removeFile () {
		
		listSize = removeList.size(); //제거할 파일 번호 리스트 크기 저장
		System.out.println("[DEGUB] 제거할 파일 번호 리스트 크기 : " + listSize);
		System.out.println("[DEGUB] 총 파일 개수: " + reader.fileCount());

		// 제거할 파일 번호 리스트 가 빈리시트 시 종료
		
		while (!removeList.isEmpty()) {
			// 제거할 파일 리스트 값 표시
			// listRemovePrint();
			// 파일에 있는 파일 이름 표시
			// fileNamePrint();
			
			// 제거할 파일 번호 저장
			int removeNum = removeList.get(0);
			
			// 파일제거
			File deleteFile = new File("src/Noise_Cut_Off/json/test" + removeNum + ".json");
			deleteFile.delete();
			System.out.println("[DEGUB] test" + removeNum +".json 제거 완료");
			fileNamePrint();
			
			// 반복 조건 변수 설정
			int forCount = reader.fileCount() - removeNum + 1;
			// System.out.println("[DEGUB] 이름 변경 " + forCount +"번 반복예정");
			
			// 변경할 파일 이름 
			int smallFileName = removeNum + 1;
			int changeFileName = smallFileName -1;
			
			// 파일 이름 변경 실행
			for(int i = 1; i<= forCount; i++) {
				
				// 파일 변경
				// 변경할 파일 경로
				Path source = Paths.get("src/Noise_Cut_Off/json/test" + smallFileName + ".json");
				// 변경할 파일 이름
			    Path target = source.resolveSibling("test" + changeFileName +".json");
			    // 메모해둔 source 위치의 파일을 target 이름으로 실제로 변경(Rename)해라!
			    try {
			        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
			        // System.out.println("[DEBUG] 이름 변경 성공: " + source.getFileName() + " -> " + target.getFileName());
			    } catch (IOException e) {
			        System.out.println("[ERROR] 파일 이름 변경 실패! (파일이 없거나 사용 중입니다)");
			        e.printStackTrace();
			    }
				// System.out.println("[DEGUB] "+ i +" test" + smallFileName + "-> test" +changeFileName);
				
				// 변경할 변경할 이름 변수 +1
				smallFileName ++; changeFileName++;
			}
			// 제거할 파일 리스트 값 -1 반복문
			int listSize = removeList.size();
			for(int i =0; i < listSize; i++) {
				removeList.set(i, removeList.get(i)-1);
			}
			removeList.remove(0);
		}
	}
}
class Regex extends JsonReader {
    JsonReader reader = new JsonReader();
    
    // 바깥쪽 ArrayList는 파일들, 안쪽 ArrayList는 그 파일의 단어들을 담는 2차원 가변 배열
    ArrayList<ArrayList<String>> fileWordList = new ArrayList<>();
    
    // 제거할 조사 리스트
    String RegexWolrd = "(은|는|이|가|을|를|과|와|에|에서|로|으로)$";

    // 2차원 가변 문자열 배열에 조사와 뜨어쓰기를 제거한 단어만 저장하는 함수
    void worldSelecter() throws IOException, ParseException {
        fileWordList.clear(); // 실행 전 초기화

        for(int i = 1; i <= reader.fileCount(); i++) {
            // 이번 파일의 단어들을 담을 '새로운 1차원 가변 배열'을 생성
            ArrayList<String> singleFileWords = new ArrayList<>();
            
            String fullText = reader.rawText(i);
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
            System.out.println(i + " " + fileWordList.get(i));
        }
    }
    void renamerawtext() {
        JsonReader reader = new JsonReader();
    	for(int i = 1; i<= reader.fileCount(); i++) {
            try {
                // 1. 파일 경로 지정 및 ObjectMapper 생성
                File jsonFile = new File("src/Noise_Cut_Off/json/test" + i + ".json"); // 실제 파일 경로를 입력하세요.
                ObjectMapper mapper = new ObjectMapper();

                // 2. JSON 파일을 ObjectNode로 읽어오기
                ObjectNode rootNode = (ObjectNode) mapper.readTree(jsonFile);

                // 3. 'raw_text' 키의 값만 수정
                rootNode.put("raw_text", fileWordList.get(i-1).toString());

                // 4. 수정된 내용을 다시 파일에 쓰기 (들여쓰기 포함해서 이쁘게 저장)
                mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, rootNode);

                System.out.println("test " + i + ".JSON 파일의 raw_text가 성공적으로 수정되었습니다.");

            } catch (Exception e) {
                e.printStackTrace();
            }
    	}
    }
}
public class NCO {

	public static void main(String[] args) 
			throws IOException, ParseException {
		
        JsonReader reader = new JsonReader();
        FileManager manager = new FileManager();
        Regex worldNormal = new Regex();
        
		int setcount = reader.fileCount(); //메시지를 보낸 횟수 = 저장된 파일 개수
        
        // 텍스트 정보 정규화
		for(int i =1; i<=setcount; i++) {
			String rawtext = reader.rawText(i);
			// json rawtext 보는 print
	        // System.out.println("text: " + rawtext);
	        // rawtext 의미 없는 파일 제거 리스트 생성 함수
	        manager.textsum(i, rawtext);
		}
		// 정규화 탈락 파일 제거 + 파일 이름 정리
		manager.removeFile();
		
		worldNormal.worldSelecter();
		worldNormal.renamerawtext();
	}

}

/*
 type(타입) : title(제목)

body(본문, 생략 가능)

Resolves : #issueNo, ...(해결한 이슈 , 생략 가능)

See also : #issueNo, ...(참고 이슈, 생략 가능)

Type
Type 키워드	사용 시점
feat	새로운 기능 추가
fix	버그 수정
docs	문서 수정
style	코드 스타일 변경 (코드 포매팅, 세미콜론 누락 등)
기능 수정이 없는 경우
design	사용자 UI 디자인 변경 (CSS 등)
test	테스트 코드, 리팩토링 테스트 코드 추가
refactor	코드 리팩토링
build	빌드 파일 수정
ci	CI 설정 파일 수정
perf	성능 개선
chore	빌드 업무 수정, 패키지 매니저 수정 (gitignore 수정 등)
rename	파일 혹은 폴더명을 수정만 한 경우
remove	파일을 삭제만 한 경우

관련 이슈
사용 시점	사용 키워드
해결	Closes(종료), Fixes(수정), Resolves(해결)
참고	Ref(참고), Related to(관련), See also(참고)
 */


