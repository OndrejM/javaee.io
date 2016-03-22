package io.javaee

import spock.lang.Specification

class BeansTest extends Specification {

    def "testing equals"(a, b) {
        expect:
        a == b

        where:
        a                                                                                 | b
        new EntityProject(name: 'name', contributors: [new EntityContributor(name: 'a')]) | new EntityProject(name: 'name', contributors: [new EntityContributor(name: 'a')])
        new EntityProject(name: 'name', contributors: [])                                 | new EntityProject(name: 'name', contributors: [new EntityContributor(name: 'a')])
        new EntityContributor(name: 'a', projects: [new EntityProject(name: 'name')])     | new EntityContributor(name: 'a')
    }

}
