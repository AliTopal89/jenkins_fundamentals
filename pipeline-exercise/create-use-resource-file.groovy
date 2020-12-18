 // ../vars/runLinuxScript.groovy

def call(Map config = [:]) {
  def scriptcontents = libraryResource "scripts/${config.name}"
  writeFile file: "${config.name}", text: scriptcontents
  sh """
    chmod a+x ./${config.name}
    ./${config.name}
  """
}



// ../resources/scripts/build.sh

#!/usr/bin/env bash

git clean -xfd
env
mvn -B -DskipTests clean package



// pipeline-lab repo Jenkinsfile

@Library('shared-library') _
pipeline {
  agent none
  stages {
    stage('Fluffy Build') {
      parallel {
        stage('Build Java 7') {
          agent {
            node {
              label 'java7'
            }

          }
          post {
            success {
              postBuildSuccess(stashName: "Java 7")
            }

          }
          steps {
            //sh './jenkins/build.sh'
            runLinuxScript(name: "build.sh")
          }
        }

        stage('Build Java 8') {
          agent {
            node {
              label 'java8'
            }

          }
          post {
            success {
              stash(name: 'Java 8', includes: 'target/**')
            }

          }
          steps {
            //sh './jenkins/build.sh'
            runLinuxScript(name: "build.sh")
          }
        }

      }
    }
// emitted
...

