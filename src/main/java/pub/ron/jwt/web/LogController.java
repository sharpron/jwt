package pub.ron.jwt.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.ron.jwt.annotation.LogDesc;
import pub.ron.jwt.annotation.PermissionDesc;
import pub.ron.jwt.domain.Log;
import pub.ron.jwt.service.LogService;

/**
 * 日志接口
 * @author ron
 * 2019.05.09
 */
@RestController
@RequestMapping("/logs")
@Api("日志")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @ApiOperation("查询日志列表")
    @LogDesc("查询日志列表")
    @PermissionDesc("查询日志列表")
    @RequiresPermissions("logs:list")
    @GetMapping
    public ResponseEntity<Page<Log>> getLogs(Pageable pageable) {
        return ResponseEntity.ok(logService.findByPage(pageable));
    }
}
