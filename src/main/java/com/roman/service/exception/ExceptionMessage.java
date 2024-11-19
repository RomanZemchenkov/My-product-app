package com.roman.service.exception;

public class ExceptionMessage {

    //product
    public static final String PRODUCT_WITH_ID_EXIST_EXCEPTION_MESSAGE = "Продукт с id '%d' не существует.";
    public static final String PRODUCT_WITH_TITLE_EXIST_EXCEPTION_MESSAGE = "Продукт с названием '%s' не существует.";
    public static final String PRODUCT_COST_EXCEPTION_MESSAGE = "Стоимость продукта не может быть меньше 0.";
    public static final String PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE = "Описание товара не должно превышать 4096 символов.";
    public static final String PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE = "У товара должно быть описание.";
    public static final String PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE = "У товара должно быть название.";
    public static final String PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE = "Названиет товара не должно превышать 255 символов.";
    public static final String PRODUCT_COUNT_IN_STOCK_EXCEPTION_MESSAGE = "Количество продуктов не должно быть меньше 0.";
    public static final String PAGE_NUMBER_EXCEPTION_MESSAGE = "Страницы не должны быть меньше 0.";
    public static final String SIZE_PAGE_EXCEPTION_MESSAGE = "Количество товаров на странице не может быть меньше 1.";

    //delivery
    public static final String DELIVERY_TITLE_LENGTH_MESSAGE_EXCEPTION = "Название доставки должно быть не более 255 символов.";
    public static final String DELIVERY_TITLE_EMPTY_MESSAGE_EXCEPTION = "Название доставки должно быть не более 255 символов.";
    public static final String DELIVERY_COUNT_EMPTY_MESSAGE_EXCEPTION = "Количество товаров для поставки не должно быть пустым.";
    public static final String DELIVERY_COUNT_MIN_MESSAGE_EXCEPTION = "Количество товаров для доставки не может быть меньше 1.";
    public static final String DELIVERY_WITH_ID_EXIST_EXCEPTION_MESSAGE = "Поставка с id '%d' названием не найдена.";

}
