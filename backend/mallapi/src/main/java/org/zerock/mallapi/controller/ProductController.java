package org.zerock.mallapi.controller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


//11
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {
    private final CustomFileUtil fileUtil;
    private final ProductService productService;
    @PostMapping(value="/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,Long> register (@ModelAttribute ProductDTO productDTO) {
        log.info("register: "+productDTO);
        List<MultipartFile>files=productDTO.getFiles(); // 빈 객체여도 getFile해도 오류가 안난다!!
        List<String> uploadFileNames=fileUtil.saveFiles(files); // STRING으로 저장하는게 DB에 저장하기 유리하다.
        productDTO.setUploadFileNames(uploadFileNames);

        Long pno=productService.register(productDTO);

        log.info(uploadFileNames);
        return Map.of("result",pno);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") // 호출하려는 사용자가 ROLE_USER 또는 ROLE_ADMIN 권한을 가지고 있어야 허용한다는 뜻입니다.
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("list.............."+pageRequestDTO);

        return productService.getList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable(name="pno")Long pno)
    {
        return productService.get(pno);
    }

    @PutMapping(value="/{pno}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,String> modify(@PathVariable(name="pno")Long pno, @ModelAttribute ProductDTO productDTO)
    {
        productDTO.setPno(pno);
        ProductDTO oldProductDTO=productService.get(pno);
        List<String>oldFileNames=oldProductDTO.getUploadFileNames();
        List<MultipartFile>files=productDTO.getFiles();
        List<String>currentUploadFileNames=fileUtil.saveFiles(files);
        List<String>uploadedFileNames=productDTO.getUploadFileNames();
        if(currentUploadFileNames!=null&&currentUploadFileNames.size()>0)
        {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO);
        if(oldFileNames!=null&&oldFileNames.size()>0) 
        {
            List<String>removeFiles=oldFileNames
            .stream()
            .filter(fileName->uploadedFileNames.indexOf(fileName)==-1)
            .collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);
        }
        return Map.of("RESULT","SUCCESS");

    }

    @DeleteMapping("/{pno}")
    public Map<String,String> remove(@PathVariable(name="pno")Long pno)
    {
        List<String> oldFileNames=productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);
        return Map.of("RESULT","SUCCESS");
    }

}
