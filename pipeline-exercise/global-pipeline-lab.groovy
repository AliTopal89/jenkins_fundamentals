//gitserver/butler/shared-library/src/branch/master/vars/helloWorld.groovy

 
def call(Map config) {
    sh "echo Hello world, ${config.name}"
}


//Jenkinsfile:

@Library('shared-library') _

pipeline {
  agent { label 'java'}
  
  stages {
    stage('verify') {
      steps {
        helloWorld(name: 'Vegeta')
      }
    }
  }
}

// Output:

// Hello world, Vegeta