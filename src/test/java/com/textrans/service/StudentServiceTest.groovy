package com.textrans.service

import spock.lang.Specification


class StudentServiceTest extends Specification {

    def "test mock getStudentList"() {
        given:
        StudentDao dao = new StudentDao()
        StudentService service = new StudentService(studentDao: dao)
        and:
        dao.getStudentList([1]) >> [new User(gender: 1), new User(gender: 1)]
        dao.getStudentList([2]) >> [new User(gender: 2), new User(gender: 2)]
        expect:
        List<User> users = service.getStudentList([type])
        users.stream().allMatch { x -> x.gender == gender }
        where:
        type << [1, 2]
        gender << [1, 2]
    }

    def "test throw exception"() {
        when: "input a null parameter"
        new StudentService().getStudentById(null)
        then: "throw exception"
        def e = thrown(IllegalArgumentException)
        assert e.message == "id can not be null"
    }

    def "mock demo"() {
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


    def "#name login, return #msg "() {
        given:
        def userDao = Mock(StudentDao)
        def studentService = new StudentService(studentDao: userDao)
        when: "user login"
        studentService.login(name, password)

        then: "can not find Mark, Frank Password incorrect"
        userDao.findUserByName("Mark") >> null
        userDao.findUserByName("Frank") >> new User(name: "k1", password: "123")

        then: "throw exception"
        def e = thrown(IllegalArgumentException)
        msg == e.message
        where:
        name    | password || msg
        "Mark"  | null     || "Mark不存在"
        "Frank" | "pwd"    || "Frank密码输入错误"
    }

}
