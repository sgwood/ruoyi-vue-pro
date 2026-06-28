# 审批通过流程时序图

代码位置：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:551`

```mermaid
sequenceDiagram
  participant UI as Admin UI
  participant TaskCtl as BpmTaskController
  participant TaskSvc as BpmTaskServiceImpl
  participant ProcSvc as BpmProcessInstanceService
  participant Model as BpmModelService
  participant DefSvc as BpmProcessDefinitionService
  participant Runtime as Flowable RuntimeService
  participant Task as Flowable TaskService

  UI->>TaskCtl: PUT /bpm/task/approve
  TaskCtl->>TaskSvc: approveTask(userId, reqVO)
  TaskSvc->>TaskSvc: validateTask(userId, reqVO.id)
  TaskSvc->>ProcSvc: getProcessInstance(task.processInstanceId)
  TaskSvc->>Model: getBpmnModelByDefinitionId(task.processDefinitionId)
  TaskSvc->>TaskSvc: parseSignEnable/parseReasonRequire

  alt 委派任务
    TaskSvc->>TaskSvc: approveDelegateTask(reqVO, task)
    TaskSvc-->>TaskCtl: 200 OK
  else 后加签任务
    TaskSvc->>TaskSvc: approveAfterSignTask(task, reqVO)
    TaskSvc-->>TaskCtl: 200 OK
  else 普通任务
    TaskSvc->>Task: setVariableLocal(status=APPROVE, reason)
    opt 需签名
      TaskSvc->>Task: setVariableLocal(signPicUrl)
    end
    TaskSvc->>Task: addComment(APPROVE, reason)
    TaskSvc->>TaskSvc: 合并历史流程变量与前端变量
    TaskSvc->>TaskSvc: validateAndSetNextAssignees(...)
    TaskSvc->>Runtime: setVariables(processInstanceId, variables)
    TaskSvc->>Runtime: 更新 NEED_SIMULATE_TASK_IDS 移除当前节点
    TaskSvc->>Task: complete(taskId, variables, true)
    TaskSvc->>TaskSvc: handleParentTaskIfSign(parentTaskId)
    TaskSvc-->>TaskCtl: 200 OK
  end
```

