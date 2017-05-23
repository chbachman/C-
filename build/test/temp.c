#include <stdio.h>

struct Animal initAnimal ();

struct Animal initAnimal () {struct Animal createdAnimal;
createdAnimal.name = "Cow";
createdAnimal.age = 4;
return createdAnimal;
}

int main () {
