CREATE TABLE Villagers
(
    villager_id   bigint auto_increment PRIMARY KEY comment '村民ID',
    villager_name VARCHAR(256)  null comment '村民姓名',
    account       VARCHAR(256)  not null comment '村民账号',
    old_password  VARCHAR(512)  not null comment '旧密码',
    new_password  VARCHAR(512)  not null comment '新密码',
    avatar        VARCHAR(1024) null comment '头像',
    introduction  TEXT          null comment '简介',
    phone_number  VARCHAR(50)   null comment '手机号',
    address       VARCHAR(512)  null comment '住址',
    userRole      varchar(256)  not null default 'user' comment '用户角色（user,admin,ban）',
    created_at    datetime      not null default CURRENT_TIMESTAMP comment '创建时间',
    updated_at    datetime      not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted    tinyint       not null default 0 comment '是否删除'
) comment '村民表' collate = utf8mb4_unicode_ci;
CREATE TABLE Points
(
    point_id         bigint auto_increment PRIMARY KEY comment 'ID',
    user_id          bigint        not null comment '用户ID',
    total_points     INT default 0 not null comment '总积分',
    remaining_points INT default 0 not null comment '剩余积分',
    settlement_date  datetime      null comment '结算日期'
) comment '积分表' collate = utf8mb4_unicode_ci;
CREATE TABLE Announcements
(
    announcement_id   bigint auto_increment PRIMARY KEY comment '公告ID',
    announcement_type VARCHAR(256)                       not null comment '公告类型',
    title             VARCHAR(512)                       not null comment '公告标题',
    content           TEXT                               not null comment '公告文字',
    image_url         VARCHAR(1024)                      null comment '公告图片',
    user_id           bigint                             not null comment '发布用户ID',
    publish_date      datetime default CURRENT_TIMESTAMP not null comment '发布日期',
    updated_user_id   bigint                             not null comment '更新用户ID',
    update_date       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新日期',
    is_deleted        tinyint  default 0                 not null comment '是否删除'
) comment '公告表' collate = utf8mb4_unicode_ci;
CREATE TABLE Tasks
(
    task_id         bigint auto_increment PRIMARY KEY comment '任务ID',
    task_content    TEXT                               not null comment '任务内容',
    rule_id         bigint                             not null comment '规则ID',
    user_id         bigint                             not null comment '发布用户ID',
    publish_date    datetime default CURRENT_TIMESTAMP not null comment '发布日期',
    deadline        datetime default CURRENT_TIMESTAMP not null comment '截止日期',
    updated_user_id bigint                             not null comment '更新用户ID',
    update_date     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新日期',
    points_value    INT      default 0                 not null comment '积分数值',
    is_deleted      tinyint  default 0                 not null comment '是否删除'
) comment '任务中心表' collate = utf8mb4_unicode_ci;
CREATE TABLE TaskSubmissions
(
    submission_id bigint auto_increment PRIMARY KEY comment 'ID',
    user_id       bigint            not null comment '用户ID',
    task_id       bigint            not null comment '任务ID',
    is_deleted    tinyint default 0 not null comment '是否删除'
) comment '任务提交表' collate = utf8mb4_unicode_ci;
CREATE TABLE LearningMaterials
(
    material_id     bigint auto_increment PRIMARY KEY comment '资料ID',
    task_id         bigint                             not null comment '任务ID',
    video_url       VARCHAR(1024)                      null comment '视频',
    text_content    TEXT                               null comment '文字内容',
    points_value    INT      default 0                 not null comment '积分数值',
    user_id         bigint                             not null comment '发布用户ID',
    publish_date    datetime default CURRENT_TIMESTAMP not null comment '发布日期',
    updated_user_id bigint                             not null comment '更新用户ID',
    update_date     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新日期',
    is_deleted      tinyint  default 0                 not null comment '是否删除'
) comment '学习中心表' collate = utf8mb4_unicode_ci;
CREATE TABLE LearningCourses
(
    user_id          bigint        not null comment '用户ID',
    material_id      bigint        not null comment '资料ID',
    completion_count INT default 0 not null comment '完成次数'
) comment '学习课程表' collate = utf8mb4_unicode_ci;
CREATE TABLE CourseSubmissions
(
    submission_id bigint auto_increment PRIMARY KEY comment 'ID',
    user_id       bigint            not null comment '用户ID',
    material_id   bigint            not null comment '资料ID',
    is_deleted    tinyint default 0 not null comment '是否删除'
) comment '课程提交表' collate = utf8mb4_unicode_ci;
CREATE TABLE Products
(
    product_id          bigint auto_increment PRIMARY KEY comment '商品ID',
    product_name        VARCHAR(512)                           not null comment '商品名称',
    product_description TEXT                                   not null comment '商品详情',
    product_type        VARCHAR(256) default 'food'            not null comment '商品类型',
    price               DOUBLE       default 0                 not null comment '商品价格',
    stock_quantity      INT          default 0                 not null comment '商品库存',
    shelf_time          datetime     default CURRENT_TIMESTAMP not null comment '上架时间',
    update_time         datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted          tinyint      default 0                 not null comment '是否删除'
) comment '商品表' collate = utf8mb4_unicode_ci;
CREATE TABLE Transactions
(
    user_id              bigint                             not null comment '用户ID',
    product_id           bigint                             not null comment '商品ID',
    transaction_quantity INT      default 0                 not null comment '交易数量',
    transaction_time     datetime default CURRENT_TIMESTAMP not null comment '交易时间',
    is_deleted           tinyint  default 0                 not null comment '是否删除'
) comment '交易表' collate = utf8mb4_unicode_ci;
CREATE TABLE Check_Ins
(
    check_ID        bigint auto_increment PRIMARY KEY comment 'ID',
    check_in_date   datetime default CURRENT_TIMESTAMP not null comment '签到日期',
    user_id         bigint                             not null comment '用户ID',
    check_in_points INT      default 0                 not null comment '签到积分'
) comment '签到表' collate = utf8mb4_unicode_ci;
CREATE TABLE Rules
(
    rule_id         bigint auto_increment PRIMARY KEY comment '规则ID',
    rule_content    TEXT                               not null comment '规则内容',
    rule_points     INT                                null comment '规则积分',
    publish_user_id bigint                             not null comment '发布用户ID',
    publish_date    datetime default CURRENT_TIMESTAMP not null comment '发布日期',
    update_user_id  bigint                             not null comment '更新用户ID',
    update_date     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新日期',
    is_deleted      tinyint  default 0                 not null comment '是否删除'
) comment '规则表' collate = utf8mb4_unicode_ci;
CREATE TABLE Comments
(
    comment_id            bigint auto_increment PRIMARY KEY comment '评论ID',
    comment_content       TEXT                               null comment '评论内容',
    commenter_id          bigint                             not null comment '评论者ID',
    commenter_avatar      VARCHAR(1024)                      null comment '评论者头像',
    commented_user_id     bigint                             not null comment '被评论者ID',
    commented_user_avatar VARCHAR(1024)                      null comment '被评论者头像',
    create_time           datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time           datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    parent_comment_id     bigint                             null comment '父评论ID',
    FOREIGN KEY (parent_comment_id) REFERENCES Comments (comment_id) ON DELETE CASCADE
) comment '评论表' collate = utf8mb4_unicode_ci;