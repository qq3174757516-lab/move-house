package com.movehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class Publish implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private String cover;
    private String imgs;
    private String address;
    private Long driverId;
    private BigDecimal valuation;

    // 0已发布, 1已接单并估计价格, 2已同意
    private Integer status;
    private Date createTime;

    // 以下是必须补全的数据库已有字段
    private String originAddress;      // 搬出地址(起点)
    private String originDetail;       // 搬出地门牌号
    private String destinationAddress; // 搬入地址(终点)
    private String destinationDetail;  // 搬入地门牌号
    private BigDecimal distance;       // 预估距离(公里)
    private String cargoOptions;       // 货物选项(JSON格式) - 对应数据库 json 类型可以使用 String 接收
    private Integer floor;             // 楼层
    private Integer hasElevator;       // 是否有电梯(0无 1有)
    private BigDecimal estimatedPrice; // 平台预估价格
    private String startAddr;          // 搬出地址快照
    private String endAddr;            // 搬入地址快照
    private BigDecimal price;          // 系统预估价格
    private String goodsDesc;          // 货物详细描述
}