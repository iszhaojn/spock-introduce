package com.textrans.service

import groovy.sql.Sql
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Timeout

import java.time.LocalDate
import java.util.concurrent.TimeUnit

/**
 * 1. Given-When-Then
 * 2. where block
 */
class BasicGrammar extends Specification {

    def "given-when-then"() {
        given: "generate a user"
        User user = new User()

        when: "set birthday for user"
        user.setBirthday(LocalDate.of(2000, 1, 1))

        then: "verify user's age"
        user.getAge() == 24
    }

    def "where block"() {
        given: "generate a user with birthday"
        User user = new User()
        user.setBirthday(LocalDate.of(y, m, d))

        expect: "verify user's age"
        user.getAge() == age

        where: "check data in dataset"
        y    | m | d || age
        1999 | 1 | 1 || 25
        2000 | 1 | 1 || 24
        2001 | 1 | 2 || 23
    }

    def "maximum of two numbers"() {
        expect:
        Math.max(a, b) == c
        Math.max(d, e) == f

        where:
        a | b || c
        1 | 3 || 3
        7 | 4 || 7
        0 | 0 || 0

        d; e; f
        1; 3; 3
        7; 4; 7
        0; 0; 0
    }


    def "helper method"() {
        when:
        PersonalComputer pc = new PersonalComputer(cpu: "i5", vendor: "Sunny PC Store", ram: 4096, os: "Linux")
        then:
        matchesPreferredConfiguration(pc)
    }


    void matchesPreferredConfiguration(pc) {
        assert pc.cpu == "i7"
        assert pc.ram >= 4096
        assert pc.os == "Linux"
    }

    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    def "test Timeout"() {
        when: "sleep 0.5s"
        Thread.sleep(500)
        then: "check the result"
        true
    }

    def "test"() {
        List<String> list = new ArrayList<>()
        list.add("a")
        list.add("b")
        expect:
        list.stream().anyMatch(s -> s == "a")
    }

    @Shared
            db = Sql.newInstance("jdbc:h2:mem:", "org.h2.Driver")


    def "test2"() {
        def list = ['a', 'b', 'c']
        def user = new User(name: 'a', type: 1, gender: 1)
        def id = 1
        def sql = """
        select * from t_user where id=${id}
            and name='${user.name}'
        """
        db.rows(sql)
        expect:
        list.stream().anyMatch(s -> s == "a")
    }


    def "test sql"() {
        given:
        db.execute("create table t_user (a int, b int, c int)")
        db.execute("insert into t_user values (1, 3, 3)")

        expect:
        [a, b, c] << db.rows("select a, b, c from t_user")

        where:
        a | b | c || _
        1 | 3 | 3 || _

    }


}