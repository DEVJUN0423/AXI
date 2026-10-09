package main;

import Noise_Cut_Off.FileManager;
import Noise_Cut_Off.JsonReader;
import Noise_Cut_Off.ncoRun;

import AxiDb.dbInsertRun;


public class AXI {

	public static void main(String[] args) throws Exception {
    	
		JsonReader reader = new JsonReader("src/Noise_Cut_Off/json/test", ".json");
		FileManager manager = new FileManager("src/Noise_Cut_Off/json/test", ".json");
		ncoRun nco = new ncoRun();
        dbInsertRun runner = new dbInsertRun();
        

		// 전처리 통함 실행 함수 json 에 다시 저장됨
		nco.NcoRun();
        
        // dbInsertRun의 로직을 여기서 실행
        runner.run();
		System.out.println("Db 전송 완료");
        
		int temp = reader.fileCount();
		for(int i = 1; i <=temp; i++) {
			manager.removeFile(i);
		}
		System.out.println("db 전송후 남은 파일 제거 완료");

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
