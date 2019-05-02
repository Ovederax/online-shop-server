package net.thumbtack.onlineshop.enums;

import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;

public enum ProductSortOrder {
    PRODUCT, CATEGORY;

    public static  ProductSortOrder fromString(String order) throws ServerException {
        try {
            return valueOf(order.toUpperCase());
        } catch(IllegalArgumentException ex) {
            throw new ServerException(ErrorCode.BAD_ORDER_FOR_GET_PROGUCT_LIST);
        }
    }
}
