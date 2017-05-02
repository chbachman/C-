#include <stdio.h>

int temp();

int temp() { return 2 + 2; }

int main() {
  for (int index = 1; index <= 100; index++) {
    printf("%d\n", index);
  }
  int t = 2 + 1;
  printf("%d\n", t);
  return 0;
}
