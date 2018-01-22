#include <stdio.h>

void print(char *x) {}
void print$Int(int x) {}
int main(int argc, char **argv) {
  print("Hello, World!");
  print$Int(argc);
}
