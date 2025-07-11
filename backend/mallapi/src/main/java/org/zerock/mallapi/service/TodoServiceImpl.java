package org.zerock.mallapi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.dto.TodoDto;
import org.zerock.mallapi.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable; 
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@Log4j2
@RequiredArgsConstructor //final이나 @NonNull에 대해서만 생성자 생성
public class TodoServiceImpl implements TodoService{
    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;
    @Override
    public Long register(TodoDto todoDto) {
        log.info(".........");
        Todo todo=modelMapper.map(todoDto,Todo.class);
        Todo savedTodo=todoRepository.save(todo);
        return savedTodo.getTno();
    }
    
    @Override
    public TodoDto get(Long tno) {
        Optional<Todo> result=todoRepository.findById(tno);
        Todo todo=result.orElseThrow();
        TodoDto dto=modelMapper.map(todo,TodoDto.class);
        return dto;
    }

    @Override
    public void modify(TodoDto todoDto)
    {
        Optional<Todo> result=todoRepository.findById(todoDto.getTno());

        Todo todo=result.orElseThrow();
        todo.changeTitle(todoDto.getTitle());
        todo.changeDueDate(todoDto.getDueDate());
        todo.changeComplete(todoDto.isComplete());

        todoRepository.save(todo);
    }
    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public PageResponseDTO<TodoDto>list(PageRequestDTO pageRequestDTO)
    {
        Pageable pageable=PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize(),Sort.by("tno").descending());

        Page<Todo>result=todoRepository.findAll(pageable);
        List<TodoDto>dtoList=result.getContent().stream()
        .map(todo->modelMapper.map(todo,TodoDto.class)).collect(Collectors.toList());

        long totalCount=result.getTotalElements();
        PageResponseDTO<TodoDto>responseDTO=PageResponseDTO.<TodoDto>withAll().dtoList(dtoList).pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();
        return responseDTO;
    }

 
}
