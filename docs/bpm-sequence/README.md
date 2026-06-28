# BPM 模块核心流程时序图

以下为 bpm 模块中的核心复杂逻辑时序图与代码位置参考：

- 审批通过流程：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:551`
- 审批拒绝流程：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:797`
- 退回流程：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:915`
- 撤回流程：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:1253`
- 加签/减签：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:1098`、`1215`
- 自动审批与分配策略：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:1334`、`1438`
- 边界事件超时处理：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/framework/flowable/core/listener/BpmTaskEventListener.java:96`
- 跳转到结束：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/service/task/BpmTaskServiceImpl.java:1059`
- 流程完成与事件：`yudao-module-bpm/src/main/java/cn/iocoder/yudao/module/bpm/framework/flowable/core/listener/BpmProcessInstanceEventListener.java:47`

文档列表：

- approve-task.md
- reject-task.md
- return-task.md
- withdraw-task.md
- sign-tasks.md
- auto-approval.md
- timeout-boundary.md
- move-to-end.md
- process-complete.md
- gateway-compare.md
