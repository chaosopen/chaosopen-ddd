package com.chaosopen.ddd.domain.store.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门店实体。
 */
@Data
public class Store {

    /**
     * 门店ID。
     */
    private Long storeId;
    /**
     * 门店名称。
     */
    private String storeName;
    /**
     * 门店状态。
     */
    private Integer status;
    /**
     * 门店地址。
     */
    private String address;
    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;
}
