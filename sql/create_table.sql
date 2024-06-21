# 数据库初始化

-- 创建库
create database if not exists my_db;

-- 切换库
use my_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
#     unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    address      varchar(1024)                          null comment '邮箱',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
#     index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;
-- 题目表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '题目标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    JudgeCase   text                               null comment '判题用例(JSON数组)',
    JudgeConfig text                               null comment '判题配置(JSON对象,时间空间限制)',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    JudgeInfo  text                               null comment '判题信息(JSON对象)',
    status     int      default 0                 not null comment '判题状态(0->待判题、1->判题中、2->成功、3->失败)',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '提交答案的用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '提交时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交表';
-- 此处使用索引（不把字符串设为索引,对于此处两个索引,在查询语句中分别对这两个字段单独查询,提高查询效率）

-- 公告表
CREATE TABLE if not exists Announcements
(
    announcement_id   bigint auto_increment PRIMARY KEY comment '公告ID',
    announcement_type VARCHAR(256) default '比赛公告'        not null comment '公告类型',
    title             VARCHAR(512)                           not null comment '公告标题',
    content           TEXT                                   not null comment '公告文字',
    image_url         VARCHAR(1024)                          null comment '公告图片',
    user_id           bigint                                 not null comment '发布用户ID',
    publish_date      datetime     default CURRENT_TIMESTAMP not null comment '发布日期',
    updated_user_id   bigint                                 not null comment '更新用户ID',
    update_date       datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新日期',
    is_deleted        tinyint      default 0                 not null comment '是否删除',
    foreign key (user_id) references user (id) on delete no action,
    foreign key (updated_user_id) references user (id) on delete no action
) comment '公告表' collate = utf8mb4_unicode_ci;

# todo-----------------------------------------add----------------------------------------------
# 文章表
drop table if exists articles;
CREATE TABLE if not exists Articles
(
    article_id      bigint auto_increment PRIMARY KEY comment '文章ID',
    article_title   varchar(512)                       not null comment '文章标题',
    article_content TEXT                               not null comment '文章内容',
    user_id         bigint                             not null comment '用户ID',
    watch_count     int      default 0                 not null comment '观看数',
    thumbs_count    int      default 0                 not null comment '点赞数',
    collect_count   int      default 0                 not null comment '收藏数',
    forward_count   int      default 0                 not null comment '转发数',
    reply_count     int      default 0                 not null comment '回复数量',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted      tinyint  default 0                 not null comment '是否删除'
) comment '文章表' collate = utf8mb4_unicode_ci;
# 评论表 todo 后面再添加，这里还有些问题
drop table if exists comments;
CREATE TABLE if not exists Comments
(
    comment_id        bigint auto_increment PRIMARY KEY comment '评论ID',
    comment_content   TEXT                               not null comment '评论内容',
    commenter_id      bigint                             not null comment '评论者ID',
    commented_user_id bigint                             null comment '被评论者ID',
    create_time       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    parent_comment_id bigint                             null comment '父评论ID',
    article_id        bigint                             null comment '文章id',
    thumbs_count      int      default 0                 not null comment '点赞数',
    forward_count     int      default 0                 not null comment '转发数',
    reply_count       int      default 0                 not null comment '回复数量',
    is_deleted        tinyint  default 0                 not null comment '是否删除',
    index idx_parent_comment_id (parent_comment_id)
) comment '评论表' collate = utf8mb4_unicode_ci;

# 视频观看表
CREATE TABLE if not exists video_watch
(
    watch_id    bigint auto_increment PRIMARY KEY comment 'ID',
    material_id bigint                             not null comment '视频id',# 对应资料里面的资料ID
    user_id     bigint                             not null comment '观看用户ID',
    watch_date  datetime default CURRENT_TIMESTAMP not null comment '开始观看日期',
    is_deleted  tinyint  default 0                 not null comment '是否删除' # 如果视频被删除了，这里对应的数据也要软删除
) comment '视频观看表' collate = utf8mb4_unicode_ci;

# 学习中心
CREATE TABLE if not exists Learning_Materials
(
    material_id     bigint auto_increment PRIMARY KEY comment '资料ID',
    video_url       VARCHAR(1024)                      null comment '视频',
    thumb_Num       int      default 0                 not null comment '视频点赞数',
    favour_Num      int      default 0                 not null comment '视频收藏数',
    share_Num       int      default 0                 not null comment '视频转发数',
    watch_Num       bigint   default 0                 not null comment '视频观看数',
    text_content    TEXT                               null comment '文字内容',
    user_id         bigint                             not null comment '发布用户ID',
    publish_date    datetime default CURRENT_TIMESTAMP not null comment '发布日期',
    updated_user_id bigint                             not null comment '更新用户ID',
    update_date     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新日期',
    is_deleted      tinyint  default 0                 not null comment '是否删除'
) comment '学习中心表' collate = utf8mb4_unicode_ci;

# 学习内容上传审核表
CREATE TABLE if not exists Learning_materials_examine
(
    material_id     bigint auto_increment PRIMARY KEY comment '资料ID',
    video_url       VARCHAR(1024)                      null comment '视频',
    text_content    TEXT                               null comment '文字内容',
    user_id         bigint                             not null comment '上传用户ID',
    upload_date     datetime default CURRENT_TIMESTAMP not null comment '上传日期',
    updated_user_id bigint                             not null comment '更新用户ID',
    update_date     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新日期',
    is_deleted      tinyint  default 0                 not null comment '是否删除'
) comment '学习内容上传审核表' collate = utf8mb4_unicode_ci;

# todo-----------------------------------------end----------------------------------------------