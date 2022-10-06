/*********************************************************************
 *
 * Copyright (C) 2020-2021 David C. Harrison. All right reserved.
 *
 * You may not use, distribute, publish, or modify this code without 
 * the express written permission of the copyright holder.
 *
 ***********************************************************************/

#include <stddef.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dirent.h>

/*
 * Extended ASCII box drawing characters:
 * 
 * The following code:
 * 
 * printf("CSE130\n");
 * printf("%s%s Assignments\n", TEE, HOR);
 * printf("%s  %s%s Assignment 1\n", VER, TEE, HOR);
 * printf("%s  %s%s Assignment 2\n", VER, TEE, HOR);
 * printf("%s  %s%s Assignment 3\n", VER, TEE, HOR);
 * printf("%s  %s%s Assignment 4\n", VER, TEE, HOR);
 * printf("%s  %s%s Assignment 5\n", VER, TEE, HOR);
 * printf("%s  %s%s Assignment 6\n", VER, ELB, HOR);
 * printf("%s%s Labs\n", ELB, HOR);
 * printf("   %s%s Lab 1\n", TEE, HOR);
 * printf("   %s%s Lab 2\n", TEE, HOR);
 * printf("   %s%s Lab 3\n", ELB, HOR);
 * printf();
 * 
 * Shows this tree:
 * 
 * CSE130
 * ├─ Assignments
 * │  ├─ Assignment 1
 * │  ├─ Assignment 2
 * │  ├─ Assignment 3
 * │  ├─ Assignment 4
 * │  ├─ Assignment 5
 * │  └─ Assignment 6
 * └─ Labs
 *    ├─ Lab 1
 *    ├─ Lab 2
 *    └─ Lab 3
 */
#define TEE "\u251C"  // ├ 
#define HOR "\u2500"  // ─ 
#define VER "\u2502"  // │
#define ELB "\u2514"  // └

/*
 * Read at most SIZE bytes from FNAME starting at FOFFSET into BUF starting 
 * at BOFFSET.
 *
 * RETURN number of bytes read from FNAME into BUFF, -1 on error.
 */
int fileman_read(
    const char *fname, 
    const size_t foffset, 
    const char *buf, 
    const size_t boffset, 
    const size_t size) 
{
  int openFile = open(fname, O_RDONLY);
  if(openFile == -1)
    return -1;
  else
    lseek(openFile, foffset, SEEK_CUR);
  return read(openFile, (void*)(buf+boffset), size);
}

/*
 * Create FILE and Write SIZE bytes from BUF starting at BOFFSET into FILE 
 * starting at FOFFSET.
 * 
 * RETURN number of bytes from BUFF written to FNAME, -1 on error or if FILE 
 * already exists
 */
int fileman_write(
    const char *fname, 
    const size_t foffset, 
    const char *buf, 
    const size_t boffset, 
    const size_t size) 
{
  int openFile = open(fname, O_WRONLY, S_IRUSR);
  if(openFile == -1){
    openFile = creat(fname, S_IRUSR);
    lseek(openFile, foffset, SEEK_SET);
  } else{
    close(openFile);
    return -1;
  }
  int writeBytes = write(openFile, (void*)(buf+boffset), size);
  close(openFile);
  return writeBytes;
}

/*
 * Append SIZE bytes from BUF to existing FILE.
 * 
 * RETURN number of bytes from BUFF appended to FNAME, -1 on error or if FILE 
 * does not exist
 */
int fileman_append(const char *fname, const char *buf, const size_t size) 
{
  int openFile = open(fname, O_APPEND|O_WRONLY, S_IRUSR);
  if(openFile == -1){
    return -1;
  } else{
    int writeBytes = write(openFile, (void*)buf, size);
    close(openFile);
    return writeBytes;
  }
}

/*
 * Copy existing file FSRC to new file FDEST.
 *
 * Do not assume anything about the size of FSRC. 
 * 
 * RETURN number of bytes from FSRC written to FDEST, -1 on error, or if FSRC 
 * does not exists, or if SDEST already exists.
 */
int fileman_copy(const char *fsrc, const char *fdest) 
{
  // Allocate
  char buf[1024];
  int openFSRC = open(fsrc, O_RDWR);
  int openFDEST = open(fdest, O_WRONLY|O_CREAT, 0x777);
  int readData = 0;
  int writeData = 0;
  int bytesCopy = 0;
  if(openFSRC == -1 || openFDEST == -1){
    return -1;
  }
  do{
    readData = read(openFSRC, (void *)buf, sizeof(buf));
    writeData = write(openFDEST, (void *)buf, readData);
    bytesCopy += writeData;
  }
  while(readData == sizeof(buf));
  return bytesCopy;
}

/*
 * Print a hierachival directory view starting at DNAME to file descriptor FD 
 * as shown in an example below where DNAME == 'data.dir'
 *
 *   data.dir
 *       blbcbuvjjko
 *           lgvoz
 *               jfwbv
 *                   jqlbbb
 *                   yfgwpvax
 *           tcx
 *               jbjfwbv
 *                   demvlgq
 *                   us
 *               zss
 *                   jfwbv
 *                       ahfamnz
 *       vkhqmgwsgd
 *           agmugje
 *               surxeb
 *                   dyjxfseur
 *                   wy
 *           tcx
 */
//Make helper; dir or file
//Stat can help
void get_dir(const int fd, const char *dname, int indent){
  for(int j = 0; j < indent; j++){
    dprintf(fd, " ");
  }
  dprintf(fd, "%s\n", dname);
  
  struct dirent **dirList;
  chdir(dname);
  int scanData;
  scanData = scandir(".", &dirList, NULL, alphasort);
  //Handle infinite loop
  for(int i = 0; i < scanData; i++){
    if(strcmp(dirList[i]->d_name, "..") == 0 || strcmp(dirList[i]->d_name, ".") == 0){
      free(dirList[i]);
      continue;
    }
    if(dirList[i]->d_type == DT_DIR){
      get_dir(fd, dirList[i]->d_name, indent+4);
    }
    else{
      for(int j = 0; j < indent+4; j++){
        dprintf(fd, " ");
      }
      dprintf(fd, "%s\n", dirList[i]->d_name);
    }
    free(dirList[i]);
  }
  free(dirList);
  chdir("..");
}
void fileman_dir(const int fd, const char *dname) 
{
  get_dir(fd, dname, 0);
}

/*
 * Print a hierachival directory tree view starting at DNAME to file descriptor 
 * FD as shown in an example below where DNAME == 'data.dir'.
 * 
 * Use the extended ASCII box drawing characters TEE, HOR, VER, and ELB.
 *
 *   data.dir
 *   ├── blbcbuvjjko
 *   │   ├── lgvoz
 *   │   │   └── jfwbv
 *   │   │       ├── jqlbbb
 *   │   │       └── yfgwpvax
 *   │   └── tcx
 *   │       ├── jbjfwbv
 *   │       │   ├── demvlgq
 *   │       │   └── us
 *   │       └── zss
 *   │           └── jfwbv
 *   │               └── ahfamnz
 *   └── vkhqmgwsgd
 *       ├── agmugje
 *       │   └── surxeb
 *       │       ├── dyjxfseur
 *       │       └── wy
 *       └── tcx
 */
void fileman_tree(const int fd, const char *dname) 
{
}
