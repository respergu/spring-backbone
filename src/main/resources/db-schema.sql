drop table TODO if exists;

create table TODO (ID integer identity primary key, CONTENT varchar(255), TODO_ORDER integer , DONE boolean);
