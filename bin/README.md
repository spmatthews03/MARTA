# Executing MTS.jar

### Script Files
* __simple.sh__ - a script file to execute the simple_test.txt scenario
* __marta.sh__ - a script file to execute a simulation from the martadb.  The commands are in the marta.txt file  

### Configuration
In order to faciliate running outside the provided VM, an option to configure connection parameters via a property file is provided.  The property file must be named _marta.props_ and must be located in the same directory that the simulation is executing from.  The file _marta.props.sample_ is provided as a sample.
The following parameters can be set in the _marta.props_ file:
* __user__ - sets the user for the DB connection
* __password__ - sets the password for the DB connection
* __ssl__ - allows to set (true) or disable(false) the use of SSL in the DB connection
* __url__ - to specify the DB connection URL



