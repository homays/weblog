package com.arrebol.web.controller;

import com.arrebol.common.aspect.ApiOperationLog;
import com.arrebol.common.util.Response;
import com.arrebol.web.service.WikiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wiki")
@Api(tags = "知识库")
public class WikiController {

    @Autowired
    private WikiService wikiService;

    @PostMapping("/list")
    @ApiOperation(value = "获取知识库数据")
    @ApiOperationLog(description = "获取知识库数据")
    public Response findWikiList() {
        return wikiService.findWikiList();
    }

}