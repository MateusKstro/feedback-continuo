create table access(
id_access numeric primary key,
access_name text not null
);

create sequence seq_access
increment 1
start 1;

create table users(
id_user numeric primary key,
id_access numeric not null,
user_name text not null,
user_role text not null,
email text unique not null,
user_password text not null,
avatar text,
constraint fk_access_id_access 
   foreign key (id_access) 
references access (id_access)
);

create sequence seq_users
increment 1
start 1;

create table feedback (
    id_feedback numeric not null,
    message text not null,
    date_create timestamp not null,
    anonymous boolean not null,
    id_user numeric not null,
    feedback_id_user numeric not null,
    primary key(id_feedback),
    constraint fk_user_id_user 
       foreign key (id_user) 
    references users (id_user),
    constraint fk_user_feedback_id_user 
       foreign key (feedback_id_user) 
    references users (id_user)
);

create sequence seq_feedback
increment 1
start 1;

create table tags(
    id_tag numeric not null,
    name_tag text not null unique,
    primary key(id_tag)
);

create sequence seq_tags
increment 1
start 1;

create table feedback_tags (
     id_feedback numeric not null,
     id_tag numeric not null,
     constraint fk_ft_id_feedback 
        foreign key (id_feedback) 
     references feedback (id_feedback),
     constraint fk_ft_id_tag 
        foreign key (id_tag) 
     references tags (id_tag)
);

create sequence seq_feedback_tags
increment 1
start 1;

insert into access(id_access, access_name)
values (nextval('seq_access'), 'ROLE_USER');