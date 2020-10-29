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
   - Using blue ocean simplifies the pipeline creation even more
   - Use the `script` step to include bits of code when you need capabilites beyond declarative syntax

**Classic web UI** provides tools such as Declarative Directive Generator and Snippet Generator
  - **Declarative Generator**:
    - The Directive Generator allows you to generate the Pipeline code for a Declarative Pipeline directive, such as agent, options, when, and more. Choose the directive you're interested in from the dropdown, and then choose the contents of the directive from the new form. Once you've filled out the form with the choices and values you want for your directive, click Generate Declarative Directive and the Pipeline code will appear in the box below. You can copy that code directly into the pipeline block in your Jenkinsfile, for top-level directives, or into a stage block for stage directives.
  - **Snippet Generator**:
    - will help you learn the Pipeline Script code which can be used to define various steps. Pick a step you are interested in from the list, configure it, click Generate Pipeline Script, and you will see a Pipeline Script statement that would call the step with that configuration. You may copy and paste the whole statement into your script, or pick up just the options you care about. (Most parameters are optional and can be omitted in your script, leaving them at default values.)

**Blue Ocean pipeline editor** generates a Jenkinsfile and stores it in the source code repo. 
  - Allows you to add/remove config stages and steps with a GUI
  - Provides visualization for the pipeline run, becomes handy with parallel steps especially
  - the GUI doesn't support all features such as `build now`, `build with params` classic web UI option
  - Supports apply options to the pipeline, and use the `when` directive. `post` section of your job is supported on blue ocean.

**Benefits**: 
  - Jenkins master can restart and Pipeline continues to run (durable), 
  - can stop for manual approval (pausable),
  - supports typical CD requirements like fork, parallelize, loop, join (versatile),
  - supports custom extensions to its DSL (Extensible),
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
  - `stage` conceptually a distinct subset of the pipeline, used to present pipeline status/progress.
  - `steps` A series of distinct tasks inside a stage. 

