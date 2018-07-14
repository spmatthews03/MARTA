# MTS Application
This project is seeded from the code from Assignment 2

The only modification from the original code is the addition of support for a local property file that can change the default connection settings for the database connection.  This was done in order to facilitate running out of the VM.  The VM can be extremely slow.

The code is added in class FileProps. The uploadMARTAData method of SimDriver was modified to check for the presence of the propertied file and use these settings if they are present in the file.

The file should be called __marta.props__ and needs to be placed in the directory where the executable is executed from.  See [Project bin folder](https://github.gatech.edu/msmith606/CS6310-summer-2018-A7-8/tree/master/bin) for additional information.



