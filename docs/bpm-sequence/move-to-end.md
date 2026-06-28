# 跳转到结束节点时序图

代码位置：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:1059`

```mermaid
sequenceDiagram
  participant TaskSvc as BpmTaskServiceImpl
  participant Model as BpmModelService
  participant Runtime as Flowable RuntimeService
  participant Task as Flowable TaskService

  TaskSvc->>TaskSvc: getRunningTaskListByProcessInstanceId(processInstanceId)
  loop 取消其他运行任务
    TaskSvc->>TaskSvc: processTaskCanceled(taskId)
  end
  TaskSvc->>Model: getBpmnModelByDefinitionId(defId)
  TaskSvc->>Runtime: createChangeActivityStateBuilder().moveActivityIdsToSingleActivityId(activityIds, endEventId)
  Runtime-->>TaskSvc: changeState()
  alt 流程仍未结束
    TaskSvc->>Runtime: deleteProcessInstance(processInstanceId, reason)
  end
```

