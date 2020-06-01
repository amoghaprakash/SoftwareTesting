#!/bin/bash
#===============================================================================
# Amogha Kagal Jayaprakash, Kasidit Nusitchaiyakan, Heetae Yang
# Professor McKee
# CPSC 5200-01
# June 2020                Milestone #2
#
# File: buildTestSuite.sh
#
# Description:
# This shell script builds the Battleship project and associated J-Unit unit
# tests.
#
#   DEPENDENCIES, LIMITATIONS, & DESIGN NOTES:
#           1. All source files are built, then all J-Unit test files are built.
#           2. Artifacts are placed in the $OUT_DIR directory.
#       Limitations :
#           1. Due to memory limitations on SU's CS1 server, the $CS1_HACK
#              variable is used to limit the memory used by the JVM.
#
#   Example Usage:
#   "./buildTestSuite.sh"
#===============================================================================

#set -o errexit
#set -o pipefail
set -o nounset
#set -o xtrace

#===============================================================================
# Constants
#===============================================================================
OUT_DIR="out"
PROJECT_ROOT=".."
#JDK_PATH="$PROJECT_ROOT/../jdk1.8.0_211/bin"
JUNIT_JAR="libraries/junit_4.12.jar"
HAMCREST_JAR="libraries/hamcrest.core_1.3.0.jar"

# Hack required due to memory limitations on CS1
CS1_HACK="-J-Xmx512m"  # Limit heap to 512 MB

# NOTE: This list needs to be updated if any additional source files are added
#       to the project!
sourceList=(
    PhoneBook
)

# NOTE: This list needs to be updated if any additional test files are added
#       to the project!
testList=(
    PhoneBookTest
    PhoneBookTestSuit
    PhoneBookTestRunner
)

#.csv files
resourceList=(
    contacts
    contactsEmpty
    contactsValid
    newcontact
)

#===============================================================================
# Script
#===============================================================================
echo "Building test suite..."

echo "Removing stale artifacts..."
rm -rf $PROJECT_ROOT/$OUT_DIR

echo "Creating directory for build artifacts..."
mkdir -p $PROJECT_ROOT/$OUT_DIR


echo "Printing javac version..."
javac $CS1_HACK -version

echo "Building source code..."
for i in ${sourceList[@]}; do
    echo "Building: $i.java --> $i.class"
    javac $CS1_HACK -d $PROJECT_ROOT/$OUT_DIR -cp $PROJECT_ROOT $PROJECT_ROOT/src/$i.java
    if [ $? != 0 ]; then
        echo "ERROR: Unable to build $i.java! Aborting build..."
        exit 1
    fi
done

echo "Building unit tests..."
for i in ${testList[@]}; do
    echo "Building: $i.java --> $i.class"
    javac $CS1_HACK -d $PROJECT_ROOT/$OUT_DIR -cp $PROJECT_ROOT/$OUT_DIR:$PROJECT_ROOT/$JUNIT_JAR:$PROJECT_ROOT/$HAMCREST_JAR $PROJECT_ROOT/src/$i.java
    if [ $? != 0 ]; then
        echo "ERROR: Unable to build $i.java! Aborting build..."
        exit 2
    fi
done

echo "Building unit tests..."
for i in ${resourceList[@]}; do
    echo "Copying: $i.csv --> $i.csv"
    cp $PROJECT_ROOT/src/$i.csv $PROJECT_ROOT/$OUT_DIR
    if [ $? != 0 ]; then
        echo "ERROR: Unable to build $i.csv"
        exit 2
    fi
done

echo "Build success!"
exit 0