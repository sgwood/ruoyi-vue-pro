# 退回流程时序图

代码位置：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:864`、`915`

```mermaid
sequenceDiagram
  participant UI as Admin UI
  participant TaskCtl as BpmTaskController
  participant TaskSvc as BpmTaskServiceImpl
  participant Model as BpmModelService
  participant Runtime as Flowable RuntimeService
  participant Task as Flowable TaskService

  UI->>TaskCtl: PUT /bpm/task/return
  TaskCtl->>TaskSvc: returnTask(userId, reqVO)
  TaskSvc->>TaskSvc: validateTask(userId, reqVO.id)
  TaskSvc->>Model: getBpmnModelByDefinitionId(task.processDefinitionId)
  TaskSvc->>TaskSvc: validateTargetTaskCanReturn(bpmnModel, sourceKey, targetKey)

  TaskSvc->>Task: createTaskQuery().processInstanceId(...).list()
  TaskSvc->>TaskSvc: 计算需撤回的运行任务Key集合
  loop 标记状态
    TaskSvc->>Task: addComment(RETURN/CANCEL)
    TaskSvc->>Task: setVariableLocal(status=RETURN 或 CANCEL, reason)
  end

  TaskSvc->>TaskSvc: getNeedSimulateTaskDefinitionKeys(bpmnModel, currentTask, targetElement)
  TaskSvc->>Runtime: createChangeActivityStateBuilder()
  TaskSvc->>Runtime: moveActivityIdsToSingleActivityId(runExecutionIds, targetKey)
  TaskSvc->>Runtime: processVariable(NEED_SIMULATE_TASK_IDS, set)
  TaskSvc->>Runtime: localVariable(targetKey, RETURN_FLAG=true)
  Runtime-->>TaskSvc: changeState()
  TaskSvc-->>TaskCtl: 200 OK
```

