package Noise_Cut_Off;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//파일 관리 함수
public class FileManager extends JsonReader{
	
	public FileManager(String firstlink, String lastlink) {
		super(firstlink, lastlink);
	}

	JsonReader reader = new JsonReader("src/Noise_Cut_Off/json/test", ".json");
	int removefilecount;
	
	//파일제거 -> 이름 변경 -> 파일 저장 함수 
	public void removeFile (int getnum) {
		
		//제거할 파일 번호 리스트 크기 저장
		System.out.println("[DEBUG] 제거할 파일 번호" + getnum);
		System.out.println("[DEBUG] 총 파일 개수: " + reader.fileCount());

		// 파일제거
		File deleteFile = new File(reader.getFirstlink() + getnum + reader.getLastlink());
		deleteFile.delete();
			
//		System.out.println("[DEBUG] 제거 완료한 파일 명: "+ getnum);s
//		System.out.println("==================================");
	}
	
	public void renameFile(int getnum) {
		
		// 반복 조건 변수 설정
		int forCount = reader.fileCount() - getnum + 1;
		System.out.println("[DEGUB] 이름 변경 " + forCount +"번 반복예정");
		 
		
		// 변경할 파일 이름 
		int smallFileName = getnum + 1;
		int changeFileName = getnum;
		
		for(int i = getnum; i<=forCount+1; i++) {
			// 파일 이름 변경 실행
			// 파일 변경
			
			// 변경할 파일 경로
			Path source = Paths.get(reader.getFirstlink() + smallFileName + reader.getLastlink());
			
			// 변경할 파일 이름
		    Path target = source.resolveSibling("test" + changeFileName + reader.getLastlink());
		    // 메모해둔 source 위치의 파일을 target 이름으로 실제로 변경(Rename)해라!
		    try {
		        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
//		         System.out.println("[DEBUG] 이름 변경 성공: " + source.getFileName() + " -> " + target.getFileName());
		    } catch (IOException e) {
		        System.out.println("[ERROR] 파일 이름 변경 실패! (파일이 없거나 사용 중입니다)");
		        e.printStackTrace();
		    }
			 System.out.println("[DEGUB] "+ i +" test" + smallFileName + "-> test" +changeFileName);
			
			// 변경할 변경할 이름 변수 +1
			smallFileName ++; changeFileName++;
//			System.out.println("==================================");
		}
	}
}
