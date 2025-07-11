package org.zerock.mallapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDto;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class TodoServiceTests {
        @Autowired
    private TodoService todoService;
    //@Test
    public void testGet() {
        Long tno=33L;
        TodoDto todoDto=todoService.get(tno);
        log.info(todoDto);
    }
    @Test 
    public void testList() {
        PageRequestDTO pageRequestDTO=PageRequestDTO.builder().page(2).size(10).build();

        PageResponseDTO<TodoDto>response=todoService.list(pageRequestDTO);
        log.info(response);
    }
}
