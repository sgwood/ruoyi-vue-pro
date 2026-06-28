# 撤回流程时序图

代码位置：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:1253`

```mermaid
sequenceDiagram
  participant UI as Admin UI
  participant TaskCtl as BpmTaskController
  participant TaskSvc as BpmTaskServiceImpl
  participant ProcSvc as BpmProcessInstanceService
  participant History as Flowable HistoryService
  participant Runtime as Flowable RuntimeService
  participant Task as Flowable TaskService

  UI->>TaskCtl: PUT /bpm/task/withdraw?taskId=...
  TaskCtl->>TaskSvc: withdrawTask(userId, taskId)
  TaskSvc->>History: 查询本人已办且已完成任务
  TaskSvc->>ProcSvc: getProcessInstance(processInstanceId)
  TaskSvc->>Model: getBpmnModelByDefinitionId(processDefinitionId)
  TaskSvc->>History: 查询下一节点是否已被审批
  alt 下游已审批
    TaskSvc-->>TaskCtl: 409 NEXT_TASK_NOT_ALLOW
  else 可以撤回
    TaskSvc->>Task: 查询下游运行任务列表
    loop 标记取消
      TaskSvc->>Task: addComment(CANCEL, "前一节点撤回")
      TaskSvc->>Task: setVariableLocal(status=CANCEL, reason=CANCEL_BY_WITHDRAW)
    end
    TaskSvc->>Runtime: moveExecutionsToSingleActivityId(executionIds, currentTaskKey)
    TaskSvc-->>TaskCtl: 200 OK
  end
```

