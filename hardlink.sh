#!/bin/bash
OWN_DIR=`pwd`

export -p OWN_DIR
export -p HYBRIS_DIR="$(dirname "$OWN_DIR")"
export -p HYBRIS_CONFIG=$HYBRIS_DIR/hybris/config
export -p HYBRIS_CUSTOMIZE_FOLDER=$HYBRIS_CONFIG/customize
export -p HYBRIS_EXTENSIONS=$HYBRIS_DIR/hybris/bin/custom

	# delete localextensions.xml
	if [ -f "$HYBRIS_CONFIG/localextensions.xml" ]
		then
			rm $HYBRIS_CONFIG/localextensions.xml
		else
			echo localextensions.xml DOES NOT EXIST
	fi

	# delete local.properties
	if [ -f "$HYBRIS_CONFIG/local.properties" ]
		then
			rm $HYBRIS_CONFIG/local.properties
		else
			echo local.properties DOES NOT EXIST
	fi

	# delete all data from customize folder
	rm -rfv $HYBRIS_CUSTOMIZE_FOLDER/*

	# delete all extensions from hybris custom folder
	files=($HYBRIS_EXTENSIONS/*)
		if [ ${#files[@]} -gt 0 ]; 
			then 
				 for d in $HYBRIS_EXTENSIONS/*; do
			       rm -rf $d
			     done
	fi

	# prepare platformadministration.xm for ant customize (make essential folder structure and copy file)
	mkdir -p $HYBRIS_CUSTOMIZE_FOLDER/platform/resources/ant/

	PLATFORMADMINISTRATION_FOLDER=$HYBRIS_CUSTOMIZE_FOLDER/platform/resources/ant/

	cp $OWN_DIR/config/platformadministration.xml $PLATFORMADMINISTRATION_FOLDER

	# hard link to file in hybris/config
	ln "$OWN_DIR/config/localextensions.xml" "$HYBRIS_CONFIG/localextensions.xml"
	ln "$OWN_DIR/config/local.properties" "$HYBRIS_CONFIG/local.properties"

	# simlink to for extensions folder in from hybris custom folder
	for d in $OWN_DIR/extensions/*/; do
		  extname=`basename "$d"`
		  ln -s "$d" "$HYBRIS_EXTENSIONS/$extname" 
	      echo "$extname"
	done