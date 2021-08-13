create table suzu_plain_user
(
    user_no       bigint unsigned auto_increment
        primary key,
    user_nick     varchar(20)                               null,
    user_password varchar(80)                               null,
    user_gender   enum ('M', 'F') default 'M'               null,
    user_sign     varchar(30)                               null,
    gmt_create    datetime        default CURRENT_TIMESTAMP null,
    gmt_modified  datetime                                  null
);

create table suzu_stranger_message
(
    id                   bigint unsigned auto_increment
        primary key,
    token_no             bigint unsigned                    null,
    message_from_user_no bigint unsigned                    null,
    message_content      text                               null,
    gmt_create           datetime default CURRENT_TIMESTAMP null,
    gmt_modified         datetime                           null
);

create table suzu_stranger_message_token
(
    id            bigint unsigned auto_increment
        primary key,
    message_token text                               null,
    gmt_create    datetime default CURRENT_TIMESTAMP null,
    gmt_modified  datetime                           null
);

create table suzu_user_icon
(
    id            bigint unsigned auto_increment
        primary key,
    user_no       bigint unsigned                    null,
    user_icon_url text                               null,
    gmt_create    datetime default CURRENT_TIMESTAMP null,
    gmt_modified  datetime                           null,
    constraint suzu_user_icon_suzu_plain_user_user_no_fk
        foreign key (user_no) references suzu_plain_user (user_no)
            on delete cascade
);

create table suzu_user_message
(
    message_no      bigint unsigned auto_increment
        primary key,
    user_no_send    bigint unsigned                    null,
    user_no_to      bigint unsigned                    null,
    message_content text                               null,
    gmt_create      datetime default CURRENT_TIMESTAMP null,
    gmt_modified    datetime                           null
);

create table suzu_user_subscribe
(
    id                      bigint unsigned auto_increment
        primary key,
    user_subscriber         bigint unsigned                    null,
    user_subscribe_to       bigint unsigned                    null,
    user_conversation_token text                               null,
    gmt_create              datetime default CURRENT_TIMESTAMP null,
    gmt_modified            datetime                           null
);


