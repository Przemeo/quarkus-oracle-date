#!/bin/bash
#put in /home/sql
set -Eeuo pipefail

sqlplus -s / as sysdba <<EOF
	-- Exit on any errors
	WHENEVER SQLERROR EXIT SQL.SQLCODE

	ALTER SESSION SET CONTAINER = XEPDB1;
	ALTER DATABASE SET TIME_ZONE='Asia/Seoul';
	SHUTDOWN IMMEDIATE;
	STARTUP;

   exit;
EOF

echo "Database XEPDB1 is now configured with Time Zone: Asia/Seoul."