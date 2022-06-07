#!/bin/bash
set -e
psql -U postgres -c 'select 1' -d 'user-test' &>dev/null || psql -U postgres -tc 'create database user-test'