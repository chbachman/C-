#include <stdio.h>
struct Greeting {
    char * name;
};

struct Temp {
    struct Greeting greeting;
    int id;
};


struct Greeting initGreeting() {
    struct Greeting createdGreeting;
    createdGreeting.name = "Hello, World!";
    return createdGreeting;
}

struct Temp initTemp() {
    struct Temp createdTemp;
    createdTemp.greeting = initGreeting();
    createdTemp.id = 2;
    return createdTemp;
}

int main() {
    struct Temp t = initTemp();
    printf("%s\n", t.greeting.name);
    return 0;
}
