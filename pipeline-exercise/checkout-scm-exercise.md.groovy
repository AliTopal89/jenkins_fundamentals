    node('java'){
      stage('checkout'){
        checkout([$class: 'GitSCM', 
        branches: [[name: 'simple-pipeline']], 
        doGenerateSubmoduleConfigurations: false, 
        extensions: [], 
        submoduleCfg: [], 
        userRemoteConfigs: [[credentialsId: 'butler-scripted-pipeline', url: 'http://localhost:5000/gitserver/butler/pipeline-demo.git']]])
      }
      stage('Run'){
        echo 'Build my code'
        sh './jenkins/build.sh'
        archiveArtifacts allowEmptyArchive: true, artifacts: 'target/*.jar', fingerprint: true, onlyIfSuccessful: true
      }
    }
