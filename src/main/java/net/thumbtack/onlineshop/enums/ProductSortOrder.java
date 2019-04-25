package net.thumbtack.onlineshop.enums;

import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;

public enum ProductSortOrder {
    PRODUCT, CATEGORY;

    public static  ProductSortOrder fromString(String order) throws ServerException {
        switch (order) {
            case "product":
                return ProductSortOrder.PRODUCT;
            case "category":
                return ProductSortOrder.CATEGORY;
            default:
                throw new ServerException(ErrorCode.BAD_ORDER_FOR_GET_PROGUCT_LIST);
        }
    }
}
