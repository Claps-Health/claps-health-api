# claps-health-api

This is a web API application based on the Play framework and written in Java, developed by Claps Health.


## Prerequisites

Before you can run this application, you will need the following:

- Java 8 or higher
- Sbt 1.3.4 or higher
- MySQL/MariaDB (required for storing data in the application)

## Installation

To install and configure the application, follow these steps:

1. Clone this repository to your local machine
2. Open the conf/twitter.conf file in a text editor, and replace the value of twitter.apiKey with your own API key
3. Configure the SR25519 library in Linux:
   - Execute `nano /etc/environment` in the terminal.
   - Add the following line to the end of the file: `SUBSTRATE_CLIENT_CRYPTO_LIB_PATH=$your_library_path` (e.g. `mylib`).
   - Copy the `lib/jni/glibc2.x/libjni_crypto.so` file to `$your_library_path`.
4. Configure the database username and password in `conf/db_config_mysql.conf`.
5. Create the MySQL database `claps_health`.
6. Execute the SQL script in `conf/schema.sql` to build the db schema.

## Getting Started

To start the application, follow these steps:

1. Navigate to the project directory
2. Run `sbt run` to start the application with `http://localhost:9000`


## API Documentation

To use this API service, follow these steps:

1. Open a web browser and go to `http://localhost:9000/docs/` to view the API documentation.
2. To disable the API documentation, comment out the line `play.modules.enabled += "play.modules.swagger.SwaggerModule"` in `conf/application.conf`.


## Contributing

If you would like to contribute to this project, please follow these guidelines:

1. Fork the repository
2. Create a new branch for your changes
3. Make your changes and commit them
4. Push your changes to your forked repository
5. Create a pull request to merge your changes into the main repository

## Attribution

This project includes code and other assets from the following open-source repositories:

- [substrate-client-java](https://github.com/strategyobject/substrate-client-java)
    - Provides a Java API and components that include the SR25519 JNI library.

- [play-java-jwt](https://github.com/franzgranlund/play-java-jwt)
    - JWT validation implemented by JAVA in play
    

All code and assets from these repositories are used under their respective licenses. See the `LICENSE` file in each repository for details.

## License

This project is licensed under the Apache 2.0 License. See the `LICENSE` file for details.
This is just a basic example, but you can add more information to the README as needed. Remember to use proper Markdown syntax to format your content.







