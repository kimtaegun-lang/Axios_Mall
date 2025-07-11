package org.zerock.mallapi.service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;



@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    /* @Override
    public PageResponseDTO<ProductDTO>getList(PageRequestDTO pageRequestDTO)
    {
        log.info("getList................");
        Pageable pageable=PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize(),Sort.by("pno").descending());
        Page<Object[]> result=productRepository.selectList(pageable);
    }
*/
    
}