### Lab - Create first pipeline
- New Pipeline -> Git -> add ssh [url](http://localhost:5000/gitserver/butler/pipeline-lab)
- [Final First Pipeline script](../pipeline-exercise/first-pipeline-lab.md)

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

In production env, the build chain system (Maven, Gradle etc) publishes artifact to an artifact repo
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
 [Parallel Stages in pipeline](../pipeline-exercise/parallel-stage-lab.md)


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
  - for example: [checkout-scm exercise](../pipeline-exercise/checkout-scm-exercise.md)

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
- Date Time changes per build
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
  - Do not use agent none for steps that execute a shell command or do other activities that require an agent
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
When you specify a docker container for an agent, Jenkins calls APIs directly as these command are serialized so they can
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


### Stash/Unstash

Stashed large files consumes significant resources on both Master and agent nodes
- Good options
  - [External Workspace Manager]('https://github.com/jenkinsci/external-workspace-manager-plugin')
    - There may be cases when you have more than one Node with the same label. Instead of specifying 
    the same External Workspace Node properties for multiple Nodes that share the same label, 
    you can make use of the External Workspace Templates from the Jenkins global config.

    - external repo such as artifactory and nexus
    - Artifact Manager on S3 plugin:  that allow you to store your artifacts into a 
    S3 Bucket on Amazon. The use of this S3 Bucket as a artifact storage is transparent to 
    Jenkins and your jobs,

`stash` and `unstash` are implemented as `steps` within a `stage`
- stash requires `name` parameter, which is a simple identifier for the set of files being stashed.
You can use optional parameter `includes` to store files from another directory which accepts
a set of [Ant-style](https://stackoverflow.com/a/8821223) include patterns.
- Optionally use the `dir` step to create a directory where the files will be written to `unstash`.

- [Stash Unstash Exercise](../pipeline-exercise/stash-exercise.md)

#### Interactive Input

Jenkins provides the ability to pause a pipeline for manual input, like confirmation before deployment
It is implemented asa a step usually within its own stage and should run on `agent none`, use a timeout
parameter to avoid infinite waiting.

```groovy
stage('Confirm Deploy to Staging') {
      steps {
        timeout(time: 40, unit: 'SECONDS') {
          input(message: 'Do you want to deploy to staging?', ok: 'Yes, lets do it!')
        }
      }
    }

```

#### Deploying from Jenkins pipeline

Create a new stage with an `agent` because it cannot be combined with "wait for input" stage
Unstash the **Buzz Java 8** as the first step before deploying to staging

```groovy
stage('Deploy to Staging') {
      agent {
        node {
          label 'java8'
        }

      }
      steps {
        unstash 'Buzz Java 8'
        sh './jenkins/deploy.sh staging'
      }
    }
```

##### Note: 
- recursively: constituting a procedure that can repeat itself
- recursion: occurs when a thing is defined in terms of itself or of its type. 
  The most common application of recursion is in mathematics and computer science, 
  where a function being defined is applied within its own definition.
- ambiguity: the quality of being open to more than one interpretation; inexactness. 
- idle: without purpose or effect

### Lab: Multi Environment Pipeline

[Multi ENV Pipeline Lab](../pipeline-exercise/multi-env-pipeline-lab.md)

### Post Section

Post section is divided into conditions such as `always` `success` `aborted` `failure`.
These condition blocks allow the execution of steps inside each condition depending 
on the completion status of the Pipeline or stage, but an error in post section doesn't make 
the pipeline run unsuccessful.

Particularly useful for archiving artifacts and storing test [results](../pipeline-exercise/archiving-with-stash-post-build.md).

#### Environment Directive
Sequence of key-value pairs that are defined as environment variables specifies for all steps or individual ones
`withEnv` inside a `steps` block can be specified for one or more *but not all* steps within a stage.

Pipeline DSL uses Groovy Syntax for dereferencing single and double quoted variables
 - Single vs Double quote: Most important difference. Single-quoted is ordinary Java-like string. 
 Double-quoted is a GString, and it allows **string-interpolation**. I.e. you can have expressions 
 embedded in it: `println("${40 + 5}")` prints `45`, while `println('${ 40 + 5}')` will produce 
 `${ 40 + 5}`. This expression can be pretty complex, can reference variables or call methods.

global pipeline examples can be seen in `/jenkins-url/pipeline-syntax/globals` such as
`BUILD_TAG` - String of `jenkins-${JOB_NAME}-${BUILD_NUMBER}`. Convenient to put into a 
resource file, a jar file, etc for easier identification.

```groovy
pipeline {
  agent any 
  environment {
      CC = 'king kobold'
  }

  stages {
    stage('Example'){
      environment {
        SUPER_SECRET_KEY = credentials('warlocks-are-cool')
      }
      steps {
        sh 'printenv'
      }
    }
  }
}
```
- The first `environment` block applies to whole pipeline the latter is for only that `stage Example`

#### Notifications

Email & Slack notifications for when a build starts and succeeds or fails.
sample notifications come with build url, job name and build id, but you can
add any global environment variables to notifications for additiona information.

```groovy
post {
  failure {
    slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
      emailext (
        subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
        <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
        recipientProviders: [[$class: 'DevelopersRecipientProvider']]
        )
    }
}
```
#### When Directive

Supports nested conditions(`not`, `allOf`, or `anyOf`) that mut be met for a pipeline to execute a `stage`.
- ```groovy
  when {
    branch 'master'
  }
  ```
  branch - **execute the** `stage` *when* the branch being built matches the pattern, in this case the master branch

- ```groovy
  when {
    environment name: 'DEPLOY_TO', value: 'production'
  }
  ```
  environment - **execute the** `stage` *when* the specified variable is set to given value

- ```groovy
  when {
    expression {
        fileExists('manifests/enjoi.yml')
    }
  }
  ```
  expression - **execute the** `stage` *when* the specified expression evaulates to true

- ```groovy
  when {
    allOf {
      branch 'master'
      environment name: 'DEPLOY_TO', value: 'production'
    }
  }
  ```
  allOf - **execute the** `stage` *when* all nested conditions are true

- ```groovy
  when {
    anyOf {
      branch 'master'
      branch 'staging'
    }
  }
  ```
  anyOf - **execute the** `stage` *when* atleast one nested conidition is true

- ```groovy
  when { not { branch 'master' } }
  ```
  not - **execute the** `stage` *when* the nested condition is false

if the `when` directive has multiple conditions then all the conditions must return true, 
which is similiar to using `allOf` condition

#### Git Enviroment Variables

`GIT_COMMIT`, `GIT_BRANCH`, `GIT_URL` are examples of variables that can be used within the pipeline

for example:
```groovy
stage('Generate Reports') {
  steps {
    sh './jenkins/generate-report.sh'
    sh 'tar -czv target/reports.tar.gz target/reports'
    archiveArtifacts 'target/*.tar.gz'
    echo 'Finished run for commit ${ env.GIT_COMMIT.substring(0,6) }'
  }
}
```

If you want to compare the contents of an archive, there needs to be a unique identifier in the name
of each file like `GIT_COMMIT`, and use bash shell conventions to use just 6 characters of the commit
instead of the whole thing.

```groovy
    sh 'tar -czv target/reports-${GIT_COMMIT:0:6}.tar.gz target/reports'
    echo 'Finished run for commit ${ env.GIT_COMMIT.substring(0,6) }'
```
- ```groovy
    post {
      always {
        mail (
          to: 'clorox@lysol.com',
          subject: "Finish build ${ env.BUILD_ID }, commit ${ env.GIT_BRANCH }, (${ env.GIT_COMMIT.substring(0,6) })",
          body: 'Placeholder'
        )
      }
    }
  ```
  - `subject:` line is in double quotes to surround the value

  - GIT variables available to pipeline to `sh` step in Pipeline:
    
    ```groovy
      sh 'env | grep GIT_'
    ```

#### Credentials

Get trusted access to resources without having to share the actual passwords
are stored in an obfuscated form on the jenkins master

An internal unique ID by which these credentials are identified from jobs and other 
configuration. Normally left blank, in which case an ID will be generated, 

System Credentials - Credentials where Jenkins instance itself is using the credential
  - System credentials are accessible only from Jenkins configuration (e.g., plugins).
Global Credentials = Most credentials are in called in the pipelines 
  - Global credentials are the same as System but are also accessible from Jenkins jobs.

#### Credential Types

- Secret Text:
  - `MYVARNAME` contains the path of the file that contanis the Secret Text
- Username Password combo:
  - `MYVARNAME` is set to `<username>:<password>`
  - `MYVARNAME_USR` is set for username and `MYVARNAME_PSW` is set for password
- Jenkins' declarative Pipeline syntax has the credentials() helper method 
(used within the environment directive) which supports `secret text`, `username and password`, 
as well as `secret file` credentials.
- environment variables created by the `credentials()` method will always be 
appended with `_USR` and `_PSW` (i.e. in the format of an underscore followed by three capital letters).
- For other credential types such as `SSH keys` or `certificates`, 
then use Jenkins' `Snippet Generator` feature.
  - Use `sshagent` step for ssh creds, `withCredentials` does not support it.
- Using the Snippet Generator, you can make multiple credentials available within
a single `withCredentials( …​ ) { …​ }` step. 
- Plugin usage may differ, e.g. git and git client plugin allows username/password and private key credentials
but does not allow secret text, certificates.

```groovy
stage('Deploy Reports') {
  steps {
    ...
    withCredentials(bindings: [string[credentialsId:'my-elastic-key', variable: 'ELASTIC_ACCESS_KEY')]){
        // ENV varaible available in the remote shell
        sh "env | grep ELASTIC_ACCESS_KEY"
        sh "echo ${ELASTIC_ACCESS_KEY} > booyakasha.txt"
    }
  }
}
```

- `my-elastic-key` uses the credential that is defined with that ID
- withCredentials binds that to a local pipeline variable called `ELASTIC_ACCESS_KEY`
- The step uses the `ELASTIC_ACCESS_KEY` local pipeline variable to establish the credentials
  that it needs.
- bindings.string 
  - Sets a variable to the text given in the credentials.
- variable: 
  - Name of an environment variable to be set during the build. 
    The contents of this location are not masked.
- credentialsId
  - Credentials of an appropriate type to be set to the variable.

#### Options and Configurations

Are methods for controlling the characteristics and behaviours of the pipeline
*Options* are set on declarative pipelines *configurations* are set on classic web UI

```groovy
options {
  disableConcurrentBuilds()
}
```
Using declarative directive generator to set options and and options syntax.

another example:
```groovy
options {
  buildDiscarder(logRotator(numToKeepStr: '30')
}
```
[logrotator](https://github.com/jenkinsci/jenkins/blob/master/core/src/main/java/hudson/tasks/LogRotator.java#L111)
`numToKeepStr`: only this number of build logs are kept.

#### Parameters

provides a list of parameters a user should provide when triggering the pipeline. 
the user-specified values with the `params` object.

example params

```groovy
pipeline {
  agent {label 'blah-blyat' }
  parameters {
    string(name: 'DEPLOY_TO', defaultValue: 'staging', description: 'deploy to staging env')
  }
}
```

#### Build Tools for steps

- Make
  - The original UNIX build script
    - ```groovy
         def clean() {
            sh 'make clean'
            deleteDir()
        }
        ...
        post {
            always { script { clean() } }
        }
      ```
- Apache Ant
  - Ant Plugin required

    - ```groovy 
       sh 'ant clean compile jar run'
       ```
- Apache Maven
  - uses an xml file to describe the project, 
  - includes predefined targets for common tasks such compiling and packaging,
  - downloads libraries and plugins for necessary repos and puts them in cache on your local setup
    - ```groovy
       sh 'mvn clean install'
      ```
- Gradle
  - Gradle is an open-source build automation tool that is designed to be flexible enough to build almost any type of software. 
  - requires gradle plugin, uses domain specific language for project configuration,
  - supports very large, multi project builds
    - ```groovy
       sh './gradlew clean test'
      ```
- NPM
  - can install packages, manage dependencies and manage versions of packages
  - require to run Grunt build tool for javascript, bower package manager for the web
    - ```groovy
       stage('Build') {
         steps {
           nodejs(nodeJSInstallationName: 'Node 6.x', configId: '<config-file-provider-id>') {
             sh 'npm config ls'
             sh 'npm build'
             // sh 'npm run build'
           }
         }
       }
      ```

the `tools` section defines the tools to auto-install and put on the `PATH`,
and it is ignored if `agent none` is specified,
`maven, jdk, gradle, nodejs` are supported on the tools section

for example:

```groovy
pipeline {
    agent any
    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
                // the version configured in the tools section will be the first on the path.
            }
        }
        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }
    }
}

```
- the values used to define each tool must be configured on the web UI under `Manage Jenkins` -> `Global Tool Configuration` 
- `install` is one of the phases defined for the "default"
  -  install the package into the local repository, for use as a dependency in other projects locally
- when you run Maven with a phase maven executes in [order](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#a-build-lifecycle-is-made-up-of-phases)
- if you want to archive reports for sucessfull Maven builds use `success` instead of `always` for post builds.


##### Notes:
- dereferencing syntax: the dollar sign `"$"` is the dereference operator, used to translate 
the name of a variable into its contents, and is notably absent when assigning to a variable
- email-extension plugin [vulnerability](https://www.jenkins.io/security/advisory/2020-09-16/#SECURITY-1851) workaround:
  - ```groovy
       System.setProperty("mail.smtp.ssl.checkserveridentity", "true")`
     ```
- Nested conditions: comprise condition statements contained within the definition of other condition statements
- Ant: main known usage of Ant is the build of Java applications. Ant supplies a number of built-in tasks allowing to compile, assemble, test and run Java applications
- Domain Specific Lanugage (DSL): A programming language that has been designed with an application domain in mind. For example, a language specifically designed for modelling chemical processes, or another language designed for investment/trading at the stock exchange.

### Lab: Finished Pipeline
[Pipeline finalized](../pipeline-exercise/finished-pipeline-lab.md)


### Further Reading and References
1. [Decentralied ci-cd vs centralized](https://medium.com/@oprearocks/centralized-vs-decentralized-ci-cd-strategies-for-multiple-teams-dd1ba792c1ac)
1. [Sections, Directives, Options, Matrix](https://www.jenkins.io/doc/book/pipeline/syntax/)
1. [Guide to deploying artifacts with jenkins](https://codurance.com/training/2014/10/03/guide-to-deploying-artifacts-with-jenkins/)
1. [Freestyle Jenkins Project](https://wiki.jenkins.io/display/JENKINS/Building+a+software+project)
1. [centralising jenkins pipeline](https://medium.com/@danielantelo/standardising-and-centralising-your-jenkins-ci-pipeline-e1e097785408)
1. [Maven build cycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)
