package com.chaosopen.ddd.infrastructure.inventory.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_inventory")
public class InventoryDO {

    @TableId(value = "inventory_id", type = IdType.AUTO)
    private Long inventoryId;
    private Long storeId;
    private Long skuId;
    private Integer totalStock;
    private Integer lockedStock;
    private Integer availableStock;
}
