# [Pastebin](https://www.pastebin.com) Latest-Files Collector

## Summary
This Java project downloads the latest files of Pastebin (from [here](https://www.pastebin.com/archive)) as '.txt' files. It includes a multi-threaded version (**x4 times faster**) and a sub-program to find words in the documents.

## Important files
- **./time_spent.xlsx**
  - Chart that shows the difference between single and multithreaded versions.
- **./criteria.txt**
  - Includes the words to be found in the files. They must be separated by line breaks and it is not case-sensitive. 
  Example:
     ```
      test
      Test
      word
      WoRd
      ```
      It will try to find `test` and `word` in the files no matter if they have any upper case or not because they will be compared (both criteria-words and file-words) to their lower case versions.
      *Notice that `Test` and `WoRd` are useless because they are converted to their lower case versions.*
- **./src/Main.java**
  - (Single-threaded) Program that will download the latest files continuously. By default, it creates the files in `files/`. 
- **src/MainParallel.java**
  - (Multithreaded) Program that will download the latest files continuously. By default, it creates the files in `files/`. 
- **./src/FindFile.java**
  - Program that will print the files' names that contain any of the words specified in `criteria.txt` and the words found 'as they are' in the file. 
  Example:
  *Given the following file named `HelloTest.txt`:*
    ```text
    Hello world, hello!
    ```
    *And `criteria.txt` as:*
      ```
      hello
      world
      ```
    *The output will be:*
      ```
      Number of files checked: 1
      Files that met the criteria: 1
        HelloTest.txt
          Hello
          world
          hello
      ```
