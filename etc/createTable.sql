create TABLE sysProps 
{
			name varchar(300) not null,
			value varchar(300)
};


select count(*)   from sys.SYSTABLES t where t.TABLENAME='sysprops' ;