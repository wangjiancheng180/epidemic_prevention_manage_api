package com.wjc.service.iot.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjc.enetity.iot.Task;
import com.wjc.mapper.iot.TaskMapper;
import com.wjc.service.iot.TaskService;
import org.springframework.stereotype.Service;


/**
 * @author 王建成
 * @date 2022/12/21--13:19
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
}
