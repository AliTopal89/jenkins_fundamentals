### Jenkins fundementals

Jenkins is an open source automation server

Jenkins is the CD Orchestrator
 - Commit -> Build -> Test -> Stage -> Deploy -> Run

Cloudbees and the Jenkins community
 - Organizes Jenkins World and supports numerous Jenkins events

Continuous Integration(CI)
  - is the frequent, automatic integration of code, meaning, automatically tests all new and modified code with the master code

Continuous Delivery(CD)
 - ensures that the code is always ready to be deployed

Continuous Deployment
  - automatically deploys all the validated changes to production

#### Source Code Management (SCM)
  - is used to track modifications to a source code repository. SCM tracks a running history of changes to a code base and helps resolve conflicts when merging updates from multiple contributors. 

#### Types of testing 
  - Unit tests - test a small piece of code 
   - a function or method or command
  - Integration tests - Validate integration between multiple sub-systems
   - including external sub-systems like a database
  - Smoke tests - validates basic functions of the system
  - Functional tests - validate the normal software behavior against the expectations and requirements
  - Non-regression tests - validate that the system still produces the same end result
  - Acceptance tests - test the full product from the perspective of an end user cases

### Installation requirements
  - Change JVM memory Heap Size: `-Xms1G -Xmx2G`
    - ```java
         java -Xmx64m -classpath ".:${THE_CLASSPATH}" ${PROGRAM_NAME} 
      ```
  - G1 garbage collector for heap > 4GB: `-XX:+UseG1GC`
    - `Garbage First garbage collector (G1 GC)`
    - `-XX:+UseG1GC - Tells the JVM to use the G1 Garbage collector.`
    - For example:
    ```java
    java -Xmx50m -Xms50m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar c:\javademos\demo\jfc\Java2D\Java2demo.jar
    ```
    
### Installing Jenkins and Running Jenkins

- Settings are in `/etc/default/jenkins` or in `/etc/sysconfig/jenkins`
- `$JENKINS_HOME` defaults to `/var/lib/jenkins`

Web Application Archive (WAR) distribution of jenkins can be either 
  - run as a stand-alone application 
  - servlet container

#### WAR as Standalone

```java
java ${JAVA_OPTS} -jar jenkins.war ${JENKINS_OPTS}
// JENKINS_OPTS - httpPort, prefix, logfile etc.
```
For example:

```java
java -jar jenkins.war --httpPort=8081 --prefix=/ci httpListenAddress=127.0.0.1
```  

JENKINS_HOME directory structure
  - config.xml - General configuration file
  - jobs/ directory - Build job workspaces, hisory and artifacts.
  - plugins/ directory - installed plugins
  - users/ directory - local user databases

**d. Artifacts are files which are associated with a single build. A jenkins build can have any number of artifacts associated with it**

### Install Jenkins updates

  - Download from "Manage Jenkins"
  - Use "Prepare Shutdown" screen under "Manage Jenkins"
  - Replace **jenkins.war** file usually on Linux this located in `/usr/share/jenkins`
  - Restart jenkins and apply the upgrade 

### Manage Jenkins

- **Configure System** 
  - most of the fundamental tools that Jenkins uses. Plugins may add sections to this page

- **Number of executors**
  - Unit of task execution on a computer
  - Set this to `0` to prevent builds from running on master

- **Quiet Period**
  - When this option is non-zero, newly triggered builds of this project will be added to the queue, but Jenkins will wait for the specified period of time (in seconds) before actually starting the build.

  - For example, if your builds take a long time to execute, you may want to prevent multiple source control commits that are made at approximately the same time from triggering multiple builds. Enabling the quiet period would prevent a build from being started as soon as Jenkins discovers the first commit;

- **SCM and AUthentication**
  - Git, SVN, Mercurial, Team Foundation Server... all use credentials plugin

- **Timestamper**
  - Systemtime format
    - `'<b>'HH:mm:ss'</b> '`
    - The system clock time format defines how the timestamps will be rendered. Default is like above
  - Endtime Format
    - The elapsed time format defines how the timestamps will be rendered when the elapsed time option has been selected.

#### Global Tools Configuration

For Example:

- **Install JDK**
  - Name	openjdk8
 	  JAVA_HOME	 /usr/lib/jvm/java-8-openjdk-amd64
  - AUtomatic install asks for `Install Oracle Java SE Development Kit from the website`
    - Select Java SE Development Kit installation 

- **Reload config from Disk**
  - Use this when you modify Jenkins or its environment outside of the UI

### Security

- **Configure Global Security**
  - Define how users are authenticated and what they are authorized to do.

- **Manage Users Screen**
  - is used to add user to the Jenkins user database and lists all users who are in the database

- **Configure Credentials**
  - Jenkins credentials control access to third party sites and applications such as artifact repos and cloud based storage

- **Script Console**
  - Allows you to execute arbitrary Groovy scripts on the server.
  - Apache Groovy is the foundation of the DSL used for Jenkins Pipelines
  - ```groovy
       Jenkins.instance.queue.clear()
    ```
- **Manage Nodes**
  - Recap of NODES, AGENTS & EXECUTORS:
    - A `node` (e.g. jdk8-node) is a server where Jenkins runs jobs on `executors`
    - The `agent` is the tool that manages the `executors` on a remote `node` on behalf of Jenkins.
    - The Jenkins master also runs on a `node` 

    - Click the flywheel to the right of the node to configure the items Jenkins monitors for all nodes it runs. 
    - This is where you can also define the number of executors and other characteristics of the node. 

- **Monitoring Node Usage**
  - Load Statistics:
    - page to monitor node utilization
  - Number of online executors
  - Number of busy executors
  - Number of available executors
  - Queue length

  - *Note: The number of busy executors and the number of available executors* 
  *need not necessarily be equal to the number of online executors as executors* 
  *can be suspended from accepting builds and thus be neither busy nor available.*

