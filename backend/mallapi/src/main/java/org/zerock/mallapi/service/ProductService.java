package org.zerock.mallapi.service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

@Transactional
public interface ProductService {
   // public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);
}
