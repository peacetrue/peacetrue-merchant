drop table if exists `merchant`;
create table `merchant`
(
    id            bigint primary key auto_increment comment '主键',
    username      varchar(32)  not null comment '商家名',
    password      varchar(255) not null comment '密码',
    creator_id    bigint       not null comment '创建者主键',
    created_time  datetime     not null comment '创建时间',
    modifier_id   bigint       not null comment '修改者主键',
    modified_time timestamp    not null comment '最近修改时间',
    unique (username)
);

comment on table `merchant` is '商家';
