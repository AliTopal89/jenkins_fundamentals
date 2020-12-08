//Jenkinsfile
// @shared-starter is another branch from shared library repository
@Library(shared-library@shared-starter)
pipeline {
  agent any

  stages {
    stage('hello') {
      steps {
        helloWorldSimple('Fred','Thursday')
      }
    }
  }
}

// shared-library/src/branch/shared-starter/vars/helloWorldSimple.groovy

def call(String name, String dayOfWeek) {
  sh "echo Hello World ${name}. It is ${dayOfWeek}"
}