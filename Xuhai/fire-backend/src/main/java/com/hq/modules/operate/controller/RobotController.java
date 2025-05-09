package com.hq.modules.operate.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hq.modules.operate.entity.Robot;
import com.hq.modules.operate.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/robot")
public class RobotController {

    @Autowired
    private RobotService robotService;


    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Robot robot) {
        Map<String, Object> result = new HashMap<>();
        boolean success = robotService.insert(robot);
        result.put("success", success);
        if (success) {
            result.put("robotId", robot.getRobotId());
        }
        return result;
    }

    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = robotService.deleteById(id);
        result.put("success", success);
        return result;
    }


    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Robot robot) {
        Map<String, Object> result = new HashMap<>();
        boolean success = robotService.updateById(robot);
        result.put("success", success);
        return result;
    }


    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam Integer id) {
        Map<String, Object> result = new HashMap<>();
        Robot robot = robotService.selectById(id);
        result.put("success", robot != null);
        result.put("data", robot);
        return result;
    }


    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String robotCode,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) String status) {

        EntityWrapper<Robot> wrapper = new EntityWrapper<>();

        if (robotCode != null && !robotCode.isEmpty()) {
            wrapper.like("robot_code", robotCode);
        }
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        if (typeId != null) {
            wrapper.eq("type_id", typeId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }

        Page<Robot> page = new Page<>(pageNum, pageSize);
        Page<Robot> resultPage = robotService.selectPage(page, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", resultPage.getRecords());
        result.put("total", resultPage.getTotal());
        result.put("current", resultPage.getCurrent());
        result.put("size", resultPage.getSize());

        return result;
    }


    @GetMapping("/statusCount")
    public Map<String, Object> statusCount(@RequestParam String status) {
        EntityWrapper<Robot> wrapper = new EntityWrapper<>();
        wrapper.eq("status", status);

        int count = robotService.selectCount(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("count", count);
        result.put("status", status);

        return result;
    }

//
//            * 获取所有处于活动状态的机器人
//     * @return 机器人列表
//     */
    @GetMapping("/activeRobots")
    public Map<String, Object> getActiveRobots() {
        EntityWrapper<Robot> wrapper = new EntityWrapper<>();
        wrapper.eq("status", "active");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", robotService.selectList(wrapper));

        return result;
    }


//     * @return 机器人列表
//     */
    @GetMapping("/byLocation")
    public Map<String, Object> getRobotsByLocation(@RequestParam String location) {
        EntityWrapper<Robot> wrapper = new EntityWrapper<>();
        wrapper.like("current_location", location);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", robotService.selectList(wrapper));

        return result;
    }
}