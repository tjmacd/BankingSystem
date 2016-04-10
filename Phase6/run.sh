#!/bin/bash

# Add Permissions
chmod +x simulation/daily.sh
#chmod +x simulation/weekly.sh

if [ -a simulation/day_*/ ] ; then
  rm simulation/day_*/
fi;

#
# Lets Run Frontend
#
if [ -a simulation/frontend ] ; then
  echo "Frontend Executable found!"
else
  echo "No Frontend Executable found. Compiling new frontend..."
  cd frontend
  make
  cd ..
fi;

#
# Lets Run Backend
#
if [ -a simulation/.class ] ; then
  echo "Backend Executable found!"
else
  echo "No Backend Executable found. Compiling new backend..."
  cd backend
  make
  cd ..
fi;

#
# Remove old files
#

if [ -a simulation/daily ] ; then
  echo "Found daily files. Removing..."
  rm -r simulation/daily
  mkdir simulation/daily
else
  mkdir simulation/daily
fi;

for day in $(ls input_files | grep day_)
do
  mkdir simulation/daily/$day
  cp -R input_files/$day simulation/daily/
done


echo "Running simulation..."

cd simulation
./weekly.sh
