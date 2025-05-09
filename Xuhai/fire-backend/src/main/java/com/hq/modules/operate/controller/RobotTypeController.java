package com.hq.modules.operate.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.RobotType;
import com.hq.modules.operate.service.RobotTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/robotType")
public class RobotTypeController {

    @Autowired
    private RobotTypeService robotTypeService;


    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody RobotType robotType) {
        Map<String, Object> result = new HashMap<>();
        boolean success = robotTypeService.insert(robotType);
        result.put("success", success);
        if (success) {
            result.put("typeId", robotType.getTypeId());
        }
        return result;
    }


    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = robotTypeService.deleteById(id);
        result.put("success", success);
        return result;
    }


    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody RobotType robotType) {
        Map<String, Object> result = new HashMap<>();
        boolean success = robotTypeService.updateById(robotType);
        result.put("success", success);
        return result;
    }


    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        RobotType robotType = robotTypeService.selectById(id);
        result.put("success", robotType != null);
        result.put("data", robotType);
        return result;
    }


    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String typeName) {

        EntityWrapper<RobotType> wrapper = new EntityWrapper<>();
        if (typeName != null && !typeName.isEmpty()) {
            wrapper.like("type_name", typeName);
        }

        Page<RobotType> page = new Page<>(pageNum, pageSize);
        Page<RobotType> resultPage = robotTypeService.selectPage(page, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("current", resultPage.getCurrent());
        result.put("size", resultPage.getSize());

        return result;
    }


    @GetMapping("/checkTypeName")
    public Map<String, Object> checkTypeName(@RequestParam String typeName) {
        EntityWrapper<RobotType> wrapper = new EntityWrapper<>();
        wrapper.eq("type_name", typeName);

        int count = robotTypeService.selectCount(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("exists", count > 0);

        return result;
    }
}