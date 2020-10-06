## JENKINS PIPELINE FUNDAMENTALS

### Pipeline introduction

Continous Delivery (CD) means every change is ready to be deployed.
  - SCM hooks/webhooks detect the source code changes and trigger a pipeline run

CD build flow is defined as a project with:
- Freestyle Projects
- Pipeline Projects
  - Declarative Pipeline
  - Scripted Pipeline

#### Jenkins Vocab
   - **Master Jenkins**
     - Computer VM or container where jenkins is installed
     - serves requests and handles build tasks

   - **Jenkins Agent**
     - Computer VM or container that connects to a jenkins master
     - Executes tasks directed by master jenkins
     - has a number and scope of operations to perform

   - **Jenkins Node**
     - is sometimes used to refer to master jenkins and agents

   - **Executor**
     - Computational resource for running builds, performs operations
     - can be parallelized on master or agent


#### Freestyle ("Chained") Projects

- Use job orchestration tools such as [Job DSL Plugin](https://github.com/jenkinsci/job-dsl-plugin) or [Jenkins Job Builder](https://opendev.org/jjb/jenkins-job-builder)

- Provides only sequential steps

#### Pipeline Pojects

Uses the Pipeline DSL which programatically manipulates Jenkins objects. Captures the entire
continuous delivery process as code.

- Scripted: sequential execution, using Groovy expressions for flow control
- Declarative: uses a framework to control execution
- A pipeline is defined in a Jenkinsfile, Uses a DSL based Apache Groovy syntax
- The Jenkinsfile is stored on an SCM so works with conventions such as pull requests, branches
- Jenkinsfile is structured in sections called `stages`
- each stage includes `steps`
- `steps` include the actual tests/commands to run
- An `agent` defines where the program or scripts to execute eg `agent { label 'docker-jenkins-rails' }`
  - Example:

    ```groovy
    pipeline {
      agent { label 'buyakasha' }
      stages {
        stage('My Build') {
          steps {
            sh './jenkins/build.sh'
          }
        }
        stage('My Test Build') {
          steps {
            sh './jenkins/testbuild.sh'
          }
        }
      }
    }
    ```

**Benefits**: 
  - Jenkins master can restart and Pipeline continues to run (durable), 
  - can stop for manual approval (pausable),
  - supports typical CD requirements like fork, parallelize, loop, join (versatile),
  - supports custom extensions to its DSl (Extensible),
    -  for example:
      - Create a new plugin project, either fork the simple build one, or add a dependency to it in your `pom.xml` / `build.gradle` file,
      - Put your dsl in the resources directory (note the "package dsl" declaration at the top)
      - Create the equivalent extension that just points to the DSL by name like [this](https://github.com/jenkinsci/simple-build-for-pipeline-plugin/blob/master/src/main/java/org/jenkinsci/plugins/simplebuild/SimpleBuildDSL.java) This is mostly "boiler plate" but it tells Jenkins there is a `GlobalVariable` extension available when Pipelines run
       - Deploy it to an Jenkins Update Center to share with your org, or everyone!
  - Reduces number of jobs 
  - Decentralization of job configurations
    - *A decentralized model empowers individual teams to make decisions based on their own needs not some pre-configured corporate policy.*

### Further Reading and References
1. [Decentralied ci-cd vs centralized](https://medium.com/@oprearocks/centralized-vs-decentralized-ci-cd-strategies-for-multiple-teams-dd1ba792c1ac)
