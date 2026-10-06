package Noise_Cut_Off;

import java.io.IOException;
import java.sql.Timestamp;

import org.json.simple.parser.ParseException;

import Noise_Cut_Off.AxiDb.OracleDB;
import Noise_Cut_Off.AxiDb.UserDataDao;
import Noise_Cut_Off.AxiDb.UserDataDto;
import Noise_Cut_Off.AxiDb.UserTextDao;
import Noise_Cut_Off.AxiDb.UserTextDto;
import Noise_Cut_Off.AxiDb.dbInsertRun;


// MessagerPreprocessor.java 통합 실행 함수
class Nco {
	  JsonReader reader = new JsonReader();
	  FileManager manager = new FileManager();
	  Regex worldNormal = new Regex();
	
	Boolean NcoRun() throws IOException, ParseException {
		  
			int setcount = reader.fileCount(); //메시지를 보낸 횟수 = 저장된 파일 개수
		  
		  // 텍스트 정보 정규화
			for(int i =1; i<=setcount; i++) {
				// rawtext 문자열에 json 파일에서 raw_text type 에 저장됨 문자열 불러오는 함수 호출 후 저장
				String rawtext = reader.rawText("raw_text", i);
				// json rawtext 보는 print
//		      System.out.println(i + " text: " + rawtext);
		      // rawtext 의미 없는 파일 제거 리스트 생성 함수
		      manager.textsum(i, rawtext);
			}
//			 정규화 탈락 파일 제거 + 파일 이름 정리
			manager.removeFile();
			
			//정규화 통과한 문자열 조사 제거 함수
			worldNormal.worldSelecter();
			
			//최종 파일 저장
			worldNormal.renamerawtext();
			
			return true;
	}
}
public class AXI {

	public static void main(String[] args) throws Exception {
		JsonReader reader = new JsonReader();
		Nco nco = new Nco();
        OracleDB oracleDB = new OracleDB();
        UserTextDao utdao = new UserTextDao(oracleDB);
        UserDataDao uddao = new UserDataDao(oracleDB);
        dbInsertRun runner = new dbInsertRun();
        
		// 전처리 통함 실행 함수 json 에 다시 저장됨
//		nco.NcoRun();
        
        runner.run();   // dbInsertRun의 로직을 여기서 실행
		System.out.println("Db 전송 완료");

	}

}

/*type(타입) : title(제목)

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
