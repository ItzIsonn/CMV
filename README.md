# CMV Programming Language
Язык представляет собой смесь C++ и Python, лучшие аспекты каждого из этих языков. С примерами кода вы можете ознакомится [здесь](#примеры-кода)

## Преимущества: 
- открытый исходный код
- понятный синтаксис
- поддержка от разработчика





# Использование
Язык написан на Java, поэтому у вас должна быть установлена минимум [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
Для запуска кода, необходимо выполнить команду: `???`





# Синтаксис

## Правильное наименование и присвоение значений
- Имя может содержать только английские буквы и нижние подчеркивания
- Значение может быть:
  - числовым - целые и дробные числа
  - логическим - true или false
  - буквенным - "текст"
  - выражением - математические операции (с возможностью использования переменных)

> [!IMPORTANT]
> Эти правила применяются для всех имён и значений без исключений



## Переменные

### Создание
`var NAME = VALUE`

> [!CAUTION]
> Имя не должно использоваться другими переменными


### Переопределение
`NAME = VALUE`

> [!CAUTION]
> Переменная с таким именем уже должна существовать

> [!NOTE]
> Значение не обязательно должно быть того же типа, используемого переменной до переопределения


### Использование
> [!NOTE]
> Чтобы использовать значения переменной, надо обратиться к ней по имени



## Функции

### Создание
```
function NAME(PARAMS) {
  BODY
}
```

> [!IMPORTANT]
> Параметры определяются так: `var NAME`, перечисляются через запятую и не должны повторятся именами с другими параметрами

> [!CAUTION]
> Значения параметров можно использовать в теле функции (через имя), но не за её пределами

> [!NOTE]
> Если необходимости в параметрах нет, то можно оставить скобочки пустыми

> [!NOTE]
> `BODY` - тело функции, в нём могут использоваться все возможности языка, кроме создания функций
> Переменные, созданные в функции, могут использоваться только внутри неё и имеют больший приоритет перед глобальными


### Вызов
`NAME(PARAMS)`

> [!CAUTION]
> Вызываемая функция уже должна существовать

> [!IMPORTANT]
> Число параметров должно соответствовать их числу при создании функции


### Уже созданные функции
- `print(VALUE)` - 1 параметр - выводит переданный параметр в консоль
- `println(VALUE)` - 1 параметр - выводит переданный параметр в консоль c переходом после на следующую строку



# Примеры кода
