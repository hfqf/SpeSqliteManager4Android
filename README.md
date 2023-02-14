<!--
 * @Author: user.email
 * @Date: 2023-02-14 22:20:49
 * @LastEditors: user.email
 * @LastEditTime: 2023-02-15 00:03:45
 * @FilePath: /undefined/Users/points/Documents/git/SpeSqliteManager4Android/README.md
 * @Description: 
 * 
 * Copyright (c) 2023 by ${git_email}, All Rights Reserved. 
-->
# SpeSqliteManager4Android

#### 介绍
一个轻量级管理android数据自动升级的管理类

#### 核心设计思想
1.以静制动:配置项代替代码，保证代码稳定性

#### 设计思路
SpeSqliteUpdateManager类和SpeSqlSetting.josn一起用来控制本地数据的创建和升级功能。升级功能基于应用每次升级后，本地目录中的数据不变做的。

#### 关配置项json:
* 1.dbName:是保存到沙盒的数据库文件的名称。
* 2.dbVersion:数据库版本号,判断本地数据库文件是否升级就通过此key。
* 3.dbTables:想要创建的表名,每个表名下是具体的字段。
* 4.上面3个字段名最好不要改，如果修改了话，连同程序里的宏也请同时修改下。
* 5.对于已经已使用xml的应用，注意xml中的值不要乱改动(保证配置项的稳定)。
#### 关配置项json:
* 1.目前写的逻辑只是用来只创建了一个db文件。如果配置创建多个db文件，请注意。
* 2.修改表时,之前不用的表和字段作为冗余表和字段,不要删。
#### 继承方式
1.直接调用如下代码即可,可自动创建和升级数据库
```SqliteDataManager.sharedInstance().init()```
