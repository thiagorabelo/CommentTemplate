#!/bin/bash

DROPBOX=Dropbox/temp/bkp
WORKSPACE=NetBeansProjects
PROJECT_FOLDER=CommentTemplate

echo "Removendo cópia do Dropbox"
rm -fr ~/${DROPBOX}/${PROJECT_FOLDER}
echo "Cópia removida"

echo ""
echo ""

echo "Copiando novos arquivos para o Dropbox"
cp -fr ~/${WORKSPACE}/${PROJECT_FOLDER} ~/${DROPBOX}/
echo "Cópia terminada"
echo ""

