#include <stdio.h>
#include <stdlib.h>

struct String$truct {
  int buffer;
  int length;
};
typedef struct String$truct *String;
String String꞉꞉init() {
  String this = (String)malloc(sizeof(struct String$truct));
  this->buffer = 56;
  this->length = 24;
  this->length = 45;
  return this;
}
void String꞉꞉append(String s) {}

int main() { String temp = String꞉꞉init(); }
