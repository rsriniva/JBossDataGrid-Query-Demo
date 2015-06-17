JBoss Enterprise Application Platform and JBoss Data Grid Demo
========================
Author: Isaac Galvan  
Level: Intermediate  
Technologies: CDI, jQuery, JAX-RS, Infinispan HotRod  
Summary: The `datagrid-demo-app` shows how JBoss DataGrid can store, remove, retrive and query data using the server memory instead of a database. It shows how DataGrid can increase speed and performance of any application.  
Target Product: JBoss DataGrid and JBoss Enterprise Application Platform.

What is it?
-----------

The `datagrid-demo-app` is a deployable Maven 3 project designed to help you get your foot in the door developing high performance apps on Red Hat JBoss DataGrid. 

It demonstrates how to create a compliant Java EE 6 application using JQuery, CDI 1.0 and JAX-RS on JBoss Enterprise Application Platform. It also includes a persistence layer on a remote JBoss DataGrid to introduce you to data storage access in enterprise Java. 


System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss DataGrid 6.4 or later and JBoss Enterprise Application Platform 6.4 or later. 

All you need to build this project is Java 7.0 (Java SDK 1.7) or later and Maven 3.0 or later.

Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP_HOME/bin/standalone.sh
        For Windows: EAP_HOME\bin\standalone.bat

Configure the JBoss DataGrid
-------------------------

1. Open file JDG_HOME/standalone/configuration/standalone.xml.
2. Add the following lines below ```<cache-container name="local" default-cache="default" statistics="true">``` tag:

```
    <local-cache name="members" start="EAGER">
        <indexing index="LOCAL">
            <property name="default.directory_provider">ram</property>
            <property name="default.indexmanager">near-real-time</property>
        </indexing>
        <locking isolation="NONE" acquire-timeout="30000" concurrency-level="1000" striping="false"/>
        <transaction mode="NONE"/>
    </local-cache>
```

3. Save changes.


Start the JBoss DataGrid
-------------------------

1. Open a command prompt and navigate to the root of the JBoss DataGrid directory.
2. The following shows the command line to start the server:

        For Linux:   JDG_HOME/bin/standalone.sh -Djboss.socket.binding.port-offset=100
        For Windows: JDG_HOME\bin\standalone.bat -Djboss.socket.binding.port-offset=100

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/BUILD_AND_DEPLOY.md#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this application.
3. Type this command to build and deploy the archive:

        mvn clean install jboss-as:deploy

4. This will deploy `target/datagrid-demo-app.war` to the running instance of the server.
 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/datagrid-demo-app/>.

Using the application 
---------------------

Create Members: This button populate JBoss Data Grid with 600,000 members. Every member name will be setup as "Name"+member id.
Example: Member with id 3000 will have a name "Name3000".

Add Member: Display a form to create a new member. The id must nos exist.

Count Members: Display how many members are registered on JBoss Data Grid.

This demo allow to search members by name on real time (For every key press action on search field). 
This use case is often slow if one query is executed to a database on key pressed action. We show how performance is increased by searching by quering to JBoss Data Grid.

Delete icon on every row: This icon will delete selected member from JBoss Data Grid
    

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this application.
3. When you are finished testing, type this command to undeploy the archive:

        mvn jboss-as:undeploy
   
