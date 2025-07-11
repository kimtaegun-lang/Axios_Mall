package org.zerock.mallapi.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import net.coobird.thumbnailator.Thumbnails;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {
        @Value("${org.zerock.upload.path}")
        private String uploadPath;

        @PostConstruct // Spring에서 Bean이 초기화된 직후 자동으로 실행할 메서드에 붙이는
        public void init() {
            File tempFolder=new File(uploadPath);
            if(tempFolder.exists()==false)
            {
                tempFolder.mkdir();
            }
            uploadPath=tempFolder.getAbsolutePath();
            log.info("_______________________________________________");
            log.info(uploadPath);
        }

        public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException{
            if(files==null||files.size()==0) {
                return null;
            }

            List<String>uploadNames=new ArrayList<>();
            for(MultipartFile multipartFile : files) {
                String savedName=UUID.randomUUID().toString()+"_"+multipartFile.getOriginalFilename(); // 랜덤 UUID_+파일명으로 파일 이름 안겹치게!
                Path savePath=Paths.get(uploadPath,savedName); // uploadPath 디렉토리에 savedName 파일을 저장할 경로를 설정
                try {
                    Files.copy(multipartFile.getInputStream(),savePath); // 클라이언트가 보낸 파일의 InputStream을 받아서, 위에서 만든 경로에 복사 저장함 (서버 디스크에 저장!)
                    // inputstream을 사용하는 이유? outofmemory 방지하기때문! 파일 전체를 메모리에 올리지 않고, 조금씩 읽고 바로 저장할 수 있어요.
                    // 이 부분에서 저장이 일어남
                    String contentType=multipartFile.getContentType(); // 사용자가 올린 파일이 어떤 종류인지 알려주는 정보예요. 예: "image/png", "image/jpeg", "application/pdf" 등
                    // mimi 타입이래
                    if(contentType!=null&&contentType.startsWith("image")) {
                        Path thumbnailPath=Paths.get(uploadPath,"s_"+savedName);

                        Thumbnails.of(savePath.toFile())
                        .size(200,200)
                        .toFile(thumbnailPath.toFile()); // 썸네일 만드는 과정
                    }
                    uploadNames.add(savedName);
                }
                catch(IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            return uploadNames; // 업로드한 이미지 파일을 확인하기 위해 리턴함ㅁ
        }

        public ResponseEntity<Resource>getFile (String fileName)
        {
            Resource resource = new FileSystemResource(uploadPath+File.separator+fileName); // 서버 디스크의 실제 파일을 나타내는 Resource 객체를 생성합니다. uploadPath + File.separator + fileName → 파일 경로를 문자열로 만들고 FileSystemResource는 그 경로의 파일을 읽을 수 있는 Resource 타입으로 만듭니다

            if(!resource.exists())
            {
                resource=new FileSystemResource(uploadPath+File.separator+"default.jpeg"); // 대신 **기본 이미지(default.jpeg)**를 읽어 반환하도록 resource를 교체합니다.
            } // 이렇게 하면 OS에 맞게 경로 구분자가 자동으로 들어가서, (File.separator을 사용하면,)
            HttpHeaders headers = new HttpHeaders(); // HTTP 응답 헤더를 담을 객체를 새로 만듭니다.

            try {
                headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath())); // Files.probeContentType(Path)로 파일의 MIME 타입을 찾아서 HTTP 응답 헤더의 "Content-Type"에 넣습니다.
                // 파일을 얻고 이를 path객체로 변환 후, 그 파일의 **컨텐츠 타입(MIME 타입)**을 **추측(판단)**해 줍니다.예를 들어, 이미지 파일이면 "image/png" 같은 문자열을 반환해요.
            }
            catch(Exception e)
            {
                return ResponseEntity.internalServerError().build();
            }
            return ResponseEntity.ok().headers(headers).body(resource); // HTTP 상태 200(OK)와 헤더, 그리고 실제 파일 내용(resource)을 응답 본문에 담아 반환합니다.
        }

        public void deleteFiles(List<String>fileNames)
        {
            if(fileNames==null||fileNames.size()==0)
            {
                return;
            }

            fileNames.forEach(fileName-> {
                String thumbnailFileName="s_"+fileName;
                Path thumbnailPath=Paths.get(uploadPath,thumbnailFileName);
                Path filePath=Paths.get(uploadPath,fileName);

                try {
                    Files.deleteIfExists(filePath); // 각각 썸네일과 파일 삭제
                    Files.deleteIfExists(thumbnailPath);
                }
                catch(IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        }
}
