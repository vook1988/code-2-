package com.hq.modules.operate.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.baomidou.mybatisplus.plugins.Page;
import com.hq.common.utils.R;
import com.hq.modules.operate.entity.InspectionTask;
import com.hq.modules.operate.entity.ProblemLogging;
import com.hq.modules.operate.entity.Route;
import com.hq.modules.operate.service.IProblemLoggingService;
import com.hq.modules.operate.service.IRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


 /**
 * @Description: 路线管理
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
@Api(tags="路线管理")
@RestController
@RequestMapping("/inspection/route")
public class RouteController {

	 @Autowired
	 private IRouteService routeService;
	/**
	 * 分页列表查询
	 *
	 * @param route
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "路线管理-分页列表查询")
	@ApiOperation(value="路线管理-分页列表查询", notes="路线管理-分页列表查询")
	@GetMapping(value = "/list")
	public R queryPageList(Route route,
						   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
						   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
						   HttpServletRequest req) {
		try {
			// 创建分页对象
			Page<Route> page = new Page<>(pageNo, pageSize);
			// 调用服务层的分页查询方法
			Page<Route> pageList = routeService.queryPageList(page, route);
			Map<String, Object> map = new HashMap<>();
			map.put("page", pageList);
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("分页查询失败");
		}
	}
	
	/**
	 *   添加
	 *
	 * @param route
	 * @return
	 */
	@ApiOperation(value="路线管理-添加", notes="路线管理-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:route:add")
	@PostMapping(value = "/add")
	public R add(@RequestBody Route route) {
		try {
			// 调用服务层的添加方法
			boolean result = routeService.insert(route);
			if (result) {
				return R.ok("添加成功");
			} else {
				return R.error("添加失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("添加失败");
		}
	}
	
	/**
	 *  编辑
	 *
	 * @param route
	 * @return
	 */
	@ApiOperation(value="路线管理-编辑", notes="路线管理-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:route:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public R edit(@RequestBody Route route) {
		try {
			// 调用服务层的更新方法
			boolean result = routeService.updateById(route);
			if (result) {
				return R.ok("编辑成功");
			} else {
				return R.error("编辑失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("编辑失败");
		}
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="路线管理-通过id删除", notes="路线管理-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:route:delete")
	@DeleteMapping(value = "/delete")
	public R delete(@RequestParam(name = "id", required = true) Long id) {
		try {
			// 调用服务层的删除方法
			boolean result = routeService.deleteById(id);
			if (result) {
				return R.ok("删除成功");
			} else {
				return R.error("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("删除失败");
		}
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value="路线管理-批量删除", notes="路线管理-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:route:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public R deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		try {
			// 将逗号分隔的 id 字符串转换为字符串数组
			String[] idArray = ids.split(",");
			// 调用服务层的批量删除方法
			boolean result = routeService.deleteBatchIds(Arrays.asList(idArray));
			if (result) {
				return R.ok("批量删除成功");
			} else {
				return R.error("批量删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("批量删除失败");
		}
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "路线管理-通过id查询")
	@ApiOperation(value="路线管理-通过id查询", notes="路线管理-通过id查询")
	@GetMapping(value = "/queryById")
	public R queryById(@RequestParam(name="id",required=true) String id) {
		try {
			// 调用服务层的查询方法
			Route inspectionTask = routeService.selectById(id);
			Map<String, Object> map = new HashMap<>();
			map.put("inspectionTask", inspectionTask);
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("查询失败");
		}
	}


}
