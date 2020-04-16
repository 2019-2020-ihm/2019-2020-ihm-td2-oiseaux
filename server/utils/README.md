# TD2 Oiseaux : Utility programs
## IOC Parser
### Description
This program uses the [IOC World Bird List - Master list](https://www.worldbirdnames.org/ioc-lists/master-list-2/) (converted to CSV format) and the [IOC World Bird List - Multilingual list](https://www.worldbirdnames.org/ioc-lists/master-list-2/) (converted to CSV format) to gather all possible information about bird species and to generate all the SQL requests to fill the `species` table.

### Requirements :
- Python 3+
- PyMySQL

### Run
- Download the [IOC World Bird List - Master list](https://www.worldbirdnames.org/ioc-lists/master-list-2/).
- Convert the Master list to CSV format using an office suite and rename it to `ioc.csv`.
- Download the [IOC World Bird List - Multilingual list](https://www.worldbirdnames.org/ioc-lists/master-list-2/).
- Convert the Master list to CSV format using an office suite and rename it to `ioc-multilingual.csv`.
- Run the program :
```
python3 ioc-parse.py
```
