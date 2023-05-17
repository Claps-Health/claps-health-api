create table user_identity (
id                            bigint auto_increment not null,
did                           varchar(255) not null,
pubkey                        varchar(255) not null,
year_of_birth                 integer,
gender                        varchar(20),
living_country                varchar(255),
living_city                   varchar(255),
email_hash                    varchar(255),
email_verified                tinyint(1) DEFAULT 0,
twitter_account_hash          varchar(255),
twitter_verified              tinyint(1) DEFAULT 0,
account_level                 integer,
date_created                  datetime not null,
date_updated                  datetime,
verification_id               bigint,
constraint uq_user_identity_did unique (did),
constraint uq_user_identity_verification_id unique (verification_id),
constraint pk_user_identity primary key (id)
);

create table user_identity_verification (
id                            bigint auto_increment not null,
did                           varchar(255) not null,
verification_type             varchar(32) not null,
link                          varchar(1024),
account                       varchar(255),
account_hash                  varchar(255),
verified                      tinyint(1) DEFAULT 0,
constraint uq_user_identity_verification_did unique (did),
constraint pk_user_identity_verification primary key (id)
);

alter table user_identity add constraint fk_user_identity_verification_id foreign key (verification_id) references user_identity_verification (id) on delete restrict on update restrict;