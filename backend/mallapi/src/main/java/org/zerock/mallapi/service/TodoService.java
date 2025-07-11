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
import org.zerock.mallapi.dto.TodoDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;



public interface TodoService {
        Long register(TodoDto todoDto);
        TodoDto get(Long tno);
        void modify(TodoDto todoDto);
        void remove(Long tno);
        PageResponseDTO<TodoDto> list(PageRequestDTO pageRequestDTO);
      
}
