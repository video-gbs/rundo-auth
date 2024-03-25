create table rbac_dict
(
    id          bigint auto_increment
        primary key,
    group_name  varchar(255) not null comment '分组名称',
    group_code  varchar(255) not null comment '分组编码',
    item_name   varchar(255) not null comment '字典名称',
    item_value  varchar(255) not null comment '字典值',
    description varchar(255) null comment '描述',
    create_time datetime     null,
    update_time datetime     null
);

create index in_group_code
    on rbac_dict (group_code);

create table rbac_func
(
    id           bigint auto_increment
        primary key,
    menu_id      int     default 0 not null comment '菜单id',
    service_name varchar(250)      not null comment '服务名称',
    func_name    varchar(255)      not null,
    scope        varchar(100)      null comment '范围',
    path         varchar(255)      not null comment '资源路径',
    method       tinyint           not null comment '方法',
    disabled     tinyint default 0 not null comment '是否禁用',
    create_time  datetime          null,
    update_time  datetime          null,
    constraint uni_method_path
        unique (method, path)
)
    comment '功能表';

create table rbac_func_resource
(
    id             bigint auto_increment
        primary key,
    func_id        bigint            not null comment '功能id',
    resource_key   varchar(255)      null comment '资源组',
    validate_param varchar(255)      null comment '需要校验的字段名称，为空返回resource_key数据',
    disabled       tinyint default 0 not null,
    create_time    datetime          null,
    update_time    datetime          null,
    multi_group    varchar(100)      null
)
    comment '功能资源表';

create table rbac_menu
(
    id             bigint auto_increment
        primary key,
    menu_pid       bigint        not null comment '菜单父id',
    sort           int           not null comment '菜单排序',
    menu_type      tinyint       not null comment '菜单类型，1-目录 2-页面',
    path           varchar(255)  null comment '前端路由',
    component      varchar(255)  null comment '前端组件',
    name           varchar(255)  not null comment '菜单名称',
    icon           varchar(255)  null comment '菜单图片',
    description    varchar(255)  null comment '描述',
    level          varchar(255)  not null comment '层级',
    level_num      int           not null comment '层级数',
    is_full_screen int default 0 null,
    disabled       tinyint       not null comment '是否禁用',
    create_time    datetime      null,
    update_time    datetime      null
);

create table rbac_resource
(
    id             bigint auto_increment
        primary key,
    resource_pid   bigint       null comment '父id',
    resource_type  tinyint      null comment '资源类型 1-节点 2-资源',
    resource_name  varchar(255) null,
    resource_key   varchar(255) null,
    resource_value varchar(255) null,
    sort           bigint       null comment '排序',
    level          varchar(255) null comment '层级',
    create_time    datetime     null,
    update_time    datetime     null,
    constraint uni_resource_key_value
        unique (resource_key, resource_value)
);

create table rbac_role
(
    id          bigint auto_increment
        primary key,
    role_name   varchar(100)      not null comment '角色名称',
    role_code   varchar(50)       null comment '角色编码',
    role_desc   varchar(255)      null comment '角色描述',
    disabled    tinyint default 0 not null comment '禁用状态，0-否 1-是',
    deleted     tinyint default 0 not null comment '删除状态',
    create_by   varchar(255)      null,
    create_time datetime          null,
    update_time datetime          null,
    constraint uni_role_name
        unique (role_name)
);

create table rbac_role_func
(
    id          bigint auto_increment
        primary key,
    role_id     bigint       not null,
    func_id     bigint       not null,
    create_time datetime     null,
    create_by   varchar(255) null,
    constraint uni_role_function_id
        unique (role_id, func_id)
);

create table rbac_role_menu
(
    id          bigint auto_increment
        primary key,
    role_id     bigint       not null,
    menu_id     bigint       not null,
    create_time datetime     null,
    create_by   varchar(255) null,
    constraint uni_role_menu_id
        unique (role_id, menu_id)
);

create table rbac_role_resource
(
    id          bigint auto_increment
        primary key,
    role_id     bigint       not null,
    resource_id bigint       not null,
    auth_func   varchar(255) null comment '功能权限，逗号分隔',
    create_time datetime     null,
    create_by   varchar(255) null
);

create table rbac_section
(
    id           bigint auto_increment
        primary key,
    section_pid  bigint       not null comment '父节点id',
    section_name varchar(255) not null comment '部门名称',
    sort         bigint       not null comment '排序',
    leader_name  varchar(255) null comment '负责人',
    phone        varchar(255) null comment '电话号码',
    level        varchar(100) null comment '层级,使用''-''分割',
    description  varchar(255) null comment '描述',
    create_time  datetime     null,
    update_time  datetime     null
);

create table rbac_user
(
    id                bigint auto_increment
        primary key,
    username          varchar(255)      not null comment '用户名',
    password          varchar(255)      not null comment '密码',
    work_name         varchar(255)      not null comment '工作名称',
    phone             varchar(20)       null comment '电话号码',
    work_num          varchar(255)      null comment '工号',
    section_id        bigint            null comment '部门id',
    address           varchar(255)      null comment '住址',
    expiry_start_time datetime          null comment '生效时间',
    expiry_end_time   datetime          null comment '失效时间',
    description       varchar(255)      null comment '描述',
    disabled          tinyint default 0 not null comment '禁用状态 1-是，0否',
    deleted           tinyint default 0 not null comment '软删除标注',
    create_by         varchar(255)      null comment '创建人',
    create_time       datetime          null,
    update_time       datetime          null,
    constraint uni_user_name
        unique (username),
    constraint uni_user_phone
        unique (phone)
);

create table rbac_user_role
(
    id          bigint auto_increment
        primary key,
    user_id     bigint       not null,
    role_id     bigint       not null,
    create_time datetime     null,
    create_by   varchar(255) null,
    constraint uni_user_role_id
        unique (user_id, role_id)
)
    comment '用户角色关系表';

