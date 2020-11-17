pipeline {
  agent none
  stages {
    stage('Back End') {
      agent { docker { image 'maven-3:alpine' } }
      steps {
        sh 'mvn clean install'
      }
    }

    stage('Front End') {
      agent { docker { image 'node:11-alpine' } }
      steps {
        sh 'node --version'
      }
    }
  }
}