package org.feidian.dha.console.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.feidian.dha.console.domain.DhaDatasource;

import java.util.List;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-17 15:29
 **/

@Mapper
public interface DhaDatasourceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DhaDatasource record);

    int insertSelective(DhaDatasource record);

    DhaDatasource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DhaDatasource record);

    int updateByPrimaryKey(DhaDatasource record);

    List<DhaDatasource> selectAll();

    List<DhaDatasource> selectByDataSourceName(DhaDatasource record);
}