- **About Jenkins**
  - Release and version of Jenkins running
  - List of all third party libraries
  - List of installed plugins

- **System Information**
  - provides detailed information with what is available on this Jenkins instance, for example"
    - `java.version	1.8.0_212` - system
    - `JAVA_HOME	/usr/lib/jvm/java-1.8-openjdk` -env 
    - `gradle	1.36` - plugin

- **Jenkins CLI**
  - provides commands that perform functions that are usually executed using the GUI
    - Available commands, for example:
      - `java -jar jenkins-cli.jar -s http://localhost:5000/jenkins/ -webSocket help`

- **Service Lifecycle**
  - Managing options used to `/start/stop/reload`

### Install Maven lab notes learned the hard way

- install the jenkins.war file from previos lectures which looks like an example but it does need it seems like
- Manage Jenkins -> Global Configuration and set JDK Installation with 
  - `NAME` `ORACLE JDK8`
  - `NAME` `ORACLE JDK7`
  - Install automatically
  - Git - `Path to git executable` `/usr/bin/git`
  - Maven lab practive
    - `NAME` `mvn3`, `Version 3.5.4`, Install automatically
    - `NAME` `mvn2`, `Version 2.2.1`, Install automatically
  - APPLY & SAVE

Notes: You may have to install jenkins.war, from jenkins upgrades lecture.
I didn't install the jenkins.war file from that lecture at first, once I installed that `java -jar ~/Downloads/jenkins.war httpListenAddress=http://localhost:5000/jenkins/`
I was able to do maven project and free-style job works just fine. 


### Jenkins Plugins

What are Plugins?
- A plugin is JAR file with some special conventions. Jenkins uses plugins to provide much of its functionality.
This **"modular"** architecture means that your Jenkins installation has the features and functionality you need and not get
bloated with functionality you don't need.

- Contained in a fule with an hpi or jpi extension

- Stored in `${JENKINS_HOME}/plugins` unless you use `--pluginroot` to change the binaries of the plugin
- Plugins are versioned artifacts that can be upgraded

- Installation Wizard will suggest plugins that provide commonly-used functionality.

#### Manage Plugins

- Installed Tab:
  - Enabled - Check mark indicates that the plugin is enabled
    - Greyed out check marks indicate plugins that are required by other plugins 
    *(Always thought they were plugins installed/updated by Jenkins by default)*
- Uninstall a plugin:
  - Removes the plugin binary(hpi or jpi extension) from the disk
  - Does not remove the configuration that the plugin may have created 
    - If the plugin contributed extensions to any job/build/views/agents etc, Jenkins reports unrecognized
    fragments in config files
    - Until you remove the config file (config.xml) you can re-install the plugin and restart 
    jenkins to restore the configurations.
- Disable a plugin:
  - softer way to retire a plugin, usually binary is changed to jpi/hpi.disabled
- Update plugins:
  - Always read the Changelog information of new plugins before installing it.
  *(Yet Another Docker definitelly killed us at one point)*

*Click advanced on plugin manager if you are running under HTTP Proxy.*

- Plugins can be installed manually without "Manage Plugins"
  - The documentation for each plugin lists all dependencies, you would 
  have to manually install optional dependencies though
    - For Example click `Branch API` plugin, go to Dependencies tab 
      - ```
        Required
        Folders ≥ 6.14
        SCM API ≥ 2.6.3
        *Implied*
        Trilead API ≥ 1.0.4
        ```
  - Some plugins requie

Note on Implied plugins:
  - ```
    Plugins that depend on a Jenkins core version before such a plugin was detached 
    from core may or may not actually use any of its features. To ensure that 
    plugins don't break whenever functionality they depend on is detached from Jenkins core, 
    it is considered to have a dependency on the detached plugin if it declares a dependency 
    on a version of Jenkins core before the split. Since that dependency to the detached plugin 
    is not explicitly specified, it is implied.
    ```
#### Manual Plugin installation

Chuck Norris Plugin
- Go to `http://localhost:5000/chucknorris.hpi` 
- `Manage Plugin` -> `Advanced` -> `Upload Plugin`
- Upload the hpi file, then restart jenkins fom Update Center

Ways to install PLugins:
  - https://github.com/jenkinsci/docker#preinstalling-plugins -jenkins docker image 
  provides a script that fetches the plugins

  - https://github.com/jenkinsci/custom-war-packager - which allows you to package your
  own plugins. Custom WAR Packager (CWP) allows building ready-to-fly Jenkins packages using 
  a YAML specification. The tool can produce Docker images, WAR files, and Jenkinsfile Runner 
  docker images (aka single-shot Jenkins masters).

### Configure Notifications
- Notifications for CI/CD can be delivered with email, Slack and the most unfortunate TEAMS
- Built in Email Notifications
  - Main configuration is about SMTP(sending) server
  - Every failed build
  - Every successful build after a failed
  - Every unstable build after a successful 
    - triggers a new email
- Slack Notification:
  - Notifications can be configured to be sent for any build status
    - Start, Aborted, Failure, Unstable
    - Custom content like Commit list can be added etc.

### Distributed Build Architecture

Distrubuted builds run on nodes instead of master node. 
  - The Jenkins master is webserver that also actis as a brain for deciding how/when/where to run tasks
  - A `node` is a server whre Jenkins runs jobs for `executors`.
  - The `agent` is the tool that manages executors on remote node.
  - Jenkins Master
    - Files written when a pipeline executes, are written to the filesystem on the master 
      unless they are off-loaded to an artifact repo such as nexus.
    - Some administrative tasks (such as backups) are run on master 
    node (in our case at work, it is mounted to EFS from the agent).
    - A `node` is taken offline if Disk Space, free swap etc. go outside the configured threshold.
    - Distributed Builds:
      - Master - serves the http request and stores all important information `/var/lib/jenkins/workspace/...`
      - Advantages of distributed builds - `${JENKINS_HOME}` is proected, and makes the instance more reliable
      - 
    - Distributed Build agents:
      - Uses `slave.jar` file
      - on your browser at `jnlpJars/slave.jar` under `$JENKINS_URL`

