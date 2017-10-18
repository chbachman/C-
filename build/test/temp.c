#include <stdio.h>

struct Animal {
  char *name;
  int age;
};

struct Animal initAnimal();

struct Animal initAnimal() {
  struct Animal createdAnimal;
  createdAnimal.name = "Cow";
  createdAnimal.age = 4;
  return createdAnimal;
}

int main() {
  struct Animal cow = initAnimal();
  printf("%s\n", cow.name);
  printf("%d\n", cow.age);
  return 0;
}
