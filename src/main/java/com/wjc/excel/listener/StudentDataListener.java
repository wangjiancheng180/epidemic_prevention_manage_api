package com.wjc.excel.listener;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.wjc.excel.data.university.StudentData;
import com.wjc.service.university.StudentService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/5/19--18:18
 */
@Slf4j
public class StudentDataListener implements ReadListener<StudentData> {


    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List<StudentData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private StudentService studentService;

    public StudentDataListener() {
    }

    public StudentDataListener(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(StudentData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        studentService.saveDataBatch(cachedDataList);
        log.info("存储数据库成功！");
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }
}
