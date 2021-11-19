# Motivation
    As connecting to the CSE server is so complicated... and the user interface is not even usable.

# What to change and what not to change?
    - Can change:
        1. MYSQL_USER in docker-compose.yml (default: Group42)
        2. MYSQL_PASSWORD in docker-compose.yml (default: physicsisawesome)
        3. MYSQL_DATABASE in docker-compose.yml (default: Project)
        4. Appending the files in "volumes" in docker-compose.yml
            - Let say you make a new file called "hello.java" and you want yourself be able to use it inside the container.
            - Append one line with: - "./hello.java:/home/Project/hello.java"
        5. left hand side of "ports" in the phpmyadmin:
            - (I know it should be less likely to happen) if your localhost has anything that uses port 7999 already, change it to anything like 13231 or 8081 (you like it!)
    
    - Cannot change:
        - Any other thing I haven't mentioned.


# How to use?
    1. Firstly, in your ubuntu run 
        - "sed 's/#.*//' requirements.txt | xargs sudo apt-get install -y"

    2. Run 
        - "make build" 
        it takes around 15 minutes (only the first time, later on you don't need to do this)

    3. start the container (it should start whenever you boot your computer)
        - "make start"

    4. Whenever you need to enter to the mock-up server to run your code, run
        - "make enterSQL"
    
    5. Caution!!! Very unrecommended method:
        Whenever you need to stop the container due to some bugs, run
        - "make _remove"
        It is very unrecommended as the data you inserted into the database will not be found.

    6. To enter the mysql using the command line (if you want to make your life harder), run:
        - "mysql --host=projgw --port=2633 -u Group42 -p"
        and enter your password, but I don't recommend this method as you can have a better experience using phpmyadmin.

# What's more?
    If you want to check your database situation, you can go to "http://localhost:7999" and enter your username and password. (If you have changed the "ports" in the docker-compose.yml please change your url to corresponding port.)


