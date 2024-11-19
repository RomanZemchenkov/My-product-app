package com.roman.web.controller;

import com.roman.service.dto.product.CreateProductDto;
import com.roman.service.dto.product.UpdateProductDto;
import com.roman.service.exception.ExceptionMessage;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

public class ProductArgumentProvider {

    static Stream<Arguments> argumentsForCreateProductTest(){
        return Stream.of(
                Arguments.of(new CreateProductDto("Mobile","desc",100,1),"EXIST",1L),
                Arguments.of(new CreateProductDto("Mobile1","desc",100,0),"NOT_EXIST",2L)
        );
    }


    static Stream<Arguments> argumentForCreateProductWithWrongParameters(){
        return Stream.of(
                Arguments.of(new CreateProductDto("","desc",100,1), ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto(range(0, 256).mapToObj(i -> "a").collect(joining()), "desc",100,1), ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", "",100,1), ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", range(0, 4097).parallel().mapToObj(i -> "a").collect(joining()),100,1), ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", "desc",-1,1), ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE)
        );
    }

    static Stream<Arguments> argumentsForFindProductByFilterWithWrongParameters(){
        return Stream.of(
                Arguments.of("title", IntStream.range(0,256).mapToObj(i -> "a").collect(joining()),ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of("cost", "-1",ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of("costMin", "-1",ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of("costMax", "-1",ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of("page", "-1",ExceptionMessage.PAGE_NUMBER_EXCEPTION_MESSAGE),
                Arguments.of("size", "0",ExceptionMessage.SIZE_PAGE_EXCEPTION_MESSAGE)
        );
    }
    static Stream<Arguments> argumentForUpdateProductWithWrongParameters(){
        return Stream.of(
                Arguments.of(1L,400,new UpdateProductDto("","desc",100,1), ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto(range(0, 256).mapToObj(i -> "a").collect(joining()), "desc",100,1), ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", "",100,1), ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", range(0, 4097).parallel().mapToObj(i -> "a").collect(joining()),100,1), ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", "desc",-1,1), ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of(100L,404,new UpdateProductDto("Mobile phone", "desc",100,1), ExceptionMessage.PRODUCT_WITH_ID_EXIST_EXCEPTION_MESSAGE.formatted(100))
        );
    }

    public static Stream<Arguments> argumentForFindWithFilter(){
        return Stream.of(
                Arguments.of(Map.of("title","Mobile"),10),
                Arguments.of(Map.of("cost","2000"),3),
                Arguments.of(Map.of("costMin","5000"),7),
                Arguments.of(Map.of("costMax","3000","size","30"),29),
                Arguments.of(Map.of("costMax","7000","costMin","3000"),10),
                Arguments.of(Map.of("title","Table","costMin","5500"),0),
                Arguments.of(Map.of("inStock","EXIST","size","30"),28)
        );
    }

    public static Stream<Arguments> argumentForFindWithFilterAndSort(){
        return Stream.of(
                Arguments.of(Map.of("title","Mobile","sortBy","cost","orderBy","DESC"),"Mobile 10"),
                Arguments.of(Map.of("title","Spoon","sortBy","cost","orderBy","ASC"),"Spoon 1"),
                Arguments.of(Map.of("cost","2000", "sortBy","title","orderBy","ASC"), "Mobile 2")
        );
    }

    public static Stream<Arguments> argumentForFindWithFilterSortAndPageable(){
        return Stream.of(
                Arguments.of(Map.of("title","Mobile","sortBy","cost","orderBy","DESC","size","4","page","1"),4,"Mobile 6"),
                Arguments.of(Map.of("title","Spoon","sortBy","cost","orderBy","ASC","size","2","page","3"),2,"Spoon 7"),
                Arguments.of(Map.of("cost","2000","sortBy","title","orderBy","ASC","size","1","page","1"),1,"Mouse 10")
        );
    }
}
