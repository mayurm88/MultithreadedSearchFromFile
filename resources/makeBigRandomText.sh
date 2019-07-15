#!/bin/bash

for i in {1..100000};
do
cat randomText.txt >> veryBigRandomText.txt;
done;
