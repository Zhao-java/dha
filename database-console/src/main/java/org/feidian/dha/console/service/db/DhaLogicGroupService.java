package org.feidian.dha.console.service.db;

import org.feidian.dha.console.domain.DhaLogicGroup;
import org.feidian.dha.console.mapper.DhaLogicGroupMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-20 17:08
 **/

@Service
public class DhaLogicGroupService {

    @Resource
    private DhaLogicGroupMapper dhaLogicGroupMapper;

    public int deleteByPrimaryKey(Long id) {
        return dhaLogicGroupMapper.deleteByPrimaryKey(id);
    }

    public int insert(DhaLogicGroup record) {
        return dhaLogicGroupMapper.insert(record);
    }

    public int insertSelective(DhaLogicGroup record) {
        return dhaLogicGroupMapper.insertSelective(record);
    }

    public DhaLogicGroup selectByPrimaryKey(Long id) {
        return dhaLogicGroupMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(DhaLogicGroup record) {
        return dhaLogicGroupMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(DhaLogicGroup record) {
        return dhaLogicGroupMapper.updateByPrimaryKey(record);
    }

}

