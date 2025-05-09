package com.hq.modules.operate.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hq.common.utils.R;
import com.hq.modules.operate.entity.InspectionTask;
import com.hq.modules.operate.service.IInspectionTaskService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 巡查任务控制器
 */
@Api(tags = "巡查任务")
@RestController
@RequestMapping("/inspection/inspectionTask")
public class InspectionTaskController {

	@Autowired
	private IInspectionTaskService inspectionTaskService;

	/**
	 * 分页列表查询
	 *
	 * @param inspectionTask 查询条件
	 * @param pageNo         页码，默认值为 1
	 * @param pageSize       每页记录数，默认值为 10
	 * @param req            HttpServletRequest 对象
	 * @return 包含分页查询结果的响应对象
	 */
	@GetMapping(value = "/list")
	public R queryPageList(InspectionTask inspectionTask,
						   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
						   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
						   HttpServletRequest req) {
		try {
			// 创建分页对象
			Page<InspectionTask> page = new Page<>(pageNo, pageSize);
			// 调用服务层的分页查询方法
			Page<InspectionTask> pageList = inspectionTaskService.queryPageList(page, inspectionTask);
			Map<String, Object> map = new HashMap<>();
			map.put("page", pageList);
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("分页查询失败");
		}
	}

	/**
	 * 添加巡查任务
	 *
	 * @param inspectionTask 要添加的巡查任务对象
	 * @return 包含添加结果的响应对象
	 */
	@ApiOperation(value = "巡查任务-添加", notes = "巡查任务-添加")
	@PostMapping(value = "/add")
	public R add(@RequestBody InspectionTask inspectionTask) {
		try {
			// 调用服务层的添加方法
			boolean result = inspectionTaskService.insert(inspectionTask);
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
	 * 编辑巡查任务
	 *
	 * @param inspectionTask 要编辑的巡查任务对象
	 * @return 包含编辑结果的响应对象
	 */
	@ApiOperation(value = "巡查任务-编辑", notes = "巡查任务-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
	public R edit(@RequestBody InspectionTask inspectionTask) {
		try {
			// 调用服务层的更新方法
			boolean result = inspectionTaskService.updateById(inspectionTask);
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
	 * 通过 id 删除巡查任务
	 *
	 * @param id 要删除的巡查任务的 id
	 * @return 包含删除结果的响应对象
	 */
	@ApiOperation(value = "巡查任务-通过 id 删除", notes = "巡查任务-通过 id 删除")
	@DeleteMapping(value = "/delete")

	public R delete(@RequestParam(name = "id", required = true) Long id) {
		try {
			// 调用服务层的删除方法
			boolean result = inspectionTaskService.deleteById(id);
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
	 * 批量删除巡查任务
	 *
	 * @param ids 要删除的巡查任务的 id 数组，多个 id 用逗号分隔
	 * @return 包含批量删除结果的响应对象
	 */
	@ApiOperation(value = "巡查任务-批量删除", notes = "巡查任务-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public R deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		try {
			// 将逗号分隔的 id 字符串转换为字符串数组
			String[] idArray = ids.split(",");
			// 调用服务层的批量删除方法
			boolean result = inspectionTaskService.deleteBatchIds(Arrays.asList(idArray));
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
	 * 通过 id 查询巡查任务
	 *
	 * @param id 要查询的巡查任务的 id
	 * @return 包含查询结果的响应对象
	 */
	@ApiOperation(value = "巡查任务-通过 id 查询", notes = "巡查任务-通过 id 查询")
	@GetMapping(value = "/queryById")
	public R queryById(@RequestParam(name = "id", required = true) String id) {
		try {
			// 调用服务层的查询方法
			InspectionTask inspectionTask = inspectionTaskService.selectById(id);
			Map<String, Object> map = new HashMap<>();
			map.put("inspectionTask", inspectionTask);
			return R.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			return R.error("查询失败");
		}
	}
}