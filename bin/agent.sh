#!/bin/bash

java -cp ` find lib/*.jar | xargs echo | sed 's/ /:/g' ` net.pixelcop.monstrous.agent.Main $*

