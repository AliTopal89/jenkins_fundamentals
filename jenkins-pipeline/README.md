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
#### Declarative VS Scripted pipeline

Both definitions are stored in a jenkinsfile under SCM (pipeline-as-code)
Both can use steps to build into pipeline or provided in plugins
Both support shared libraries
 - Scripted Pipeline:
   - Executed sequentially from top to bottom
   - Limitations are groovy lang limitations
 - Declarative Pipeline
   - Stricter, pre-defined structure
   - Execution may resume after interruptions
   - Using blue ocean simplfies the pipeline creation even more
   - Use the `script` step to include bits of code when you need capabilites beyond declarative syntax

**Classic web UI** provides tools such as Declarative Directive Generator and Snippet Generator
  - **Declarative Generator**:
    - The Directive Generator allows you to generate the Pipeline code for a Declarative Pipeline directive, such as agent, options, when, and more. Choose the directive you're interested in from the dropdown, and then choose the contents of the directive from the new form. Once you've filled out the form with the choices and values you want for your directive, click Generate Declarative Directive and the Pipeline code will appear in the box below. You can copy that code directly into the pipeline block in your Jenkinsfile, for top-level directives, or into a stage block for stage directives.
  - **Snippet Generator**:
    - will help you learn the Pipeline Script code which can be used to define various steps. Pick a step you are interested in from the list, configure it, click Generate Pipeline Script, and you will see a Pipeline Script statement that would call the step with that configuration. You may copy and paste the whole statement into your script, or pick up just the options you care about. (Most parameters are optional and can be omitted in your script, leaving them at default values.)

**Blue Ocean pipeline editor** generates a Jenkinsfile and stores it in the source code repo. 
  - Allows you to add/remove config stages and steps with a GUI
  - Provides visualization for the pipeline run, becomes handy with parallel steps especially
  - the GUI doesn't support all features such as `build now` `build with params` classic web UI option
  - Supports apply options to the pipeline, and use the `when` directive. `post` section of your job is supported on blue ocean.

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

### Create Pipeline

Declarative pipeline keeps the complex logic of individual steps seperate from the pipeline itself, making the pipeline code clean to read

