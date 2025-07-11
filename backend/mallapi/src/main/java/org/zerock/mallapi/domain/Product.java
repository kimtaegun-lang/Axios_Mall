package org.zerock.mallapi.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="tbl_product")
@Getter
@ToString(exclude="imageList")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private boolean delFlag;

    @ElementCollection // 엔티티가 List<String>, Set<Integer> 같은 기본 값 타입 컬렉션이나 @Embeddable로 만든 내장 타입(값 객체) 컬렉션을 가질 때, 별도의 테이블에 그 컬렉션을 저장하고 관리하도록 지정합니다.
    @Builder.Default
    private List<ProductImage> imageList=new ArrayList<>();

    public void changePrice(int price) { 
        this.price = price;
    }
    public void changeDesc(String desc){ 
        this.pdesc = desc;
    }
    public void changeName(String name) {   
    this.pname = name;
    }
    public void addImage(ProductImage image) { 
        image.setOrd(this.imageList.size()); 
        imageList.add(image);
    }
    public void addImageString(String fileName){
    ProductImage productImage = ProductImage.builder()
    .fileName(fileName)
    .build(); addImage(productImage);
    }
public void clearList() {
    this.imageList.clear(); // 자바 컬렉션(List, Set 등)의 모든 요소를 삭제함
    }
    public void changeDel(boolean delFlag) {
        this.delFlag=delFlag;
    }

}
