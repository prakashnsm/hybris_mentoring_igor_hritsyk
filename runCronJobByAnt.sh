#!/bin/bash
OWN_DIR=`pwd`

export -p OWN_DIR
export -p HYBRIS_DIR="$(dirname "$OWN_DIR")"
export -p HYBRIS_PLATFORM_DIR=$HYBRIS_DIR/hybris/bin/platform


cd $HYBRIS_PLATFORM_DIR
. ./setantenv.sh
ant customize && ant runcronjob -Dcronjob=mailNotificationJob -Dtenant=master
