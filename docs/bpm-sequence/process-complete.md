# 流程完成与事件时序图

代码位置：监听入口 `yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/framework/flowable/core/listener/BpmProcessInstanceEventListener.java:47`；完成处理 `BpmProcessInstanceServiceImpl.java:966`

```mermaid
sequenceDiagram
  participant Listener as BpmProcessInstanceEventListener
  participant ProcSvc as BpmProcessInstanceService
  participant Runtime as Flowable RuntimeService
  participant Msg as BpmMessageService
  participant EventPub as BpmProcessInstanceEventPublisher

  Listener->>ProcSvc: processProcessInstanceCompleted(instance)
  ProcSvc->>Runtime: 读取 variables(status, reason)
  alt status=RUNNING
    ProcSvc->>Runtime: setVariable(status=APPROVE)
  end
  alt 子流程拒绝需上推
    ProcSvc->>Runtime: 获取父流程实例
    ProcSvc->>ProcSvc: updateProcessInstanceReject(parent, REJECT_CHILD_PROCESS)
    ProcSvc->>TaskSvc: moveTaskToEnd(parentProcessId)
  end
  alt 审批通过
    ProcSvc->>Msg: sendMessageWhenProcessInstanceApprove(...)
  else 审批拒绝
    ProcSvc->>Msg: sendMessageWhenProcessInstanceReject(...)
  end
  ProcSvc->>EventPub: sendProcessInstanceResultEvent(event)
  opt 流程后置通知
    ProcSvc->>Http: 执行配置的 HTTP 回调
  end
```

