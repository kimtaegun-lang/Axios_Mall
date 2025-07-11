package org.zerock.mallapi.controller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


//
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;

    @PostMapping("/")
    public Map<String,String> register (ProductDTO productDTO) {
        log.info("register: "+productDTO);
        List<MultipartFile>files=productDTO.getFiles(); // 빈 객체여도 getFile해도 오류가 안난다!!
        List<String> uploadFileNames=fileUtil.saveFiles(files); // STRING으로 저장하는게 DB에 저장하기 유리하다.
        productDTO.setUploadFileNames(uploadFileNames);
        log.info(uploadFileNames);
        return Map.of("RESULT","SUCCESS");
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }
    

}
