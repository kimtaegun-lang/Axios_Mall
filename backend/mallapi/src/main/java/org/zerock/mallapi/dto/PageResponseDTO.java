package org.zerock.mallapi.dto;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
public class PageResponseDTO<E> {
   private List<E> dtoList;
   private List<Integer>pageNumList;
   private PageRequestDTO pageRequestDTO;
   private boolean prev,next;
   private int totalCount, prevPage,nextPage,totalPage,current;

   @Builder(builderMethodName = "withAll")
   public PageResponseDTO(List<E> dtoList,PageRequestDTO pageRequestDTO,long totalCount)
   {
    this.dtoList=dtoList;
    this.pageRequestDTO=pageRequestDTO;
    this.totalCount=(int)totalCount;

    int end=(int)(Math.ceil(pageRequestDTO.getPage()/10.0))*10; // 해당 페이지의 끝번호를 나타냄
    int start=end-9; // 현재 페이지의 첫번째 번호를 나타냄
    int last=(int)(Math.ceil((totalCount/(double)pageRequestDTO.getSize()))); // 마지막 페이지 번호를 나타냄

    end=end>last?last:end; // 해당 페이지의 끝 번호가 마지막 페이지 번호보다 클 경우,
    this.prev=start>1;
    this.next=totalCount>end*pageRequestDTO.getSize();

    this.pageNumList=IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
//start부터 end까지 연속된 정수(int) 범위를 스트림으로 만들고, boxed()로 int → Integer로 변환한 뒤,리스트로 수집해서 pageNumList에 저장함.
    if(prev) 
        this.prevPage=start-1;
    if(next) 
        this.nextPage=end+1;

    this.totalPage=this.pageNumList.size();
    this.current=pageRequestDTO.getPage();
   }

}
