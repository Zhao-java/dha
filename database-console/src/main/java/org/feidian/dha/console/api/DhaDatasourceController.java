package org.feidian.dha.console.api;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.feidian.dha.console.domain.DhaDatasource;
import org.feidian.dha.console.response.Response;
import org.feidian.dha.console.response.Responses;
import org.feidian.dha.console.service.db.DhaDatasourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @program: dha-console
 * @description:
 * @author: zys
 * @create: 2022-08-20 20:58
 **/

@RestController
@RequestMapping("/datasource")
@Slf4j(topic = "DhaDatasource")
public class DhaDatasourceController {
    /**
     * 服务对象
     */
    @Resource
    private DhaDatasourceService dhaDatasourceService;

    /**
     * 分页查询数据
     *
     * @return 所有数据
     */
    @GetMapping("page")
    public Response selectOne(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNum") Integer pageNum) {
        PageInfo<DhaDatasource> datasourceList = dhaDatasourceService.selectListByPageQuery(pageSize, pageNum);
        return Responses.successResponse(Collections.singletonMap("data", datasourceList));
    }

    @PostMapping()
    public Response add(@RequestBody DhaDatasource dhaDatasource) {
        log.info("add:{}",dhaDatasource);
        int count = dhaDatasourceService.insert(dhaDatasource);
        if (count > 0) {
            return Responses.successResponse();
        }
        return Responses.errorResponse("add error");
    }

    @PutMapping()
    public Response update(@RequestBody DhaDatasource datasource) {
        int count = dhaDatasourceService.updateByPrimaryKey(datasource);
        if (count > 0) {
            return Responses.successResponse();
        }
        return Responses.errorResponse("update error");
    }

    @DeleteMapping()
    public Response delete(@RequestParam("id") Long id) {
        int count = dhaDatasourceService.deleteByPrimaryKey(id);
        if (count > 0) {
            return Responses.successResponse();
        }
        return Responses.errorResponse("delete error");
    }

}
