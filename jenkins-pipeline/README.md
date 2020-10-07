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

### Further Reading and References
1. [Decentralied ci-cd vs centralized](https://medium.com/@oprearocks/centralized-vs-decentralized-ci-cd-strategies-for-multiple-teams-dd1ba792c1ac)
1. [Sections, Directives, Options, Matrix](https://www.jenkins.io/doc/book/pipeline/syntax/)
