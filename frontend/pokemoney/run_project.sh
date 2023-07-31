#!/bin/bash

# Define the path to your project
PROJECT_PATH="D:\Data_engineering\diploma project\Pokemoney\frontend\pokemoney"

# Define the device name
DEVICE_NAME="Pixel 4 XL API 30"

# Open VS Code with the project folder
code $PROJECT_PATH

# Start the emulator
echo "no" | flutter emulators --launch $DEVICE_NAME

# Run the project
cd $PROJECT_PATH
flutter run
