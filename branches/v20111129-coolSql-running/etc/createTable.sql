create TABLE prmextensions
(                           -- 不是大括弧
    extensionpoint integer not null, --扩展点
    extensionbean varchar(1000) not null, --实现类
    extensiondesc varchar(1000), --描述
    extensioni varchar(1000), --接口
    extensionclass  varchar(1000), --抽象类
    inserttime timestamp default current timestamp --插入时间
)
;

create TABLE PRMPROPS 
(
			n varchar(300) not null,--name
			v varchar(300) --value
);

select count(*)   from sys.SYSTABLES t where t.TABLENAME='PRMPROPS';--系统属性表

-- 插入
select * from PRMPROPS
select count(*) from PRMPROPS  t where  t.n='user.dir';

insert into PRMPROPS values('user.dir','i')

update PRMPROPS t set t.v='me' where t.n='user.dir'

insert into PRMPROPS values('a','b')


select count(*) from PRMPROPS where n='os.name'

select * from PRMPROPS

delete from PRMPROPS where 1=1

drop table PRMPROPS

update PRMPROPS set v='MOS' where n!='os.name'