#include <stdio.h>

struct Str {
  char *name;
};
struct Str string$initStr(char *str) {
  struct Str createdStr;
  createdStr.name = str;
  return createdStr;
}

struct Temp {
  int id;
  char *name;
};
struct Temp initTemp() {
  struct Temp createdTemp;
  createdTemp.id = 2;
  createdTemp.name = "Hello";
  return createdTemp;
}

int main() {
  struct Temp t = initTemp();
  printf("%d\n", t.id);
  printf("%s\n", t.name);
  return 0;
}
