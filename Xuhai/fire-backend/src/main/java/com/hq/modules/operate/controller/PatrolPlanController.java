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
import com.hq.modules.operate.entity.PatrolPlan;
import com.hq.modules.operate.service.IInspectionTaskService;
import com.hq.modules.operate.service.IPatrolPlanService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


 /**
 * @Description: 巡查计划
 * @Author: jeecg-boot
 * @Date:   2025-02-16
 * @Version: V1.0
 */
@Api(tags="巡查计划")
@RestController
@RequestMapping("/inspection/patrolPlan")
@Slf4j
public class PatrolPlanController {


	 @Autowired
	 private IPatrolPlanService iPatrolPlanService;
	/**
	 * 分页列表查询
	 *
	 * @param patrolPlan
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "巡查计划-分页列表查询")
	@ApiOperation(value="巡查计划-分页列表查询", notes="巡查计划-分页列表查询")
	@GetMapping(value = "/list")
	public R queryPageList(PatrolPlan patrolPlan,
						   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
						   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
						   HttpServletRequest req) {
		try {
			// 创建分页对象
			Page<PatrolPlan> page = new Page<>(pageNo, pageSize);
			// 调用服务层的分页查询方法
			Page<PatrolPlan> pageList = iPatrolPlanService.queryPageList(page, patrolPlan);
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
	 * @param patrolPlan
	 * @return
	 */
	@ApiOperation(value="巡查计划-添加", notes="巡查计划-添加")
	//@RequiresPermissions("org.jeecg.modules.demo:patrol_plan:add")
	@PostMapping(value = "/add")
	public R add(@RequestBody PatrolPlan patrolPlan) {
		try {
			// 调用服务层的添加方法
			boolean result = iPatrolPlanService.insert(patrolPlan);
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
	 * @param patrolPlan
	 * @return
	 */

	@ApiOperation(value="巡查计划-编辑", notes="巡查计划-编辑")
	//@RequiresPermissions("org.jeecg.modules.demo:patrol_plan:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public R edit(@RequestBody PatrolPlan patrolPlan) {
		try {
			// 调用服务层的更新方法
			boolean result = iPatrolPlanService.updateById(patrolPlan);
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
	@ApiOperation(value = "巡查计划-通过id删除", notes = "巡查计划-通过id删除")
	//@RequiresPermissions("org.jeecg.modules.demo:patrol_plan:delete")
	@DeleteMapping(value = "/delete")
	public R delete(@RequestParam(name = "id", required = true) Long id) {
		try {
			// 调用服务层的删除方法
			boolean result = iPatrolPlanService.deleteById(id);
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

	@ApiOperation(value="巡查计划-批量删除", notes="巡查计划-批量删除")
	//@RequiresPermissions("org.jeecg.modules.demo:patrol_plan:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public R deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		try {
			// 将逗号分隔的 id 字符串转换为字符串数组
			String[] idArray = ids.split(",");
			// 调用服务层的批量删除方法
			boolean result = iPatrolPlanService.deleteBatchIds(Arrays.asList(idArray));
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

	@ApiOperation(value="巡查计划-通过id查询", notes="巡查计划-通过id查询")
	@GetMapping(value = "/queryById")
	public R queryById(@RequestParam(name="id",required=true) String id) {
		try {
			// 调用服务层的查询方法
			PatrolPlan inspectionTask = iPatrolPlanService.selectById(id);
			Map<String, Object> map = new HashMap<>();
			map.put("inspectionTask", inspectionTask);
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("查询失败");
		}
	}



}
