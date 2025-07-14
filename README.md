Pipeline Azure DevOps

Este arquivo descreve passo a passo o pipeline de build (CI) configurado para a API de Produtos Spring Boot, definido no arquivo azure-pipelines.yml.

Descrição Geral
Este pipeline realiza a integração contínua da API, compilando, testando e gerando o artefato da aplicação.

Gatilhos (Triggers)
yaml
Copiar
Editar
trigger:
  branches:
    include:
      - main 
      - hm

pr:
  branches:
    include:
      - main
      - hm
O pipeline é disparado automaticamente em commits (trigger) e Pull Requests (pr) nas branches main e hm.

Pool e Agente
yaml
Copiar
Editar
pool:
  name: Default
  demands:
        - agent.name -equals michele-token
Define o pool de agentes onde o pipeline será executado.

Especifica a demanda para usar um agente chamado michele-token.

Variáveis
yaml
Copiar
Editar
variables:
  JAVA_VERSION: '21'
  MAVEN_CACHE_FOLDER: $(Pipeline.Workspace)/.m2/repository
  MAVEN_OPTS: '-Dmaven.repo.local=$(MAVEN_CACHE_FOLDER)'
  BUILD_ARTIFACT_NAME: 'apiprodutos-artifact'
Variáveis reutilizáveis para facilitar a manutenção:

Versão do Java (JAVA_VERSION)

Local para cache das dependências Maven

Opções para Maven usar o cache local

Nome do artefato que será gerado

Passos do Pipeline
1. Instalar JDK
yaml
Copiar
Editar
- task: JavaToolInstaller@0
  inputs:
    versionSpec: $(JAVA_VERSION)
    jdkArchitectureOption: 'x64'
    jdkSourceOption: 'PreInstalled'
    preferLatestVersion: true
  displayName: 'Instalar JDK $(JAVA_VERSION)'
Instala o Java Development Kit na versão 21.

Usa um JDK pré-instalado no agente se disponível.

2. Compilar e Empacotar com Maven
yaml
Copiar
Editar
- task: Maven@4
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'clean package'
    publishJUnitResults: true
    testResultsFiles: '**/surefire-reports/TEST-*.xml'
    javaHomeOption: 'JDKVersion'
    jdkVersionOption: '$(JAVA_VERSION)'
    mavenSetM2Home: true
    mavenOpts: $(MAVEN_OPTS)
  displayName: 'Compilar e Empacotar com Maven'
Executa o Maven para limpar e empacotar o projeto.

Publica resultados dos testes JUnit.

Utiliza o cache configurado para acelerar builds.

3. Copiar arquivos JAR para Staging
yaml
Copiar
Editar
- task: CopyFiles@2
  inputs:
    contents: '**/*.jar'
    targetFolder: '$(Build.ArtifactStagingDirectory)'
  displayName: 'Copiar JAR para Staging'
Copia os arquivos .jar gerados para a pasta de staging de artefatos.

4. Publicar Artefato de Build
yaml
Copiar
Editar
- task: PublishBuildArtifacts@1
  inputs:
    pathToPublish: '$(Build.ArtifactStagingDirectory)'
    artifactName: $(BUILD_ARTIFACT_NAME)
  displayName: 'Publicar Artefato de Build'
Publica o artefato para ser usado nas etapas seguintes do processo (como o Release).

