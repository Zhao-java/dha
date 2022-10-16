package org.feidian.dha.console.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.feidian.dha.console.domain.DhaLogicGroup;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-17 14:56
 **/

@Mapper
public interface DhaLogicGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DhaLogicGroup record);

    int insertSelective(DhaLogicGroup record);

    DhaLogicGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DhaLogicGroup record);

    int updateByPrimaryKey(DhaLogicGroup record);
}