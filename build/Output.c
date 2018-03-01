#include <stdio.h>
#include <stdlib.h>

struct Foo$truct;
typedef struct Foo$truct *Foo;
struct Foo$truct {
  int x;
  int y;
  Foo (*bar)(Foo this);
};

Foo Foo꞉꞉bar(Foo this) {
  printf("%d", this->x + this->y);
  return this;
}

Foo Foo꞉꞉init() {
  Foo this = (Foo)malloc(sizeof(struct Foo$truct));
  this->bar = &Foo꞉꞉bar;
  this->x = 3;
  this->y = this->x + 3;
  return this;
}

void main() {
  Foo foo = Foo꞉꞉init();
  Foo x = foo->bar(foo);
  x->bar(x);
}
