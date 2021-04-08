#### 问题
1. 在使用 profile 'sharding-databases' 的时候启动报错：Failed to determine a suitable driver class

解决方式：
在 pom 里排除 druid-spring-boot-starter，并手动引入 druid

### 测试结果
#### sharding-databases
##### 指定默认的分片策略
``
spring.shardingsphere.sharding.default-database-strategy.inline.sharding-column=user_id
spring.shardingsphere.sharding.default-database-strategy.inline.algorithm-expression=ds$->{user_id % 2}
``
但是仅仅指定了分片规则，没有指定需要分片的表不会分片的，比如 user 表没有按 user_id 进行分库分表，相反，record 和 task 都指定了需要分片，都按照 user_id 分片
```
spring.shardingsphere.sharding.tables.health_record.actual-data-nodes=ds$->{0..1}.health_record
spring.shardingsphere.sharding.tables.health_task.actual-data-nodes=ds$->{0..1}.health_task
```
为了让 user 也按user_id 分片，加上以下配置，重新跑下测试用例
```
spring.shardingsphere.sharding.tables.user.actual-data-nodes=ds$->{0..1}.user
```
重新查看数据库，user 记录按照 user_id 分片了。

##### 绑定表
引入绑定表的概念，主要为了减少关联查询的笛卡尔积，比如:
``
SELECT record.remark_name FROM health_record record JOIN health_task task ON record.record_id=task.record_id WHERE record.record_id in (1, 2);
``
在分库分表场景下，路由后sql如下
``
SELECT record.remark_name FROM health_record0 record JOIN health_task0 task ON record.record_id=task.record_id WHERE record.record_id in (1, 2);
SELECT record.remark_name FROM health_record0 record JOIN health_task1 task ON record.record_id=task.record_id WHERE record.record_id in (1, 2);
SELECT record.remark_name FROM health_record1 record JOIN health_task0 task ON record.record_id=task.record_id WHERE record.record_id in (1, 2);
SELECT record.remark_name FROM health_record1 record JOIN health_task1 task ON record.record_id=task.record_id WHERE record.record_id in (1, 2);
``
假如两个表在分片键相同的前提，为它们设定了绑定关系，那么路由后的SQL为：
```
SELECT record.remark_name FROM health_record0 record JOIN health_task0 task ON record.record_id=task.record_id WHERE record.record_id in (1, 2);
SELECT record.remark_name FROM health_record1 record JOIN health_task1 task ON record.record_id=task.record_id WHERE record.record_id in (1, 2);
```
设定绑定的配置语句如下:
``
spring.shardingsphere.sharding.binding-tables=health_record, health_task
``
注意：该测试用例里的，分片键为 user_id

#####广播表
是指所有分片数据源中都存在的表，也就是说，这种表的表结构和表中的数据在每个数据库中都是完全一样的
```
spring.shardingsphere.sharding.broadcast-tables=health_level
```