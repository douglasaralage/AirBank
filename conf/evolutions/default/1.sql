# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table event (
  id                        bigint not null,
  nome_evento               varchar(255),
  horas_eventos             varchar(255),
  sobre_evento              varchar(255),
  local_evento              varchar(255),
  data_evento               varchar(255),
  img_event                 LONGBLOB,
  constraint pk_event primary key (id))
;

create table noticias (
  id                        bigint not null,
  nome_noticias             varchar(255),
  sobre_noticias            varchar(255),
  data_noticias             varchar(255),
  img_noticias              LONGBLOB,
  constraint pk_noticias primary key (id))
;

create table account (
  id                        bigint not null,
  email                     varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  tipo                      varchar(2),
  quantidadehoras           integer,
  constraint ck_account_tipo check (tipo in ('P','A')),
  constraint pk_account primary key (id))
;

create sequence event_seq;

create sequence noticias_seq;

create sequence account_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists event;

drop table if exists noticias;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists event_seq;

drop sequence if exists noticias_seq;

drop sequence if exists account_seq;

