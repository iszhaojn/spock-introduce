## 1. 介绍

### 为什么选择Spock?

Spock是一个`Java`和`Groovy`
应用的测试和规范框架。之所以能够在众多测试框架中脱颖而出，是因为它优美而富有表现力的规范语言。Spock的灵感来自JUnit、RSpec、jMock、Mockito、Groovy、Scala、Vulcans。

Spock主要特点如下：

- 基于Groovy这种JVM平台的动态语言,可以很方便嵌入现有java项目,提供了很多语法糖,让单元测试代码更简洁
- 遵从[BDD](https://en.wikipedia.org/wiki/Behavior-driven_development)（行为驱动开发）模式，有助于提升测试代码的可读性和代码质量
- 底层通过junit runner调用测试，兼容绝大部分junit的运行场景
- 自带Mock功能

### 什么是BDD?

```
BDD（行为驱动开发，Behavior Driven Development）是一种敏捷软件开发方法，旨在通过与业务利益相关者和开发团队之间的有效沟通和协作来确保软件的开发满足业务需求。核心做法是将需求用自然语言描述，形成易于理解的场景，并将这些场景转化为可自动化的测试
```

### 关于Groovy

```
Groovy是Java平台上设计的面向对象编程语言。这门动态语言拥有类似Python、Ruby和Smalltalk中的一些特性，可以作为Java平台的脚本语言使用。

Groovy的语法与Java非常相似，以至于多数的Java代码也是正确的Groovy代码。Groovy代码动态的被编译器转换成Java字节码。由于其运行在JVM上的特性，Groovy可以使用其他Java语言编写的库。
```

## 2. 如何开始

##### Gradle

```groovy
testImplementation "org.spockframework:spock-core:2.1-groovy-3.0"
```

##### Maven

```xml

<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-core</artifactId>
    <version>2.4-M4-groovy-4.0</version>
    <scope>test</scope>
</dependency>

<dependency>
<groupId>org.apache.groovy</groupId>
<artifactId>groovy-all</artifactId>
<version>4.0.22</version>
<type>pom</type>
<scope>test</scope>
</dependency>
```

##### 基本规范

```groovy
class MyFirstSpecification extends Specification {
    // fields
    def obj = new ClassUnderSpecification()
    @Shared
            res = new VeryExpensiveResource()

    // fixture methods
    def setupSpec() {} // runs once - before the first feature method
    def setup() {} // runs before every feature method
    def cleanup() {} // runs after every feature method
    def cleanupSpec() {} // runs once - after the last feature method

    // feature methods
    def "pushing an element on the stack"() {

        //blocks go here  `given` 、 `when` 、 `then` 、 `expect` 、 `cleanup` 和 `where` 块
        given: "generate a user"
        User user = new User()

        when: "set birthday for user"
        user.setBirthday(LocalDate.of(2000, 1, 1))

        then: "verify user's age"
        user.getAge() == 24

    }

    // helper methods
    void matchesPreferredConfiguration(pc) {
        assert pc.vendor == "Sunny"
        assert pc.clockRate >= 2333
        assert pc.ram >= 4096
        assert pc.os == "Linux"
    }

```

## 3. 一些特性介绍和样例

##### 3.1 什么是 block?

- `given`：输入条件（前置参数），在测试方法之前执行的代码
- `when`：执行行为（`Mock`接口、真实调用）
- `then`：输出条件（验证结果）
- `and`：衔接上个标签，补充的作用
- `expect`: 用于期望特定的结果，通常与 `then` 互换使用
- `where`： 用于参数化测试，提供测试数据

| @Timeout      | 为方法设置超时时间                                                                                               |
|---------------|---------------------------------------------------------------------------------------------------------|
| `@Ignore`     | 忽略任何带有此注释的特征方法。                                                                                         |
| `@IgnoreRest` | 任何带有此注解的特征方法都将被执行，其他方法将被忽略。对于快速运行单个方法非常有用。                                                              |
| `@FailsWith`  | 期望功能方法突然完成。`@FailsWith` 有两个用例：1. 记录无法立即解决的已知错误。2. 在某些无法使用异常条件的情况下替代异常条件（如指定异常条件的行为）。在所有其他情况下，异常条件都是可取的。 |

##### 3.2 Mock 和 Exception

> _mock_ 返回一个所有方法都为空值的对象，当我们对一个对象部分方法进行 mock，但其余方法希望基于真实对象逻辑时可采用 Spy()。
> _spy_ 基于一个真实的对象，只有被 Mock 的方法会按指定值返回，其余方法行为与真实对象一致。
> _stub_ 与 mock 的区别在于它只有 mock 的方法返回对应值，未 mock 的会返回无意义的 Stub 对象本身，另外 mock、spy 对象能统计调用次数。

```groovy
    def "mock"() {
    given:
    def mockPerson = Mock(User)
    def spyPerson = Spy(User)
    def stubPerson = Stub(User)
    mockPerson.getName() >> "bob"
    spyPerson.getName() >> "bob"
    stubPerson.getName() >> "bob"
    expect:
    mockPerson.getName() == "bob"
    spyPerson.getName() == "bob"
    stubPerson.getName() == "bob"
    mockPerson.getAge() == null
    spyPerson.getAge() == 18
    stubPerson.getAge() != 18 && stubPerson.getAge() != null
}

```

```groovy
// _ 表示匹配任意类型参数
List<StudentDTO> students = studentDao.getStudentInfo(_)
// 如果有同名的方法，使用as指定参数类型区分
List<StudentDTO> students = studentDao.getStudentInfo(_ as String)

//单次返回
studentDao.getStudentInfo([1]) >> [student1]
studentDao.getStudentInfo([2]) >> [student2]

//多次返回值, 第一次调用返回[student1,student2],第二次返回[student3,student4] ...
studentDao.getStudentInfo() >>> [[student1, student2], [student3, student4], [student5, student6]]
```

```groovy
def "test throw exception"() {
    when: "input a null parameter"
    new StudentService().getStudentById(null)
    then: "throw exception"
    def e = thrown(IllegalArgumentException)
    assert e.message == "id can not be null"
}

def "test not throw exception"() {
    when: "input a null parameter"
    new StudentService().getStudentById(1)
    then: "throw exception"
    notThrown(IllegalArgumentException)
}
```

```groovy
@Shared sql = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")

def "maximum of two numbers"() {
    expect:
    Math.max(a, b) == c
    where:
    [a, b, c] << sql.rows("select a, b, c from maxdata")
}
```

```groovy
...
where:
[a, b, _, c] << sql.rows("select * from maxdata")
```

##### 3.3 与Spring集成

```xml

<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-spring</artifactId>
    <scope>test</scope>
</dependency>
```

```groovy
class UserServiceUnitTest extends Specification

  {

       
    UserService userService = new UserService()
       
    UserDao userDao = Mock(UserDao)

        def setup() {
                userService.userDao = userDao
           
    }

        def "test login with success"() {
                when:
                userService.login("k", "p")
               
        then:
                1 * userDao.findByName("k") >> new User("k", 12, "p")
           
    }

        def "test login with error"() {

                given:
               
        def name = "k"
               
        def passwd = "p"
               
               
        when:
                userService.login(name, passwd)
               
               
        then:
                1 * userDao.findByName(name) >> null
               
               
        then:
               
        def e = thrown(RuntimeException)
                e.message == "${name}不存在"
           
    }

        @Unroll
        def "test login with "() {
                when:
                userService.login(name, passwd)
               
               
        then:
                userDao.findByName("k") >> null
                userDao.findByName("k1") >> new User("k1", 12, "p")
               
               
        then:
               
        def e = thrown(RuntimeException)
                e.message == errMsg
               
               
        where:
                name         |   passwd   |   errMsg
                "k"         |   "k"     |   "${name}不存在"
                "k1"         |   "p1"     |   "${name}密码输入错误"
           
    }

}
```

##### 3.4 更多资料

1. [Spock Web Console](https://spockframework.org/spock/docs/2.3/getting_started.html#_spock_web_console)
2. [Spock Example Project](https://github.com/spockframework/spock-example)
3. [Spock Framework Reference Documentation](https://spockframework.org/spock/docs/2.3/all_in_one.html)
4. [Spock系列博客](https://javakk.com/category/spock)