#### Master agent connectors

- Agent to master control enabled by default
- An agent can be launced by ssh from master, agent managed as windwos service, 
  JNLP(Java web start), or a custom script from master
- Communication between Jenkins and Unix agents can use SSH
- SSHD only need to be installed on agents
  - Agents JAR file is managed by Jenkins master
  - masters SSH public key must be in remote node's authorized keys

##### Windows Service
To connect Jenkins to Windows agent, DCOM is an option *Although cloudbees suggest not to, info [here](https://support.cloudbees.com/hc/en-us/articles/203773700-Why-not-use-DCOM)*
  - Just need an admin username and password.

##### JNLP
- launced as a single command with `slave.jar` file
- TCP or HTTP tunneling socket connection is made
- TCP port must be defined on Global security
  - *TCP is connection-oriented, and a connection between client and* 
    *server is established before data can be sent. The server must be listening* 
    *(passive open) for connection requests from clients before a connection is established.*
- Can also be run "headlessly" (is software capable of working on a device without a graphical user interface) 
  via jar instead of Java Web Start with Gui

##### Custom Script
- Fetch `slave.jar` & Establish connection by launching remote agent.

#### Benefits of master agent node
- Scalability - when build demand increases scale up merely pointing Jenkins at another agent node,
- Security 
  - Job that run on master have full permissions to jenkins resources on master, potentially allowing
    malicious user to access private information
  - Use Cloud agents, that are created for each build then destroyed (YAD Plugin for example)

#### Installed tool problems

JDK versions, Maven, Gradle etc are installed causing too many versioning problems?

Dedicated Agents:

  - Specialized hardware/ operating systems
  - Specialized Software
  - User the `Restrict where this project can be run` option from project configuration

Fungible Agents:

  - Fungibility is the property of a good or commodity whose individual units
    are capable of mutual substitution
  - Configuring a machine to act as an agent inside your infrastructure can be tedious 
  and time consuming. This is especially true when the same set-up has to be replicated on 
  a large pool of agents. Because of this, is ideal to have `fungible` agents, which are agents 
  that are easily replaceable. Agents should be generic for all builds rather customized for a 
  specific job or a set of jobs. The more generic the agents, the more easily they are 
  interchanged, which in turn allows for a better use of resources and a reduced impact 
  on productivity if some agents suffer an outage

Cloud agents:
  - Agent in a cloud like Amazon EC2, MS Azure
  - Easier to do with container tech like Docker
    - Start a Cloud-Based agent
    - Run the build inside the agent
    - Then tear it down

#### Monitoring nodes

- NodeMonitor in Jenkins
- Runs healthcheck on CPU Memory Usage, Disk Space etc.

### Distributed Jenkins Build (LAB)

- ```bash
  cloudbees-devbox $ ssh -i /ssh-keys/vagrant_insecure_key -p22 \
  jenkins@jdk8-ssh-agent \
  echo "-- Hello from jdk8-ssh-agent"
  ```

- Copy the content of `/ssh-keys/vagrant_insecure_key` somewhere 

- Manage Jenkins -> Manage Nodes -> New Node
  - #2 of executors
  - Usage - `Use this node as much as possible`
    - *Use this node as much as possible: This is the default setting.*
      *In this mode, Jenkins uses this node freely. Whenever there is a*
      *build that can be done by using this node, Jenkins will use it.*
  - Host `jdk8-ssh-agent`
  - Credentials - `Jenkins (SSH Key for agent)` 
  - Click Advanced - Port 22
  - Host Key Verification Strategy: `Non Verifying Verification Strategy`
    - *This is the legacy behavior. It does not perform Host Key Verification and is considered unsafe.*
    - More [strategies](https://support.cloudbees.com/hc/en-us/articles/115000073552-Host-Key-Verification-for-SSH-Agents#howtosetuphostkeyverificationforjenkins)
  - Name - JAVA_HOME
  - Value - `/var/lib/jvm/java-1.8-openjdk`
  - Save
  - Agent may be offline for a few, click on Logs from left pannel to see the progress

#### Reconfiguring Master agent.

- Configure System
- numer of executors #0
- Usage - Only build jobs with label restrictions matching this node. 
- Configure Global Security
  - Enable Agent -> Master Access Control

### SECURITY

- Know the system
- Least privilede  - do not give permissions to everyone to do everything
  Manage AAA concepts:
    - Authentication - User acess
    - Authorization - User priviledges
    - Accounting - Monitor your system
- Defense in Depth - put scurity on layered systems
- Prevention is Good, Detection is better - monitor jenkins installation, detective mode!

#### Agents and Security

By Default, the pipeline  executes with full authority.
All of the pipeline logic, groovy conditional and loops executes on master
Creates `workspace` for each build that runs.
Pipeline calls steps that run on agents - Each of which are combination of scripts or commands.
  - Agent writes some files to nodes, sends data back to master, request info from master
  - Agents on master jenkins maybe able to access Jenkins configuration and workspace of other builds
  - An agent can write malicious code to local disk so the node can get tainted

For maximum security, run all builds on ephermal agents in the cloud 
that get destroyed after the build is finished.

Pipelines may need access to external resources such as Brakeman, Nexus, Elasticsearch DB
  - Store the usernames and password in Credentials instead of directly on pipeline

##### Authentication

- Security Realm tell jenkins which referential to use for authentication, which is a dedicated
database for user and passwords
  - Defines the security implementation used to establish the identity of users
  - Only one security realm can be active at a time
  - By default, users and groups come from jenkins internal db.
- 4 kinds of Realm
  - Jenkins User Database
  - Unix user/group Database
  - Server Container
  - External LDAP
    - *LDAP provides the communication language that applications use to communicate* 
    *with other directory services servers. Directory services store the users,*
    *passwords, and computer accounts, and share that information with other entities on the network.*

Eg.
Configure Global Security -> Realm: Jenkins own user database -> 
  Manage Users -> Create User -> with Username Password and email
  - Moodify User with Flywheel to give description of user, 
    ssh public keys, timezone, modify username & password 

##### AUthorization

- Grants access rights to:
  - `Resource`: Task, Object/Action to manipulate
  - `Role`: Set of privileges grouped by comodity
  - `Requester`: User or group with role that want to manipulate resources

- Use security matrix to populate authorization
  - `Matrix based security`: assign global privileges to users/groups
  - `Project based matrix authorization strategy`: assign privileges based on specific projec
  - `Role Staretgy`: assign permission to specific roles

- Configure authorization

  - `Logged in users can do anything`: In this mode, every logged-in user gets 
  full control of Jenkins. The only user who won't have full control is 
  anonymous user, who only gets read access.

  - `Matrix-based security`: Permissions are additive. 
  That is, if an user X is in group A, B, and C, then the permissions that this 
  user actually has are the union of all permissions given to X, A, B, C, and anonymous.
    - `anonymous`: permission granted to all *unauthenticated* users who access Jenkins env
    - `authenticated` permissions granted to all *authenticated* users who access jenkins env
    - Permissions Groups: 
      - `Overall`
        - Administer - Make system wide configuration changes
        - Read - View almost all pages within Jenkins env
        - RunScripts - Run groovy scripts via the groovy console or groovy cli command.
          - If the script attempts and unapproved operation, it is killed
          - Unapproved operation is added to approval queue which an administrator can review
            and add it to the whitelist. 
        - UploadPlugins - Upload arbitrary plugins.
        - ConfigureUpdateCenter - Configure update sites and proxy settings.
      - `Credentials`
      - `Agent`
      - `Job`
      - `Run`
      - `View`
      - `SCM`
      - `Metrics`
      - `Lockable Resources`
  - `Project Based Matrix Authorization`: Similiar to Matrix based but define authorization by project,
  rather than user.
     - This authorization scheme is an extension to *Matrix-based security* which 
     allows additional access control lists (ACLs) to be defined for each project 
     separately in the Project configuration screen. This allows granting specific 
     users or groups access only to specified projects, instead of all projects in the Jenkins environment.

##### Accounting

- Occurs in the context of a user who is authenticated
- Mesures resources used or cosumed by users during access.
- all logging output to `stdout`

Logs on Linux:
- Default logs are available in `/var/log/jenkins/jenkins.log` can be customized though:
  - `/etc/default/jenkins` - for `*.deb`
  - `/etc/sysconfig/jenkins` - for `*.rpm`

Logs on Windows
  - `%JENKINS_HOME%/jenkins.out`
  - `%JENKINS_HOME%/jenkins.err`
  - location can be be customized in `%JENKINS_HOME%/jenkins.xml`

Logs on Docker, if jenkins inside Docker detached container
  - `docker logs <contanierID>`

Logs on Jenkins GUI
  - Manage Jenkins -> System Log

Monitor Server Load
  - Manage Jenkins -> Load Statistic
    - Displays a graph of server load time for the master node
    - Graph Keeps track of 
      - **Number of online executors**
        `For a computer`: if the computer is online then this is the number of executors 
        that the computer has; if the computer is offline then this is zero.
        `For a label`: this is the sum of all executors across all online computers in this label.
        `For the entire Jenkins`: this is the sum of all executors across all online 
        computers in this Jenkins installation.
        Other than configuration changes, this value can also change when agents go offline.
      - **Number of busy executors**
        This line tracks the number of executors (among the executors counted above) 
        that are carrying out builds. The ratio of this to the number of online 
        executors gives you the resource utilization. If all your executors are busy 
        for a prolonged period of time, consider adding more computers to your Jenkins cluster.
      - **Number of available executors**
        This line tracks the number of executors (among the online executors counted above) 
        that are available to carry out builds. The ratio of this to the total number of executors 
        gives you the resource availability. If none of your executors are available for a prolonged 
        period of time, consider adding more computers to your Jenkins cluster.
      - **Queue length**
        This is the number of jobs that are in the build queue, waiting for an available 
        executor (of this computer, of this label, or in this Jenkins, respectively). This doesn't 
        include jobs that are in the quiet period, nor does it include jobs that are in the queue 
        because earlier builds are still in progress. If this line ever goes above 0, that means 
        your Jenkins will run more builds by adding more computers.

Monitoring
  - **Monitoring**
    - The Monitoring plugin provides monitoring of Jenkins with JavaMelody
    - Charts of CPU, memory, system load average, HTTP response time
    - Details of HTTP sessions, errors and logs, actions for GC, heap dump, invalid session(s).

  - **Disk Usage**
    - The Disk Usage plugin shows project-wide details for all jobs and all workspaces
    - It also displays Disk Usage Trend.

  - **Build Monitor**
    - The Build Monitor Plugin provides a detailed view of the status of selected Jenkins jobs
    - It also shows the names of people who might be responsible for "breaking the build".

Auditing
  - **Audit Trail**
    - Keeps a log of users who performed particular Jenkins operations, such as configuring jobs
    - This plugin adds an Audit Trail section in the main Jenkins configuration page
    - Many configuration options are supported
      - Save output audit logs in rolling files
      - Send audit logs to a Syslog server
      - Output audit logs in `stdout` or `stderr`. Primarily intended for debugging purposes
  - **Job Config History**
    - Saves a copy of the configuration file of a job (config.xml) for each change made and of 
    the system configuration
    - Provides an overview page of all changes
    - The overview page only lists changes made to the system configuration (for performance reasons)
      - Use links to view either all job configuration histories or just the deleted jobs 
        or all kinds of configuration history entries together.

### Global Security Settings

Global Security Settings close off intrusion paths to Jenkins instance
**Access Control for Builds**
  - Similar to access control for users, builds in Jenkins run with an 
  associated user authorization. By default, builds run as the internal 
  SYSTEM user that has full permissions to run on any node, create or delete jobs, 
  start and cancel other builds, etc.
  
  In a Jenkins setup with fine-grained permissions control, this is undesirable. 
  For example, having builds run as SYSTEM could allow users with access to configure 
  and start one job to start builds of any other jobs using Pipeline Build Step Plugin.
  
  The solution to this is to configure access control for builds. The most notable 
  plugin in this space is Authorize Project Plugin, which allows flexible configuration 
  of global and per-project build authorization
  - Project default build authorization:
    - Specify the authorization to use for projects.
  - Per-project configurable build authorization
    - Allows the authentication that a project will run as to be configured 
      from the project configuration page.
      - **Run as specific user**
        - Run a build as a specified user. You are required to "one" of following 
        condition to successfully save the configuration.
          - You are an administrator.
          - You yourself are the specified user.
          - The specified user is not changed from the last configuration,
          and "No need for re-authentication" was checked in the last configuration.
          - You enter the password for the specified user.
      - **Run as user who triggred the build**
        - Run a build as a user who triggered it. If the build was triggered 
        as a downstream build, the build runs as a user who triggered the upstream build.
        This does not work when the build is triggered by SCM polling or scheduled triggering. 
        The build runs as SYSTEM authorization in those cases.
      - **Run as anonymous user**
        - Run a build as an anonymous user.
      - **Run as system**
        - Run as SYSTEM user

**CSRF (Cross Site Request Forgery)**

- is an exploit that enables an unauthorized third party to perform requests
  against a web application by impersonating another, authenticated user. 
- when enabled Jenkins checks for a CSRF token or "crumb" with default crumb issuer.
  In practical terms, this means that each request to the Jenkins API needs to have 
  what is known as a crumb defined in the headers.
- Accessing Jenkins through a poorly configured reverse proxy may result in 
  CSRF HTP header being stripped consider [Jenkins behind a NGINX reverse proxy](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-with-jenkins/)

**JNLP (Java Network Launch Protocol)**

- can be used to launch an application on a client desktop by using resources that are hosted
  on a remote web server. (ECS jenkins rails instance config)
- Use "agents" field to configure JNLP
  - If you are not using inbound agents, it's recommended that you disable this entirely, 
    which is the default installation behavior. Jenkins uses a TCP port to communicate 
    with agents connected inbound. If you're going to use inbound agents, you can allow 
    the system to randomly select a port at launch 
    (this avoids interfering with other programs, including other Jenkins instances). 
    As it's hard for firewalls to secure a random port, you can instead specify 
    a fixed port number and configure your firewall accordingly.

**Agent-to-Master Access Control**

- filters the commands that agents can send to the Jenkins master
- recommended to keep this protection enabled, especially 
  because of agents can be taken from other teams.
- if builds are failing 
  - upgrade latest version of all plugins
  - whitelist specific commands that you need
  - Add `allow/deny` rules to refine list of files on master, ant type of access on agent

**Markup Formatng**

- Jenkins allows some user input
- HTML formatting could inadvertently inserting unsafe HTML/JS.
  - Could be used for XSS (Cross-site scripting) attacks
- Use Markup Formatter to control how HTML is rendered
- You can choose Safe HTML to allow subset of HTML markup
- Plain Text option:
  - Treats all input as plain text. HTML unsafe characters 
  like `<` and `&` are escaped to their respective character entities.

**Content Security Policy**

- is applied to static files on Jenkins
- This header is set to a very restrictive default set of permissions 
to protect Jenkins users from malicious HTML/JS files in workspaces, 
`/userContent`, or archived artifacts.

- The CSP header sent by Jenkins can be modified by setting 
the system property `hudson.model.DirectoryBrowserSupport.CSP:`

- For example:

```groovy
  System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "default-src 'self'; style-src 'self' 'unsafe-inline';")
 ```

### Managing Credentials

- Credentials are used to get trusted access to other resources without having
-  Jenkins users credentials to get trusted access to for example
  - SCM services for getting and pushing code, i.e GITHUB_CREDENTIALS
  - Remote Secured Sercies i.e LDAP pluings
  - Artifact Servers i.e Nexus
  - Webhooks i.e Slack, TEAMS
  - AWS credentials i.e. Serverless aws_key_id, aws_secret_key
- Using Credentials provides and API consumable by plugins and jenkins resources
- Authorization matrix provides create, delete, update view credentials
- Each plugin defines the credential types it supports and may not support all credential types
  - For example git plugin supports username, password and private key but 
    not certificates or secret text.
- Instead of sharing this username and password, it is passed in to credential
- Pipelines then can use `environment` or `withCredentials` directive to supply the username and password.
- All credentials are stored as `type Secret` in an encrypted form on Jenkins Master
- Credentials are encrypted using a key from master key, keys are tied to and encrypted
  by the instance ID, meaning they cannot be migrated or copied to a new jenkins instance
- Keys to decrypt secrets are stored `$JENKINS_HOME/secrets/` directory
- Credentials can be added by authorized users from `Credentials -> Create`
  - Assign an ID that can be used to the access the credential, if you don't Jenkins assigns a GUID
  - Scope defines whre this credential can be used
    - **System**: 
      - This credential is only available to the object on which the credential is associated. 
      Typically you would use system-scoped credentials for things like email auth, 
      agent connection, etc, i.e. where the Jenkins instance itself is using the credential. 
      Unlike the global scope, this significantly restricts where the credential can be used, 
      thereby providing a higher degree of confidentiality to the credential.
    - **Global**:
    - This credential is available to the object on which the credential is associated 
      and all objects that are children of that object. Typically you would use global-scoped 
      credentials for things that are needed by jobs.
- Credentials Type/Kind:
  - **Secret Text**:
    - MYVARNAME contains the path of the file with secret text, i.e NFS_SERVER_IP, TEAMS_WEBHOOK_URL
  - **Standard username and password**:
    - MYVARNAME is set to `<username>:<password>` or `MYVARNAME_USR` & `MYVARNAME_PSW`
- Each `credential` type supported for the `environment` directive, must be supported in the credential
  binding plugin and have a special handler in Declarative Pipeline

- ```groovy
  steps {
                 withCredentials([string(credentialsId: 'mytoken', variable: 'TOKEN')]) {
                 sh 'env | grep TOKEN'
                 sh 'echo ${TOKEN} > secret-file.txt'
                 }
  ```
  - `withCredentials` binds that to a local Pipeline variable called `TOKEN`
  - The single-quotes will cause the secret to be expanded by the shell as an environment variable. 
    The double-quotes are potentially less secure as the secret is interpolated by Groovy, 
    and so typical operating system process listings (as well as Blue Ocean, and the 
    pipeline steps tree in the classic UI) will accidentally disclose it.

### Lab: Enable Jenkins security

OpenLDAP is an open-source implementation of LDAP
Configure Global Security -> Security Realm -> LDAP
  - Server: 
    - Specify the name of the LDAP server host name(s) 
    `(like ldap.sun.com)`. If your LDAP server uses a port other than 
    389 (which is the standard for LDAP), you can also append a 
    port number here, like `ldap.acme.org:1389.`
  - root DN:
    - For authenticating user and determing the roles given to this user, 
      Jenkins performs multiple LDAP queries. Since an LDAP database 
      is conceptually a big tree and the search is performed recursively, 
      in theory if we can start a search starting at a sub-node (as opposed to root), 
      you get a better performance because it narrows down the scope of a search.
      for example: `dc=sun` `dc=com`
  - User search base:
    - One of the searches Jenkins does on LDAP is to locate the user record 
    given the user name. If you specify a relative DN (from the root DN) here, 
    Jenkins will further narrow down searches to the sub-tree.
    for example: `ou=people`
  - User search filter:
    - One of the searches Jenkins does on LDAP is to locate the user record given the user name.
    If your LDAP server doesn't have uid or doesn't use a meaningful uid value, try `"mail={0}"`, 
    which lets people login by their e-mail address.
  - Group Search base:
    - This field determines the query to be run to identify the organizational unit that contains groups.
  - Group Search filter:
    - relative to the Group search base to determine if there is a group with the 
    specified name ( `{0}` is substituted by the name being searched for)
  - Group membeership:
    - Search for LDAP groups containing `member={0}`
  - Display Name LDAP attribute:
    - When a user's details are resolved from LDAP, the specified attribute in those 
    user details will be consulted to retrieve the display name for the user.
  - Email address LDAP attribute:
    - When a user's details are resolved from LDAP, the specified attribute in those 
    user details will be consulted to retrieve the email address for the user unless 
    the email address resolver is disabled.
*Test LDAP setting not working on this version of jenkins so apply and save*

Login and verify username and email address from `.../jenkins/user/<username>/configure`
  - Some fields have been populated using LDAP Data, Updating only changes it inside jenkins 
    database not in LDAP Directory Service.

We want to seperate rights based on group membership so that jenkins can be scaled for the
organizations growth using 
**Project based Matrix Strategy**
  - Non authenticated users (Jenkins system's anonymous user):
    - No access to Jenkins
  - Authenticated users, members of `jenkins-users`(member login `Contributor`) LDAP group:
    - Can view Jenkins dashboard and non-private jobs
  - Authenticated users, members of `jenkins-developers`(member login `Colleague`) LDAP group:
    - Can Launch, Create/View/Update/Delete (CRUD), Configure, Move jobs, Can CRUD Credentials
  - Authenticated users, members of `jenkins-admin`(member login same as default) LDAP group:
    - Can administer Jenkins

Privacy and Credentials:
- [running private admin scripts](http://localhost:5000/gitserver/butler/admin-scripts.git)
- create `admin-script` project and folder name with above 
   git [repo](http://localhost:5000/gitserver/butler/admin-scripts) [at](http://localhost:5000/webide/)
- create new file `run-admin-task.sh`
  - 
  ```bash
  #!/bin/bash

  # Exit immediately if a pipeline retur
  set -e

  # Treat unset variables and parameters other than the special
  # parameters ‘@’ or ‘*’ as an error when 
  # performing parameter expansion
  set -u

  echo "Some secret admin task here"
  ```
- commit and push and login to jenkins as butler and create a freestyle project with name admin-tasks
- select project based security and add `jenkins-admin` user/group permissions
- build: `bash -x run-admin-task.sh`
- SCM - the git repo `http://localhost:5000/gitserver/butler/admin-scripts.git` will give failed to 
connect status 128 error cause you gotta add the credentials
- add credentials with kind username and password, use `butler-gogs-creds` for ID and 
  `Butler's credentials for Gogs` as the description
- run and expect to see `echo "Some secret admin task here"`
- once you login as `colleague` you shouldn't be able to see this admin task

### Organize Builds

Use `Folders` to organize large number of builds around taxonomies (organizes its data into categories and subcategories)
such as projects or departments
Folders can be nested like file system folders

You can clone an existing folder leaving the children not damaged.
 - Clone from `MY_JOB` to `MY_CLONE`. `MY_JOB` stays intact
Folders can define properties like assigning credentials only visible to jobs inside them

You can use Folders with the Role-Based Access Control plugin to enable folder-level 
security roles. By default, the roles are inherited by sub-folders.

### Lab: Organize Folders and Views

Create a TeamA folder with jenkins-admins and jenkins-developers groups
  - This object will not inherit the global security security settings, 
  or any permissions from its ancestors. Only permissions explicitly 
  enabled in properties with `Do not inherit permissions from other ACL's` granted.
Move `pipeline-job` to Team A


New Item -> Call it `Team B` Copy from `Team A` with the same config as Team A.
  - the pipeline-job in `TeamB` folder is greyed out. 
    This is because any job you copy is disabled or rather, not buildable by default.
    You usually don't recognize it because the form opens and you change things and 
    upon saving the job becomes buildable. You can disable the project and enable it 
    back to see the Build Now option on the left hand side menu

On both `Team A` & `Team B` behave as independent jobs, because 
when an existing folder is copied the jobs stay intact.

- `docker exec -it jenkins /bin/bash` to launch bash sheel
- `diff -u TeamA/config.xml TeamB/config.xml`

```xml
--- Team A/config.xml
+++ Team B/config.xml
@@ -1,5 +1,6 @@
 <?xml version='1.1' encoding='UTF-8'?>
 <com.cloudbees.hudson.plugins.folder.Folder plugin="cloudbees-folder@6.11.1">
+  <actions/>
   <description></description>
   <properties>
     <com.cloudbees.hudson.plugins.folder.properties.AuthorizationMatrixProperty>
```
### Monitor Jenkins

Understand how resources are being used before taking actions on it.
Define Build Logs and System Logs

For metrics:
  - You can use box metricks, or build time or agent related plugins

Metric Aggregator is recommended
  - When the metrics-aggregation-plugin is installed, there is a metrics 
  view available on every build, showing the metrics collected 
  from the available sources for this build.

### Backup and Restore

Recover an old configuration and have your backup strategy to include a validatio for each backup

#### Backup Tools
- Filesystem snapshots:
  - faster thanlive backups, supported by Linux Logical Volume Manager(LVM), Solaris ZFS
    - ZFS uses the concept of storage pools to manage physical storage.
      ZFS eliminates volume management altogether. Instead of forcing you 
      to create virtualized volumes, ZFS aggregates devices into a storage pool.
      
      The storage pool describes the physical characteristics of the storage 
      (device layout, data redundancy, and so on) and acts as an arbitrary data 
      store from which file systems can be created. File systems are no longer constrained 
      to individual devices, allowing them to share disk space with all file systems in the pool.
- Plugins:
  - Most are outdated but there is [thinBackup](https://github.com/jenkinsci/thin-backup-plugin) plugin
    - *thinkBackups* can be scheduled and only backs up the most vital configuration info.
- Write a shell script:
  - Use a cron job
  - Create it on `/mnt/backup`, as a seperate filesystem with its own mount point. 
    Or in a subdirectory to `/var/`
  - Create a unique identifier for backups such as timestamp
  - Consider copying the completed backup to a remote backup server or device for long term storage.

#### Backup JENKINS_HOME
- Whole $JENKINS_HOME directory can be backed up. To restore the system, just copy the entire
backup to the new system.

#### Backup Configuration files
- Configuration files are directly stored in $JENKINS_HOME directory and `./config.xml` is the main jenkins
  configuration files and other configuration files also has the `.xml` suffix. 
- Config files can also be stored in and SCM repository (not a bad idea unless you have sensitive data).

#### Backup ./jobs
- `$JENKINS_HOME/jobs` contains informaion related to all jobs you create
- `./builds` contains build records and `./builds/archive` has archived artifacts that maybe too large to backup.
- `./workspace` Contains files from SCM no that important to backup but you can do clean 
  checkout after restoring the system.
    - `git clean -fdx` force cleans the working tree by recursively removing files and 
    removing all untracked files that are not under version control, starting from the current directory.
- `./plugins/*.hpi` & `./plugins/*jpi` plugin packages with specified versions used
- no need to backup `./war` `./cache` , `./plugins/xxx`(subdirectories of plugins), `./tools` 

###### What is an artifact?
> Think of what an artifact really is. The Egyptians created wonderful artifacts such as pottery. 
> But, if you were holding an Egyptian bowl in your hand, you wouldn't refer to it as an "artifact" 
> unless you were discussing the fact that it IS an artifact (fact). You would refer to it as a bowl. 
> They ate out of the bowl. They didn't eat out of the artifact.

#### Backup Validation

- set $JENKINS_HOME to a specific directory and specify a random HTTP port so it doesn't collide with the real instance
  - `export JENKINS_HOME=mnt/backup-test`
  - `java -jar jenkins.war httpPort=9999`

####  Example Restore issue fix
How to fix missing ConfigureClouds:

In case cloud config gets wiped out (current reason unclear as to why agency wide some 
jenkins master instances had config.xml error such as below)

```
Also:   java.nio.file.FileSystemException: $JENKINS_HOME/blah.blah.123 tmp -> /$JENKINS_HOME/config.xml: Function not implemented
```
You can restore to a previous version of the setting by locating the configuration 
from `localhost:over9000/jobConfigHistory` and click on the RAW output section so your 
xml heading for example should start with:

```xml
<?xml version='1.1' encoding='UTF-8'?>
<hudson>
```
Copy the xml output to a file and scp it to master jenkins and update `$JENKINS_HOME/config.xml` 
(or YOLO and directly update $JENKINS_HOME/config.xml) make sure the owner 
is jenkins with `sudo chown jenkins:jenkins config.xml`. Then go to `$JENKINS_HOME/restart` 
check the configuration and run few jobs to test it out

**Somehow Github Pull Requester Builder Plugin creds got wiped out**
For this issues use case it was `org.jenkinsci.plugins.ghprb.GhprbTrigger.xml` 
that needed to be updated, check out the diffs from here and grab the one with correct credentials, 

Copy the content to an xml file and on master jenkins update `$JENKINS_HOME/org.jenkinsci.plugins.ghprb.GhprbTrigger.xml`

### Automate Jenkins

Scripting allows you to automate routine tasks, bulk updates, and provides efficiency and consistency
Jenkins Command Line interface is a Java application provided as jenkins-cli.jar downloadable from jenkins,
which doesn't have to run on Jenkins server

CLI makes an HTTP call to jenkins, discovers the port used for JNLP Agent. CLI attempts TCP/IP connection
if it fails goes back HTTP-based connection. Execution happens on master, `jenkins-cli.jar` gets
downloaded locally

#### CLI Authentication

- with SSH public key registered on jenkins under user account
- `java -jar jenkins-cli.jar -s http://localhost:8080 help` from jenkins cli where `-s` is the url to connect to
- you can check the existing commands from JENKINC CLI page on manage jenkins
- CLI `build` command paramaters:
  - `-c` run polling first and build only if there is a change
  - `-s` do not just schedule, but wait until the build is completed
  - `-p` specify build parameters
  - `-v` report console output as well
- CRUD of jobs
  - reads/writes xml representation of the job
  - can be used to script job creation/updates
- Groovysh:
  - you can use the interactive groovy shell
    - ```java
      java -jar jenkins-cli.jar -s http://${JENKINS_URL} groovysh
      ``` 
    - ```groovy
        groovy:000> import Jenkins.model.Jenkins
        => [import hudson.model.*, import Jenkins.model.Jenkins]
        ...
      ```
    - Useful groovy [scripts](https://github.com/jenkinsci/jenkins-scripts)

#### Using Jenkins API

- Machine to machine communication is required when the service talks to jenkins not the GUI.
- Jenkins API is REST-like API
- No single entrypoint, but per resource API (Pojects API: `http://${JENKINS_URL}/job/api`) URL
- Example JSON API call:
  - ```js
      // example - sending a "File Parameter":
      curl -X POST \ 
        --user USER:PASSWORD \
        --form file0=@PATH_TO_FILE \
        --form json='{"parameter": [{"name":"FILE_LOCATION_AS_SET_IN_JENKINS", "file":"file0"}]}' \
        http:/\/${JENKINS_URL}/job/${JOB_NAME}/build

      // example -sending "String Parameters":
      curl -X POST \
        --data token=$TOKEN \
        --data-urlencode json='{"parameter": [{"name":"id", "value":"123"}, {"name":"verbosity", "value":"high"}]}'
        http:/\/${JENKINS_URL}/job/${JOB_NAME}/build/api \
      ```


###### Note on REST
> **RE**presentational **S**tate **T**ransfer.
> RESTful Web services allow the requesting systems to access and manipulate textual 
> representations of Web resources by using a uniform and predefined set of 
> stateless(no session information is retained by the receiver/server) operations

### Lab: Jenkins CLI

`TCP port for inbound agents` must be correctly configured to ensure that Jenkins CLI will work, using port 50000

- Go to `http://localhost:5000/jenkins/user/<username>/configure` and add new API token
- Cick add new token and generate the token and save it somewhere

- Use the the jenkins private url `http://jenkins:8080/jenkins` instead of the public one, this allows us to reach to
jenkins directly instead of using a reverse-proxy

```
cloudbees-devbox $ curl -LO \
   http://jenkins:8080/jenkins/jnlpJars/jenkins-cli.jar
```

```
cloudbees-devbox $ java -jar ./jenkins-cli.jar -s http://jenkins:8080/jenkins help

ERROR: You must authenticate to access this Jenkins.
Jenkins CLI
Usage: java -jar jenkins-cli.jar [-s URL] command [opts...] args...
```

```
export JENKINS_URL=http://jenkins:8080/jenkins
java -jar jenkins-cli.jar -auth ${USERNAME}:${API_TOKEN} help
```

- kick off a pipeline-job from Team A folder

`cloudbees-devbox $ java -jar jenkins-cli.jar -auth ${USERNAME}:${API_TOKEN} build -s Team\ A/pipeline-job -v`

- to skip a build use `-c`
`cloudbees-devbox $ java -jar jenkins-cli.jar -auth ${USERNAME}:${API_TOKEN} build -c Team\ A/pipeline-job -v`


###### Note on reverse-proxy:

> In  computer networks, a reverse proxy is a type of proxy server 
> that retrieves resources on behalf of a client from one or more servers. 
> These resources are then returned to the client, appearing as 
> if they originated from the server itself.

### Going Further and reference guide
1. [Distributed Builds](https://wiki.jenkins.io/display/jenkins/distributed+builds)
1. [Distributed Builds Architecture](https://www.jenkins.io/doc/book/architecting-for-scale/#distributed-builds-architecture)
1. [So you wanna build the world's largest Jenkins cluster](https://www.cloudbees.com/sites/default/files/2016-jenkins-world-soyouwanttobuildtheworldslargestjenkinscluster_final.pdf)
1. [Jenkins Security - Authorization](https://www.jenkins.io/doc/book/managing/security/#authorization)
1. [Quick and simple security](https://wiki.jenkins.io/display/jenkins/quick+and+simple+security)
1. [LDAP vs. ActiveDirectory](https://www.varonis.com/blog/the-difference-between-active-directory-and-ldap/)
1. [Matrix Based Security](https://wiki.jenkins.io/display/jenkins/matrix-based+security)
1. [Auditing best practices](https://www.cloudbees.com/blog/best-practices-setting-jenkins-auditing-and-compliance)
1. [Oracle Solaris ZFS](https://docs.oracle.com/cd/E23823_01/html/819-5461/zfsover-2.html)
1. [Smart and efficent backup and restores](https://www.cloudbees.com/blog/why-smart-efficient-backup-and-restore-techniques-are-essential-jenkins-production-server)
1. [Groovy scripts](https://github.com/jenkinsci/jenkins-scripts)
