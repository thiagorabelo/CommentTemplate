#!/bin/bash

DROPBOX_TEMP=Dropbox/temp/bkp
WORKSPACE=NetBeansProjects
PROJECT_FOLDER=CommentTemplate

CURRENT=`pwd`

echo "Movendo para o workspace: ${HOME}/${WORKSPACE}/"
cd ${HOME}/${WORKSPACE}

echo ""

echo "Copiando arquivos temporários"
cp ${HOME}/${DROPBOX_TEMP}/${PROJECT_FOLDER}.tar.gz ${HOME}/${WORKSPACE}
echo "Arquivos copiados"

echo ""

echo "Removendo cópia antiga do Workspace"
rm -fr ${HOME}/${WORKSPACE}/${PROJECT_FOLDER}
echo "Cópia removida"

echo ""

echo "Extraindo aquivos"
tar xzf ${PROJECT_FOLDER}.tar.gz
echo "Arquivos extraidos"

echo ""

echo "Removendo aquivo temporário"
rm  ${HOME}/${WORKSPACE}/${PROJECT_FOLDER}.tar.gz
echo "Arquivo removido"

echo ""

echo "Voltando para pasta inicial"
cd ${CURRENT}

