#!/bin/bash

DROPBOX_TEMP=$HOME/Dropbox/temp/bkp
WORKSPACE=NetBeansProjects
PROJECT_FOLDER=CommentTemplate

CURRENT=`pwd`

echo "Removendo backup do Dropbox"
rm $DROPBOX_TEMP/${PROJECT_FOLDER}.tar.gz
echo "Backup removido"

echo ""

echo "Indo para o workspace"
cd $HOME/$WORKSPACE
echo ""

echo "Gerando novo backup"
tar czf $DROPBOX_TEMP/${PROJECT_FOLDER}.tar.gz $PROJECT_FOLDER
echo "Backup terminado"

echo ""

echo "Voltando à pasta anterior"
cd $CURRENT
echo""


