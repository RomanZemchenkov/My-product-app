package com.roman.dao.repository;

import com.roman.service.dto.SortProductDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ProductSort {

    public PageRequest createSortParameters(SortProductDto sort, int page, int size){
        Sort.Direction direction;
        if(sort.getOrderBy().equals("ASC")){
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        if(sort.getSortBy() == null){
            return PageRequest.of(page,size, direction, "id");
        }
        return PageRequest.of(page, size, Sort.by(direction, sort.getSortBy()));
    }
}
