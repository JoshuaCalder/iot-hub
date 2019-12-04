# iot-hub
An Internet of Things Hub application that simulates an IOT network interface. Created for SENG330

This application mimicks an Internet of Things Controller App. Upon startup,
the user is brought to a welcome page which then leads to a login page. Once
identified and verified, the user is shown the Hub page where it is possible
to add smart devices to the hub and track their status as well as configure
them.

*******************************************************************************
Running the program: To run this program, the user must go to the folder
                     "complete" and run the command "Gradle bootRun" in the
                     command line. Next, the user can navigate how he/she sees
                     fit and must use the login credentials username: user,
                     password: password to acces further screens.
                     Sequence of events:
                     1) $ cd complete
                     2) $ Gradle BootRun (Executing should stop at around 75%. This is supposed to happen)
                     3) Open up web browser and visit localhost:8080 (or whatever host:port combo you specified) 
                     4) Click 'get started'
                     5) Create a new user account
                     5) Go back to login page and sign in with newly created account
                     6) You're good to go
*******************************************************************************