#### SCM with blue ocean and first pipeline
- New Pipeline -> Git -> for training purposes [use](http://localhost:5000/gitserver/butler/pipeline-demo)
- When supplied with a valid URL, Jenkins detects that the repo is available for SSH access and auto generates an SSH key
  with git, github, gitea supports ssh out of the box, so it puts the public part of the ssh key into the remote blue ocean server.
- Blue Ocean displays the generated ssh key for the user

- A `stage` groups task to be done; it includes steps
  - Stages are the logical segmentation of a pipeline
- A `step` defines the actual task (test/commands to run, script to execute)
- The pipeline code executes on master
- Most of the stage blocks executes on agents

- Create a Buzz Buzz Stage on `any` agent select print message `step` on Buzz Buzz `stage`, commit, save & run
- With Blue ocean it created Jenkinsfile
  ```groovy
    pipeline {
      agent any
      stages {
        stage('Buzz Buzz') {
          steps {
            echo 'Bees Buzz!'
          }
        }
      }
    }
  ```
  - cmd+S on mac brings the jenkinsfile pipeline script to edit. 
  - for declarative pipeline, the first line is the `pipeline` block
  - `agent` specifies where the pipeline or specific `stage` executes
  - `stage` conceptually a distinct subset of the pipeline, Used to present pipeline status/progress.
  - `steps` A series of distinct tasks inside a stage. 

### Lab - Create first pipeline
- New Pipeline -> Git -> add ssh [url](http://localhost:5000/gitserver/butler/pipeline-lab)
- Final pipeline should be
```groovy
pipeline {
  agent any
  stages {
    stage('Fluffy Build') {
      steps {
        echo 'Placeholder'
        sh 'echo Edited Placeholder'
      }
    }

    stage('Fluffy Test') {
      steps {
        sh 'sleep 5'
        sh 'echo Success!'
      }
    }

    stage('Fluffy Deploy') {
      steps {
        echo 'Placeholder'
      }
    }

  }
}
```

### Pipeline to a Branch

Blue Ocean by default saves to the branch last used in this case `simple-pipeline` branch

#### Artifacts and Fingerprints

An `artifact` is a file produced as a result of a Jenkins build. By default they are stored
where they are created so they are deleted when the workspace gets wiped out, unless it gets archived.

Jobs can be configured to archive artifacts based o filename patterns and are kept forever unless there
is a retention policy to delete them periodically. 

- Use the pipeline step `archiveArtifacts` 
- Requires a pattern `like my-app.zip`, `images/.*png`, `target/**/*.jar` 
- Archiving keeps those files in `${JENKINS_HOME}` forever unless you have a retention policy to delete them.

A fingerprint is the mD5 checksum of an artifact. 
Jenkins uses Fingerprints to keep track of artifacts without any ambiguity

- Archive the Artifacts on blue ocean with adding `Archive the Artifacts` step and type `target/*.jar`
- This will create an archive that contains the artifact that is the output of build.sh

All artifacts of a build live in:
  - http://${JENKINS_URL}/job/${YOUR_JOB}/${BUILD_NUMBER}/artifact

In production env, the build chain system (Maven, Gradle etc) publishes artifact to an artifacr repo
like Artifactory, Nexus etc. 

Each pipeline generates a `pipeline.log` artifact when it runs.
Good practice to discard builds after certain days and max number of builds 
with Configure -> Discard old items checkbox. 

#### JUnit

Testing framework for Java programs. In Jenkins context, it is a publisher that consumes XML test reports.
  - which generates graphical visualization of test results
  - You must specify the appropriate path name for the XML test reports generated by the build
    tool you are using.

#### Set Env Variables

BUILD_NUMBER, NODE_NAME, JOB_NAME - jenkins specific env variables, additional env variables can be set 
in the pipeline.

```groovy
environment {
    BUZZ_NAME = 'Worker Bee'
  }
  ...
    steps {
        sh 'echo "I am ${BUZZ_NAME}"'
```
#### Parallel Stages

Each parallelized branch is a stage
- a stage can either have `steps` or `parallel` at the top level but not both at top level
- a stage within a `parallel` stage can contain `agent` and `steps` sections
- a `parallel` stage cannot contain `agents` or `tools` 

Add `failFast true` to force all parallel process to abort if one fails. 
Pipeline doesn't support arbitrary parallel stage depth (you cannot have parallel block in a parallel block).

How Parallel Stages are scheduled: Multiple parallel steps execute on the same node while others are idle.

### Lab - Simple pipeline with parallel stages

```groovy
pipeline {
  agent any
  stages {
    stage('Fluffy Build') {
      steps {
        sh './jenkins/build.sh'
        archiveArtifacts 'target/*.jar'
      }
    }

    stage('Fluffy Test') {
      parallel {
        stage('Backend') {
          steps {
            sh './jenkins/test-backend.sh'
            junit 'target/surefire-reports/**/TEST*.xml'
          }
        }

        stage('Frontend') {
          steps {
            sh './jenkins/test-frontend.sh'
            junit 'target/test-results/**/TEST*.xml'
          }
        }

        stage('Performance') {
          steps {
            sh './jenkins/test-performance.sh'
          }
        }

        stage('Static') {
          steps {
            sh './jenkins/test-static.sh'
          }
        }

      }
    }

    stage('Fluffy Deploy') {
      steps {
        sh './jenkins/deploy.sh staging'
      }
    }

  }
}
```

### Scripted Pipeline

Differs from Declarative where:
  - its more flexible, allows more complexity
  - can be more difficult to learn and use

Simple Scripted pipeline
  ```groovy
    stage('Build') {
        parallel linux: {
            node('linux') {
                checkout scm
                try {
                    sh 'make'
                }
                finally {
                    junit '**/target/*.xml'
                }
            }
        },
        windows: {
            node('windows') {
                /* .. snip .. */
            }
        }
    }
  ```
  - uses `node` instead of `agent`, 
  - no `pipeline` block, 
  - must explicitly handle `checkout scm`
  - for example:
    - ```groovy
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
      ```


#### pipeline-sytax
 
Generates valid code for Scripted Pipeline steps, based on desired task, which can be also used for declarative pipelines.

When needed, the Snippet Generator provides forms/checklists where you provide arguments for fields used for selected step.

#### Multibranch

Jenkins creaetes a subproject for each branch in SCM repo.
Recommended for all new pipelines because, 
  - automatic workflow creation for each new branch in the repo
  - Builds are specific to that branch, its unique SCM change and build history
  - Automatic prunning/deleteion for branches deleted in scm repo.
  - Ability to override the parent properties for a specific branch if necessary such as:
    - `$class PipelineProperty` - Configure a task name and which stage the project 
    should be grouped by in the delivery pipeline view.
    - `readTrusted` From a multibranch Pipeline project, reads a file from 
    the associated SCM and returns its contents. Thus `readTrusted 'subdir/file'` is similar to 
    `node {checkout scm; readFile 'subdir/file'}`.

#### Pipeline Stage View

Shows a matrix with build history and stages as dimensions, is an alternative to blue ocean display
Displays pipeline Data, just as Blue ocean does
- Date time changes per build
- Execution Time per build and per stage
- Status and Output logs per stage
- You can replay a build but not start a new build on blue ocean at our work jenkins

#### Reference Documentation

- Pipeline Syntax
```groovy
pipeline {
    /* insert Declarative Pipeline here */
} 
```
  - Sections in Declarative Pipeline typically contain one or more Directives or Steps.
    - Directives
      - envrionment - specifies a sequence of key-value pairs which will be 
      defined as environment variables for all steps, or stage-specific steps 
        - eg: `AN_ACCESS_KEY = credentials('my-predefined-secret-text')` 
      - options - directive allows configuring Pipeline-specific options from within the Pipeline itself.
        - eg: `timestamper`, `buildDiscarder(logRotator(numToKeepStr: '1')) }` etc.
      - paramaters - directive provides a list of parameters that a user should provide when triggering the Pipeline
        - eg: `parameters { string(name: 'DEPLOY_ENV', defaultValue: 'staging', description: '') }`
      - [flow control](https://www.jenkins.io/doc/book/pipeline/syntax/#flow-control)
  - Steps
     Fundamentally, steps tell Jenkins what to do and serve as the basic building 
     block for both Declarative and Scripted Pipeline syntax.
  - Steps Provided by Plugins - Each plugin link offers more information about the parameters for each step.
    - eg: Adds support for standard ANSI escape sequences, including color, to Console Output.
      - `wrap([$class: 'AnsiColorBuildWrapper'])`
    - eg: Allows to configure various aspects of the JaCoCo code coverage report.
        ```groovy
            stage('Build') {
              steps {
                  sh './jenkins_build.sh'
                  junit '*/build/test-results/*.xml'
                  step( [ $class: 'JacocoPublisher' ] )
              }
            }
        ```

### Agents

In a Jenkins cluster, a `node` is a machine ready to take any build workload
The global agent section must be defined in a `pipeline` block at top level or it can ve defined at each `stage('...')`
- `agent any`  - Stage can execute on any node
- `agent none` - Do not define an agent
  - Use for global `agent` to allow you to specify particular nodes for each `stage`
  - Use for a stage that must execute without an agent
  - Do noy use agent none for steps that execute a shell command or do other activities that require an agent
- `agent { label '' }` - agent specified by a specific label aka - `agent { label 'jenkins-ecs-rails' }`
- `agent { node }` - like agent label but allows more specifications

#### Docker agent

Docker is a tool that can package an application and its dependencies in a virtual 
container that can run on any Linux server.
A Docker container is self sufficient running on an isolated process.
Docker containers can be used as the agent to provide a build environment
- `agent { docker }` - allows container to to dynamically provision a docker container as a Jenkins agent node, 
let that run a single build, then tear-down that node, without the build process (or Jenkins job definition) 
requiring any awareness of docker.
  - ```groovy
    agent {
        docker {
            image 'myregistry.com/node'
            label 'my-defined-label'
            registryUrl 'https://myregistry.com/'
            registryCredentialsId 'myPredefinedCredentialsInJenkins'
        }
    }
    ```

- `agent { dockerfile }` - Execute the Pipeline, or stage, with a container built from a Dockerfile contained in the source repository.
  - ```groovy
        agent {
        dockerfile {
            filename 'Dockerfile.build'
            dir 'build'
            label 'my-defined-label'
            additionalBuildArgs  '--build-arg version=1.0.2'
            args '-v /tmp:/tmp'
        }
    }
    ```
When you specify a docker container for an agent, Jenkins calls APIs directly these command are serialized so they can
resume after master jenkins restart from `/restart`. eg. yet another docker plugin with `docker-jenkins-rails` agents.

When you use a shell step to run a docker command directly the step is bound to the durable task of the shell
  - The docker container and any tasks running in the container are terminated when the shell terminates
  - Run all pipeline stages on a contanier based image `bzzzcentos:7`:
    - ```groovy
          pipeline {
            agent { docker 'bzzzcentos:7' }
          }
      ```
#### Per stage agent syntax

```groovy
   pipeline {
      agent none
      stages {
        stage('Build'){
          agent { label 'buzzzmaven' }
          steps {
              sh 'echo my build'
              sh './jenkins_build.sh'
          } //steps 
        } // Build

        stage('Deploy') {
          agent { label 'buzzprod' }
          steps {
            sh 'echo buzz prod deploy'
            sh './jenkins/deploy.sh'
          }
        }
      }
   }
```

- Do not run stages on an agent by default
- Run build stage on node machine tagged `buzzmaven`
- Run deploy stage on node machine tagged `buzzprod`

#### Specify agents for pipeline

Change global agent to `none` and use `jdk7` & `jdk8` for build and test steps
- Select `Settings` from **Buzz Build** stage and **Buzz Test** stages with node label `java7`
- Add `java8` Build steps as is with `java7` on **Buzz Build** specify java8 node based environment
  variable for `${BUZZ_NAME}`
 
##### Note: 
- recursively: constituting a procedure that can repeat itself
- recursion: occurs when a thing is defined in terms of itself or of its type. 
  The most common application of recursion is in mathematics and computer science, 
  where a function being defined is applied within its own definition.
- ambiguity: the quality of being open to more than one interpretation; inexactness. 
- idle: without purpose or effect

### Further Reading and References
1. [Decentralied ci-cd vs centralized](https://medium.com/@oprearocks/centralized-vs-decentralized-ci-cd-strategies-for-multiple-teams-dd1ba792c1ac)
1. [Sections, Directives, Options, Matrix](https://www.jenkins.io/doc/book/pipeline/syntax/)
1. [Guide to deploying artifacts with jenkins](https://codurance.com/training/2014/10/03/guide-to-deploying-artifacts-with-jenkins/)
