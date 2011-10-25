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