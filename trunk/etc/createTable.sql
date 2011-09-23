create TABLE sysProps if not exist
{
			name varchar(300) not null,
			value varchar(300)
};

var v;
select count(*) into v  from sys.SYSTABLES t where t.TABLENAME='sysprops' ;