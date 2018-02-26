#include <stdio.h>
#include <stdlib.h>

struct String$truct;
typedef struct String$truct *String;
struct String$truct {
  int buffer;
  int length;
  void (*append)(String this, String s);
};

void String꞉꞉append(String this, String s) {
  this->length = this->length + s->length;
}

String String꞉꞉init() {
  String this = (String)malloc(sizeof(struct String$truct));
  this->append = &String꞉꞉append;
  this->length = 24;
  this->buffer = this->length;
  return this;
}

int main() {
  String temp = String꞉꞉init();
  String temp2 = String꞉꞉init();
  temp->append(temp, temp2);
}
