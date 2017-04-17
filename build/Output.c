struct Temp {
    int name;
    char * temp;
    char * temp2;
};

void temp();

void temp() {
    char * name;
    name = "Hello!";
    printf("%s\n", name);
}

struct Temp initTemp() {
    struct Temp createdTemp;
    return createdTemp;
}

int main() {
    for (int index = 1; index <= 100; index++) {
        printf("%d\n", index);
        if (index % 5 == 0) {
            printf("%s\n", "Fizz");
        }
        if (index % 3 == 0) {
            printf("%s\n", "Buzz");
        }
    }
    return 0;
}
