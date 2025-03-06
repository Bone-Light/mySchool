package com.example.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.StoreImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper extends BaseMapper<StoreImage> {

}
