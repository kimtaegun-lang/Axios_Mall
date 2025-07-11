package org.zerock.mallapi.repository;



import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.TodoDto;
import org.zerock.mallapi.service.TodoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoService todoService;
    @Test
    public void testInsert() {
        for(int i=1;i<=100;i++)
        {
            Todo todo=Todo.builder() // builder 객체에 필드값 저장
            .title("Title..."+i)
            .dueDate(LocalDate.of(2023,12,31))
            .writer("user00")
            .build();

            todoRepository.save(todo);
        }
    } //테스트를 통해 maria db에 값 저장하는 구문 

   /*  @Test 
    public void testRead() {
        Long tno=33L;
        java.util.Optional<Todo> result = todoRepository.findById(tno);
        Todo todo=result.orElseThrow();
        //result에 Todo 객체가 있으면 그 객체를 todo 변수에 저장하고

        // 만약 없으면 예외를 던져서 다음 코드가 실행되지 않도록 멈춥니다.


        log.info(todo);
    
        tno가 33번인 객체에 대해 로그를 찍는 코드

    } */

    /* @Test
    public void testModify() {
        Long tno=33L;
        Optional<Todo> result=todoRepository.findById(tno);
        Todo todo=result.orElseThrow();
        todo.changeTitle("Modified 33...");
        todo.changeComplete(true);
        todo.changeDueDate(LocalDate.of(2023,10,10));
        todoRepository.save(todo);
    }
        tno가 33인 객체에 대해 값 변경 하는 구문
*/

 /* @Test 
public void testDelete() {
    Long tno=1L;
    todoRepository.deleteById(tno);
}  */ // tno가 1번인 객체 삭제 

/* @Test 
public void testPaging(){
    Pageable pageable=PageRequest.of(0,10,Sort.by("tno").descending());
    Page<Todo> result=todoRepository.findAll(pageable);
    log.info(result.getTotalPages());
    result.getContent().stream().forEach(todo->log.info(todo));

    // 페이지 화 했을때 Page 데이터에는 getTotalPages:페이지 수, getTotalElements: 전체 레코드 수, getContent: 현재페이지 수를 나타냄
}*/

//@Test
public void testRegister() {
      TodoDto todoDTO=TodoDto.builder() 
            .title("서비스 테스트")
            .writer("tester")
            .dueDate(LocalDate.of(2023,10,10))
            .build();

    Long tno=todoService.register(todoDTO);
    log.info("TNO: "+tno);
}
}
