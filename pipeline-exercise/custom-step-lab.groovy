// shared-library/src/branch/master/vars/postBuildSuccess.groovy
def call (Map config = [:]) {
  stash(name: "${config.stashName}", includes: 'target/**')
  archiveArtifacts(artifacts: 'target/*.jar')
}

//Jenkinsfile from pipeline-lab repo
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
              // filename becomes the method call
              postBuildSuccess(stashName: "Java 7")
            }
          // used to be

          /* success {
              stash(name: 'Java 7', includes: 'target/**')
              archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
          }*/

          }
          steps {
            sh './jenkins/build.sh'
          }
        }
        //...