# Executing MTS.jar

###Script Files
* simple.sh - a script file to execute the simple_test.txt scenario
* marta.sh - a script file to execute a simulation from the martadb.  The commands are in the marta.txt file  

###Configuration
In order to faciliate running outside the provided VM, an option to configure connection parameters via a property file is provided.  The property file must be named _marta.props_ and must be located in the same directory that the simulation is executing from.  The file _marta.props.sample_ is provided as a sample.
The following parameters can be set in the _marta.props_ file:
* user - sets the user for the DB connection
* password - sets the password for the DB connection
* ssl - allows to set (true) or disable(false) the use of SSL in the DB connection
* url - to specify the DB connection URL



