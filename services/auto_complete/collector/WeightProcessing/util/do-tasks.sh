#!/bin/bash

TARGET_ID=$(date +%Y%m%d_%H%M)
MAX_NUMBER_OF_INPUT_FOLDERS="3"
AVRO_LIBPATH="/HADOOP_TASKS/avro/avro-tools-1.8.2.jar"

echo "Checking if /words/kafka_sink/words-from-client/ exists ..."
while ! $(hadoop fs -test -d "/words/kafka_sink/words-from-client/") ; do \
    echo "Waiting /words/kafka_sink/words-from-client/ to be created by kafka sink ..."; 
done

echo "Checking for contents in /words/kafka_sink/words-from-client/.."
while [[ $(hadoop fs -ls /words/kafka_sink/words-from-client/ | sed 1,1d) == "" ]] ; do \
    echo "Waiting for kafka sink to populate /words/kafka_sink/words-from-client/* ..."; 
done

#========================== COMPUTE WEIGHT==================================
TASK_NAME="WeightCompute"
INPUT_FOLDERS=`hadoop fs -ls /words/kafka_sink/words-from-client/ | sed 1,1d | sort -r -k8 | awk '{print \$8}' | head -${MAX_NUMBER_OF_INPUT_FOLDERS} | sort`
JAR_FILEPATH="/HADOOP_TASKS/${TASK_NAME}/${TASK_NAME}.jar"
OUTPUT_PATH="/words/temp"
export HADOOP_CLASSPATH=`echo ${AVRO_LIBPATH} | sed s/,/:/g`
# CREATE WEIGHT HDFS BATCH
hadoop fs -mkdir -p /words/with_weight/${TARGET_ID}/

#build jar 
./HADOOP_TASKS/build-jar.sh ${TASK_NAME}

echo "---- JAR TEST----------"
ls HADOOP_TASKS/${TASK_NAME}
echo "-----------------------"
ls HADOOP_TASKS/${TASK_NAME}/com/example/GatheringService
echo "-----------------------"
hadoop fs -cat /words/kafka_sink/words-from-client/20211211_1151/words-from-client+0+0000000000+0000000002.avro
echo "---- JAR TEST----------"

cd HADOOP_TASKS/${TASK_NAME}

base_weight=1
for input_folder in ${INPUT_FOLDERS}; do 
    echo "============================================================"
    echo "Processing input: " $input_folder 
	echo "base_weight: " $base_weight 

    echo "------- INPUT DATA --------------" 
    pwd
    hadoop fs -ls ${input_folder}
    echo "---------------------------------"

    hadoop jar ${JAR_FILEPATH} ${TASK_NAME} \
               -libjars ${AVRO_LIBPATH} \
               ${input_folder} ${OUTPUT_PATH} ${base_weight}

    echo "----------------- OUTPUT TEMP ------------------------------"
    hadoop fs -cat ${OUTPUT_PATH}/*
    echo "------------------------------------------------------------"

    hadoop fs -rm -r ${OUTPUT_PATH}
    base_weight=$((base_weight*2))
    echo "============================================================"
done


